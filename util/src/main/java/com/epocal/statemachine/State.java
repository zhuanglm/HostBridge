package com.epocal.statemachine;

import com.epocal.util.RefWrappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dning on 5/15/2017.
 */

public class State<P> implements IStateMachineState<P> {

    private StateType mStateType;

    public StateType getStateType() {
        return mStateType;
    }

    public void setStateType(StateType stateType) {
        mStateType = stateType;
    }

    private IStateEnum mStateID;

    public IStateEnum getStateID() {
        return mStateID;
    }

    public void setStateID(IStateEnum stateID) {
        mStateID = stateID;
    }

    private String mStateName;

    public String getStateName() {
        return mStateName;
    }

    public void setStateName(String stateName) {
        mStateName = stateName;
    }

    private IState mParentState;

    public IState getParentState() {
        return mParentState;
    }

    public void setParentState(IState parentState) {
        mParentState = parentState;
    }

    private Map<IEventEnum, ITransition> mTransitionCollection;
    private List<IState> mStateCollection;

    private IState mInitialState;
    private IState mCurrentState;

    private IState mErrorState;

    private Object mMutex = new Object();

    public State() {
        mStateType = StateType.Normal;
    }

    public void addTransition(IEventEnum eventID, ITransition transition) {
        if (mTransitionCollection == null) {
            mTransitionCollection = new HashMap<IEventEnum, ITransition>();
        }
        mTransitionCollection.put(eventID, transition);
    }

    public void removeTransition(IEventEnum eventID, ITransition transition) {
        if (mTransitionCollection != null && mTransitionCollection.containsKey(eventID)) {
            mTransitionCollection.remove(eventID);
        }
    }

    /**
     * to add child state to child state list
     * @param childState
     */
    public void addState(IState childState) {
        if (mStateCollection == null) {
            mStateCollection = new ArrayList<IState>();
        }
        if (!mStateCollection.contains(childState)) {
            mStateCollection.add(childState);
        }
    }

    public void removeState(IState childState) {
        if (mStateCollection != null && mStateCollection.contains(childState)) {
            mStateCollection.remove(childState);
        }
    }

    public void setInitialState(IState state) {
        if (state == null && mStateCollection != null && mStateCollection.size() > 0) {
            mInitialState = mStateCollection.get(0);
        } else {
            mInitialState = state;
        }
    }

    public void setInitialState(IStateEnum stateID) {
        setInitialState(findChildByID(stateID));
    }

    public IState findChildByID(IStateEnum id) {
        for (IState state : mStateCollection) {
            if (state.getStateID() == id) {
                return state;
            }
        }
        return null;
    }

    public void setCurrentState(IState state) {
        mCurrentState = state;
    }

    public void setErrorState(IState state) {
        mErrorState = state;
    }

    /**
     * to distrubite event to corresponding state which will handle this event.
     * @param stateMachine
     * @param stateDataObject
     * @return
     */
    public TransitionHandled eventPreDistributor(IStateMachine<P> stateMachine, P stateDataObject) {
        IEventEnum handledEventID = null;
        RefWrappers<IEventEnum> iByRef = new RefWrappers<IEventEnum>(handledEventID);
        return eventProcessor(stateMachine, stateDataObject, iByRef);
    }

    /**
     * to find corresponding state to process event which is coming from outside state machine
     * @param stateMachine
     * @param stateDataObject
     * @param handledEventID
     * @return
     */
    public TransitionHandled eventProcessor(IStateMachine<P> stateMachine, P stateDataObject, RefWrappers<IEventEnum> handledEventID) {
        if (mCurrentState != null) {
            TransitionHandled ret = ((IStateMachineState<P>) mCurrentState).eventProcessor(stateMachine, stateDataObject, handledEventID);
            if (ret != TransitionHandled.Handled) {
                IEventEnum eventID = null;
                if (handledEventID == null) {
                    eventID = onEventPreHandle(stateDataObject);
                } else {
                    eventID = handledEventID.getRef();
                }
                if ((mTransitionCollection != null) && mTransitionCollection.containsKey(eventID) && mTransitionCollection.get(eventID).isTransitable(eventID)) {
                    if (((ret == TransitionHandled.UnhandledByFinal || ret == TransitionHandled.UnhandledBySingleton) && mTransitionCollection.get(eventID).getTransitionType() == TransitionType.Normal)
                            || (mTransitionCollection.get(eventID).getTransitionType() == TransitionType.Interrupt || mTransitionCollection.get(eventID).getTransitionType() == TransitionType.Error)) {
                        doTransit(stateMachine, stateDataObject, mTransitionCollection.get(eventID));
                        return TransitionHandled.Handled;
                    } else if (ret == TransitionHandled.UnhandledBySingleton) {
                        if (mTransitionCollection.get(eventID).getTransitionType() == TransitionType.Internal) {
                            doTransitToChild(stateMachine, stateDataObject, mTransitionCollection.get(eventID));
                            return TransitionHandled.Handled;
                        }
                    }
                }
            }
            return ret;
        } else {
            IEventEnum eventID = onEventPreHandle(stateDataObject);
            handledEventID.setRef(eventID);
            if ((eventID != null) && (mTransitionCollection != null) && mTransitionCollection.containsKey(eventID) && mTransitionCollection.get(eventID).isTransitable(eventID)) {
                if (mTransitionCollection.get(eventID).getTransitionType() == TransitionType.Internal) {
                    doTransitToChild(stateMachine, stateDataObject, mTransitionCollection.get(eventID));
                } else {
                    doTransit(stateMachine, stateDataObject, mTransitionCollection.get(eventID));
                }
                return TransitionHandled.Handled;
            }
            if (mStateType == StateType.Normal) {
                return TransitionHandled.UnhandledByNormal;
            } else if (mStateType == StateType.FinalState) {
                return TransitionHandled.UnhandledByFinal;
            } else if (mStateType == StateType.Singleton) {
                return TransitionHandled.UnhandledBySingleton;
            } else {
                return TransitionHandled.UnHandledByRoot;
            }
        }
    }

    private void doTransit(IStateMachine<P> stateMachine, P stateDataObject, ITransition transition) {
        synchronized (mMutex) {
            try {
                invokeExit(stateMachine, stateDataObject);
                stateMachine.doTransition(mStateID, transition.getTargetStateID(), transition.getEventID(), stateDataObject);
                IStateMachineState<P> child = ((IStateMachineState<P>) getParentState().findChildByID(transition.getTargetStateID()));
                child.invokeEntry(stateMachine, stateDataObject);
            } finally {
            }
        }
    }

    private void doTransitToChild(IStateMachine<P> stateMachine, P stateDataObject, ITransition transition) {
        synchronized (mMutex) {
            try {
                if (mCurrentState != null)//current child
                {
                    ((IStateMachineState<P>) mCurrentState).invokeExit(stateMachine, stateDataObject);
                } else {
                    invokeExit(stateMachine, stateDataObject);
                }
                stateMachine.doTransition(mStateID, transition.getTargetStateID(), transition.getEventID(), stateDataObject);
                ((IStateMachineState<P>) findChildByID(transition.getTargetStateID())).invokeEntry(stateMachine, stateDataObject);
            } finally {
            }
        }
    }

    /**
     * to be excuted when system enter this state
     * @param stateMachine
     * @param stateDataObject
     */
    public void invokeEntry(IStateMachine<P> stateMachine, P stateDataObject) {
        if (mParentState != null) {
            mParentState.setCurrentState((IState) this);
        }
        if (mInitialState != null) {
            ((IStateMachineState<P>) mInitialState).invokeEntry(stateMachine, stateDataObject);
        }
        onEntry(stateDataObject);
    }

    /**
     * to be excuted when system leave this state
     * @param stateMachine
     * @param stateDataObject
     */
    public void invokeExit(IStateMachine<P> stateMachine, P stateDataObject) {
        onExit(stateDataObject);
    }

    public void onEntry(P stateDataObject) {
    }

    public void onExit(P stateDataObject) {
    }

    public IEventEnum onEventPreHandle(P stateDataObject) {
        return null;
    }

    public List<IState> getAllChild() {
        return mStateCollection;
    }

    public void resetResource() {
    }

    public void dispose() {
        mParentState = null;
        mInitialState = null;
        mCurrentState = null;
        mErrorState = null;

        if (mTransitionCollection != null) {
            for (Map.Entry<IEventEnum, ITransition> entry : mTransitionCollection.entrySet()) {
                entry.getValue().dispose();
            }

            mTransitionCollection.clear();
            mTransitionCollection = null;
        }

        if (mStateCollection != null) {
            for (IState state : mStateCollection) {
                state.dispose();
            }
            mStateCollection.clear();
            mStateCollection = null;
        }
    }
}

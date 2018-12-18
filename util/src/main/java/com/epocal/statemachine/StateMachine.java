package com.epocal.statemachine;

import java.util.Map;

/**
 * Created by dning on 5/16/2017.
 */
public class StateMachine<P> implements IStateMachine<P> {

    private IState mRootState;

    private Map<IStateEnum, IState> mStateList;

    public Map<IStateEnum, IState> getStateList() {
        return mStateList;
    }

    public void setStateList(Map<IStateEnum, IState> states) {
        mStateList = states;
    }

    public IState addRootState(IState state) {
        state.setStateType(StateType.Root);
        mRootState = state;
        return mRootState;
    }

    public void addState(IState state) {
        if (mRootState == null) {
            mRootState = (IState) new State<P>();
            mRootState.setStateID(StateMachineStateEnum.root);
            mRootState.setStateType(StateType.Root);
        }
        mRootState.addState(state);
    }

    public void start(P stateDataObject) {
        ((IStateMachineState<P>) mRootState).invokeEntry(this, stateDataObject);
    }

    public void stop() {
        cleanUpStateMachine();
    }

    public void doTransition(IStateEnum sourceID, IStateEnum targetID, IEventEnum eventID, P stateDataObject) {
    }

    public void postEvent(P eventObject) {
        try {
            if (mRootState != null) {
                ((IStateMachineState<P>) mRootState).eventPreDistributor(this, eventObject);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cleanUpStateMachine() {
        mRootState.dispose();
        mRootState = null;
    }

    public void resetState() {
        if (mRootState.getAllChild() == null || mRootState.getAllChild().size() == 0) {
            mRootState.resetResource();
            return;
        } else {
            resetState(mRootState);
        }
    }

    private void resetState(IState state) {
        if (state.getAllChild() == null || state.getAllChild().size() == 0) {
            state.resetResource();
            return;
        } else {
            for (int i = 0; i < state.getAllChild().size(); i++) {
                resetState(state.getAllChild().get(i));
            }
            state.resetResource();
            return;
        }
    }
}

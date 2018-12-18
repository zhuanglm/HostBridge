package com.epocal.statemachine;

/**
 * Created by dning on 5/18/2017.
 */

public class Transition implements ITransition {
    private TransitionType mTransitionType;

    public TransitionType getTransitionType() {
        return mTransitionType;
    }

    public void setTransitionType(TransitionType transitionType) {
        mTransitionType = transitionType;
    }

    private IEventEnum mEventID;

    public IEventEnum getEventID() {
        return mEventID;
    }

    public void setEventID(IEventEnum eventID) {
        mEventID = eventID;
    }

    private IStateEnum mTargetStateID;

    public IStateEnum getTargetStateID() {
        return mTargetStateID;
    }

    public void setTargetStateID(IStateEnum stateID) {
        mTargetStateID = stateID;
    }

    private IState mSourceState;

    public void setSourceState(IState state) {
        mSourceState = state;
    }

    public Transition() {
        mTransitionType = TransitionType.Normal;
    }

    public boolean isTransitable(IEventEnum eventID) {
        return true;
    }

    public void onTransition() {
    }

    @Override
    public void dispose() {
        mSourceState = null;
    }
}

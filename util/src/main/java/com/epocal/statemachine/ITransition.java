package com.epocal.statemachine;

/**
 * Created by dning on 5/15/2017.
 */

public interface ITransition {
    TransitionType getTransitionType();

    void setTransitionType(TransitionType transitionType);

    IEventEnum getEventID();

    void setEventID(IEventEnum eventID);

    IStateEnum getTargetStateID();

    void setTargetStateID(IStateEnum stateID);

    void setSourceState(IState state);

    boolean isTransitable(IEventEnum eventID);

    void dispose();
}

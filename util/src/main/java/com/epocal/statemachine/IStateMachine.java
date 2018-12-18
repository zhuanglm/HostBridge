package com.epocal.statemachine;

import java.util.Map;

/**
 * Created by dning on 5/15/2017.
 */

public interface IStateMachine<P> {

    Map<IStateEnum, IState> getStateList();

    void setStateList(Map<IStateEnum, IState> states);

    IState addRootState(IState state);

    void addState(IState state);

    void start(P stateDataObject);

    void stop();

    void doTransition(IStateEnum sourceID, IStateEnum targetID, IEventEnum eventID, P stateDataObject);

    void postEvent(P stateDataObject);

    void resetState();
}
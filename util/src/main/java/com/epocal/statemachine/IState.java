package com.epocal.statemachine;

import java.util.List;

/**
 * Created by dning on 5/15/2017.
 */

public interface IState {
    StateType getStateType();

    void setStateType(StateType stateType);

    IStateEnum getStateID();

    void setStateID(IStateEnum stateID);

    String getStateName();

    void setStateName(String stateName);

    IState getParentState();

    void setParentState(IState parentState);

    void addTransition(IEventEnum eventID, ITransition transition);

    void removeTransition(IEventEnum eventID, ITransition transition);

    void addState(IState childState);

    void removeState(IState childState);

    void setInitialState(IState state);

    void setInitialState(IStateEnum stateID);

    IState findChildByID(IStateEnum stateID);

    List<IState> getAllChild();

    void setCurrentState(IState state);

    void setErrorState(IState state);

    void resetResource();

    void dispose();
}

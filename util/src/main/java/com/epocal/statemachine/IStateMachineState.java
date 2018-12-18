package com.epocal.statemachine;

import com.epocal.util.RefWrappers;

/**
 * Created by dning on 5/15/2017.
 */

public interface IStateMachineState<P> extends IState {
    TransitionHandled eventPreDistributor(IStateMachine<P> stateMachine, P eventDataObject);

    TransitionHandled eventProcessor(IStateMachine<P> stateMachine, P eventDataObject, RefWrappers<IEventEnum> handledEventID);

    void invokeEntry(IStateMachine<P> stateMachine, P stateDataObject);

    void invokeExit(IStateMachine<P> stateMachine, P stateDataObject);
}

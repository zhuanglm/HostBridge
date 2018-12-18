package com.epocal.statemachine;

import com.epocal.statemachine.xmlParser.SimpleStateMachineParser;

import java.util.EnumSet;

/**
 * Created by dning on 5/16/2017.
 */

public class StateMachineFactory {
    public StateMachineFactory() {
    }

    /**
     * instance factory to create state machine
     * @param xml
     * @param stateMachine
     * @param stateEnumSet
     * @param eventEnumSet
     * @param <P>
     * @param <S>
     * @param <E>
     * @return
     */
    public <P, S extends Enum<S>, E extends Enum<E>> IStateMachine<P> StateMachineInstance(String xml, IStateMachine<P> stateMachine, EnumSet<S> stateEnumSet, EnumSet<E> eventEnumSet) {
        SimpleStateMachineParser<P, S, E> parser = new SimpleStateMachineParser<P, S, E>();
        try {
            return parser.parser(xml, stateMachine, stateEnumSet, eventEnumSet);
        } catch (Exception ex) {
        }
        return null;
    }
}

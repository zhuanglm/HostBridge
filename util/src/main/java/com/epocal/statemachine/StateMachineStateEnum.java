package com.epocal.statemachine;

/**
 * Created by dning on 5/18/2017.
 */

public enum StateMachineStateEnum implements IStateEnum {
    root {
        @Override
        public String toString() {
            return "Root";
        }
    }
}
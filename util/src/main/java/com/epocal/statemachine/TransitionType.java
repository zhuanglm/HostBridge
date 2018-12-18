package com.epocal.statemachine;

/**
 * Created by dning on 5/15/2017.
 */

public enum TransitionType {
    Normal
    {@Override
        public String toString() {
                return "normal";
            }},

    Interrupt{@Override
        public String toString() {
        return "interrupt";
    }},

    Error{@Override
        public String toString() {
        return "error";
    }},

    Internal{@Override
        public String toString() {
        return "internal";
    }},

}

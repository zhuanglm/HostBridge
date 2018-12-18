package com.epocal.statemachine;

/**
 * Created by dning on 5/15/2017.
 */

public enum TransitionHandled {
    Handled,
    UnHandledByRoot,
    UnhandledByNormal,
    UnhandledByFinal,
    UnhandledBySingleton,
}

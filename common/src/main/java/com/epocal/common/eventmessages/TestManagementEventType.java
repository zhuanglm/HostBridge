package com.epocal.common.eventmessages;

/**
 * Created by bmate on 7/31/2017.
 */

public enum TestManagementEventType {

    FOREGROUND_TEST_STARTED,
    FOREGROUND_TEST_RESTARTED,
    BACKGROUND_TEST_STARTED,
    BACKGROUND_TEST_RESTARTED,

    TEST_STOPPED,
    CANNOT_STOP_TEST,
    CANNOT_STOP_MULTIPLE_TESTS,

    CANNOT_START_BACKGROUND_TEST,
    CANNOT_START_FOREGROUND_TEST,
    CANNOT_RESTART_FOREGROUND_TEST,
    CANNOT_RESTART_BACKGROUND_TEST,
    All_TESTS_STOPPED,


}

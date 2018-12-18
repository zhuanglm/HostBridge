package com.epocal.epoctest.testprocess;

/**
 * Created by dning on 5/24/2017.
 */

public enum TestStateActionEnum {
    None {
        @Override
        public String toString() {
            return "None";
        }
    },
    Timeout {
        @Override
        public String toString() {
            return "Timeout";
        }
    },
    Connecting {
        @Override
        public String toString() {
            return "connecting";
        }
    },
    ConnectionFailed {
        @Override
        public String toString() {
            return "ConnectionFailed";
        }
    },
    ConnectionLost {
        @Override
        public String toString() {
            return "ConnectionLost";
        }
    },
    CancelConnection {
        @Override
        public String toString() {
            return "CancelConnection";
        }
    },
    Connected {
        @Override
        public String toString() {
            return "Connected";
        }
    },
    NextStep {
        @Override
        public String toString() {
            return "NextStep";
        }
    },
    CommunicationFailed {
        @Override
        public String toString() {
            return "CommunicationFailed";
        }
    },
    TestFinishedWithFailed {
        @Override
        public String toString() {
            return "CommunicationFailed";
        }
    },
    RealTimeQCFailed {
        @Override
        public String toString() { return "RealTimeQCFailed"; }
    }
}

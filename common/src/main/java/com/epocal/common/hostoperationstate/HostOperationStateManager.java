package com.epocal.common.hostoperationstate;

/**
 * Created by bmate on 08/01/2018.
 */

public class HostOperationStateManager {
    private static HostOperationStateManager instance = null;
    private static Object synchroot = new Object();

    private HostOperationStateManager() {
        // customize if needed

    }
    public static HostOperationStateManager getInstance() {
        synchronized (synchroot){
            if (instance == null) {
                instance = new HostOperationStateManager();
            }
            return instance ;
        }

    }
    public OperationStateType getOperationStateType(){
        OperationStateType retval = OperationStateType.IDLE;
        return retval;
    }
}

package com.epocal.epoctest.teststate.epoctest.patienttest;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusType;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.reader.IMsgPayload;
import com.epocal.reader.enumtype.DAMStageType;
import com.epocal.reader.protocolcommontype.HostErrorCode;
import com.epocal.reader.enumtype.GenericNotificationResponse;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.TestActionResponse;
import com.epocal.reader.nextgen.message.request.test.ack.TstAckReqAck;
import com.epocal.reader.nextgen.message.response.test.action.TstActRspBGETest;
import com.epocal.reader.nextgen.message.response.test.action.TstActRspDevicePeripheralMonitor;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by dning on 7/24/2017.
 */

public class PerformSampleIntroduction extends PerformTest {
    public PerformSampleIntroduction(){}

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);
        stateDataObject.startTimer();

        // TODO: Remove later.For Debugging purpose only to run test with old card.
        //runBloodTestWithOldCard(stateDataObject);
        // TODO: END_DEBUG
    }

    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject) {
        if(stateDataObject.getTestStateAction() == TestStateActionEnum.CommunicationFailed)
        {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.CancelTest;
        }
        IMsgPayload message = stateDataObject.getIMsgPayload();
        if (message == null)
        {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.CancelTest;
        }
        if (message.getDescriptor().getMsgClass() == MessageClass.Generic &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Notification &&
                message.getDescriptor().getType() == GenericNotificationResponse.CommandStarted.value)
        {
            stateDataObject.StopTimer();
            setCommandStatus(CommandStatus.started);
            setTestRunningStatus(TestRunningStatus.NoTestRunning.value);
            return TestStateEventEnum.Handled;
        }
        else if (message.getDescriptor().getMsgClass() == MessageClass.Generic &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Notification &&
                message.getDescriptor().getType() == GenericNotificationResponse.CommandFinished.value)
        {
            //timeout during sample introduction
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.UNKNOWN);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.CancelTest;
        }

        if (message.getDescriptor().getMsgInterface() == InterfaceType.NextGen &&
                message.getDescriptor().getMsgClass() == MessageClass.Test &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Action) {
            IEventEnum eret = handleBGEDataPacket(stateDataObject, message);
            if(eret == TestStateEventEnum.Handled)
            {
                return handleMeasurements(stateDataObject, message);
            }
            return eret;
        }

        return super.onEventPreHandle(stateDataObject);
    }

    private IEventEnum handleMeasurements(TestStateDataObject stateDataObject, IMsgPayload message)
    {
        if (message.getDescriptor().getMsgClass() == MessageClass.Test &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Action &&
                message.getDescriptor().getType() == TestActionResponse.BGETest.value) {
            TstActRspBGETest bge = (TstActRspBGETest) message;
            if (bge == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.CancelTest;
            }
            if (TstActRspBGETest.MessageCodeType.convert(bge.getMessageCode()) == TstActRspBGETest.MessageCodeType.Data) {
                return super.detectSampling(stateDataObject, bge);
            }
            return TestStateEventEnum.Handled;
        }
        return TestStateEventEnum.Handled;
    }

    /**
     * To run the bloodtest using previously captured *.buf files (in Analytical Manager).
     * This way, the old card can be re-used.
     * This method is used for debugging purpose only.
     *
     * 1. Run the blood test to capture AnalyticalManager .buf files with the new card once.
     *    In AnalyticalManager.java file, set WRITE_REQUEST_TO_FILE = true. When the bloodtest runs successfully (with the new card),
     *    a set of input files (*.buf files) will be written out to FileSystem. In subsequent run, AnalyticalManager can use these input files
     *    to run the bloodtest when READ_REQUEST_* flag is set to true.
     *
     *    private static final boolean WRITE_REQUEST_TO_FILE = true;
     *    private static final boolean READ_REQUEST_FROM_FILE_CALCULATE_BGE = false;
     *    private static final boolean READ_REQUEST_FROM_FILE_CALCULATE_COOX_RESULTS = false;
     *
     *    OR:
     *
     *    Copy AnalyticalManager buf files to device on the following location.
     *    IMPORTANT: Do not manually create the folder structure to match below path. Let the application create it.
     *    When you run the application for the first time after the fresh install, "/sdcard/Android/data/com.epocal.host4/files/AM/"
     *    folder will be created. Then terminate the app. Copy the individual *.buf file to "AM" folder in the path below.
     *    IMPORTANT2: You cannot copy the files while the app is running. Make sure the app is terminated when copying the files.
     *    IMPORTANT3: When you delete the application from the device and re-install, all files are deleted at the same time. (You need to copy the files again.)
     *    On Device, using the File Manager. Read "IMPORTANT1" above how to create the folder structure.
     *    /sdcard/Android/data/com.epocal.host4/files/AM/CalculateBGERequest.buf
     *    /sdcard/Android/data/com.epocal.host4/files/AM/CalculateCooxResultsRequest.buf
     *
     * 2. AnalyticalManager.java set the flags to below. This instructs the AnalyticalManager to read the input from files.
     *     private static final boolean WRITE_REQUEST_TO_FILE = false;
     *     private static final boolean READ_REQUEST_FROM_FILE_CALCULATE_BGE = true;
     *     private static final boolean READ_REQUEST_FROM_FILE_CALCULATE_COOX_RESULTS = true;
     *
     * 3. PerformTestCalculation.java. Set below flag. This instructs the TestCalculation to skip copying of Coox data read from the card.
     *     (Since in this case, the input is coming from the files not from the card.) 
     *     private static final boolean CALCULATE_TEST_FROM_ANALYTICAL_MANAGER_FILE = true;
     *
     * 4. PerformSampleIntroduction.java. This instructs to run the bloodtest when the card state reaches “SampleIntroduction” state.
     *     Uncomment the code in onEntry() to call this method.
     *          public void onEntry(TestStateDataObject stateDataObject) {
     *              ...
     *              // For Debugging purpose only
     *              runBloodTestWithOldCard(stateDataObject);
     *              // END_DEBUG
     *          }
     *  5. Compile and run the code on device. Connect to the card reader, when the displays shows “Insert the card”, insert the used-card into the reader.
     *      Once the testing is done, please revert the code in AnalyticalManager.java, PerformTestCalculation.java and PerformSampleIntroduction.java back. 
     *
     */  

    private void runBloodTestWithOldCard(TestStateDataObject stateDataObject) {
        stateDataObject.calculateResults();
    }
}

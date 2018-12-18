package com.epocal.epoctest.teststate.epoctest;

import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.TestStatusType;
import com.epocal.epoctest.teststate.TestState;
import com.epocal.reader.IMsgPayload;
import com.epocal.reader.enumtype.GenericInterfaceResponse;
import com.epocal.reader.enumtype.InterfaceFlag;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.nextgen.message.request.generic.interfaces.GenIntReqBarcodeInterface;
import com.epocal.reader.nextgen.message.request.generic.interfaces.GenIntReqBgeInterface;
import com.epocal.reader.nextgen.message.response.generic.interfaces.GenIntRspBarcodeInterface;
import com.epocal.reader.nextgen.message.response.generic.interfaces.GenIntRspBgeInterface;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by dning on 6/23/2017.
 */

public class SetReaderInterface extends TestState {
    private int mInterfaceSupported = 0;
    private int mInterfaceEnabled = 0;
    private int mNeedTobeEnabled = 0;

    private enum EnableInterfaceChecking {
        NoInterfaceSupported,
        InterfaceEnabled,
        NoInterfaceNeedToEnable,
        CommunicationError,
    }

    public SetReaderInterface() {
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);
        mNeedTobeEnabled = mInterfaceSupported = stateDataObject.getTestDataProcessor().getDeviceIdInfoData().getDeviceIdMainIdInfo().getSupportedInterfaces();
        mInterfaceEnabled = stateDataObject.getTestDataProcessor().getDeviceStatusData().getEnabledInterfaces();

        EnableInterfaceChecking rChecking = SendMessageToReader(stateDataObject);
        if (rChecking == EnableInterfaceChecking.NoInterfaceSupported) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
            eventInfo.setTestStatusType(TestStatusType.DISCONNECTED);
            stateDataObject.postEvent(eventInfo);
        } else if (rChecking == EnableInterfaceChecking.InterfaceEnabled) {
            stateDataObject.startTimer();
        } else if (rChecking == EnableInterfaceChecking.NoInterfaceNeedToEnable) {
            stateDataObject.postEventToStateMachine(stateDataObject, TestStateActionEnum.NextStep);
        } else {
            stateDataObject.postEventToStateMachine(stateDataObject, TestStateActionEnum.CommunicationFailed);
        }
    }

    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject) {
        if (stateDataObject.getTestStateAction() == TestStateActionEnum.CommunicationFailed) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestBeforeTestBegun;
        } else if (stateDataObject.getTestStateAction() == TestStateActionEnum.NextStep) {
            return TestStateEventEnum.ReaderSettings;
        }
        IMsgPayload message = stateDataObject.getIMsgPayload();
        if (message == null) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestBeforeTestBegun;
        }
        if (message.getDescriptor().getMsgClass() == MessageClass.Generic &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Interface &&
                message.getDescriptor().getType() == GenericInterfaceResponse.Barcode.value) {
            stateDataObject.StopTimer();
            GenIntRspBarcodeInterface resp = (GenIntRspBarcodeInterface) message;
            if (resp == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }
            if (resp.getMessageCode() == GenIntRspBarcodeInterface.MessageCodeType.Error.value || resp.getMessageCode() == GenIntRspBarcodeInterface.MessageCodeType.NotDefined.value) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.READERNOCOMPATIBLE);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }
            return doEnableInterface(stateDataObject);
        }
        if (message.getDescriptor().getMsgClass() == MessageClass.Generic &&
                message.getDescriptor().getMsgGroup() == MessageGroup.Interface &&
                message.getDescriptor().getType() == GenericInterfaceResponse.Bge.value) {
            stateDataObject.StopTimer();
            GenIntRspBgeInterface resp = (GenIntRspBgeInterface) message;
            if (resp == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }
            if (resp.getMessageCode() == GenIntRspBgeInterface.MessageCodeType.Error.value || resp.getMessageCode() == GenIntRspBgeInterface.MessageCodeType.NotDefined.value) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.READERNOCOMPATIBLE);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }
            return doEnableInterface(stateDataObject);
        }

        return super.onEventPreHandle(stateDataObject);
    }

    private TestStateEventEnum doEnableInterface(TestStateDataObject stateDataObject) {
        EnableInterfaceChecking rChecking = SendMessageToReader(stateDataObject);
        if (rChecking == EnableInterfaceChecking.NoInterfaceNeedToEnable) {
            return TestStateEventEnum.ReaderSettings;
        } else if (rChecking == EnableInterfaceChecking.InterfaceEnabled) {
            stateDataObject.startTimer();
            return TestStateEventEnum.Handled;
        } else {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.READERNOCOMPATIBLE);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestBeforeTestBegun;
        }
    }

    private EnableInterfaceChecking SendMessageToReader(TestStateDataObject stateDataObject) {
        if (mInterfaceSupported <= 0) {
            return EnableInterfaceChecking.NoInterfaceSupported;
        }
        if (mNeedTobeEnabled <= 0) {
            return EnableInterfaceChecking.NoInterfaceNeedToEnable;
        }
        if (((mNeedTobeEnabled & InterfaceFlag.Barcode.value) != 0) && ((mInterfaceEnabled & InterfaceFlag.Barcode.value) == 0)) {
            GenIntReqBarcodeInterface req = new GenIntReqBarcodeInterface();
            req.setMessageCode(GenIntReqBarcodeInterface.MessageCodeType.Enable.value);
            mNeedTobeEnabled = mNeedTobeEnabled - InterfaceFlag.Barcode.value;
            if (!stateDataObject.sendMessage(req)) {
                return EnableInterfaceChecking.CommunicationError;
            }
            return EnableInterfaceChecking.InterfaceEnabled;
        }
        if (((mNeedTobeEnabled & InterfaceFlag.Bge.value) != 0) && ((mInterfaceEnabled & InterfaceFlag.Bge.value) == 0)) {
            GenIntReqBgeInterface req = new GenIntReqBgeInterface();
            req.setMessageCode(GenIntReqBgeInterface.MessageCodeType.Enable.value);
            mNeedTobeEnabled = mNeedTobeEnabled - InterfaceFlag.Bge.value;
            if (!stateDataObject.sendMessage(req)) {
                return EnableInterfaceChecking.CommunicationError;
            }
            return EnableInterfaceChecking.InterfaceEnabled;
        }

        return EnableInterfaceChecking.NoInterfaceNeedToEnable;
    }
}

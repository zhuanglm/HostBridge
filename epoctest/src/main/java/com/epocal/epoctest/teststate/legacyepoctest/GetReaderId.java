package com.epocal.epoctest.teststate.legacyepoctest;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusErrorType;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.epoctest.testprocess.TestStateEventEnum;
import com.epocal.epoctest.teststate.legacyepoctest.type.LegacyTestState;
import com.epocal.epoctest.teststate.legacyepoctest.type.VersionSetup;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.message.request.command.LegacyReqDeviceId;
import com.epocal.reader.legacy.message.response.LegacyRspDeviceID;
import com.epocal.reader.type.Legacy.LegacyInfo;
import com.epocal.statemachine.IEventEnum;

/**
 * Created by Rzhuang on July 18 2018.
 */

public class GetReaderId extends LegacyTestState {
    private String mDeviceSN;

    public GetReaderId() {
    }

    @Override
    public void onEntry(TestStateDataObject stateDataObject) {
        super.onEntry(stateDataObject);
        mDeviceSN = stateDataObject.getTestDataProcessor().getTestRecord().getReader().getSerialNumber();

        if (sendMessageToReader(stateDataObject)) {
            stateDataObject.startTimer();
        } else {
            stateDataObject.postEventToStateMachine(stateDataObject, TestStateActionEnum.CommunicationFailed);
        }
    }

    @Override
    public IEventEnum onEventPreHandle(TestStateDataObject stateDataObject) {
        message = stateDataObject.getIMsgPayload();
        if (message == null) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
            eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
            stateDataObject.postEvent(eventInfo);
            return TestStateEventEnum.EndTestBeforeTestBegun;
        }

        if (message.getDescriptor().getType() == LegacyMessageType.DeviceIdResponse.value) {
            stateDataObject.StopTimer();
            LegacyRspDeviceID resp = (LegacyRspDeviceID) message;

            if (resp == null) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            }

            LegacyInfo deviceInfo = resp.getLegacyInfo();
            stateDataObject.getTestDataProcessor().getDeviceIdInfoData().setLegacyDeviceInfo(deviceInfo);
            stateDataObject.getTestDataProcessor().mDeviceIDResponse = resp;

            TestRecord testRecord = stateDataObject.getTestDataProcessor().getTestRecord();
            if (testRecord != null && testRecord.getReader() != null) {
                testRecord.getReader().setSoftwareVersion(stateDataObject.getTestDataProcessor()
                        .getDeviceIdInfoData().getLegacyDeviceInfo().getReaderFirmwareVersion());
                testRecord.getReader().setHardwareVersion(stateDataObject.getTestDataProcessor()
                        .getDeviceIdInfoData().getLegacyDeviceInfo().getHardwareId() + " " +
                        stateDataObject.getTestDataProcessor().getDeviceIdInfoData()
                                .getLegacyDeviceInfo().getSibId());
                testRecord.getReader().setMechanicalVersion(stateDataObject.getTestDataProcessor()
                        .getDeviceIdInfoData().getLegacyDeviceInfo().getMechanicId());

                //GlobalDataContext.Instance.DataAccess.Save<epoc.common.Reader>(stateDataObject.TestDataProcessor.TestRecord.Reader);
            }
//            if (testRecord != null) {
//                Reader reader = testRecord.getReader();
//                Integer localContentVersion = reader.getReaderQCContentVersion();
//            }
            //QCFeature.ReaderQCTableManager.Instance.CreateNewReaderInformation(reader);
            //resp =  var deviceIdResponse = stateDataObject.TestDataProcessor.deviceIdResponse;

//            ReaderQCTableStatus readerQCTableStatus = QCFeatureConfiguration.GetReaderQCTableStatus(
//                    deviceInfo.getHostReaderInterface(),deviceInfo.getQCTableFormatVersion(),
//                    deviceInfo.getQCTableContentVersion(),localContentVersion);

//            List<IMsgPayload> messages = null;
//            boolean success = false;
//            if (deviceInfo != null && deviceInfo.isSupportQCTable() &&
//                    readerQCTableStatus != ReaderQCTableStatus.ReaderQCNoSupported)
//            {
//                if (readerQCTableStatus == ReaderQCTableStatus.CleanResetReader)
//                {
//                    messages = CleanResetQCTableToReader(host, reader);
//                }
//                else if (readerQCTableStatus == ReaderQCTableStatus.RestoreReader)
//                {
//                    messages = RestoreQCTableToReader(host, reader);
//                }
//                else
//                {
//                    if (deviceInfo.getQCTableContentVersion().getValue() > localContentVersion)
//                    {
//                        messages = RequestDescriptorQCTableFromReader(host, reader);
//                    }
//                    if (deviceInfo.getQCTableContentVersion().getValue() < localContentVersion)
//                    {
//                        messages = RestoreQCTableToReader(host, reader);
//                    }
//                }
//
//                if (messages != null)
//                {
//                    for (int i = 0; i < messages.size(); i++)
//                    {
//                        success = stateDataObject.sendMessage(messages.get(i));
//                        if (!success)
//                        {
//                            break;
//                        }
//                    }
//                }
//            }
//            stateDataObject.sendMessage(reqCustomConfigMsg(stateDataObject,null,CustomConfigSubcode.eCustomConfigGet,
//                    CustomConfigTask.QCResultsTable, CustomConfigType.Descriptor,
//                    0,(short)0,0));     //fake sending


            if (!stateDataObject.getTestDataProcessor().IsReaderCompatible()) {
                TestEventInfo eventInfo = new TestEventInfo();
                eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                eventInfo.setTestStatusErrorType(TestStatusErrorType.READERNOCOMPATIBLE);
                stateDataObject.postEvent(eventInfo);
                return TestStateEventEnum.EndTestBeforeTestBegun;
            } else {
                if (stateDataObject.getTestDataProcessor().CompareToReaderVersionWithStatistics()) {
                    // get stats for maintenance
                    return TestStateEventEnum.WaitingForStatisticsResponse;
                } else {
                    // reader version doesn't support stats.. go straight to reader status
                    // request the reader status.. we must know if the hcm is corrupted before asking it for
                    // a self-test
                    if (askForReaderStatus(stateDataObject, false, true)) {
                        return TestStateEventEnum.CheckFirstReaderStatus;
                    } else {
                        TestEventInfo eventInfo = new TestEventInfo();
                        eventInfo.setTestEventInfoType(TestEventInfoType.ERROR_INFO);
                        eventInfo.setTestStatusErrorType(TestStatusErrorType.COMMUNICATIONFAILED);
                        stateDataObject.postEvent(eventInfo);
                        return TestStateEventEnum.EndTestBeforeTestBegun;
                    }
                }

            }
        }

        return super.onEventPreHandle(stateDataObject);
    }

    private boolean sendMessageToReader(TestStateDataObject stateDataObject) {

        LegacyReqDeviceId req = new LegacyReqDeviceId(mDeviceSN, VersionSetup.MajorVersion,
                VersionSetup.MinorVersion, VersionSetup.RevisionVersion,
                VersionSetup.MajorInterfaceVersion, VersionSetup.MinorInterfaceVersion);
        return stateDataObject.sendMessage(req);
    }

}
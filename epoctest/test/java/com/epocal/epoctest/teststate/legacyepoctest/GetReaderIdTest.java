package com.epocal.epoctest.teststate.legacyepoctest;

import com.epocal.common.realmentities.Reader;
import com.epocal.common.realmentities.TestRecord;
import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.testprocess.TestDataProcessor;
import com.epocal.epoctest.testprocess.TestStateActionEnum;
import com.epocal.epoctest.testprocess.TestStateDataObject;
import com.epocal.reader.legacy.message.request.command.LegacyReqDeviceId;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GetReaderId.class)
public class GetReaderIdTest {
    private GetReaderId mGetReaderId;

    @Before
    public void setUp() {
        mGetReaderId = new GetReaderId();
        mGetReaderId = PowerMockito.spy(mGetReaderId);
    }

    @After
    public void tearDown() {
        mGetReaderId = null;
    }

    @Test
    public void onEntryFail() {
        TestStateDataObject object = Mockito.mock(TestStateDataObject.class);
        TestDataProcessor dataProcessor = Mockito.mock(TestDataProcessor.class);
        TestRecord testRecord = Mockito.mock(TestRecord.class);
        Reader reader = Mockito.mock(Reader.class);

        Mockito.doReturn(dataProcessor).when(object).getTestDataProcessor();
        Mockito.doReturn(testRecord).when(dataProcessor).getTestRecord();
        Mockito.doReturn(reader).when(testRecord).getReader();
        Mockito.when(reader.getSerialNumber()).thenReturn("12345");
        mGetReaderId.onEntry(object);

        Mockito.verify(object).postEventToStateMachine(object, TestStateActionEnum.CommunicationFailed);
    }

    @Test
    public void onEntrySuccess() {
        TestStateDataObject object = Mockito.mock(TestStateDataObject.class);
        TestDataProcessor dataProcessor = Mockito.mock(TestDataProcessor.class);
        TestRecord testRecord = Mockito.mock(TestRecord.class);
        TestEventInfo testEventInfo = Mockito.mock(TestEventInfo.class);
        LegacyReqDeviceId reqDeviceId = Mockito.mock(LegacyReqDeviceId.class);
        Reader reader = Mockito.mock(Reader.class);

        Mockito.doReturn(dataProcessor).when(object).getTestDataProcessor();
        Mockito.doReturn(testRecord).when(dataProcessor).getTestRecord();
        Mockito.doReturn(reader).when(testRecord).getReader();
        Mockito.when(reader.getSerialNumber()).thenReturn("12345");
        Mockito.doReturn(true).when(object).sendMessage(reqDeviceId);

        try {
            //PowerMockito.when(mGetReaderId,"sendMessageToReader",object).thenReturn(true);
            PowerMockito.whenNew(TestEventInfo.class).withAnyArguments().thenReturn(testEventInfo);
            PowerMockito.doReturn(true).when(mGetReaderId, "sendMessageToReader", object);
            //PowerMockito.when(object,"sendMessage",reqDeviceId).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mGetReaderId.onEntry(object);

        try {
            PowerMockito.verifyPrivate(mGetReaderId).invoke("sendMessageToReader", object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Mockito.verify(object).postEventToStateMachine(object,TestStateActionEnum.CommunicationFailed);
        Mockito.verify(object).postEvent(testEventInfo);

    }

    @Test
    public void onEventPreHandle() {
        //TestStateDataObject object = Mockito.mock(TestStateDataObject.class);
    }
}

package com.epocal.reader.nextgen.message.response.generic.command;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.enumtype.GenericCommandResponse;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.type.AdditionalIdInfo;
import com.epocal.reader.type.MainIdInfo;
import com.epocal.reader.type.MessageDescriptor;

import java.io.IOException;

/**
 * Created by dning on 6/7/2017.
 */

public class GenCmdRspDeviceId extends MsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Generic, MessageGroup.Command,
            GenericCommandResponse.DeviceIdResponse.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public enum MessageCodeType {
        NotDefined((byte) 0),
        Data((byte) 1),
        Error((byte) 2);

        public final byte value;

        MessageCodeType(byte value) {
            this.value = Byte.valueOf(value);
        }

        public static MessageCodeType convert(byte value) {
            return MessageCodeType.values()[value];
        }
    }

    public enum ScopeCode {
        NotDefined((byte) 0),
        MainId((byte) 1),
        AdditionalId((byte) 2);

        public final byte value;

        ScopeCode(byte value) {
            this.value = Byte.valueOf(value);
        }

        public static ScopeCode convert(byte value) {
            return ScopeCode.values()[value];
        }
    }

    public MessageCodeType mResponse;

    public MessageCodeType getResponse() {
        return MessageCodeType.convert(getMessageCode());
    }

    private ScopeCode mScope;

    public ScopeCode getScope() {
        return mScope;
    }

    public void setScope(ScopeCode mScope) {
        this.mScope = mScope;
    }

    private byte mResponseDetails;

    public byte getResponseDetails() {
        return mResponseDetails;
    }

    public void setResponseDetails(byte mResponseDetails) {
        this.mResponseDetails = mResponseDetails;
    }

    public MainIdInfo getMainIdInfo() {
        return mMainIdInfo;
    }

    public void setMainIdInfo(MainIdInfo mMainIdInfo) {
        this.mMainIdInfo = mMainIdInfo;
    }

    private MainIdInfo mMainIdInfo;

    public AdditionalIdInfo getAdditionalIdInfo() {
        return mAdditionalIdInfo;
    }

    public void setAdditionalIdInfo(AdditionalIdInfo mAdditionalIdInfo) {
        this.mAdditionalIdInfo = mAdditionalIdInfo;
    }

    private AdditionalIdInfo mAdditionalIdInfo;

    public GenCmdRspDeviceId() {
        super();
        mScope = ScopeCode.NotDefined;
        mMainIdInfo = new MainIdInfo();
        mAdditionalIdInfo = new AdditionalIdInfo();
    }

    public GenCmdRspDeviceId(ScopeCode scope, MainIdInfo mainInfo, AdditionalIdInfo additionalInfo) {
        setMessageCode(MessageCodeType.Data.value);
        mScope = scope;
        mMainIdInfo = mainInfo;
        mAdditionalIdInfo = additionalInfo;
    }

    public GenCmdRspDeviceId(byte errorCode) {
        setMessageCode(MessageCodeType.Error.value);
        mResponseDetails = errorCode;
    }

    @Override
    public int fillBuffer() {
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer) {
        setRawBuffer(buffer);

        DataStreamReader dsr = new DataStreamReader(getRawBuffer());
        ParseResult parseResult = ParseResult.Success;

        try {
            getDescriptor().readBytes(dsr);
            setMessageCode(dsr.readByte());
        } catch (IOException e) {
            parseResult = ParseResult.UnknownFailure;
        }

        if (parseResult == ParseResult.Success) {
            switch (MessageCodeType.convert(getMessageCode())) {
                case Data:
                    try {
                        mScope = ScopeCode.convert(dsr.readByte());
                    } catch (IOException e) {
                        mScope = ScopeCode.NotDefined;
                    }
                    switch (mScope) {
                        case MainId:
                            if (mMainIdInfo == null) {
                                mMainIdInfo = new MainIdInfo();
                            }
                            try {
                                mMainIdInfo.readBytes(dsr);
                            } catch (IOException e) {
                                parseResult = ParseResult.UnknownFailure;
                            }
                            break;
                        case AdditionalId:
                            if (mAdditionalIdInfo == null) {
                                mAdditionalIdInfo = new AdditionalIdInfo();
                            }
                            try {
                                mAdditionalIdInfo.readBytes(dsr);
                            } catch (IOException e) {
                                parseResult = ParseResult.UnknownFailure;
                            }
                            break;
                        default:
                            parseResult = ParseResult.UnknownFailure;
                    }
                    break;
                case Error:
                    try {
                        mResponseDetails = dsr.readByte();
                    } catch (IOException e) {
                        parseResult = ParseResult.UnknownFailure;
                    }
                    break;

                default:
                    parseResult = ParseResult.InvalidMessageCode;
            }
        }
        dsr.dispose();
        return parseResult;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MsgCode: " + MessageCodeType.convert(getMessageCode()).toString());

        switch (MessageCodeType.convert(getMessageCode())) {
            case Data:
                sb.append("Scope: " + mScope.toString());
                switch (mScope) {
                    case MainId:
                        sb.append("MainIdInfo: " + mMainIdInfo.toString());
                        break;
                    case AdditionalId:
                        sb.append("AdditionalIdInfo: " + mAdditionalIdInfo.toString());
                        break;
                }
                break;
            case Error:
                sb.append("ResponseDetails: " + mResponseDetails);
                break;
        }
        return sb.toString();
    }
}

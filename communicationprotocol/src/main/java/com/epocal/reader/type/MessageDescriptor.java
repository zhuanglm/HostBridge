package com.epocal.reader.type;

import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.enumtype.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/6/2017.
 */

public class MessageDescriptor {
    public static final MessageDescriptor NotDefined =
            new MessageDescriptor(InterfaceType.NotDefined, MessageClass.NotDefined, MessageGroup.NotDefined, (byte)0);

    public InterfaceType getMsgInterface() {
        return mMsgInterface;
    }

    public void setMsgInterface(InterfaceType mMsgInterface) {
        this.mMsgInterface = mMsgInterface;
    }

    public MessageClass getMsgClass() {
        return mMsgClass;
    }

    public void setMsgClass(MessageClass mMsgClass) {
        this.mMsgClass = mMsgClass;
    }

    public MessageGroup getMsgGroup() {
        return mMsgGroup;
    }

    public void setMsgGroup(MessageGroup mMsgGroup) {
        this.mMsgGroup = mMsgGroup;
    }

    public byte getType() {
        return mType;
    }

    public void setType(byte mType) {
        this.mType = mType;
    }

    private InterfaceType mMsgInterface;
    private MessageClass mMsgClass;
    private MessageGroup mMsgGroup;
    private byte mType;

    public MessageDescriptor(){}

    public MessageDescriptor(
            InterfaceType msginterface,
            MessageClass msgclass,
            MessageGroup msggroup,
            byte type)
    {
        mMsgInterface = msginterface;
        mMsgClass = msgclass;
        mMsgGroup = msggroup;
        mType = type;
    }

    @Override
    public String toString() {
        return String.format(
                "Message Type {{ Interface = %d, Class = %d, Group = %d, Type = %d }}", mMsgInterface, mMsgClass, mMsgGroup, mType);
    }

    public byte[] toBytes() throws IOException
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write((byte)mMsgClass.value);
        output.write((byte)mMsgGroup.value);
        output.write(mType);
        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    public void readBytes(DataStreamReader dsr) throws IOException
    {
        mMsgClass = MessageClass.convert(dsr.readByte());
        mMsgGroup = MessageGroup.convert(dsr.readByte());
        mType = dsr.readByte();
    }

}

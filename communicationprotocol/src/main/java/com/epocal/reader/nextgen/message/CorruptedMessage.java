package com.epocal.reader.nextgen.message;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.enumtype.GenericCommandResponse;
import com.epocal.reader.enumtype.GenericErrorResponse;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.type.MessageDescriptor;

import java.util.Arrays;

/**
 * Created by dning on 6/9/2017.
 */

public class CorruptedMessage extends MsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NotDefined, MessageClass.NotDefined, MessageGroup.NotDefined,
            GenericErrorResponse.NotDefined.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    @Override
    public int fillBuffer() {
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer)
    {
        setRawBuffer(buffer);
        return ParseResult.Success;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Unknown Message: " + Arrays.toString(getRawBuffer()));
        return sb.toString();
    }
}

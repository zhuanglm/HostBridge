package com.epocal.reader.parser;

import com.epocal.reader.IMsgPayload;
import com.epocal.reader.enumtype.InterfaceType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/13/2017.
 */

public class PacketFactory implements IPacket {
    private final byte EOFByte = (byte) 0xAA;
    private final byte[] CRCByte = new byte[]{0x78, 0x56};

    @Override
    public byte[] createPayload(IPacketHeader header, IMsgPayload payload) {
        if (payload.getDescriptor().getMsgInterface() == InterfaceType.NextGen) {
            payload.fillBuffer();
            header.setPayloadLength((short) (payload.getRawBuffer().length + 2)); //payload length includes CRC
            header.setInterfaceType(payload.getDescriptor().getMsgInterface().value);

            byte[] buff = new byte[0];
            try {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                output.write(header.createHeader());
                output.write(payload.getRawBuffer());
                output.write(CRCByte); //CRC for payload, hardcoded
                output.write(EOFByte);
                output.flush();
                buff = output.toByteArray();
                output.close();
            } catch (IOException e) {
            }
            return buff;
        } else if(payload.getDescriptor().getMsgInterface() == InterfaceType.Legacy){ // added by rzhuang at July 24 2018 for Legacy
            byte[] preamble = { (byte)0xC0, (byte)0xDE, (byte)0xC0, (byte)0xCA };
            int nCount = payload.fillBuffer();

            byte[] buff = new byte[0];
            try {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                output.write(preamble);

                //message.Type;
                output.write(payload.getDescriptor().getType());

                // fill message number
                int nNumber = header.getPacketNumber();
                output.write((byte)(nNumber >> 8));
                output.write((byte)nNumber);

                // fill payload and payload length
                output.write((byte)(nCount >> 8));
                output.write((byte)nCount);
                output.write(payload.getRawBuffer());

                // place EOF after payload
                output.write(EOFByte);
                output.flush();
                buff = output.toByteArray();
                output.close();
            } catch (IOException e) {
            }
            return buff;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

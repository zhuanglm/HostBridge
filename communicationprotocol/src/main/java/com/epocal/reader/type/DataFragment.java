package com.epocal.reader.type;
import com.epocal.reader.common.DataStreamReader;

import java.io.IOException;

/**
 * Created by dning on 6/7/2017.
 */

public abstract class DataFragment {
    public DataFragment() { }
    public abstract byte[] toBytes() throws IOException;
    public abstract void readBytes(DataStreamReader dsr) throws IOException;
}

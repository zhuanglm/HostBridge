package com.epocal.epoctest.testconfiguration.loader;

import com.epocal.common.androidutil.FileSystemUtil;
import com.epocal.common.types.ReaderType;
import com.epocal.epoctest.testconfiguration.TestConfigurationSet;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by dning on 9/21/2017.
 */

public class TestConfigurationLoader {
    public TestConfigurationLoader(){}

    public TestConfigurationSet loadXMLConfiguration(ReaderType readerType)
    {
        FileSystemUtil testConfigFile = new FileSystemUtil("testconfig.txt", FileSystemUtil.FileLocation.Assets);
        String rawData = testConfigFile.read();
        testConfigFile.close();
        if (rawData.equals("")) return null;

        TCXMLParser parser = new TCXMLParser();
        try {
            Document doc = parser.load(rawData);
            if (doc != null)
            {
                return parser.parse(doc, readerType);
            }
        }
        catch (ParserConfigurationException e)
        {
            return null;
        }
        catch (SAXException e)
        {
            return null;
        }
        catch (IOException e)
        {
            return null;
        }
        return null;
    }
}

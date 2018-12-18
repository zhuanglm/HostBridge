package com.epocal.epoctest.testconfiguration.loader;

import com.epocal.common.types.ReaderType;
import com.epocal.epoctest.testconfiguration.CardSetup;
import com.epocal.epoctest.testconfiguration.CardTabSetup;
import com.epocal.epoctest.testconfiguration.ChannelLimit;
import com.epocal.epoctest.testconfiguration.ExtraParameter;
import com.epocal.epoctest.testconfiguration.HumidityDetect;
import com.epocal.epoctest.testconfiguration.InsanityRange;
import com.epocal.epoctest.testconfiguration.LimitKey;
import com.epocal.epoctest.testconfiguration.QualityControlLimit;
import com.epocal.epoctest.testconfiguration.QualityControlLimitPhase;
import com.epocal.epoctest.testconfiguration.RealTimeQc;
import com.epocal.epoctest.testconfiguration.ReportableRange;
import com.epocal.epoctest.testconfiguration.SensorDescriptor;
import com.epocal.epoctest.testconfiguration.SensorLayout;
import com.epocal.epoctest.testconfiguration.SequenceBlock;
import com.epocal.epoctest.testconfiguration.SequenceItem;
import com.epocal.epoctest.testconfiguration.TestConfiguration;
import com.epocal.epoctest.testconfiguration.TestConfigurationSet;
import com.epocal.epoctest.testconfiguration.Window;
import com.epocal.epoctest.testconfiguration.WindowPhase;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by dning on 9/21/2017.
 */

public class TCXMLParser extends TestConfigurationLoader {
    public TCXMLParser() {
    }

    public Document load(String xml)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        InputSource input = new InputSource(new StringReader(xml));
        Document xmlDoc = builder.parse(input);
        return xmlDoc;
    }

    public TestConfigurationSet parse(Document xmlDoc, ReaderType readerType) {
        NodeList nodes;
        TestConfigurationSet testConfigurationSet = new TestConfigurationSet();
        xmlDoc.getDocumentElement().normalize();
        Element root = xmlDoc.getDocumentElement();

        //testonfig
        parseTestConfig(root, testConfigurationSet, readerType);

        //test sequence
        for (TestConfiguration testConfiguration : testConfigurationSet.getTestConfigurationList(readerType)) {
            parseTestSequence(root, testConfiguration);
        }

        // sensor descriptor
        for (TestConfiguration testConfiguration : testConfigurationSet.getTestConfigurationList(readerType)) {
            parseSensorDescriptor(root, testConfiguration);
        }

        // sensor layout
        for (TestConfiguration testConfiguration : testConfigurationSet.getTestConfigurationList(readerType)) {
            parseSensorLayout(root, testConfiguration);
        }

        //DryCardCheck
        for (TestConfiguration testConfiguration : testConfigurationSet.getTestConfigurationList(readerType)) {
            parseDryCardCheck(root, testConfiguration);
        }

        //SelfCheck
        for (TestConfiguration testConfiguration : testConfigurationSet.getTestConfigurationList(readerType)) {
            parseSelfCheck(root, testConfiguration);
        }

        //ReportableRanges
        for (TestConfiguration testConfiguration : testConfigurationSet.getTestConfigurationList(readerType)) {
            parseReportableRanges(root, testConfiguration);
        }

        //InsanityRanges
        for (TestConfiguration testConfiguration : testConfigurationSet.getTestConfigurationList(readerType)) {
            parseInsanityRanges(root, testConfiguration);
        }

        //RealTimeQC
        for (TestConfiguration testConfiguration : testConfigurationSet.getTestConfigurationList(readerType)) {
            parseRealTimeQC(root, testConfiguration);
        }

        //HumidityDetect
        for (TestConfiguration testConfiguration : testConfigurationSet.getTestConfigurationList(readerType)) {
            parseHumidityDetect(root, testConfiguration);
        }

        //Limit key
        for (TestConfiguration testConfiguration : testConfigurationSet.getTestConfigurationList(readerType)) {
            parseLimitKey(root, testConfiguration);
        }

        //ChannelLimits
        for (TestConfiguration testConfiguration : testConfigurationSet.getTestConfigurationList(readerType)) {
            parseChannelLimits(root, testConfiguration);
        }

        //HostTestResultConfigSetting
        for (TestConfiguration testConfiguration : testConfigurationSet.getTestConfigurationList(readerType)) {
            parseHostTestResultConfigSetting(root, testConfiguration);
        }
        /*
        Node childNode = xelements.item(0);
        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            Element childElement = (Element) childNode;
            if (childElement.getAttribute(XMLAttributeEnum.id.toString()).equals(StateMachineStateEnum.root.toString())) //root state
            {
                stateMachine.addRootState(parseState(childElement, null));
            } else {
                IState root = new State<P>();
                root.setStateID(StateMachineStateEnum.root);
                root.setStateName(StateMachineStateEnum.root.toString());
                root = stateMachine.addRootState((IState) root);

                stateMachine.addState(parseState(childElement, root));
                while (childNode.getNextSibling() != null) {
                    childNode = childNode.getNextSibling();
                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element childElement2 = (Element) childNode;
                        stateMachine.addState(parseState(childElement2, root));
                    }
                }
            }
        }
        */

        return testConfigurationSet;
    }

    private void parseTestConfig(Element root, TestConfigurationSet testConfigurationSet, ReaderType readerType) {
        NodeList nodes = root.getElementsByTagName("TestConfig");
        if (nodes.getLength() == 0) {
            return;
        }
        for (int i = 0; i < nodes.getLength(); i++) {
            Node childNode = nodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                NodeList subnodes = childNode.getChildNodes();
                if (subnodes == null) {
                    return;
                }
                TestConfiguration testConfiguration = new TestConfiguration();
                testConfigurationSet.addTestConfiguration(readerType, testConfiguration);
                for (int j = 0; j < subnodes.getLength(); j++) {
                    Node subChildNode = subnodes.item(j);
                    if (subChildNode.getNodeType() == Node.ELEMENT_NODE) {
                        String name = subChildNode.getNodeName();
                        if (name.equals("CardTypeString")) {
                            testConfiguration.CardTypeString = subChildNode.getTextContent();
                            continue;
                        }
                        if (name.equals("CardType")) {
                            testConfiguration.CardType = Integer.parseInt(subChildNode.getTextContent());
                            continue;
                        }

                        if (name.equals("Version")) {
                            testConfiguration.ConfigVersion.Comments = subChildNode.getTextContent();
                            continue;
                        }

                        if (name.equals("TransmissionMode")) {
                            testConfiguration.ReaderConfigSetting.TransmissionMode = Integer.parseInt(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("BubbleDetectMode")) {
                            testConfiguration.ReaderConfigSetting.BubbleDetectSetting.BDMode = Integer.parseInt(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("BubbleDetectFrequency")) {
                            testConfiguration.ReaderConfigSetting.BubbleDetectSetting.BDFrequency = Double.parseDouble(subChildNode.getTextContent());
                            continue;
                        }

                        if (name.equals("CalInitTime")) {
                            testConfiguration.ReaderConfigSetting.CalibrationInitExpiryTimer = Double.parseDouble(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("AirInitThreshold")) {
                            testConfiguration.ReaderConfigSetting.AirInitThreshold = Double.parseDouble(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("FluidInitThreshold")) {
                            testConfiguration.ReaderConfigSetting.FluidInitThreshold = Double.parseDouble(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("AirAfterFluidThreshold")) {
                            testConfiguration.ReaderConfigSetting.AirAfterFluidThreshold = Double.parseDouble(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("FluidAfterFluidThreshold")) {
                            testConfiguration.ReaderConfigSetting.FluidAfterFluidThreshold = Double.parseDouble(subChildNode.getTextContent());
                            continue;
                        }

                        if (name.equals("DAMADCFilterOrder")) {
                            testConfiguration.ReaderConfigSetting.ADCConfigSetting.DAMADCFilterOrder = Double.parseDouble(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("DAMADCInputBuffer")) {
                            testConfiguration.ReaderConfigSetting.ADCConfigSetting.DAMADCInputBuffer = Double.parseDouble(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("DAMADCPolarityMode")) {
                            testConfiguration.ReaderConfigSetting.ADCConfigSetting.DAMADCPolarityMode = Double.parseDouble(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("DAMADCVDACOffset")) {
                            testConfiguration.ReaderConfigSetting.ADCConfigSetting.DAMADCVDACOffset = Double.parseDouble(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("DAMADCPGA")) {
                            testConfiguration.ReaderConfigSetting.ADCConfigSetting.DAMADCPGA = Double.parseDouble(subChildNode.getTextContent());
                            continue;
                        }
//begin of *next gen*
                        if (name.equals("ReadyTimer")) {
                            testConfiguration.ReaderConfigSetting.ReaderTimerSetting.ReadyTimer = Integer.parseInt(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("HandleTurningTimer")) {
                            testConfiguration.ReaderConfigSetting.ReaderTimerSetting.HandleTurningTimer = Integer.parseInt(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("CardRemovingTimer")) {
                            testConfiguration.ReaderConfigSetting.ReaderTimerSetting.CardRemovingTimer = Integer.parseInt(subChildNode.getTextContent());
                            continue;
                        }

                        if (name.equals("CalibrationInitTime")) {
                            testConfiguration.ReaderConfigSetting.ReaderTimerSetting.CalibrationInitTime = Integer.parseInt(subChildNode.getTextContent());
                            continue;
                        }
//end of *next gen*
                        if (name.equals("CalibrationExpiry")) {
                            testConfiguration.ReaderConfigSetting.ReaderTimerSetting.CalibrationExpiry = Integer.parseInt(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("SampleIntroductionExpiry")) {
                            testConfiguration.ReaderConfigSetting.ReaderTimerSetting.SampleIntroductionExpiry = Integer.parseInt(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("SampleCollectionExpiry")) {
                            testConfiguration.ReaderConfigSetting.ReaderTimerSetting.SampleCollectionExpiry = Integer.parseInt(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("SampleCollectionExpiryAqueous")) {
                            testConfiguration.ReaderConfigSetting.ReaderTimerSetting.SampleCollectionExpiryAqueous = Integer.parseInt(subChildNode.getTextContent());
                            continue;
                        }

                        if (name.equals("TopSetPoint")) {
                            testConfiguration.ReaderConfigSetting.TopHeaterSetting.SetPoint = Double.parseDouble(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("TopKP")) {
                            testConfiguration.ReaderConfigSetting.TopHeaterSetting.KP = Double.parseDouble(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("TopKD")) {
                            testConfiguration.ReaderConfigSetting.TopHeaterSetting.KD = Double.parseDouble(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("TopKI")) {
                            testConfiguration.ReaderConfigSetting.TopHeaterSetting.KI = Double.parseDouble(subChildNode.getTextContent());
                            continue;
                        }

                        if (name.equals("BottomSetPoint")) {
                            testConfiguration.ReaderConfigSetting.BottomHeaterSetting.SetPoint = Double.parseDouble(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("BottomKP")) {
                            testConfiguration.ReaderConfigSetting.BottomHeaterSetting.KP = Double.parseDouble(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("BottomKD")) {
                            testConfiguration.ReaderConfigSetting.BottomHeaterSetting.KD = Double.parseDouble(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("BottomKI")) {
                            testConfiguration.ReaderConfigSetting.BottomHeaterSetting.KI = Double.parseDouble(subChildNode.getTextContent());
                            continue;
                        }

                        if (name.equals("SampleFrequency")) {
                            testConfiguration.ReaderConfigSetting.SamplingInfo.SamplingFrequency = Double.parseDouble(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("AnalogClock")) {
                            testConfiguration.ReaderConfigSetting.SamplingInfo.AnalogClock = Integer.parseInt(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("SamplesToSend")) {
                            testConfiguration.ReaderConfigSetting.SamplingInfo.SamplesPerChannel = Integer.parseInt(subChildNode.getTextContent());
                            continue;
                        }

                        if (name.equals("TestSequenceVersionMajor")) {
                            testConfiguration.TestConfigSetting.testSequenceVersion.Major = Integer.parseInt(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("TestSequenceVersionMinor")) {
                            testConfiguration.TestConfigSetting.testSequenceVersion.Minor = Integer.parseInt(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("TestSequenceVersionRevision")) {
                            testConfiguration.TestConfigSetting.testSequenceVersion.Revision = Integer.parseInt(subChildNode.getTextContent());
                            continue;
                        }
                    }
                }
            }
        }

    }

    private void parseDryCardCheck(Element root, TestConfiguration testConfiguration) {
        NodeList nodes = root.getElementsByTagName("DryCardCheck");
        Node childNode = nodes.item(0);
        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            NodeList subnodes = childNode.getChildNodes();
            if (subnodes.getLength() == 0) {
                return;
            }
            for (int j = 0; j < subnodes.getLength(); j++) {
                Node subChildNode = subnodes.item(j);
                if (subChildNode.getNodeType() == Node.ELEMENT_NODE) {
                    String name = subChildNode.getNodeName();
                    if (name.equals("ReaderType")) {
                        testConfiguration.DryCardCheckConfigSetting.ReaderType = Integer.parseInt(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("TransmissionMode")) {
                        testConfiguration.DryCardCheckConfigSetting.TransmissionMode = Integer.parseInt(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("QCLimitAmperoometric")) {
                        testConfiguration.DryCardCheckConfigSetting.QCLimitAmperometricLow = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("QCLimitAmperometricHigh")) {
                        testConfiguration.DryCardCheckConfigSetting.QCLimitAmperometricHigh = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("QCLimit30K")) {
                        testConfiguration.DryCardCheckConfigSetting.QCLimit30KLow = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("QCLimit30KHigh")) {
                        testConfiguration.DryCardCheckConfigSetting.QСLimit30KHigh = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("DryCardBDMode")) {
                        testConfiguration.DryCardCheckConfigSetting.BubbleDetectSetting.BDMode = Integer.parseInt(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("DryCardBDFrequency")) {
                        testConfiguration.DryCardCheckConfigSetting.BubbleDetectSetting.BDFrequency = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }

                    if (name.equals("DryCardAirThreshold")) {
                        testConfiguration.DryCardCheckConfigSetting.DryCardAirThreshold = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("DryCardFluidThreshold")) {
                        testConfiguration.DryCardCheckConfigSetting.DryCardFluidThreshold = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }

                    if (name.equals("DAMADCFilterOrder")) {
                        testConfiguration.DryCardCheckConfigSetting.ADCConfigSetting.DAMADCFilterOrder = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("DAMADCInputBuffer")) {
                        testConfiguration.DryCardCheckConfigSetting.ADCConfigSetting.DAMADCInputBuffer = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("DAMADCPolarityMode")) {
                        testConfiguration.DryCardCheckConfigSetting.ADCConfigSetting.DAMADCPolarityMode = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("DAMADCVDACOffset")) {
                        testConfiguration.DryCardCheckConfigSetting.ADCConfigSetting.DAMADCVDACOffset = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("DAMADCPGA")) {
                        testConfiguration.DryCardCheckConfigSetting.ADCConfigSetting.DAMADCPGA = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }

                    if (name.equals("DryCardCheckDuration")) {
                        testConfiguration.DryCardCheckConfigSetting.Duration = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }

                    if (name.equals("SampleFrequency")) {
                        testConfiguration.DryCardCheckConfigSetting.SamplingInfoSetting.SamplingFrequency = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("AnalogClock")) {
                        testConfiguration.DryCardCheckConfigSetting.SamplingInfoSetting.AnalogClock = Integer.parseInt(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("SamplesPerChannel")) {
                        testConfiguration.DryCardCheckConfigSetting.SamplingInfoSetting.SamplesPerChannel = Integer.parseInt(subChildNode.getTextContent());
                        continue;
                    }

                    if (name.equals("SequenceLength")) {
                        testConfiguration.DryCardCheckConfigSetting.SequenceLength = Integer.parseInt(subChildNode.getTextContent());
                        continue;
                    }

                    if (name.equals("DryCardCheckVersionMajor")) {
                        testConfiguration.DryCardCheckConfigSetting.DryCheckSequenceVersion.Major = Integer.parseInt(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("DryCardCheckVersionMinor")) {
                        testConfiguration.DryCardCheckConfigSetting.DryCheckSequenceVersion.Minor = Integer.parseInt(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("DryCardCheckVersionRevision")) {
                        testConfiguration.DryCardCheckConfigSetting.DryCheckSequenceVersion.Revision = Integer.parseInt(subChildNode.getTextContent());
                        continue;
                    }
                }
            }
        }
        parseDryCardCheckSequence(root, testConfiguration);
    }

    private void parseDryCardCheckSequence(Element root, TestConfiguration testConfiguration) {
        NodeList nodes = root.getElementsByTagName("DryCardCheckSequenceBlocks");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node childNode = nodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                NodeList subnodes = childNode.getChildNodes();
                if (subnodes.getLength() == 0) {
                    return;
                }
                SequenceBlock block = new SequenceBlock();
                for (int j = 0; j < subnodes.getLength(); j++) {
                    Node subChildNode = subnodes.item(j);
                    if (subChildNode.getNodeType() == Node.ELEMENT_NODE) {
                        String name = subChildNode.getNodeName();
                        if (name.equals("BlockNumber")) {
                            block.BlockNumber = Integer.parseInt(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("BlockLength")) {
                            block.BlockLength = Integer.parseInt(subChildNode.getTextContent());
                            continue;
                        }
                    }
                }
                testConfiguration.DryCardCheckConfigSetting.SequenceBlocks.add(block);
            }
        }
        int idx = 0;
        SequenceBlock block = testConfiguration.DryCardCheckConfigSetting.SequenceBlocks.get(idx);
        NodeList nodes2 = root.getElementsByTagName("DryCardCheckSampling");
        for (int j = 0; j < nodes2.getLength(); j++) {
            Node subChildNode2 = nodes2.item(j);
            if (subChildNode2.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement2 = (Element) subChildNode2;
                parseTestSequence(childElement2, block);
                if (block.BlockLength == block.Sequences.size()) {
                    idx++;
                    if (idx >= testConfiguration.DryCardCheckConfigSetting.SequenceBlocks.size()) {
                        break;
                    }
                    block = testConfiguration.DryCardCheckConfigSetting.SequenceBlocks.get(idx);
                }
            }
        }
    }

    private void parseSelfCheck(Element root, TestConfiguration testConfiguration) {
        NodeList nodes = root.getElementsByTagName("SelfCheck");
        Node childNode = nodes.item(0);
        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            NodeList subnodes = childNode.getChildNodes();
            if (subnodes.getLength() == 0) {
                return;
            }
            for (int j = 0; j < subnodes.getLength(); j++) {
                Node subChildNode = subnodes.item(j);
                if (subChildNode.getNodeType() == Node.ELEMENT_NODE) {
                    String name = subChildNode.getNodeName();
                    if (name.equals("QCLimitPotentiometricLow")) {
                        testConfiguration.SelfCheckConfigSetting.QCLimitPotentiometricLow = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("QCLimitPotentiometricHigh")) {
                        testConfiguration.SelfCheckConfigSetting.QCLimitPotentiometricHigh = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("QCLimitAmperometricLow")) {
                        testConfiguration.SelfCheckConfigSetting.QCLimitAmperometricLow = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("QCLimitAmperometricHigh")) {
                        testConfiguration.SelfCheckConfigSetting.QCLimitAmperometricHigh = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("QCLimitConductivityLow")) {
                        testConfiguration.SelfCheckConfigSetting.QCLimitConductivityLow = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("QCLimitConductivityHigh")) {
                        testConfiguration.SelfCheckConfigSetting.QCLimitConductivityHigh = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("QCLimitGroundLow")) {
                        testConfiguration.SelfCheckConfigSetting.QCLimitGroundLow = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("QCLimitGroundHigh")) {
                        testConfiguration.SelfCheckConfigSetting.QCLimitGroundHigh = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }

                    if (name.equals("SelfCheckBDMode")) {
                        testConfiguration.SelfCheckConfigSetting.BubbleDetectSetting.BDMode = Integer.parseInt(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("SelfCheckBDFrequency")) {
                        testConfiguration.SelfCheckConfigSetting.BubbleDetectSetting.BDFrequency = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }

                    if (name.equals("DAMADCFilterOrder")) {
                        testConfiguration.SelfCheckConfigSetting.ADCConfigSetting.DAMADCFilterOrder = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("DAMADCInputBuffer")) {
                        testConfiguration.SelfCheckConfigSetting.ADCConfigSetting.DAMADCInputBuffer = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("DAMADCPolarityMode")) {
                        testConfiguration.SelfCheckConfigSetting.ADCConfigSetting.DAMADCPolarityMode = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("DAMADCVDACOffset")) {
                        testConfiguration.SelfCheckConfigSetting.ADCConfigSetting.DAMADCVDACOffset = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("DAMADCPGA")) {
                        testConfiguration.SelfCheckConfigSetting.ADCConfigSetting.DAMADCPGA = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }

                    if (name.equals("SelfCheckDuration")) {
                        testConfiguration.SelfCheckConfigSetting.Duration = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }

                    if (name.equals("SampleFrequency")) {
                        testConfiguration.SelfCheckConfigSetting.SamplingInfoSetting.SamplingFrequency = Double.parseDouble(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("AnalogClock")) {
                        testConfiguration.SelfCheckConfigSetting.SamplingInfoSetting.AnalogClock = Integer.parseInt(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("SamplesPerChannel")) {
                        testConfiguration.SelfCheckConfigSetting.SamplingInfoSetting.SamplesPerChannel = Integer.parseInt(subChildNode.getTextContent());
                        continue;
                    }

                    if (name.equals("SequenceLength")) {
                        testConfiguration.SelfCheckConfigSetting.SequenceLength = Integer.parseInt(subChildNode.getTextContent());
                        continue;
                    }

                    if (name.equals("SelfCheckVersionMajor")) {
                        testConfiguration.SelfCheckConfigSetting.SelfCheckVersion.Major = Integer.parseInt(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("SelfCheckVersionMinor")) {
                        testConfiguration.SelfCheckConfigSetting.SelfCheckVersion.Minor = Integer.parseInt(subChildNode.getTextContent());
                        continue;
                    }
                    if (name.equals("SelfCheckVersionRevision")) {
                        testConfiguration.SelfCheckConfigSetting.SelfCheckVersion.Revision = Integer.parseInt(subChildNode.getTextContent());
                        continue;
                    }
                }
            }
        }
        parseSelfCheckSequence(root, testConfiguration);
    }

    private void parseSelfCheckSequence(Element root, TestConfiguration testConfiguration) {
        NodeList nodes = root.getElementsByTagName("SelfCheckSequenceBlocks");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node childNode = nodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                NodeList subnodes = childNode.getChildNodes();
                if (subnodes.getLength() == 0) {
                    return;
                }
                SequenceBlock block = new SequenceBlock();
                for (int j = 0; j < subnodes.getLength(); j++) {
                    Node subChildNode = subnodes.item(j);
                    if (subChildNode.getNodeType() == Node.ELEMENT_NODE) {
                        String name = subChildNode.getNodeName();
                        if (name.equals("BlockNumber")) {
                            block.BlockNumber = Integer.parseInt(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("BlockLength")) {
                            block.BlockLength = Integer.parseInt(subChildNode.getTextContent());
                            continue;
                        }
                    }
                }
                testConfiguration.SelfCheckConfigSetting.SequenceBlocks.add(block);
            }
        }
        int idx = 0;
        SequenceBlock block = testConfiguration.SelfCheckConfigSetting.SequenceBlocks.get(idx);
        NodeList nodes2 = root.getElementsByTagName("SelfCheckSampling");
        for (int j = 0; j < nodes2.getLength(); j++) {
            Node subChildNode2 = nodes2.item(j);
            if (subChildNode2.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement2 = (Element) subChildNode2;
                parseTestSequence(childElement2, block);
                if (block.BlockLength == block.Sequences.size()) {
                    idx++;
                    if (idx >= testConfiguration.SelfCheckConfigSetting.SequenceBlocks.size()) {
                        break;
                    }
                    block = testConfiguration.SelfCheckConfigSetting.SequenceBlocks.get(idx);
                }
            }
        }
    }

    private void parseTestSequence(Element root, TestConfiguration testConfiguration) {
        NodeList nodes = root.getElementsByTagName("TestSequenceBlocks");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node childNode = nodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                NodeList subnodes = childNode.getChildNodes();
                if (subnodes.getLength() == 0) {
                    return;
                }
                SequenceBlock block = new SequenceBlock();
                for (int j = 0; j < subnodes.getLength(); j++) {
                    Node subChildNode = subnodes.item(j);
                    if (subChildNode.getNodeType() == Node.ELEMENT_NODE) {
                        String name = subChildNode.getNodeName();
                        if (name.equals("BlockNumber")) {
                            block.BlockNumber = Integer.parseInt(subChildNode.getTextContent());
                            continue;
                        }
                        if (name.equals("BlockLength")) {
                            block.BlockLength = Integer.parseInt(subChildNode.getTextContent());
                            continue;
                        }
                    }
                }
                testConfiguration.TestConfigSetting.SequenceBlocks.add(block);
            }
        }
        int idx = 0;
        SequenceBlock block = testConfiguration.TestConfigSetting.SequenceBlocks.get(idx);
        NodeList nodes2 = root.getElementsByTagName("TestSampling");
        for (int j = 0; j < nodes2.getLength(); j++) {
            Node subChildNode2 = nodes2.item(j);
            if (subChildNode2.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement2 = (Element) subChildNode2;

                NodeList nodes3 = childElement2.getElementsByTagName("CardType");
                if (testConfiguration.CardType != Integer.parseInt(nodes3.item(0).getTextContent())) {
                    continue;
                }

                parseTestSequence(childElement2, block);
                if (block.BlockLength == block.Sequences.size()) {
                    idx++;
                    if (idx >= testConfiguration.TestConfigSetting.SequenceBlocks.size()) {
                        break;
                    }
                    block = testConfiguration.TestConfigSetting.SequenceBlocks.get(idx);
                }
            }
        }
    }

    private void parseTestSequence(Node childNode, SequenceBlock block) {
        SequenceItem sequence = new SequenceItem();
        NodeList nodes = childNode.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node subChildNode = nodes.item(i);
            if (subChildNode.getNodeType() == Node.ELEMENT_NODE) {
                String name = subChildNode.getNodeName();
                if (name.equals("SequenceNumber")) {
                    sequence.SequenceNumber = sequence.ChannelConfigSetting.SequenceNumber = Integer.parseInt(subChildNode.getTextContent());
                    continue;
                }
                if (name.equals("SensorLayout")) {
                    sequence.ChannelConfigSetting.SensorLayout = Integer.parseInt(subChildNode.getTextContent());
                    continue;
                }
                if (name.equals("ChannelType")) {
                    sequence.ChannelConfigSetting.ChannelType = (byte) Integer.parseInt(subChildNode.getTextContent());
                    continue;
                }
                if (name.equals("Inputs")) {
                    sequence.ChannelConfigSetting.Inputs = (byte) Integer.parseInt(subChildNode.getTextContent());
                    continue;
                }
                if (name.equals("MUXControl")) {
                    sequence.ChannelConfigSetting.MuxControl = (byte) Integer.parseInt(subChildNode.getTextContent());
                    continue;
                }
                if (name.equals("ADCMUX")) {
                    sequence.ChannelConfigSetting.ADCMUX = (byte) Integer.parseInt(subChildNode.getTextContent());
                    continue;
                }
                if (name.equals("VAPP1")) {
                    sequence.ChannelConfigSetting.Vapp1 =  Integer.parseInt(subChildNode.getTextContent());
                    continue;
                }
                if (name.equals("VAPP2")) {
                    sequence.ChannelConfigSetting.Vapp2 =  Integer.parseInt(subChildNode.getTextContent());
                    continue;
                }
                if (name.equals("VAPP3")) {
                    sequence.ChannelConfigSetting.Vapp3 =  Integer.parseInt(subChildNode.getTextContent());
                    continue;
                }
                if (name.equals("VAPP4")) {
                    sequence.ChannelConfigSetting.Vapp4 =  Integer.parseInt(subChildNode.getTextContent());
                    continue;
                }
                if (name.equals("NumSamples")) {
                    sequence.ChannelConfigSetting.NumSamples = (byte) Integer.parseInt(subChildNode.getTextContent());
                    continue;
                }
                if (name.equals("Inputs2")) {
                    sequence.ChannelConfigSetting.Inputs2 = (byte) Integer.parseInt(subChildNode.getTextContent());
                    continue;
                }
            }
        }
        block.Sequences.add(sequence);
    }

    private void parseSensorLayout(Element root, TestConfiguration testConfiguration) {
        NodeList nodes = null;
        XPath xpath = XPathFactory.newInstance().newXPath();
        try {
            nodes = (NodeList) xpath.compile("//TestConfig/SensorLayout").evaluate(root, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
        }
        if (nodes != null) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node childNode = nodes.item(i);
                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element childElement = (Element) childNode;
                    if (childElement.getElementsByTagName("CardType").getLength() == 0) {
                        continue;
                    }
                    if (Integer.parseInt(childElement.getElementsByTagName("CardType").item(0).getTextContent()) != testConfiguration.CardType) {
                        continue;
                    }
                    NodeList nodes2 = childNode.getChildNodes();
                    SensorLayout layout = new SensorLayout();
                    for (int j = 0; j < nodes2.getLength(); j++) {
                        Node subChildNode = nodes2.item(j);
                        if (subChildNode.getNodeType() == Node.ELEMENT_NODE) {
                            String name = subChildNode.getNodeName();
                            if (name.equals("LayoutNumber")) {
                                layout.SensorLayoutNumber = Integer.parseInt(subChildNode.getTextContent());
                                continue;
                            }
                            if (name.equals("ChannelType")) {
                                layout.ChannelType = Byte.parseByte(subChildNode.getTextContent());
                                continue;
                            }
                            if (name.equals("SensorType")) {
                                layout.SensorObject.SensorType = Byte.parseByte(subChildNode.getTextContent());
                                continue;
                            }
                            if (name.equals("SensorSubType")) {
                                layout.SensorObject.SubSensorType = Byte.parseByte(subChildNode.getTextContent());
                                continue;
                            }
                            if (name.equals("FromDate")) {
                                layout.SensorObject.FromDate = com.epocal.util.DateUtil.CreateDate(subChildNode.getTextContent(), "EEE MMM dd HH:mm:ss z yyyy");
                                continue;
                            }
                        }
                    }
                    testConfiguration.TestConfigSetting.SensorLayouts.add(layout);
                    for (SensorDescriptor descriptor : testConfiguration.TestConfigSetting.SensorDescriptors) {
                        if ((descriptor.SensorType == layout.SensorObject.SensorType) && (descriptor.SubSensorType == layout.SensorObject.SubSensorType)) {
                            layout.SensorObject.Descriptor = descriptor;
                            break;
                        }
                    }
                }
            }
        }
    }

    private void parseSensorDescriptor(Element rootNode, TestConfiguration testConfiguration) {
        NodeList nodes = rootNode.getElementsByTagName("SensorInformation");
        for (int i = 0; i < nodes.getLength(); i++) {
            SensorDescriptor descriptor = new SensorDescriptor();
            Node childNodeInfo = nodes.item(i);
            NodeList nodes2 = childNodeInfo.getChildNodes();
            for (int j = 0; j < nodes2.getLength(); j++) {
                Node childNodeItem = nodes2.item(j);
                if (childNodeItem.getNodeType() == Node.ELEMENT_NODE) {
                    String name = childNodeItem.getNodeName();
                    if (name.equals("SensorType")) {
                        descriptor.SensorType = Byte.parseByte(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("SensorSubType")) {
                        descriptor.SubSensorType = Byte.parseByte(childNodeItem.getTextContent());
                        continue;
                    }

                    if (name.equals("CalDelimit")) {
                        setWindowStart(Double.parseDouble(childNodeItem.getTextContent()), WindowPhase.Calibration, descriptor);
                        continue;
                    }
                    if (name.equals("CalWindowSize")) {
                        setWindowSize(Double.parseDouble(childNodeItem.getTextContent()), WindowPhase.Calibration, descriptor);
                        continue;
                    }

                    if (name.equals("SampleDelimit")) {
                        setWindowStart(Double.parseDouble(childNodeItem.getTextContent()), WindowPhase.Sampling, descriptor);
                        continue;
                    }
                    if (name.equals("SampleWindowSize")) {
                        setWindowSize(Double.parseDouble(childNodeItem.getTextContent()), WindowPhase.Sampling, descriptor);
                        continue;
                    }

                    if (name.equals("PostDelimit")) {
                        setWindowStart(Double.parseDouble(childNodeItem.getTextContent()), WindowPhase.Post, descriptor);
                        continue;
                    }
                    if (name.equals("PostWindowSize")) {
                        setWindowSize(Double.parseDouble(childNodeItem.getTextContent()), WindowPhase.Post, descriptor);
                        continue;
                    }

                    if (name.equals("Extrapolation")) {
                        descriptor.Extrapolation = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("CalConcentration")) {
                        descriptor.CalConcentration = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("SlopeFactor")) {
                        descriptor.SlopeFactor = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }

                    if (name.equals("CalCurveWeight")) {
                        descriptor.CalCurveWeight = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("SampleCurveWeight")) {
                        descriptor.SampleCurveWeight = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("PostCurvatureWeight")) {
                        descriptor.PostCurvatureWeight = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("Offset")) {
                        descriptor.Offset = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }

                    if (name.equals("CalMeanLowQC")) {
                        setQualityControlLimit("MeanLowQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.CalibrationQC, descriptor);
                        continue;
                    }
                    if (name.equals("CalMeanHighQC")) {
                        setQualityControlLimit("MeanHighQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.CalibrationQC, descriptor);
                        continue;
                    }
                    if (name.equals("CalDriftLowQC")) {
                        setQualityControlLimit("DriftLowQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.CalibrationQC, descriptor);
                        continue;
                    }
                    if (name.equals("CalDriftHighQC")) {
                        setQualityControlLimit("DriftHighQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.CalibrationQC, descriptor);
                        continue;
                    }
                    if (name.equals("CalSecondLowQC")) {
                        setQualityControlLimit("SecondLowQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.CalibrationQC, descriptor);
                        continue;
                    }
                    if (name.equals("CalSecondHighQC")) {
                        setQualityControlLimit("SecondHighQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.CalibrationQC, descriptor);
                        continue;
                    }
                    if (name.equals("CalNoiseHighQC")) {
                        setQualityControlLimit("NoiseHighQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.CalibrationQC, descriptor);
                        continue;
                    }

                    if (name.equals("SampleMeanLowQC")) {
                        setQualityControlLimit("MeanLowQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.SamplingQC, descriptor);
                        continue;
                    }
                    if (name.equals("SampleMeanHighQC")) {
                        setQualityControlLimit("MeanHighQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.SamplingQC, descriptor);
                        continue;
                    }
                    if (name.equals("SampleDriftLowQC")) {
                        setQualityControlLimit("DriftLowQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.SamplingQC, descriptor);
                        continue;
                    }
                    if (name.equals("SampleDriftHighQC")) {
                        setQualityControlLimit("DriftHighQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.SamplingQC, descriptor);
                        continue;
                    }
                    if (name.equals("SampleSecondLowQC")) {
                        setQualityControlLimit("SecondLowQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.SamplingQC, descriptor);
                        continue;
                    }
                    if (name.equals("SampleSecondHighQC")) {
                        setQualityControlLimit("SecondHighQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.SamplingQC, descriptor);
                        continue;
                    }
                    if (name.equals("SampleNoiseHighQC")) {
                        setQualityControlLimit("NoiseHighQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.SamplingQC, descriptor);
                        continue;
                    }

                    if (name.equals("PostMeanLowQC")) {
                        setQualityControlLimit("MeanLowQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.PostQC, descriptor);
                        continue;
                    }
                    if (name.equals("PostMeanHighQC")) {
                        setQualityControlLimit("MeanHighQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.PostQC, descriptor);
                        continue;
                    }
                    if (name.equals("PostDriftLowQC")) {
                        setQualityControlLimit("DriftLowQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.PostQC, descriptor);
                        continue;
                    }
                    if (name.equals("PostDriftHighQC")) {
                        setQualityControlLimit("DriftHighQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.PostQC, descriptor);
                        continue;
                    }
                    if (name.equals("PostSecondLowQC")) {
                        setQualityControlLimit("SecondLowQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.PostQC, descriptor);
                        continue;
                    }
                    if (name.equals("PostSecondHighQC")) {
                        setQualityControlLimit("SecondHighQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.PostQC, descriptor);
                        continue;
                    }
                    if (name.equals("PostNoiseHighQC")) {
                        setQualityControlLimit("NoiseHighQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.PostQC, descriptor);
                        continue;
                    }

                    if (name.equals("DeltaDriftLowQC")) {
                        setQualityControlLimit("DriftLowQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.DeltaDriftQC, descriptor);
                        continue;
                    }
                    if (name.equals("DeltaDriftHighQC")) {
                        setQualityControlLimit("DriftHighQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.DeltaDriftQC, descriptor);
                        continue;
                    }

                    if (name.equals("ReaderMeanLow")) {
                        setQualityControlLimit("MeanLowQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.ReaderQC, descriptor);
                        continue;
                    }
                    if (name.equals("ReaderMeanHigh")) {
                        setQualityControlLimit("MeanHighQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.ReaderQC, descriptor);
                        continue;
                    }
                    if (name.equals("ReaderDriftLow")) {
                        setQualityControlLimit("DriftLowQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.ReaderQC, descriptor);
                        continue;
                    }
                    if (name.equals("ReaderDriftHigh")) {
                        setQualityControlLimit("DriftHighQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.ReaderQC, descriptor);
                        continue;
                    }
                    if (name.equals("ReaderNoiseLow")) {
                        setQualityControlLimit("NoiseLowQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.ReaderQC, descriptor);
                        continue;
                    }
                    if (name.equals("ReaderNoiseHigh")) {
                        setQualityControlLimit("NoiseHighQC", Float.parseFloat(childNodeItem.getTextContent()), QualityControlLimitPhase.ReaderQC, descriptor);
                        continue;
                    }

                    if (name.equals("tPlus")) {
                        descriptor.TPlus = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("tMinus")) {
                        descriptor.TMinus = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }

                    if (name.equals("BloodPointsToSkip")) {
                        descriptor.BloodPointsToSkip = Integer.parseInt(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("BloodPointsInWindow")) {
                        descriptor.BloodPointsInWindow = Integer.parseInt(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("BloodNoiseHigh")) {
                        descriptor.BloodNoiseHigh = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }

                    if (name.equals("AqPointsToSkip")) {
                        descriptor.AqPointsToSkip = Integer.parseInt(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("AqPointsInWindow")) {
                        descriptor.AqPointsInWindow = Integer.parseInt(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("AqNoiseHigh")) {
                        descriptor.AqNoiseHigh = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }

                    if (name.equals("LateBloodPointsToSkip")) {
                        descriptor.LateBloodPointsToSkip = Integer.parseInt(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("LateBloodPointsInWindow")) {
                        descriptor.LateBloodPointsInWindow = Integer.parseInt(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("LateBloodNoiseHigh")) {
                        descriptor.LateBloodNoiseHigh = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }

                    if (name.equals("LateAqPointsToSkip")) {
                        descriptor.LateAqPointsToSkip = Integer.parseInt(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("LateAqPointsInWindow")) {
                        descriptor.LateAqPointsInWindow = Integer.parseInt(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("LateAqNoiseHigh")) {
                        descriptor.LateAqNoiseHigh = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }

                    if (name.equals("RTPointLimitLow")) {
                        descriptor.RTPointLimitLow = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("RTPointLimitHigh")) {
                        descriptor.RTPointLimitHigh = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }

                    if (name.equals("D1Low")) {
                        descriptor.D1Low = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("D1High")) {
                        descriptor.D1High = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("P1D2Low")) {
                        descriptor.P1D2Low = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("P1D2High")) {
                        descriptor.P1D2High = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("P2D2Low")) {
                        descriptor.P2D2Low = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("P2D2High")) {
                        descriptor.P2D2High = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("P3D2Low")) {
                        descriptor.P3D2Low = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("P3D2High")) {
                        descriptor.P3D2High = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }

                    if (name.equals("A")) {
                        descriptor.AParam = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("B")) {
                        descriptor.BParam = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("C")) {
                        descriptor.CParam = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("D")) {
                        descriptor.DParam = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("F")) {
                        descriptor.FParam = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("G")) {
                        descriptor.GParam = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }

                    if (name.equals("TAmbOffset")) {
                        descriptor.AmbientTempOffset = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("InjectionTimeOffset")) {
                        descriptor.InjectionTimeOffset = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("AgeOffset")) {
                        descriptor.AgeOffset = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("PowerOffset")) {
                        descriptor.PowerOffset = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("NeuralNetBlood")) {
                        descriptor.NeuralNetBlood = childNodeItem.getTextContent();
                        continue;
                    }
                    if (name.equals("NeuralNetAQ")) {
                        descriptor.NeuralNetAQ = childNodeItem.getTextContent();
                        continue;
                    }
                    if (name.indexOf("param") == 0) {
                        int idx = Integer.parseInt(name.substring(5));
                        ExtraParameter extraParam = new ExtraParameter();
                        extraParam.extraParameterID = idx;
                        extraParam.extraParameterValue = Double.parseDouble(childNodeItem.getTextContent());
                        descriptor.ExtraParameters.add(extraParam);
                    }
                }
            }
            testConfiguration.TestConfigSetting.SensorDescriptors.add(descriptor);
        }
    }

    private void setWindowStart(double start, WindowPhase phase, SensorDescriptor descriptor) {
        for (Window window : descriptor.Windows) {
            if (window.Phase == phase.value) {
                window.Start = start;
                return;
            }
        }
        Window window = new Window();
        window.Phase = phase.value;
        window.Start = start;
        descriptor.Windows.add(window);
    }

    private void setWindowSize(double length, WindowPhase phase, SensorDescriptor descriptor) {
        for (Window window : descriptor.Windows) {
            if (window.Phase == phase.value) {
                window.Length = length;
                return;
            }
        }
        Window window = new Window();
        window.Phase = phase.value;
        window.Length = length;
        descriptor.Windows.add(window);
    }

    private void setQualityControlLimit(String name, float val, QualityControlLimitPhase phase, SensorDescriptor descriptor) {
        QualityControlLimit limitQC = null;
        for (QualityControlLimit limit : descriptor.QualityControlLimits) {
            if (limit.Phase == phase.value) {
                limitQC = limit;
                break;
            }
        }
        if (limitQC == null) {
            limitQC = new QualityControlLimit();
            limitQC.Phase = phase.value;
            descriptor.QualityControlLimits.add(limitQC);
        }

        if (name.equals("MeanLowQC")) {
            limitQC.MeanLowQC = val;
            return;
        }
        if (name.equals("MeanHighQC")) {
            limitQC.MeanHighQC = val;
            return;
        }
        if (name.equals("DriftLowQC")) {
            limitQC.DriftLowQC = val;
            return;
        }
        if (name.equals("DriftHighQC")) {
            limitQC.DriftHighQC = val;
            return;
        }
        if (name.equals("SecondLowQC")) {
            limitQC.SecondLowQC = val;
            return;
        }
        if (name.equals("SecondHighQC")) {
            limitQC.SecondHighQC = val;
            return;
        }
        if (name.equals("NoiseLowQC")) {
            limitQC.NoiseLowQC = val;
            return;
        }
        if (name.equals("NoiseHighQC")) {
            limitQC.NoiseHighQC = val;
            return;
        }
    }

    private void parseReportableRanges(Element root, TestConfiguration testConfiguration) {
        NodeList nodes = root.getElementsByTagName("ReportableRange");
        for (int i = 0; i < nodes.getLength(); i++) {
            ReportableRange range = new ReportableRange();
            Node childNodeInfo = nodes.item(i);
            NodeList nodes2 = childNodeInfo.getChildNodes();
            for (int j = 0; j < nodes2.getLength(); j++) {
                Node childNodeItem = nodes2.item(j);
                if (childNodeItem.getNodeType() == Node.ELEMENT_NODE) {
                    String name = childNodeItem.getNodeName();
                    if (name.equals("Analyte")) {
                        range.Analyte = Integer.parseInt(childNodeItem.getTextContent());
                        continue;
                    }

                    if (name.equals("Low")) {
                        range.Low = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("High")) {
                        range.High = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }

                    if (name.equals("DefaultUnits")) {
                        range.DefaultUnit = Integer.parseInt(childNodeItem.getTextContent());
                        continue;
                    }
                }
            }
            testConfiguration.ReportableRanges.add(range);
        }
    }

    private void parseInsanityRanges(Element root, TestConfiguration testConfiguration) {
        NodeList nodes = root.getElementsByTagName("InsanityRange");
        for (int i = 0; i < nodes.getLength(); i++) {
            InsanityRange range = new InsanityRange();
            Node childNodeInfo = nodes.item(i);
            NodeList nodes2 = childNodeInfo.getChildNodes();
            for (int j = 0; j < nodes2.getLength(); j++) {
                Node childNodeItem = nodes2.item(j);
                if (childNodeItem.getNodeType() == Node.ELEMENT_NODE) {
                    String name = childNodeItem.getNodeName();
                    if (name.equals("Analyte")) {
                        range.Analyte = Integer.parseInt(childNodeItem.getTextContent());
                        continue;
                    }

                    if (name.equals("Low")) {
                        range.Low = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("High")) {
                        range.High = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }

                    if (name.equals("DefaultUnits")) {
                        range.DefaultUnit = Integer.parseInt(childNodeItem.getTextContent());
                        continue;
                    }

                    if (name.equals("QALow")) {
                        range.QALow = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                    if (name.equals("QAHigh")) {
                        range.QAHigh = Double.parseDouble(childNodeItem.getTextContent());
                        continue;
                    }
                }
            }
            testConfiguration.InsanityRanges.add(range);
        }
    }

    private void parseRealTimeQC(Element root, TestConfiguration testConfiguration) {
        NodeList nodes = root.getElementsByTagName("RealTimeQC");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node childNode = nodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) childNode;
                if (childElement.getElementsByTagName("CardType").getLength() == 0) {
                    continue;
                }
                if (Integer.parseInt(childElement.getElementsByTagName("CardType").item(0).getTextContent()) != testConfiguration.CardType) {
                    continue;
                }
                RealTimeQc realTimeQC = new RealTimeQc();
                NodeList nodes2 = childNode.getChildNodes();
                for (int j = 0; j < nodes2.getLength(); j++) {
                    Node childNodeItem = nodes2.item(j);
                    if (childNodeItem.getNodeType() == Node.ELEMENT_NODE) {
                        String name = childNodeItem.getNodeName();
                        if (name.equals("Key")) {
                            realTimeQC.RealTimeQCKey = Integer.parseInt(childNodeItem.getTextContent());
                            continue;
                        }
                        if (name.equals("Enabled")) {
                            realTimeQC.Enabled = Boolean.parseBoolean(childNodeItem.getTextContent());
                            continue;
                        }
                        if (name.equals("StartTime")) {
                            realTimeQC.StartTime = Integer.parseInt(childNodeItem.getTextContent());
                            continue;
                        }
                        if (name.equals("Interval")) {
                            realTimeQC.Interval = Integer.parseInt(childNodeItem.getTextContent());
                            continue;
                        }
                        if (name.equals("Type")) {
                            realTimeQC.RealTimeQCType = Integer.parseInt(childNodeItem.getTextContent());
                            continue;
                        }
                        if (name.equals("NumPoints")) {
                            realTimeQC.NumPoints = Integer.parseInt(childNodeItem.getTextContent());
                            continue;
                        }
                        if (name.equals("HumidityDetectUntil")) {
                            realTimeQC.HumidityDetectUntil = Integer.parseInt(childNodeItem.getTextContent());
                            continue;
                        }

                        if (name.toLowerCase().indexOf("extra") == 0) {
                            int idx = Integer.parseInt(name.substring(5));
                            ExtraParameter extraParam = new ExtraParameter();
                            extraParam.extraParameterID = idx;
                            extraParam.extraParameterValue = Double.parseDouble(childNodeItem.getTextContent());
                            realTimeQC.ExtraParameters.add(extraParam);
                        }
                    }
                }
                testConfiguration.RealTimeQCSetting = realTimeQC;
                break;
            }
        }
    }

    private void parseHumidityDetect(Element root, TestConfiguration testConfiguration) {
        NodeList nodes = root.getElementsByTagName("HumidityDetect");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node childNode = nodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) childNode;
                if (childElement.getElementsByTagName("CardType").getLength() == 0) {
                    continue;
                }
                if (Integer.parseInt(childElement.getElementsByTagName("CardType").item(0).getTextContent()) != testConfiguration.CardType) {
                    continue;
                }
                HumidityDetect humidityDetect = new HumidityDetect();
                NodeList nodes2 = childNode.getChildNodes();
                for (int j = 0; j < nodes2.getLength(); j++) {
                    Node childNodeItem = nodes2.item(j);
                    if (childNodeItem.getNodeType() == Node.ELEMENT_NODE) {
                        String name = childNodeItem.getNodeName();
                        if (name.equals("SensorObject")) {
                            humidityDetect.SensorType = Integer.parseInt(childNodeItem.getTextContent());
                            continue;
                        }
                        if (name.equals("StartTime")) {
                            humidityDetect.StartTime = Double.parseDouble(childNodeItem.getTextContent());
                            continue;
                        }
                        if (name.equals("WindowSize")) {
                            humidityDetect.WindowSize = Double.parseDouble(childNodeItem.getTextContent());
                            continue;
                        }
                        if (name.equals("LowLimit")) {
                            humidityDetect.LowLimit = Double.parseDouble(childNodeItem.getTextContent());
                            continue;
                        }
                        if (name.equals("HighLimit")) {
                            humidityDetect.HighLimit = Double.parseDouble(childNodeItem.getTextContent());
                            continue;
                        }

                        if (name.toLowerCase().indexOf("extra") == 0) {
                            int idx = Integer.parseInt(name.substring(5));
                            ExtraParameter extraParam = new ExtraParameter();
                            extraParam.extraParameterID = idx;
                            extraParam.extraParameterValue = Double.parseDouble(childNodeItem.getTextContent());
                            humidityDetect.ExtraParameters.add(extraParam);
                        }
                    }
                }
                testConfiguration.HumidityDetects.add(humidityDetect);
            }
        }
    }

    private void parseLimitKey(Element root, TestConfiguration testConfiguration) {
        NodeList nodes = root.getElementsByTagName("LimitKeys");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node childNode = nodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                LimitKey limitKey = new LimitKey();
                NodeList nodes2 = childNode.getChildNodes();
                for (int j = 0; j < nodes2.getLength(); j++) {
                    Node childNodeItem = nodes2.item(j);
                    if (childNodeItem.getNodeType() == Node.ELEMENT_NODE) {
                        String name = childNodeItem.getNodeName();
                        if (name.equals("key")) {
                            limitKey.Key = Integer.parseInt(childNodeItem.getTextContent());
                            continue;
                        }
                        if (name.equals("Value")) {
                            limitKey.LimitKeyValue = Double.parseDouble(childNodeItem.getTextContent());
                            continue;
                        }
                    }
                }
                testConfiguration.LimitKeySet.add(limitKey);
            }
        }
    }

    private void parseChannelLimits(Element root, TestConfiguration testConfiguration) {
        NodeList nodes = root.getElementsByTagName("ChannelLimits");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node childNode = nodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) childNode;
                if (childElement.getElementsByTagName("CardType").getLength() == 0) {
                    continue;
                }
                if (Integer.parseInt(childElement.getElementsByTagName("CardType").item(0).getTextContent()) != testConfiguration.CardType) {
                    continue;
                }

                ChannelLimit channelLimit = new ChannelLimit();
                NodeList nodes2 = childNode.getChildNodes();
                for (int j = 0; j < nodes2.getLength(); j++) {
                    Node childNodeItem = nodes2.item(j);
                    if (childNodeItem.getNodeType() == Node.ELEMENT_NODE) {
                        String name = childNodeItem.getNodeName();
                        if (name.equals("Channel")) {
                            channelLimit.Channel = Integer.parseInt(childNodeItem.getTextContent());
                            continue;
                        }
                        if (name.equals("LimitKey")) {
                            channelLimit.LimitKey = Integer.parseInt(childNodeItem.getTextContent());
                            continue;
                        }
                    }
                }
                testConfiguration.ChannelLimitSet.add(channelLimit);
            }
        }
    }

    private void parseHostTestResultConfigSetting(Element root, TestConfiguration testConfiguration) {
        parseCardSetup(root, testConfiguration);
        parseCardTabSetup(root, testConfiguration);
    }

    private void parseCardSetup(Element root, TestConfiguration testConfiguration) {
        NodeList nodes = root.getElementsByTagName("CardSetup");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node childNode = nodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) childNode;
                if (childElement.getElementsByTagName("CardType").getLength() == 0) {
                    continue;
                }
                if (Integer.parseInt(childElement.getElementsByTagName("CardType").item(0).getTextContent()) != testConfiguration.CardType) {
                    continue;
                }

                CardSetup cardSetup = new CardSetup();
                NodeList nodes2 = childNode.getChildNodes();
                for (int j = 0; j < nodes2.getLength(); j++) {
                    Node childNodeItem = nodes2.item(j);
                    if (childNodeItem.getNodeType() == Node.ELEMENT_NODE) {
                        String name = childNodeItem.getNodeName();
                        if (name.equals("ResultType")) {
                            cardSetup.ResultType = Integer.parseInt(childNodeItem.getTextContent());
                            continue;
                        }
                        if (name.equals("Analyte")) {
                            cardSetup.Analyte = Integer.parseInt(childNodeItem.getTextContent());
                            continue;
                        }
                        if (name.equals("GlobalPosition")) {
                            cardSetup.GlobalPosition = Integer.parseInt(childNodeItem.getTextContent());
                            continue;
                        }
                        if (name.equals("PositionWithinTab")) {
                            cardSetup.PositionWithinTab = Integer.parseInt(childNodeItem.getTextContent());
                            continue;
                        }
                        if (name.equals("TabType")) {
                            cardSetup.TabType = Integer.parseInt(childNodeItem.getTextContent());
                            continue;
                        }
                    }
                }
                testConfiguration.HostTestResultConfigSetting.CardSetups.add(cardSetup);
            }
        }
    }

    private void parseCardTabSetup(Element root, TestConfiguration testConfiguration) {
        NodeList nodes = root.getElementsByTagName("CardTabsSetup");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node childNode = nodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) childNode;
                if (childElement.getElementsByTagName("CardType").getLength() == 0) {
                    continue;
                }
                if (Integer.parseInt(childElement.getElementsByTagName("CardType").item(0).getTextContent()) != testConfiguration.CardType) {
                    continue;
                }

                CardTabSetup cardTabSetup = new CardTabSetup();
                NodeList nodes2 = childNode.getChildNodes();
                for (int j = 0; j < nodes2.getLength(); j++) {
                    Node childNodeItem = nodes2.item(j);
                    if (childNodeItem.getNodeType() == Node.ELEMENT_NODE) {
                        String name = childNodeItem.getNodeName();
                        if (name.equals("TabType")) {
                            cardTabSetup.TabType = Integer.parseInt(childNodeItem.getTextContent());
                            continue;
                        }
                        if (name.equals("TabPosition")) {
                            cardTabSetup.TabPosition = Integer.parseInt(childNodeItem.getTextContent());
                            continue;
                        }
                    }
                }
                testConfiguration.HostTestResultConfigSetting.CardTabSetups.add(cardTabSetup);
            }
        }
    }
}

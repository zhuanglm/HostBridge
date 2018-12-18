package com.epocal.statemachine.xmlParser;

import com.epocal.statemachine.IState;
import com.epocal.statemachine.IStateEnum;
import com.epocal.statemachine.IStateMachine;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.EnumSet;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by dning on 5/16/2017.
 */

public abstract class SCXMLParser<P, S extends Enum<S>, E extends Enum<E>> {
    public enum XMLAttributeEnum {
        id {
            @Override
            public String toString() {
                return "id";
            }
        },
        root {
            @Override
            public String toString() {
                return "root";
            }
        },
        state {
            @Override
            public String toString() {
                return "state";
            }
        },
        singleton {
            @Override
            public String toString() {
                return "singleton";
            }
        },
        finalstate {
            @Override
            public String toString() {
                return "final";
            }
        },
        initial {
            @Override
            public String toString() {
                return "initial";
            }
        },
        transition {
            @Override
            public String toString() {
                return "transition";
            }
        },
        target {
            @Override
            public String toString() {
                return "target";
            }
        },
        event {
            @Override
            public String toString() {
                return "event";
            }
        },
        type {
            @Override
            public String toString() {
                return "type";
            }
        },
    }

    protected Map<IStateEnum, IState> mStateObject = null;

    /**
     * create state machine based on xml
     * @param xml
     * @param stateMachine
     * @param stateEnumSet
     * @param eventEnumSet
     * @return state machine instance
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public IStateMachine<P> parser(String xml, IStateMachine<P> stateMachine, EnumSet<S> stateEnumSet, EnumSet<E> eventEnumSet)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        InputSource input = new InputSource(new StringReader(xml));
        Document xmlDoc = builder.parse(input);

        parseXML(xmlDoc, stateMachine, stateEnumSet, eventEnumSet);
        return stateMachine;
    }

    /**
     * parse xml to generate state machine structure
     * @param xmlDoc
     * @param stateMachine
     * @param stateEnumSet
     * @param eventEnumSet
     * @return
     */
    protected abstract boolean parseXML(Document xmlDoc, IStateMachine<P> stateMachine, EnumSet<S> stateEnumSet, EnumSet<E> eventEnumSet);
}

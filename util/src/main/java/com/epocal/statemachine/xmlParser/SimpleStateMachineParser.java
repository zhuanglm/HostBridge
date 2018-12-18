package com.epocal.statemachine.xmlParser;

import com.epocal.util.EnumUtil;
import com.epocal.statemachine.IEventEnum;
import com.epocal.statemachine.IState;
import com.epocal.statemachine.IStateEnum;
import com.epocal.statemachine.IStateMachine;
import com.epocal.statemachine.State;
import com.epocal.statemachine.StateMachineStateEnum;
import com.epocal.statemachine.StateType;
import com.epocal.statemachine.Transition;
import com.epocal.statemachine.TransitionType;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.EnumSet;

import static com.epocal.statemachine.xmlParser.SCXMLParser.XMLAttributeEnum.initial;

/**
 * Created by dning on 5/16/2017.
 */

public class SimpleStateMachineParser<P, S extends Enum<S>, E extends Enum<E>> extends SCXMLParser<P, S, E> {
    private EnumSet<S> mGenericStateEnumSet;
    private EnumSet<E> mGenericEventEnumSet;

    @Override
    protected boolean parseXML(Document xmlDoc, IStateMachine<P> stateMachine, EnumSet<S> stateEnumSet, EnumSet<E> eventEnumSet) {
        mStateObject = stateMachine.getStateList();
        mGenericStateEnumSet = stateEnumSet;
        mGenericEventEnumSet = eventEnumSet;

        if (xmlDoc == null) {
            return false;
        }
        xmlDoc.getDocumentElement().normalize();
        NodeList xelements = xmlDoc.getElementsByTagName(XMLAttributeEnum.state.toString());
        if (xelements.getLength() == 0) {
            return false;
        }
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
        return true;
    }

    /**
     * to parse xml element to generate state object
     * @param xelement
     * @param parentState
     * @return
     */
    private IState parseState(Element xelement, IState parentState) {
        IState state;
        String attr = xelement.getAttribute(XMLAttributeEnum.id.toString());
        Enum<S> enumItem = EnumUtil.ofEx(mGenericStateEnumSet, attr);
        if (attr != null) {
            if (mStateObject.containsKey(enumItem)) {
                state = mStateObject.get(enumItem);
            } else {
                state = (IState) new State<P>();
            }
        } else {
            state = (IState) new State<P>();
        }
        state.setStateID((IStateEnum) enumItem);
        state.setParentState(parentState);

        if (xelement.hasChildNodes()) {
            NodeList n1 = xelement.getChildNodes();
            for (int isd = 0; isd < n1.getLength(); isd++) {
                if ((n1.item(isd)).getNodeName().equals(XMLAttributeEnum.transition.toString())) {
                    if (n1.item(isd).getNodeType() == Node.ELEMENT_NODE) {
                        Element childElement = (Element) n1.item(isd);
                        parseTransitions(childElement, state);
                    }
                } else if ((n1.item(isd)).getNodeName().equals(XMLAttributeEnum.finalstate.toString())) {
                    if (n1.item(isd).getNodeType() == Node.ELEMENT_NODE) {
                        Element childElement = (Element) n1.item(isd);
                        parseFinalState(childElement, state);
                    }
                } else if ((n1.item(isd)).getNodeName().equals(XMLAttributeEnum.state.toString())) {
                    if (n1.item(isd).getNodeType() == Node.ELEMENT_NODE) {
                        Element childElement = (Element) n1.item(isd);
                        state.addState(parseState(childElement, state));
                    }
                }
            }
            for (int isd = 0; isd < n1.getLength(); isd++) {
                if ((n1.item(isd)).getNodeName().equals(initial.toString())) {
                    if (n1.item(isd).getNodeType() == Node.ELEMENT_NODE) {
                        Element childElement = (Element) n1.item(isd);
                        parseInitialState(childElement, state);
                    }
                }
            }
        }
        return state;
    }

    /**
     * to parse xml element to generate singleton state object
     * @param xelement
     * @param state
     */
    private void parseSingletonState(Element xelement, IState state) {
        IState s = parseState(xelement, state);
        s.setStateType(StateType.Singleton);
        state.addState(s);
    }

    /**
     * to parse xml element to generate final state object
     * @param xelement
     * @param state
     */
    private void parseFinalState(Element xelement, IState state) {
        IState s = parseState(xelement, state);
        s.setStateType(StateType.FinalState);
        state.addState(s);
    }

    /**
     * to parse xml element to generate inital state
     * @param xelement
     * @param state
     */
    private void parseInitialState(Element xelement, IState state) {
        if (xelement.hasChildNodes()) {
            NodeList n1 = xelement.getChildNodes();
            for (int isd = 0; isd < n1.getLength(); isd++) {
                if ((n1.item(isd)).getNodeName().equals(XMLAttributeEnum.transition.toString())) {
                    if (n1.item(isd).getNodeType() == Node.ELEMENT_NODE) {
                        String target = ((Element) n1.item(isd)).getAttribute(XMLAttributeEnum.target.toString());
                        Enum<S> enumState = EnumUtil.ofEx(mGenericStateEnumSet, target);
                        state.setInitialState((IStateEnum) enumState);
                    }
                }
            }
        }
    }

    /**
     * to parse xml element to generate transition object for state
     * @param xelement
     * @param state
     */
    private void parseTransitions(Element xelement, IState state) {
        String target = xelement.getAttribute(XMLAttributeEnum.target.toString());
        String event = xelement.getAttribute(XMLAttributeEnum.event.toString());
        String type = xelement.getAttribute(XMLAttributeEnum.type.toString());

        try {
            Transition t = new Transition();
            t.setSourceState(state);
            Enum<S> enumState = EnumUtil.ofEx(mGenericStateEnumSet, target);
            t.setTargetStateID((IStateEnum) enumState);
            Enum<E> enumEvent = EnumUtil.ofEx(mGenericEventEnumSet, event);
            t.setEventID((IEventEnum) enumEvent);
            TransitionType enumType = (TransitionType) EnumUtil.ofEx(EnumSet.allOf(TransitionType.class), type);
            t.setTransitionType(enumType);

            state.addTransition((IEventEnum) enumEvent, t);
        } catch (Exception e) {
            return;
        }
    }
}




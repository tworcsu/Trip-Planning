package edu.csu2017sp314.DTR02.view.gui;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ItineraryLeg {
	ItineraryTuple start;
	ItineraryTuple finish;
	int sequence;
	int distance;
	String units;

	public ItineraryLeg(Node leg) {
		NodeList elements = leg.getChildNodes();
		for (int i = 0; i < elements.getLength(); i++) {
			Node current = elements.item(i);
			switch (current.getNodeName()) {
			case "sequence":
				sequence = Integer.parseInt(current.getTextContent());
				break;
			case "start":
				start = new ItineraryTuple(current);
				break;
			case "finish":
				finish = new ItineraryTuple(current);
				break;
			case "distance":
				distance = Integer.parseInt(current.getTextContent());
				break;
			case "units":
				units = current.getTextContent();
				break;
			}
		}
	}
}

package io.svdparser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Utils {
	static List<Element> getFirstOrderChildElementsByTagName(Element parent, String name) {
		List<Element> nodeList = new ArrayList<Element>();
		for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling())
			if (child.getNodeType() == Node.ELEMENT_NODE && name.equals(child.getNodeName()))
				nodeList.add((Element) child);
		return nodeList;
	}

	static Element getSingleFirstOrderChildElementByTagName(Element parent, String name) throws SvdParserException {
		List<Element> elements = Utils.getFirstOrderChildElementsByTagName(parent, name);
		if (elements.size() != 1)
			throw new SvdParserException("More than one " + name + " element in " + parent.getNodeName() + "!");
		return elements.get(0);
	}
}

/*
 * Copyright (C) Antonio Vázquez Blanco 2023
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.svdparser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This is a class containing miscellaneous SVD parsing functions and helpers.
 */
public class Utils {
	static List<Element> getFirstOrderChildElementsByTagName(Element parent, String name) {
		List<Element> nodeList = new ArrayList<>();
		for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling())
			if (child.getNodeType() == Node.ELEMENT_NODE && name.equals(child.getNodeName()))
				nodeList.add((Element) child);
		return nodeList;
	}

	static Element getSingleFirstOrderChildElementByTagName(Element parent, String name) throws SvdParserException {
		List<Element> elements = Utils.getFirstOrderChildElementsByTagName(parent, name);
		if (elements.size() == 0)
			return null;
		if (elements.size() > 1)
			throw new SvdParserException("More than one " + name + " element in " + parent.getNodeName() + "!");
		return elements.get(0);
	}

	static SvdPeripheral getPeripheralFromName(List<SvdPeripheral> periphs, String name) {
		if (periphs == null)
			return null;
		for (SvdPeripheral p : periphs)
			if (p.getName().equals(name))
				return p;
		return null;
	}
}

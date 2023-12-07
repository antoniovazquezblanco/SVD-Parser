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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class SvdDevice {

	private SvdCpu mCpu;
	private List<SvdPeripheral> mPeripherals;

	public static SvdDevice fromFile(File f) throws SAXException, IOException, ParserConfigurationException, SvdParserException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(f);
		return fromDocument(doc);
	}

	public static SvdDevice fromDocument(Document doc) throws SvdParserException {
		// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        doc.getDocumentElement().normalize();
        return fromElement(doc.getDocumentElement());        
	}
	
	public static SvdDevice fromElement(Element el) throws SvdParserException {
		if (!el.getNodeName().equals("device"))
			throw new SvdParserException("Cannot build an SvdDevice from a " + el.getNodeName() + " node!");

		// Parse CPU info
		Element cpuElement = Utils.getSingleFirstOrderChildElementByTagName(el, "cpu");
		SvdCpu cpu = SvdCpu.fromElement(cpuElement);

		// Parse peripherals info
		Element peripheralsElement = Utils.getSingleFirstOrderChildElementByTagName(el, "peripherals");
		List<SvdPeripheral> periphs = new ArrayList<SvdPeripheral>();
		for (Element e : Utils.getFirstOrderChildElementsByTagName(peripheralsElement, "peripheral"))
			periphs.add(SvdPeripheral.fromElement(e));
		
		// Return the new SVD device
		return new SvdDevice(cpu, periphs);
	}

	private SvdDevice(SvdCpu cpu, List<SvdPeripheral> periphs) {
		mCpu = cpu;
		mPeripherals = periphs;
	}

	public SvdCpu getCpu() {
		return mCpu;
	}
	
	public List<SvdPeripheral> getPeripherals() {
		return mPeripherals;
	}
}

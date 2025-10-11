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

/**
 * This class represents an SVD device. The element device provides the
 * outermost frame of the description in an SVD file. A device contains one or
 * more peripherals, but one CPU description.
 */
public class SvdDevice {
	private String mVendor;
	private String mVendorID;
	private String mName;
	private String mSeries;
	private String mVersion;
	private String mDescription;
	private String mLicenseText;
	private SvdCpu mCpu;
	private Integer mAddressUnitBits;
	private Integer mWidth;
	private List<SvdPeripheral> mPeripherals;

	/**
	 * Obtain a SvdDevice object directly from an SVD file.
	 * 
	 * @param f File to be read.
	 * @return SvdDevice object.
	 * @throws SAXException                 On XML parsing error.
	 * @throws IOException                  On file operation error.
	 * @throws ParserConfigurationException On XML parsing error.
	 * @throws SvdParserException           On a SVD format error.
	 */
	public static SvdDevice fromFile(File f)
			throws SAXException, IOException, ParserConfigurationException, SvdParserException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(f);
		return fromDocument(doc);
	}

	/**
	 * Obtain a SvdDevice object from a DOM document.
	 * 
	 * @param doc Document object.
	 * @return SvdDevice object.
	 * @throws SvdParserException On a SVD format error.
	 */
	public static SvdDevice fromDocument(Document doc) throws SvdParserException {
		// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();
		return fromElement(doc.getDocumentElement());
	}

	/**
	 * Obtain a SvdDevice object from a DOM element.
	 * 
	 * @param el Element object.
	 * @return SvdDevice object.
	 * @throws SvdParserException On a SVD format error.
	 */
	public static SvdDevice fromElement(Element el) throws SvdParserException {
		if (!el.getNodeName().equals("device"))
			throw new SvdParserException("Cannot build an SvdDevice from a " + el.getNodeName() + " node!");

		// Parse device info
		String vendor = null;
		Element vendorElement = Utils.getSingleFirstOrderChildElementByTagName(el, "vendor");
		if (vendorElement != null)
			vendor = vendorElement.getTextContent();

		String vendorID = null;
		Element vendorIDElement = Utils.getSingleFirstOrderChildElementByTagName(el, "vendorID");
		if (vendorIDElement != null)
			vendorID = vendorIDElement.getTextContent();

		String name = null;
		Element nameElement = Utils.getSingleFirstOrderChildElementByTagName(el, "name");
		if (nameElement != null)
			name = nameElement.getTextContent();

		String series = null;
		Element seriesElement = Utils.getSingleFirstOrderChildElementByTagName(el, "series");
		if (seriesElement != null)
			series = seriesElement.getTextContent();

		String version = null;
		Element versionElement = Utils.getSingleFirstOrderChildElementByTagName(el, "version");
		if (versionElement != null)
			version = versionElement.getTextContent();

		String description = null;
		Element descriptionElement = Utils.getSingleFirstOrderChildElementByTagName(el, "description");
		if (descriptionElement != null)
			description = descriptionElement.getTextContent();

		String licenseText = null;
		Element licenseTextElement = Utils.getSingleFirstOrderChildElementByTagName(el, "licenseText");
		if (licenseTextElement != null)
			licenseText = licenseTextElement.getTextContent();

		// Parse CPU info
		Element cpuElement = Utils.getSingleFirstOrderChildElementByTagName(el, "cpu");
		SvdCpu cpu = SvdCpu.fromElement(cpuElement);

		Integer addressUnitBits = null;
		Element addressUnitBitsElement = Utils.getSingleFirstOrderChildElementByTagName(el, "addressUnitBits");
		if (addressUnitBitsElement != null)
			addressUnitBits = Integer.decode(addressUnitBitsElement.getTextContent());

		Integer width = null;
		Element widthElement = Utils.getSingleFirstOrderChildElementByTagName(el, "width");
		if (widthElement != null)
			width = Integer.decode(widthElement.getTextContent());

		// Try to parse a size element
		Element sizeElement = Utils.getSingleFirstOrderChildElementByTagName(el, "size");
		Integer defaultSize = -1;
		if (sizeElement != null)
			defaultSize = Integer.decode(sizeElement.getTextContent());

		// Parse peripherals info
		Element peripheralsElement = Utils.getSingleFirstOrderChildElementByTagName(el, "peripherals");
		List<SvdPeripheral> periphs = new ArrayList<>();
		for (Element e : Utils.getFirstOrderChildElementsByTagName(peripheralsElement, "peripheral"))
			periphs.addAll(SvdPeripheral.fromElement(e, defaultSize, periphs));

		// Return the new SVD device
		return new SvdDevice(vendor, vendorID, name, series, version, description, licenseText, addressUnitBits, width,
				cpu, periphs);
	}

	private SvdDevice(String vendor, String vendorID, String name, String series, String version, String description,
			String licenseText, Integer addressUnitBits, Integer width, SvdCpu cpu, List<SvdPeripheral> periphs) {
		mVendor = vendor;
		mVendorID = vendorID;
		mName = name;
		mSeries = series;
		mVersion = version;
		mDescription = description;
		mLicenseText = licenseText;
		mAddressUnitBits = addressUnitBits;
		mWidth = width;
		mCpu = cpu;
		mPeripherals = periphs;
	}

	/**
	 * Get the vendor name.
	 *
	 * @return The vendor name.
	 */
	public String getVendor() {
		return mVendor;
	}

	/**
	 * Get the vendor ID.
	 *
	 * @return The vendor ID.
	 */
	public String getVendorID() {
		return mVendorID;
	}

	/**
	 * Get the device name.
	 *
	 * @return The device name.
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Get the device series.
	 *
	 * @return The device series.
	 */
	public String getSeries() {
		return mSeries;
	}

	/**
	 * Get the device version.
	 *
	 * @return The device version.
	 */
	public String getVersion() {
		return mVersion;
	}

	/**
	 * Get the device description.
	 *
	 * @return The device description.
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Get the license text.
	 *
	 * @return The license text.
	 */
	public String getLicenseText() {
		return mLicenseText;
	}

	/**
	 * Get the CPU portion of a device.
	 * 
	 * @return An SvdCpu object.
	 */
	public SvdCpu getCpu() {
		return mCpu;
	}

	/**
	 * Get the address unit bits.
	 *
	 * @return The address unit bits.
	 */
	public Integer getAddressUnitBits() {
		return mAddressUnitBits;
	}

	/**
	 * Get the bus width.
	 *
	 * @return The bus width.
	 */
	public Integer getWidth() {
		return mWidth;
	}

	/**
	 * Get the peripheral list of the device.
	 *
	 * @return A list of SvdPeripheral objects.
	 */
	public List<SvdPeripheral> getPeripherals() {
		return mPeripherals;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SvdDevice{\n");
		if (mVendor != null)
			sb.append(" vendor=\"" + mVendor + "\",\n");
		if (mVendorID != null)
			sb.append(" vendorID=\"" + mVendorID + "\",\n");
		if (mName != null)
			sb.append(" name=\"" + mName + "\",\n");
		if (mSeries != null)
			sb.append(" series=\"" + mSeries + "\",\n");
		if (mVersion != null)
			sb.append(" version=\"" + mVersion + "\",\n");
		if (mDescription != null)
			sb.append(" description=\"" + mDescription + "\",\n");
		if (mLicenseText != null)
			sb.append(" licenseText=\"" + mLicenseText + "\",\n");
		if (mAddressUnitBits != null)
			sb.append(" addressUnitBits=" + mAddressUnitBits + ",\n");
		if (mWidth != null)
			sb.append(" width=" + mWidth + ",\n");
		sb.append(" cpu=" + mCpu.toString() + ",\n");
		sb.append(" periphs=[\n");
		for (SvdPeripheral p : mPeripherals)
			sb.append("  " + p.toString() + ",\n");
		sb.append(" ]\n");
		sb.append("}");
		return sb.toString();
	}
}

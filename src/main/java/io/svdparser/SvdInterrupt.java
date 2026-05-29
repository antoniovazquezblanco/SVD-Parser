/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Antonio Vázquez Blanco 2023-2025
 */
package io.svdparser;

import org.w3c.dom.Element;

/**
 * This class represents an interrupt defined within a peripheral. Interrupt
 * entries associate an interrupt name and numeric value with the peripheral
 * that generates them.
 */
public class SvdInterrupt {
	private String mName;
	private String mDescription;
	private Integer mValue;

	/**
	 * Create an SvdInterrupt from a DOM element.
	 *
	 * @param el DOM element object.
	 * @return An SvdInterrupt object.
	 * @throws SvdParserException on SVD format errors.
	 */
	public static SvdInterrupt fromElement(Element el) throws SvdParserException {
		if (el == null)
			return null;

		if (!el.getNodeName().equals("interrupt"))
			throw new SvdParserException("Cannot build an SvdInterrupt from a " + el.getNodeName() + " node!");

		Element nameElement = Utils.getSingleFirstOrderChildElementByTagName(el, "name");
		String name = nameElement.getTextContent();

		String description = null;
		Element descriptionElement = Utils.getSingleFirstOrderChildElementByTagName(el, "description");
		if (descriptionElement != null)
			description = descriptionElement.getTextContent();

		Element valueElement = Utils.getSingleFirstOrderChildElementByTagName(el, "value");
		Integer value = Integer.decode(valueElement.getTextContent());

		return new SvdInterrupt(name, description, value);
	}

	private SvdInterrupt(String name, String description, Integer value) {
		mName = name;
		mDescription = description;
		mValue = value;
	}

	/**
	 * Get the interrupt name.
	 *
	 * @return The interrupt name.
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Get the interrupt description.
	 *
	 * @return The interrupt description, or null if not specified.
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Get the interrupt number.
	 *
	 * @return The interrupt number (position in the interrupt vector table).
	 */
	public Integer getValue() {
		return mValue;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SvdInterrupt{name=\"" + mName + "\"");
		if (mDescription != null)
			sb.append(", description=\"" + mDescription + "\"");
		sb.append(", value=" + mValue);
		sb.append("}");
		return sb.toString();
	}
}

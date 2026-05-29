/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Antonio Vázquez Blanco 2023-2025
 */
package io.svdparser;

import org.w3c.dom.Element;

/**
 * This class represents a single enumerated value within an enumeratedValues
 * container of a field. Each entry defines a symbolic name and description for
 * a specific numeric value that the field can hold.
 */
public class SvdEnumeratedValue {
	private String mName;
	private String mDescription;
	private Long mValue;
	private Boolean mIsDefault;

	/**
	 * Create an SvdEnumeratedValue from a DOM element.
	 *
	 * @param el DOM element object.
	 * @return An SvdEnumeratedValue object.
	 * @throws SvdParserException on SVD format errors.
	 */
	public static SvdEnumeratedValue fromElement(Element el) throws SvdParserException {
		if (el == null)
			return null;

		if (!el.getNodeName().equals("enumeratedValue"))
			throw new SvdParserException("Cannot build an SvdEnumeratedValue from a " + el.getNodeName() + " node!");

		Element nameElement = Utils.getSingleFirstOrderChildElementByTagName(el, "name");
		String name = nameElement.getTextContent();

		String description = null;
		Element descriptionElement = Utils.getSingleFirstOrderChildElementByTagName(el, "description");
		if (descriptionElement != null)
			description = descriptionElement.getTextContent();

		Long value = null;
		Element valueElement = Utils.getSingleFirstOrderChildElementByTagName(el, "value");
		if (valueElement != null)
			value = Long.decode(valueElement.getTextContent());

		Boolean isDefault = false;
		Element isDefaultElement = Utils.getSingleFirstOrderChildElementByTagName(el, "isDefault");
		if (isDefaultElement != null)
			isDefault = Boolean.parseBoolean(isDefaultElement.getTextContent());

		return new SvdEnumeratedValue(name, description, value, isDefault);
	}

	private SvdEnumeratedValue(String name, String description, Long value, Boolean isDefault) {
		mName = name;
		mDescription = description;
		mValue = value;
		mIsDefault = isDefault;
	}

	/**
	 * Get the enumerated value name.
	 *
	 * @return The symbolic name for this enumerated value.
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Get the enumerated value description.
	 *
	 * @return A human-readable description of this value.
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Get the numeric value.
	 *
	 * @return The numeric value, or null if not specified.
	 */
	public Long getValue() {
		return mValue;
	}

	/**
	 * Check whether this is the default enumerated value.
	 *
	 * @return True if this entry is the default catch-all value.
	 */
	public Boolean isDefault() {
		return mIsDefault;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SvdEnumeratedValue{name=\"" + mName + "\"");
		if (mDescription != null)
			sb.append(", description=\"" + mDescription + "\"");
		if (mValue != null)
			sb.append(", value=0x" + Long.toHexString(mValue));
		if (Boolean.TRUE.equals(mIsDefault))
			sb.append(", isDefault=true");
		sb.append("}");
		return sb.toString();
	}
}

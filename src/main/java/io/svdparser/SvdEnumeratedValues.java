/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Antonio Vázquez Blanco 2023-2026
 */
package io.svdparser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

/**
 * This class represents an {@code <enumeratedValues>} block within a field. A
 * single field may contain up to two such blocks: one for read access and one
 * for write access, distinguished by the {@link Usage} value. When
 * {@code <usage>} is absent the block applies to both read and write
 * ({@link Usage#READ_WRITE}).
 */
public class SvdEnumeratedValues {

	private String mName;
	private SvdEnumeratedValuesUsage mUsage;
	private List<SvdEnumeratedValue> mValues;

	/**
	 * Create an {@link SvdEnumeratedValues} from a DOM element.
	 *
	 * @param el DOM element object representing an {@code <enumeratedValues>} node.
	 * @return An {@link SvdEnumeratedValues} object.
	 * @throws SvdParserException on SVD format errors.
	 */
	public static SvdEnumeratedValues fromElement(Element el) throws SvdParserException {
		if (el == null)
			return null;

		if (!el.getNodeName().equals("enumeratedValues"))
			throw new SvdParserException("Cannot build an SvdEnumeratedValues from a " + el.getNodeName() + " node!");

		String name = null;
		Element nameElement = Utils.getSingleFirstOrderChildElementByTagName(el, "name");
		if (nameElement != null)
			name = nameElement.getTextContent();

		SvdEnumeratedValuesUsage usage = SvdEnumeratedValuesUsage.READ_WRITE;
		Element usageElement = Utils.getSingleFirstOrderChildElementByTagName(el, "usage");
		if (usageElement != null)
			usage = SvdEnumeratedValuesUsage.fromString(usageElement.getTextContent());

		List<SvdEnumeratedValue> values = new ArrayList<>();
		for (Element valueElement : Utils.getFirstOrderChildElementsByTagName(el, "enumeratedValue"))
			values.add(SvdEnumeratedValue.fromElement(valueElement));

		return new SvdEnumeratedValues(name, usage, values);
	}

	private SvdEnumeratedValues(String name, SvdEnumeratedValuesUsage usage, List<SvdEnumeratedValue> values) {
		mName = name;
		mUsage = usage;
		mValues = values;
	}

	/**
	 * Get the optional name for this enumeration group.
	 *
	 * @return The group name, or {@code null} if not specified.
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Get the usage for this enumeration group.
	 *
	 * @return The {@link SvdEnumeratedValuesUsage} value; never {@code null}.
	 */
	public SvdEnumeratedValuesUsage getUsage() {
		return mUsage;
	}

	/**
	 * Get the list of enumerated values in this group.
	 *
	 * @return A non-null list of {@link SvdEnumeratedValue} objects.
	 */
	public List<SvdEnumeratedValue> getValues() {
		return mValues;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SvdEnumeratedValues{usage=\"" + mUsage.getSvdValue() + "\"");
		if (mName != null)
			sb.append(", name=\"" + mName + "\"");
		sb.append(", values=[");
		for (SvdEnumeratedValue ev : mValues)
			sb.append(ev.toString() + ",");
		sb.append("]}");
		return sb.toString();
	}
}

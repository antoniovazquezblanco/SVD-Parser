/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Antonio Vázquez Blanco 2023-2025
 */
package io.svdparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;

/**
 * This class represents a field within a register.
 */
public class SvdField {
	private static final Pattern BIT_RANGE_PATTERN = Pattern.compile("\\[(\\d+):(\\d+)\\]");

	private String mName;
	private String mDescription;
	private Integer mBitOffset;
	private Integer mBitWidth;

	/**
	 * Create an SvdField from a DOM element.
	 *
	 * @param el DOM element object.
	 * @return A SvdField object.
	 * @throws SvdParserException on SVD format errors.
	 */
	public static SvdField fromElement(Element el) throws SvdParserException {
		// Element null check
		if (el == null)
			return null;

		// XML node name check
		if (!el.getNodeName().equals("field"))
			throw new SvdParserException("Cannot build an SvdField from a " + el.getNodeName() + " node!");

		// Get name
		Element nameElement = Utils.getSingleFirstOrderChildElementByTagName(el, "name");
		String name = nameElement.getTextContent();

		// Get description
		String description = null;
		Element descriptionElement = Utils.getSingleFirstOrderChildElementByTagName(el, "description");
		if (descriptionElement != null)
			description = descriptionElement.getTextContent();

		// Bit positioning can be specified in three ways: bitRangeLsbMsbStyle,
		// bitRangeOffsetWidthStyle, bitRangePattern
		Integer bitOffset = null;
		Integer bitWidth = null;

		// Try to parse bitRangeLsbMsbStyle
		Element bitOffsetElement = Utils.getSingleFirstOrderChildElementByTagName(el, "bitOffset");
		Element bitWidthElement = Utils.getSingleFirstOrderChildElementByTagName(el, "bitWidth");
		if (bitOffsetElement != null && bitWidthElement != null) {
			bitOffset = Integer.decode(bitOffsetElement.getTextContent());
			bitWidth = Integer.decode(bitWidthElement.getTextContent());
		}

		// Try to parse bitRangeLsbMsbStyle
		if (bitOffset == null || bitWidth == null) {
			Element lsbElement = Utils.getSingleFirstOrderChildElementByTagName(el, "lsb");
			Element msbElement = Utils.getSingleFirstOrderChildElementByTagName(el, "msb");
			if (lsbElement != null && msbElement != null) {
				int lsb = Integer.decode(lsbElement.getTextContent());
				int msb = Integer.decode(msbElement.getTextContent());
				bitOffset = lsb;
				bitWidth = msb - lsb + 1;
			}
		}

		// Try to parse bitRangePattern
		if (bitOffset == null || bitWidth == null) {
			Element bitRangeElement = Utils.getSingleFirstOrderChildElementByTagName(el, "bitRange");
			if (bitRangeElement != null) {
				String bitRangeStr = bitRangeElement.getTextContent();
				if (bitRangeStr != null) {
					Matcher matcher = BIT_RANGE_PATTERN.matcher(bitRangeStr);
					if (matcher.matches()) {
						int msb = Integer.parseInt(matcher.group(1));
						int lsb = Integer.parseInt(matcher.group(2));
						bitOffset = lsb;
						bitWidth = msb - lsb + 1;
					}
				}
			}
		}

		return new SvdField(name, description, bitOffset, bitWidth);
	}

	private SvdField(String name, String description, Integer bitOffset, Integer bitWidth) {
		mName = name;
		mDescription = description;
		mBitOffset = bitOffset;
		mBitWidth = bitWidth;
	}

	/**
	 * Get the field name.
	 *
	 * @return The field name.
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Get the field description.
	 *
	 * @return The field description.
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Get the bit offset of the field.
	 *
	 * @return The bit offset, or null if not specified.
	 */
	public Integer getBitOffset() {
		return mBitOffset;
	}

	/**
	 * Get the bit width of the field.
	 *
	 * @return The bit width, or null if not specified.
	 */
	public Integer getBitWidth() {
		return mBitWidth;
	}

	/**
	 * Get the least significant bit position of the field.
	 *
	 * @return The LSB position, or null if not specified.
	 */
	public Integer getLsb() {
		return mBitOffset;
	}

	/**
	 * Get the most significant bit position of the field.
	 *
	 * @return The MSB position, or null if not specified.
	 */
	public Integer getMsb() {
		if (mBitOffset == null || mBitWidth == null)
			return null;
		return mBitOffset + mBitWidth - 1;
	}

	/**
	 * Get the bit range string of the field.
	 *
	 * @return The bit range string (e.g., "[7:0]"), or null if not specified.
	 */
	public String getBitRange() {
		if (mBitOffset == null || mBitWidth == null)
			return null;
		int msb = getMsb();
		int lsb = getLsb();
		return "[" + msb + ":" + lsb + "]";
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SvdField{name=\"" + mName + "\"");
		if (mDescription != null)
			sb.append(", description=\"" + mDescription + "\"");
		if (mBitOffset != null) {
			sb.append(", bitOffset=" + mBitOffset);
			if (mBitWidth != null)
				sb.append(", bitWidth=" + mBitWidth);
		}
		sb.append("}");
		return sb.toString();
	}
}

/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Antonio Vázquez Blanco 2023-2026
 */
package io.svdparser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

/**
 * This class represents a register of a device peripheral.
 */
public class SvdRegister {
	private String mName;
	private String mDescription;
	private Integer mSize;
	private Integer mOffset;
	private SvdAccess mAccess;
	private List<SvdField> mFields;

	/**
	 * Create an SvdRegister from a DOM element.
	 *
	 * @param el            DOM element object.
	 * @param defaultSize   Default register size inherited from the parent.
	 * @param defaultAccess Default access mode inherited from the parent.
	 * @return A list of SvdRegister objects (more than one when dim > 1).
	 * @throws SvdParserException on SVD format errors.
	 */
	public static List<SvdRegister> fromElement(Element el, Integer defaultSize, SvdAccess defaultAccess)
			throws SvdParserException {
		return fromElement(el, defaultSize, defaultAccess, 0, "");
	}

	/**
	 * Create an SvdRegister from a DOM element, adjusting address and name for
	 * cluster membership.
	 *
	 * @param el            DOM element object.
	 * @param defaultSize   Default register size inherited from the parent.
	 * @param defaultAccess Default access mode inherited from the parent.
	 * @param baseOffset    Address offset already accumulated from enclosing
	 *                      clusters.
	 * @param namePrefix    Name prefix to prepend (e.g. "CH_" for a cluster named
	 *                      CH).
	 * @return A list of SvdRegister objects (more than one when dim > 1).
	 * @throws SvdParserException on SVD format errors.
	 */
	static List<SvdRegister> fromElement(Element el, Integer defaultSize, SvdAccess defaultAccess, int baseOffset,
			String namePrefix) throws SvdParserException {
		// Element null check
		if (el == null)
			return null;

		// XML node name check
		if (!el.getNodeName().equals("register"))
			throw new SvdParserException("Cannot build an SvdRegister from a " + el.getNodeName() + " node!");

		// Parse dim elements
		Element dimElement = Utils.getSingleFirstOrderChildElementByTagName(el, "dim");
		Integer dim = (dimElement != null) ? Integer.decode(dimElement.getTextContent()) : 1;
		Element dimIncrementElement = Utils.getSingleFirstOrderChildElementByTagName(el, "dimIncrement");
		Integer dimIncrement = (dimIncrementElement != null) ? Integer.decode(dimIncrementElement.getTextContent()) : 0;

		// Get a name
		Element nameElement = Utils.getSingleFirstOrderChildElementByTagName(el, "name");
		String name = nameElement.getTextContent();

		// Get a description
		Element descriptionElement = Utils.getSingleFirstOrderChildElementByTagName(el, "description");
		String description = (descriptionElement != null) ? descriptionElement.getTextContent() : null;

		// Get the size
		Element sizeElement = Utils.getSingleFirstOrderChildElementByTagName(el, "size");
		if (sizeElement != null)
			defaultSize = Integer.decode(sizeElement.getTextContent());

		// Get the offset
		Element offsetElement = Utils.getSingleFirstOrderChildElementByTagName(el, "addressOffset");
		Integer offset = Integer.decode(offsetElement.getTextContent());

		// Parse access (register-level overrides inherited default)
		SvdAccess access = defaultAccess;
		Element accessElement = Utils.getSingleFirstOrderChildElementByTagName(el, "access");
		if (accessElement != null)
			access = SvdAccess.fromString(accessElement.getTextContent());

		// Parse fields
		List<SvdField> fields = new ArrayList<SvdField>();
		Element fieldsElement = Utils.getSingleFirstOrderChildElementByTagName(el, "fields");
		if (fieldsElement != null) {
			for (Element e : Utils.getFirstOrderChildElementsByTagName(fieldsElement, "field")) {
				fields.add(SvdField.fromElement(e, access));
			}
		}

		ArrayList<SvdRegister> regs = new ArrayList<SvdRegister>();
		for (Integer i = 0; i < dim; i++) {
			Integer addrIncrement = i * dimIncrement;
			String regName = namePrefix + name.formatted(String.valueOf(i));
			regs.add(new SvdRegister(regName, description, defaultSize, baseOffset + offset + addrIncrement, access,
					fields));
		}
		return regs;
	}

	private SvdRegister(String name, String description, int size, int offset, SvdAccess access,
			List<SvdField> fields) {
		mName = name;
		mDescription = description;
		mSize = size;
		mOffset = offset;
		mAccess = access;
		mFields = fields;
	}

	/**
	 * Get the register name.
	 * 
	 * @return A string representing a register name.
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Get the register description.
	 * 
	 * @return A string containing the register description.
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Get the register size.
	 * 
	 * @return The size of the register.
	 */
	public Integer getSize() {
		return mSize;
	}

	/**
	 * Get the register offset.
	 *
	 * @return The offset of the register.
	 */
	public Integer getOffset() {
		return mOffset;
	}

	/**
	 * Get the access permission for this register.
	 *
	 * @return The access permission for this register, or null if not specified.
	 */
	public SvdAccess getAccess() {
		return mAccess;
	}

	/**
	 * Get the fields in this register.
	 *
	 * @return List of fields.
	 */
	public List<SvdField> getFields() {
		return mFields;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SvdRegister{name=\"" + mName + "\"");
		if (mDescription != null)
			sb.append(", description=\"" + mDescription + "\"");
		if (mSize != null)
			sb.append(", size=" + mSize);
		if (mOffset != null)
			sb.append(", offset=0x" + Integer.toHexString(mOffset));
		if (mAccess != null)
			sb.append(", access=\"" + mAccess + "\"");
		if (mFields != null && !mFields.isEmpty()) {
			sb.append(", fields=[");
			for (SvdField f : mFields) {
				sb.append(f.toString() + ", ");
			}
			sb.append("]");
		}
		sb.append("}");
		return sb.toString();
	}
}

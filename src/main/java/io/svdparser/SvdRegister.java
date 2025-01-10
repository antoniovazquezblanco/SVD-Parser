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

import org.w3c.dom.Element;

/**
 * This class represents a register of a device peripheral.
 */
public class SvdRegister {

	private String mName;
	private String mDescription;
	private Integer mSize;
	private Integer mOffset;

	/**
	 * Create an SvdRegister from a DOM element.
	 * 
	 * @param el          DOM element object.
	 * @param defaultSize Default register size to inherit.
	 * @return A SvdRegister object.
	 * @throws SvdParserException on SVD format errors.
	 */
	public static SvdRegister fromElement(Element el, Integer defaultSize) throws SvdParserException {
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

		return new SvdRegister(name, description, defaultSize, offset);
	}

	private SvdRegister(String name, String description, int size, int offset) {
		mName = name;
		mDescription = description;
		mSize = size;
		mOffset = offset;
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

	public String toString() {
		return "SvdRegister{name=" + mName + "}";
	}
}

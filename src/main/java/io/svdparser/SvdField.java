/*
 * Copyright (C) Antonio Vázquez Blanco 2025
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
 * This class represents a field within a register.
 */
public class SvdField {
	private String mName;
	private String mDescription;

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

		return new SvdField(name, description);
	}

	private SvdField(String name, String description) {
		mName = name;
		mDescription = description;
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

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SvdField{name=\"" + mName + "\"");
		if (mDescription != null)
			sb.append(", description=\"" + mDescription + "\"");
		sb.append("}");
		return sb.toString();
	}
}
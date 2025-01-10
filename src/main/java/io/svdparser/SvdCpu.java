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
 * This represents the CPU section in a SVD file. The CPU section describes the
 * processor included in the microcontroller device. This section is mandatory
 * if the SVD file is used to generate the device header file.
 */
public class SvdCpu {
	/**
	 * Construct a CPU object from a DOM element.
	 *
	 * @param el DOM element.
	 * @return A SvdCpu object.
	 * @throws SvdParserException when the element does not correspond to a CPU.
	 */
	public static SvdCpu fromElement(Element el) throws SvdParserException {
		if (el == null)
			return new SvdCpu();
		if (!el.getNodeName().equals("cpu"))
			throw new SvdParserException("Cannot build an SvdCpu from a " + el.getNodeName() + " node!");
		return new SvdCpu();
	}

	private SvdCpu() {

	}

	@Override
	public String toString() {
		return "SvdCpu{}";
	}
}

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
 * A class representing a CMSIS SVD Address Block element. This object specifies
 * an address range uniquely mapped to a peripheral. A peripheral must have at
 * least one address block. However, if a peripheral is derived form another
 * peripheral, the address block is not mandatory.
 *
 * @see <a href=
 *      "https://www.keil.com/pack/doc/CMSIS/SVD/html/elem_peripherals.html#elem_addressBlock">CMSIS
 *      SVD Address Block documentation</a> for more information.
 */
public class SvdAddressBlock {
	private Long mOffset;
	private Long mSize;
	private String mUsage;

	/**
	 * Construct an address block from a DOM element.
	 *
	 * @param el DOM element.
	 * @return A SvdAddressBlock object.
	 * @throws SvdParserException when the element does not correspond to an address
	 *                            block.
	 */
	public static SvdAddressBlock fromElement(Element el) throws SvdParserException {
		if (!el.getNodeName().equals("addressBlock"))
			throw new SvdParserException("Cannot build an SvdAddressBlock from a " + el.getNodeName() + " node!");

		// Offset
		Element offsetElement = Utils.getSingleFirstOrderChildElementByTagName(el, "offset");
		Long offset = Long.decode(offsetElement.getTextContent());

		// Size
		Element sizeElement = Utils.getSingleFirstOrderChildElementByTagName(el, "size");
		Long size = Long.decode(sizeElement.getTextContent());

		// Usage
		Element usageElement = Utils.getSingleFirstOrderChildElementByTagName(el, "usage");
		String usage = usageElement.getTextContent();

		return new SvdAddressBlock(offset, size, usage);
	}

	private SvdAddressBlock(Long offset, Long size, String usage) {
		mOffset = offset;
		mSize = size;
		mUsage = usage;
	}

	/**
	 * Returns address block usage description. The following predefined values can
	 * be used:
	 * <ul>
	 * <li>registers</li>
	 * <li>buffer</li>
	 * <li>reserved</li>
	 * </ul>
	 *
	 * @return Address block usage description.
	 */
	public String getUsage() {
		return mUsage;
	}

	/**
	 * Returns the start address of an address block relative to the peripheral
	 * baseAddress.
	 *
	 * @return Offset of the address block.
	 */
	public Long getOffset() {
		return mOffset;
	}

	/**
	 * Returns the number of addressUnitBits being covered by this address block.
	 * The end address of an address block results from the sum of baseAddress,
	 * offset, and (size - 1).
	 *
	 * @return Size of the address block.
	 */
	public Long getSize() {
		return mSize;
	}
}

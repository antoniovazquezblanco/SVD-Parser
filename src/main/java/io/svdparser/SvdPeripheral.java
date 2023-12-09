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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

/**
 * This class represents a device peripheral.
 */
public class SvdPeripheral {
	private String mName;
	private Long mBaseAddr;
	private List<SvdAddressBlock> mAddressBlocks;

	/**
	 * Create an SvdPeripheral from a DOM element.
	 * 
	 * @param el DOM element object.
	 * @return A SvdPeripheral peripheral object.
	 * @throws SvdParserException on SVD format errors.
	 */
	public static SvdPeripheral fromElement(Element el) throws SvdParserException {
		// Get a name
		Element nameElement = Utils.getSingleFirstOrderChildElementByTagName(el, "name");
		String name = nameElement.getTextContent();

		// Get the base addr
		Element baseAddrElement = Utils.getSingleFirstOrderChildElementByTagName(el, "baseAddress");
		Long baseAddr = Long.decode(baseAddrElement.getTextContent());

		// Parse address blocks
		List<SvdAddressBlock> addressBlocks = new ArrayList<>();
		for (Element e : Utils.getFirstOrderChildElementsByTagName(el, "addressBlock"))
			addressBlocks.add(SvdAddressBlock.fromElement(e));

		return new SvdPeripheral(name, baseAddr, addressBlocks);
	}

	private SvdPeripheral(String name, Long baseAddr, List<SvdAddressBlock> addressBlocks) {
		mName = name;
		mBaseAddr = baseAddr;
		mAddressBlocks = addressBlocks;
	}

	/**
	 * Get the peripheral name.
	 * 
	 * @return A string representing a peripheral name.
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Get the peripheral base address.
	 * 
	 * @return The base address of the peripheral.
	 */
	public Long getBaseAddr() {
		return mBaseAddr;
	}

	/**
	 * Get a list of address blocks that the peripheral contains.
	 * 
	 * @return A list of SvdAddressBlock objects.
	 */
	public List<SvdAddressBlock> getAddressBlocks() {
		return mAddressBlocks;
	}

	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + "[name=" + mName + ", baseAddr=0x"
				+ Long.toHexString(mBaseAddr) + "]";
	}
}

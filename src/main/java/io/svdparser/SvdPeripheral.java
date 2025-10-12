/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Antonio Vázquez Blanco 2023-2025
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
	private String mVersion;
	private String mDescription;
	private String mGroupName;
	private Long mBaseAddr;
	private List<SvdAddressBlock> mAddressBlocks;
	private List<SvdRegister> mRegisters;

	/**
	 * Create an SvdPeripheral from a DOM element.
	 * 
	 * @param el           DOM element object.
	 * @param defaultSize  Default register size to inherit.
	 * @param otherPeriphs Peripherals to search for peripheral derivation.
	 * @return A SvdPeripheral peripheral object.
	 * @throws SvdParserException on SVD format errors.
	 */
	public static ArrayList<SvdPeripheral> fromElement(Element el, int defaultSize, List<SvdPeripheral> otherPeriphs)
			throws SvdParserException {
		// Element null check
		if (el == null)
			return null;

		// XML node name check
		if (!el.getNodeName().equals("peripheral"))
			throw new SvdParserException("Cannot build an SvdPeripheral from a " + el.getNodeName() + " node!");

		// Get a name. The name is parsed first to be able to provide better error msg
		// on derivedFrom parsing...
		Element nameElement = Utils.getSingleFirstOrderChildElementByTagName(el, "name");
		String name = nameElement.getTextContent();

		// Check if the peripheral derives from any other...
		SvdPeripheral derivedFrom = null;
		String derivedFromName = el.getAttribute("derivedFrom");
		if (derivedFromName != null && !derivedFromName.equals("")) {
			derivedFrom = Utils.getPeripheralFromName(otherPeriphs, derivedFromName);
			if (derivedFrom == null)
				throw new SvdParserException(
						"Cannot find peripheral " + derivedFromName + " to derive " + name + " from...");
		}

		// Parse dim elements
		Element dimElement = Utils.getSingleFirstOrderChildElementByTagName(el, "dim");
		Integer dim = (dimElement != null) ? Integer.decode(dimElement.getTextContent()) : 1;
		Element dimIncrementElement = Utils.getSingleFirstOrderChildElementByTagName(el, "dimIncrement");
		Integer dimIncrement = (dimIncrementElement != null) ? Integer.decode(dimIncrementElement.getTextContent()) : 0;

		// Get version
		String version = null;
		Element versionElement = Utils.getSingleFirstOrderChildElementByTagName(el, "version");
		if (versionElement != null)
			version = versionElement.getTextContent();

		// Get description
		String description = null;
		Element descriptionElement = Utils.getSingleFirstOrderChildElementByTagName(el, "description");
		if (descriptionElement != null)
			description = descriptionElement.getTextContent();

		// Get group name
		String groupName = null;
		Element groupNameElement = Utils.getSingleFirstOrderChildElementByTagName(el, "groupName");
		if (groupNameElement != null)
			groupName = groupNameElement.getTextContent();

		// Get the base addr
		Element baseAddrElement = Utils.getSingleFirstOrderChildElementByTagName(el, "baseAddress");
		Long baseAddr = Long.decode(baseAddrElement.getTextContent());

		// Try to parse a size element
		Element sizeElement = Utils.getSingleFirstOrderChildElementByTagName(el, "size");
		if (sizeElement != null)
			defaultSize = Integer.decode(sizeElement.getTextContent());

		// Parse address blocks
		List<SvdAddressBlock> addressBlocks = new ArrayList<>();
		for (Element e : Utils.getFirstOrderChildElementsByTagName(el, "addressBlock"))
			addressBlocks.add(SvdAddressBlock.fromElement(e));

		// Parse registers
		List<SvdRegister> registers = new ArrayList<>();
		Element registersElement = Utils.getSingleFirstOrderChildElementByTagName(el, "registers");
		if (registersElement != null)
			for (Element e : Utils.getFirstOrderChildElementsByTagName(registersElement, "register"))
				registers.addAll(SvdRegister.fromElement(e, defaultSize));

		ArrayList<SvdPeripheral> periph = new ArrayList<SvdPeripheral>();
		for (Integer i = 0; i < dim; i++) {
			Integer addrIncrement = i * dimIncrement;
			String periphName = name.formatted(String.valueOf(i));
			periph.add(new SvdPeripheral(derivedFrom, periphName, version, description, groupName,
					baseAddr + addrIncrement, addressBlocks, registers));
		}
		return periph;
	}

	private SvdPeripheral(SvdPeripheral derivedFrom, String name, String version, String description, String groupName,
			Long baseAddr, List<SvdAddressBlock> addressBlocks, List<SvdRegister> registers) {
		mName = name;
		mVersion = version;
		mDescription = description;
		mGroupName = groupName;
		mBaseAddr = baseAddr;
		mAddressBlocks = new ArrayList<SvdAddressBlock>();
		mRegisters = new ArrayList<SvdRegister>();
		if (derivedFrom != null) {
			mVersion = (mVersion != null) ? mVersion : derivedFrom.getVersion();
			mDescription = (mDescription != null) ? mDescription : derivedFrom.getDescription();
			mGroupName = (mGroupName != null) ? mGroupName : derivedFrom.getGroupName();
			mAddressBlocks.addAll(derivedFrom.getAddressBlocks());
			mRegisters.addAll(derivedFrom.getRegisters());
		}
		mAddressBlocks.addAll(addressBlocks);
		mRegisters.addAll(registers);
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
	 * Get the peripheral version.
	 *
	 * @return The peripheral version.
	 */
	public String getVersion() {
		return mVersion;
	}

	/**
	 * Get the peripheral description.
	 *
	 * @return The peripheral description.
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Get the peripheral group name.
	 *
	 * @return The peripheral group name.
	 */
	public String getGroupName() {
		return mGroupName;
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

	/**
	 * Get a list of registers that the peripheral contains.
	 * 
	 * @return A list of SvdRegister objects.
	 */
	public List<SvdRegister> getRegisters() {
		return mRegisters;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SvdPeripheral{");
		sb.append("name=\"" + mName + "\"");
		if (mVersion != null)
			sb.append(", version=\"" + mVersion + "\"");
		if (mDescription != null)
			sb.append(", description=\"" + mDescription + "\"");
		if (mGroupName != null)
			sb.append(", groupName=\"" + mGroupName + "\"");
		sb.append(", baseAddr=0x" + Long.toHexString(mBaseAddr));
		sb.append(", regs=[");
		for (SvdRegister r : mRegisters)
			sb.append(r.toString() + ",");
		sb.append("]");
		sb.append("}");
		return sb.toString();
	}
}

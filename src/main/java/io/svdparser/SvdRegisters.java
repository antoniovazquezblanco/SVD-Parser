/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Antonio Vázquez Blanco 2023-2026
 */
package io.svdparser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

/**
 * This class represents the {@code <registers>} block of a peripheral.
 */
public class SvdRegisters {
	public static List<SvdRegister> fromElement(Element el, Integer defaultSize, SvdAccess defaultAccess)
			throws SvdParserException {
		List<SvdRegister> registers = new ArrayList<>();

		// Element null check
		if (el == null)
			return registers;

		// XML node name check
		if (!el.getNodeName().equals("registers"))
			throw new SvdParserException("Cannot build an SvdRegisters from a " + el.getNodeName() + " node!");

		// Process cluster children
		for (Element c : Utils.getFirstOrderChildElementsByTagName(el, "cluster"))
			registers.addAll(SvdCluster.fromElement(c, defaultSize, defaultAccess));

		// Process register children
		for (Element r : Utils.getFirstOrderChildElementsByTagName(el, "register"))
			registers.addAll(SvdRegister.fromElement(r, defaultSize, defaultAccess));

		return registers;
	}
}

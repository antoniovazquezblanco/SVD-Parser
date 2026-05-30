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

		// Parse register elements
		for (Element e : Utils.getFirstOrderChildElementsByTagName(el, "register"))
			registers.addAll(SvdRegister.fromElement(e, defaultSize, defaultAccess));

		return registers;
	}
}

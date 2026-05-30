/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Antonio Vázquez Blanco 2023-2025
 */
package io.svdparser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

class SvdSizeTest {
	@Test
	void testDefaultSize() throws SAXException, IOException, ParserConfigurationException, SvdParserException {
		SvdDevice dev = SvdDevice.fromFile(new File("src/test/resources/01_default_size.svd"));

		// PERIPH1 declares <size>16</size>; REG_FROM_PERIPH has no explicit size.
		SvdPeripheral periph1 = dev.getPeripherals().get(0);
		assertEquals(16, periph1.getRegisters().get(0).getSize());

		// REG_OVERRIDE inside PERIPH1 declares <size>8</size>, taking precedence over
		// the peripheral.
		assertEquals(8, periph1.getRegisters().get(1).getSize());

		// PERIPH2 has no <size>; REG_FROM_DEVICE has no explicit size either,
		// so both fall back to the device-level default of 32.
		SvdPeripheral periph2 = dev.getPeripherals().get(1);
		assertEquals(32, periph2.getRegisters().get(0).getSize());
	}
}

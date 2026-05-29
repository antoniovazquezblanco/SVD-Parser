/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Antonio Vázquez Blanco 2023-2025
 */
package io.svdparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

class SvdEnumeratedValuesTest {
	@Test
	void testEnumeratedValues() throws SAXException, IOException, ParserConfigurationException, SvdParserException {
		SvdDevice dev = SvdDevice.fromFile(new File("src/test/resources/08_enumerated_values.svd"));
		SvdField field = dev.getPeripherals().get(0).getRegisters().get(0).getFields().get(0);

		List<SvdEnumeratedValue> evs = field.getEnumeratedValues();
		assertNotNull(evs);
		assertEquals(4, evs.size());
		assertEquals("IDLE", evs.get(0).getName());
		assertEquals(0L, evs.get(0).getValue());
		assertEquals("RUN", evs.get(1).getName());
		assertEquals(1L, evs.get(1).getValue());
	}
}
/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Antonio Vázquez Blanco 2023-2026
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

		List<SvdEnumeratedValues> groups = field.getEnumeratedValues();
		assertNotNull(groups);
		assertEquals(1, groups.size());

		SvdEnumeratedValues group = groups.get(0);
		assertEquals(SvdEnumeratedValuesUsage.READ_WRITE, group.getUsage());

		List<SvdEnumeratedValue> evs = group.getValues();
		assertNotNull(evs);
		assertEquals(4, evs.size());
		assertEquals("IDLE", evs.get(0).getName());
		assertEquals(0L, evs.get(0).getValue());
		assertEquals("RUN", evs.get(1).getName());
		assertEquals(1L, evs.get(1).getValue());
	}

	@Test
	void testEnumeratedValuesUsage() throws SAXException, IOException, ParserConfigurationException, SvdParserException {
		SvdDevice dev = SvdDevice.fromFile(new File("src/test/resources/18_enumerated_values_usage.svd"));
		SvdField field = dev.getPeripherals().get(0).getRegisters().get(0).getFields().get(0);

		List<SvdEnumeratedValues> groups = field.getEnumeratedValues();
		assertNotNull(groups);
		assertEquals(2, groups.size());

		SvdEnumeratedValues readGroup = groups.get(0);
		assertEquals(SvdEnumeratedValuesUsage.READ, readGroup.getUsage());
		assertEquals(2, readGroup.getValues().size());
		assertEquals("Disabled", readGroup.getValues().get(0).getName());
		assertEquals(0L, readGroup.getValues().get(0).getValue());
		assertEquals("Enabled", readGroup.getValues().get(1).getName());
		assertEquals(1L, readGroup.getValues().get(1).getValue());

		SvdEnumeratedValues writeGroup = groups.get(1);
		assertEquals(SvdEnumeratedValuesUsage.WRITE, writeGroup.getUsage());
		assertEquals(1, writeGroup.getValues().size());
		assertEquals("Set", writeGroup.getValues().get(0).getName());
		assertEquals(1L, writeGroup.getValues().get(0).getValue());
	}
}
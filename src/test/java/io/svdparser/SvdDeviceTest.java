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

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

class SvdDeviceTest {

	@Test
	void testSample() {
		try {
			SvdDevice dev = SvdDevice.fromFile(new File("src/test/resources/00_sample.svd"));
			List<SvdPeripheral> periphs = dev.getPeripherals();
			assertNotEquals(periphs.size(), 0);
			for (SvdPeripheral p : periphs) {
				System.out.println(p.toString());
			}
		} catch (SAXException | IOException | ParserConfigurationException | SvdParserException e) {
			e.printStackTrace();
			fail("Failed to parse sample file!");
		}
	}

	@Test
	void testSample01() {
		try {
			SvdDevice dev = SvdDevice.fromFile(new File("src/test/resources/01_default_size.svd"));
			List<SvdPeripheral> periphs = dev.getPeripherals();
			assertNotEquals(periphs.size(), 0);
			for (SvdPeripheral p : periphs) {
				System.out.println(p.toString());
			}
		} catch (SAXException | IOException | ParserConfigurationException | SvdParserException e) {
			e.printStackTrace();
			fail("Failed to parse sample file!");
		}
	}
}

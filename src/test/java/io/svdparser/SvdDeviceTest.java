/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Antonio Vázquez Blanco 2023-2025
 */
package io.svdparser;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.xml.sax.SAXException;

class SvdDeviceTest {

	@ParameterizedTest
	@MethodSource("testResourceProvider")
	void testResourceFile(File f) {
		try {
			System.out.println("Testing file "+f.toString());
			SvdDevice dev = SvdDevice.fromFile(f);
			List<SvdPeripheral> periphs = dev.getPeripherals();
			assertNotEquals(periphs.size(), 0);
			System.out.println(dev.toString());
		} catch (SAXException | IOException | ParserConfigurationException | SvdParserException e) {
			e.printStackTrace();
			fail(String.format("Failed to parse '%s' sample file!", f.getPath()));
		}
	}

	static Stream<File> testResourceProvider() {
		File resdir = new File("src/test/resources/");
		File[] testFiles = resdir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".svd");
			}
		});
		return Stream.of(testFiles);
	}
}

/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Antonio Vázquez Blanco 2023-2026
 */
package io.svdparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SvdAccessTest {

	@Test
	void testCanonicalValues() throws SvdParserException {
		assertEquals(SvdAccess.READ_ONLY, SvdAccess.fromString("read-only"));
		assertEquals(SvdAccess.WRITE_ONLY, SvdAccess.fromString("write-only"));
		assertEquals(SvdAccess.READ_WRITE, SvdAccess.fromString("read-write"));
		assertEquals(SvdAccess.WRITE_ONCE, SvdAccess.fromString("writeOnce"));
		assertEquals(SvdAccess.READ_WRITE_ONCE, SvdAccess.fromString("read-writeOnce"));
	}

	@Test
	void testCaseInsensitive() throws SvdParserException {
		assertEquals(SvdAccess.READ_ONLY, SvdAccess.fromString("READ-ONLY"));
		assertEquals(SvdAccess.WRITE_ONCE, SvdAccess.fromString("writeonce"));
	}

	/**
	 * Some vendor packs (e.g. NSING's N32G05x) emit the non-standard spellings
	 * {@code read} and {@code write} instead of the CMSIS-SVD {@code read-only}
	 * and {@code write-only}. These should be accepted as aliases.
	 */
	@Test
	void testVendorAliases() throws SvdParserException {
		assertEquals(SvdAccess.READ_ONLY, SvdAccess.fromString("read"));
		assertEquals(SvdAccess.WRITE_ONLY, SvdAccess.fromString("write"));
		assertEquals(SvdAccess.READ_ONLY, SvdAccess.fromString("READ"));
		assertEquals(SvdAccess.WRITE_ONLY, SvdAccess.fromString("Write"));
	}

	@Test
	void testUnknownValueThrows() {
		assertThrows(SvdParserException.class, () -> SvdAccess.fromString("read-sometimes"));
	}
}

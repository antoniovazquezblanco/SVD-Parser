/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Antonio Vázquez Blanco 2023-2026
 */
package io.svdparser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

class SvdClusterTest {

	/**
	 * A single, non-dim cluster whose registers are expected to appear before the
	 * peripheral's direct registers (clusters are processed before registers in
	 * SvdRegisters). Register offsets must be adjusted by the cluster's
	 * addressOffset, and names must be prefixed with the cluster name.
	 *
	 * SVD: 10_cluster.svd
	 *   PERIPH0
	 *     <register> CTRL      offset 0x000
	 *     <cluster>  CH        addressOffset 0x100
	 *       <register> CONFIG  offset 0x000  → effective 0x100, name CH_CONFIG
	 *       <register> STATUS  offset 0x004  → effective 0x104, name CH_STATUS (read-only)
	 *
	 * Expected flat list order (clusters before registers):
	 *   [0] CH_CONFIG  0x100
	 *   [1] CH_STATUS  0x104  access=READ_ONLY
	 *   [2] CTRL       0x000
	 */
	@Test
	void testBasicCluster() throws SAXException, IOException, ParserConfigurationException, SvdParserException {
		SvdDevice dev = SvdDevice.fromFile(new File("src/test/resources/10_cluster.svd"));
		List<SvdRegister> regs = dev.getPeripherals().get(0).getRegisters();

		assertEquals(3, regs.size());

		assertEquals("CH_CONFIG", regs.get(0).getName());
		assertEquals(0x100, regs.get(0).getOffset());

		assertEquals("CH_STATUS", regs.get(1).getName());
		assertEquals(0x104, regs.get(1).getOffset());
		assertEquals(SvdAccess.READ_ONLY, regs.get(1).getAccess());

		assertEquals("CTRL", regs.get(2).getName());
		assertEquals(0x0, regs.get(2).getOffset());
	}

	/**
	 * A cluster array (dim=2, dimIncrement=0x20) produces one set of registers per
	 * dim element. Each cluster instance's name is formed by substituting the dim
	 * index into the %s placeholder (CH0, CH1). Offsets accumulate accordingly.
	 *
	 * SVD: 16_cluster_dim.svd
	 *   PERIPH0
	 *     <cluster> CH%s  dim=2  dimIncrement=0x20  addressOffset=0x10
	 *                     access=read-write
	 *       <register> CTRL    offset 0x0  (inherits read-write)
	 *       <register> STATUS  offset 0x4  access=read-only
	 *
	 * Expected flat list:
	 *   [0] CH0_CTRL    0x10  access=READ_WRITE
	 *   [1] CH0_STATUS  0x14  access=READ_ONLY
	 *   [2] CH1_CTRL    0x30  access=READ_WRITE
	 *   [3] CH1_STATUS  0x34  access=READ_ONLY
	 */
	@Test
	void testDimCluster() throws SAXException, IOException, ParserConfigurationException, SvdParserException {
		SvdDevice dev = SvdDevice.fromFile(new File("src/test/resources/16_cluster_dim.svd"));
		List<SvdRegister> regs = dev.getPeripherals().get(0).getRegisters();

		assertEquals(4, regs.size());

		assertEquals("CH0_CTRL", regs.get(0).getName());
		assertEquals(0x10, regs.get(0).getOffset());
		assertEquals(SvdAccess.READ_WRITE, regs.get(0).getAccess());

		assertEquals("CH0_STATUS", regs.get(1).getName());
		assertEquals(0x14, regs.get(1).getOffset());
		assertEquals(SvdAccess.READ_ONLY, regs.get(1).getAccess());

		assertEquals("CH1_CTRL", regs.get(2).getName());
		assertEquals(0x30, regs.get(2).getOffset());
		assertEquals(SvdAccess.READ_WRITE, regs.get(2).getAccess());

		assertEquals("CH1_STATUS", regs.get(3).getName());
		assertEquals(0x34, regs.get(3).getOffset());
		assertEquals(SvdAccess.READ_ONLY, regs.get(3).getAccess());
	}

	/**
	 * A cluster nested inside another cluster. Offsets accumulate across nesting
	 * levels and name prefixes are separated by '_'. Within each cluster, nested
	 * clusters are processed before direct registers.
	 *
	 * SVD: 17_cluster_nested.svd
	 *   PERIPH0
	 *     <cluster> OUTER  addressOffset=0x100
	 *       <cluster> INNER  addressOffset=0x20
	 *         <register> REG   offset=0x0  → effective 0x120, name OUTER_INNER_REG
	 *       <register> DATA    offset=0x0  → effective 0x100, name OUTER_DATA
	 *
	 * Expected flat list (nested clusters before sibling registers):
	 *   [0] OUTER_INNER_REG  0x120
	 *   [1] OUTER_DATA       0x100
	 */
	@Test
	void testNestedCluster() throws SAXException, IOException, ParserConfigurationException, SvdParserException {
		SvdDevice dev = SvdDevice.fromFile(new File("src/test/resources/17_cluster_nested.svd"));
		List<SvdRegister> regs = dev.getPeripherals().get(0).getRegisters();

		assertEquals(2, regs.size());

		assertEquals("OUTER_INNER_REG", regs.get(0).getName());
		assertEquals(0x120, regs.get(0).getOffset());

		assertEquals("OUTER_DATA", regs.get(1).getName());
		assertEquals(0x100, regs.get(1).getOffset());
	}
}

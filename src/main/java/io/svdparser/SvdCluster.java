/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Antonio Vázquez Blanco 2023-2026
 */
package io.svdparser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class represents a cluster element within a peripheral's registers
 * block. A cluster groups a sequence of neighboring registers and defines a
 * base {@code addressOffset} relative to the peripheral base address. All
 * registers inside the cluster specify their own {@code addressOffset} relative
 * to the cluster base.
 *
 * <p>Clusters are flattened into a plain list of {@link SvdRegister} objects.
 * Each register's effective offset is adjusted by the cluster's
 * {@code addressOffset} and its name is prefixed with the cluster name (e.g.
 * a register {@code CONFIG} inside cluster {@code CH} becomes
 * {@code CH_CONFIG}). Nested clusters accumulate offsets and prefixes
 * recursively.</p>
 */
public class SvdCluster {
	/**
	 * Parse a {@code <cluster>} DOM element and return its registers as a flat
	 * list.
	 *
	 * @param el            The {@code <cluster>} DOM element.
	 * @param defaultSize   Default register size inherited from the parent.
	 * @param defaultAccess Default access mode inherited from the parent.
	 * @return A flat list of {@link SvdRegister} objects for all registers in the
	 *         cluster (including nested clusters, expanded for {@code dim}).
	 * @throws SvdParserException on SVD format errors.
	 */
	public static List<SvdRegister> fromElement(Element el, Integer defaultSize, SvdAccess defaultAccess) throws SvdParserException {
		return fromElement(el, defaultSize, defaultAccess, 0, "");
	}

	/**
	 * Parse a {@code <cluster>} DOM element and return its registers as a flat
	 * list.
	 *
	 * @param el            The {@code <cluster>} DOM element.
	 * @param defaultSize   Default register size inherited from the parent.
	 * @param defaultAccess Default access mode inherited from the parent.
	 * @param baseOffset    Address offset accumulated from any enclosing clusters.
	 * @param namePrefix    Name prefix accumulated from any enclosing clusters.
	 * @return A flat list of {@link SvdRegister} objects for all registers in the
	 *         cluster (including nested clusters, expanded for {@code dim}).
	 * @throws SvdParserException on SVD format errors.
	 */
	public static List<SvdRegister> fromElement(Element el, Integer defaultSize, SvdAccess defaultAccess, int baseOffset, String namePrefix) throws SvdParserException {
        // Element null check
        if (el == null)
            return null;

		// XML node name check
		if (!el.getNodeName().equals("cluster"))
			throw new SvdParserException("Cannot build an SvdCluster from a " + el.getNodeName() + " node!");

		// Parse dim elements
		Element dimElement = Utils.getSingleFirstOrderChildElementByTagName(el, "dim");
		int dim = (dimElement != null) ? Integer.decode(dimElement.getTextContent()) : 1;
		Element dimIncrementElement = Utils.getSingleFirstOrderChildElementByTagName(el, "dimIncrement");
		int dimIncrement = (dimIncrementElement != null) ? Integer.decode(dimIncrementElement.getTextContent()) : 0;

		// Parse name and address offset
		String name = Utils.getSingleFirstOrderChildElementByTagName(el, "name").getTextContent();
		int clusterOffset = Integer.decode(Utils.getSingleFirstOrderChildElementByTagName(el, "addressOffset").getTextContent());

		// Parse size/access overrides from the cluster
		Element sizeElement = Utils.getSingleFirstOrderChildElementByTagName(el, "size");
		if (sizeElement != null)
			defaultSize = Integer.decode(sizeElement.getTextContent());
		Element accessElement = Utils.getSingleFirstOrderChildElementByTagName(el, "access");
		if (accessElement != null)
			defaultAccess = SvdAccess.fromString(accessElement.getTextContent());

		List<SvdRegister> registers = new ArrayList<>();
		for (int i = 0; i < dim; i++) {
			// Cluster name for this dim expansion (substitutes %s placeholder when present)
			String clusterName = name.formatted(String.valueOf(i));
			// Build the prefix that all child registers will carry, e.g. "CH_" or "CH0_"
			String clusterPrefix = namePrefix + clusterName + "_";
			int effectiveBase = baseOffset + clusterOffset + i * dimIncrement;

			// Process nested clusters
            for (Element c : Utils.getFirstOrderChildElementsByTagName(el, "cluster"))
                registers.addAll(SvdCluster.fromElement(c, defaultSize, defaultAccess, effectiveBase, clusterPrefix));

			// Process registers
            for (Element r : Utils.getFirstOrderChildElementsByTagName(el, "register"))
                registers.addAll(SvdRegister.fromElement(r, defaultSize, defaultAccess, effectiveBase, clusterPrefix));
		}
		return registers;
	}
}

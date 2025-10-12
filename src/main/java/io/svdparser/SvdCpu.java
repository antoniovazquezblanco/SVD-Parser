/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Antonio Vázquez Blanco 2023-2025
 */
package io.svdparser;

import org.w3c.dom.Element;

/**
 * This represents the CPU section in a SVD file. The CPU section describes the
 * processor included in the microcontroller device. This section is mandatory
 * if the SVD file is used to generate the device header file.
 */
public class SvdCpu {
	private String mName;
	private String mRevision;
	private String mEndian;
	private Boolean mMpuPresent;
	private Boolean mFpuPresent;
	private Integer mNvicPrioBits;
	private Boolean mVendorSystickConfig;

	/**
	 * Construct a CPU object from a DOM element.
	 *
	 * @param el DOM element.
	 * @return A SvdCpu object.
	 * @throws SvdParserException when the element does not correspond to a CPU.
	 */
	public static SvdCpu fromElement(Element el) throws SvdParserException {
		if (el == null)
			return new SvdCpu(null, null, null, null, null, null, null);

		if (!el.getNodeName().equals("cpu"))
			throw new SvdParserException("Cannot build an SvdCpu from a " + el.getNodeName() + " node!");

		String name = null;
		Element nameElement = Utils.getSingleFirstOrderChildElementByTagName(el, "name");
		if (nameElement != null)
			name = nameElement.getTextContent();

		String revision = null;
		Element revisionElement = Utils.getSingleFirstOrderChildElementByTagName(el, "revision");
		if (revisionElement != null)
			revision = revisionElement.getTextContent();

		String endian = null;
		Element endianElement = Utils.getSingleFirstOrderChildElementByTagName(el, "endian");
		if (endianElement != null)
			endian = endianElement.getTextContent();

		Boolean mpuPresent = null;
		Element mpuPresentElement = Utils.getSingleFirstOrderChildElementByTagName(el, "mpuPresent");
		if (mpuPresentElement != null)
			mpuPresent = Boolean.parseBoolean(mpuPresentElement.getTextContent());

		Boolean fpuPresent = null;
		Element fpuPresentElement = Utils.getSingleFirstOrderChildElementByTagName(el, "fpuPresent");
		if (fpuPresentElement != null)
			fpuPresent = Boolean.parseBoolean(fpuPresentElement.getTextContent());

		Integer nvicPrioBits = null;
		Element nvicPrioBitsElement = Utils.getSingleFirstOrderChildElementByTagName(el, "nvicPrioBits");
		if (nvicPrioBitsElement != null)
			nvicPrioBits = Integer.decode(nvicPrioBitsElement.getTextContent());

		Boolean vendorSystickConfig = null;
		Element vendorSystickConfigElement = Utils.getSingleFirstOrderChildElementByTagName(el, "vendorSystickConfig");
		if (vendorSystickConfigElement != null)
			vendorSystickConfig = Boolean.parseBoolean(vendorSystickConfigElement.getTextContent());

		return new SvdCpu(name, revision, endian, mpuPresent, fpuPresent, nvicPrioBits, vendorSystickConfig);
	}

	private SvdCpu(String name, String revision, String endian, Boolean mpuPresent, Boolean fpuPresent,
			Integer nvicPrioBits, Boolean vendorSystickConfig) {
		mName = name;
		mRevision = revision;
		mEndian = endian;
		mMpuPresent = mpuPresent;
		mFpuPresent = fpuPresent;
		mNvicPrioBits = nvicPrioBits;
		mVendorSystickConfig = vendorSystickConfig;
	}

	/**
	 * Get the CPU name.
	 *
	 * @return The CPU name.
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Get the CPU revision.
	 *
	 * @return The CPU revision.
	 */
	public String getRevision() {
		return mRevision;
	}

	/**
	 * Get the CPU endianness.
	 *
	 * @return The CPU endianness.
	 */
	public String getEndian() {
		return mEndian;
	}

	/**
	 * Check if MPU is present.
	 *
	 * @return True if MPU is present, false otherwise.
	 */
	public Boolean isMpuPresent() {
		return mMpuPresent;
	}

	/**
	 * Check if FPU is present.
	 *
	 * @return True if FPU is present, false otherwise.
	 */
	public Boolean isFpuPresent() {
		return mFpuPresent;
	}

	/**
	 * Get the number of NVIC priority bits.
	 *
	 * @return The number of NVIC priority bits.
	 */
	public Integer getNvicPrioBits() {
		return mNvicPrioBits;
	}

	/**
	 * Check if vendor Systick configuration is used.
	 *
	 * @return True if vendor Systick configuration is used, false otherwise.
	 */
	public Boolean isVendorSystickConfig() {
		return mVendorSystickConfig;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SvdCpu{");
		if (mName != null)
			sb.append("name=\"" + mName + "\", ");
		if (mRevision != null)
			sb.append("revision=\"" + mRevision + "\", ");
		if (mEndian != null)
			sb.append("endian=\"" + mEndian + "\", ");
		if (mMpuPresent != null)
			sb.append("mpuPresent=" + mMpuPresent + ", ");
		if (mFpuPresent != null)
			sb.append("fpuPresent=" + mFpuPresent + ", ");
		if (mNvicPrioBits != null)
			sb.append("nvicPrioBits=" + mNvicPrioBits + ", ");
		if (mVendorSystickConfig != null)
			sb.append("vendorSystickConfig=" + mVendorSystickConfig);
		sb.append("}");
		return sb.toString();
	}
}

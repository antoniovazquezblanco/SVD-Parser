/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Antonio Vázquez Blanco 2023-2026
 */
package io.svdparser;

/**
 * The access direction for which an {@link SvdEnumeratedValues} block applies.
 */
public enum SvdEnumeratedValuesUsage {
	/** The enumeration describes values returned by a read. */
	READ("read"),

	/** The enumeration describes values that may be written. */
	WRITE("write"),

	/** The enumeration applies to both read and write accesses (default). */
	READ_WRITE("read-write");

	private final String mSvdValue;

	SvdEnumeratedValuesUsage(String svdValue) {
		mSvdValue = svdValue;
	}

	/**
	 * Get the SVD string representation of this usage value.
	 *
	 * @return The SVD string, e.g. {@code "read"}.
	 */
	public String getSvdValue() {
		return mSvdValue;
	}

	/**
	 * Parse a {@link SvdEnumeratedValuesUsage} from its SVD string representation.
	 *
	 * @param value The string as it appears in the SVD XML.
	 * @return The matching {@link SvdEnumeratedValuesUsage} constant.
	 * @throws SvdParserException if the value does not match any known usage.
	 */
	public static SvdEnumeratedValuesUsage fromString(String value) throws SvdParserException {
		for (SvdEnumeratedValuesUsage u : values())
			if (u.mSvdValue.equals(value))
				return u;
		throw new SvdParserException("Unknown enumeratedValues usage: \"" + value + "\"");
	}
}

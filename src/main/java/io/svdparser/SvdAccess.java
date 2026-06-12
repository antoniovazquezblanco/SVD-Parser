/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Antonio Vázquez Blanco 2023-2026
 */
package io.svdparser;

/**
 * Enumeration of the access permission values defined by the CMSIS-SVD
 * specification. Access rights can be defined at device, peripheral, register,
 * and field level, with each lower level being able to override the one above.
 */
public enum SvdAccess {
	/** Read access is permitted. Write operations have an undefined result. */
	READ_ONLY("read-only", "read"),

	/** Read operations have an undefined result. Write access is permitted. */
	WRITE_ONLY("write-only", "write"),

	/**
	 * Read and write accesses are permitted. Writes affect the state of the
	 * register and reads return the register value.
	 */
	READ_WRITE("read-write"),

	/**
	 * Read operations have an undefined result. Only the first write after reset
	 * has an effect.
	 */
	WRITE_ONCE("writeOnce"),

	/**
	 * Read access is always permitted. Only the first write access after a reset
	 * will have an effect on the content. Other write operations have an undefined
	 * result.
	 */
	READ_WRITE_ONCE("read-writeOnce");

	private final String mSvdValue;
	private final String[] mAliases;

	SvdAccess(String svdValue, String... aliases) {
		mSvdValue = svdValue;
		mAliases = aliases;
	}

	/**
	 * Get the SVD string representation of this access value (as it appears in the
	 * XML file).
	 *
	 * @return The SVD string, e.g. {@code "read-only"}.
	 */
	public String getSvdValue() {
		return mSvdValue;
	}

	/**
	 * Parse an {@link SvdAccess} from its SVD string representation.
	 *
	 * In addition to the canonical CMSIS-SVD spelling, a few non-standard aliases
	 * emitted by some vendor packs are accepted, such as {@code "read"} (mapped to
	 * {@link #READ_ONLY}) and {@code "write"} (mapped to {@link #WRITE_ONLY}).
	 *
	 * @param value The string as it appears in the SVD XML, e.g.
	 *              {@code "read-only"}.
	 * @return The matching {@link SvdAccess} constant.
	 * @throws SvdParserException if the value does not match any known access type.
	 */
	public static SvdAccess fromString(String value) throws SvdParserException {
		for (SvdAccess access : values()) {
			if (access.mSvdValue.equalsIgnoreCase(value))
				return access;
			for (String alias : access.mAliases)
				if (alias.equalsIgnoreCase(value))
					return access;
		}
		throw new SvdParserException("Unknown access value: \"" + value + "\"");
	}
}

/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Antonio Vázquez Blanco 2023-2025
 */
package io.svdparser;

/**
 * A runtime exception that may happen when parsing a SVD file.
 */
public class SvdParserException extends RuntimeException {

	private static final long serialVersionUID = 7346114051394569394L;

	/**
	 * Create an SvdParserException from an error message.
	 * @param string Error message.
	 */
	public SvdParserException(String string) {
		super(string);
	}
}

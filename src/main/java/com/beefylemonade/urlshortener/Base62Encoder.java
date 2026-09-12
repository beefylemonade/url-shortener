package com.beefylemonade.urlshortener;

public class Base62Encoder {
	private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static final int BASE = 62;

	public static String encode(long id) {

		// Edge case handling. Return "0" immediate for 0
		if (id < 0) {
			throw new IllegalArgumentException("id cannot be negative: " + id);
		}
		if (id == 0) {
			return "0";
		}

		StringBuilder sb = new StringBuilder();
		// Repeatedly divide id by BASE, use the remainder to index
		// into ALPHABET, prepend each char, until id == 0.
		while (id > 0) {
			sb.append(ALPHABET.charAt((int) (id % BASE)));
			id /= BASE;

		}

		return sb.reverse().toString();
	}

	public static long decode(String shortCode) {
		// Reverse of encode. Walk each character left to right,

		long result = 0;

		for (char c : shortCode.toCharArray()) {

			int index = ALPHABET.indexOf(c);

			if (index < 0) {
				throw new IllegalArgumentException("Invalid character in short code: " + c);

			}
			result = result * BASE + index;
		}

		return result;

	}

}

package com.beefylemonade.urlshortener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class Base62EncoderTests {

	@Test
	void encode_zeroInput_returnsZeroString() {
		long input = 0;
		String expectedOutput = "0";
		assertEquals(expectedOutput, Base62Encoder.encode(input));
	}

	@Test
	void encode_alphabetBoundary_returnsZ() {
		long input = 61;
		String expectedOutput = "z";
		assertEquals(expectedOutput, Base62Encoder.encode(input));
	}

	@Test
	void encode_baseRollover_returnsTwoDigits() {
		long input = 62;
		String expectedOutput = "10";
		assertEquals(expectedOutput, Base62Encoder.encode(input));
	}

	@Test
	void decode_twoDigitRollover_returns62() {
		String input = "10";
		long expectedOutput = 62;
		assertEquals(expectedOutput, Base62Encoder.decode(input));
	}

	@Test
	void decodeEncode_roundTrip_returnsOriginalValue() {
		long original = 123456;
		assertEquals(original, Base62Encoder.decode(Base62Encoder.encode(original)));
	}

	@Test
	void encodeDecode_largeValue_roundTrips() {
		long original = 999_999_999_999L;
		assertEquals(original, Base62Encoder.decode(Base62Encoder.encode(original)));
	}

	@Test
	void encode_negativeId_throwIllegalArgumentException() {
		long input = -1;

		assertThrows(IllegalArgumentException.class, () -> Base62Encoder.encode(input));
	}

	@Test
	void decode_invalidCode_throwIllegalArgumentException() {
		String input = "+";
		assertThrows(IllegalArgumentException.class, () -> Base62Encoder.decode(input));
	}

}

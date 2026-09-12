package com.beefylemonade.urlshortener;

public class Base62Encoder {
	private static final String ALPHABET =
	        "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	    private static final int BASE = 62;

	    public static String encode(long id) {
	    	
	    	String encodedString = "";
	        // TODO: repeatedly divide id by 62, use the remainder to index
	        // into ALPHABET, prepend each char, until id == 0.
	        // Edge case: what does your loop do if id == 0 going in?
	    	
	    	return encodedString;
	    }

	    public static long decode(String shortCode) {
	        // TODO: reverse of encode — walk each character left to right,
	        // result = result * 62 + indexOf(char in ALPHABET)
	    	
	    	long result = 0;
	    	
	    	for(char c: shortCode.toCharArray()) {
	    		result = result * 62 + ALPHABET.indexOf(c);
	    	}
	    	
	    	
	    	
	    	return result;
	    	
	    }


}

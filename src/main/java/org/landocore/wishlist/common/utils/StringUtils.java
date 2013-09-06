package org.landocore.wishlist.common.utils;

import org.apache.commons.lang.RandomStringUtils;


/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 29/07/13
 * Time: 21:50
 * Utility class for strings
 */
public final class StringUtils {
	
	/**
	 * max number of different character.
	 */
	private static final int NB_DIFFERENT_CHAR = 4;

    /**
     * generates a random alphanumeric string.
     * @param length length of the string
     * @return the random string
     */
    public static String generateRandomPassword(final int length) {
        return RandomStringUtils.randomAlphanumeric(length);

    }
    
    /**
     * checks if the password meets the strength.
     * @param password the password to be checked
     * @param length minimum length of password
     * @param differentCharacterType number of different characters
     * @return true if meets strength
     */
    public static boolean isPasswordComplexEnough(final String password,
    		final int length, final int differentCharacterType) {
    	
    	boolean passwordOk = true;
    	
    	if (password.length() < length) {
    		passwordOk = false;
    	}
    	    	
    	int nbCharacterType = 0;
    	boolean foundUpperCase = false;
    	boolean foundLowerCase = false;
    	boolean foundNumber = false;
    	boolean foundSpecialChar = false;
    	
    	//uppercase
    	for (char c : password.toCharArray()) {
    		if (Character.isLetter(c)) {
    			if (Character.isUpperCase(c)) {
    				foundUpperCase = true;
    			} else {
    				foundLowerCase = true;
    			}
    		} else {
    			if (Character.isDigit(c)) {
    				foundNumber = true;
    			} else {
    				foundSpecialChar = true;
    			}
    		}
    	}
    	
    	if (foundLowerCase) {
    		nbCharacterType++;
    	}
    	if (foundUpperCase) {
    		nbCharacterType++;
    	}
    	if (foundNumber) {
    		nbCharacterType++;
    	}
    	if (foundSpecialChar) {
    		nbCharacterType++;
    	}
    	
    	if (nbCharacterType < Math.
    			min(differentCharacterType, NB_DIFFERENT_CHAR)) {
    		passwordOk = false;
    	}
    	
    	return passwordOk;
    }

    /**
     * Utility class : no public constructor.
     */
    private StringUtils() {
    }
}

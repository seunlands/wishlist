package org.landocore.wishlist.common.exception;

/**
 * Password strength exception.
 * @author LANDSBERG-S
 *
 */
public class PasswordStrengthException extends Exception {

	
    /**
	 * UID.
	 */
	private static final long serialVersionUID = 20130906L;

	/**
     * constructor with message.
     * @param pMessage message of the exception
     */
    public PasswordStrengthException(final String pMessage) {
        super(pMessage);
    }

}

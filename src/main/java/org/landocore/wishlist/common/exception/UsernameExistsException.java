package org.landocore.wishlist.common.exception;

/**
 * Created with IntelliJ IDEA.
 * User: LANDSBERG-S
 * Date: 24/08/13
 * Time: 20:04
 * To change this template use File | Settings | File Templates.
 */
public class UsernameExistsException extends Exception {

    /**
	 * UID.
	 */
	private static final long serialVersionUID = 20130920L;

	/**
     * constructor with message.
     * @param pMessage message of the exception
     */
    public UsernameExistsException(final String pMessage) {
        super(pMessage);
    }
}

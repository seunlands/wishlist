package org.landocore.wishlist.common.exception;

/**
 * Created with IntelliJ IDEA.
 * User: LANDSBERG-S
 * Date: 23/08/13
 * Time: 15:57
 * Exception to be thrown at a database initialization errors.
 */
public class DatabaseInitException extends RuntimeException {


    /**
     * Constructor with an Exception
     * @param pException mother of this exception
     */
    public DatabaseInitException(final Exception pException){
        super(pException);
    }
}

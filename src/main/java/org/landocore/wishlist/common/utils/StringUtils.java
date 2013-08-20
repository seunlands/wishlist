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
     * generates a random alphanumeric string.
     * @param length length of the string
     * @return the random string
     */
    public static String generateRandomPassword(final int length) {
        return RandomStringUtils.randomAlphanumeric(length);

    }

    /**
     * Utility class : no public constructor.
     */
    private StringUtils() {
    }
}

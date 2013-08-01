package org.landocore.wishlist.business.util;

import org.apache.commons.lang.RandomStringUtils;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 29/07/13
 * Time: 21:50
 * To change this template use File | Settings | File Templates.
 */
public final class StringUtils {

    public static String generateRandomPassword(int length){
        return RandomStringUtils.randomAlphanumeric(length);

    }
}

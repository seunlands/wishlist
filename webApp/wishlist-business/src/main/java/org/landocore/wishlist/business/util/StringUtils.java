package org.landocore.wishlist.business.util;

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
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length ; i++){
            char c = (char)(r.nextInt((int)Character.MAX_VALUE));
            sb.append(c);
        }
        return sb.toString();
    }
}

package org.landocore.wishlist.web.utils;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 29/07/13
 * Time: 23:25
 * To change this template use File | Settings | File Templates.
 */
public final class LibelleUtil {

    private static final Logger logger = Logger.getLogger(LibelleUtil.class);


    public static String getString(InputStream inputStream, Object[] args) {
        StringBuffer buffer = new StringBuffer();
        if (inputStream != null) {
            InputStreamReader isr = new InputStreamReader(inputStream);

            BufferedReader br = new BufferedReader(isr);
            String temp;
            try {
                while ((temp = br.readLine()) != null) {
                    buffer.append(temp).append("\n");

                }
                br.close();
                isr.close();
            } catch (IOException e) {
                logger.warn(e.getMessage(), e);
            }
        }
        try {
            return MessageFormat.format(buffer.toString(), args);
        } catch (final IllegalArgumentException e) {
            logger.warn(e.getMessage(), e);
            return buffer.toString();
        }
    }
}

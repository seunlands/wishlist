package org.landocore.wishlist.common.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Utility class for the names.
 */
public final class LibelleUtil {

    /**
     * the logger.
     */
    private static final Logger LOGGER = LoggerFactory.
            getLogger(LibelleUtil.class);

    /**
     * replaces the {n} (n is a integer) in InputStream with args[n].
     * @param inputStream the stream to read
     * @param args the items to replace
     * @return the formatted string
     */
    public static String getString(
            final InputStream inputStream, final Object[] args) {
        StringBuilder buffer = new StringBuilder();
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
                LOGGER.warn(e.getMessage(), e);
            }
        }
        try {
            return MessageFormat.format(buffer.toString(), args);
        } catch (final IllegalArgumentException e) {
            LOGGER.warn(e.getMessage(), e);
            return buffer.toString();
        }
    }

    /**
     * private constructor -> utility class.
     */
    private LibelleUtil() {
    }
}

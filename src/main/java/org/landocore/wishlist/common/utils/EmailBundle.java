package org.landocore.wishlist.common.utils;

import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 29/07/13
 * Time: 23:05
 * Bundle to access the email resources
 */
public class EmailBundle extends ResourceBundleMessageSource {

    /**
     * Returns de message from the key.
     * @param pKey : the key to look for
     * @return String the message
     */
    public final String getMessage(final String pKey) {
        return super.getMessage(pKey, null, Locale.ROOT);
    }
}

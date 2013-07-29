package org.landocore.wishlist.web.utils;

import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 29/07/13
 * Time: 23:05
 * To change this template use File | Settings | File Templates.
 */
public class EmailBundle extends ResourceBundleMessageSource {

    public String getMessage(final String message){
        return super.getMessage(message, null, Locale.ROOT);
    }
}

package i2.application.extranet.utils;

import java.util.Locale;

import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Classe d'accés aux messages de la couche Web.
 * 
 * @author Bull
 */
public class WebBundle extends ResourceBundleMessageSource {

    /**
     * @param message
     *            Clé du message dans le fichier de propriétés
     * 
     * @see ResourceBundleMessageSource#getMessage(String, Object[], Locale)
     * 
     */
    public String getMessage(final String message) {
	return super.getMessage(message, null, Locale.ROOT);
    }

}

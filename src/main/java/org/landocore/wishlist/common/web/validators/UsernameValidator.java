package org.landocore.wishlist.common.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Created with IntelliJ IDEA.
 * User: LANDSBERG-S
 * Date: 26/08/13
 * Time: 13:15
 * Reset password form validator.
 */
//@FacesValidator("UserNameValidator")
public class UsernameValidator implements Validator {

    @Override
    public final void validate(final FacesContext context,
                final UIComponent component, final Object value) {

        if (value == null) {
            FacesMessage message = new FacesMessage();
            message.setSummary("Username is required");
            message.setDetail("Username should be filled in");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }

    }

}

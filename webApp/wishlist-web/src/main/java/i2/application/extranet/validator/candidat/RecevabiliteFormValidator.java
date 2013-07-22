package i2.application.extranet.validator.candidat;

import i2.application.extranet.form.recevabilite.RecevabiliteForm;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator Spring du formulaire RecevabiliteForm
 * 
 * @author bull
 * 
 */
public class RecevabiliteFormValidator implements Validator {

    @Override
    public boolean supports(@SuppressWarnings("rawtypes") Class clazz) {
	return RecevabiliteForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	RecevabiliteForm recevabiliteForm = (RecevabiliteForm) target;

	if (recevabiliteForm.getCanNeph().isEmpty()) {
	    errors.rejectValue("canNeph", "field.required");
	}
	if (recevabiliteForm.getTepCode().isEmpty()) {
	    errors.rejectValue("tepCode", "field.required");
	}

    }
}

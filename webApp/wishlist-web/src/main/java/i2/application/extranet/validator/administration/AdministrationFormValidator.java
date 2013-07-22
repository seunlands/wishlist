package i2.application.extranet.validator.administration;

import i2.application.extranet.bean.view.administration.ParametreDefaultBean;
import i2.application.extranet.form.administration.AdministrationForm;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator Spring du form AdministrationForm
 * 
 * @author bull
 * 
 */
public class AdministrationFormValidator implements Validator {

    @Override
    public boolean supports(@SuppressWarnings("rawtypes") Class clazz) {
	return AdministrationForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	AdministrationForm adminForm = (AdministrationForm) target;
	int i = 0;
	for (ParametreDefaultBean param : adminForm.getListParametres()) {

	    if (param.getValeurDefault() == null) {
		errors.pushNestedPath("listParametres[" + i + "]");
		errors.rejectValue("valeurDefault", "field.required");
		errors.popNestedPath();
	    }
	    i++;
	}
	// TODO Auto-generated method stub

    }

}

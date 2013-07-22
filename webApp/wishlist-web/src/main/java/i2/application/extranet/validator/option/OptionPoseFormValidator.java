package i2.application.extranet.validator.option;

import i2.application.extranet.form.option.OptionPoseForm;
import i2.application.extranet.form.txt.RechercheTxtCompositeForm;
import i2.application.extranet.form.txt.SubmitFieldsTxtForm;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class OptionPoseFormValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
	return RechercheTxtCompositeForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	SubmitFieldsTxtForm optionPoseForm = ((RechercheTxtCompositeForm) target).getSubmitFieldsTxtForm();
	List<OptionPoseForm> lstOption = optionPoseForm.getOptionsPose();
	for (OptionPoseForm form : lstOption) {
	    if (form != null && form.getOptionsPose() < 0) {
		errors.rejectValue("submitFieldsTxtForm.optionsPose", "field.positif");
	    }
	    if (form != null && form.getOptionsPose() > form.getPlaceDispo()) {
		errors.rejectValue("submitFieldsTxtForm.optionsPose", "field.trop.options");
	    }
	}
    }
}

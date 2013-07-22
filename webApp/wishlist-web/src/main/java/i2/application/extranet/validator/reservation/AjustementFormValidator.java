package i2.application.extranet.validator.reservation;

import i2.application.extranet.bean.view.multigroup.AbstractViewLigneMultiGroup;
import i2.application.extranet.bean.view.multigroup.LigneAjustementMultiGroup;
import i2.application.extranet.form.AjustementRequestForm;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator pour les valeurs saisies dans le tableau d'ajustement.
 * 
 * La valeur maximale est 999.
 * 
 * @author Bull
 * 
 */
public class AjustementFormValidator implements Validator {

    private final static int MAX_VALUE = 999;

    @SuppressWarnings("rawtypes")
    @Override
    public boolean supports(Class clazz) {
	return AjustementRequestForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	AjustementRequestForm form = (AjustementRequestForm) target;
	for (AbstractViewLigneMultiGroup ligne : form.getLigneAjustement()) {
	    if (((LigneAjustementMultiGroup) ligne).getRetenues() != null && ((LigneAjustementMultiGroup) ligne).getRetenues() > MAX_VALUE) {
		errors.rejectValue(null, "ajustement.retenue.limit.value");
	    }
	    if (((LigneAjustementMultiGroup) ligne).getCls() != null && (((LigneAjustementMultiGroup) ligne).getCls() > MAX_VALUE || ((LigneAjustementMultiGroup) ligne).getCls() < -MAX_VALUE)) {
		errors.rejectValue(null, "ajustement.cls.limit.value");
	    }
	}
    }
}

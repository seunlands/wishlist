package i2.application.extranet.validator.reservation;

import i2.application.extranet.form.reservation.AutorisationDroitsForm;

import java.text.SimpleDateFormat;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 
 * @author BULL
 * 
 */
public class AutorisationDroitsFormValidator implements Validator {

    @Override
    public boolean supports(@SuppressWarnings("rawtypes") Class clazz) {
	return AutorisationDroitsForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	AutorisationDroitsForm autorisationDroitsForm = (AutorisationDroitsForm) target;
	String anneeMoisSelectionne = autorisationDroitsForm.getAnneeMoisSelectionne();

	// Vérifier le format de la date envoyé depuis le formulaire
	SimpleDateFormat formatAnneeMoisSelected = new SimpleDateFormat("yyyyMM");
	try {
	    formatAnneeMoisSelected.parse(anneeMoisSelectionne);
	} catch (Exception e) {
	    errors.rejectValue("anneeMoisSelectionne", "autorisation.date.format");
	}

    }
}

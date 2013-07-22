/**
 * 
 */
package i2.application.extranet.validator.reservation;

import i2.application.extranet.form.txt.RechercheTxtCompositeForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Bull-SAS
 * 
 */
public class AttributionFormValidatorTxt implements Validator {
    @Override
    public boolean supports(@SuppressWarnings("rawtypes") Class clazz) {
	return RechercheTxtCompositeForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	RechercheTxtCompositeForm reservationForm = (RechercheTxtCompositeForm) target;

	// que les valeurs saisies au niveau des semaines est correcte ( >=0)
	if (reservationForm.getSubmitFieldsTxtForm().getReservation().getSemaine1() < 0) {
	    errors.rejectValue("submitFieldsTxtForm.reservation.semaine1", "form.incorrect.data");
	}
	if (reservationForm.getSubmitFieldsTxtForm().getReservation().getSemaine2() < 0) {
	    errors.rejectValue("submitFieldsTxtForm.reservation.semaine2", "form.incorrect.data");
	}
	if (reservationForm.getSubmitFieldsTxtForm().getReservation().getSemaine3() < 0) {
	    errors.rejectValue("submitFieldsTxtForm.reservation.semaine3", "form.incorrect.data");
	}
	if (reservationForm.getSubmitFieldsTxtForm().getReservation().getSemaine4() < 0) {
	    errors.rejectValue("submitFieldsTxtForm.reservation.semaine4", "form.incorrect.data");
	}
	if (reservationForm.getSubmitFieldsTxtForm().getReservation().getSemaine5() < 0) {
	    errors.rejectValue("submitFieldsTxtForm.reservation.semaine5", "form.incorrect.data");
	}

    }
}

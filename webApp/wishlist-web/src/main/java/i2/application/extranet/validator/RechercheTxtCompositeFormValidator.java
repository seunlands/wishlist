package i2.application.extranet.validator;

import i2.application.extranet.form.affetation.AffectationCreneauForm;
import i2.application.extranet.form.option.OptionPoseForm;
import i2.application.extranet.form.txt.RechercheTxtCompositeForm;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RechercheTxtCompositeFormValidator implements Validator {

    private Integer min;

    private Integer max;

    public RechercheTxtCompositeFormValidator() {
	// ...
    }

    public RechercheTxtCompositeFormValidator(Integer min, Integer max) {
	this.min = min;
	this.max = max;
    }

    @Override
    public boolean supports(@SuppressWarnings("rawtypes") Class clazz) {
	return RechercheTxtCompositeForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	// TODO: Créer une méthode permettant de tester si le nombre passer en
	// paramétre est entre une valMin et valMax
	RechercheTxtCompositeForm form = (RechercheTxtCompositeForm) target;
	// validation de l'affectation
	if (form.getAffectationForm() != null && !form.getAffectationForm().getAffectations().isEmpty()) {
	    List<AffectationCreneauForm> creneaux = form.getAffectationForm().getAffectations();
	    int i = 0;
	    for (AffectationCreneauForm acf : creneaux) {
		if (acf.getNbUnites() < 0) {
		    errors.rejectValue("affectationForm.affectations[" + i + "].nbUnites", "number.negative");
		}
		i++;
	    }
	}

	if (form.getSubmitFieldsTxtForm() != null) {
	    if (form.getSubmitFieldsTxtForm().getAttribution() != null) {
		// que les valeurs saisies au niveau des semaines est correcte ( >=0 et <=999)
		if (form.getSubmitFieldsTxtForm().getAttribution().getSemaine1() < 0) {
		    errors.rejectValue("submitFieldsTxtForm.attribution.semaine1", "number.negative");
		}
		if (form.getSubmitFieldsTxtForm().getAttribution().getSemaine2() < 0) {
		    errors.rejectValue("submitFieldsTxtForm.attribution.semaine2", "number.negative");
		}
		if (form.getSubmitFieldsTxtForm().getAttribution().getSemaine3() < 0) {
		    errors.rejectValue("submitFieldsTxtForm.attribution.semaine3", "number.negative");
		}
		if (form.getSubmitFieldsTxtForm().getAttribution().getSemaine4() < 0) {
		    errors.rejectValue("submitFieldsTxtForm.attribution.semaine4", "number.negative");
		}
		if (form.getSubmitFieldsTxtForm().getAttribution().getSemaine5() < 0) {
		    errors.rejectValue("submitFieldsTxtForm.attribution.semaine5", "number.negative");
		}
		if (form.getSubmitFieldsTxtForm().getAttribution().getSemaine1() > 999) {
		    errors.rejectValue("submitFieldsTxtForm.attribution.semaine1", "number.maxValue");
		}
		if (form.getSubmitFieldsTxtForm().getAttribution().getSemaine2() > 999) {
		    errors.rejectValue("submitFieldsTxtForm.attribution.semaine2", "number.maxValue");
		}
		if (form.getSubmitFieldsTxtForm().getAttribution().getSemaine3() > 999) {
		    errors.rejectValue("submitFieldsTxtForm.attribution.semaine3", "number.maxValue");
		}
		if (form.getSubmitFieldsTxtForm().getAttribution().getSemaine4() > 999) {
		    errors.rejectValue("submitFieldsTxtForm.attribution.semaine4", "number.maxValue");
		}
		if (form.getSubmitFieldsTxtForm().getAttribution().getSemaine5() > 999) {
		    errors.rejectValue("submitFieldsTxtForm.attribution.semaine5", "number.maxValue");
		}
	    }
	    if (form.getSubmitFieldsTxtForm().getReservation() != null) {
		// que les valeurs saisies au niveau des semaines est correcte ( >=0)
		if (form.getSubmitFieldsTxtForm().getReservation().getSemaine1() < 0) {
		    errors.rejectValue("submitFieldsTxtForm.reservation.semaine1", "number.negative");
		}
		if (form.getSubmitFieldsTxtForm().getReservation().getSemaine2() < 0) {
		    errors.rejectValue("submitFieldsTxtForm.reservation.semaine2", "number.negative");
		}
		if (form.getSubmitFieldsTxtForm().getReservation().getSemaine3() < 0) {
		    errors.rejectValue("submitFieldsTxtForm.reservation.semaine3", "number.negative");
		}
		if (form.getSubmitFieldsTxtForm().getReservation().getSemaine4() < 0) {
		    errors.rejectValue("submitFieldsTxtForm.reservation.semaine4", "number.negative");
		}
		if (form.getSubmitFieldsTxtForm().getReservation().getSemaine5() < 0) {
		    errors.rejectValue("submitFieldsTxtForm.reservation.semaine5", "number.negative");
		}
	    }

	    // option posees
	    if (form.getSubmitFieldsTxtForm().getOptionsPose() != null) {
		int i = 0;
		for (OptionPoseForm optionPoses : form.getSubmitFieldsTxtForm().getOptionsPose()) {
		    if (optionPoses != null) {
			if (optionPoses.getOptionsPose() < 0) {
			    errors.rejectValue("submitFieldsTxtForm.optionsPose[" + i + "].optionsPose", "number.negative");
			}
			if (optionPoses.getOptionsPose() > optionPoses.getPlaceDispo()) {
			    errors.rejectValue("submitFieldsTxtForm.optionsPose[" + i + "].optionsPose", "number.maxValue");
			}
		    }
		    i++;
		}
	    }
	}
    }

}

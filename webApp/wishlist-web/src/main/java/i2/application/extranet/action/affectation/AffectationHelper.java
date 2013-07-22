package i2.application.extranet.action.affectation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import i2.application.extranet.bean.view.affectation.AffectationCreneauHoraire;
import i2.application.extranet.bean.view.affectation.AffectationSeanceDetail;
import i2.application.extranet.bean.view.txt.PlanningJourTxtView;
import i2.application.extranet.form.affetation.AffectationCreneauForm;

public class AffectationHelper {

    Comparator<AffectationCreneauHoraire> creneauHoraireComparator = new Comparator<AffectationCreneauHoraire>() {
	@Override
	public int compare(AffectationCreneauHoraire o1, AffectationCreneauHoraire o2) {
	    if (o1.getActId() == o2.getActId()) {
		return Integer.valueOf(o1.getIndex()).compareTo(o2.getIndex());
	    } else {
		return Integer.valueOf(o1.getActId()).compareTo(o2.getActId());
	    }
	}
    };

    protected AffectationCreneauHoraire convertAffectationForm(AffectationCreneauForm affectationCreneauForm) {
	AffectationCreneauHoraire affectation = null;
	if (affectationCreneauForm.getActId() != null) {
	    affectation = new AffectationCreneauHoraire();
	    affectation.setActId(affectationCreneauForm.getActId());
	    affectation.setIndex(affectationCreneauForm.getIndex());
	    affectation.setNbPlaceResa(affectationCreneauForm.getNbUnites());
	}
	return affectation;
    }

    protected void convertData(final PlanningJourTxtView resultat, final AffectationSeanceDetail detail) {

	List<String> horaires = new ArrayList<>();
	List<Integer> activites = new ArrayList<>();
	for (AffectationCreneauHoraire ach : detail.getCreneauxHoraire()) {
	    if (!horaires.contains(ach.getHoraire())) {
		horaires.add(ach.getHoraire());
	    }
	    if (!activites.contains(ach.getActId())) {
		activites.add(ach.getActId());
	    }
	}
	Collections.sort(detail.getCreneauxHoraire(), creneauHoraireComparator);

    }

    protected void sortSeances(final AffectationSeanceDetail detail) {

	Collections.sort(detail.getCreneauxHoraire(), creneauHoraireComparator);

    }

    protected AffectationSeanceDetail cloneAffectationSeanceDetail(final AffectationSeanceDetail seance) {
	AffectationSeanceDetail clone = new AffectationSeanceDetail();
	clone.setPlacesDispo(seance.getPlacesDispo());
	clone.setPlacesPrises(seance.getPlacesPrises());
	clone.setPlacesRajoutees(seance.getPlacesRajoutees());
	clone.setPlacesResa(seance.getPlacesResa());
	clone.setPlacesTotal(seance.getPlacesTotal());
	for (AffectationCreneauHoraire ach : seance.getCreneauxHoraire()) {
	    clone.getCreneauxHoraire().add(cloneAffectationCreneauHoraire(ach));
	}
	return clone;
    }

    protected AffectationCreneauHoraire cloneAffectationCreneauHoraire(final AffectationCreneauHoraire creneau) {
	AffectationCreneauHoraire clone = new AffectationCreneauHoraire();
	clone.setActId(creneau.getActId());
	clone.setHoraire(creneau.getHoraire());
	clone.setIndex(creneau.getIndex());
	clone.setNbPlaceDispo(creneau.getNbPlaceDispo());
	clone.setNbPlaceRajout(creneau.getNbPlaceRajout());
	clone.setNbPlaceResa(creneau.getNbPlaceResa());
	clone.setNbPlaceTotal(creneau.getNbPlaceTotal());
	return clone;
    }

}

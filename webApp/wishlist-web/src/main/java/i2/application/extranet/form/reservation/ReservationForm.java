package i2.application.extranet.form.reservation;

import i2.application.extranet.bean.view.IViewLignePlanning;
import i2.application.extranet.bean.view.reservation.LigneReservationBean;
import i2.application.extranet.form.IPlanningSemaineForm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.AutoPopulatingList;
import org.springframework.util.AutoPopulatingList.ElementFactory;
import org.springframework.util.AutoPopulatingList.ElementInstantiationException;

/**
 * Formulaire de retour d'un planning semaine reservation
 * 
 * @author Bull
 * @category _return_form
 */
public class ReservationForm implements IPlanningSemaineForm {

    private List<LigneReservationBean> ligneReservation;
    private String aueCode;

    @SuppressWarnings("unchecked")
    public ReservationForm() {
	ligneReservation = new AutoPopulatingList(new ElementFactory() {
	    /*
	     * Attention il faut que la liste auto populer soit ordonner! i.e // get(0), get(1), get(2),... get(n)
	     * 
	     * @see org.springframework.util.AutoPopulatingList.ElementFactory#createElement(int)
	     */
	    @Override
	    public Object createElement(int index) throws ElementInstantiationException {
		return new LigneReservationBean();
	    }
	});

    }

    @Override
    public List<IViewLignePlanning> getLignePlanning() {
	List<IViewLignePlanning> listeRetour = new ArrayList<IViewLignePlanning>();
	for (LigneReservationBean ligne : ligneReservation) {
	    listeRetour.add(ligne);
	}
	return listeRetour;
    }

    @Override
    public String getAueCodeSelected() {
	return aueCode;
    }

    /**
     * @return the ligneReservation
     */
    public List<LigneReservationBean> getLigneReservation() {
	return ligneReservation;
    }

    /**
     * @param ligneReservation
     *            the ligneReservation to set
     */
    public void setLigneReservation(List<LigneReservationBean> ligneReservation) {
	this.ligneReservation = ligneReservation;
    }

    /**
     * @return the aueCode
     */
    public String getAueCode() {
	return aueCode;
    }

    /**
     * @param aueCode
     *            the aueCode to set
     */
    public void setAueCode(String aueCode) {
	this.aueCode = aueCode;
    }

}

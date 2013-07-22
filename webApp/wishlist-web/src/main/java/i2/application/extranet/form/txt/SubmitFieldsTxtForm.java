package i2.application.extranet.form.txt;

import i2.application.extranet.bean.view.attribution.LigneAttributionBean;

import i2.application.extranet.bean.view.reservation.LigneReservationBean;
import i2.application.extranet.form.option.OptionPoseForm;

import java.util.List;

import org.springframework.util.AutoPopulatingList;
import org.springframework.util.AutoPopulatingList.ElementFactory;
import org.springframework.util.AutoPopulatingList.ElementInstantiationException;

public class SubmitFieldsTxtForm {

    private List<OptionPoseForm> optionsPose;
    private LigneReservationBean reservation;
    private LigneAttributionBean attribution;

    @SuppressWarnings("unchecked")
    public SubmitFieldsTxtForm() {
	setOptionsPose(new AutoPopulatingList(new ElementFactory() {
	    /*
	     * Attention il faut que la liste auto populer soit ordonner! i.e // get(0), get(1), get(2),... get(n)
	     * 
	     * @see org.springframework.util.AutoPopulatingList.ElementFactory#createElement(int)
	     */
	    @Override
	    public Object createElement(int index) throws ElementInstantiationException {
		return new OptionPoseForm();
	    }
	}));
	setReservation(new LigneReservationBean());
	setAttribution(new LigneAttributionBean());
    }

    public List<OptionPoseForm> getOptionsPose() {
	return optionsPose;
    }

    public void setOptionsPose(List<OptionPoseForm> optionsPose) {
	this.optionsPose = optionsPose;
    }

    public LigneReservationBean getReservation() {
	return reservation;
    }

    public void setReservation(LigneReservationBean ligneReservationBean) {
	this.reservation = ligneReservationBean;
    }

    public LigneAttributionBean getAttribution() {
	return attribution;
    }

    public void setAttribution(LigneAttributionBean attribution) {
	this.attribution = attribution;
    }
}

package i2.application.extranet.form.option;

import i2.application.extranet.bean.view.option.ViewAUEAttribUnitesSupp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.AutoPopulatingList;

/**
 * form pour le retour des elements modifies
 * 
 * @author bull
 * 
 */
public class AttribuerUnitesSuppForm {

    private List<ViewAUEAttribUnitesSupp> attributions = new AutoPopulatingList(ViewAUEAttribUnitesSupp.class);

    private String centre;
    private String salle;
    private List<Long> timestamps = new ArrayList<>();
    private List<Integer> actIds = new ArrayList<>();

    public String getCentre() {
	return centre;
    }

    public void setCentre(String centre) {
	this.centre = centre;
    }

    public String getSalle() {
	return salle;
    }

    public void setSalle(String salle) {
	this.salle = salle;
    }

    /*
     * @SuppressWarnings("unchecked") public AttribuerUnitesSuppForm() {
     * 
     * attributions = new AutoPopulatingList(new ElementFactory() {
     * 
     * @Override public Object createElement(int index) throws ElementInstantiationException { attributions.removeAll(Collections.singletonList(null)); return new ViewAUEAttribUnitesSupp(); } });
     * 
     * 
     * }
     */

    public List<ViewAUEAttribUnitesSupp> getAttributions() {
	if (attributions == null) {
	    attributions = new ArrayList<>();
	}
	return attributions;
    }

    public void setAttributions(List<ViewAUEAttribUnitesSupp> attributions) {
	this.attributions = attributions;
    }

    public List<Long> getTimestamps() {
	return timestamps;
    }

    public void setTimestamps(List<Long> timestamps) {
	this.timestamps = timestamps;
    }

    public List<Integer> getActIds() {
	return actIds;
    }

    public void setActIds(List<Integer> actIds) {
	this.actIds = actIds;
    }

}

package i2.application.extranet.form.affetation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.AutoPopulatingList;
import org.springframework.util.AutoPopulatingList.ElementFactory;
import org.springframework.util.AutoPopulatingList.ElementInstantiationException;

public class AffectationForm {

    List<AffectationCreneauForm> affectations;
    private String aueCodeSelected;
    private List<Long> timestamps = new ArrayList<>();
    private List<Integer> actIds = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public AffectationForm() {
	affectations = new AutoPopulatingList(new ElementFactory() {
	    @Override
	    public Object createElement(int index) throws ElementInstantiationException {
		return new AffectationCreneauForm();
	    }
	});
    }

    public List<AffectationCreneauForm> getAffectations() {
	if (affectations == null) {
	    affectations = new ArrayList<>();
	}
	return affectations;
    }

    public void setAffectations(List<AffectationCreneauForm> affectations) {
	this.affectations = affectations;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("AffectationForm [affectations=");
	for (AffectationCreneauForm affectationCreneauForm : affectations) {
	    builder.append(affectationCreneauForm).append(", ");
	}
	builder.setLength(builder.length() - 2);
	builder.append("]");
	return builder.toString();
    }

    /**
     * @return the aueCodeSelected
     */
    public String getAueCodeSelected() {
	return aueCodeSelected;
    }

    /**
     * @param aueCodeSelected
     *            the aueCodeSelected to set
     */
    public void setAueCodeSelected(String aueCodeSelected) {
	this.aueCodeSelected = aueCodeSelected;
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

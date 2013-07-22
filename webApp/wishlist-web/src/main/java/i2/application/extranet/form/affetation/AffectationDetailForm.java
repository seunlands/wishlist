package i2.application.extranet.form.affetation;

import java.util.ArrayList;
import java.util.List;

public class AffectationDetailForm {

    private List<Integer> ids = new ArrayList<>();
    private String aueCodeSelected;

    public List<Integer> getIds() {
	return ids;
    }

    public void setIds(List<Integer> ids) {
	if (ids != null) {
	    this.ids.clear();
	    this.ids.addAll(ids);
	}
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("AffectationsDetailForm [ids=");
	builder.append(ids);
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

}

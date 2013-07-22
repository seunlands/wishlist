package i2.application.extranet.form.attribution;

import i2.application.extranet.bean.view.IViewLignePlanning;
import i2.application.extranet.bean.view.attribution.LigneAttributionBean;
import i2.application.extranet.form.IPlanningSemaineForm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.AutoPopulatingList;
import org.springframework.util.AutoPopulatingList.ElementFactory;
import org.springframework.util.AutoPopulatingList.ElementInstantiationException;

/**
 * Formulaire de retour d'un planning semaine attribution
 * 
 * @author Bull
 * 
 */
public class AttributionForm implements IPlanningSemaineForm {

    private List<LigneAttributionBean> ligneAttribution;
    private String aueCode;

    @SuppressWarnings("unchecked")
    public AttributionForm() {
	ligneAttribution = new AutoPopulatingList(new ElementFactory() {
	    /*
	     * Attention il faut que la liste auto populer soit ordonner! i.e get(0), get(1), get(2),... get(n) (non-Javadoc)
	     * 
	     * @see org.springframework.util.AutoPopulatingList.ElementFactory#createElement(int)
	     */
	    @Override
	    public Object createElement(int index) throws ElementInstantiationException {
		return new LigneAttributionBean();
	    }
	});

    }

    @Override
    public List<IViewLignePlanning> getLignePlanning() {
	List<IViewLignePlanning> listeRetour = new ArrayList<IViewLignePlanning>();
	for (LigneAttributionBean ligne : ligneAttribution) {
	    listeRetour.add(ligne);
	}
	return listeRetour;
    }

    @Override
    public String getAueCodeSelected() {
	return aueCode;
    }

    /**
     * @return the ligneAttribution
     */
    public List<LigneAttributionBean> getLigneAttribution() {
	return ligneAttribution;
    }

    /**
     * @param ligneAttribution
     *            the ligneAttribution to set
     */
    public void setLigneAttribution(List<LigneAttributionBean> ligneAttribution) {
	this.ligneAttribution = ligneAttribution;
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

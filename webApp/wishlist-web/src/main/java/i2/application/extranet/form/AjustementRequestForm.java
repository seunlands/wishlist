package i2.application.extranet.form;

import i2.application.extranet.bean.view.multigroup.AbstractViewLigneMultiGroup;
import i2.application.extranet.bean.view.multigroup.LigneAjustementMultiGroup;

import java.util.List;

import org.springframework.util.AutoPopulatingList;
import org.springframework.util.AutoPopulatingList.ElementFactory;
import org.springframework.util.AutoPopulatingList.ElementInstantiationException;

public class AjustementRequestForm {

    private String anneeMois;
    private String group;
    private List<AbstractViewLigneMultiGroup> ligneAjustement;

    @SuppressWarnings("unchecked")
    public AjustementRequestForm() {
	ligneAjustement = new AutoPopulatingList(new ElementFactory() {
	    /*
	     * Attention il faut que la liste auto populer soit ordonner! i.e // get(0), get(1), get(2),... get(n)
	     * 
	     * @see org.springframework.util.AutoPopulatingList.ElementFactory#createElement(int)
	     */
	    @Override
	    public Object createElement(int index) throws ElementInstantiationException {
		return new LigneAjustementMultiGroup();
	    }
	});
    }

    public String getAnneeMois() {
	return anneeMois;
    }

    public void setAnneeMois(String anneeMois) {
	this.anneeMois = anneeMois;
    }

    public String getGroup() {
	return group;
    }

    public void setGroup(String group) {
	this.group = group;
    }

    /**
     * @return the listLignes
     */
    public List<AbstractViewLigneMultiGroup> getLigneAjustement() {
	return ligneAjustement;
    }

    /**
     * @param listLignes
     *            the listLignes to set
     */
    public void setLigneAjustement(List<AbstractViewLigneMultiGroup> listLignes) {
	this.ligneAjustement = listLignes;
    }

}

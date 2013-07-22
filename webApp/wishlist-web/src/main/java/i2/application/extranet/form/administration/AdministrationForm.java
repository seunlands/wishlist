package i2.application.extranet.form.administration;

import i2.application.extranet.bean.view.administration.ParametreDefaultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Form Spring pour le formulaire de la page de gestion des paramètres
 * 
 * @author Bull
 * 
 */
public class AdministrationForm {
    private List<ParametreDefaultBean> listParametres;

    public AdministrationForm(List<ParametreDefaultBean> params) {
	listParametres = params;
    }

    public AdministrationForm() {
	listParametres = new ArrayList<ParametreDefaultBean>();
    }

    public List<ParametreDefaultBean> getListParametres() {
	return listParametres;
    }

    public void setListParametres(List<ParametreDefaultBean> listParametres) {
	this.listParametres = listParametres;
    }

}

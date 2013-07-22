package i2.application.extranet.form.alertes;

import i2.application.extranet.bean.view.alertes.ViewAlertes;

import java.util.ArrayList;
import java.util.List;

/**
 * form de la partie alerte
 * 
 * @author bull
 * 
 */
public class AlertesForm {

    private List<ViewAlertes> lstAlertes;

    public List<ViewAlertes> getLstAlertes() {
	return lstAlertes;
    }

    public void setLstAlertes(List<ViewAlertes> lstAlertes) {
	this.lstAlertes.clear();
	this.lstAlertes.addAll(lstAlertes);
    }

    public AlertesForm() {
	super();
	lstAlertes = new ArrayList<ViewAlertes>();
    }

}

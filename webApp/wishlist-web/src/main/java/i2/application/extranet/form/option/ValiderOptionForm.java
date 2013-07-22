package i2.application.extranet.form.option;

import java.util.ArrayList;
import java.util.List;

public class ValiderOptionForm {

    List<Integer> ids = new ArrayList<Integer>();

    private String centre;
    private String salle;
    private Integer salleId;
    private String cjo;
    private String jour;

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

    public List<Integer> getIds() {
	return ids;
    }

    public void setIds(List<Integer> ids) {
	if (ids != null) {
	    this.ids.clear();
	    this.ids.addAll(ids);
	}
    }

    public Integer getSalleId() {
	return salleId;
    }

    public void setSalleId(Integer salleId) {
	this.salleId = salleId;
    }

    public String getCjo() {
	return cjo;
    }

    public void setCjo(String cjo) {
	this.cjo = cjo;
    }

    public String getJour() {
	return jour;
    }

    public void setJour(String jour) {
	this.jour = jour;
    }

}

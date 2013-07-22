package i2.application.extranet.form.recevabilite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author bull
 * 
 *         formulaire de recherche de la recevabilité d'un candidat
 * 
 */
public class RecevabiliteForm {

    public RecevabiliteForm() {
	super();
	listeEpreuve = new ArrayList<>();
	// TODO Auto-generated constructor stub
    }

    public RecevabiliteForm(String canNeph, String tepCode, Date dateExam) {
	super();
	this.canNeph = canNeph;
	this.tepCode = tepCode;
	this.dateExam = dateExam;
    }

    public String getCanNeph() {
	return canNeph;
    }

    public void setCanNeph(String canNeph) {
	this.canNeph = canNeph;
    }

    public String getTepCode() {
	return tepCode;
    }

    public void setTepCode(String tepCode) {
	this.tepCode = tepCode;
    }

    public Date getDateExam() {
	return dateExam;
    }

    public void setDateExam(Date dateExam) {
	this.dateExam = dateExam;
    }

    public List<String> getListeEpreuve() {
	return listeEpreuve;
    }

    public void setListeEpreuve(List<String> listeEpreuve) {
	this.listeEpreuve = listeEpreuve;
    }

    private String canNeph;
    private String tepCode;
    private Date dateExam;
    private List<String> listeEpreuve;

}

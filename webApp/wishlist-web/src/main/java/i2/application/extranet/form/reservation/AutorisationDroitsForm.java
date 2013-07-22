package i2.application.extranet.form.reservation;

/**
 * 
 * @author BULL
 * 
 */
public class AutorisationDroitsForm {

    /**
     * Attribut mappé sur la liste déroulante dans la jsp
     */
    private String anneeMoisSelectionne;

    /**
     * Constructeur sans Argument
     */
    public AutorisationDroitsForm() {
    }

    /**
     * Constructeur sans Argument
     */
    public AutorisationDroitsForm(String anneeMois) {
	this.anneeMoisSelectionne = anneeMois;
    }

    /**
     * @return the anneeMoisSelectionne
     */
    public String getAnneeMoisSelectionne() {
	return anneeMoisSelectionne;
    }

    /**
     * @param anneeMoisSelectionne
     *            the anneeMoisSelectionne to set
     */
    public void setAnneeMoisSelectionne(String anneeMoisSelectionne) {
	this.anneeMoisSelectionne = anneeMoisSelectionne;
    }

}
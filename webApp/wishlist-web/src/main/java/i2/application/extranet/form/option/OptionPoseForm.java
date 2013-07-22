package i2.application.extranet.form.option;

import i2.application.extranet.enums.EnumEtatSeance;

/**
 * @author bull
 * 
 *         form réponse OptionsPose
 * 
 */
public class OptionPoseForm {

    private int optionsPose;
    private int actId;
    private int placeDispo;
    private String aueCodeSelected;
    private int nbActOptions;
    private EnumEtatSeance etatSeance;

    public int getOptionsPose() {
	return optionsPose;
    }

    public void setOptionsPose(int optionsPose) {
	this.optionsPose = optionsPose;
    }

    public int getActId() {
	return actId;
    }

    public void setActId(int actId) {
	this.actId = actId;
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

    public int getNbActOptions() {
	return nbActOptions;
    }

    public int getPlaceDispo() {
	return placeDispo;
    }

    public void setNbActOptions(int nbActOptions) {
	this.nbActOptions = nbActOptions;
    }

    public void setPlaceDispo(int placeDispo) {
	this.placeDispo = placeDispo;
    }

    public EnumEtatSeance getEtatSeance() {
	return etatSeance;
    }

    public void setEtatSeance(EnumEtatSeance etatSeance) {
	this.etatSeance = etatSeance;
    }

}

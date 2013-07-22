package i2.application.extranet.form.txt;

import i2.application.extranet.bean.view.txt.RechercheTxt;
import i2.application.extranet.form.affetation.AffectationDetailForm;
import i2.application.extranet.form.affetation.AffectationForm;

public class RechercheTxtCompositeForm {

    private RechercheTxt rechercheForm;

    private AffectationDetailForm detailForm;

    private SubmitFieldsTxtForm submitFieldsTxtForm;

    private AffectationForm affectationForm;

    public RechercheTxtCompositeForm() {
	rechercheForm = new RechercheTxt();
	submitFieldsTxtForm = new SubmitFieldsTxtForm();
	detailForm = new AffectationDetailForm();
	affectationForm = new AffectationForm();
    }

    public RechercheTxt getRechercheForm() {
	return rechercheForm;
    }

    public AffectationForm getAffectationForm() {
	return affectationForm;
    }

    public void setAffectationForm(AffectationForm affectationForm) {
	this.affectationForm = affectationForm;
    }

    public void setRechercheForm(RechercheTxt rechercheForm) {
	this.rechercheForm = rechercheForm;
    }

    public SubmitFieldsTxtForm getSubmitFieldsTxtForm() {
	return submitFieldsTxtForm;
    }

    public void setSubmitFieldsTxtForm(SubmitFieldsTxtForm submitFieldsTxtForm) {
	this.submitFieldsTxtForm = submitFieldsTxtForm;
    }

    public AffectationDetailForm getDetailForm() {
	return detailForm;
    }

    public void setDetailForm(AffectationDetailForm detailForm) {
	this.detailForm = detailForm;
    }

}

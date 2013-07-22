package i2.application.extranet.action.option;

import i2.application.extranet.action.AbstractPlanningJourTxtController;
import i2.application.extranet.bean.view.IViewLignePlanning;
import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.bean.view.option.ViewRecapOptionsByPermis;
import i2.application.extranet.bean.view.txt.ResultatTxtView;
import i2.application.extranet.business.exceptions.ConcurrentDataAccessException;
import i2.application.extranet.business.exceptions.TechnicalError;
import i2.application.extranet.business.option.IOptionManager;
import i2.application.extranet.enums.EnumEtatSeance;
import i2.application.extranet.form.option.OptionPoseForm;
import i2.application.extranet.form.txt.RechercheTxtCompositeForm;
import i2.application.extranet.form.txt.SubmitFieldsTxtForm;
import i2.application.extranet.utils.Constants;
import i2.application.extranet.validator.option.OptionPoseFormValidator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller d'action pour le tableau de la pose d'otpions
 * 
 * @author Bull
 * 
 */
public class OptionPoseTxtController extends AbstractPlanningJourTxtController {

    private final static Logger LOGGER = Logger.getLogger(OptionPoseTxtController.class);

    private IOptionManager optionsPoseManager;

    public OptionPoseTxtController() {
	super();
	setValidators(new Validator[] { new OptionPoseFormValidator() });
    }

    public void setOptionsPoseManager(IOptionManager optionsPoseManager) {
	this.optionsPoseManager = optionsPoseManager;
    }

    @Override
    protected Object getRecapitulatif(HttpServletRequest request, int annee, int mois, List<IViewLignePlanning> lstPlanning) {
	List<ViewRecapOptionsByPermis> lstRecap = optionsPoseManager.getRecapOptionsByPermis(getAutoEcoleConnected(request), annee, mois, lstPlanning);
	return lstRecap;
    }

    @Override
    public ModelAndView submitModification(HttpServletRequest request, HttpServletResponse response, Object command2) {

	RechercheTxtCompositeForm command1 = (RechercheTxtCompositeForm) command2;
	SubmitFieldsTxtForm command = command1.getSubmitFieldsTxtForm();

	if (LOGGER.isDebugEnabled()) {
	    LOGGER.debug("#submit : form = " + command);
	}

	ResultatTxtView resultat = new ResultatTxtView();

	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);

	try {
	    for (OptionPoseForm pose : command.getOptionsPose()) {
		if (pose != null && (pose.getActId() != 0 && pose.getOptionsPose() >= 0 && !pose.getEtatSeance().equals(EnumEtatSeance.ETAT_SEANCE_CLOTUREE))) {
		    if (LOGGER.isDebugEnabled())
			LOGGER.debug(pose.getActId() + " : " + pose.getOptionsPose());
		    optionsPoseManager.savePositionOptionsByAueByActivite(getAutoEcoleConnected(request), userConnected.getEmail(), pose.getActId(), pose.getOptionsPose(), pose.getNbActOptions());
		}
	    }
	    resultat.getSuccessMessages().add(messages.getMessage("msg.common.modifs.enregistrees"));
	} catch (TechnicalError e) {
	    LOGGER.warn("erreur technique lors de l'enregistrement", e);
	    resultat.getErrorMessages().add(e.getMessage());
	} catch (ConcurrentDataAccessException e) {
	    LOGGER.warn("erreur de concurrence d'accès aux données", e);
	    resultat.getErrorMessages().add(messages.getMessage("option.error.concurrence"));
	}

	updateModel(request, response, command1);

	ModelAndView mv = createModelAndView(command1, request, null);
	mv.addObject(Constants.MESSAGES_MODEL_TXT, resultat);
	return mv;

    }

}

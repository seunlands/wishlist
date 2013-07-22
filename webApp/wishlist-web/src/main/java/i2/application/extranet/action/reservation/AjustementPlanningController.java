package i2.application.extranet.action.reservation;

import i2.application.extranet.action.AbstractMultiGroupingPlanningController;
import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.bean.view.multigroup.AbstractViewLigneMultiGroup;
import i2.application.extranet.bean.view.multigroup.LigneAjustementMultiGroup;
import i2.application.extranet.business.exceptions.ApplicationException;
import i2.application.extranet.business.exceptions.DataValidateWarning;
import i2.application.extranet.business.reservation.IAjusterReservationManager;
import i2.application.extranet.enums.EnumProfils;
import i2.application.extranet.form.AjustementRequestForm;
import i2.application.extranet.utils.ActionFactory;
import i2.application.extranet.utils.AsyncResponse;
import i2.application.extranet.utils.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

public class AjustementPlanningController extends AbstractMultiGroupingPlanningController {

    private static final Logger LOGGER = Logger.getLogger(AjustementPlanningController.class);

    @Override
    public ModelAndView submitModification(HttpServletRequest request, HttpServletResponse response, AjustementRequestForm form) throws Exception {
	AsyncResponse servletResponse = ActionFactory.createResponse(response);

	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);

	if (LOGGER.isInfoEnabled()) {
	    for (AbstractViewLigneMultiGroup line : form.getLigneAjustement()) {
		LOGGER.info("... ligneAjustement: " + line.getAnneeMois() + "  " + line.getPermis() + "  " + line.getCodeRafaelAutoEcole() + "  " + line.getLibelleAutoEcole() + "  "
			+ line.getCentre() + "  " + line.getCexNumero() + "  " + ((LigneAjustementMultiGroup) line).getCls() + "  " + ((LigneAjustementMultiGroup) line).getRetenues());
	    }
	}

	if (userConnected.getLibelleProfil().equals(EnumProfils.REPARTITEUR.getLibelle())) {
	    try {
		((IAjusterReservationManager) groupedPlanning).saveModifPlanning(form.getLigneAjustement(), getDepartementConnected(request), getMailUserConnected(request),
			getProfilUserConnected(request));
		servletResponse.addMessage(messages.getMessage("msg.common.modifs.enregistrees"));
		servletResponse.sendSuccess();
	    } catch (DataValidateWarning e) {
		servletResponse.addMessage(messages.getMessage("msg.common.modifs.enregistrees"));
		for (String msg : e.getListMessage()) {
		    servletResponse.addMessage(msg);
		}
		servletResponse.sendWarn();
	    } catch (ApplicationException e) {
		servletResponse.sendFail();
		LOGGER.error(e.getMessage(), e);
	    } catch (Exception e) {
		LOGGER.fatal(e.getMessage(), e);
		servletResponse.sendFail();
		throw e;
	    }
	}
	return null;
    }

    @Override
    public boolean isGroupByCentreExamen() {
	return false;
    }

    @Override
    public boolean isGroupByGroupePermis() {
	return true;
    }

    @Override
    public int getNbrParPage() {
	return Integer.parseInt(getServletContext().getInitParameter("nbAutoEcoleParPage"));
    }

    @Override
    public String getElementReferencePagination(AbstractViewLigneMultiGroup ligne) {
	return ligne.getCodeAutoEcole();
    }

    @Override
    public boolean isNotGrouped() {
	return false;
    }

}

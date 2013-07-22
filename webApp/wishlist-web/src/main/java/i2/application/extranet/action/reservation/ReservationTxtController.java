package i2.application.extranet.action.reservation;

import i2.application.aurige.bean.AutoEcole;
import i2.application.extranet.action.PlanningSemTxtController;
import i2.application.extranet.bean.view.IViewLignePlanning;
import i2.application.extranet.bean.view.ViewDroitMensuel;
import i2.application.extranet.bean.view.ViewPermis;
import i2.application.extranet.bean.view.txt.PlanningSemTxtView;
import i2.application.extranet.business.exceptions.ApplicationException;
import i2.application.extranet.business.reservation.IReservationManager;
import i2.application.extranet.business.util.BeanViewUtil;
import i2.application.extranet.form.txt.RechercheTxtCompositeForm;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author BULL SAS
 * 
 *         Contrôleur d'actions pour le tableau reservation d'unités
 */

public class ReservationTxtController extends PlanningSemTxtController {

    private final static Logger LOGGER = Logger.getLogger(ReservationTxtController.class);

    protected IReservationManager reservationManager;

    public void setReservationManager(IReservationManager reservationManager) {
	this.reservationManager = reservationManager;
    }

    @Override
    protected ModelAndView afficher(HttpServletRequest request, HttpServletResponse response, RechercheTxtCompositeForm command) {
	LOGGER.debug("#reservationTxtController.afficher");
	ModelAndView mv = super.afficher(request, response, command);

	return mv;
    }

    @Override
    public ModelAndView submit(HttpServletRequest request, HttpServletResponse response, RechercheTxtCompositeForm command) {
	ModelAndView mv = super.submit(request, response, command);
	return mv;
    }

    @Override
    protected void saveAbstract(List<IViewLignePlanning> lignePlanning, List<IViewLignePlanning> lstPlanningSession, AutoEcole ecole, String mailUser, String profilUserConnected,
	    PlanningSemTxtView resultat) throws ApplicationException {

	reservationManager.saveModifPlanning(lignePlanning, lstPlanningSession, ecole, mailUser, profilUserConnected);

    }

    @Override
    protected PlanningSemTxtView updateRecap(PlanningSemTxtView resultat, List<IViewLignePlanning> lstLignesSession, String userProfilConnected, AutoEcole ecole) {
	// Récupération des droits Mensuels mis à jour
	Calendar anneeMois = resultat.getCalendarAnneeMois();
	List<ViewPermis> lstPermisView = BeanViewUtil.getListGroupePermis(lstLignesSession);
	List<ViewDroitMensuel> lstRecapitulatif = reservationManager.getListDroitsMensuelPourCriteres(ecole, ecole.getDepartement(), anneeMois.get(Calendar.YEAR), anneeMois.get(Calendar.MONTH) + 1,
		userProfilConnected, lstPermisView);

	// Alimenter la liste Recap par les unités réservées (resp. attribuées) pour la phase de réservation (resp. attribution)
	Object lstRecapToSend = abstractPlanningSem.getListDroitsMensuelPourCriteresWithNbrUnites(lstRecapitulatif, lstLignesSession);

	resultat.setRecapitulatif(lstRecapToSend);

	return resultat;
    }
}

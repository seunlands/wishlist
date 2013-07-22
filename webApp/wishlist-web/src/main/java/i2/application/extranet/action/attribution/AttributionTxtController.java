package i2.application.extranet.action.attribution;

import java.util.ArrayList;
import java.util.List;

import i2.application.aurige.bean.AutoEcole;
import i2.application.extranet.action.PlanningSemTxtController;
import i2.application.extranet.bean.view.IViewLignePlanning;
import i2.application.extranet.bean.view.ViewPermis;
import i2.application.extranet.bean.view.affectation.RecapUnitesCount;
import i2.application.extranet.bean.view.attribution.LigneAttributionBean;
import i2.application.extranet.bean.view.attribution.ViewRecapAttribution;
import i2.application.extranet.bean.view.txt.PlanningSemTxtView;
import i2.application.extranet.business.attribution.IAttributionManager;
import i2.application.extranet.business.exceptions.ApplicationException;
import i2.application.extranet.business.util.BeanViewUtil;
import i2.application.extranet.enums.EnumProfils;
import i2.application.extranet.form.txt.RechercheTxtCompositeForm;
import i2.application.extranet.utils.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import csb.common.tools.date.MonthForCalendar;

/**
 * Contrôleur d'actions pour le tableau d'attribution des unites
 * 
 * @author Bull
 * 
 */
public class AttributionTxtController extends PlanningSemTxtController {

    private final static Logger LOGGER = Logger.getLogger(AttributionTxtController.class);

    private IAttributionManager attributionManager;

    public void setAttributionManager(IAttributionManager attributionManager) {
	this.attributionManager = attributionManager;
    }

    @Override
    protected ModelAndView afficher(HttpServletRequest request, HttpServletResponse response, RechercheTxtCompositeForm command) {
	LOGGER.debug("#attributionTxtController.afficher");
	ModelAndView mv = super.afficher(request, response, command);

	PlanningSemTxtView resultat = (PlanningSemTxtView) mv.getModel().get(Constants.RESULTATS_MODEL_TXT);

	MonthForCalendar moisExamen = new MonthForCalendar(resultat.getCalendarAnneeMois());
	boolean history = false;
	boolean isTTEnCours = false;
	boolean isEditable = attributionManager.isEditable(getDepartementConnected(request), moisExamen.getAnneeMois(), getProfilUserConnected(request));
	if (!isEditable & EnumProfils.LECTEUR.getLibelle().equals(getProfilUserConnected(request))) {
	    history = !attributionManager.isEditable(getDepartementConnected(request), moisExamen.getAnneeMois(), EnumProfils.REPARTITEUR.getLibelle());
	} else {
	    history = !isEditable;
	}
	if (!history) {
	    isTTEnCours = attributionManager.isTableauTravailEnCoursModification(moisExamen.getAnneeMois(), getDepartementConnected(request));
	}
	request.setAttribute("isTTEnCours", isTTEnCours);

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
	attributionManager.saveModifPlanning(lignePlanning, lstPlanningSession, ecole, mailUser, profilUserConnected);
    }

    @Override
    protected PlanningSemTxtView updateRecap(PlanningSemTxtView resultat, List<IViewLignePlanning> lstLignePlanningSession, String userProfilConnected, AutoEcole ecole) {
	List<ViewPermis> lstPermis = BeanViewUtil.getListGroupePermis(lstLignePlanningSession);
	List<ViewRecapAttribution> lstRecap = new ArrayList<>();
	for (ViewPermis permis : lstPermis) {
	    int nbAttribuees = 0;
	    int nbRetenues = 0;
	    for (IViewLignePlanning ligne : lstLignePlanningSession) {
		LigneAttributionBean ligneAttrib = (LigneAttributionBean) ligne;
		if (ligneAttrib.getPermis().equals(permis.getId())) {
		    nbAttribuees += ligneAttrib.getSemaine1();
		    nbAttribuees += ligneAttrib.getSemaine2();
		    nbAttribuees += ligneAttrib.getSemaine3();
		    nbAttribuees += ligneAttrib.getSemaine4();
		    nbAttribuees += ligneAttrib.getSemaine5();
		    nbRetenues += ligneAttrib.getRetenueMensuelle();
		}
	    }
	    ViewRecapAttribution recap = new ViewRecapAttribution();
	    recap.setPermis(permis.getId());
	    recap.setNbUnitesAttribuees(nbAttribuees);
	    recap.setNbUnitesRetenues(nbRetenues);
	    lstRecap.add(recap);
	}
	resultat.setRecapitulatif(lstRecap);
	return resultat;
    }

}

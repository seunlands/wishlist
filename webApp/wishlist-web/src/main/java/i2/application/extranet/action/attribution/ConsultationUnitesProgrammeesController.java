package i2.application.extranet.action.attribution;

import i2.application.extranet.action.AbstractMultiGroupingPlanningController;
import i2.application.extranet.bean.view.multigroup.AbstractViewLigneMultiGroup;
import i2.application.extranet.business.attribution.IAttributionManager;
import i2.application.extranet.form.AjustementRequestForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class ConsultationUnitesProgrammeesController extends AbstractMultiGroupingPlanningController {

    private IAttributionManager attributionManager;

    // FIXME Attetion cette méthode n'est valable que pour le panneau de consultation d'unités programmées
    @Override
    public ModelAndView submitModification(HttpServletRequest request, HttpServletResponse response, AjustementRequestForm form) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public boolean isGroupByCentreExamen() {
	return false;
    }

    @Override
    public boolean isGroupByGroupePermis() {
	return false;
    }

    @Override
    public int getNbrParPage() {
	return Integer.parseInt(getServletContext().getInitParameter("nbPermisParPageUnitesProgrammees"));
    }

    @Override
    public String getElementReferencePagination(AbstractViewLigneMultiGroup ligne) {
	return ligne.getPermis();
    }

    @Override
    public boolean isNotGrouped() {
	return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see i2.application.extranet.action.AbstractMultiGroupingPlanningController#init(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) throws Exception {
	ModelAndView mv = super.init(request, response);
	String moisSelection = (String) request.getAttribute("moisSelection");
	boolean history = (boolean) request.getAttribute("history");
	boolean isTTEnCours = false;
	if (moisSelection != null && !history) {
	    isTTEnCours = attributionManager.isTableauTravailEnCoursModification(moisSelection, getDepartementConnected(request));
	}
	request.setAttribute("isTTEnCours", isTTEnCours);

	return mv;
    }

    /**
     * @param attributionManager
     *            the attributionManager to set
     */
    public void setAttributionManager(IAttributionManager attributionManager) {
	this.attributionManager = attributionManager;
    }
}
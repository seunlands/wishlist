package i2.application.extranet.action;

import i2.application.aurige.bean.AutoEcole;
import i2.application.aurige.bean.Departement;
import i2.application.extranet.bean.compare.CompareMultiGroupLineByAe;
import i2.application.extranet.bean.compare.CompareMultiGroupLineByPermis;
import i2.application.extranet.bean.compare.CompareMultiGroupLineByPermisAndCentre;
import i2.application.extranet.bean.view.IViewCritere;
import i2.application.extranet.bean.view.ViewAE;
import i2.application.extranet.bean.view.ViewAnneeMois;
import i2.application.extranet.bean.view.ViewCentreExamen;
import i2.application.extranet.bean.view.ViewPermis;
import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.bean.view.multigroup.AbstractViewLigneMultiGroup;
import i2.application.extranet.bean.view.multigroup.GroupeLignePlanning;
import i2.application.extranet.business.IMultiGroupedPlanningManager;
import i2.application.extranet.enums.EnumProfils;
import i2.application.extranet.form.AjustementRequestForm;
import i2.application.extranet.utils.ActionFactory;
import i2.application.extranet.utils.Constants;
import i2.application.extranet.utils.WebBundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * 
 * @author BULL SAS
 * 
 *         Contrôleur d'actions pour le tableau reservation d'unitées
 */

public abstract class AbstractMultiGroupingPlanningController extends MultiActionController {

    private final static Logger LOGGER = Logger.getLogger(AbstractMultiGroupingPlanningController.class);
    protected WebBundle messages;
    protected IMultiGroupedPlanningManager groupedPlanning;
    private String constantForJson;
    private String planningViewName;
    private final SimpleDateFormat formatId = new SimpleDateFormat("yyyyMM");

    /**
     * Determine si le controlleur MultiGroupe sur le centre d'examen
     * 
     * @return true pour grouper sur le centre d'examen
     */
    public abstract boolean isGroupByCentreExamen();

    /**
     * Determine si le controlleur MultiGroupe sur le groupe d'examen
     * 
     * @return true pour grouper sur le groupe d'examen
     */
    public abstract boolean isGroupByGroupePermis();

    /**
     * Determine si le controlleur MultiGroupe par dept
     * 
     * @return true pour grouper par dept
     */
    public abstract boolean isNotGrouped();

    public IMultiGroupedPlanningManager getGroupedPlanning() {
	return groupedPlanning;
    }

    public void setGroupedPlanning(IMultiGroupedPlanningManager groupedPlanning) {
	this.groupedPlanning = groupedPlanning;
    }

    public String getConstantForJson() {
	return constantForJson;
    }

    public void setConstantForJson(String constantForJson) {
	this.constantForJson = constantForJson;
    }

    public AutoEcole getAutoEcoleConnected(HttpServletRequest request) {
	AutoEcole autoEcoleConnected = null;
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	if (userConnected.getRestriction() instanceof AutoEcole) {
	    autoEcoleConnected = (AutoEcole) userConnected.getRestriction();
	}
	return autoEcoleConnected;
    }

    public Departement getDepartementConnected(HttpServletRequest request) {
	Departement departementConnected = null;
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	departementConnected = userConnected.getDepartement();

	return departementConnected;
    }

    public String getMailUserConnected(HttpServletRequest request) {
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	return userConnected.getEmail();
    }

    public String getProfilUserConnected(HttpServletRequest request) {
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	return userConnected.getLibelleProfil();
    }

    /**
     * Calcule les débuts et fin de page de la liste de lignes.
     * <p>
     * La valeur de retour est une liste de deux éléments : la liste contenant les indices des débuts de page et la liste contenant les indices des fins de pages
     * </p>
     * 
     * @param lignes
     * @param nbParPage
     * @return
     */
    public List<List<Integer>> getPagination(GroupeLignePlanning lignes, int nbPage) {
	List<Integer> start = new ArrayList<>();
	List<Integer> end = new ArrayList<>();

	int debut = 0;

	if (!lignes.getListLignesAjustement().isEmpty()) {
	    String el = getElementReferencePagination(lignes.getListLignesAjustement().get(0));
	    int nbEl = 0;

	    for (int i = 0; i < lignes.getListLignesAjustement().size(); i++) {
		if (nbEl >= nbPage) {
		    start.add(debut);
		    end.add(i - 1);
		    debut = i - 1;
		    nbEl = 0;
		}
		if (!el.equals(getElementReferencePagination(lignes.getListLignesAjustement().get(i)))) {
		    nbEl++;
		    el = getElementReferencePagination(lignes.getListLignesAjustement().get(i));
		}
	    }

	    start.add(debut);
	    end.add(lignes.getListLignesAjustement().size());
	}

	List<List<Integer>> list = new ArrayList<>();
	list.add(start);
	list.add(end);
	return list;
    }

    /**
     * Récupère le nombre de groupe de second niveau à afficher par page
     * 
     * @return
     */
    public abstract int getNbrParPage();

    /**
     * Récupère la valeur de l'élément comparé pour décider de la pagination (par exemple, le code de l'auto-école ou le code du groupe de permis)
     * 
     * @param ligne
     * @return
     */
    public abstract String getElementReferencePagination(AbstractViewLigneMultiGroup ligne);

    /**
     * Gère l'affichage initial de panneaux
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) throws Exception {
	LOGGER.debug("Avant Business");
	Map<String, Object> modele = new HashMap<String, Object>();
	ModelAndView mv = new ModelAndView(planningViewName, Constants.MODELE, modele);
	// FIXME : Le controlleur ne doit pas dépendre d'un format tel JSON
	JSONObject jsonObj = new JSONObject();

	Calendar cal = Calendar.getInstance();
	List<ViewAnneeMois> listAnneeMois = new ArrayList<>();
	List<ViewPermis> listPermis = new ArrayList<>();
	List<ViewAE> listAue = new ArrayList<>();
	List<ViewCentreExamen> listCentre = new ArrayList<>();

	String moisSelection = request.getParameter("moisSelection");
	List<List<IViewCritere>> listCriteres = null;
	listCriteres = groupedPlanning.getListCriteres(moisSelection, getDepartementConnected(request), getProfilUserConnected(request));

	ViewAnneeMois viewAnneeMoisSelection = null;
	boolean isEditable = false;
	boolean history = false;
	ViewCentreExamen viewCentreExamenSelection = null;
	ViewPermis viewPermisSelection = null;
	String idForMultiGrouping = null;
	// String libelleForMultiGrouping = null;
	if (listCriteres != null) {
	    for (List<? extends IViewCritere> criteres : listCriteres) {
		if (!criteres.isEmpty()) {
		    if (criteres.get(0) instanceof ViewAnneeMois) {
			listAnneeMois.addAll((Collection<ViewAnneeMois>) criteres);
		    }
		    if (criteres.get(0) instanceof ViewPermis) {
			listPermis.addAll((Collection<ViewPermis>) criteres);
		    }
		    if (criteres.get(0) instanceof ViewAE) {
			listAue.addAll((Collection<ViewAE>) criteres);
		    }
		    if (criteres.get(0) instanceof ViewCentreExamen) {
			listCentre.addAll((Collection<ViewCentreExamen>) criteres);
		    }
		}
	    }

	    if (!listAnneeMois.isEmpty()) {
		viewAnneeMoisSelection = listAnneeMois.get(0);
		if (moisSelection != null) {
		    for (ViewAnneeMois item : listAnneeMois) {
			if (item.getId().equals(moisSelection)) {
			    viewAnneeMoisSelection = item;
			}
		    }
		}
		request.setAttribute("moisSelection", viewAnneeMoisSelection.getId());
		cal.setTime(formatId.parse(viewAnneeMoisSelection.getId()));
		isEditable = groupedPlanning.isEditable(viewAnneeMoisSelection, getDepartementConnected(request), getProfilUserConnected(request));
		if (!isEditable && EnumProfils.LECTEUR.getLibelle().equals(getProfilUserConnected(request))) {
		    history = !groupedPlanning.isEditable(viewAnneeMoisSelection, getDepartementConnected(request), EnumProfils.REPARTITEUR.getLibelle());
		} else {
		    history = !isEditable;
		}
	    }

	    if (this.isGroupByGroupePermis()) {
		String permisSelection = request.getParameter("permisSelection");
		if (!listPermis.isEmpty()) {
		    viewPermisSelection = listPermis.get(0);
		    if (permisSelection != null) {
			for (ViewPermis item : listPermis)
			    if (item.getId().equals(permisSelection))
				viewPermisSelection = item;
		    }
		    idForMultiGrouping = viewPermisSelection.getId();
		    request.setAttribute("permis", viewPermisSelection.getLibelle());
		}
	    }

	    if (this.isGroupByCentreExamen()) {
		String centreSelection = request.getParameter("centreSelection");
		if (!listCentre.isEmpty()) {
		    viewCentreExamenSelection = listCentre.get(0);
		    if (centreSelection != null) {
			for (ViewCentreExamen item : listCentre)
			    if (item.getId().equals(centreSelection))
				viewCentreExamenSelection = item;
		    }
		    idForMultiGrouping = viewCentreExamenSelection.getId();
		}
	    }
	    if (this.isNotGrouped()) {
		String permisSelection = request.getParameter("permisSelection");
		if (permisSelection != null) {
		    for (ViewPermis item : listPermis)
			if (item.getId().equals(permisSelection))
			    viewPermisSelection = item;

		}
		if (viewPermisSelection != null) {
		    idForMultiGrouping = viewPermisSelection.getId();
		    request.setAttribute("permis", viewPermisSelection.getLibelle());
		} else {
		    idForMultiGrouping = "notGrouped";
		}

	    }
	}
	boolean tableauVide = true;
	GroupeLignePlanning groupLignePlanning = null;
	List<List<Integer>> listPages = null;
	int nbParPage = 0;
	if (idForMultiGrouping != null && viewAnneeMoisSelection != null) {
	    groupLignePlanning = groupedPlanning.getPlanningByGroup(idForMultiGrouping, getDepartementConnected(request), viewAnneeMoisSelection.getId(), getProfilUserConnected(request), history);
	    if (this.isGroupByGroupePermis()) {
		Collections.sort(groupLignePlanning.getListLignesAjustement(), new CompareMultiGroupLineByAe());
	    }
	    if (this.isGroupByCentreExamen() && groupLignePlanning.getListLignesAjustement() != null) {
		Collections.sort(groupLignePlanning.getListLignesAjustement(), new CompareMultiGroupLineByPermis());
	    }
	    if (this.isNotGrouped() && groupLignePlanning.getListLignesAjustement() != null) {
		Collections.sort(groupLignePlanning.getListLignesAjustement(), new CompareMultiGroupLineByPermisAndCentre());
	    }

	    nbParPage = getNbrParPage();

	    listPages = getPagination(groupLignePlanning, nbParPage);
	    tableauVide = groupLignePlanning.getListLignesAjustement().isEmpty();

	}

	request.setAttribute("tableauVide", tableauVide);
	request.setAttribute("mois", viewAnneeMoisSelection == null ? null : viewAnneeMoisSelection.getLibelle());
	request.setAttribute("history", history);

	LOGGER.debug("Apres Business");
	if (this.isGroupByGroupePermis()) {
	    jsonObj = ActionFactory.createAffichageGroupedPlanningByPermis(groupLignePlanning, isEditable);
	}
	if (this.isGroupByCentreExamen()) {
	    jsonObj = ActionFactory.createAffichageGroupedPlanningByCentre(groupLignePlanning, isEditable);
	}
	if (this.isNotGrouped()) {
	    jsonObj = ActionFactory.createAffichageGroupedPlanningByNothing(groupLignePlanning, isEditable);
	}
	jsonObj = ActionFactory.addListMois(jsonObj, listAnneeMois, viewAnneeMoisSelection);
	jsonObj = ActionFactory.addListPermis(jsonObj, listPermis, viewPermisSelection);
	jsonObj = ActionFactory.addListCentreExamen(jsonObj, listCentre, viewCentreExamenSelection);
	jsonObj = ActionFactory.addHeaders(jsonObj, cal);
	jsonObj = ActionFactory.addListAutoEcolesByMois(jsonObj, listAue);
	if (listPages != null && listPages.size() == 2)
	    jsonObj = ActionFactory.addPages(jsonObj, listPages, nbParPage);

	if (this.isGroupByGroupePermis()) {
	    String centreSelection = request.getParameter("centreSelection");
	    centreSelection = centreSelection == null ? "(Tous)" : centreSelection;
	    jsonObj = ActionFactory.addCentreEtPermisSelection(jsonObj, null, centreSelection);
	}

	// Mise à  jour du JSON dans le modèle
	modele.put(getConstantForJson(), jsonObj.toString());
	LOGGER.debug("Apres JSON");
	// modele.put(key, value)
	return mv;
    }

    // Attetion cette méthode n'est valable que pour le panneau ajustement reservation
    public abstract ModelAndView submitModification(HttpServletRequest request, HttpServletResponse response, AjustementRequestForm form) throws Exception;

    public String getPlanningViewName() {
	return planningViewName;
    }

    public void setPlanningViewName(String planningViewName) {
	this.planningViewName = planningViewName;
    }

    public WebBundle getMessages() {
	return messages;
    }

    public void setMessages(WebBundle messages) {
	this.messages = messages;
    }

}

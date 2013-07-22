package i2.application.extranet.utils.convert;

import i2.application.extranet.bean.view.AbstractViewLignePlanningSemaine;
import i2.application.extranet.bean.view.IViewLignePlanning;
import i2.application.extranet.bean.view.ViewAE;
import i2.application.extranet.bean.view.ViewAnneeMois;
import i2.application.extranet.bean.view.ViewCentreExamen;
import i2.application.extranet.bean.view.ViewDroitMensuel;
import i2.application.extranet.bean.view.ViewPermis;
import i2.application.extranet.bean.view.affectation.AffectationSeanceDetail;
import i2.application.extranet.bean.view.affectation.ViewRecapitulatifHebdomadaire;
import i2.application.extranet.bean.view.multigroup.AbstractViewLigneMultiGroup;
import i2.application.extranet.bean.view.multigroup.GroupeLignePlanning;
import i2.application.extranet.bean.view.multigroup.LigneAjustementMultiGroup;
import i2.application.extranet.bean.view.multigroup.LigneConsultationRetenueMultiGroup;
import i2.application.extranet.bean.view.option.ViewAEOptionClassement;
import i2.application.extranet.bean.view.option.ViewAffUnitesSuppCreneauHoraire;
import i2.application.extranet.bean.view.option.ViewRecapOptionsByPermis;
import i2.application.extranet.bean.view.planningjour.LignePlanningJourBean;
import i2.application.extranet.bean.view.planningjour.PlanningJour;
import i2.application.extranet.utils.Constants;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

import csb.common.tools.date.DateFormater;

/**
 * Implementation de la conversion des éléments de tableau de travail
 * 
 * @author BULL SAS
 * @version 1.0
 */

public class JsonConverterImpl implements JsonConverter {

    private final static Logger logger = Logger.getLogger(JsonConverterImpl.class);

    @Override
    public JSONObject getAsJson(String value) {
	return null;
    }

    @Override
    public JSONObject getAsJson(String valueOf, String msg) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public JSONArray getAsJsonArray(List<String> msg) {
	return JSONArray.fromObject(msg);
    }

    @Override
    public JSONObject getAsJson(Object response) {
	JSONObject result = new JSONObject();
	try {
	    if (response instanceof GroupeLignePlanning) {
		throw new NotImplementedException(GroupeLignePlanning.class);
	    } else {
		result = JSONObject.fromObject(response);
	    }
	} catch (JSONException e) {
	    logger.warn("json conversion error : response = " + response);
	}
	return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see i2.application.extranet.utils.convert.JsonConverter#getAsJson(java.util .List, java.util.Calendar, java.lang.Boolean)
     */
    @Override
    public JSONObject getAsJson(List<IViewLignePlanning> lstLignesPlanning, Calendar mois, boolean isEditable, boolean tableauVide) {
	JSONObject lignesJSONduPlanning = new JSONObject();
	JSONArray lignesUnitPermisJson = new JSONArray();
	List<String> lstHeaders = DateFormater.getSemaineHeader(mois);
	lignesJSONduPlanning.put("header", JSONArray.fromObject(lstHeaders));
	lignesJSONduPlanning.put("tableauVide", tableauVide);
	lignesJSONduPlanning.put("editable", isEditable);
	lignesJSONduPlanning.put("root", "data");

	if (lstLignesPlanning == null || lstLignesPlanning.isEmpty()) {
	    lignesJSONduPlanning.put("data", JSONArray.fromObject("[]"));
	} else {
	    lignesJSONduPlanning.put("data", JSONArray.fromObject(lstLignesPlanning));
	}

	if (lstLignesPlanning != null && !lstLignesPlanning.isEmpty()) {
	    if (lstLignesPlanning.get(0) instanceof LignePlanningJourBean) {
		lignesJSONduPlanning = this.getAffectationAsJson(lstLignesPlanning, mois);
	    } else {
		if (lstLignesPlanning.get(0) instanceof AbstractViewLignePlanningSemaine) {
		    Map<String, Integer> uniteByPermis = new HashMap<>();
		    for (IViewLignePlanning ligne : lstLignesPlanning) {
			AbstractViewLignePlanningSemaine ligneSemaine = (AbstractViewLignePlanningSemaine) ligne;
			int nbUnite = ligneSemaine.getSemaine1() + ligneSemaine.getSemaine2() + ligneSemaine.getSemaine3() + ligneSemaine.getSemaine4() + ligneSemaine.getSemaine5();
			if (uniteByPermis.get(ligneSemaine.getPermis()) == null) {
			    uniteByPermis.put(ligneSemaine.getPermis(), nbUnite);
			} else {
			    Integer nbUnitAnte = uniteByPermis.get(ligneSemaine.getPermis());
			    uniteByPermis.remove(ligneSemaine.getPermis());
			    uniteByPermis.put(ligneSemaine.getPermis(), nbUnite + nbUnitAnte);
			}
		    }
		    for (Map.Entry<String, Integer> entry : uniteByPermis.entrySet()) {
			JSONObject unitPermis = new JSONObject();
			unitPermis.put("id", entry.getKey());
			unitPermis.put("nbUnit", entry.getValue());
			lignesUnitPermisJson.add(unitPermis);
		    }
		}
	    }
	} else {
	    JSONObject ligne = new JSONObject();
	    // TODO : ça sert à quoi ça??
	    ligne.put("centre", "");
	    JSONArray jsonArray = new JSONArray();
	    jsonArray.add(ligne);
	    lignesJSONduPlanning.put("data", "[]");
	    lignesJSONduPlanning.put("root", "data");
	    lignesJSONduPlanning.put("header", "");

	}
	// lignesJSONduPlanning.put("uniteByPermis", lignesUnitPermisJson);
	return lignesJSONduPlanning;
    }

    // FIXME A voir si ce code ne peut être rendu générique dans la méthode
    // appelante (PFR)
    private JSONObject getAffectationAsJson(List<IViewLignePlanning> lstAffectation, final Calendar mois) {
	JSONObject lignesAffectatonsJson = new JSONObject();
	JSONArray lignesLstJson = new JSONArray();
	if (lstAffectation != null) {
	    for (IViewLignePlanning ligneView : lstAffectation) {
		LignePlanningJourBean ligne = (LignePlanningJourBean) ligneView;
		JSONObject ligneAffectation = new JSONObject();
		ligneAffectation.put("centre", ligne.getCentre());
		ligneAffectation.put("cexNumero", ligne.getCexNumero());
		ligneAffectation.put("salle", ligne.getSalle());
		ligneAffectation.put("salleId", ligne.getSalleId());
		ligneAffectation.put("mois", ligne.getMois());
		ligneAffectation.put("moisStr", new DateFormatSymbols().getMonths()[ligne.getMois() - 1]);
		ligneAffectation.put("annee", ligne.getAnnee());
		for (PlanningJour jour : ligne.getLstPlanningJour()) {
		    ligneAffectation.put(jour.getDateStr(), getLstJourAndHeader(jour));
		}
		lignesLstJson.add(ligneAffectation);
	    }
	    lignesAffectatonsJson.put("data", lignesLstJson);
	    addLstJourAndHeader(lignesAffectatonsJson, mois);
	    lignesAffectatonsJson.put("root", "data");
	}
	return lignesAffectatonsJson;
    }

    protected JSONObject getLstJourAndHeader(PlanningJour jour) {
	JSONObject o = new JSONObject();
	o.put("dateStr", jour.getDateStr());
	if (jour.getDate() != null) {
	    o.put("date", DateFormater.formatDate(jour.getDate()));
	} else {
	    o.put("date", "");
	}
	o.put("lstSeances", JSONArray.fromObject(jour.getLstSeances()));
	return o;
    }

    protected JSONObject addLstJourAndHeader(JSONObject json, Calendar mois) {

	Calendar dateDebut = (Calendar) mois.clone();
	dateDebut.set(Calendar.DAY_OF_MONTH, mois.getActualMinimum(Calendar.DAY_OF_MONTH));
	Calendar dateFin = (Calendar) mois.clone();
	dateFin.set(Calendar.DAY_OF_MONTH, mois.getActualMaximum(Calendar.DAY_OF_MONTH));
	List<String> lstHeader = new ArrayList<String>();
	List<String> lstJours = new ArrayList<String>();
	for (Calendar cal = (Calendar) dateDebut.clone(); cal.compareTo(dateFin) <= 0; cal.add(Calendar.DATE, 1)) {
	    lstHeader.add(DateFormater.formatJourDate(cal));
	    lstJours.add(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
	}
	json.put("header", JSONArray.fromObject(lstHeader));
	json.put("lstJours", JSONArray.fromObject(lstJours));
	return json;
    }

    @Override
    public JSONObject addListRecapOptions(JSONObject json, List<ViewRecapOptionsByPermis> lstRecapOptions) {
	json.put("listRecapOptions", lstRecapOptions);
	return json;
    }

    /*
     * (non-Javadoc)
     * 
     * @see i2.application.extranet.utils.convert.JsonConverter#addListMois(net.sf .json.JSONObject, java.util.List, i2.application.extranet.bean.view.ViewAnneeMois)
     */
    @Override
    public JSONObject addListMois(JSONObject json, List<ViewAnneeMois> listAnneeMois, ViewAnneeMois anneeMoisSelection) {
	if (listAnneeMois != null && !listAnneeMois.isEmpty()) {
	    json.put("listMois", JSONArray.fromObject(listAnneeMois));
	}
	if (anneeMoisSelection != null) {
	    json.put(Constants.MOIS_SELECTION, anneeMoisSelection);
	}
	return json;
    }

    /*
     * (non-Javadoc)
     * 
     * @see i2.application.extranet.utils.convert.JsonConverter#addEditable(net.sf.json.JSONObject, boolean)
     */
    @Override
    public JSONObject addEditable(JSONObject json, boolean isEditable) {
	json.put("isEditable", isEditable);
	return json;
    }

    /*
     * (non-Javadoc)
     * 
     * @see i2.application.extranet.utils.convert.JsonConverter#addListCentreEtPermis (net.sf.json.JSONObject, java.util.List, java.util.List)
     */
    @Override
    public JSONObject addCentreEtPermisSelection(JSONObject json, String permisSelection, String centreSelection) {
	if (permisSelection != null) {
	    json.put("permisSelection", permisSelection);
	}
	if (centreSelection != null) {
	    json.put("centreSelection", centreSelection);
	}
	return json;
    }

    /*
     * (non-Javadoc)
     * 
     * @see i2.application.extranet.utils.convert.JsonConverter#addListDroitsMensuels (net.sf.json.JSONObject, java.util.List)
     */
    @Override
    public JSONObject addListDroitsMensuels(JSONObject json, List<ViewDroitMensuel> listDroitsMensuels) {
	JSONObject treeDroitsMensuelsJson = new JSONObject();

	if (listDroitsMensuels != null && !listDroitsMensuels.isEmpty()) {
	    JSONArray arrayNodesPermisJson = new JSONArray();

	    for (ViewDroitMensuel droitMensuel : listDroitsMensuels) {
		JSONObject nodesPermisJson = new JSONObject();
		nodesPermisJson.put("id", droitMensuel.getPermisId());

		nodesPermisJson.put("valueDroit", droitMensuel.getDroitsAjuste());
		nodesPermisJson.put("text", "");
		nodesPermisJson.put("leaf", false);
		nodesPermisJson.put("iconCls", "permis" + droitMensuel.getPermisId());
		nodesPermisJson.put("cls", "forum-ct");
		nodesPermisJson.put("expanded", true);

		JSONArray arrayNodesPopCoefJson = new JSONArray();
		JSONObject nodesPopCoefJson = new JSONObject();
		nodesPopCoefJson.put("text", "Unités de ref: " + droitMensuel.getPopulationRef());
		nodesPopCoefJson.put("leaf", true);
		nodesPopCoefJson.put("cls", "file");
		arrayNodesPopCoefJson.add(nodesPopCoefJson);
		nodesPopCoefJson = new JSONObject();

		Double coefMensuel = droitMensuel.getCoefficientMensuel();
		DecimalFormatSymbols decimalFormat = new DecimalFormatSymbols();
		decimalFormat.setDecimalSeparator('.');
		NumberFormat formatter = new DecimalFormat("#0.00", decimalFormat);

		nodesPopCoefJson.put("text", "Coeff mensuel: " + formatter.format(coefMensuel));
		nodesPopCoefJson.put("leaf", true);
		nodesPopCoefJson.put("cls", "file");
		arrayNodesPopCoefJson.add(nodesPopCoefJson);

		// FIXME: Les espaces ajoutées à revoir => je l'ai ajouté pour avoir la même ergonomie que dans la spéc.
		// => il y a une possibilité à le faire sans les espaces, il s'agit de redimensionner la largeur du panneau gauche, mais
		// ce dernier est commun entre les différents !!
		nodesPopCoefJson.put("text",
			"NH unités attribuées et <br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;non affectées: " + droitMensuel.getUniteNhAffecte());
		nodesPopCoefJson.put("leaf", true);
		nodesPopCoefJson.put("cls", "file");
		arrayNodesPopCoefJson.add(nodesPopCoefJson);

		nodesPopCoefJson.put("text", "NH au  cours des examens: " + droitMensuel.getUniteNhEcExamen());
		nodesPopCoefJson.put("leaf", true);
		nodesPopCoefJson.put("cls", "file");
		arrayNodesPopCoefJson.add(nodesPopCoefJson);
		nodesPermisJson.put("children", arrayNodesPopCoefJson);

		arrayNodesPermisJson.add(nodesPermisJson);
	    }
	    treeDroitsMensuelsJson.put("data", arrayNodesPermisJson);
	}
	json.put("listDroitsMensuels", treeDroitsMensuelsJson);
	return json;
    }

    /**
     * @see JsonConverter#getAsJson(AffectationSeanceDetail)
     */
    @Override
    public JSONObject getAsJson(AffectationSeanceDetail affectationSeanceDetail) {
	return JSONObject.fromObject(affectationSeanceDetail);
    }

    /**
     * @see JsonConverter#addRecapitulatifAffectations(JSONObject, List)
     */
    @Override
    public JSONObject addRecapitulatifAffectations(JSONObject json, List<ViewRecapitulatifHebdomadaire> listRecapitulatifHebdomadaire) {
	json.put("listRecapitulatif", JSONArray.fromObject(listRecapitulatifHebdomadaire));
	return json;
    }

    @Override
    public JSONArray getAsJsonClassement(List<ViewAEOptionClassement> lstAue) {
	return JSONArray.fromObject(lstAue);
    }

    @Override
    public JSONArray getAsJsonCreneauHoraire(List<ViewAffUnitesSuppCreneauHoraire> lstCreneau) {
	return JSONArray.fromObject(lstCreneau);
    }

    @Override
    public JSONObject addListAutoEcoles(JSONObject json, List<ViewAE> lstAue) {
	if (lstAue == null) {
	    json.put("lstAutoEcolesByMois", JSONArray.fromObject("[]"));
	} else {
	    json.put("lstAutoEcolesByMois", JSONArray.fromObject(lstAue));
	}
	return json;
    }

    @Override
    public JSONObject addUserSelected(JSONObject json, String codeRafaelUserSelected, String aueCodeUserSelected) {
	if (codeRafaelUserSelected != null) {
	    json.put("codeRafaelUserSelected", codeRafaelUserSelected);
	}
	if (codeRafaelUserSelected != null) {
	    json.put("aueCodeUserSelected", aueCodeUserSelected);
	}
	return json;
    }

    @Override
    public JSONObject getAsJsonGroupeLigneAjustement(GroupeLignePlanning groupeLigne, String groupProperty, boolean isEditable) {

	JSONObject planning = new JSONObject();
	JSONArray jsonArray = new JSONArray();
	planning.put("root", "data");
	planning.put("editable", isEditable);

	if (groupeLigne != null)
	    for (AbstractViewLigneMultiGroup viewLigne : groupeLigne.getListLignesAjustement()) {
		JSONObject jsonLigne = getAsJsonLigneAjustement(viewLigne, groupeLigne.getLibelleGroupe(), groupProperty);
		jsonArray.add(jsonLigne);
	    }
	planning.put("data", jsonArray);
	return planning;
    }

    @Override
    public JSONObject getAsJsonLigneAjustement(AbstractViewLigneMultiGroup ligne, String libelleCentreGroupePermis, String groupProperty) {
	JSONObject json = JSONObject.fromObject(ligne);
	json = (JSONObject) JSONSerializer.toJSON(ligne);
	json.put(groupProperty, libelleCentreGroupePermis);
	if (ligne instanceof LigneAjustementMultiGroup && ((LigneAjustementMultiGroup) ligne).getRetenues() == null)
	    json.put("retenues", "");
	if (ligne instanceof LigneAjustementMultiGroup && ((LigneAjustementMultiGroup) ligne).getReservationPlafond() == null)
	    json.put("reservationPlafond", "");
	if (ligne instanceof LigneAjustementMultiGroup && ((LigneAjustementMultiGroup) ligne).getTotalReservationPlafond() == null)
	    json.put("totalReservationPlafond", "");
	if (ligne instanceof LigneConsultationRetenueMultiGroup && ((LigneConsultationRetenueMultiGroup) ligne).getUniteRetenueSemaine1() == null)
	    json.put("uniteRetenueSemaine1", "");
	if (ligne instanceof LigneConsultationRetenueMultiGroup && ((LigneConsultationRetenueMultiGroup) ligne).getUniteRetenueSemaine2() == null)
	    json.put("uniteRetenueSemaine2", "");
	if (ligne instanceof LigneConsultationRetenueMultiGroup && ((LigneConsultationRetenueMultiGroup) ligne).getUniteRetenueSemaine3() == null)
	    json.put("uniteRetenueSemaine3", "");
	if (ligne instanceof LigneConsultationRetenueMultiGroup && ((LigneConsultationRetenueMultiGroup) ligne).getUniteRetenueSemaine4() == null)
	    json.put("uniteRetenueSemaine4", "");
	if (ligne instanceof LigneConsultationRetenueMultiGroup && ((LigneConsultationRetenueMultiGroup) ligne).getUniteRetenueSemaine5() == null)
	    json.put("uniteRetenueSemaine5", "");
	if (ligne instanceof LigneConsultationRetenueMultiGroup && ((LigneConsultationRetenueMultiGroup) ligne).getRetenuesMensuelle() == null)
	    json.put("retenuesMensuelle", "");
	return json;
    }

    @Override
    public JSONObject addListCentreExamen(JSONObject json, List<ViewCentreExamen> listCentre, ViewCentreExamen viewCentreSelection) {
	if (listCentre == null) {
	    json.put("lstCentreExamen", JSONArray.fromObject("[]"));
	} else {
	    json.put("lstCentreExamen", JSONArray.fromObject(listCentre));
	    if (viewCentreSelection != null) {
		json.put("centreSelection", JSONObject.fromObject(viewCentreSelection));
	    }
	}
	return json;
    }

    @Override
    public JSONObject addListPermis(JSONObject json, List<ViewPermis> listPermis, ViewPermis viewPermisSelection) {
	if (listPermis != null && !listPermis.isEmpty()) {
	    json.put("listPermis", JSONArray.fromObject(listPermis));
	}
	if (viewPermisSelection != null) {
	    json.put("permisSelection", viewPermisSelection.getLibelle());
	}
	return json;
    }

    @Override
    public JSONObject addHeaders(JSONObject json, List<String> lstHeader) {
	if (lstHeader.get(0) == null) {
	    lstHeader.remove(0);
	}
	json.put("header", JSONArray.fromObject(lstHeader));

	return json;
    }

    @Override
    public JSONObject addPages(JSONObject json, List<List<Integer>> listPages, int nbParPage) {
	json.put("start", JSONArray.fromObject(listPages.get(0)));
	json.put("end", JSONArray.fromObject(listPages.get(1)));
	json.put("nbElParPage", String.valueOf(nbParPage));
	return json;
    }
}

package i2.application.extranet.utils;

import i2.application.extranet.bean.view.IViewLignePlanning;
import i2.application.extranet.bean.view.ViewAE;
import i2.application.extranet.bean.view.ViewAnneeMois;
import i2.application.extranet.bean.view.ViewCentreExamen;
import i2.application.extranet.bean.view.ViewDroitMensuel;
import i2.application.extranet.bean.view.ViewPermis;
import i2.application.extranet.bean.view.affectation.AffectationSeanceDetail;
import i2.application.extranet.bean.view.affectation.ViewRecapitulatifHebdomadaire;
import i2.application.extranet.bean.view.multigroup.GroupeLignePlanning;
import i2.application.extranet.bean.view.option.ViewAEOptionClassement;
import i2.application.extranet.bean.view.option.ViewAffUnitesSuppCreneauHoraire;
import i2.application.extranet.bean.view.option.ViewRecapOptionsByPermis;
import i2.application.extranet.utils.convert.JsonConverter;
import i2.application.extranet.utils.convert.JsonConverterImpl;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import csb.common.tools.date.DateFormater;

/**
 * 
 * @author BULL SAS
 * 
 * 
 *         ActionFactory permet d'instencier les objets dont le type exacte n'est pas connu par l'appelant. Elle represente la farbique de la couche presentation
 */

public final class ActionFactory {

    private static JsonConverter converter = createJsonConverter();

    private ActionFactory() {
    }

    /**
     * createResponse
     * 
     * @param servletResponse
     * @return AsyncResponse
     */
    public static AsyncResponse createResponse(HttpServletResponse servletResponse) {
	return new AsyncJsonResponseWrapper(servletResponse, new JsonConverterImpl());
    }

    /**
     * createJsonConverter
     * 
     * @return JsonConverter
     */
    public static JsonConverter createJsonConverter() {
	return new JsonConverterImpl();
    }

    /**
     * Créé l'affichage du Planning en retournant un objet JSON
     * 
     * @param lstLignePlanning
     *            ligne du planning
     * @param mois
     * @param tableauVide2
     * @param listAnneeMois
     * @param anneeMoisSelectionne
     * @param listCentres
     * @param listPermis
     * @param listDroitsMensuels
     * @param affichageSemaine
     *            détermine si la planning est affiché au niveau de la semaine ou du jour
     * @return JSONObject
     */
    public static JSONObject createAffichageDuPlanning(List<IViewLignePlanning> lstLignePlanning, Calendar mois, boolean isEditable, boolean tableauVide) {
	return converter.getAsJson(lstLignePlanning, mois, isEditable, tableauVide);
    }

    public static JSONObject addListMois(final JSONObject json, List<ViewAnneeMois> listAnneeMois, ViewAnneeMois anneeMoisSelection) {
	return converter.addListMois(json, listAnneeMois, anneeMoisSelection);
    }

    public static JSONObject addCentreEtPermisSelection(final JSONObject json, String permisSelection, String centreSelection) {
	return converter.addCentreEtPermisSelection(json, permisSelection, centreSelection);
    }

    public static JSONObject addListDroitsMensuels(final JSONObject json, List<ViewDroitMensuel> listDroitMensuel) {
	return converter.addListDroitsMensuels(json, listDroitMensuel);
    }

    public static JSONObject createAffichageAcitivtePlanningDetail(final AffectationSeanceDetail affectationsSeanceDetail) {
	return converter.getAsJson(affectationsSeanceDetail);
    }

    public static JSONArray createClassement(final List<ViewAEOptionClassement> lstAue) {
	return converter.getAsJsonClassement(lstAue);
    }

    public static JSONArray createCreneauUniteSupp(List<ViewAffUnitesSuppCreneauHoraire> lstCreneau) {
	return converter.getAsJsonCreneauHoraire(lstCreneau);
    }

    public static JSONObject addRecapitulatifAffectations(final JSONObject json, List<ViewRecapitulatifHebdomadaire> listRecapitulatifHebdomadaire) {
	return converter.addRecapitulatifAffectations(json, listRecapitulatifHebdomadaire);
    }

    public static JSONObject addRecapOptionsByPermis(final JSONObject json, List<ViewRecapOptionsByPermis> lstRecap) {
	return converter.addListRecapOptions(json, lstRecap);
    }

    public static JSONObject addListAutoEcolesByMois(JSONObject json, List<ViewAE> lstAue) {
	return converter.addListAutoEcoles(json, lstAue);
    }

    public static JSONObject addUserSelected(final JSONObject json, String codeRafaelUserSelected, String aueCodeUserSelected) {
	return converter.addUserSelected(json, codeRafaelUserSelected, aueCodeUserSelected);
    }

    public static JSONObject addHeaders(final JSONObject json, Calendar cal) {
	List<String> lstHeaders = DateFormater.getSemaineHeader(cal);
	return converter.addHeaders(json, lstHeaders);
    }

    public static JSONObject createAffichageGroupedPlanningByPermis(GroupeLignePlanning groupeLigne, boolean isEditable) {
	return converter.getAsJsonGroupeLigneAjustement(groupeLigne, Constants.MULTI_GROUP_BY_PERMIS, isEditable);
    }

    public static JSONObject createAffichageGroupedPlanningByCentre(GroupeLignePlanning groupeLigne, boolean isEditable) {
	return converter.getAsJsonGroupeLigneAjustement(groupeLigne, Constants.MULTI_GROUP_BY_CENTRE, isEditable);
    }

    public static JSONObject createAffichageGroupedPlanningByNothing(GroupeLignePlanning groupeLigne, boolean isEditable) {
	return converter.getAsJsonGroupeLigneAjustement(groupeLigne, Constants.MULTI_NOT_GROUP, isEditable);
    }

    // @Deprecated
    // public static JSONObject createReservationList(
    // List<LigneReservationBean> lstReservation, Calendar date) {
    // JsonConverter converter = createJsonConverter();
    // JSONObject jsonLstReservation = converter.getAsJson(lstReservation,
    // date);
    // return jsonLstReservation;
    // }
    // @Deprecated
    // public static JSONObject createAffectationList(
    // List<LigneAffectationBean> lstAffectations) {
    // JsonConverter converter = createJsonConverter();
    // JSONObject jsonLstReservation =
    // converter.getAffectationAsJson(lstAffectations);
    // return jsonLstReservation;
    // }
    //
    // @Deprecated
    // public static JSONObject createAttributionList(List<LigneAttributionBean>
    // lstAttributions,
    // List<ViewAnneeMois> listAnneeMois,
    // List<ViewCentreExamen> listCentres,
    // List<ViewPermis> listPermis,
    // ViewAnneeMois anneeMoisSelectionne) {
    // JsonConverter converter = createJsonConverter();
    // JSONObject jsonLstAttribution =
    // converter.getAttributionAsJson(lstAttributions);
    // jsonLstAttribution = converter.addListCentreEtPermis(jsonLstAttribution,
    // listCentres, listPermis);
    // jsonLstAttribution = converter.addListMois(jsonLstAttribution,
    // listAnneeMois,anneeMoisSelectionne);
    // return jsonLstAttribution;
    // }

    public static JSONObject addListCentreExamen(JSONObject jsonObj, List<ViewCentreExamen> listCentre, ViewCentreExamen viewCentreExamenSelection) {
	return converter.addListCentreExamen(jsonObj, listCentre, viewCentreExamenSelection);
    }

    public static JSONObject addListPermis(JSONObject json, List<ViewPermis> listPermis, ViewPermis viewPermisSelection) {
	return converter.addListPermis(json, listPermis, viewPermisSelection);
    }

    public static JSONObject addPages(JSONObject json, List<List<Integer>> listPages, int nbParPage) {
	return converter.addPages(json, listPages, nbParPage);
    }

}
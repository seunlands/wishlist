package i2.application.extranet.utils.convert;

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
import i2.application.extranet.bean.view.option.ViewAEOptionClassement;
import i2.application.extranet.bean.view.option.ViewAffUnitesSuppCreneauHoraire;
import i2.application.extranet.bean.view.option.ViewRecapOptionsByPermis;

import java.util.Calendar;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Interface décrivant les méthodes de convertion JSON
 * 
 * @author Bull
 * 
 */
public interface JsonConverter {

    JSONObject getAsJson(String value);

    JSONObject getAsJson(String valueOf, String msg);

    JSONArray getAsJsonArray(List<String> msg);

    JSONObject getAsJson(Object response);

    /**
     * Retourne le planning en tableau JSON
     * 
     * @param lstLignePlanning
     *            liste des lignes du planning
     * @param mois
     *            sur lequel on affiche le planning
     * @param tableauVide
     * @return un JSONObject
     */
    JSONObject getAsJson(List<IViewLignePlanning> lstLignesPlanning, Calendar mois, boolean isEditable, boolean tableauVide);

    /**
     * Ajoute à l'obejt JSON la liste d'année mois
     * 
     * @param json
     *            le JSONObject utilisé
     * @param listAnneeMois
     * @param anneeMoisSelection
     * @return le JSONObject modifié
     */
    JSONObject addListMois(JSONObject json, List<ViewAnneeMois> listAnneeMois, ViewAnneeMois anneeMoisSelection);

    /**
     * Ajoute à l'objet JSON la liste de centre examen et de groupe de permis
     * 
     * @param json
     *            le JSONObject utilisé
     * @param listCentre
     * @param listPermis
     * @return le JSONObject modifié
     */
    JSONObject addCentreEtPermisSelection(JSONObject json, String permisSelection, String centreSelection);

    /**
     * Ajoute à l'objet JSON la liste des droits
     * 
     * @param json
     *            le JSONObject utilisé
     * @param listDroitMensuel
     * @return le JSONObject modifié
     */
    JSONObject addListDroitsMensuels(JSONObject json, List<ViewDroitMensuel> listDroitMensuel);

    // @Deprecated
    // public JSONObject getAsJson(List<LigneReservationBean>
    // lignesReservations);
    //
    // @Deprecated
    // public JSONObject getAffectationAsJson(
    // List<LigneAffectationBean> lstAffectation);
    //
    // @Deprecated
    // JSONObject getAttributionAsJson(List<LigneAttributionBean>
    // lignesAttributionBean);
    //
    //

    /**
     * Retourne une séance en tableau JSON
     * 
     * @param lstLignePlanning
     *            détail d'une séance de planning
     * @return un JSONArray
     */
    JSONObject getAsJson(final AffectationSeanceDetail affectationsSeanceDetail);

    /**
     * Retourne le classment des auto ecoles du departement
     * 
     * @param lstAue
     * @return
     */
    JSONArray getAsJsonClassement(final List<ViewAEOptionClassement> lstAue);

    /**
     * Ajoute le récapitulatif des affectations.
     * 
     * @param json
     * @param listRecapitulatifHebdomadaire
     * @return
     */
    JSONObject addRecapitulatifAffectations(final JSONObject json, List<ViewRecapitulatifHebdomadaire> listRecapitulatifHebdomadaire);

    /**
     * Ajoute le recapitulatif de l'ecran des options
     * 
     * @param json
     * @param lstRecapOptions
     * @return
     */
    JSONObject addListRecapOptions(JSONObject json, List<ViewRecapOptionsByPermis> lstRecapOptions);

    /**
     * retourne la seance en tableau JSON
     * 
     * @param lstCreneau
     * @return
     */
    JSONArray getAsJsonCreneauHoraire(List<ViewAffUnitesSuppCreneauHoraire> lstCreneau);

    JSONObject getAsJsonGroupeLigneAjustement(GroupeLignePlanning groupeLigne, String groupProperty, boolean isEditable);

    JSONObject getAsJsonLigneAjustement(AbstractViewLigneMultiGroup ligne, String libelleCentreGroupePermis, String groupProperty);

    /**
     * Ajout à l'objet JSON la liste des écoles de conduite à afficher dans la panneau droit pour Reservation & Attribution & Affectation
     * 
     * @param json
     * @param lstAue
     * @return
     */
    JSONObject addListAutoEcoles(JSONObject json, List<ViewAE> lstAue);

    /**
     * Retourne l'école de conduite selectionne par le répartiteur
     * 
     * @param json
     * @param codeRafaelUserSelected
     * @return
     */
    JSONObject addUserSelected(JSONObject json, String codeRafaelUserSelected, String aueCodeUserSelected);

    JSONObject addListCentreExamen(JSONObject jsonObj, List<ViewCentreExamen> listCentre, ViewCentreExamen viewCentreSelection);

    JSONObject addListPermis(JSONObject json, List<ViewPermis> listPermis, ViewPermis viewPermisSelection);

    JSONObject addHeaders(JSONObject json, List<String> lstHeader);

    JSONObject addPages(JSONObject json, List<List<Integer>> listPages, int nbParPage);

    /**
     * Retourne le JSON avec le flag editable
     * 
     * @param json
     * @param isEditable
     * @return
     */
    JSONObject addEditable(JSONObject json, boolean isEditable);
}

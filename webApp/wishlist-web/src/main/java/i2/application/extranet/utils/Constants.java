package i2.application.extranet.utils;

/**
 * 
 * @author BULL SAS
 * 
 *         Class utilitaire contenant tout les constant de la couche presentation
 * 
 */
public final class Constants {

    private Constants() {
    }

    /*
     * -------------Constants Reservation-----------------
     */
    public static final String PLANNING_RESERVATION = "planningReservation";
    public static final String TABLEAU_RESERVATION = "reservation";
    public static final String TABLEAU_RESERVATION_TXT = "reservationTxt";

    /* * -------------Constants Attribution----------------- */
    public static final String PLANNING_ATTRIBUTION = "planningAttribution";
    public static final String TABLEAU_ATTRIBUTION = "attribution";
    public static final String TABLEAU_ATTRIBUTION_TXT = "attributionTxt";
    public static final String CONSULT_ATTRIBUTION = "consultation";
    public static final String CONSULT_ATTRIBUTION_TXT = "consultationTxt";
    public static final String CONSULT_UNITES_PROGRAMMEES = "unitesProgrammees";
    public static final String PLANNING_UNITES_PROGRAMMEES = "planningUnitesProgrammees";
    public static final String CONSULT_ATTRIB_UNITES_CALCULEES = "consulterAttribution";
    public static final String PLANNING_ATTRIB_UNITES_CALCULEES = "planningAttributionCalculee";

    /* * -------------Constants Rattachement----------------- */
    public static final String RATTACHEMENT = "rattachement";
    public static final String RATTACHEMENT_URL = "/gpe/rattachement/demanderRattachement.do";
    public static final String RECHERCHE_CENTRE = "rechercheCentre";
    public static final String QUALIFICATIONS_LIST = "qualificationsList";
    public static final String SELECTION_AE = "selectionAutoEcole";
    public static final String LIST_AE = "listAutoEcole";
    public static final String SES_CHOIX_AE_RATTACH = "autoEcoleChoisi";

    /* * -------------Constants Validation demande ----------------- */
    public static final String VALIDATION = "validation";
    public static final String DEMANDES_ATTENTES = "demandesAttentesValidation";
    /* * -------------Constants Affectations----------------- */
    public static final String TABLEAU_AFFECTATION = "affectation";
    public static final String TABLEAU_AFFECTATION_TXT = "affectationTxt";
    public static final String PLANNINGS_AFFECTATION = "planningAffectation";

    /* * -------------Constants Restitutions----------------- */
    public static final String TABLEAU_RESTITUTION = "restitution";
    public static final String TABLEAU_RESTITUTION_TXT = "restitutionTxt";
    public static final String PLANNINGS_RESTITUTION = "planningRestitution";

    /* * ------------Constantes Administration---------------- */
    public static final String GESTION_PARAMETRES = "administration";

    /* * --------------Constantes Gestion options */
    public static final String TABLEAU_OPTION = "option";
    public static final String TABLEAU_OPTION_TXT = "optionTxt";
    public static final String PLANNINGS_OPTION = "planningOption";
    public static final String PLANNINGS_VALIDER_OPTION = "planningValiderOption";
    public static final String TABLEAU_VALIDER_OPTION = "validerOption";
    public static final String TABLEAU_VALIDER_OPTION_TXT = "validerOptionTxt";

    /* * --------------Constantes Ajustement */
    public static final String TABLEAU_AJUSTEMENT_RESERVATION = "ajustementReservation";
    public static final String PLANNING_AJUSTEMENT_RESERVATION = "planningAjustementReservation";
    public static final String TABLEAU_CONSULT_RETENUE = "unitesRetenues";
    public static final String PLANNING_CONSULT_RETENUE = "planningConsultationRetenue";

    /* * Pour les droits et la session */
    public static final String SES_DROITS = "droits";
    public static final String SES_UTILISATEUR = "utilisateur";
    public static final String SES_THEME_TEXTE = "themeTexte";
    public static final String SES_PATH_TEXTE = "pathTexte";
    public static final String SES_DEPARTEMENT_ACTUEL = "departementActuel";
    public static final String SES_ERREUR = "erreur";
    public static final String SES_PLANNING = "planning";
    public static final String SES_PROFILS = "profils";
    public static final String SES_PROFIL_SEL = "profilSelected";

    /* ------------- Constants Generale ----------------- */

    public static final String MODELE = "modele";
    public static final String MONTH = "mois";
    public static final String YEAR = "annee";
    public static final String NB_ALERTES = "nbalertes";
    public static final String MOIS_SELECTION = "moisSelection";

    /* -------------- Constantes JSON pour les MultiGroup ------------------ */
    public static final String MULTI_GROUP_BY_PERMIS = "permis";
    public static final String MULTI_GROUP_BY_CENTRE = "centre";
    public static final String MULTI_NOT_GROUP = "notGrouped";

    /* Mode texte */
    public static final String PLANNING_TXT = "planningList";
    public static final String CRITERES_MODEL_TXT = "criteres";
    public static final String CRITERES_FORM_TXT = "selectionCriteres";
    public static final String RESULTATS_MODEL_TXT = "resultats";
    public static final String SEANCE_MODEL_TXT = "seance";
    public static final String MESSAGES_MODEL_TXT = "messages";
    public static final String COMMAND_TXT = "command";

    public static final String ACTION_GET_PARAM = "action";
    public static final String ACTION_POST_PARAM = "submit";
    public static final String ACTION_RECHERCHE_CENTRE = "Recherche Centre et Permis";
    public static final String ACTION_RECHERCHE_SALLE = "Recherche Salle";
    public static final String ACTION_RECHERCHE_DATE = "Recherche Date";
    public static final String ACTION_AFFICHE = "Afficher";
    public static final String ACTION_ENREGISTRE = "Enregistrer";
}

package i2.application.extranet.action.rattachement;

import i2.application.aurige.bean.AutoEcole;
import i2.application.aurige.bean.Departement;
import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.bean.view.rattachement.LigneCentreBean;
import i2.application.extranet.bean.view.rattachement.RechercheCentresBean;
import i2.application.extranet.business.exceptions.ApplicationException;
import i2.application.extranet.business.exceptions.ConcurrentDataAccessException;
import i2.application.extranet.business.rattachement.IRattachementsManager;
import i2.application.extranet.business.util.LibelleUtil;
import i2.application.extranet.enums.EnumProfils;
import i2.application.extranet.utils.Constants;
import i2.application.extranet.utils.WebBundle;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import csb.common.tools.network.EmailSender;

/**
 * Controller d'actions pour les tableaux de soumission des demandes de rattachement et de detachement
 * 
 * @author bull
 * 
 */
public class RattachementController extends MultiActionController {

    private WebBundle messages;

    private ResourceBundle emailBundle;

    public WebBundle getMessages() {
	return messages;
    }

    private IRattachementsManager rattachementsManager;

    private String viewName;

    public String getViewName() {
	return viewName;
    }

    public void setViewName(String viewName) {
	this.viewName = viewName;
    }

    public void setRattachementsManager(IRattachementsManager rattachementsManager) {
	this.rattachementsManager = rattachementsManager;
    }

    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) throws Exception {
	AutoEcole autoEcole = (AutoEcole) request.getSession().getAttribute(Constants.SES_CHOIX_AE_RATTACH);
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	Map<String, Object> map = new HashMap<String, Object>();
	if (userConnected.getRestriction() instanceof AutoEcole) {
	    if (autoEcole == null)
		autoEcole = (AutoEcole) userConnected.getRestriction();
	    List<LigneCentreBean> lstCentreRattaches = rattachementsManager.rechercheCentreRattaches(autoEcole);
	    RechercheCentresBean rechercheCentresBean = new RechercheCentresBean();
	    rechercheCentresBean.setAutoEcole(autoEcole);
	    rechercheCentresBean.setCentresRattachesList(lstCentreRattaches);
	    map.put(Constants.RECHERCHE_CENTRE, rechercheCentresBean);
	    map.put(Constants.QUALIFICATIONS_LIST, rattachementsManager.getQualificationsNonPro());
	} else {
	    sendUnauthorized("init", map, userConnected);
	}
	return new ModelAndView(viewName, Constants.MODELE, map);
    }

    protected void sendUnauthorized(final String action, Map<String, Object> map, final ViewUtilisateur userConnected) {
	logger.warn("action non autorisee : " + action + " / user = " + userConnected.getInformations() + " / profil = " + userConnected.getLibelleProfil());
	map.put("msgInfos", messages.getMessage("action.non.autorisee"));
    }

    /**
     * methode appelée lorsque l'on clique sur le bouton "rechercher" les centres disponible
     * 
     * @param request
     * @param response
     * @param command
     * @return
     * @throws Exception
     */
    public ModelAndView submitCriteres(HttpServletRequest request, HttpServletResponse response, RechercheCentresBean rechercheCentresBean) throws Exception {
	Map<String, Object> map = new HashMap<String, Object>();
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	AutoEcole autoEcole = (AutoEcole) request.getSession().getAttribute(Constants.SES_CHOIX_AE_RATTACH);
	if (userConnected.getRestriction() instanceof AutoEcole) {
	    if (autoEcole == null)
		autoEcole = (AutoEcole) userConnected.getRestriction();
	    rechercheCentresBean.setCentresRattachesList(rattachementsManager.rechercheCentreRattaches(autoEcole));
	    String nom = rechercheCentresBean.getNom();
	    String numero = rechercheCentresBean.getNumero();
	    String codePostal = rechercheCentresBean.getCode();
	    String ville = rechercheCentresBean.getVille();
	    List<LigneCentreBean> lstCentreDispo = rattachementsManager.rechercherCentresDispo(nom, numero, codePostal, ville, autoEcole);
	    rechercheCentresBean.setCentresDispoList(lstCentreDispo);
	    map.put(Constants.QUALIFICATIONS_LIST, rattachementsManager.getQualificationsNonPro());
	    map.put(Constants.RECHERCHE_CENTRE, rechercheCentresBean);
	} else {
	    sendUnauthorized("submitCriteres", map, userConnected);
	}
	return new ModelAndView(viewName, Constants.MODELE, map);
    }

    /*
     * Ajout d'une demande de rattachement ou de detachement
     */
    private boolean ajoutDemande(HttpServletRequest request, List<LigneCentreBean> lstCentre, boolean isDemandeRattachement, AutoEcole aue, Departement dept, Map<String, Object> map)
	    throws ApplicationException {
	List<String> listPerCode = rattachementsManager.getQualificationsNonPro();
	boolean isAjoute = false;
	List<String> errors = new ArrayList<>();
	for (LigneCentreBean ligneCentreBeanView : lstCentre) {
	    for (String percode : listPerCode) {
		String numeroCentreBean = ligneCentreBeanView.getNumero();
		String numeroCentre;
		if (isDemandeRattachement) {
		    numeroCentre = "rattach_" + numeroCentreBean + "_" + percode;
		} else {
		    numeroCentre = "detach_" + numeroCentreBean + "_" + percode;
		}
		String check = request.getParameter(numeroCentre);
		if (check != null) {

		    boolean isRepartiteurDemandeur = false;
		    // attention ici on laisse aue_code car il ne s'agit pas de lauto ecole connected
		    try {
			rattachementsManager.ajoutDemande(aue, numeroCentreBean, percode, dept, isDemandeRattachement, isRepartiteurDemandeur);
			isAjoute = true;
		    } catch (ConcurrentDataAccessException ex) {
			if (isDemandeRattachement) {
			    errors.add(MessageFormat.format(messages.getMessage("rattachement.concurrent.modification"), new Object[] { percode, ligneCentreBeanView.getNom() }));
			} else {
			    errors.add(MessageFormat.format(messages.getMessage("detachement.concurrent.modification"), new Object[] { percode, ligneCentreBeanView.getNom() }));
			}
		    }
		    String typeDemande = "rattachement";
		    if (!isDemandeRattachement) {
			typeDemande = "détachement";
		    }
		    envoiMessageRepartiteur(typeDemande, percode, ligneCentreBeanView, aue, dept);
		}
	    }
	}
	if (!errors.isEmpty()) {
	    if (isAjoute) {
		map.put("msgPartSaved", messages.getMessage("soumission.demandes.partiellement.enregistrees"));
	    }
	    map.put("msgErrorConcurrent", errors);

	}
	return isAjoute;
    }

    /**
     * methode appelée lorsque l'on clique sur le bouton "détachement qualifications" MAJ du tableau des centres rattachés
     * 
     * @param request
     * @param response
     * @param command
     * @return
     * @throws Exception
     * @throws ApplicationException
     */
    public ModelAndView submitDetachement(HttpServletRequest request, HttpServletResponse response, RechercheCentresBean rechercheCentresBean) throws Exception, ApplicationException {
	Map<String, Object> map = new HashMap<String, Object>();
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	AutoEcole autoEcole = (AutoEcole) request.getSession().getAttribute(Constants.SES_CHOIX_AE_RATTACH);
	if (userConnected.getRestriction() instanceof AutoEcole) {
	    if (userConnected.getLibelleProfil().equals(EnumProfils.REPARTITEUR.getLibelle()) || userConnected.getLibelleProfil().equals(EnumProfils.AE.getLibelle())) {
		if (autoEcole == null)
		    autoEcole = (AutoEcole) userConnected.getRestriction();
		Departement dept = autoEcole.getDepartement();
		// on detache les qualifications cochées
		try {
		    if (rechercheCentresBean.getCentresRattachesList() != null) {
			boolean isAjoute = ajoutDemande(request, rechercheCentresBean.getCentresRattachesList(), false, autoEcole, dept, map);
			List<LigneCentreBean> lstCentreRattaches = rattachementsManager.rechercheCentreRattaches(autoEcole);
			RechercheCentresBean rechercheCentresBeanNew = new RechercheCentresBean();
			rechercheCentresBeanNew.setCentresRattachesList(lstCentreRattaches);
			map.put(Constants.RECHERCHE_CENTRE, rechercheCentresBeanNew);
			map.put(Constants.QUALIFICATIONS_LIST, rattachementsManager.getQualificationsNonPro());
			if (isAjoute) {
			    map.put("msgInfos", messages.getMessage("soumission.demandes.detachement.enregistrees"));
			} else {
			    map.put("msgErrors", messages.getMessage("detachement.qualification.required"));
			}

		    } else {
			logger.debug("Pour se détacher d''un centre,il faut selectionner un centre dans le tableau nommé centres d'examen rattachés.");
		    }
		} catch (ApplicationException ex) {
		    map.put("msgErrors", ex.getListMessage());
		}
	    }
	} else {
	    sendUnauthorized("submitDetachement", map, userConnected);
	}
	return new ModelAndView(viewName, Constants.MODELE, map);
    }

    /**
     * methode appelée lorsque l'on clique sur le bouton "rattacher qualifications" MAJ du tableau des centres disponibles
     * 
     * @param request
     * @param response
     * @param command
     * @return
     * @throws Exception
     */
    public ModelAndView submitRattachement(HttpServletRequest request, HttpServletResponse response, RechercheCentresBean rechercheCentresBean) throws Exception {
	Map<String, Object> map = new HashMap<String, Object>();
	// on attache les qualifications cochées
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	AutoEcole autoEcole = (AutoEcole) request.getSession().getAttribute(Constants.SES_CHOIX_AE_RATTACH);
	if (userConnected.getLibelleProfil().equals(EnumProfils.REPARTITEUR.getLibelle()) || userConnected.getLibelleProfil().equals(EnumProfils.AE.getLibelle())) {
	    if (userConnected.getRestriction() instanceof AutoEcole) {
		if (autoEcole == null)
		    autoEcole = (AutoEcole) userConnected.getRestriction();
		try {
		    if (rechercheCentresBean.getCentresDispoList() != null) {
			List<LigneCentreBean> lstNumeroCentreDispo = rechercheCentresBean.getCentresDispoList();
			boolean isAjoute = ajoutDemande(request, lstNumeroCentreDispo, true, autoEcole, autoEcole.getDepartement(), map);
			List<LigneCentreBean> lstCentreDispoRetour = rattachementsManager.rechercherCentresDispo(rechercheCentresBean.getNumero(), rechercheCentresBean.getNom(),
				rechercheCentresBean.getCode(), rechercheCentresBean.getVille(), autoEcole);
			rechercheCentresBean.setCentresDispoList(lstCentreDispoRetour);
			rechercheCentresBean.setCentresRattachesList(rattachementsManager.rechercheCentreRattaches(autoEcole));
			map.put(Constants.QUALIFICATIONS_LIST, rattachementsManager.getQualificationsNonPro());
			map.put(Constants.RECHERCHE_CENTRE, rechercheCentresBean);
			if (isAjoute) {
			    map.put("msgInfos", messages.getMessage("soumission.demandes.rattachement.enregistrees"));
			} else {
			    map.put("msgErrors", messages.getMessage("rattachement.qualification.required"));
			}
		    } else {
			logger.debug("Pour se rattacher à un centre,il faut selectionner un centre dans le tableau nommé centres d'examen disponibles.");
		    }
		} catch (ApplicationException ex) {
		    map.put("msgErrors", ex.getListMessage());
		}
	    }
	} else {
	    sendUnauthorized("submitRattachement", map, userConnected);
	}
	return new ModelAndView(viewName, Constants.MODELE, map);
    }

    /* Serveur mail */
    private EmailSender emailSender;

    public EmailSender getEmailSender() {
	return emailSender;
    }

    public void setEmailSender(EmailSender emailSender) {
	this.emailSender = emailSender;
    }

    public ResourceBundle getEmailBundle() {
	return emailBundle;
    }

    public void setEmailBundle(ResourceBundle emailBundle) {
	this.emailBundle = emailBundle;
    }

    public void setMessages(WebBundle messages) {
	this.messages = messages;
    }

    public void envoiMessageRepartiteur(String typeDemande, String nomQualification, LigneCentreBean ligneCentreBean, AutoEcole aue, Departement dept) {
	List<String> destinatairesList = rattachementsManager.getMailRepartiteur(dept);
	Object[] args = { aue.getAueCodeRafael(), typeDemande, nomQualification, ligneCentreBean.getNom(), ligneCentreBean.getNumero() };
	String filename = emailBundle.getString("email.soumission.demande.path");
	String sujet = MessageFormat.format(emailBundle.getString("email.soumission.demande.sujet"), typeDemande);
	InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filename);
	emailSender.sendMail(sujet, destinatairesList, LibelleUtil.getString(inputStream, args));

    }
}
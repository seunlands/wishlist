package i2.application.extranet.action.rattachement;

import i2.application.aurige.bean.AutoEcole;
import i2.application.aurige.bean.Departement;
import i2.application.extranet.bean.view.ViewUtilisateur;
import i2.application.extranet.bean.view.rattachement.LigneDemandeValidationBean;
import i2.application.extranet.bean.view.rattachement.ValidationDemandesBean;
import i2.application.extranet.business.authentification.IAutoEcoleManager;
import i2.application.extranet.business.rattachement.IValidationDemandesManager;
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
 * Controller d'actions pour le tableau de validation des demandes de rattachement et de detachement par le repartiteur
 * 
 * @author bull
 */
public class ValidationDemandesController extends MultiActionController {

    private IValidationDemandesManager validationDemandesManager;

    private String viewName = Constants.VALIDATION;

    private IAutoEcoleManager autoEcoleManager;

    private ResourceBundle emailBundle;

    public ResourceBundle getEmailBundle() {
	return emailBundle;
    }

    public void setEmailBundle(ResourceBundle emailBundle) {
	this.emailBundle = emailBundle;
    }

    private WebBundle messages;

    public WebBundle getMessages() {
	return messages;
    }

    public void setMessages(WebBundle messages) {
	this.messages = messages;
    }

    public void setAutoEcoleManager(IAutoEcoleManager autoEcoleManager) {
	this.autoEcoleManager = autoEcoleManager;
    }

    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) throws Exception {
	Map<String, Object> map = new HashMap<String, Object>();

	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	if (userConnected.getRestriction() instanceof AutoEcole) {
	    AutoEcole autoEcoleConnected = (AutoEcole) userConnected.getRestriction();
	    Departement departement = autoEcoleConnected.getDepartement();
	    map.put(Constants.DEMANDES_ATTENTES, majTableau(departement));

	} else {
	    sendUnauthorized("init", map, userConnected);
	}
	return new ModelAndView(viewName, map);
    }

    public ModelAndView submitValider(HttpServletRequest request, HttpServletResponse response, ValidationDemandesBean command) throws Exception {
	Map<String, Object> map = new HashMap<String, Object>();
	boolean bModifie = false;
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	if (userConnected.getLibelleProfil().equals(EnumProfils.REPARTITEUR.getLibelle()) && userConnected.getRestriction() instanceof AutoEcole) {
	    AutoEcole autoEcoleConnected = (AutoEcole) userConnected.getRestriction();
	    Departement departement = autoEcoleConnected.getDepartement();
	    for (LigneDemandeValidationBean ligneDemandeValidationBean : command.getDemandesList()) {
		if (ligneDemandeValidationBean != null && ligneDemandeValidationBean.isSelected()) {
		    bModifie = true;
		    validationDemandesManager.validerDemande(ligneDemandeValidationBean.getIdDmd());
		    envoiMessageReponseAutoEcole(ligneDemandeValidationBean, true, null);
		}
	    }
	    if (bModifie) {
		map.put("msgInfos", messages.getMessage("validation.demandes.acceptees"));
		map.put(Constants.DEMANDES_ATTENTES, majTableau(departement));
	    } else {
		map.put("msgErrors", messages.getMessage("validation.demande.selected.required"));
		map.put(Constants.DEMANDES_ATTENTES, command);
	    }

	} else {
	    sendUnauthorized("submitValider", map, userConnected);
	}
	return new ModelAndView(viewName, map);
    }

    private ValidationDemandesBean majTableau(Departement departement) {
	List<LigneDemandeValidationBean> listeDemandes = validationDemandesManager.rechercheDemandesEnCoursParDepartement(departement);
	ValidationDemandesBean demandesBean = new ValidationDemandesBean();
	demandesBean.getDemandesList().addAll(listeDemandes);
	return demandesBean;

    }

    protected void sendUnauthorized(final String action, Map<String, Object> map, final ViewUtilisateur userConnected) {
	logger.warn("action non autorisee : " + action + " / user = " + userConnected.getInformations() + " / profil = " + userConnected.getLibelleProfil());
	map.put("msgInfos", messages.getMessage("action.non.autorisee"));

    }

    public ModelAndView submitRefuser(HttpServletRequest request, HttpServletResponse response, ValidationDemandesBean command) throws Exception {
	Map<String, Object> map = new HashMap<String, Object>();
	ViewUtilisateur userConnected = (ViewUtilisateur) request.getSession().getAttribute(Constants.SES_UTILISATEUR);
	if (userConnected.getLibelleProfil().equals(EnumProfils.REPARTITEUR.getLibelle()) && userConnected.getRestriction() instanceof AutoEcole) {
	    AutoEcole autoEcoleConnected = (AutoEcole) userConnected.getRestriction();
	    Departement departement = autoEcoleConnected.getDepartement();
	    boolean isRefusee = false;
	    if (command.getMotifRefus().isEmpty()) {
		map.put("msgErrors", messages.getMessage("validation.demandes.motif.obligatoire"));
		map.put(Constants.DEMANDES_ATTENTES, command);
	    } else {
		for (LigneDemandeValidationBean ligneDemandeValidationBean : command.getDemandesList()) {
		    if (ligneDemandeValidationBean.isSelected()) {
			isRefusee = true;
			validationDemandesManager.refuserDemande(ligneDemandeValidationBean.getIdDmd(), command.getMotifRefus());
			envoiMessageReponseAutoEcole(ligneDemandeValidationBean, false, command.getMotifRefus());
		    }
		}
		if (isRefusee) {
		    map.put("msgInfos", messages.getMessage("validation.demandes.refusees"));
		    map.put(Constants.DEMANDES_ATTENTES, majTableau(departement));
		} else {
		    map.put(Constants.DEMANDES_ATTENTES, command);
		    map.put("msgErrors", messages.getMessage("validation.demande.selected.required"));
		}

	    }

	} else {
	    sendUnauthorized("submitRefuser", map, userConnected);
	}
	return new ModelAndView(viewName, map);
    }

    /* Serveur mail */
    private EmailSender emailSender;

    public EmailSender getEmailSender() {
	return emailSender;
    }

    public void setEmailSender(EmailSender emailSender) {
	this.emailSender = emailSender;
    }

    public void envoiMessageReponseAutoEcole(LigneDemandeValidationBean ligneDemandeValidationBean, boolean bValidee, String motifRefus) {
	String typeDemande = ligneDemandeValidationBean.getTypeDemande();
	String typeOperation;
	if (bValidee) {
	    typeOperation = "validée";
	} else {
	    typeOperation = "refusée";
	}
	AutoEcole autoEcole = autoEcoleManager.readAutoEcole(ligneDemandeValidationBean.getAueCode());
	String nomQualification = ligneDemandeValidationBean.getPerCode();
	List<String> destinatairesList = new ArrayList<String>();
	destinatairesList.add(autoEcole.getAueEmail());
	String nomCentre = ligneDemandeValidationBean.getNomCentre();
	Object[] args = { typeDemande, nomQualification, nomCentre, typeOperation, motifRefus };
	String filename;
	if (bValidee) {
	    filename = emailBundle.getString("email.validation.demande.path");
	} else {
	    filename = emailBundle.getString("email.refus.demande.path");
	}
	String sujet = MessageFormat.format(emailBundle.getString("email.validation.demande.sujet"), typeDemande, typeOperation);
	InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filename);
	emailSender.sendMail(sujet, destinatairesList, LibelleUtil.getString(inputStream, args));
    }

    public void setValidationDemandesManager(IValidationDemandesManager validationDemandesManager) {
	this.validationDemandesManager = validationDemandesManager;
    }
}

package i2.application.extranet.action.restitution;

import i2.application.extranet.action.affectation.AffectationTxtController;
import i2.application.extranet.business.restitution.IRestitutionManager;

import org.apache.log4j.Logger;

/**
 * 
 * @author BULL SAS
 * 
 *         Contrôleur d'actions pour le tableau restitutions d'unités.
 */

public class RestitutionTxtController extends AffectationTxtController {

    private final static Logger logger = Logger.getLogger(RestitutionTxtController.class);

    /**
     * Constructeur par défaut.
     */
    public RestitutionTxtController() {
	super.setModeAccessibilite(false);
    }

    public void setRestitutionManager(IRestitutionManager restitutionManager) {
	this.affectationManager = restitutionManager;
    }

}

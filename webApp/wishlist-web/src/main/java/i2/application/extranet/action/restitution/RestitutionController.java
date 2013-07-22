package i2.application.extranet.action.restitution;

import i2.application.extranet.action.affectation.AffectationController;
import i2.application.extranet.business.restitution.IRestitutionManager;

import org.apache.log4j.Logger;

/**
 * 
 * @author BULL SAS
 * 
 *         Contrôleur d'actions pour le tableau restitutions d'unités
 */

public class RestitutionController extends AffectationController {

    private final static Logger logger = Logger.getLogger(RestitutionController.class);

    /**
     * Constructeur par défaut.
     */
    public RestitutionController() {
	super.setModeAccessibilite(false);
    }

    public void setRestitutionManager(IRestitutionManager restitutionManager) {
	this.affectationManager = restitutionManager;
    }

}

package i2.application.extranet.form;

import i2.application.extranet.bean.view.IViewLignePlanning;

import java.util.List;

/**
 * Interface du formulaire retour d'un planning semaine
 * 
 * @author Bull
 * @category _return_form
 * 
 */
public interface IPlanningSemaineForm {

    /**
     * @return the lignePlanningSemaine
     */
    public List<IViewLignePlanning> getLignePlanning();

    /**
     * 
     * @return the String
     */
    public String getAueCodeSelected();

}

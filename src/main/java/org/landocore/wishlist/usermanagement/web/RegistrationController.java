package org.landocore.wishlist.usermanagement.web;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.landocore.wishlist.common.exception.IncompleteUserException;
import org.landocore.wishlist.common.exception.PasswordStrengthException;
import org.landocore.wishlist.profile.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Faces controller bean for the account creation part.
 * @author LANDSBERG-S
 *
 */
@ManagedBean
@RequestScoped
public class RegistrationController {
	
	/**
	 * The Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.
			getLogger(RegistrationController.class);

	/**
	 * the user service.
	 */
	@ManagedProperty(value = "#{profileService}")
	private ProfileService profileService;

	/**
	 * the account creation form.
	 */
	@ManagedProperty(value = "#{registrationForm}")
	private RegistrationForm registrationForm;

	/**
	 * account creation method.
	 * @return the next view
	 */
	public final String register() {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Received request to register a new users");
		}
		
		
		try {
			profileService.createProfile(registrationForm.getUserName(),
					registrationForm.getRawPassword(),
					registrationForm.getEmail(),
					registrationForm.getName(), registrationForm.getLastName(),
					registrationForm.getBirthDate());
			FacesMessage msg = new FacesMessage();
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			msg.setSummary("User succesfully created.");
			msg.setDetail("User succesfully created.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "/auth/login";
			
		} catch (IncompleteUserException e) {
			LOGGER.info("User isn't complete. Reason: " + e.getMessage());	
			FacesMessage msg = new FacesMessage();
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("Error when creating the user.");
			msg.setDetail("User is incomplete");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (PasswordStrengthException e) {
			LOGGER.info("Password issue. Reason: " + e.getMessage());
			FacesMessage msg = new FacesMessage();
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setSummary("Password isn't complexe enough.");
			msg.setDetail("Password isn't complexe enough.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		return null;

	}

	//------------------getter and setters

	/**
	 * Setter of the registerForm.
	 * @param pRegistrationForm the registerForm to set
	 */
	public final void setRegistrationForm(
			final RegistrationForm pRegistrationForm) {
		this.registrationForm = pRegistrationForm;
	}

	/**
	 * @param pProfileService the profileService to set
	 */
	public final void setProfileService(final ProfileService pProfileService) {
		this.profileService = pProfileService;
	}



}

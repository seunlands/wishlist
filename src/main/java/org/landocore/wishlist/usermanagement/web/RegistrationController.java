package org.landocore.wishlist.usermanagement.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.landocore.wishlist.usermanagement.domain.User;
import org.landocore.wishlist.usermanagement.service.UserService;

/**
 * Faces controller bean for the account creation part.
 * @author LANDSBERG-S
 *
 */
@ManagedBean
@RequestScoped
public class RegistrationController {

	/**
	 * the user service.
	 */
	@ManagedProperty(value = "#{userService}")
	private UserService userService;

	/**
	 * the account creation form.
	 */
	@ManagedProperty(value = "#{registrationForm}")
	private RegistrationForm registrationForm;

	/**
	 * account creation method.
	 */
	public final void register() {
		
		User user = new User(registrationForm.getUserName(), registrationForm.getEmail(), registrationForm.getRawPassword());
		//every user should be enabled by administrator
		

	}

	//------------------getter and setters
	/**
	 * Setter of the user service.
	 * @param pUserService the user service
	 */
	public final void setUserService(final UserService pUserService) {
		this.userService = pUserService;
	}

	/**
	 * Setter of the registerForm.
	 * @param pRegistrationForm the registerForm to set
	 */
	public final void setRegsiterForm(
			final RegistrationForm pRegistrationForm) {
		this.registrationForm = pRegistrationForm;
	}



}

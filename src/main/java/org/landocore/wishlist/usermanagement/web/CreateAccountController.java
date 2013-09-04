package org.landocore.wishlist.usermanagement.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.landocore.wishlist.usermanagement.service.UserService;

/**
 * Faces controller bean for the account creation part.
 * @author LANDSBERG-S
 *
 */
@ManagedBean
@RequestScoped
public class CreateAccountController {

	/**
	 * the user service.
	 */
	@ManagedProperty(value = "#{userService}")
	private UserService userService;

	/**
	 * the account creation form.
	 */
	@ManagedProperty(value = "#{createAccountForm}")
	private CreateAccountForm createAccountForm;

	/**
	 * account creation method.
	 */
	public final void createAccount() {

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
	 * Setter of the createAccountForm.
	 * @param pCreateAccountForm the createAccountForm to set
	 */
	public final void setCreateAccountForm(
			final CreateAccountForm pCreateAccountForm) {
		this.createAccountForm = pCreateAccountForm;
	}



}

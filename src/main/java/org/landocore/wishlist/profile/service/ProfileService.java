package org.landocore.wishlist.profile.service;

import java.util.Date;

import org.landocore.wishlist.common.exception.IncompleteUserException;
import org.landocore.wishlist.common.exception.PasswordStrengthException;
import org.landocore.wishlist.profile.domain.Profile;


/**
 *  Interface for the business services regarding the profile.
 * @author LANDSBERG-S
 */
public interface ProfileService {

	/**
	 * creates the user and the profile.
	 * @param userName the username
	 * @param rawPassword the password
	 * @param email the email
	 * @param name the name
	 * @param lastName the lastname
	 * @param birthDate the birthDate
	 * @return the profile created
	 * @throws IncompleteUserException when required fields are missing
	 * @throws PasswordStrengthException when password isn't secure enough
	 */
	Profile createProfile(final String userName,
			final String rawPassword, final String email,
			final String name, final String lastName,
			final Date birthDate)
					throws IncompleteUserException, PasswordStrengthException;

}

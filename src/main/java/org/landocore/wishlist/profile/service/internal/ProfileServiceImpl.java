package org.landocore.wishlist.profile.service.internal;

import java.util.Date;

import org.landocore.wishlist.common.exception.IncompleteUserException;
import org.landocore.wishlist.common.exception.PasswordStrengthException;
import org.landocore.wishlist.profile.domain.Profile;
import org.landocore.wishlist.profile.repository.ProfileRepository;
import org.landocore.wishlist.profile.service.ProfileService;
import org.landocore.wishlist.usermanagement.domain.User;
import org.landocore.wishlist.usermanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * business class of the profile part.
 * @author LANDSBERG-S
 *
 */
@Service("profileService")
public class ProfileServiceImpl implements ProfileService {
	
	/**
	 * the logger.
	 */
	private static final Logger LOGGER = LoggerFactory.
			getLogger(ProfileServiceImpl.class);
	
	/**
	 * the profile repository.
	 */
	private ProfileRepository profileRepository;
	
	/**
	 * the user service.
	 */
	private UserService userService;

	@Override
	public final Profile createProfile(final String userName,
			final String rawPassword, final String email,
			final String name, final String lastName,
			final Date birthDate)
					throws IncompleteUserException, PasswordStrengthException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Creating user account for username : "
					+ userName + " / rawPassword :" + rawPassword 
					+ " / email : " + email);
		}
		
		Profile profile = new Profile();
		User user = null;
		try {
			user = userService.createUser(new User(
					userName, email, rawPassword));
		} catch (IncompleteUserException exception) {
			LOGGER.warn("Error creating user. Reason : "
					+ exception.getMessage());
			throw exception;
		} catch (PasswordStrengthException exception) {
			LOGGER.warn("Error creating user. Reason : "
					+ exception.getMessage());
			throw exception;
		} 
		profile.setUser(user);
		profile.setBirthDate(birthDate);
		profile.setLastName(lastName);
		profile.setName(name);

		profileRepository.saveOrUpdate(profile);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Profile created with id " + profile.getId());
		}
		
		return profile;

	}

	
	
	//getters and setters
	
	/**
	 * @param pProfileRepository the profileRepository to set
	 */
	public final void setProfileRepository(
			final ProfileRepository pProfileRepository) {
		this.profileRepository = pProfileRepository;
	}

	/**
	 * @param pUserService the userService to set
	 */
	public final void setUserService(final UserService pUserService) {
		this.userService = pUserService;
	}


}

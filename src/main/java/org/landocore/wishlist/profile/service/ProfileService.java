package org.landocore.wishlist.profile.service;

import org.landocore.wishlist.common.exception.IncompleteUserException;
import org.landocore.wishlist.profile.domain.Profile;


/**
 *  Interface for the business services regarding the profile.
 * @author LANDSBERG-S
 */
public interface ProfileService {

    /**
     * creates the user and the profile.
     * @param profile the profile to create
     * @throws IncompleteUserException if user isn t fully filled in
     */
    Profile createProfile(String userName, String rawPassword, String email) throws IncompleteUserException;

}

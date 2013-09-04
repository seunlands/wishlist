package org.landocore.wishlist.profile.repository;

import org.landocore.wishlist.common.repository.AbstractDao;
import org.landocore.wishlist.profile.domain.Profile;
import org.landocore.wishlist.usermanagement.domain.User;

/**
 * Interface of the profile entity repository.
 * @author LANDSBERG-S
 *
 */
public interface ProfileRepository extends AbstractDao<Profile, Long> {

    /**
     * Find the profile associated with the user.
     * @param pUser user whom the profile is
     * @return a profile, null if not found, or username of user entity NULL
     */
    Profile findProfileByUser(User pUser);

}

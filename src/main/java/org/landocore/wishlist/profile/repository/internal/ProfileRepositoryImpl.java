package org.landocore.wishlist.profile.repository.internal;

import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.landocore.wishlist.common.repository.internal.AbstractDaoImpl;
import org.landocore.wishlist.profile.domain.Profile;
import org.landocore.wishlist.profile.repository.ProfileRepository;
import org.landocore.wishlist.usermanagement.domain.User;

/**
 * @author LANDSBERG-S Profile repository
 */
public class ProfileRepositoryImpl extends AbstractDaoImpl<Profile, Long>
        implements ProfileRepository {

    /**
     * Constructor.
     *
     * @param sessionFactory the hibernate sessionfactory
     */
    public ProfileRepositoryImpl(final SessionFactory sessionFactory) {
        super(Profile.class, sessionFactory);
    }

    @Override
    public final Profile findProfileByUser(final User pUser) {

        Profile profile = null;

        if (pUser != null) {
            Criterion criterion = Restrictions.eq("user", pUser);
            List<Profile> lstProfiles = findByCriteria(criterion);
            if (lstProfiles != null && !lstProfiles.isEmpty()) {
                profile = lstProfiles.get(0);
            }
        }

        return profile;
    }
}

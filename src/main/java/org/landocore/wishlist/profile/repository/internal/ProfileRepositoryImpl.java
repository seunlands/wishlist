package org.landocore.wishlist.profile.repository.internal;

import org.hibernate.SessionFactory;
import org.landocore.wishlist.common.repository.internal.AbstractDaoImpl;
import org.landocore.wishlist.profile.domain.Profile;
import org.landocore.wishlist.profile.repository.ProfileRepository;

/**
 * @author LANDSBERG-S
 *
 */
public class ProfileRepositoryImpl extends AbstractDaoImpl<Profile, Long>
implements ProfileRepository {

	/**
	 * Constructor.
	 * @param sessionFactory the hibernate sessionfactory
	 */
	public ProfileRepositoryImpl(final SessionFactory sessionFactory) {
		super(Profile.class, sessionFactory);
	}


}

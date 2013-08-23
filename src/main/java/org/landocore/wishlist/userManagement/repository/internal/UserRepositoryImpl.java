package org.landocore.wishlist.usermanagement.repository.internal;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.landocore.wishlist.common.repository.internal.AbstractDaoImpl;
import org.landocore.wishlist.usermanagement.domain.User;
import org.landocore.wishlist.usermanagement.repository.UserRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 28/07/13
 * Time: 07:54
 * Implementation of the user repository
 * @see org.landocore.wishlist.usermanagement.repository.UserRepository
 */

public class UserRepositoryImpl extends AbstractDaoImpl<User, Long>
        implements UserRepository {

    /**
     * Constructor of the repository.
     * @param sessionFactory the session factory
     */
    public UserRepositoryImpl(final SessionFactory sessionFactory) {
        super(User.class, sessionFactory);
    }


    @Override
    public User findByLogin(final String login) {
        Criterion criterion = Restrictions.eq("username", login);
        List<User> ret = super.findByCriteria(criterion);
        if (ret == null || ret.isEmpty()) {
            return null;
        }
        return ret.get(0);
    }

    @Override
    public final User save(final User user) {
        super.saveOrUpdate(user);
        return user;
    }

    @Override
    public final User findByEmail(final String email) {
        Criterion criterion = Restrictions.eq("email", email);
        List<User> ret = super.findByCriteria(criterion);
        if (ret == null || ret.isEmpty()) {
            return null;
        }
        return ret.get(0);
    }
}

package org.landocore.wishlist.repositories.login;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.landocore.wishlist.beans.login.User;
import org.landocore.wishlist.repositories.AbstractDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 28/07/13
 * Time: 07:54
 * To change this template use File | Settings | File Templates.
 */

@Repository("userRepository")
public class UserRepositoryImpl extends AbstractDaoImpl<User, Long> implements UserRepository{

    public UserRepositoryImpl(SessionFactory sessionFactory){
        super(User.class, sessionFactory);
    }


    @Override
    public User findByLogin(String login) {
        Criterion criterion = Restrictions.eq("username", login);
        List<User> ret = super.findByCriteria(criterion);
        if(ret==null || ret.isEmpty()){
            return null;
        }
        return ret.get(0);
    }

    @Override
    public User save(User user) {
        super.saveOrUpdate(user);
        return user;
    }

    @Override
    public User findById(Long userId) {
        return super.findById(userId);
    }

    @Override
    public User findByEmail(String email) {
        Criterion criterion = Restrictions.eq("email", email);
        List<User> ret = super.findByCriteria(criterion);
        if(ret==null || ret.isEmpty()){
            return null;
        }
        return ret.get(0);
    }

    @Override
    public User findByLoginOpenId(String loginOpenId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public User findByFacebookId(String facebookId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

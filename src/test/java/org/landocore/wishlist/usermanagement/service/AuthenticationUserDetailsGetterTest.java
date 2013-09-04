package org.landocore.wishlist.usermanagement.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.landocore.wishlist.login.service.internal.AuthenticationUserDetailsGetter;
import org.landocore.wishlist.usermanagement.domain.User;
import org.landocore.wishlist.usermanagement.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created with IntelliJ IDEA.
 * User: LANDSBERG-S
 * Date: 24/08/13
 * Time: 20:27
 * Unit tests for class AuthenticationUserDetailsGetter.
 */
public class AuthenticationUserDetailsGetterTest {

	/**
	 * the authenticationuserdetailsgetter to be tested
	 */
    private AuthenticationUserDetailsGetter authenticationUserDetailsGetter;

    /**
     * 
     */
    private static final String USER_NULL = "userNull";


    @Before
    public void setup() {
        authenticationUserDetailsGetter = new AuthenticationUserDetailsGetter();

        UserRepository userRepo = new UserRepository() {
            @Override
            public User findByLogin(String pUsername) {
                if(pUsername.equals(USER_NULL)){
                    return null;
                }
                return new User(pUsername, pUsername, pUsername);
            }

            @Override
            public User save(User pUser) {
                return null;
            }

            @Override
            public User findById(Long pUserId) {
                return null;
            }

            @Override
            public User findByEmail(String pEmail) {
                return null;
            }

            @Override
            public void saveOrUpdate(User entity) {

            }

            @Override
            public void delete(User entity) {

            }

            @Override
            public List findByCriteria(Criterion criterion) {
                return null;
            }

            @Override
            public List findByCriteria(Map<String, Object> criteria) {
                return null;
            }
        };
        authenticationUserDetailsGetter.setUserRepository(userRepo);

    }


    @Test
    public void testLoadUserByUsername() {
        UserDetails userDetails = authenticationUserDetailsGetter.loadUserByUsername("test");
        assertNotNull("UserDetails shouldn't be NULL", userDetails);
        assertEquals("username is not correct", "test", userDetails.getUsername());
        assertEquals("password is not correct", "test", userDetails.getPassword());

        try{
            userDetails = authenticationUserDetailsGetter.loadUserByUsername(USER_NULL);
            Assert.fail();
        } catch (UsernameNotFoundException e) {

        }


    }

}

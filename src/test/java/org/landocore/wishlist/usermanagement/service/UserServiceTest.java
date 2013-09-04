package org.landocore.wishlist.usermanagement.service;

import org.junit.Assert;
import org.hibernate.criterion.Criterion;
import org.junit.Before;
import org.junit.Test;
import org.landocore.wishlist.common.exception.IncompleteUserException;
import org.landocore.wishlist.usermanagement.domain.User;
import org.landocore.wishlist.usermanagement.repository.UserRepository;
import org.landocore.wishlist.usermanagement.service.internal.UserServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: LANDSBERG-S
 * Date: 25/06/13
 * Time: 13:43
 * User service unit tests.
 */

public class UserServiceTest {

	/**
	 * The user service to be tested.
	 */
    private UserServiceImpl userService;

    /**
     * password encoder.
     */
    private PasswordEncoder passwordEncoder;

    private final static String RETURN_NULL = "returnNull";


    @Before
    public void setup() {
        userService = new UserServiceImpl();

        UserRepository userRepo = new UserRepository() {
            @Override
            public User findByLogin(String pUsername) {
                if(pUsername == null || pUsername.equals(RETURN_NULL) ){
                    return null;
                }
                return new User(pUsername, pUsername, pUsername);
            }

            @Override
            public User save(User pUser) {
                pUser.setId(999L);
                return pUser;
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
                entity.setId(999L);
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
        } ;
        userService.setUserRepository(userRepo);

        passwordEncoder = new StandardPasswordEncoder();
        userService.setPasswordEncoder(passwordEncoder);
    }

    @Test
    public final void testGetUserByUsername(){
        User user = userService.getUserByUsername("test");
        assertNotNull("User shouldn't be NULL", user);
        assertEquals("Username returned isn't correct", "test", user.getUsername());

        User userNull = userService.getUserByUsername(RETURN_NULL);
        assertNull("User should be NULL", userNull);
    }

    @Test
    public final void testCreateUser() throws IncompleteUserException {

        User user = new User("test", "test", "test");
        user = userService.createUser(user);
        assertNotNull("user shouldn't be NULL", user);
        assertEquals("id should be 999", 999L, (long)user.getId());
        assertTrue("encoded password isn't correct", passwordEncoder.matches("test", user.getPassword()));


        //null user
        user = null;
        user = userService.createUser(user);
        assertNull("user should be NULL", user);

        //null password
        user = new User("test", "test", null );
        try{
            user = userService.createUser(user);
            Assert.fail();
        } catch (IncompleteUserException e) {

        }

    }

}

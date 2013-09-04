package org.landocore.wishlist.usermanagement.repository;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.landocore.wishlist.common.repository.AbstractRepositoryTesting;
import org.landocore.wishlist.usermanagement.domain.User;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created with IntelliJ IDEA.
 * User: LANDSBERG-S
 * Date: 21/08/13
 * Time: 11:54
 * Unit test for user repository.
 */

public class UserRepositoryTest extends AbstractRepositoryTesting {

    @Autowired
    private UserRepository userRepository;

    @Before
    public void prepare(){
        //on ajoute des user dans la base
        
    	User user = new User("test", "test", "test");
    	userRepository.save(user);
        
    }

    @Test
    public void testFindByLogin() {
        //user in db
        User user = userRepository.findByLogin("test");
        assertNotNull("User shouldn't be null", user);
        assertEquals("Username is not correct", "test", user.getUsername());

        //user not in db
        User user1 = userRepository.findByLogin("not there");
        assertNull("User should be null", user1);
    }

    @Test
    public void testSave() {
        //user not in db -> save
        {
            User user = new User("test1", "test1", "test1");
            userRepository.save(user);
            assertNotNull("Id should not be null", user.getId());
            assertEquals("Username is not correct", "test1", user.getUsername());
            assertEquals("Email is not correct", "test1", user.getUsername());
            assertEquals("Password is not correct", "test1", user.getUsername());
        }
        //user in db -> update
        {
            User user = userRepository.findByLogin("test");
            user.setUsername("test1");
            user.setEmail("test1");
            user.setPassword("test1");
            userRepository.save(user);
            assertNotNull("Id should not be null", user.getId());
            assertEquals("Username is not correct", "test1", user.getUsername());
            assertEquals("Email is not correct", "test1", user.getUsername());
            assertEquals("Password is not correct", "test1", user.getUsername());
        }

        //username already taken
        {
            User user = new User("test1", "test1", "test1");
            try{
                userRepository.save(user);
                Assert.fail();
            } catch (ConstraintViolationException e) {


            }
        }

    }

    @Test
    public void testFindByEmail() {
        //user in db
        User user = userRepository.findByEmail("test");
        assertNotNull("User shouldn't be null", user);
        assertEquals("Username is not correct", "test", user.getUsername());

        //user not in db
        User user1 = userRepository.findByEmail("not there");
        assertNull("User should be null", user1);

    }
}

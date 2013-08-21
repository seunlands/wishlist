package org.landocore.wishlist.userManagement.repository;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.landocore.wishlist.common.config.TestDataConfig;
import org.landocore.wishlist.userManagement.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestDataConfig.class, loader = AnnotationConfigContextLoader.class)
public class UserRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests{

    @Autowired
    private UserRepository userRepository;

    @Before
    public void prepare(){
        //on ajoute des user dans la base
        {
            User user = new User("test", "test", "test");
            userRepository.saveOrUpdate(user);
        }
    }

    @Test
    public void testFindByLogin(){
        //user in db
        User user = userRepository.findByLogin("test");
        assertNotNull("User shouldn't be null", user);
        assertEquals("Username is not correct", "test", user.getUsername());

        //user not in db
        User user1 = userRepository.findByLogin("not there");
        assertNull("User should be null", user1);
    }

    @Test
    public void testSave(){
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
}

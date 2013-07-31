package org.landocore.wishlist.repositories.login;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.Transaction;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 28/07/13
 * Time: 16:22
 * JUnit for user repo
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {org.landocore.wishlist.repositories.DataTestConfig.class})
public class UserRepositoryTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    protected UserRepository userRepository;

    @Test
    public void test(){
        userRepository.findById(1L);
    }


}

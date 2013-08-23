package org.landocore.wishlist.common.repository;

import org.junit.runner.RunWith;
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
@ContextConfiguration(classes = DataConfigTests.class, loader = AnnotationConfigContextLoader.class)
public abstract class AbstractRepositoryTesting extends AbstractTransactionalJUnit4SpringContextTests {

}

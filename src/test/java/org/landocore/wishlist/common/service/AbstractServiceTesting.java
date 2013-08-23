package org.landocore.wishlist.common.service;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * Created with IntelliJ IDEA.
 * User: LANDSBERG-S
 * Date: 21/08/13
 * Time: 11:54
 * Unit test for service unit tests.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceConfigTests.class, loader = AnnotationConfigContextLoader.class)
public abstract class AbstractServiceTesting extends AbstractJUnit4SpringContextTests {

}

package org.landocore.wishlist.web;

import org.junit.Test;
import org.landocore.wishlist.login.web.LoginLogoutController;
import org.springframework.ui.ModelMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: LANDSBERG-S
 * Date: 25/08/13
 * Time: 17:59
 * Unit tests for loginlogoutcontroller class.
 */
public class LoginLogoutControllerTest {

    private LoginLogoutController loginLogoutController = new LoginLogoutController();

    @Test
    public void testGetLoginPage() {


        //no error
        {
            ModelMap model = new ModelMap();
            String returnedView = loginLogoutController.getLoginPage(false, model);
            assertNotNull("returnedView shouldn't be NULL", returnedView);
            assertEquals("returnedView should be /loginpage", "/loginpage", returnedView);
            assertEquals("error in ModelMap should be empty", "", model.get("error"));
        }
        //with error
        {
            ModelMap model = new ModelMap();
            String returnedView = loginLogoutController.getLoginPage(true, model);
            assertNotNull("returnedView shouldn't be NULL", returnedView);
            assertEquals("returnedView should be /loginpage", "/loginpage", returnedView);
            assertEquals("error in ModelMap shouldn't be empty", "You have enter invalid credentials", model.get("error"));
        }


    }


    @Test
    public void testGetDeniedPage() {
        String returnedView = loginLogoutController.getDeniedPage();
        assertNotNull("returnedView shouldn't be NULL", returnedView);
        assertEquals("returnedView should be /deniedpage", "/deniedpage", returnedView);
    }
}

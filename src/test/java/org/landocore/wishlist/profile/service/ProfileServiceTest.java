package org.landocore.wishlist.profile.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.landocore.wishlist.common.enums.EnumAuthority;
import org.landocore.wishlist.common.exception.IncompleteUserException;
import org.landocore.wishlist.profile.domain.Profile;
import org.landocore.wishlist.profile.service.internal.ProfileServiceImpl;

/**
 * unit test for profile service.
 * @author LANDSBERG-S
 *
 */
public class ProfileServiceTest {
	
	private ProfileService profileService;
	
	@Before
	public void prepare() {
		profileService = new ProfileServiceImpl();
	}
	
	
	@Test
	public void testCreateProfile() throws Exception {
		
		Profile profile = profileService.createProfile("userName", "rawPassword", "email");
		assertNotNull("profile shouldn't be NULL", profile);
		assertNotNull("User shouldn't be NULL", profile.getUser());
		assertEquals("Username should be userName", "userName", profile.getUser().getUsername());
		assertEquals("password should be rawPassword", "rawPassword", profile.getUser().getPassword());
		assertEquals("email should be email", "email", profile.getUser().getEmail());
		assertFalse("account should be disabled", profile.getUser().isEnabled());
		assertEquals("account authority shouldn't be NULL", profile.getUser().getListAuthorities());
		assertEquals("size of authority should be 1", 1, profile.getUser().getListAuthorities().size());
		assertEquals("user should have authority ROLE_USER", EnumAuthority.ROLE_USER.getIdAuthority(), profile.getUser().getListAuthorities().get(0).getId());
		
	}

}

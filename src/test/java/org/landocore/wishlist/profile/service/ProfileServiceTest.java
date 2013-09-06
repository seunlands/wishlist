package org.landocore.wishlist.profile.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.junit.Before;
import org.junit.Test;
import org.landocore.wishlist.common.enums.EnumAuthority;
import org.landocore.wishlist.common.exception.IncompleteUserException;
import org.landocore.wishlist.common.exception.PasswordStrengthException;
import org.landocore.wishlist.profile.domain.Profile;
import org.landocore.wishlist.profile.repository.ProfileRepository;
import org.landocore.wishlist.profile.service.internal.ProfileServiceImpl;
import org.landocore.wishlist.usermanagement.domain.Authority;
import org.landocore.wishlist.usermanagement.domain.User;
import org.landocore.wishlist.usermanagement.service.UserService;

/**
 * unit test for profile service.
 * @author LANDSBERG-S
 *
 */
public class ProfileServiceTest {
	
	private ProfileServiceImpl profileService;
	
	private Date birthDate;
	
	@Before
	public void prepare() {
		birthDate = new Date();
		profileService = new ProfileServiceImpl();
		
		UserService userService = new UserService() {
			
			@Override
			public User resetPassword(String pUsername) {
				return null;
			}
			
			@Override
			public User getUserByUsername(String pUsername) {
				return null;
			}
			
			@Override
			public User createUser(User pUser) throws IncompleteUserException,
					PasswordStrengthException {
				User user = new User(pUser.getUsername(), pUser.getEmail(), pUser.getPassword());
				List<Authority> lst = new ArrayList<>();
				Authority aut = new Authority();
				aut.setId(EnumAuthority.ROLE_USER.getIdAuthority());
				lst.add(aut);
				user.setListAuthorities(lst);
				user.setEnabled(false);
				return user;
			}
		};
		
		ProfileRepository profileRepository = new ProfileRepository() {
			
			@Override
			public void saveOrUpdate(Profile entity) {
				
				
			}
			
			@Override
			public Profile findById(Long id) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<Profile> findByCriteria(Map<String, Object> criteria) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<Profile> findByCriteria(Criterion criterion) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void delete(Profile entity) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Profile findProfileByUser(User pUser) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		profileService.setUserService(userService);
		profileService.setProfileRepository(profileRepository);
	}
	
	
	@Test
	public void testCreateProfile() throws Exception {
		
		Profile profile = profileService.createProfile("userName", "rawPassword", "email", "name", "lastName", birthDate);
		assertNotNull("profile shouldn't be NULL", profile);
		assertNotNull("User shouldn't be NULL", profile.getUser());
		assertEquals("Username should be userName", "userName", profile.getUser().getUsername());
		assertEquals("password should be rawPassword", "rawPassword", profile.getUser().getPassword());
		assertEquals("email should be email", "email", profile.getUser().getEmail());
		assertFalse("account should be disabled", profile.getUser().isEnabled());
		assertNotNull("account authority shouldn't be NULL", profile.getUser().getListAuthorities());
		assertEquals("size of authority should be 1", 1, profile.getUser().getListAuthorities().size());
		assertEquals("user should have authority ROLE_USER", EnumAuthority.ROLE_USER.getIdAuthority(), profile.getUser().getListAuthorities().get(0).getId());
		assertEquals("lastName should be lastName", "lastName", profile.getLastName());
		assertEquals("name should be name", "name", profile.getName());
		assertEquals("date of birth isnt correct", birthDate, profile.getBirthDate());
		
	}

}

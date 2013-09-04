package org.landocore.wishlist.profile.repository;

import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.landocore.wishlist.common.repository.AbstractRepositoryTesting;
import org.landocore.wishlist.profile.domain.Profile;
import org.landocore.wishlist.usermanagement.domain.User;
import org.landocore.wishlist.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


/**
 * Unit test for Profile repo
 * @author LANDSBERG-S
 */
public class ProfileRepositoryTest extends AbstractRepositoryTesting {
   
    @Autowired
    private ProfileRepository profileRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private Date date;
    private User user;
    private User userNoProfile;
    
    @Before
    public void prepare() {
        
        date = new Date();
        user = new User("test","test","test");
        
        userNoProfile = new User("test1","test1","test1");
        userRepository.save(userNoProfile);
       
        {
            //creation profile and user
            Profile profile = new Profile();
            profile.setBirthDate(this.date);
            profile.setLastName("landsberg");
            profile.setName("seun");
            profile.setUser(user);
            profileRepository.saveOrUpdate(profile);
        }
    }
    
    @Test
    public void testFindProfileByUser() {
        
        //test for a profile and a user existing
        {
            User user = userRepository.findByLogin("test");
        
            Profile profile = profileRepository.findProfileByUser(user);
            assertNotNull("profile shouldn't be NULL", profile);
            assertEquals("profile name is not correct", "seun", profile.getName());
            assertEquals("profile lastname is not correct", "landsberg", profile.getLastName());
            assertEquals("profile birthdate is not correct", this.date, profile.getBirthDate());
            assertEquals("profile user is not correct", user, profile.getUser());
        }
        
        //test with null passed
        {
            Profile profile = profileRepository.findProfileByUser(null);
            assertNull("Profile should NULL", profile);
        }
        
        //test user without a profile
        {
            Profile profile = profileRepository.findProfileByUser(userNoProfile);
            assertNull("Profile should NULL", profile);
        }
    }
    
    
}

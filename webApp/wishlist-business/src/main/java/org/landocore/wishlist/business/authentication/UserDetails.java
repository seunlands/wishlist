package org.landocore.wishlist.business.authentication;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 26/07/13
 * Time: 23:59
 * To change this template use File | Settings | File Templates.
 */
public interface UserDetails extends Serializable{

    Collection<GrantedAuthority> getAuthorities();
    String getPassword();
    String getUsername();
    boolean isAccountNonExpired();
    boolean isAccountNonLocked();
    boolean isCredentialsNonExpired();
    boolean isEnabled();

}

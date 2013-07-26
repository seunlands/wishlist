package org.landocore.wishlist.business.authentication;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 27/07/13
 * Time: 00:02
 * To change this template use File | Settings | File Templates.
 */
public class AuthenticationUserDetails implements UserDetails {

    private Long id;
    private final String login;
    private final String passwordHash;
    private final boolean enabled;
    private HashSet<GrantedAuthority> grantedAuthorities = new HashSet<>();

    public AuthenticationUserDetails(User user){
        this.login = user.getUsername();
        this.passwordHash = user.getPassword();
        this.enabled = user.isEnabled();
        this.grantedAuthorities.addAll(user.getAuthorities());
    }


    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}

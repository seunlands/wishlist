package org.landocore.wishlist.business.authentication;

import org.landocore.wishlist.beans.login.Authority;
import org.landocore.wishlist.beans.login.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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
        this.id = user.getId();
        this.grantedAuthorities.addAll(this.getGrantedAuthoritiesFromUser(user.getListAuthorities()));
    }

    public Long getId(){
        return this.id;
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

    private List<GrantedAuthority> getGrantedAuthoritiesFromUser(List<Authority> lstAuthority){
        if(lstAuthority==null || lstAuthority.isEmpty()){
            return null;
        }
        List<GrantedAuthority> lstGrantedAuthorities = new ArrayList<>();
        for(Authority auth : lstAuthority){
            GrantedAuthority ga = new SimpleGrantedAuthority(auth.getName());
            lstGrantedAuthorities.add(ga);
        }
        return lstGrantedAuthorities;
    }
}

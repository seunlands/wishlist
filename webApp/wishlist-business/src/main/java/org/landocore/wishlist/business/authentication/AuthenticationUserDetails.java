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
 * user authentication details
 */
public class AuthenticationUserDetails implements UserDetails {

    /**
     * id of the user.
     */
    private Long id;

    /**
     * login of the user.
     */
    private final String login;

    /**
     * the password hash of the user.
     */
    private final String passwordHash;

    /**
     * the enabled status of the user.
     */
    private final boolean enabled;

    /**
     * the authorities of the user.
     */
    private HashSet<GrantedAuthority> grantedAuthorities = new HashSet<>();


    /**
     * Constructor of the AuthenticationUseDetails.
     * @param pUser the user from which to create it
     */
    public AuthenticationUserDetails(final User pUser) {
        this.login = pUser.getUsername();
        this.passwordHash = pUser.getPassword();
        this.enabled = pUser.isEnabled();
        this.id = pUser.getId();
        this.grantedAuthorities.addAll(
                this.getGrantedAuthoritiesFromUser(pUser.getListAuthorities()));
    }

    /**
     * getter of the id attribute.
     * @return id of the user
     */
    public final Long getId() {
        return this.id;
    }


    @Override
    public final Collection<GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public final String getPassword() {
        return passwordHash;
    }

    @Override
    public final String getUsername() {
        return login;
    }

    @Override
    public final boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public final boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public final boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public final boolean isEnabled() {
        return enabled;
    }

    /**
     * Transform a list if Authority to a list of GrantedAuhtority.
     * @param lstAuthority list of Authority to change
     * @return list of GrantedAuthority
     */
    private List<GrantedAuthority> getGrantedAuthoritiesFromUser(
            final List<Authority> lstAuthority) {
        if (lstAuthority == null || lstAuthority.isEmpty()) {
            return null;
        }
        List<GrantedAuthority> lstGrantedAuthorities = new ArrayList<>();
        for (Authority auth : lstAuthority) {
            GrantedAuthority ga = new SimpleGrantedAuthority(auth.getName());
            lstGrantedAuthorities.add(ga);
        }
        return lstGrantedAuthorities;
    }
}

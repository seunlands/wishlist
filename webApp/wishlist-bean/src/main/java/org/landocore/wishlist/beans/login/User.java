package org.landocore.wishlist.beans.login;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 22/07/13
 * Time: 21:35
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="wl_user")
public class User implements Serializable{

    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username", nullable = false, length = 30)
    private String username;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "password", nullable = false, length = 256)
    private String password;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @ManyToMany(targetEntity = org.landocore.wishlist.beans.login.Authority.class)
    @JoinTable(name = "wl_tr_user_authority",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
        inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "authority_id")})
    private List<Authority> listAuthorities;


    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Authority> getListAuthorities() {
        return listAuthorities;
    }

    public void setListAuthorities(List<Authority> listAuthorities) {
        this.listAuthorities = listAuthorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!username.equals(user.username)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return 13 * username.hashCode();
    }
}

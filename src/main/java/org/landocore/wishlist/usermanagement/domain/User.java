package org.landocore.wishlist.usermanagement.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 22/07/13
 * Time: 21:35
 * Entity representing the user. Table wl_user.
 */
@Entity
@Table(name = "wl_user")
public class User implements Serializable {

    /**
     * Id of the user.
     */
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    /**
     * username of the user.
     */
    @Column(name = "username", nullable = false, length = 30, unique = true)
    private String username;

    /**
     * email of the user.
     */
    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    /**
     * password of the user.
     */
    @Column(name = "password", nullable = false, length = 256)
    private String password;

    /**
     * status of the account.
     */
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    /**
     * List of authorities of the user.
     */
    @ManyToMany(targetEntity =
            org.landocore.wishlist.usermanagement.domain.Authority.class)
    @JoinTable(name = "wl_tr_user_authority",
        joinColumns = { @JoinColumn(name = "user_id",
                referencedColumnName = "user_id") },
        inverseJoinColumns = { @JoinColumn(name = "authority_id",
                referencedColumnName = "authority_id") })
    private List<Authority> listAuthorities;

    /**
     * @return the Email of the user
     */
    public final String getEmail() {
        return email;
    }

    /**
     * sets the email.
     * @param pEmail email to be set
     */
    public final void setEmail(final String pEmail) {
        this.email = pEmail;
    }


    /**
     * @return id of the user
     */
    public final Long getId() {
        return id;
    }

    /**
     * sets the id of the user.
     * @param pId sets the id of the user
     */
    public final void setId(final Long pId) {
        this.id = pId;
    }

    /**
     * @return  the user's username
     */
    public final String getUsername() {
        return username;
    }

    /**
     * sets the username.
     * @param pUsername username to be set
     */
    public final void setUsername(final String pUsername) {
        this.username = pUsername;
    }

    /**
     *
     * @return the user's password
     */
    public final String getPassword() {
        return password;
    }

    /**
     * sets the password.
     * @param pPassword password to be set
     */
    public final void setPassword(final String pPassword) {
        this.password = pPassword;
    }

    /**
     *
     * @return the user enabled status
     */
    public final boolean isEnabled() {
        return enabled;
    }

    /**
     * sets status of the account.
     * @param pEnabled sets enabled for the user
     */
    public final void setEnabled(final boolean pEnabled) {
        this.enabled = pEnabled;
    }

    /**
     *
     * @return Authorities of the user
     */
    public final List<Authority> getListAuthorities() {
        return listAuthorities;
    }

    /**
     * set the autorities of the user.
     * @param pListAuthorities sets the list of authorities of the users
     */
    public final void setListAuthorities(
            final List<Authority> pListAuthorities) {
        this.listAuthorities = pListAuthorities;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return !username.equals(user.username);
    }

    @Override
    public final int hashCode() {
        int prime = 13;
        return prime * username.hashCode();
    }

    /**
     * Default constructor.
     */
    public User() {
    }

    /**
     * Contructor of the user class with username, password and email.
     * @param pUsername username of the user
     * @param pEmail email of the user
     * @param pPassword email of the password
     */
    public User(final String pUsername,
                final String pEmail, final String pPassword) {
        this.username = pUsername;
        this.email = pEmail;
        this.password = pPassword;
    }
}

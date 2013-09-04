package org.landocore.wishlist.profile.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import org.landocore.wishlist.usermanagement.domain.User;

/**
 * Entity Profile.
 *
 * @author LANDSBERG-S
 *
 */
@Entity
@Table(name = "wl_profile")
public class Profile implements Serializable {

    /**
     * the sUID.
     */
    private static final long serialVersionUID = 20130903L;
    /**
     * the id.
     */
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;
    /**
     * the user.
     */
    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    @Cascade(CascadeType.SAVE_UPDATE)
    private User user;
    /**
     * the birth date.
     */
    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private Date birthDate;
    /**
     * the users first name.
     */
    @Column(name = "name")
    private String name;
    /**
     * the users last name.
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param pId the id to set
     */
    public void setId(final Long pId) {
        this.id = pId;
    }

    /**
     * @return the user
     */
    public final User getUser() {
        return user;
    }

    /**
     * @param pUser the user to set
     */
    public void setUser(final User pUser) {
        this.user = pUser;
    }

    /**
     * @return the birthDate
     */
    public Date getBirthDate() {
        return new Date(birthDate.getTime());
    }

    /**
     * @param pBirthDate the birthDate to set
     */
    public void setBirthDate(final Date pBirthDate) {
        this.birthDate = new Date(pBirthDate.getTime());
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param pName the name to set
     */
    public void setName(final String pName) {
        this.name = pName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param pLastName the lastName to set
     */
    public void setLastName(final String pLastName) {
        this.lastName = pLastName;
    }
}

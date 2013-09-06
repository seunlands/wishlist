package org.landocore.wishlist.usermanagement.web;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * the user account creation form.
 * @author LANDSBERG-S
 *
 */
@ManagedBean
@SessionScoped
public class RegistrationForm implements Serializable {

    /**
     * the UID.
     */
    private static final long serialVersionUID = 20130905L;

    /**
     * the user's name.
     */
    private String name;

    /**
     * the user s lastname.
     */
    private String lastName;

    /**
     * the user s firstname.
     */
    private String userName;

    /**
     * the user s email address.
     */
    private String email;

    /**
     * the user s email address.
     */
    private String emailConfirm;

    /**
     * the user s password.
     */
    private String rawPassword;

    /**
     * the user s password.
     */
    private String rawPasswordConfirm;

    /**
     * the user s birth date.
     */
    private Date birthDate;

    /**
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the lastname.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastname to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the username.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the username to set.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password.
     */
    public String getRawPassword() {
        return rawPassword;
    }

    /**
     * @param rawPassword the password to set.
     */
    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
    }

    /**
     * @return the birthdate.
     */
    public Date getBirthDate() {
    	Date date = null;
    	if(this.birthDate != null) {
    		date = new Date(birthDate.getTime());
    	}
        return date;
    }

    /**
     * @param birthDate the birthdate to set.
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = new Date(birthDate.getTime());
    }

	/**
	 * @return the emailConfirm
	 */
	public String getEmailConfirm() {
		return emailConfirm;
	}

	/**
	 * @param emailConfirm the emailConfirm to set
	 */
	public void setEmailConfirm(String emailConfirm) {
		this.emailConfirm = emailConfirm;
	}

	/**
	 * @return the rawPasswordConfirm
	 */
	public String getRawPasswordConfirm() {
		return rawPasswordConfirm;
	}

	/**
	 * @param rawPasswordConfirm the rawPasswordConfirm to set
	 */
	public void setRawPasswordConfirm(String rawPasswordConfirm) {
		this.rawPasswordConfirm = rawPasswordConfirm;
	}

}

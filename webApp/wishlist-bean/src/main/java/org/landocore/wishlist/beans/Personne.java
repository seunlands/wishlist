package org.landocore.wishlist.beans;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 22/07/13
 * Time: 21:35
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="wl_personne")
public class Personne implements Serializable{

    @Id
    private String email;

    private String nom;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }
}

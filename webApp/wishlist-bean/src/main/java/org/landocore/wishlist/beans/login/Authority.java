package org.landocore.wishlist.beans.login;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 28/07/13
 * Time: 11:15
 * Entity representing the authorities the user can have. table wl_authority
 */


@Entity
@Table(name = "wl_authority")
public class Authority implements Serializable {

    /**
     * id of the entity.
     */
    @Id
    @Column(name = "authority_id")
    private Long id;

    /**
     * the name of the authority.
     */
    @Column(name = "authority_name", length = 10)
    private String name;

    /**
     * returns the ID.
     * @return return the ID of the authority
     */
    public final Long getId() {
        return id;
    }

    /**
     * sets the ID.
     * @param pId id to be set
     */
    public final void setId(final Long pId) {
        this.id = pId;
    }

    /**
     * return the name.
     * @return the name of the authority
     */
    public final String getName() {
        return name;
    }

    /**
     * sets the name.
     * @param pName name to be set
     */
    public final void setName(final String pName) {
        this.name = pName;
    }


    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Authority authority = (Authority) o;
        return name != null && name.equals(authority.name);
    }

    @Override
    public final int hashCode() {
        if (name == null) {
            return 0;
        }
        return name.hashCode();
    }
}

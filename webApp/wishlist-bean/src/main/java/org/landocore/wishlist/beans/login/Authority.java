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
 * To change this template use File | Settings | File Templates.
 */


@Entity
@Table(name = "wl_authority")
public class Authority implements Serializable{

    @Id
    @Column(name = "authority_id")
    private Long id;

    @Column(name="authority_name", length = 10)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

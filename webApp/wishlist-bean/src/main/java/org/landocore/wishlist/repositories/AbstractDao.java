package org.landocore.wishlist.repositories;

import org.hibernate.criterion.Criterion;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 28/07/13
 * Time: 07:39
 * To change this template use File | Settings | File Templates.
 */
public interface AbstractDao<E,I> {

    E findById(I id);

    void saveOrUpdate(E entity);

    void delete(E entity);

    List findByCriteria(Criterion criterion);

    List findByCriteres(Map<String, Object> criteres);
}

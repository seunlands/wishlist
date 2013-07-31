package org.landocore.wishlist.repositories;

import org.hibernate.criterion.Criterion;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 28/07/13
 * Time: 07:39
 * Abstract class for all the repositories.
 * @param <E> entity class for the repository
 * @param <I> class on the primary id
 *
 */
public interface AbstractDao<E, I> {

    /**
     * finds the entity E by its it.
     * @param id
     * @return
     */
    E findById(I id);

    /**
     * save the entity.
     * @param entity
     */
    void saveOrUpdate(E entity);

    /**
     * deletes the entity.
     * @param entity
     */
    void delete(E entity);

    /**
     * finds the entities by the criterion.
     * @param criterion
     * @return
     */
    List findByCriteria(Criterion criterion);

    /**
     * finds the entities by the criteria map.
     * @param criteria
     * @return
     */
    List findByCriteria(Map<String, Object> criteria);
}

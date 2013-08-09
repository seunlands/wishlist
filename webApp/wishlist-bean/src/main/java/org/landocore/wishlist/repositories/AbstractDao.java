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
     * @param id the id of the entity to find
     * @return the entity <E>
     */
    E findById(I id);

    /**
     * save the entity.
     * @param entity entity to be save or updated
     */
    void saveOrUpdate(E entity);

    /**
     * deletes the entity.
     * @param entity entity to be deleted
     */
    void delete(E entity);

    /**
     * finds the entities by the criterion.
     * @param criterion restriction of the entities to be find
     * @return List of entities corresponding to the Criterion
     */
    List findByCriteria(Criterion criterion);

    /**
     * finds the entities by the criteria map.
     * @param criteria map of restrictions to apply
     * @return list<E> of entities corresponding to the criteria supplied
     */
    List findByCriteria(Map<String, Object> criteria);
}

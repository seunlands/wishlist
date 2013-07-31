package org.landocore.wishlist.repositories;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 28/07/13
 * Time: 07:31
 * implementation of the AbstractDao Interface
 * @see org.landocore.wishlist.repositories.AbstractDao
 * @param <E> the entity class of the DAO
 * @param <I> the ID class of the DAO
 */
public class AbstractDaoImpl<E, I extends Serializable>
        implements AbstractDao<E, I> {

    /**
     * The entity of the repository.
     */
    private Class<E> entityClass;

    /**
     * The hibernate sessionFactory.
     */
    private SessionFactory sessionFactory;

    /**
     * The constructor of the class
     * @param pEntityClass
     * @param pSessionFactory
     */
    protected AbstractDaoImpl(Class<E> pEntityClass, SessionFactory pSessionFactory) {
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;
    }

    /**
     *
     * @return returns the hibernate session factory.
     */
    public final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public E findById(I id) {
        return (E) getCurrentSession().get(entityClass, id);
    }

    @Override
    public final void saveOrUpdate(E entity) {
        getCurrentSession().saveOrUpdate(entity);
    }

    @Override
    public final void delete(E entity) {
        getCurrentSession().delete(entity);
    }

    @Override
    public final List findByCriteria(final Criterion criterion) {
        Criteria criteria = getCurrentSession().createCriteria(entityClass);
        criteria.add(criterion);
        return criteria.list();
    }

    @Override
    public final List findByCriteria(final Map<String, Object> criteria) {
        Criterion criterion = this.getCriterionByCriteria(criteria);
        return this.findByCriteria(criterion);
    }


    /**
     *
     * @param criteria
     * @return The corresponding Criterion. Return NULL if criteria is NULL or empty
     */
    private Criterion getCriterionByCriteria(final Map<String, Object> criteria) {
        if (criteria == null || criteria.isEmpty()) {
            return null;
        }
        Conjunction conjunction = Restrictions.conjunction();
        for (Map.Entry<String, Object> entry : criteria.entrySet()) {
            conjunction.add(Restrictions.eq(entry.getKey(), entry.getValue()));
        }

        return conjunction;
    }
}

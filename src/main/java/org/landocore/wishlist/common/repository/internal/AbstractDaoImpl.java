package org.landocore.wishlist.common.repository.internal;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.landocore.wishlist.common.repository.AbstractDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 28/07/13
 * Time: 07:31
 * implementation of the AbstractDao Interface
 * @see org.landocore.wishlist.common.repository.AbstractDao
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
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * The constructor of the class.
     * @param pEntityClass class of the entity for which is the dao
     * @param pSessionFactory hibernate SessionFactory
     */
    protected AbstractDaoImpl(final Class<E> pEntityClass,
                              final SessionFactory pSessionFactory) {
        this.sessionFactory = pSessionFactory;
        this.entityClass = pEntityClass;
    }

    /**
     *
     * @return returns the hibernate session factory.
     */
    public final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public final E findById(final I id) {
        return (E) getCurrentSession().get(entityClass, id);
    }

    @Override
    public final void saveOrUpdate(final E entity) {
        getCurrentSession().saveOrUpdate(entity);
    }

    @Override
    public final void delete(final E entity) {
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
     * @param criteria map of criteria to be transformed to criterion
     * @return The corresponding Criterion.
     * Return NULL if criteria is NULL or empty
     */
    private Criterion getCriterionByCriteria(
            final Map<String, Object> criteria) {
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

package org.landocore.wishlist.repositories;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 28/07/13
 * Time: 07:31
 * To change this template use File | Settings | File Templates.
 */
public class AbstractDaoImpl<E, I extends Serializable> implements AbstractDao<E,I> {

    private Class<E> entityClass;

    public AbstractDaoImpl(Class<E> entityClass, SessionFactory sessionFactory){
        this.entityClass = entityClass;
        this.sessionFactory = sessionFactory;
    }

    private SessionFactory sessionFactory;

    public Session getCurrentSession(){
        return sessionFactory.getCurrentSession();
    }

    @Override
    public E findById(I id){
        return (E) getCurrentSession().get(entityClass, id);
    }

    @Override
    public void saveOrUpdate(E entity){
        getCurrentSession().saveOrUpdate(entity);
    }

    @Override
    public void delete(E entity){
        getCurrentSession().delete(entity);
    }

    @Override
    public List findByCriteria(Criterion criterion){
        Criteria criteria = getCurrentSession().createCriteria(entityClass);
        criteria.add(criterion);
        return criteria.list();
    }

    @Override
    public List findByCriteres(Map<String, Object> criteres){
        Criterion criterion = this.getCriterionByCriteres(criteres);
        return this.findByCriteria(criterion);
    }



    private Criterion getCriterionByCriteres(Map<String, Object> criteres){
        if(criteres == null || criteres.isEmpty()){
            return null;
        }
        Conjunction conjunction = Restrictions.conjunction();
        for(Map.Entry<String, Object> entry : criteres.entrySet()) {
            conjunction.add(Restrictions.eq(entry.getKey(), entry.getValue()));
        }

        return conjunction;
    }
}

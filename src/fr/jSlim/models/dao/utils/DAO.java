package fr.jSlim.models.dao.utils;

import org.hibernate.Session;

public abstract class DAO<T> {
	
	public DAO(){
	}
	
	public void persist(T transientInstance) throws DAOException {
		Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
		try {
			session.getTransaction().begin();
			session.saveOrUpdate(transientInstance);
			session.getTransaction().commit();
		} catch (Exception re) {
			throw new DAOException(re);
		} finally {
			session.close();
		}
	}
	
	

	public void remove(T persistentInstance) {
		Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
		try {
			session.getTransaction().begin();
			session.remove(session.merge(persistentInstance));
			session.getTransaction().commit();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			session.close();
		}
	}
	
	
	
}


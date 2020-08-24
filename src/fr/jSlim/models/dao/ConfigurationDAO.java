package fr.jSlim.models.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;

import fr.jSlim.models.dao.utils.DAO;
import fr.jSlim.models.dao.utils.DAOException;
import fr.jSlim.models.dao.utils.HibernateSessionFactory;
import fr.jSlim.models.grid.Configuration;

public class ConfigurationDAO extends DAO<Configuration>{

	
	public ConfigurationDAO() {
		
		
	}
	
	public List<Configuration> list() throws DAOException {
		Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
		try {
			session.getTransaction().begin();
			TypedQuery<Configuration> query = session.createQuery("SELECT c FROM configuration c ORDER BY c.id", Configuration.class);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			session.close();
		}
	}

	public Configuration findById(int id) {
		Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
		try {
			session.getTransaction().begin();
			Configuration instance = session.find(Configuration.class, id);
			return instance;
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			session.close();
		}
	}
}

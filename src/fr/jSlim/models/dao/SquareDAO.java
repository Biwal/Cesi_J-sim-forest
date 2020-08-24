package fr.jSlim.models.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;

import fr.jSlim.models.cell.Square;
import fr.jSlim.models.cell.SquareImpl;
import fr.jSlim.models.dao.utils.DAO;
import fr.jSlim.models.dao.utils.DAOException;
import fr.jSlim.models.dao.utils.HibernateSessionFactory;
import fr.jSlim.models.grid.Configuration;

public class SquareDAO extends DAO<SquareImpl>{

	
	public SquareDAO() {
		
		
	}
	public ArrayList<Square> findByConfiguration(int id) {
		String hql = "SELECT s FROM square s JOIN s.configuration WHERE s.configuration = :configuration";
		
		Configuration configuration = new Configuration();
		configuration.setId(id);
		Square square = new SquareImpl();
		square.setConfiguration(configuration);
		Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
		try {
			session.getTransaction().begin();
			@SuppressWarnings("unchecked")
			ArrayList<Square> query = (ArrayList<Square>) session.createQuery(hql).setProperties(square).getResultList();
			return query;
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			session.close();
		}
	}
	public List<SquareImpl> list() {
		Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
		try {
			session.getTransaction().begin();
			TypedQuery<SquareImpl> query = session.createQuery("SELECT s FROM square s ORDER BY s.id", SquareImpl.class);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			session.close();
		}
	}

	public SquareImpl findById(int id) {
		Session session = HibernateSessionFactory.getSessionFactory().getCurrentSession();
		try {
			session.getTransaction().begin();
			SquareImpl instance = session.find(SquareImpl.class, id);
			return instance;
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			session.close();
		}
	}
}

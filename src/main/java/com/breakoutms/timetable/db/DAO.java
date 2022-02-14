package com.breakoutms.timetable.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import lombok.extern.log4j.Log4j2;


@Log4j2
public class DAO<T>{

	private Class<T> type;

	public DAO(Class<T> type){
		this.type = type;
	}
	
	public T save(T obj) {
		Transaction tx = null;
		Session session = HibernateHelper.getSession();
		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(obj);
			tx.commit();
		}
		catch(HibernateException ex){
			try {
				if(tx != null){
					tx.rollback();
				}
			} catch (Exception e) {
				log.error("Unable to rollback transaction: "+ e);
			}
			ex.printStackTrace();
			log.error(ex);
			obj = null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex);
			obj = null;
		}
		finally{
			session.close();
		}
		return obj;
	}

	public T load(Serializable id) {
		Session session = HibernateHelper.getSession();
		T obj = null;
		try{
			obj = session.load(type, id);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex);
		}
		finally{
			session.close();
		}

		return obj;
	}

	public T get(Serializable id) {
		Session session = HibernateHelper.getSession();
		T obj = null;
		try{
			obj = session.get(type, id);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex);
		}
		finally{
			session.close();
		}

		return obj;
	}
	
	public List<T> all() {
		Session session = HibernateHelper.getSession();
		List<T> list = new ArrayList<>();
		try {
			StringBuilder hql = new StringBuilder("from ").append(type.getName());
			Query<T> query = session.createQuery(hql.toString(), type);
			list = query.list();
		}
		catch(Exception ex){
			ex.printStackTrace();
			log.error(ex);
		}
		finally{
			session.close();
		}
		return list;
	}

	public Long count() {
		Session session = HibernateHelper.getSession();
		Long size = null;
		try {
			Query<Long> query = session.createQuery("select count(*) from "+type.getName(), 
					Long.class);
			size = query.getSingleResult();
		}
		catch(Exception ex){
			ex.printStackTrace();
			log.error(ex);
		}
		finally{
			session.close();
		}
		return size;
	}
	
	public void delete(Object obj) {
		Session session = HibernateHelper.getSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.delete(obj);
			tx.commit();
		}
		catch(HibernateException ex){
			try {
				if(tx != null){
					tx.rollback();
				}
			} catch (Exception e) {
				log.error("Unable to rollback transaction: "+ e);
			}
			ex.printStackTrace();
			log.error(ex);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex);
		}
		finally{
			session.close();
		}
	}
}

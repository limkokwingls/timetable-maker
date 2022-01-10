package com.breakoutms.timetable.db;

import java.util.HashMap;
import java.util.Map;

import com.breakoutms.timetable.model.beans.Course;
import com.breakoutms.timetable.model.beans.Lecturer;
import com.breakoutms.timetable.model.beans.StudentClass;
import com.breakoutms.timetable.model.beans.Venue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

public class HibernateHelper {

	private static Logger logger = LogManager.getLogger(HibernateHelper.class);

	public static final String USERNAME = "sa";
	public static final String PASSWORD = "";

	private static StandardServiceRegistry registry;
	private static SessionFactory sessionFactory;

	private HibernateHelper() {}

	public static synchronized Session getSession() {
		if(sessionFactory == null) {
			sessionFactory = getSessionFactory();
		}
		return sessionFactory.openSession();
	}

	private static synchronized SessionFactory getSessionFactory() {
		try{
			Map<String, String> settings = new HashMap<>();
			settings.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
			settings.put(Environment.DRIVER, "org.h2.Driver");
			settings.put(Environment.URL, "jdbc:h2:file:~/.timetable/database");
			settings.put(Environment.USER, USERNAME);
			settings.put(Environment.PASS, PASSWORD);

			settings.put(Environment.HBM2DDL_AUTO, "update");
//			settings.put(Environment.SHOW_SQL, "true");

			StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
			registryBuilder.applySettings(settings);
			registry = registryBuilder.build();

			MetadataSources sources = new MetadataSources(registry);
			sources.addAnnotatedClass(Lecturer.class);
			sources.addAnnotatedClass(Course.class);
			sources.addAnnotatedClass(StudentClass.class);
			sources.addAnnotatedClass(Venue.class);

			Metadata metadata = sources.getMetadataBuilder().build();
			return metadata.getSessionFactoryBuilder()
//					.applyInterceptor( new AuditInterceptor() )
					.build();
		}
		catch(Throwable th) {
			logger.fatal("Error creating Hibernate SessionFactory", th);
			th.printStackTrace();
			if (registry != null) {
				StandardServiceRegistryBuilder.destroy(registry);
			}
			throw new ExceptionInInitializerError(th);
		}
	}

	public static void close(Session session) {
		if (session != null) {
			try {
				session.close();
			} catch (HibernateException e) {
				logger.error("Couldn't close Session ", e);
			}
		}
	}
}

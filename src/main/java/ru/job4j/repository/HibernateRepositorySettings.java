package ru.job4j.repository;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import ru.job4j.model.Car;
import ru.job4j.model.User;
import ru.job4j.model.Role;

public class HibernateRepositorySettings {
	private SessionFactory sf;

	public HibernateRepositorySettings() {
		Configuration configuration = new Configuration();
		StandardServiceRegistry registry = null;
		try {
			Properties settings = new Properties();
			settings.put(Environment.DRIVER, "org.postgresql.Driver");
			settings.put(Environment.URL, "jdbc:postgresql://127.0.0.1:5432/tracker");
			settings.put(Environment.USER, "postgres");
			settings.put(Environment.PASS, "123");
			settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
			settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQL10Dialect");
			settings.put(Environment.HBM2DDL_AUTO, "update");
			settings.put(Environment.SHOW_SQL, "true");
			settings.put(Environment.FORMAT_SQL, "true");
			settings.put(Environment.USE_SQL_COMMENTS, "true");
			settings.put(Environment.JDBC_TIME_ZONE, "Africa/Addis_Ababa");
			configuration.setProperties(settings);
			configuration.addAnnotatedClass(Car.class);
			configuration.addAnnotatedClass(Role.class);
			configuration.addAnnotatedClass(User.class);
			registry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();
		} catch (Exception e) {
		}
		sf = configuration.buildSessionFactory(registry);
	}

	public SessionFactory getSessionFactory() {
		return sf;
	}
}

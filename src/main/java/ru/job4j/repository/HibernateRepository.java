package ru.job4j.repository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class HibernateRepository {
	private SessionFactory sf;

	public HibernateRepository(SessionFactory sf) {
		this.sf = sf;
	}

	private <T> T tx(final Function<Session, T> command) {
		final Session session = sf.openSession();
		final Transaction tx = session.beginTransaction();
		try {
			T rsl = command.apply(session);
			tx.commit();
			return rsl;
		} catch (final Exception e) {
			session.getTransaction().rollback();
			return null;
		} finally {
			session.close();
		}
	}

	public <T> T add(T model) {
		return (T) this.tx(session -> session.save(model));
	}

	public <T> boolean replace(String id, T model) {
		return this.tx(session -> {
			T res = (T) session.find(model.getClass(), Integer.valueOf(id));
			Method[] methods = model.getClass().getMethods();
			for (Method method : methods) {
				int startIndex = -1;
				if (!method.getName().equals("getId") && !method.getName().equals("getClass")
						&& !method.getName().equals("getUser") && !method.getName().equals("getPhotoIds")
						&& !method.getName().equals("of") && !method.getName().equals("getSell")) {
					if (method.getName().indexOf("get") == 0) {
						startIndex = 3;
					} else if (method.getName().indexOf("is") == 0) {
						startIndex = 2;
					}
					if (startIndex != -1) {
						try {
							Method resMethod;
							Field resField;
							resField = res.getClass()
									.getDeclaredField(method.getName().substring(startIndex).toLowerCase());
							resMethod = res.getClass().getMethod("set" + method.getName().substring(startIndex),
									resField.getType());
							resMethod.invoke(res, method.invoke(model, null));
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
								| NoSuchMethodException | SecurityException | NoSuchFieldException e) {
							e.printStackTrace();
						}
					}
				}
			}
			session.update(res);
			return Boolean.TRUE;
		});

	}

	public <T> boolean delete(Class<T> cl, String id) {
		return this.tx(session -> {
			session.createQuery("DELETE FROM " + cl.getName() + " u WHERE u.id = " + id);
			return true;
		});
	}

	public <T> List<T> findAll(Class<T> cl) {
		return this.tx(session -> session.createQuery("SELECT u FROM " + cl.getName() + " u").getResultList());
	}

	public <T> T findById(Class<T> cl, String id) {
		return this.tx(session -> session.get(cl, Integer.valueOf(id)));
	}

	public <T> List<T> findByName(Class<T> cl, String key) {
		return this.tx(session -> session.createQuery("SELECT u FROM " + cl.getName() + " u WHERE u.name = :name")
				.setParameter("name", key).getResultList());
	}

	public <T> T findByEmail(Class<T> cl, String key) {
		return (T) this.tx(session -> session.createQuery("SELECT u FROM " + cl.getName() + " u WHERE u.email = :email")
				.setParameter("email", key).getSingleResult());
	}

	public <T> List<T> findByUserId(Class<T> cl, int key) {
		return this.tx(session -> session.createQuery("SELECT u FROM " + cl.getName() + " u WHERE u.user.id = :id")
				.setParameter("id", key).getResultList());
	}

	public <T> void setSell(Class<T> cl, String key) {
		System.err.println(" -- 11  key = " + key);
		this.tx(session -> session.createQuery("UPDATE " + cl.getName() + " u SET u.sell = true WHERE u.id = :id")
				.setParameter("id", Integer.valueOf(key)).executeUpdate());
	}

	public <T> void setNotSell(Class<T> cl, String key) {
		System.err.println(" -- 11  key = " + key);
		this.tx(session -> session.createQuery("UPDATE " + cl.getName() + " u SET u.sell = false WHERE u.id = :id")
					.setParameter("id", Integer.valueOf(key)).executeUpdate());
	}
}

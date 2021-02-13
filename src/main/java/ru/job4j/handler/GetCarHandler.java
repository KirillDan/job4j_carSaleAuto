package ru.job4j.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;

import ru.job4j.model.Car;
import ru.job4j.repository.HibernateRepository;
import ru.job4j.repository.HibernateRepositorySettings;

public class GetCarHandler {
	private final HibernateRepository repository;
	private final Jsonb jsonb;
		
	public GetCarHandler(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		if (context.getAttribute("sessionFactory") == null) {
			context.setAttribute("sessionFactory", new HibernateRepositorySettings().getSessionFactory());
		}	
		this.repository = new HibernateRepository((SessionFactory) context.getAttribute("sessionFactory"));
		jsonb = JsonbBuilder.create();
	}
	
	public void findAll(PrintWriter w, String action, HttpServletRequest req, HttpServletResponse resp) {
		List<Car> cars = this.repository.findAll(Car.class);
		String result = jsonb.toJson(cars);
		w.println(result);
		return;
	}
	
	public void findById(PrintWriter w, String action, HttpServletRequest req, HttpServletResponse resp) {
		Car car = this.repository.findById(Car.class, action.substring(1));
		w.println(jsonb.toJson(car));
		return;
	}
	
	public void findByUserId(PrintWriter w, String action, HttpServletRequest req, HttpServletResponse resp) {
		List<Car> cars = this.repository.findByUserId(Car.class, Integer.valueOf(action.substring(5)));
		w.println(jsonb.toJson(cars));
		return;
	}
	
	public void setSell(PrintWriter w, String action, HttpServletRequest req, HttpServletResponse resp) {
		this.repository.setSell(Car.class, action.substring(8));
		try {
			resp.sendRedirect(req.getContextPath() + "/index");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setActl(PrintWriter w, String action, HttpServletRequest req, HttpServletResponse resp) {
		this.repository.setNotSell(Car.class, action.substring(8));
		try {
			resp.sendRedirect(req.getContextPath() + "/index");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

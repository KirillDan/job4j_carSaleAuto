package ru.job4j.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;

import ru.job4j.jsonDto.CarDto;
import ru.job4j.model.Car;
import ru.job4j.model.User;
import ru.job4j.repository.HibernateRepository;
import ru.job4j.repository.HibernateRepositorySettings;

public class PostCarHandler {
	private HibernateRepositorySettings settings;
	private final HibernateRepository repository;
	private final Jsonb jsonb;

	public PostCarHandler(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		if (context.getAttribute("sessionFactory") == null) {
			context.setAttribute("sessionFactory", new HibernateRepositorySettings().getSessionFactory());
		}	
		this.repository = new HibernateRepository((SessionFactory) context.getAttribute("sessionFactory"));
		jsonb = JsonbBuilder.create();
	}

	public void save(PrintWriter w, String action, HttpServletRequest req, HttpServletResponse resp) {
		String requestBody = null;
		try {
			requestBody = req.getReader().readLine();
			System.out.println("(1) " + requestBody);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Car car = jsonb.fromJson(requestBody, Car.class);
		User user = repository.findById(User.class, String.valueOf(car.getUser().getId()));

		try {
			Car willSaveCar = Car.of(car.getMark(), car.getModel(), car.getLitre(),
					car.getHorsepower(), car.getGearbox(), car.getDrive(), car.getBody(), car.getColor(), car.getYear(),
					car.getMileage(), car.getCost(), repository.findById(User.class, String.valueOf(car.getUser().getId())));
			willSaveCar.setPhotoIds(car.getPhotoIds());
			Car savedCar = this.repository.add(willSaveCar);
//			w.println(jsonb.toJson(savedItem));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(PrintWriter w, String action, HttpServletRequest req, HttpServletResponse resp) {
		this.repository.delete(Car.class, action.substring(1));
		w.println("Delete with Id = " + action.substring(1));
	}

	public void change(PrintWriter w, String action, HttpServletRequest req, HttpServletResponse resp) {
		String requestBody = null;
		try {
			requestBody = req.getReader().readLine();
			System.out.println("(1) " + requestBody);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		CarDto car = jsonb.fromJson(requestBody, CarDto.class);

		try {
			Car willChangeCar = Car.of(car.getId(), car.getMark(), car.getModel(), car.getLitre(),
					car.getHorsepower(), car.getGearbox(), car.getDrive(), car.getBody(), car.getColor(), car.getYear(),
					car.getMileage(), car.getCost());
			this.repository.replace(String.valueOf(willChangeCar.getId()), willChangeCar);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

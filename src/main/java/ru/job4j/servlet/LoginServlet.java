package ru.job4j.servlet;

import java.io.IOException;
import java.util.Date;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ru.job4j.auth.Key;
import ru.job4j.model.User;
import ru.job4j.repository.HibernateRepository;
import ru.job4j.repository.HibernateRepositorySettings;

@WebServlet(urlPatterns = {"/auth.do"})
public class LoginServlet extends HttpServlet {
	private HibernateRepository repository;
	private Jsonb jsonb;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		if (context.getAttribute("sessionFactory") == null) {
			context.setAttribute("sessionFactory", new HibernateRepositorySettings().getSessionFactory());
		}	
		this.repository = new HibernateRepository((SessionFactory) context.getAttribute("sessionFactory"));
		jsonb = JsonbBuilder.create();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {	
		req.getRequestDispatcher("jsp/login.html").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestBody = req.getReader().readLine();
		User requestUser = jsonb.fromJson(requestBody, User.class);		
		String email = requestUser.getEmail();
		String password = requestUser.getPassword();
		if (!email.isEmpty() && !password.isEmpty()) {
			User admin = this.repository.findByEmail(User.class, email);
			if (admin == null) {
				req.setAttribute("error", "Не верный email");
				req.getRequestDispatcher("jsp/login.html").forward(req, resp);
			} else if (!admin.getPassword().equals(password)) {
				req.setAttribute("error", "Не верный пароль");
				req.getRequestDispatcher("jsp/login.html").forward(req, resp);
			} else {
				resp.setContentType("text/plain");
		        resp.setCharacterEncoding("UTF-8");
		        resp.setHeader("Access-Control-Allow-Origin", "*");
				
				JwtBuilder jwtBuilder = Jwts.builder();
				jwtBuilder.setSubject(String.valueOf(admin.getId()));
	            jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + 864000000));
	            jwtBuilder.signWith(SignatureAlgorithm.HS512, Key.secretKey);
	            String token = jwtBuilder.compact();

	            resp.setHeader("authorization", "Bearer " + token);
			}
		} else {
			req.setAttribute("error", "Не верный email или пароль");
			req.getRequestDispatcher("jsp/login.html").forward(req, resp);
		}
	}
}

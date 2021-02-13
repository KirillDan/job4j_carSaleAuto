package ru.job4j.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.job4j.handler.GetCarHandler;
import ru.job4j.handler.PostCarHandler;
import ru.job4j.router.Router;


@WebServlet("/advertRepository/*")
public class AdvertRestRepository extends HttpServlet {

	private final static String URI = "carSalesArea/advertRepository/";
	private final static String URL_EMPTY = "";
	private final static String URL_CHANGE = "change";
	private final static String URL_WITH_ID = "\\d+";
	private final static String URL_WITH_USER_ID = "user/\\d+";
	private final static String URL_SET_SELL = "setSell/\\d+";
	private final static String URL_SET_ACTUAL = "setActl/\\d+";	
	
	private ServletConfig config;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		this.config = config;
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Router router = new Router(req, resp, URI);
		GetCarHandler getHandler = new GetCarHandler(this.config);
		PostCarHandler postHandler = new PostCarHandler(this.config);
		
		router.route(URL_EMPTY, "GET").handler(getHandler::findAll);
		router.route(URL_WITH_ID, "GET").handler(getHandler::findById);
		router.route(URL_SET_SELL, "GET").handler(getHandler::setSell);
		router.route(URL_SET_ACTUAL, "GET").handler(getHandler::setActl);
		router.route(URL_WITH_USER_ID, "GET").handler(getHandler::findByUserId);
		router.route(URL_EMPTY, "POST").handler(postHandler::save);
		router.route(URL_WITH_ID, "POST").handler(postHandler::delete);
		router.route(URL_CHANGE, "POST").handler(postHandler::change);
	}
}

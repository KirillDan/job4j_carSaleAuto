package ru.job4j.router;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.job4j.function.TriConsumer;

public class Router {
	private final String CONTEXT_URI;
	private String TARGET_URI;
	private String TARGET_HTTP_METHOD;
	private HttpServletRequest req;
	private HttpServletResponse resp;
	private PrintWriter w;
	private String action;

	public Router(HttpServletRequest req, HttpServletResponse resp, String CONTEXT_URI) {
		this.req = req;
		this.resp = resp;
		this.CONTEXT_URI = CONTEXT_URI;
	}

	public Router route(String TARGET_URI, String TARGET_HTTP_METHOD) throws IOException {
		this.TARGET_URI = TARGET_URI;
		this.TARGET_HTTP_METHOD = TARGET_HTTP_METHOD;
		String uri = req.getRequestURI();
		String startUri = "/" + CONTEXT_URI;
		int firstIndex = uri.indexOf(startUri);
		if (firstIndex == 0) {
			action = uri.substring(firstIndex + startUri.length());
			resp.setContentType("text/plain");
			resp.setCharacterEncoding("UTF-8");
			resp.setHeader("Access-Control-Allow-Origin", "*");
			w = resp.getWriter();
		}
		return this;
	}
	
	public Router handler(TriConsumer<PrintWriter, String, HttpServletRequest, HttpServletResponse> consumer) {
		if (req.getMethod().equalsIgnoreCase(TARGET_HTTP_METHOD)) {
			if (action.matches(TARGET_URI)) {
				consumer.accept(w, action, req, resp);
			}
		}
		return this;
	}
}

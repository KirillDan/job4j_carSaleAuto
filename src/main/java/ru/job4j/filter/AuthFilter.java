package ru.job4j.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import ru.job4j.auth.Key;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest sreq, ServletResponse sresp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) sreq;
		HttpServletResponse resp = (HttpServletResponse) sresp;
		String uri = req.getRequestURI();
		if (uri.endsWith("auth.do") || uri.endsWith("reg.do") || uri.endsWith("insert") || uri.endsWith("index")
				|| uri.endsWith("change") || uri.endsWith("upload/") || uri.endsWith("css")) {
			chain.doFilter(sreq, sresp);
			return;
		}
		if (req.getHeader("authorization") == null || req.getHeader("authorization").isEmpty()) {
			resp.sendRedirect(req.getContextPath() + "/auth.do");
			return;
		}
		JwtParser jws = Jwts.parserBuilder().setSigningKey(Key.secretKey).build();
		Jwt jwt = jws.parse(req.getHeader("authorization").substring("Bearer ".length()));
		Claims claims = (Claims) jwt.getBody();
		Integer dateExpInteger = (Integer) claims.get("exp");
		Long dateExp = new Long(dateExpInteger);
		dateExp *= 1000;
		if (dateExp < System.currentTimeMillis()) {
			resp.sendRedirect(req.getContextPath() + "/auth.do");
			return;
		}
		chain.doFilter(sreq, sresp);
	}

	@Override
	public void destroy() {
	}
}

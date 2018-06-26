package controller;

import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import persistence.DAOFactory;
import persistence.dao.UuidDAO;
import utilities.Error;
import utilities.Utils;

@WebFilter("/session_filter")
public class SessionFilter implements Filter {

    public SessionFilter() {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {

	HttpServletRequest httpReq = (HttpServletRequest) request;
	HttpServletResponse httpResp = (HttpServletResponse) response;
	try {

	    String uri = httpReq.getRequestURI();

	    if (uri.endsWith(".css") || uri.endsWith(".js")) {
		chain.doFilter(request, response);
		return;
	    }

	    User user = (User) httpReq.getSession().getAttribute("user");

	    UuidDAO uuidDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getUuidDAO();

	    if (user == null) {

		String uuid = Utils.getCookieValue(httpReq, "uuid");

		if (uuid != null) {

		    user = uuidDAO.find(uuid);

		    if (user != null) {
			// Viene aggiornata la scadenza del cookie
			httpReq.getSession().setAttribute("user", user);
			Utils.addCookie(httpResp, "uuid", uuid, Utils.COOKIE_AGE);
		    } else {
			// IMPORTANTE! Nel caso in cui ci sia un incongruenza tra il cookie UUID
			// e i cookie salvati sul DB. Il vecchio UUID verrà poi sostituito in
			// automatico.
			Utils.removeCookie(httpResp, "uuid");
		    }

		}

	    } else {
		// session exists, re-validate cookie
		String uuid = Utils.getCookieValue(httpReq, "uuid");

		if (uuid == null) {
		    uuid = UUID.randomUUID().toString();
		    uuidDAO.createUpdate(uuid, user.getEmail());
		    Utils.addCookie(httpResp, "uuid", uuid, Utils.COOKIE_AGE);
		}

	    }

	    boolean fromLogin = uri.toLowerCase().contains("login");
	    boolean fromRegistration = uri.toLowerCase().contains("registration");
	    boolean fromForgot = uri.toLowerCase().contains("forgot-password");
	    boolean fromRecovery = uri.toLowerCase().contains("recovery");
	    boolean fromError = uri.toLowerCase().contains("error");

	    if (user == null && !fromLogin && !fromRegistration && !fromForgot && !fromRecovery && !fromError) {
		httpResp.sendRedirect("login");
	    } else {
		chain.doFilter(httpReq, httpResp);
	    }

	} catch (Exception e) {
	    Error error = Utils.catchException(e);
	    httpReq.getSession().setAttribute("error", error);
	    try {
		httpResp.sendRedirect("error-page");
	    } catch (Exception e1) {
		System.out.println(e1.getMessage());
	    }
	}

    }

    public void init(FilterConfig fConfig) throws ServletException {
    }

}

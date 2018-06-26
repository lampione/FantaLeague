package controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import persistence.PostgresDAOFactory;
import persistence.dao.UuidDAO;
import utilities.Error;
import utilities.Utils;

@WebServlet("/logout")
public class Logout extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Logout() {
	super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

	try {
	    UuidDAO uuidDAO = PostgresDAOFactory.getDAOFactory(PostgresDAOFactory.POSTGRESQL).getUuidDAO();

	    User user = (User) request.getSession().getAttribute("user");

	    uuidDAO.delete(user.getEmail());

	    Utils.removeCookie(response, "uuid");
	    request.getSession(false).invalidate();

	    response.sendRedirect("login");

	} catch (Exception e) {
	
	    Error error = Utils.catchException(e);
	    request.getSession().setAttribute("error", error);
	    try {
		response.sendRedirect("error-page");
	    } catch (Exception e1) {
		System.out.println(e1.getMessage());
	    }

	}

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
	doGet(request, response);
    }

}
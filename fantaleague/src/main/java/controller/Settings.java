package controller;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import persistence.DAOFactory;
import persistence.dao.UserDAO;
import utilities.Error;
import utilities.Utils;

@WebServlet("/settings")
public class Settings extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Settings() {
	super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
	try {
	    request.getRequestDispatcher("/settings.jsp").forward(request, response);
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

	try {
	    if (request.getParameter("p") != null) {

		UserDAO userDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getUserDAO();
		User user = ((User) request.getSession(false).getAttribute("user"));
		String email = user.getEmail();
		String pass = request.getParameter("p");

		userDAO.setPassword(email, pass);
	    } else if (request.getParameter("f") != null) {
		UserDAO userDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getUserDAO();
		User user = ((User) request.getSession(false).getAttribute("user"));
		String email = user.getEmail();
		String firstName = request.getParameter("f");

		userDAO.updateFirstName(email, firstName);
		user.setFirstName(firstName);
		request.getSession().setAttribute("user", user);

		response.getWriter().print(firstName);
	    } else if (request.getParameter("l") != null) {
		UserDAO userDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getUserDAO();
		User user = ((User) request.getSession(false).getAttribute("user"));
		String email = user.getEmail();
		String lastName = request.getParameter("l");

		userDAO.updateLastName(email, lastName);
		user.setLastName(lastName);
		request.getSession().setAttribute("user", user);

		response.getWriter().print(lastName);
	    } else if (request.getParameter("b") != null) {
		String bornField = request.getParameter("b");
		
		System.out.println("DATE: " + bornField);

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

		Long time = dateFormat.parse(bornField).getTime();
		Date born = new Date(time);
		UserDAO userDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getUserDAO();
		User user = ((User) request.getSession(false).getAttribute("user"));
		String email = user.getEmail();
		userDAO.updateBorn(email, born);
		user.setBorn(born);
		
		request.getSession().setAttribute("user", user);

		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(born);

	    } else {

		Error error = new Error();
		error.setCode(10201L);
		error.setMessage("Invalid parameters");
		request.getSession().setAttribute("error", error);
		response.sendRedirect("error-page");
		
	    }
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

}

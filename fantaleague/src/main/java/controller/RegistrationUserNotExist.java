package controller;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import persistence.DAOFactory;
import persistence.dao.UserDAO;
import utilities.Error;
import utilities.Utils;

@WebServlet("/registration-user-not-exist")
public class RegistrationUserNotExist extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public RegistrationUserNotExist() {
	super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

	try {
	    if (request.getParameter("ru") != null && request.getParameter("ru").equals("t")) {
		request.getRequestDispatcher("/registrationUserNotExist.html").forward(request, response);
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

	try {
	    String code = request.getParameter("code");
	    UserDAO userDao = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getUserDAO();
	    String email = userDao.rCodeMatch(code);
	    
	    if (email != "null") {

		String firstName = request.getParameter("first_name");
		String lastName = request.getParameter("last_name");
		String bornField = request.getParameter("born");
		String password = request.getParameter("password");

		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

		Long time = dateFormat.parse(bornField).getTime();
		Date born = new Date(time);

		userDao.updateUserInvite(email, firstName, lastName, born, password);
		response.sendRedirect("login");

	    } else {
		
		Error error = new Error();
		error.setCode(10210L);
		error.setMessage("You cannot perform this request");
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

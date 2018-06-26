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
import persistence.PersistenceException;
import persistence.PostgresDAOFactory;
import persistence.dao.UserDAO;
import persistence.dao.UuidDAO;
import utilities.Error;
import utilities.Utils;

@WebServlet("/registration")
public class Registration extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Registration() {
	super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

	try {
	    request.getRequestDispatcher("/registration.jsp").forward(request, response);

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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	try {

	    PostgresDAOFactory factory = (PostgresDAOFactory) DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
	    UuidDAO uuidDao = factory.getUuidDAO();
	    UserDAO userDao = factory.getUserDAO();

	    User userDelete = (User) request.getSession().getAttribute("user");
	    if (userDelete != null) {
		Utils.removeCookie(response, "uuid");
		uuidDao.delete(userDelete.getEmail());
		request.getSession(false).invalidate();
	    }

	    String firstName = request.getParameter("first_name");
	    String lastName = request.getParameter("last_name");
	    String bornField = request.getParameter("born");
	    String email = request.getParameter("email");
	    String password = request.getParameter("password");

	    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

	    Long time = dateFormat.parse(bornField).getTime();
	    Date born = new Date(time);

	    User user = new User(firstName, lastName, email, born);

	    userDao.create(user, password);

	    request.getSession().setAttribute("success", "true");
	    response.sendRedirect("login");

	} catch (Exception e) {
	    Error error = Utils.catchException(e);
	    
	    try {

		if (e instanceof PersistenceException) {
		    request.setAttribute("error", error);
		    request.getRequestDispatcher("registration.jsp").forward(request, response);
		} else {
		    request.getSession().setAttribute("error", error);
		    response.sendRedirect("error-page");
		}

	    } catch (Exception e1) {
		e1.printStackTrace();
	    }

	}

    }

}

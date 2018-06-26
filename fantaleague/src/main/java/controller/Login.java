package controller;

import java.util.UUID;

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

@WebServlet("/login")
public class Login extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public Login() {
	super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
	try {
	    request.getRequestDispatcher("/login.jsp").forward(request, response);
	    request.getSession().setAttribute("success", null);
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
	    DAOFactory daoFactory = PostgresDAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
	    UserDAO userDao = daoFactory.getUserDAO();
	    UuidDAO uuidDao = daoFactory.getUuidDAO();

	    String email = request.getParameter("email");
	    String password = request.getParameter("password");

	    User user = userDao.validate(email, password);
	    uuidDao.delete(email);

	    String uuid = UUID.randomUUID().toString();
	    uuidDao.createUpdate(uuid, email);
	    Utils.addCookie(response, "uuid", uuid, Utils.COOKIE_AGE);

	    request.getSession().setAttribute("user", user);

	    response.sendRedirect("home");

	} catch (Exception e) {
	    Error error = Utils.catchException(e);

	    try {

		if (e instanceof PersistenceException) {
		    request.setAttribute("error", error);
		    request.getRequestDispatcher("login.jsp").forward(request, response);
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

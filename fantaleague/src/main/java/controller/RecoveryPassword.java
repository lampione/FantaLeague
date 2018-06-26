package controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import persistence.DAOFactory;
import persistence.PostgresDAOFactory;
import persistence.dao.UserDAO;
import utilities.Error;
import utilities.Utils;

@WebServlet("/recovery-pass")
public class RecoveryPassword extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public RecoveryPassword() {
	super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

	try {

	    if (request.getParameter("id") != null) {

		PostgresDAOFactory factory = (PostgresDAOFactory) DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
		UserDAO userDao = factory.getUserDAO();

		String idUrl = request.getParameter("id").toString();

		if (userDao.existIdRecover(idUrl)) {
		    request.getRequestDispatcher("/recovery_pass.html").forward(request, response);
		} else {

		    Error error = new Error();
		    error.setCode(10205L);
		    error.setMessage("Invalid code");
		    request.getSession().setAttribute("error", error);
		    response.sendRedirect("error-page");

		}

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

	    PostgresDAOFactory factory = (PostgresDAOFactory) DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
	    UserDAO userDao = factory.getUserDAO();

	    String id = request.getParameter("id").toString();
	    String password = request.getParameter("pass_confirmation");
	    String email = userDao.findUserEmailByRequestId(id);

	    if (!email.equals(" ")) {
		userDao.setPassword(email, password);
		userDao.deleteFromRequest(id);
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

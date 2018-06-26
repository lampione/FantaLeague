package controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import email.Facade;
import persistence.DAOFactory;
import persistence.PostgresDAOFactory;
import persistence.dao.UserDAO;
import utilities.Error;

@WebServlet("/forgot-password")
public class ForgotPassword extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ForgotPassword() {
	super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
	try {
	    request.getRequestDispatcher("/forgot_password.html").forward(request, response);
	} catch (Exception e) {
	    Error error = utilities.Utils.catchException(e);
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

	    String email = request.getParameter("email").toString();
	    String[] send = email.split(" ");

	    String object = "Reset password";

	    String valueUrl = utilities.Utils.getPassCode(send[0]);

	    String protocol = "http";
	    if (request.getRequestURL().toString().contains("https")) {
		protocol = "https";
	    }

	    String url = protocol + "://" + request.getServerName() + ":" + request.getServerPort()
		    + request.getContextPath() + "/recovery-pass?id=" + valueUrl;

	    String urlFantaLeague = protocol + "://" + request.getServerName() + ":" + request.getServerPort()
		    + request.getContextPath();

	    userDao.createRecoveryPass(valueUrl, send[0]);
	    Facade.sendMessage(send, object, urlFantaLeague, url, valueUrl);
	    response.sendRedirect("recovery-invite");

	} catch (Exception e) {
	    Error error = utilities.Utils.catchException(e);
	    request.getSession().setAttribute("error", error);
	    try {
		response.sendRedirect("error-page");
	    } catch (Exception e1) {
		System.out.println(e1.getMessage());
	    }
	}
    }

}

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

@WebServlet("/recovery-invite")
public class RecoveryInvite extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public RecoveryInvite() {
	super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

	try {
	    request.getRequestDispatcher("/recovery_invite.jsp").forward(request, response);
	    request.getSession().setAttribute("error", null);
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

	    String id = request.getParameter("inputInvite");

	    if (userDao.passCodeMatch(id)) {
		response.sendRedirect("recovery-pass?id=" + id);
	    } else {

		Error error = new Error();
		error.setCode(10205L);
		error.setMessage("Invalid code");
		request.getSession().setAttribute("error", error);
		response.sendRedirect(request.getHeader("referer"));

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

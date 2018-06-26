package controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import persistence.DAOFactory;
import persistence.PersistenceException;
import persistence.PostgresDAOFactory;
import persistence.dao.LeagueDAO;
import utilities.Error;
import utilities.Utils;

@WebServlet("/handle-invite")
public class HandleInvite extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public HandleInvite() {
	super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
	try {
	    PostgresDAOFactory factory = (PostgresDAOFactory) DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
	    LeagueDAO leagueDao = factory.getLeagueDAO();

	    String selection = request.getParameter("selection");

	    if (selection != null) {

		if (selection.equals("accept")) {

		    Long leagueId = Long.valueOf(request.getParameter("id"));
		    User user = (User) request.getSession(false).getAttribute("user");
		    String email = user.getEmail();
		    String teamName = request.getParameter("team-name");
		    leagueDao.updateSignedUp(leagueId, email, teamName, "R");

		} else if (selection.equals("refuse")) {

		    Long teamId = Long.valueOf(request.getParameter("tid"));
		    leagueDao.deleteSignedUp(teamId);

		} else {

		    Error error = new Error();
		    error.setCode(10201L);
		    error.setMessage("Invalid parameters");
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

	    response.sendRedirect(request.getHeader("referer"));

	} catch (Exception e) {

	    Error error = Utils.catchException(e);

	    try {

		if (e instanceof PersistenceException) {
		    request.getSession().setAttribute("error", error);
		    response.sendRedirect("home");
		} else {
		    request.getSession().setAttribute("error", error);
		    response.sendRedirect("error-page");
		}

	    } catch (Exception e1) {
		e1.printStackTrace();
	    }

	}
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
	try {
	    doGet(request, response);
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

package controller;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Day;
import model.SerieAMatch;
import model.User;
import persistence.DAOFactory;
import persistence.dao.DayDAO;
import persistence.dao.UserDAO;
import utilities.Error;
import utilities.Invite;
import utilities.Pair;
import utilities.Utils;

@WebServlet("/home")
public class Home extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Home() {
	super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

	try {

	    DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
	    UserDAO userDAO = factory.getUserDAO();

	    DayDAO dayDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getDayDAO();

	    Day day = dayDAO.getCurrentDay();

	    String email = ((User) request.getSession().getAttribute("user")).getEmail();

	    // Leagues
	    List<Pair> adminLeagues = userDAO.findLeagues(email, "A", day.getId());
	    List<Pair> userLeagues = userDAO.findLeagues(email, "R", day.getId());

	    if (!adminLeagues.isEmpty()) {
		request.setAttribute("adminleagues", adminLeagues);
	    }
	    if (!userLeagues.isEmpty()) {
		request.setAttribute("userleagues", userLeagues);
	    }

	    // Invites
	    List<Invite> invites = userDAO.findInvites(email);

	    request.setAttribute("nextMatch", day.getStart().replaceAll(" ", "@"));

	    if (invites != null && invites.size() > 0) {
		request.setAttribute("invites", invites);
	    }

	    List<SerieAMatch> matches = dayDAO.getSerieAMatches(day.getId());
	    request.setAttribute("seriea", matches);

	    request.getRequestDispatcher("home.jsp").forward(request, response);
	    
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

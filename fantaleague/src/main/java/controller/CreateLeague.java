package controller;

import java.sql.Date;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.League;
import model.User;
import persistence.DAOFactory;
import persistence.PostgresDAOFactory;
import persistence.dao.LeagueDAO;
import utilities.Error;
import utilities.Utils;

@WebServlet("/create-league")
public class CreateLeague extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public CreateLeague() {
	super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
	try {
	    request.getRequestDispatcher("/create_league.jsp").forward(request, response);
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
	    LeagueDAO leagueDao = factory.getLeagueDAO();

	    User user = (User) request.getSession(false).getAttribute("user");
	    String email = user.getEmail();

	    String leagueName = request.getParameter("league-name");

	    int initialCredits = Integer.valueOf(request.getParameter("starting-credits"));

	    String checkbox = (request.getParameter("duplicate-players"));

	    boolean duplicatePlayers = (checkbox != null);

	    int numGoalkeepers = Integer.valueOf(request.getParameter("num_goalkeepers"));
	    int numDefenders = Integer.valueOf(request.getParameter("num_defenders"));
	    int numMidfielders = Integer.valueOf(request.getParameter("num_midfielders"));
	    int numForwards = Integer.valueOf(request.getParameter("num_forwards"));

	    int maxTime = Integer.valueOf(request.getParameter("max-time"));

	    String modules = request.getParameter("modules");

	    String teamName = request.getParameter("team-name");

	    Date creationDate = new Date(System.currentTimeMillis());

	    League league = new League(leagueName, creationDate, initialCredits, duplicatePlayers, numGoalkeepers,
		    numDefenders, numMidfielders, numForwards, maxTime, modules);

	    leagueDao.create(league, email, teamName);
	    response.sendRedirect("home");

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

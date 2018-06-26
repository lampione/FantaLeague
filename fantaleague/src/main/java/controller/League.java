package controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Team;
import model.User;
import persistence.DAOFactory;
import persistence.dao.DayDAO;
import persistence.dao.LeagueDAO;
import persistence.dao.TeamDAO;
import persistence.dao.UserDAO;
import utilities.Error;
import utilities.PlayerVote;
import utilities.TeamScore;
import utilities.Utils;

@WebServlet("/league")
public class League extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public League() {
	super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

	try {
	    if (request.getParameter("id") != null && request.getParameter("s") != null) {
		DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
		LeagueDAO leagueDAO = factory.getLeagueDAO();
		TeamDAO teamDAO = factory.getTeamDAO();
		UserDAO userDAO = factory.getUserDAO();
		DayDAO dayDAO = factory.getDayDAO();

		if (request.getParameter("d") != null) {
		    Long dayParam = Long.valueOf(request.getParameter("d"));
		    if (!dayDAO.dayVoteExist(dayParam)) {
			Error error = new Error();
			error.setCode(10221L);
			error.setMessage("Inserted day has not yet been played or is not valid");

			request.getSession().setAttribute("error", error);
			response.sendRedirect("error-page");
			return;
		    }
		}

		Long leagueId = Long.valueOf(request.getParameter("id"));
		String status = request.getParameter("s");
		String email = ((User) request.getSession().getAttribute("user")).getEmail();

		if (userDAO.isStatusUserSession(status, email, leagueId)) {

		    model.League league = leagueDAO.findById(leagueId);

		    List<TeamScore> teamScore = leagueDAO.leagueTable(leagueId);

		    Collections.sort(teamScore, (t1, t2) -> {
			return Double.compare(t2.getScore(), t1.getScore());
		    });

		    Long dayId;
		    Long currentDay = dayDAO.getCurrentDay().getId() - 1;

		    if (request.getParameter("d") != null) {
			dayId = Long.valueOf(request.getParameter("d"));
		    } else {
			dayId = currentDay;
		    }

		    Map<Long, List<PlayerVote>> playerTeamVotes = teamDAO.findTeamPlayerVote(dayId, teamScore);

		    Map<Long, String> teams = new HashMap<>();

		    Team userteam = teamDAO.getTeam(leagueId, email);

		    teamScore.forEach(t -> teams.put(t.getTeamId(), t.getTeamName()));

		    request.setAttribute("league", league);
		    request.setAttribute("status", status);
		    request.setAttribute("teamscore", teamScore);
		    request.setAttribute("day", currentDay);
		    request.setAttribute("daySelected", dayId);
		    request.setAttribute("playerteamvote", playerTeamVotes);
		    request.setAttribute("teams", teams);
		    request.setAttribute("userteam", userteam);

		    request.getRequestDispatcher("league.jsp").forward(request, response);

		    request.getSession().setAttribute("error", null);
		    request.getSession().setAttribute("success", null);
		    
		} else {
		    Error error = new Error();
		    error.setCode(10200L);
		    error.setMessage("You cannot access this league with this credentials");

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
	    e.printStackTrace();
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

	    if (request.getParameter("tN") != null && request.getParameter("tId") != null) {

		TeamDAO teamDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getTeamDAO();
		String tName = request.getParameter("tN");
		Long tId = Long.valueOf(request.getParameter("tId"));

		teamDAO.updateTName(tId, tName);
		response.getWriter().print(tName);

	    } else if (request.getParameter("lN") != null && request.getParameter("lId") != null) {

		LeagueDAO leagueDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getLeagueDAO();
		String lName = request.getParameter("lN");
		Long lId = Long.valueOf(request.getParameter("lId"));

		leagueDAO.updateLName(lName, lId);
		response.getWriter().print(lName);

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

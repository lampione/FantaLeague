package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import model.Player;
import model.User;
import persistence.DAOFactory;
import persistence.dao.LeagueDAO;
import persistence.dao.TeamDAO;
import persistence.dao.UserDAO;
import utilities.Error;
import utilities.UserTeamName;
import utilities.Utils;

@WebServlet("/create-team")
public class CreateTeam extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public CreateTeam() {
	super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

	try {

	    UserDAO userDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getUserDAO();
	    LeagueDAO leagueDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getLeagueDAO();

	    Long leagueId = Long.valueOf(request.getParameter("id"));
	    String email = ((User) request.getSession().getAttribute("user")).getEmail();

	    if (userDAO.isStatusUserSession("A", email, leagueId)) {

		List<UserTeamName> users = userDAO.findUserTeamName(leagueId);

		model.League league = leagueDAO.findById(leagueId);

		request.setAttribute("users", users);
		request.setAttribute("league", league);
		request.getRequestDispatcher("/create_team.jsp").forward(request, response);
	    } else {
		Error error = new Error();
		error.setCode(10220L);
		error.setMessage("You cannot access this page with this credentials");

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

	    TeamDAO teamDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getTeamDAO();

	    JsonParser jsonParser = new JsonParser();

	    Long teamId = Long.valueOf(request.getParameter("save-team-teamid"));

	    JsonArray g = (JsonArray) jsonParser.parse(request.getParameter("g"));
	    JsonArray d = (JsonArray) jsonParser.parse(request.getParameter("d"));
	    JsonArray m = (JsonArray) jsonParser.parse(request.getParameter("m"));
	    JsonArray f = (JsonArray) jsonParser.parse(request.getParameter("f"));

	    List<Player> players = new ArrayList<>();

	    for (JsonElement obj : g) {
		Player p = new Gson().fromJson(obj, Player.class);
		players.add(p);
	    }
	    for (JsonElement obj : d) {
		Player p = new Gson().fromJson(obj, Player.class);
		players.add(p);
	    }
	    for (JsonElement obj : m) {
		Player p = new Gson().fromJson(obj, Player.class);
		players.add(p);
	    }
	    for (JsonElement obj : f) {
		Player p = new Gson().fromJson(obj, Player.class);
		players.add(p);
	    }

	    teamDAO.saveTeam(teamId, players);

	    response.sendRedirect(request.getHeader("referer"));

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

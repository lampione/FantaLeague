package controller;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import model.Day;
import model.Player;
import persistence.DAOFactory;
import persistence.dao.DayDAO;
import persistence.dao.PlayerDAO;
import persistence.dao.TeamDAO;
import utilities.Error;
import utilities.PlayerMatch;
import utilities.Utils;

@WebServlet("/find-players")
public class FindPlayers extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public FindPlayers() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {

		try {
			if (request.getParameter("role") != null) {

				PlayerDAO playerDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getPlayerDAO();

				String role = request.getParameter("role");
				boolean duplicate = Boolean.valueOf(request.getParameter("lduplicate"));
				Long leagueId = Long.valueOf(request.getParameter("lid"));
				Long teamId = Long.valueOf(request.getParameter("tid"));

				List<Player> players = null;
				if (duplicate) {
					players = playerDAO.findPlayersByRole(role);
				} else {
					players = playerDAO.findPlayersByRoleNoDuplicate(role, leagueId, teamId);
				}

				response.setContentType("text/plain");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(new Gson().toJson(players));

			} else if (request.getParameter("tid") != null && request.getParameter("lineup") != null) {

				TeamDAO teamDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getTeamDAO();
				DayDAO dayDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getDayDAO();

				Day day = dayDAO.getCurrentDay();

				List<PlayerMatch> playersStatus = teamDAO.findPlayersMatch(Long.valueOf(request.getParameter("tid")),
						day.getId());

				response.setContentType("text/plain");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(new Gson().toJson(playersStatus));

			} else if (request.getParameter("tid") != null) {

				TeamDAO teamDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getTeamDAO();
				List<Player> players = teamDAO.findPlayers(Long.valueOf(request.getParameter("tid")));

				response.setContentType("text/plain");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(new Gson().toJson(players));

			} else if (request.getParameter("q") != null) {

				String clean = request.getParameter("q").replaceAll("\'", "");
				clean = clean.replaceAll("_", "''");

				PlayerDAO playerDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getPlayerDAO();
				String query = "%" + clean + "%";
				List<Player> players = playerDAO.findPlayerByQuery(query);

				response.setContentType("text/plain");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(new Gson().toJson(players));

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

package controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import model.Day;
import model.Player;
import model.Team;
import model.User;
import persistence.DAOFactory;
import persistence.dao.DayDAO;
import persistence.dao.LeagueDAO;
import persistence.dao.TeamDAO;
import persistence.dao.UserDAO;
import utilities.Error;
import utilities.Utils;

@WebServlet("/line-up")
public class LineUp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LineUp() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {

		try {

			if (request.getParameter("mlineup") != null) {
				DayDAO dayDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getDayDAO();
				int maxLineUp = Integer.valueOf(request.getParameter("mlineup"));

				Day day = dayDAO.getCurrentDay();
				DateFormat format = new SimpleDateFormat("EEEE, MMM dd yyyy HH:mm", Locale.ENGLISH);

				Date matchStartDate = null;
				Date currentDate = new Date(System.currentTimeMillis());

				try {
					matchStartDate = format.parse(day.getStart());
				} catch (ParseException e) {
					e.printStackTrace();
				}

				long difference = matchStartDate.getTime() - currentDate.getTime();
				long minutes = difference / 60000;
				String expired = "false";

				if (minutes <= maxLineUp) {
					expired = "true";
				}

				response.setContentType("text/plain");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(expired);

			} else if (request.getParameter("lid") != null) {

				TeamDAO teamDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getTeamDAO();
				LeagueDAO leagueDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getLeagueDAO();
				UserDAO userDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getUserDAO();
				DayDAO dayDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getDayDAO();

				Long leagueId = Long.valueOf(request.getParameter("lid"));
				String email = ((User) request.getSession().getAttribute("user")).getEmail();

				if (userDAO.isUserInLeague(email, leagueId)) {

					Day day = dayDAO.getCurrentDay();
					DateFormat format = new SimpleDateFormat("EEEE, MMM dd yyyy HH:mm", Locale.ENGLISH);

					Date matchStartDate = null;
					Date currentDate = new Date(System.currentTimeMillis());

					try {
						matchStartDate = format.parse(day.getStart());
					} catch (ParseException e) {
						e.printStackTrace();
					}

					Team team = teamDAO.getTeam(leagueId, email);

					model.League league = leagueDAO.findById(leagueId);
					List<String> modules = Arrays.asList(league.getModules().trim().split(","));

					long difference = matchStartDate.getTime() - currentDate.getTime();
					long minutes = difference / 60000;

					if (minutes <= league.getMaxTimeToLineup()) {
						request.setAttribute("expired", true);
					} else {
						request.setAttribute("expired", false);
					}

					request.setAttribute("t", team);
					request.setAttribute("modules", modules);
					request.setAttribute("maxlineup", league.getMaxTimeToLineup());
					request.getRequestDispatcher("line_up.jsp").forward(request, response);
				} else {
					Error error = new Error();
					error.setCode(10220L);
					error.setMessage("You cannot access this page with this credentials");

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

			TeamDAO teamDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getTeamDAO();
			DayDAO dayDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getDayDAO();
			Day day = dayDAO.getCurrentDay();

			JsonParser jsonParser = new JsonParser();

			Long teamId = Long.valueOf(request.getParameter("t-id"));

			JsonArray sg = (JsonArray) jsonParser.parse(request.getParameter("starters-g"));
			JsonArray sd = (JsonArray) jsonParser.parse(request.getParameter("starters-d"));
			JsonArray sm = (JsonArray) jsonParser.parse(request.getParameter("starters-m"));
			JsonArray sf = (JsonArray) jsonParser.parse(request.getParameter("starters-f"));
			JsonArray b = (JsonArray) jsonParser.parse(request.getParameter("bench"));

			List<Player> playerStarters = new ArrayList<>();
			List<Player> playerBench = new ArrayList<>();

			for (JsonElement obj : sg) {
				Player p = new Gson().fromJson(obj, Player.class);
				playerStarters.add(p);
			}
			for (JsonElement obj : sd) {
				Player p = new Gson().fromJson(obj, Player.class);
				playerStarters.add(p);
			}
			for (JsonElement obj : sm) {
				Player p = new Gson().fromJson(obj, Player.class);
				playerStarters.add(p);
			}
			for (JsonElement obj : sf) {
				Player p = new Gson().fromJson(obj, Player.class);
				playerStarters.add(p);
			}
			for (JsonElement obj : b) {
				Player p = new Gson().fromJson(obj, Player.class);
				playerBench.add(p);
			}

			if (day != null) {
				teamDAO.saveTeamMatch(teamId, day.getId(), playerStarters, playerBench);
				response.sendRedirect(request.getHeader("referer"));
			} else {
				// TODO: handle error
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

package controller;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import persistence.DAOFactory;
import persistence.dao.PlayerDAO;
import utilities.Error;
import utilities.PlayerVote;
import utilities.Utils;

@WebServlet("/player-stats")
public class PlayerStats extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public PlayerStats() {
	super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

	try {
	    if (request.getParameter("pid") != null) {

		PlayerDAO playerDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getPlayerDAO();
		Long pId = Long.valueOf(request.getParameter("pid"));

		if (playerDAO.playerExsist(pId)) {

		    request.setAttribute("player_id", request.getParameter("pid"));
		    request.getRequestDispatcher("player_stats.jsp").forward(request, response);

		} else {
		    Error error = new Error();
		    error.setCode(10201L);
		    error.setMessage("Invalid parameters");
		    request.getSession().setAttribute("error", error);
		    response.sendRedirect("error-page");
		}

	    } else if (request.getParameter("s") != null) {

		PlayerDAO playerDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getPlayerDAO();
		List<PlayerVote> votes = playerDAO.findPlayerById(Long.valueOf(request.getParameter("s")));

		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(new Gson().toJson(votes));

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
	doGet(request, response);
    }

}

package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import persistence.DAOFactory;
import persistence.dao.LeagueDAO;
import persistence.dao.UserDAO;
import utilities.Error;
import utilities.Utils;

@WebServlet("/delete-league")
public class DeleteLeague extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DeleteLeague() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
	try {
	    if (request.getParameter("id") != null) {
	    	UserDAO userDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getUserDAO();
	    	Long leagueId = Long.valueOf((request.getParameter("id")));
	    	String email = ( (User) request.getSession().getAttribute("user")).getEmail();

	    		if( userDAO.isStatusUserSession("A", email, leagueId) ) {
	    			LeagueDAO leagueDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getLeagueDAO();
	    	    	leagueDAO.deleteLeague(leagueId);

	    			response.sendRedirect("home");
	    		} else {
	    			Error error = new Error();
	    			error.setCode(10222L);
	    			error.setMessage("You cannot delete this league");

	    			request.getSession().setAttribute("error", error);
	    			response.sendRedirect("error-page");
	    		}
	    	
	    } else {
	    	Error error = new Error();
			error.setCode(10200L);
			error.setMessage("You cannot access this league with this credentials");

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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}

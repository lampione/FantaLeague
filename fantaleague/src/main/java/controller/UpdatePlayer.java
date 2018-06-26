package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import persistence.DAOFactory;
import persistence.dao.TeamDAO;
import utilities.Error;
import utilities.Utils;

@WebServlet("/update-player")
public class UpdatePlayer extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UpdatePlayer() {
	super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

	try {

	    Long oldId = Long.valueOf(request.getParameter("oid"));
	    Long newId = Long.valueOf(request.getParameter("nid"));
	    Long teamId = Long.valueOf(request.getParameter("tid"));
	    Long credits = Long.valueOf(request.getParameter("c"));

	    if (oldId != null && newId != null && teamId != null && credits != null) {

		TeamDAO teamDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getTeamDAO();
		int success = teamDAO.replacePlayer(oldId, newId, teamId, credits);

		if (success == 1) {
		    response.getWriter().print("200");
		} else {
		    response.getWriter().print("400");
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	doGet(request, response);
    }

}

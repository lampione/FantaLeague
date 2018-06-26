package controller;

import java.util.ArrayList;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import email.Facade;
import model.User;
import persistence.DAOFactory;
import persistence.dao.LeagueDAO;
import persistence.dao.UserDAO;
import utilities.Error;
import utilities.RegistrationUserInvite;
import utilities.Utils;

@WebServlet("/invite-user")
public class InviteUser extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public InviteUser() {
	super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
	try {

	    Error error = new Error();
	    error.setCode(10201L);
	    error.setMessage("Invalid parameters");
	    request.getSession().setAttribute("error", error);
	    response.sendRedirect("error-page");

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

	    User user = (User) request.getSession().getAttribute("user");

	    DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
	    LeagueDAO leagueDAO = factory.getLeagueDAO();

	    // NEW
	    UserDAO userDAO = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL).getUserDAO();

	    String email = request.getParameter("email").toString().trim();
	    String message = request.getParameter("invite-message").toString();

	    String leagueName = request.getParameter("league-name").toString();
	    Long leagueId = Long.valueOf(request.getParameter("id"));

	    String[] emails = email.split(",");
	    ArrayList<RegistrationUserInvite> emailUserNotExist = new ArrayList<>();

	    for (String e : emails) {

		if (user.getEmail().equals(e)) {

		    Error error = new Error();
		    error.setCode(10202L);
		    error.setMessage("You cannot invite yourself!");
		    request.getSession().setAttribute("error", error);
		    response.sendRedirect(request.getHeader("referer"));
		    return;

		}

		if (!isValidEmailAddress(e)) {

		    Error error = new Error();
		    error.setCode(10203L);
		    error.setMessage("Address: '" + e + "' is not a valid email format");
		    request.getSession().setAttribute("error", error);
		    response.sendRedirect(request.getHeader("referer"));
		    return;

		}

		if (leagueDAO.alreadyUserSignedUp(e, leagueId)) {

		    Error error = new Error();
		    error.setCode(10204L);
		    error.setMessage("User '" + e + "' has either been invited or is already in this league");
		    request.getSession().setAttribute("error", error);
		    response.sendRedirect(request.getHeader("referer"));
		    return;

		}

		if (!userDAO.userNotExist(e)) {
		    RegistrationUserInvite rIU = new RegistrationUserInvite();
		    String code = Utils.getRegCode();
		    rIU.setCode(code);
		    rIU.setUser(e);
		    emailUserNotExist.add(rIU);
		}

	    }

	    leagueDAO.invite(leagueId, emails, emailUserNotExist);

	    String firstName = ((User) request.getSession().getAttribute("user")).getFirstName();
	    String lastName = ((User) request.getSession().getAttribute("user")).getLastName();
	    String object = "You've been invited in '" + leagueName + "' league at FantaLeague from" + firstName + " "
		    + lastName + ".";

	    String protocol = "http";
	    if (request.getRequestURL().toString().contains("https")) {
		protocol = "https";
	    }

	    String urlFantaLeague = protocol + "://" + request.getServerName() + ":" + request.getServerPort()
		    + request.getContextPath();

	    String url = protocol + "://" + request.getServerName() + ":" + request.getServerPort()
		    + request.getContextPath() + "/registration-user-not-exist?ru=t";

	    Facade.sendMessageUserNotExists(emails, object, message, emailUserNotExist, url, urlFantaLeague, firstName,
		    lastName, leagueName);

	    request.getSession().setAttribute("success", "true");
	    
	    response.sendRedirect("league?id=" + leagueId + "&s=A");

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

    private static boolean isValidEmailAddress(String email) {
	boolean result = true;
	try {
	    InternetAddress emailAddr = new InternetAddress(email);
	    emailAddr.validate();
	} catch (AddressException ex) {
	    result = false;
	}
	return result;
    }
}

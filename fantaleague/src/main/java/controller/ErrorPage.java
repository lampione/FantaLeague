package controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utilities.Error;
import utilities.Utils;

@WebServlet("/error-page")
public class ErrorPage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ErrorPage() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			Error error = (Error) request.getSession().getAttribute("error");
			if (error != null) {

				request.getRequestDispatcher("error.jsp").forward(request, response);
				request.getSession().setAttribute("error", null);

			} else {
				response.sendRedirect("home");
			}
		} catch (Exception e) {
			// do nothing, ultimate exception
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

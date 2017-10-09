/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.servlet;

import java.io.IOException;
import static sg.edu.sutd.bank.webapp.servlet.ServletPaths.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sg.edu.sutd.bank.webapp.commons.ServiceException;
import sg.edu.sutd.bank.webapp.model.User;
import sg.edu.sutd.bank.webapp.service.UserDAO;
import sg.edu.sutd.bank.webapp.service.UserDAOImpl;


@WebServlet(LOGIN)
public class LoginServlet extends DefaultServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO = new UserDAOImpl();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String userName = req.getParameter("username");
			User user = userDAO.loadUser(userName);
			if (user != null && user.isActive()) {
				req.login(userName, req.getParameter("password"));
				HttpSession session = req.getSession(true);
				session.setAttribute("authenticatedUser", req.getRemoteUser());
				if (req.isUserInRole("client")) {
					resp.sendRedirect(getRedirectPath(CLIENT_DASHBOARD_PAGE));
				} else if (req.isUserInRole("staff")) {
					resp.sendRedirect(getRedirectPath(STAFF_DASHBOARD_PAGE));
				}
				return;
			}
			req.getSession().setAttribute("loginError", "Invalid username/password!");
		} catch(ServletException | ServiceException ex) {
			req.getSession().setAttribute("loginError", ex.getMessage());
		}
		forward(req, resp);
	}
}

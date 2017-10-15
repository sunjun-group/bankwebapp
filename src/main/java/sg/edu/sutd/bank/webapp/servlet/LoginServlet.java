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
import sg.edu.sutd.bank.webapp.model.UserStatus;
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
			if (user != null && (user.getStatus() == UserStatus.APPROVED)) {
				req.login(userName, req.getParameter("password"));
				HttpSession session = req.getSession(true);
				session.setAttribute("authenticatedUser", req.getRemoteUser());
				setUserId(req, user.getId());
				if (req.isUserInRole("client")) {
					redirect(resp, CLIENT_DASHBOARD_PAGE);
				} else if (req.isUserInRole("staff")) {
					redirect(resp, STAFF_DASHBOARD_PAGE);
				}
				return;
			}
			sendError(req, "Invalid username/password!");
		} catch(ServletException | ServiceException ex) {
			sendError(req, ex.getMessage());
		}
		forward(req, resp);
	}

}

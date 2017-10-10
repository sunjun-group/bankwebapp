/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/")
public class DefaultServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (ServletPaths.LOGOUT.equals(req.getServletPath())) {
			logout(req);
			resp.sendRedirect(getRedirectPath(ServletPaths.LOGIN));
		} else if (req.getServletPath().startsWith("/resources/")) {
			forward(req, resp);
		} else {
			forward(req, resp);
		}
	}

	protected void forward(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		forward(req.getServletPath(), req, resp);
	}
	
	protected void sendError(HttpServletRequest req, String msg) {
		req.getSession().setAttribute("req_error", msg);
	}

	protected void sendMsg(HttpServletRequest req, String msg) {
		req.getSession().setAttribute("req_msg", msg);
	}
	
	private void logout(HttpServletRequest req) throws ServletException {
		req.logout();
		HttpSession session = req.getSession(false);
		if (session != null) {
			session.setAttribute("authenticatedUser", null);
		}
	}
	
	protected void forward(String path, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestDispatcher view = req.getRequestDispatcher(getPath(path));
		view.forward(req, resp);
	}
	
	protected void redirect(HttpServletResponse resp, String templage) throws IOException {
		resp.sendRedirect(getRedirectPath(templage));
	}

	protected String getPath(String template) {
		return "WEB-INF/jsp" + template + ".jsp";
	}
	
	protected String getRedirectPath(String template) {
		return "/sutdbank" + template;
	}
}

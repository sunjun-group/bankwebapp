/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.servlet;
import static sg.edu.sutd.bank.webapp.servlet.ServletPaths.STAFF_DASHBOARD_PAGE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sg.edu.sutd.bank.webapp.commons.ServiceException;
import sg.edu.sutd.bank.webapp.model.ClientAccount;
import sg.edu.sutd.bank.webapp.model.User;
import sg.edu.sutd.bank.webapp.model.UserStatus;
import sg.edu.sutd.bank.webapp.service.ClientAccountDAO;
import sg.edu.sutd.bank.webapp.service.ClientAccountDAOImpl;
import sg.edu.sutd.bank.webapp.service.UserDAO;
import sg.edu.sutd.bank.webapp.service.UserDAOImpl;

@WebServlet(STAFF_DASHBOARD_PAGE)
public class StaffDashboardServlet extends DefaultServlet {
	public static final String REGISTRATION_DECISION_ACTION = "registrationDecisionAction";
	public static final String TRANSACTION_DECSION_ACTION = "transactionDecisionAction";
	
	private static final long serialVersionUID = 1L;
	private ClientAccountDAO clientAccountDAO = new ClientAccountDAOImpl();
	private UserDAO userDAO = new UserDAOImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			List<ClientAccount> accountList = clientAccountDAO.loadWaitingList();
			req.getSession().setAttribute("registrationList", accountList);
		} catch (ServiceException e) {
			sendError(req, e.getMessage());
		}
		forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String actionType = req.getParameter("actionType");
		if (REGISTRATION_DECISION_ACTION.endsWith(actionType)) {
			onRegistrationDecisionAction(req, resp);
		} else if (TRANSACTION_DECSION_ACTION.equals(actionType)) {
			onTransactionDecisionAction(req, resp);
		}
	}

	private void onRegistrationDecisionAction(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] decisions = req.getParameterValues("decision");
		String[] userIds = req.getParameterValues("user_id");
		List<User> users = new ArrayList<User>();
		for (int i = 0; i < userIds.length; i++) {
			int userId = Integer.valueOf(userIds[i]);
			Decision decision = Decision.valueOf(decisions[i]);
			if (decision.getStatus() != null) {
				User user = new User();
				user.setId(userId);
				user.setStatus(decision.getStatus());
				users.add(user);
			}
		}
		if (!users.isEmpty()) {
			try {
				userDAO.updateDecision(users);
			} catch (ServiceException e) {
				sendError(req, e.getMessage());
			}
		}
		redirect(resp, STAFF_DASHBOARD_PAGE);
	}

	private void onTransactionDecisionAction(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		forward(req, resp);
	}
	
	private static enum Decision {
		waiting(null), 
		approve(UserStatus.APPROVED), 
		decline(UserStatus.DECLINED);
		
		private UserStatus status;
		private Decision(UserStatus status) {
			this.status = status;
		}

		public UserStatus getStatus() {
			return status;
		}
	}
}

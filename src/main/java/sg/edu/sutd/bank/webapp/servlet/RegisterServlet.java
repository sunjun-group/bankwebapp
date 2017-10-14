/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.servlet;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sg.edu.sutd.bank.webapp.commons.ServiceException;
import sg.edu.sutd.bank.webapp.model.ClientAccount;
import sg.edu.sutd.bank.webapp.model.User;
import sg.edu.sutd.bank.webapp.service.ClientAccountDAO;
import sg.edu.sutd.bank.webapp.service.ClientAccountDAOImpl;
import sg.edu.sutd.bank.webapp.service.EmailService;
import sg.edu.sutd.bank.webapp.service.EmailServiceImp;
import sg.edu.sutd.bank.webapp.service.UserDAO;
import sg.edu.sutd.bank.webapp.service.UserDAOImpl;

/**
 * @author SUTD
 */
@WebServlet("/register")
public class RegisterServlet extends DefaultServlet {
	private static final long serialVersionUID = 1L;
	private ClientAccountDAO clientAccountDAO = new ClientAccountDAOImpl();
	private UserDAO userDAO = new UserDAOImpl();
	private EmailService emailService = new EmailServiceImp();

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		User user = new User();
		user.setUserName(request.getParameter("username"));
		user.setPassword(request.getParameter("password"));

		ClientAccount clientAccount = new ClientAccount();
		clientAccount.setFullName(request.getParameter("fullName"));
		clientAccount.setFin(request.getParameter("fin"));
		clientAccount.setDateOfBirth(Date.valueOf(request.getParameter("dateOfBirth")));
		clientAccount.setOccupation(request.getParameter("occupation"));
		clientAccount.setMobileNumber(request.getParameter("mobileNumber"));
		clientAccount.setAddress(request.getParameter("address"));
		clientAccount.setEmail(request.getParameter("email"));
		clientAccount.setUser(user);
		
		try {
			userDAO.create(user);
			clientAccountDAO.create(clientAccount);
			emailService.sendMail(clientAccount.getEmail(), "SutdBank registration", "Thank you for the registration!");
			sendMsg(request, "You are successfully registered...");
			redirect(response, ServletPaths.WELCOME);
		} catch (ServiceException e) {
			sendError(request, e.getMessage());
			forward(request, response);
		}
	}
}

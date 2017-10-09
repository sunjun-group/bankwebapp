/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sg.edu.sutd.bank.webapp.commons.ServiceException;
import sg.edu.sutd.bank.webapp.model.ClientAccount;
import sg.edu.sutd.bank.webapp.service.ClientAccountDAO;
import sg.edu.sutd.bank.webapp.service.ClientAccountDAOImpl;


@WebServlet("/register")
public class RegisterServlet extends DefaultServlet {
	private static final long serialVersionUID = 1L;
	private ClientAccountDAO dao = new ClientAccountDAOImpl();

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		ClientAccount user = new ClientAccount();
		user.setFullName(request.getParameter("fullName"));
		user.setFin(request.getParameter("fin"));
		user.setDateOfBirth(Date.valueOf(request.getParameter("dateOfBirth")));
		user.setOccupation(request.getParameter("occupation"));
		user.setMobileNumber(request.getParameter("mobileNumber"));
		user.setAddress(request.getParameter("address"));
		user.setEmail(request.getParameter("email"));
		
		try {
			int i = dao.create(user);
			if (i > 0)
				out.print("You are successfully registered...");
		} catch (ServiceException e) {
			System.out.println(e);
		}
		out.close();
	}
}

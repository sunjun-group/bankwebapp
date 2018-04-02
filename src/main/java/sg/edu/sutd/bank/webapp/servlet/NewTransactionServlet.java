/*
 * Copyright 2017 SUTD Licensed under the
	Educational Community License, Version 2.0 (the "License"); you may
	not use this file except in compliance with the License. You may
	obtain a copy of the License at

https://opensource.org/licenses/ECL-2.0

	Unless required by applicable law or agreed to in writing,
	software distributed under the License is distributed on an "AS IS"
	BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
	or implied. See the License for the specific language governing
	permissions and limitations under the License.
 */
package sg.edu.sutd.bank.webapp.servlet;

import static sg.edu.sutd.bank.webapp.servlet.ServletPaths.NEW_TRANSACTION;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sg.edu.sutd.bank.webapp.commons.Constants;

import sg.edu.sutd.bank.webapp.commons.ServiceException;
import sg.edu.sutd.bank.webapp.model.ClientTransaction;
import sg.edu.sutd.bank.webapp.model.TransactionStatus;
import sg.edu.sutd.bank.webapp.model.User;
import static sg.edu.sutd.bank.webapp.service.AbstractDAOImpl.connectDB;
import sg.edu.sutd.bank.webapp.service.ClientAccountDAO;
import sg.edu.sutd.bank.webapp.service.ClientAccountDAOImpl;
import sg.edu.sutd.bank.webapp.service.ClientTransactionDAO;
import sg.edu.sutd.bank.webapp.service.ClientTransactionDAOImpl;

@WebServlet(NEW_TRANSACTION)
public class NewTransactionServlet extends DefaultServlet {

    private static final long serialVersionUID = 1L;
    private ClientTransactionDAO clientTransactionDAO = new ClientTransactionDAOImpl();
    private ClientAccountDAO clientAccountDAO = new ClientAccountDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = null;
        try {
            conn = connectDB();
            conn.setAutoCommit(false);
            
            ClientTransaction clientTransaction = new ClientTransaction();
            User user = new User(getUserId(req));
            clientTransaction.setUser(user);
            clientTransaction.setAmount(new BigDecimal(req.getParameter("amount")));
            clientTransaction.setTransCode(req.getParameter("transcode"));
            clientTransaction.setToAccountNum(Integer.parseInt(req.getParameter("toAccountNum")));
            
            if(clientTransaction.getAmount().compareTo(Constants.TRANSACTION_LIMIT) < 0) {
                clientTransaction.setStatus(TransactionStatus.APPROVED);
            }
            clientTransactionDAO.create(clientTransaction, conn);
            
            if(TransactionStatus.APPROVED.equals(clientTransaction.getStatus())) {
                clientAccountDAO.executeTransaction(clientTransaction, conn);
            }
            
            conn.commit();
            
            redirect(resp, ServletPaths.CLIENT_DASHBOARD_PAGE);
        } catch (ServiceException | SQLException e) {
            try {conn.rollback();} catch(SQLException ex) {}
            sendError(req, e.getMessage());
            forward(req, resp);
        } finally {
            try {
                if(conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ex) {}
        }
    }
}

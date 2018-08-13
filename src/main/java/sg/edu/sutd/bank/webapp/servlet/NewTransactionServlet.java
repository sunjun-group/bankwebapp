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
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sg.edu.sutd.bank.webapp.commons.ServiceException;
import sg.edu.sutd.bank.webapp.model.ClientTransaction;
import sg.edu.sutd.bank.webapp.model.User;
import sg.edu.sutd.bank.webapp.service.ClientAccountDAO;
import sg.edu.sutd.bank.webapp.service.ClientAccountDAOImpl;
import sg.edu.sutd.bank.webapp.service.ClientTransactionDAO;
import sg.edu.sutd.bank.webapp.service.ClientTransactionDAOImpl;
import sg.edu.sutd.bank.webapp.service.TransactionCodesDAO;
import sg.edu.sutd.bank.webapp.service.TransactionCodesDAOImp;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@WebServlet(NEW_TRANSACTION)
public class NewTransactionServlet extends DefaultServlet {
	private static final long serialVersionUID = 1L;
	private ClientTransactionDAO clientTransactionDAO = new ClientTransactionDAOImpl();

	private void doTransaction(String amt, String transcode, String toAccountNum, int senderID) throws ServiceException {
		BigDecimal amount = new BigDecimal(amt);
		int receiverID = Integer.parseInt(toAccountNum);
		ClientAccountDAO cad = new ClientAccountDAOImpl();

		// check if transaction code is valid. if valid, update table
		TransactionCodesDAO tcd = new TransactionCodesDAOImp();
		if (!tcd.isValid(transcode, senderID))
			throw ServiceException.wrap(new IllegalArgumentException("Invalid or used transaction code."));

		// check if sender have enough money
		if (cad.getAmountFrom(senderID).compareTo(amount) < 0)
			throw ServiceException.wrap(new IllegalArgumentException("Your account has insufficient funds for this transaction."));

		// check if valid receiver
		if (!cad.accountExists(receiverID))
			throw ServiceException.wrap(new IllegalArgumentException("There is no such receiver."));

		tcd.updateDB(transcode);

		ClientTransaction clientTransaction = new ClientTransaction();
		User user = new User(senderID);
		clientTransaction.setUser(user);
		clientTransaction.setAmount(amount);
		clientTransaction.setTransCode(transcode);
		clientTransaction.setToAccountNum(toAccountNum);
		clientTransactionDAO.create(clientTransaction);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			int senderID = getUserId(req);
			if (ServletFileUpload.isMultipartContent(req)){
				try {
					List<FileItem> multiparts = new ServletFileUpload(
											new DiskFileItemFactory()).parseRequest(req);

					for (FileItem item : multiparts){
						if (!item.isFormField()){
							InputStream is = item.getInputStream();
							BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

							String line;
							while ((line = br.readLine()) != null) {
								String[] inputs = line.split(" ");
								// Each line consists of 3 space-separated fields,
								// Amount, Transaction Code, and ToAccountNum, e.g.
								// 100.0 fe1234567ab 27
								String amt = inputs[0];
								String transcode = inputs[1];
								String toAccountNum = inputs[2];
								doTransaction(amt, transcode, toAccountNum, senderID);
							}
							br.close();
						}
					}

				} catch (Exception e) {
					sendError(req, e.getMessage());
					forward(req, resp);
				}
			} else {
				String amt = req.getParameter("amount");
				String transcode = req.getParameter("transcode");
				String toAccountNum = req.getParameter("toAccountNum");
				doTransaction(amt, transcode, toAccountNum, senderID);
			}

			redirect(resp, ServletPaths.CLIENT_DASHBOARD_PAGE);
		} catch (ServiceException e) {
			sendError(req, e.getMessage());
			forward(req, resp);
		}
	}
}

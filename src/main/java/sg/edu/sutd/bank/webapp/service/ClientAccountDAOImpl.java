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

package sg.edu.sutd.bank.webapp.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import sg.edu.sutd.bank.webapp.commons.ServiceException;
import sg.edu.sutd.bank.webapp.model.ClientAccount;
import sg.edu.sutd.bank.webapp.model.ClientTransaction;
import sg.edu.sutd.bank.webapp.model.TransactionStatus;

public class ClientAccountDAOImpl extends AbstractDAOImpl implements ClientAccountDAO {


	@Override
	public void create(ClientAccount clientAccount) throws ServiceException {
		Connection conn = connectDB();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = prepareStmt(conn, "INSERT INTO client_account(user_id, amount) VALUES(?,?)");
			int idx = 1;
			ps.setInt(idx++, clientAccount.getUser().getId());
			ps.setBigDecimal(idx++, clientAccount.getAmount());
			executeInsert(clientAccount, ps);
		} catch (SQLException e) {
			throw ServiceException.wrap(e);
		} finally {
			closeDb(conn, ps, rs);
		}
	}

	@Override
	public void update(ClientAccount clientAccount) throws ServiceException {
		Connection conn = connectDB();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = prepareStmt(conn, "UPDATE client_account SET amount = ? WHERE user_id = ?");
			int idx = 1;
			ps.setBigDecimal(idx++, clientAccount.getAmount());
			ps.setInt(idx++, clientAccount.getUser().getId());
			executeUpdate(ps);
		} catch (SQLException e) {
			throw ServiceException.wrap(e);
		} finally {
			closeDb(conn, ps, rs);
		}
	}
	
	/* get transaction details: amount, sender, receiver from client_transaction
	   update sender and receiver alike
	*/
	public void executeTransaction(List<ClientTransaction> transactions) throws ServiceException {	
		Connection conn = connectDB();
		try {
			for (ClientTransaction transaction : transactions) {
				if (transaction.getStatus() == TransactionStatus.APPROVED) {
					PreparedStatement ps = prepareStmt(conn, "SELECT * FROM client_transaction WHERE id = ?");
					ps.setInt(1, transaction.getId()); // transaction-specific id
					ResultSet rs = ps.executeQuery();
					rs.next();
					
					// extracts USERID of sender & receiver
					transaction.setFromAccount(rs.getInt("user_id"));
					transaction.setToAccount(rs.getInt("to_account_num"));
					BigDecimal amount = rs.getBigDecimal("amount");
					
					// Deduct money from sender's account
					PreparedStatement psf = prepareStmt(conn, "UPDATE client_account SET amount = amount - ? WHERE user_id = ? AND amount >= ?");
					psf.setBigDecimal(1, amount);
					psf.setInt(2, transaction.getFromAccount());
					psf.setBigDecimal(3, amount);
					
					// Insert money into recipient's account
					PreparedStatement pst = prepareStmt(conn, "UPDATE client_account SET amount = amount + ? WHERE user_id = ?");
					pst.setBigDecimal(1, amount);
					pst.setInt(2, transaction.getToAccount());
					
					// check if receiver's account is valid
					PreparedStatement checker = prepareStmt(conn, "SELECT count(user_id) FROM client_account WHERE user_id = ?");
					checker.setInt(1, transaction.getToAccount());
					ResultSet checkResult = checker.executeQuery();
					checkResult.next();
					
					// execute if receiver exists, else throws exception
					if (checkResult.getInt("count(user_id)") == 1) {
						int from = psf.executeUpdate();
						int to = pst.executeUpdate();

						if (from != 1 || to != 1)
							throw new ServiceException(new IllegalStateException("From/to account not properly updated."));
					} else {
						throw new ServiceException(new IllegalStateException("Receiver does not exist."));
					}
				}
			}
		} catch (SQLException e) {
			throw ServiceException.wrap(e);
		}
	}

}

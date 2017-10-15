/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sg.edu.sutd.bank.webapp.commons.ServiceException;
import sg.edu.sutd.bank.webapp.model.ClientTransaction;
import sg.edu.sutd.bank.webapp.model.TransactionStatus;
import sg.edu.sutd.bank.webapp.model.User;

public class ClientTransactionDAOImpl extends AbstractDAOImpl implements ClientTransactionDAO {

	@Override
	public void create(ClientTransaction clientTransaction) throws ServiceException {
		Connection conn = connectDB();
		PreparedStatement ps;
		try {
			ps = prepareStmt(conn, "INSERT INTO client_transaction(trans_code, amount, to_account_num, user_id)"
					+ " VALUES(?,?,?,?)");
			int idx = 1;
			ps.setString(idx++, clientTransaction.getTransCode());
			ps.setBigDecimal(idx++, clientTransaction.getAmount());
			ps.setString(idx++, clientTransaction.getToAccountNum());
			ps.setInt(idx++, clientTransaction.getUser().getId());
			executeInsert(clientTransaction, ps);
		} catch (SQLException e) {
			throw ServiceException.wrap(e);
		}
	}

	@Override
	public List<ClientTransaction> load(User user) throws ServiceException {
		Connection conn = connectDB();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(
					"SELECT * FROM client_transaction WHERE user_id = ?");
			int idx = 1;
			ps.setInt(idx++, user.getId());
			rs = ps.executeQuery();
			List<ClientTransaction> transactions = new ArrayList<ClientTransaction>();
			while (rs.next()) {
				ClientTransaction trans = new ClientTransaction();
				trans.setId(rs.getInt("id"));
				trans.setUser(user);
				trans.setAmount(rs.getBigDecimal("amount"));
				trans.setDateTime(rs.getDate("datetime"));
				trans.setStatus(TransactionStatus.of(rs.getString("status")));
				trans.setTransCode(rs.getString("trans_code"));
				trans.setToAccountNum(rs.getString("to_account_num"));
				transactions.add(trans);
			}
			return transactions;
		} catch (SQLException e) {
			throw ServiceException.wrap(e);
		} finally {
			closeDb(conn, ps, rs);
		}
	}

}

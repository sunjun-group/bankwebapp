/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import sg.edu.sutd.bank.webapp.commons.ServiceException;
import sg.edu.sutd.bank.webapp.model.ClientAccount;


public class ClientAccountDAOImpl extends AbstractDAOImpl implements ClientAccountDAO {

	@Override
	public void create(ClientAccount account) throws ServiceException {
		Connection conn = connectDB();
		PreparedStatement ps;
		try {
			ps = prepareStmt(conn, "INSERT INTO CLIENT_ACCOUNT(full_name, fin, date_of_birth, occupation, mobile_number, address, user_id)"
					+ " VALUES(?,?,?,?,?,?,?)");
			int idx = 1;
			ps.setString(idx++, account.getFullName());
			ps.setString(idx++, account.getFin());
			ps.setDate(idx++, account.getDateOfBirth());
			ps.setString(idx++, account.getOccupation());
			ps.setString(idx++, account.getMobileNumber());
			ps.setString(idx++, account.getAddress());
			ps.setInt(idx++, account.getUserId());
			executeUpdate(account, ps);
		} catch (SQLException e) {
			throw ServiceException.wrap(e);
		}
	}
}

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
	public int create(ClientAccount account) throws ServiceException {
		Connection conn = connectDB();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement("insert into client_account values(?,?,?,?)");
			ps.setString(1, account.getFullName());
			int i = ps.executeUpdate();
			return i;
		} catch (SQLException e) {
			throw ServiceException.wrap(e);
		}
	}
}

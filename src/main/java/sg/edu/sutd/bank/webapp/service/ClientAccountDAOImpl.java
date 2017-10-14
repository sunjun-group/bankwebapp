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
import sg.edu.sutd.bank.webapp.model.ClientAccount;
import sg.edu.sutd.bank.webapp.model.User;


public class ClientAccountDAOImpl extends AbstractDAOImpl implements ClientAccountDAO {

	@Override
	public void create(ClientAccount account) throws ServiceException {
		Connection conn = connectDB();
		PreparedStatement ps;
		try {
			ps = prepareStmt(conn, "INSERT INTO CLIENT_ACCOUNT(full_name, fin, date_of_birth, occupation, mobile_number, address, email, user_id)"
					+ " VALUES(?,?,?,?,?,?,?,?)");
			int idx = 1;
			ps.setString(idx++, account.getFullName());
			ps.setString(idx++, account.getFin());
			ps.setDate(idx++, account.getDateOfBirth());
			ps.setString(idx++, account.getOccupation());
			ps.setString(idx++, account.getMobileNumber());
			ps.setString(idx++, account.getAddress());
			ps.setString(idx++, account.getEmail());
			ps.setInt(idx++, account.getUser().getId());
			executeInsert(account, ps);
		} catch (SQLException e) {
			throw ServiceException.wrap(e);
		}
	}

	@Override
	public List<ClientAccount> loadWaitingList() throws ServiceException {
		Connection conn = connectDB();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT acc.*, u.* FROM client_account acc, user u WHERE acc.user_id = u.id and u.status is null");
			rs = ps.executeQuery();
			List<ClientAccount> result = new ArrayList<ClientAccount>();
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("u.id"));
				user.setUserName(rs.getString("u.user_name"));
				user.setStatus(rs.getString("u.status"));
				ClientAccount clientAccount = new ClientAccount();
				clientAccount.setId(rs.getInt("acc.id"));
				clientAccount.setFullName(rs.getString("acc.full_name"));
				clientAccount.setFin(rs.getString("acc.fin"));
				clientAccount.setDateOfBirth(rs.getDate("acc.date_of_birth"));
				clientAccount.setOccupation(rs.getString("acc.occupation"));
				clientAccount.setMobileNumber(rs.getString("acc.mobile_number"));
				clientAccount.setAddress(rs.getString("acc.address"));
				clientAccount.setUser(user);
				result.add(clientAccount);
			}
			return result;
		} catch (SQLException e) {
			throw ServiceException.wrap(e);
		} finally {
			closeDb(conn, ps, rs);
		}
	}
}

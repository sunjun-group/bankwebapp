/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import sg.edu.sutd.bank.webapp.commons.ServiceException;
import sg.edu.sutd.bank.webapp.model.User;

public class UserDAOImpl extends AbstractDAOImpl implements UserDAO {

	@Override
	public User loadUser(String userName) throws ServiceException {
		Connection conn = connectDB();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT ID, USER_NAME, ACTIVE FROM USER WHERE USER_NAME=?");
			ps.setString(1, userName);
			rs = ps.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("ID"));
				user.setUserName(rs.getString("USER_NAME"));
				user.setActive(rs.getBoolean("ACTIVE"));
				return user;
			}
		} catch (SQLException e) {
			throw ServiceException.wrap(e);
		} finally {
			closeDb(conn, ps, rs);
		}
		return null;
	}

	@Override
	public void create(User user) throws ServiceException {
		Connection conn = connectDB();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = prepareStmt(conn, "INSERT INTO USER(user_name, password) VALUES(?,?)");
			ps.setString(1, user.getUserName());
			ps.setString(2, user.getPassword());
			executeUpdate(user, ps);
		} catch (SQLException e) {
			throw ServiceException.wrap(e);
		} finally {
			closeDb(conn, ps, rs);
		}
	}

}

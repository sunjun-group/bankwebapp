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
import sg.edu.sutd.bank.webapp.model.UserRole;

public class UserRoleDAOImpl extends AbstractDAOImpl implements UserRoleDAO {


	@Override
	public void create(UserRole userRole) throws ServiceException {
		Connection conn = connectDB();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = prepareStmt(conn, "INSERT INTO user_role(user_name, role) VALUES(?,?)");
			int idx = 1;
			ps.setString(idx++, userRole.getUser().getUserName());
			ps.setString(idx++, userRole.getRole().name());
			executeInsert(userRole, ps);
		} catch (SQLException e) {
			throw ServiceException.wrap(e);
		} finally {
			closeDb(conn, ps, rs);
		}
	}

}

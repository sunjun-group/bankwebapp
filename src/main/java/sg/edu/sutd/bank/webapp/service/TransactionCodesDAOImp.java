/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import sg.edu.sutd.bank.webapp.commons.ServiceException;

public class TransactionCodesDAOImp extends AbstractDAOImpl implements TransactionCodesDAO {

	@Override
	public void create(List<String> codes, int userId) throws ServiceException {
		Connection conn = connectDB();
		PreparedStatement ps;
		try {
			StringBuilder query = new StringBuilder();
			query.append("INSERT INTO transaction_code(code, user_id, used)"
					+ " VALUES ");
			int idx = 1;
			for (int i = 0; i < codes.size(); i++) {
				query.append("(?, ?, ?)");
				if (i < (codes.size() - 1)) {
					query.append(", ");
				}
			}
			ps = prepareStmt(conn, query.toString());
			for (int i = 0; i < codes.size(); i++) {
				ps.setString(idx++, codes.get(i));
				ps.setInt(idx++, userId);
				ps.setBoolean(idx++, false);
			}
			int rowNum = ps.executeUpdate();
			if (rowNum == 0) {
				throw new SQLException("Update failed, no rows affected!");
			}
		} catch (SQLException e) {
			throw ServiceException.wrap(e);
		}
	}

}

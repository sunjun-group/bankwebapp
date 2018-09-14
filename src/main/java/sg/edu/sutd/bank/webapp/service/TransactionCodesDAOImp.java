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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

	@Override
	public boolean isValid(String transcode, int userId) throws ServiceException {
		Connection conn = connectDB();
		PreparedStatement ps;
		ResultSet rs;
		
		try {
			ps = prepareStmt(conn, "SELECT count(code) FROM transaction_code WHERE code = ? AND user_id = ? AND used = FALSE");
			ps.setString(1, transcode);
			ps.setInt(2, userId);
			rs = ps.executeQuery();
			rs.next();
			
			if (rs.getInt("count(code)") == 0) {
				return false;
			} else {
				return true;
			}

		} catch (SQLException e) {
			throw ServiceException.wrap(e);
		}
	}

	@Override
	public void updateDB(String transcode) throws ServiceException {
		Connection conn = connectDB();
		PreparedStatement ps;
		try {
			ps = prepareStmt(conn, "UPDATE transaction_code SET used = ? WHERE code = ?");
			ps.setInt(1, 1);
			ps.setString(2, transcode);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw ServiceException.wrap(e);
		}
		
	}

}

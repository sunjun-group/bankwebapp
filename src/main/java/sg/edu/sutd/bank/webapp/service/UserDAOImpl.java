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
import sg.edu.sutd.bank.webapp.model.User;

public class UserDAOImpl extends AbstractDAOImpl implements UserDAO {

    @Override
    public User loadUser(String userName) throws ServiceException {
        Connection conn = connectDB();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT id, user_name, status FROM user WHERE user_name=?");
            ps.setString(1, userName);
            rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("user_name"));
                user.setStatus(rs.getString("status"));
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
    public void create(User user, Connection conn) throws ServiceException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareStmt(conn, "INSERT INTO user(user_name, password) VALUES(?,?)");
            int idx = 1;
            ps.setString(idx++, user.getUserName());
            ps.setString(idx++, user.getPassword());
            executeInsert(user, ps);
        } catch (SQLException e) {
            throw ServiceException.wrap(e);
        }
    }

    @Override
    public void updateDecision(User user, Connection conn) throws ServiceException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareStmt(conn, "UPDATE user SET status = ? WHERE id = ?");
            int idx = 1;
            ps.setString(idx++, user.getStatus().name());
            ps.setInt(idx++, user.getId());
            int rowNum = ps.executeUpdate();
            if (rowNum == 0) {
                throw new SQLException("Update failed, no rows affected!");
            }
        } catch (SQLException e) {
            throw ServiceException.wrap(e);
        }
    }

}

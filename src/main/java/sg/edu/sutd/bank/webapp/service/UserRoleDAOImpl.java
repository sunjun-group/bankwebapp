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

import sg.edu.sutd.bank.webapp.commons.ServiceException;
import sg.edu.sutd.bank.webapp.model.Role;
import sg.edu.sutd.bank.webapp.model.User;
import sg.edu.sutd.bank.webapp.model.UserRole;
import static sg.edu.sutd.bank.webapp.service.AbstractDAOImpl.closeDb;
import static sg.edu.sutd.bank.webapp.service.AbstractDAOImpl.connectDB;

public class UserRoleDAOImpl extends AbstractDAOImpl implements UserRoleDAO {

    @Override
    public UserRole loadUserRole(String userName, User user) throws ServiceException {
        Connection conn = connectDB();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT id, role FROM user_role WHERE user_name = ?");
            ps.setString(1, userName);
            rs = ps.executeQuery();
            if (rs.next()) {
                UserRole userRole = new UserRole();
                userRole.setId(rs.getInt("id"));
                userRole.setUser(user);
                userRole.setRole(Role.valueOf(rs.getString("role")));
                return userRole;
            }
        } catch (SQLException e) {
            throw ServiceException.wrap(e);
        } finally {
            closeDb(conn, ps, rs);
        }
        return null;
    }
    
    
    @Override
    public void create(UserRole userRole, Connection conn) throws ServiceException {
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
        }
    }

}

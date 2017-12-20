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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.ResourceBundle;

import sg.edu.sutd.bank.webapp.commons.ServiceException;
import sg.edu.sutd.bank.webapp.model.AbstractIdEntity;


public abstract class AbstractDAOImpl {
	private static Properties connectionProps;
	static {
		connectionProps = new Properties();
		ResourceBundle bundle = ResourceBundle.getBundle("database");
		connectionProps.setProperty("driverClassName", bundle.getString("jdbc.driverClassName"));
		connectionProps.setProperty("url", bundle.getString("jdbc.url"));
		connectionProps.setProperty("username", bundle.getString("jdbc.username"));
		connectionProps.setProperty("password", bundle.getString("jdbc.password"));
	}
	
	protected void executeInsert(AbstractIdEntity entity, PreparedStatement ps) throws SQLException {
		int rowNum = ps.executeUpdate();
		if (rowNum == 0) {
			throw new SQLException("Update failed, no rows affected!");
		}
		try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
			if (generatedKeys.next()) {
				entity.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException("Update failed, no ID obtained.");
			}
		}
	}
	
	protected void executeUpdate(PreparedStatement ps) throws SQLException {
		int rowNum = ps.executeUpdate();
		if (rowNum == 0) {
			throw new SQLException("Update failed, no rows affected!");
		}
	}
	
	protected PreparedStatement prepareStmt(Connection conn, String query) throws SQLException {
		return conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	}
	
	public Connection connectDB() throws ServiceException {
		try {
			Class.forName(connectionProps.getProperty("driverClassName"));
			Connection conn = DriverManager.getConnection(connectionProps.getProperty("url"),
					connectionProps.getProperty("username"), connectionProps.getProperty("password"));
			return conn;
		} catch (ClassNotFoundException e) {
			throw ServiceException.wrap(e);
		} catch (SQLException e) {
			throw ServiceException.wrap(e);
		}
	}
	
	protected void closeDb(Connection connection, Statement statement, ResultSet resultSet) {
		if (resultSet != null)
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if (statement != null)
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if (connection != null)
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
}

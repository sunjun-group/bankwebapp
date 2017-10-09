/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.ResourceBundle;

import sg.edu.sutd.bank.webapp.commons.ServiceException;


public class AbstractDAOImpl {
	private static Properties connectionProps;
	static {
		connectionProps = new Properties();
		ResourceBundle bundle = ResourceBundle.getBundle("database");
		connectionProps.setProperty("driverClassName", bundle.getString("jdbc.driverClassName"));
		connectionProps.setProperty("url", bundle.getString("jdbc.url"));
		connectionProps.setProperty("username", bundle.getString("jdbc.username"));
		connectionProps.setProperty("password", bundle.getString("jdbc.password"));
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

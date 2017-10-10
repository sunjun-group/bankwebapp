/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.model;


public class User extends AbstractIdEntity {
	private String userName;
	private String password;
	private boolean active;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}

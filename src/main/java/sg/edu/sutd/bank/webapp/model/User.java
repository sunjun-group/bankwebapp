/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.model;


public class User {
	private int id;
	private String userName;
	private boolean active;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

}

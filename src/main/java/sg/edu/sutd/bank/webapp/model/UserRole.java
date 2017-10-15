/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.model;

public class UserRole extends AbstractIdEntity {
	private User user;
	private Role role;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}

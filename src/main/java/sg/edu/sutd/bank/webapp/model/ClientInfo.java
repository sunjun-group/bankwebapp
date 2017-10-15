/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.model;

import java.sql.Date;
import java.util.List;


public class ClientInfo extends AbstractIdEntity {
	private String fullName;
	private String fin;
	private Date dateOfBirth;
	private String occupation;
	private String mobileNumber;
	private String address;
	private String email;
	private User user;
	private List<ClientTransaction> transactions;
	private ClientAccount account;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFin() {
		return fin;
	}

	public void setFin(String fin) {
		this.fin = fin;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<ClientTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<ClientTransaction> transactions) {
		this.transactions = transactions;
	}

	public ClientAccount getAccount() {
		return account;
	}

	public void setAccount(ClientAccount account) {
		this.account = account;
	}
	
}

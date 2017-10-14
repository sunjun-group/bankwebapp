/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.service;

import java.util.List;

import sg.edu.sutd.bank.webapp.commons.ServiceException;
import sg.edu.sutd.bank.webapp.model.User;


public interface UserDAO {

	User loadUser(String userName) throws ServiceException;

	void create(User user) throws ServiceException;

	void updateDecision(List<User> users) throws ServiceException;

}

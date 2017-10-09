/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.service;

import sg.edu.sutd.bank.webapp.commons.ServiceException;
import sg.edu.sutd.bank.webapp.model.User;


public interface UserDAO {

	User loadUser(String userName) throws ServiceException;

}

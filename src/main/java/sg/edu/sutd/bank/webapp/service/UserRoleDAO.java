/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.service;

import sg.edu.sutd.bank.webapp.commons.ServiceException;
import sg.edu.sutd.bank.webapp.model.UserRole;


public interface UserRoleDAO {

	void create(UserRole userRole) throws ServiceException;

}

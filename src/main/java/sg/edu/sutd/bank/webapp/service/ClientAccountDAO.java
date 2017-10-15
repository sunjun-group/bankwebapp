/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.service;

import sg.edu.sutd.bank.webapp.commons.ServiceException;
import sg.edu.sutd.bank.webapp.model.ClientAccount;


public interface ClientAccountDAO {

	void create(ClientAccount clientAccount) throws ServiceException;

	void update(ClientAccount clientAccount) throws ServiceException;

}

/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.service;

import java.util.List;

import sg.edu.sutd.bank.webapp.commons.ServiceException;
import sg.edu.sutd.bank.webapp.model.ClientAccount;


public interface ClientAccountDAO {

	void create(ClientAccount account) throws ServiceException;

	List<ClientAccount> loadWaitingList() throws ServiceException;

}

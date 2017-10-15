/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.service;

import java.util.List;

import sg.edu.sutd.bank.webapp.commons.ServiceException;
import sg.edu.sutd.bank.webapp.model.ClientInfo;


public interface ClientInfoDAO {

	void create(ClientInfo account) throws ServiceException;

	List<ClientInfo> loadWaitingList() throws ServiceException;

}

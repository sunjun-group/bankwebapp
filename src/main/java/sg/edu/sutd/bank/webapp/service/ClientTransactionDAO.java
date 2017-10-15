/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.service;

import java.util.List;

import sg.edu.sutd.bank.webapp.commons.ServiceException;
import sg.edu.sutd.bank.webapp.model.ClientTransaction;
import sg.edu.sutd.bank.webapp.model.User;


public interface ClientTransactionDAO {

	void create(ClientTransaction clientTransaction) throws ServiceException;

	List<ClientTransaction> load(User user) throws ServiceException;
	
	List<ClientTransaction> loadWaitingList() throws ServiceException;

	void updateDecision(List<ClientTransaction> transactions) throws ServiceException;
}

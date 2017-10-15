/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.service;

import java.util.List;

import sg.edu.sutd.bank.webapp.commons.ServiceException;

public interface TransactionCodesDAO {

	void create(List<String> codes, int userId) throws ServiceException;

}

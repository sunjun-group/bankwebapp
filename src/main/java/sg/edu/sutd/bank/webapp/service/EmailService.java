/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.service;

import sg.edu.sutd.bank.webapp.commons.ServiceException;

public interface EmailService {

	void sendMail(String toAddr, String subject, String msg) throws ServiceException;

}

/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.servlet;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.List;

public class TransactionCodeGenerator {

	public static List<String> generateCodes(int num) {
		List<String> codes = new ArrayList<String>(num);
		for (int idx = 0; idx < num; ++idx) {
			UID code = new UID();
			codes.add(code.toString());
		}
		return codes;
	}
}
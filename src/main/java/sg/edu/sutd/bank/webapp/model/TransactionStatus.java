/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.model;

public enum TransactionStatus {
	APPROVED,
	DECLINED;

	public static TransactionStatus of(String status) {
		if (status == null) {
			return null;
		}
		return valueOf(status);
	}
}

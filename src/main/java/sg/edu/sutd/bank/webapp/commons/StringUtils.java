/*
 * SUTD (Singapore)
 * 
 */

package sg.edu.sutd.bank.webapp.commons;

import java.util.List;

public class StringUtils {
	private StringUtils() {}
	
	public static String join(List<String> vals, String separator) {
		if (vals == null || vals.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < vals.size(); i++) {
			sb.append(vals.get(i));
			if (i < (vals.size() - 1)) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}
}

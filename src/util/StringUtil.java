package util;

public class StringUtil {
	
	public static boolean isNullOrEmpty(String input) {
		if (input == null || input.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
}

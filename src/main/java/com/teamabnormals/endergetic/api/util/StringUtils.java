package com.teamabnormals.endergetic.api.util;

public final class StringUtils {

	public static String capitaliseFirstLetter(String string) {
		if (string == null) {
			return null;
		} else if (string.length() == 0) {
			return "";
		} else {
			return Character.toTitleCase(string.charAt(0)) + string.substring(1);
		}
	}

}
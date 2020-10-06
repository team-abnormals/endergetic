package com.minecraftabnormals.endergetic.api.util;

public final class StringUtils {

	public static String capitaliseFirstLetter(String string) {
		if (string == null) {
			return null;
		} else if (string.length() == 0) {
			return "";
		} else {
			return new StringBuilder(string.length()).append(Character.toTitleCase(string.charAt(0))).append(string, 1, string.length()).toString();
		}
	}

}
package endergeticexpansion.api.util;

public class StringUtils {
	
	public static String capitaliseFirstLetter(String string) {
		if(string == null) {
			return null;
		} else if(string.length() == 0) {
			return "";
		} else {
			return new StringBuilder(string.length()).append(Character.toTitleCase(string.charAt(0))).append(string, 1, string.length()).toString();
        }
    }
	
	public static String intToRomanNumerals(int number) {
		String m[] = {"", "M", "MM", "MMM"}; 
		String c[] = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"}; 
		String x[] = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"}; 
		String i[] = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
		
		String thousands = m[number / 1000]; 
		String hundereds = c[(number % 1000) / 100]; 
		String tens = x[(number % 100) / 10]; 
		String ones = i[number % 10];
		
		return thousands + hundereds + tens + ones;
	}
	
}
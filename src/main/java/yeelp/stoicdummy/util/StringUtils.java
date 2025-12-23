package yeelp.stoicdummy.util;

public final class StringUtils {

	private StringUtils() {
		throw new RuntimeException("class not to be instantiated!");
	}
	
	public static String convertToRomanNumerals(int a) {
		if(a <= 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for(int i = a; i >= 100; sb.append("C"), i -= 100);
		int rem = a % 100;
		if(rem >= 90) {
			sb.append("XC");
			rem -= 90;
		}
		else if (rem >= 50) {
			sb.append("L");
			rem -= 50;
		}
		else if(rem >= 40) {
			sb.append("XL");
			rem -= 40;
		}
		for(; rem >= 10; sb.append("X"), rem -= 10);
		if(rem >= 9) {
			sb.append("IX");
			rem -= 9;
		}
		else if (rem >= 5) {
			sb.append("V");
			rem -= 5;
		}
		else if (rem >= 4) {
			sb.append("IV");
			rem -= 4;
		}
		for(; rem > 0; sb.append("I"), rem--);
		return sb.toString();
	}
}

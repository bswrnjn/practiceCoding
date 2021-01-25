package business;

public class LongestPalindromicSubstring {

	public static String longestPalSubstr(String str) {

		int maxLength = 1;
		int start = 0;
		int low, high;

		for (int i = 1; i < str.length(); ++i) {
			low = i - 1;
			high = i;
			while (low >= 0 && high < str.length()
					&& str.charAt(low) == str.charAt(high)) {
				if (high - low + 1 > maxLength) {
					start = low;
					maxLength = high - low + 1;
				}
				--low;
				++high;
			}

			low = i - 1;
			high = i + 1;
			while (low >= 0 && high < str.length()
					&& str.charAt(low) == str.charAt(high)) {
				if (high - low + 1 > maxLength) {
					start = low;
					maxLength = high - low + 1;
				}
				--low;
				++high;
			}
		}
		return str.substring(start, start + maxLength);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "forgeeksskeegfor";
		System.out.println(longestPalSubstr(str));
	}

}

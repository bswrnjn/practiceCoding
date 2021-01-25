package business;

import java.util.regex.Pattern;

public class StringReverse {

	public static void main(String args[]) {

		// quick wasy to reverse String in Java - Use StringBuffer
		String word = "HelloWorld";
		String reverse = new StringBuffer(word).reverse().toString();
		System.out.printf(" original String : %s , reversed String %s  %n",
				word, reverse);

		// another quick to reverse String in Java - use StringBuilder
		word = "WakeUp";
		reverse = new StringBuilder(word).reverse().toString();
		System.out.printf(" original String : %s , reversed String %s %n",
				word, reverse);

		// one way to reverse String without using StringBuffer or StringBuilder
		// is writing
		// own utility method
		word = "Band";
		reverse = reverse(word);
		System.out.printf(" original String : %s , reversed String %s %n",
				word, reverse);

		// Reverse word by word
		System.out.println(newReverseMethod("i love java"));
	}

	public static String reverse(String source) {
		if (source == null || source.isEmpty()) {
			return source;
		}
		String reverse = "";
		for (int i = source.length() - 1; i >= 0; i--) {
			reverse = reverse + source.charAt(i);
		}

		return reverse;
	}

	public static String reverseRecursively(String str) {

		// base case to handle one char string and empty string
		if (str.length() < 2) {
			return str;
		}

		return reverseRecursively(str.substring(1)) + str.charAt(0);

	}

	public static String newReverseMethod(String source) {

		Pattern pattern = Pattern.compile("\\s");
		String[] temp = pattern.split(source);
		String result = "";

		for (int i = 0; i < temp.length; i++) {
			if (i == temp.length - 1)
				result = temp[i] + result;
			else
				result = " " + temp[i] + result;
		}
		return result;
	}
}

package business;

import java.util.HashMap;

public class EachCharCountInString {
	static void characterCount(String inputString) {
		HashMap<Character, Integer> charCountMap = new HashMap<Character, Integer>();

		char[] strArray = inputString.toCharArray();

		for (char c : strArray) {
			if (charCountMap.containsKey(c)) {
				charCountMap.put(c, charCountMap.get(c) + 1);
			} else {
				charCountMap.put(c, 1);
			}
		}
		System.out.println(charCountMap);
	}

	public static void main(String[] args) {
		characterCount("JavaJ2EEJavaJSPJ2EE");
	}
}
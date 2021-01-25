package business;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class NoOfNonRepeatedChar {

	public static boolean getFirstNonRepeatedChar(String str) {
		Map<Character, Integer> counts = new LinkedHashMap<Character, Integer>(
				str.length());

		for (char c : str.toCharArray()) {
			counts.put(c, counts.containsKey(c) ? counts.get(c) + 1 : 1);
		}

		for (Entry<Character, Integer> entry : counts.entrySet()) {
			if (entry.getValue() == 1) {
				System.out.println(entry.getKey());
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "aabcd";
		System.out.println(getFirstNonRepeatedChar(str));
		;
	}
}

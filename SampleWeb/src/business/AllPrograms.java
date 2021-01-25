package business;

import java.util.Arrays;

public class AllPrograms {

	public static String addUnderscore(String word) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < word.length(); i++) {
			if (Character.isUpperCase(word.charAt(i))) {
				sb.append("_");
			} else {
				sb.append(word.charAt(i));
			}
		}

		return sb.toString();
	}

	public static int fib(int n) {
		if (n <= 1)
			return n;
		return fib(n - 1) + fib(n - 2);
	}

	public static int distance(String word) {

		// add code here
		return 0;
	}

	public static void sortArray(int[] arr) {

		int temp = 0;
		for (int i = 0; i < arr.length; i++) {
			for (int j = i + 1; j < arr.length; j++) {
				if (arr[i] > arr[j]) {
					temp = arr[i];
					arr[i] = arr[j];
					arr[j] = temp;
				}
			}
		}

		// printing the array
		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i] + "");
		}
	}

	public static void sortBinaryArray(int[] arr) {

		int j = -1;
		for (int i = 0; i < arr.length; i++) {

			if (arr[i] < 1) {
				j++;
				int temp = arr[j];
				arr[j] = arr[i];
				arr[i] = temp;
			}
		}
		for (int i = 0; i < arr.length; i++)
			System.out.print(arr[i] + " ");

	}

	public static boolean checkAnagram(String first, String second) {
		char[] characters = first.toCharArray();
		StringBuilder sbSecond = new StringBuilder(second);

		for (char ch : characters) {
			int index = sbSecond.indexOf("" + ch);
			if (index != -1) {
				sbSecond.deleteCharAt(index);
			} else {
				return false;
			}
		}

		return sbSecond.length() == 0 ? true : false;
	}

	public static boolean iAnagram(String word, String anagram) {
		char[] charFromWord = word.toCharArray();
		char[] charFromAnagram = anagram.toCharArray();
		Arrays.sort(charFromWord);
		Arrays.sort(charFromAnagram);

		return Arrays.equals(charFromWord, charFromAnagram);
	}

	public static int distBetweenTwoChars(String word, String a, String b) {
		int s1no = word.indexOf(a);
		int s2no = word.indexOf(b);

		if (s1no > s2no)
			return s1no - s2no;
		else
			return s2no - s1no;
	}

	public static void permutation(String perm, String word) {
		if (word.isEmpty()) {
			System.err.println(perm + word);

		} else {
			for (int i = 0; i <= word.length(); i++) {
				permutation(perm + word.charAt(i), word.substring(0, i)
						+ word.substring(i + 1, word.length()));
			}
		}
	}

	public static void print2ndSmallest(int arr[]) {
		int first, second, arr_size = arr.length;

		if (arr_size < 2) {
			System.out.println(" Invalid Input ");
			return;
		}

		first = second = Integer.MAX_VALUE;
		for (int i = 0; i < arr_size; i++) {
			if (arr[i] < first) {
				second = first;
				first = arr[i];
			} else if (arr[i] < second && arr[i] != first)
				second = arr[i];
		}
		if (second == Integer.MAX_VALUE)
			System.out.println("There is no second" + "smallest element");
		else
			System.out.println("The smallest element is " + first
					+ " and second Smallest" + " element is " + second);
	}

	public static void leftRotate(int arr[], int d) {

		for (int j = 0; j < d; j++) {
			int i, temp;
			temp = arr[0];
			for (i = 0; i < arr.length - 1; i++) {
				arr[i] = arr[i + 1];
			}
			arr[i] = temp;
		}

		for (int k = 0; k < arr.length; k++) {
			System.out.println("Array after rotation is :" + arr[k]);
		}
	}

	public static void rightRotate(int arr[], int d) {
		for (int i = 0; i < d; i++) {

			int j, last;
			last = arr[arr.length - 1];

			for (j = arr.length - 1; j > 0; j--) {
				arr[j] = arr[j - 1];
			}
			arr[0] = last;
		}
	}

	public static void main(String[] args) {

		/*
		 * int[] arr = new int[] { 1, 2, 3, 4 }; for (int i = 0; i < arr.length;
		 * i++) { int first = arr[i]; }
		 * System.out.println(Arrays.toString(arr));
		 */

		// program to add underscore
		String result = addUnderscore("OracleService");
		System.out.println(result);

		// program for palindrome - same from both sides

		// program for fibonacci
		// fib(100);

		// sort binary nums in array in one pass
		System.out.println("sort an array in java");
		int[] binarray = new int[] { 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0 };
		sortBinaryArray(binarray);

		// sort an array in java
		System.out.println("sort an array in java");
		int[] array = new int[] { 1, 4, 53, 5, 57, 87, 34 };
		sortArray(array);

		// find pythagorean triplets (a^2 + b^2 = c^2) for an array

		// check two strings anagram or not
		checkAnagram("biswa", "wsabi");

		// check two strings anagram or not - 2nd way
		iAnagram("biswa", "wsabi");

		// distance between two chars in string
		System.out.println("Distance between chars in string: "
				+ distBetweenTwoChars("biswa", "i", "a"));

		// find all permutations of string
		permutation("", "biswa");

		// print first not repeated character from given String

		// Java Program for Longest Common Subsequence - DP problem

		// Java Program for Longest Increasing Subsequence - DP problem

		// find smallest and second smallest elements in array
		int arr[] = { 12, 13, 1, 10, 34, 1 };
		print2ndSmallest(arr);

		// find missing number in array
	}
}

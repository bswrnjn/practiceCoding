package business;

public class MaxConsecutive1s {

	public static int maxConsecutive1s(int arr[]) {

		int count = 0;
		int result = 0;

		for (int i = 0; i < arr.length - 1; i++) {
			if (arr[i] == 0) {
				count = 0;
			} else {
				count++;
				result = Math.max(result, count);
			}
		}

		return result;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] arr = new int[] { 1, 0, 0, 1, 1, 1, 0, 1, 0, 0, 0, 1 };
		System.out.println(maxConsecutive1s(arr));
	}
}

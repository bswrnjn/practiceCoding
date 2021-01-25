package business;

public class Test3 {

	public void reverseString(String str) {

		String[] strArray = str.split(" ");

		StringBuilder stringBuilder = new StringBuilder();

		for (int i = strArray.length - 1; i >= 0; i--) {

			stringBuilder.append(strArray[i]);

			stringBuilder.append(" ");

		}
		System.out.println(stringBuilder.toString());

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Test3 t = new Test3();
		t.reverseString("I like this");
	}

}

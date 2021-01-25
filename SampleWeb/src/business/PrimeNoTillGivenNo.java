package business;

public class PrimeNoTillGivenNo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// int counter = 0;
		for (int i = 2; i < 1000; i++) {
			if ((i % 1 == 0) && (i % 2 != 0)) {
				if ((i % 3 != 0)) {
					if ((i % 5 != 0)) {
						if ((i % 7 != 0)) {
							if ((i % 11 != 0)) {
								System.out.println(i);
								// counter++;
								// System.out.println("the count is :" +
								// counter);
							}
						}
					}
				}
			}
		}
	}
}

package business;

import java.math.BigDecimal;
import java.math.MathContext;

public class AverageRatingNew {

	public static BigDecimal averageCalc(double[] arr) {
		BigDecimal sum = BigDecimal.ZERO;

		for (int i = 0; i < arr.length - 1; i++) {
			sum = sum.add(new BigDecimal(arr[i]));
		}

		MathContext mc = new MathContext(3);
		BigDecimal avg = sum.divide(new BigDecimal(arr.length), mc);

		return avg;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(
				averageCalc(new double[] { 4.5, 3.4, 1.44, 2.87, 3.66 }));
		// String str = "abc";
		// System.out.println(str);

		// str = "xyz";
		// String s = new String("abc");
		// System.out.println(str);
		// System.out.println(s);
	}

}

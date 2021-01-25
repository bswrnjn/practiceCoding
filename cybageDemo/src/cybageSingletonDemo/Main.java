package cybageSingletonDemo;

import java.util.HashMap;

public class Main {

	public static void main(String args[]) {

		// SingletonDemo obj = SingletonDemo.getInstance();
		// System.out.println(obj);
		//
		// SingletonDemo obj1 = SingletonDemo.getInstance();
		// System.out.println(obj1);
		//
		// SingletonDemo obj2 = SingletonDemo.getInstance();
		// System.out.println(obj2);
		//
		// SingletonDemo obj3 = SingletonDemo.getInstance();
		// System.out.println(obj3);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put(null, "abc");

		System.out.println("printing null value: " + map.get(null));

		map.put(null, "Biswa");
		System.out.println("printing map: " + map);

	}
}

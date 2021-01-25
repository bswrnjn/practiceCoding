package automationFramework;

import java.util.HashMap;

public class Sample {

	public static void main(String[] args) {
		Demo d1 = new Demo("biswa", 6);
		Demo d2 = new Demo("rahul", 9);
		Demo d3 = new Demo("biswa", 6);

		HashMap<Demo, String> map = new HashMap<>();
		map.put(d1, "Tag1");
		map.put(d2, "Tag2");
		map.put(d3, "Tag3");

		System.out.println(" Printing the map: " + map);
	}

}

class Demo {
	String name;
	int num;

	public Demo(String name, int num) {
		super();
		this.name = name;
		this.num = num;
	}

}

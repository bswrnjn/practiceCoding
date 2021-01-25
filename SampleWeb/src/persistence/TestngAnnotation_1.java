package persistence;

import org.testng.annotations.Test;

// Order of execution:
// @BeforeSuite
// @BeforeTest
// @BeforeClass
// @BeforeMethod
// @Test
// @AfterMethod
// @AfterClass
// @AfterTest
// @AfterSuite

public class TestngAnnotation_1 {

	@Test
	public void Test1() {
		System.out.println("Inside test1");
	}

}

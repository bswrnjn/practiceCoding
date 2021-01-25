package main.java.jp.co.rakuten.travel.framework;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import main.java.jp.co.rakuten.travel.framework.configuration.Equipment;
import main.java.jp.co.rakuten.travel.framework.configuration.Equipment.EquipmentType;
import main.java.jp.co.rakuten.travel.framework.logger.TestLogger;
import main.java.jp.co.rakuten.travel.framework.page.UIController;
import main.java.jp.co.rakuten.travel.framework.parameter.FrameworkObject;
import main.java.jp.co.rakuten.travel.framework.parameter.PassCriteria;
import main.java.jp.co.rakuten.travel.framework.parameter.TestApiObject;
import main.java.jp.co.rakuten.travel.framework.parameter.TestApiParameters;
import main.java.jp.co.rakuten.travel.framework.parameter.TestObjects;
import main.java.jp.co.rakuten.travel.framework.parameter.TestParameters;
import main.java.jp.co.rakuten.travel.framework.stubs.ContextStub;
import main.java.jp.co.rakuten.travel.framework.stubs.Stub.StubLocation;

/**
 * Base test for all test that will be executed within this framework
 * 
 * @param <T>
 */
public abstract class BaseTest<T extends TestObjects> implements
		BaseInterface<T> {
	// since testng creates the test classes even before log4j is initialized,
	// delay log4j usage until before suite
	// this should be solved to eliminate logging issues in Equipments and
	// Controllers

	protected final TestLogger LOG = (TestLogger) TestLogger.getLogger(this
			.getClass());

	private boolean m_asserted = false;

	protected Configuration m_config = null;

	/**
	 * This concept is to allow the test to continue even on test error to allow
	 * graceful exit and cleanup process
	 */
	private final SoftAssert m_softAssert = new SoftAssert();

	protected T m_testObjects;

	/**
	 * Node to handle Logging and test file specific features
	 * 
	 * @param context
	 *            ITestContext
	 * @throws Exception
	 *             Message when error occurs
	 */
	@BeforeSuite
	public void beforeSuite(ITestContext context) throws Exception {
		setTestStep("@BeforeSuite");
	}

	/**
	 * Node to handle parameters and features,
	 * 
	 * @param args
	 *            Parameters
	 * @param context
	 *            ITestContext
	 */
	@BeforeTest
	@org.testng.annotations.Parameters({ "args" })
	public void beforeTest(@Optional("") String args, ITestContext context) {
		setTestStep("@BeforeTest");
	}

	/**
	 * Node to handle Class and Test information display
	 * 
	 * @param desc
	 *            Test description
	 */
	@BeforeClass
	@org.testng.annotations.Parameters({ "desc" })
	public void beforeClass(String desc) {
		setTestStep("@BeforeClass");

		// if( StringUtils.isEmpty( desc ) )
		// {
		// String msg = "Test Description MUST not be empty";
		// LOG.fatal( msg );
		// assertThis( msg );
		// }
	}

	@Test(timeOut = Time.HOUR)
	public void test(ITestContext context) {
		setTestStep("@Test");

		try {
			m_config = (Configuration) context
					.getAttribute(Configuration.CONFIG);
			m_config.addTestParameters(TestParameters.class);
			m_testObjects = onTestObjects();
		} catch (InstantiationException e) {
			String msg = "Can not instantiate test object";
			LOG.error(msg, e);
			throw new SkipException(msg, e);
		}

		LOG.info("onTestParameters");
		if (onTestParameters(m_testObjects).failed()) {
			String msg = "onTestParameters failed";
			LOG.error(msg, ErrorType.TEST_PARAMETERS_ERROR);
			Assert.assertTrue(false, msg);
		}

		try {
			PassCriteria criteria = PassCriteria.get(TestApiObject.instance()
					.get(TestApiParameters.API_PASS_CRITERIA));
			switch (criteria) {
			case TYPE_1:
				singleTest(context);
				// TestNG concept to fail the test case once an assert was saved
				// prior to calling assertAll
				m_softAssert.assertAll();
				break;

			case TYPE_2:
				int iteration = Integer.valueOf(TestApiObject.instance().get(
						TestApiParameters.API_ITERATION));
				iteratedTest(context, criteria, iteration);
				break;

			case TYPE_3:
				iteratedTest(context, criteria, 3);
				break;

			default:
				assertThis("PASS CRITERIA is undefined " + criteria);
			}
		} catch (SkipException e) {
			LOG.error(e.getClass().getSimpleName() + " found with message "
					+ e.getMessage());

			LOG.info("onError");
			onError();

			throw e;
		} catch (AssertionError e) {
			if (TestApiObject.instance().bool(
					TestApiParameters.API_NEGATIVE_TEST)) {
				LOG.info("Negative Test Case");
			} else {
				LOG.info("Usual TestNG Soft Assertion");
				throw e;
			}
		} catch (UnreachableBrowserException e) {
			// try to recover
			Equipment browser = m_config.equipment(EquipmentType.BROWSER);
			browser.recover();

			// screen shot is not possible since the browser is already dead
			String msg = e.getClass().getSimpleName() + " found with message "
					+ e.getMessage();
			LOG.error("Cause: " + e.getCause(), ErrorType.TIME_OUT);
			LOG.error(msg);

			LOG.info("onError");
			onError();

			throw new SkipException(msg);
		} catch (TimeoutException e) {
			// try to recover
			Equipment browser = m_config.equipment(EquipmentType.BROWSER);
			browser.recover();

			// screen shot is not possible since the browser is already dead
			String msg = e.getClass().getSimpleName() + " found with message "
					+ e.getMessage();
			LOG.error("Cause: " + e.getCause(), ErrorType.TIME_OUT);
			LOG.error(msg);

			LOG.info("onError");
			onError();

			throw new SkipException(msg);
		} catch (WebDriverException e) {
			m_config.errorInfo();
			String msg = e.getClass().getSimpleName() + " found with message "
					+ e.getMessage();
			LOG.error("Cause: " + e.getCause());
			LOG.error(msg, ErrorType.APPIUM_SERVER_ERROR);
			LOG.info("onError");
			onError();
			assertThis(msg);
		} catch (TestNGRuntimeException e) {
			m_config.errorInfo();
			String msg = e.getClass().getSimpleName() + " found with message "
					+ e.getMessage();
			LOG.error("Cause: " + e.getCause());
			LOG.error(msg, ErrorType.AUTOMATION_ERROR);
			LOG.info("onError");
			onError();
			assertThis(msg);
		} catch (Exception e) {
			m_config.errorInfo();
			String msg = e.getClass().getSimpleName() + " found with message "
					+ e.getMessage();
			LOG.error("Cause: " + e.getCause());
			LOG.error(msg, ErrorType.UNVERIFIED);

			LOG.info("TEST FAILED BUT THE EXCEPTION SHOULD BE FIXED FIRST BECAUSE THIS SHOULD NOT HAPPEN");

			LOG.info("onError");
			onError();
			assertThis(msg);
		}

		if (TestApiObject.instance().bool(TestApiParameters.API_NEGATIVE_TEST)) {
			String msg = "This is a Negative Test Case. Thou SHALL NOT Pass";
			assertMe(msg);
		}
	}

	@Override
	public T testObjects() {
		return m_testObjects;
	}

	private void singleTest(ITestContext context) {
		// precondition stubs
		executeStubs(StubLocation.PRE_CONDITION, context);
		LOG.info("onPreCondition");
		if (onPreCondition().failed()) {
			LOG.info("onError");

			// called because assertion is not necessary on this level
			m_config.errorInfo();

			executeStubs(StubLocation.CLEAN_UP, context);
			onError();

			throw new SkipException("PRECONDITION FAILURE");
		}
		executeStubs(StubLocation.TEST, context);
		LOG.info("onTest");
		if (onTest().failed()) {
			assertMe("onTest failed");

			LOG.info("TEST FAILED");
			LOG.info("onError");
			executeStubs(StubLocation.CLEAN_UP, context);
			onError();

			return;
		}

		// post condition stubs
		executeStubs(StubLocation.POST_CONDITION, context);

		LOG.info("onPostCondition");
		if (onPostCondition().failed()) {
			assertMe("onPostCondition failed");

			LOG.info("TEST FAILED");

			LOG.info("onError");
			executeStubs(StubLocation.CLEAN_UP, context);
			onError();

			return;
		}

		LOG.info("TEST PASSED");

		// stubs
		executeStubs(StubLocation.END, context);
	}

	/**
	 * Multiple iteration based test. <br>
	 * PASS Criteria
	 * 
	 * @param iteration
	 *            Times
	 * @param criteria
	 *            Single/Iteration/Minimum
	 */
	private void iteratedTest(ITestContext context, PassCriteria criteria,
			int iteration) {
		int pass = 0;
		int fail = 0;
		int skip = 0;

		Result criteriaFlag = Result.UNKNOWN;
		for (int i = 0; i < iteration; ++i) {
			// preliminary check of criteria so it can exit the iteration in the
			// middle of the test
			switch (criteria) {
			case TYPE_2:
			case TYPE_3:
				criteriaFlag = criteria.checkCriteria(pass, fail, skip,
						iteration);
				break;
			default:
				break;
			}
			if (!criteriaFlag.equals(Result.UNKNOWN)) {
				LOG.info("Criteria met for " + criteria + " with "
						+ criteriaFlag);
				break;
			}

			setTestInfo("TEST ITERATION # " + i);
			LOG.info("partial test result : " + (pass + skip + fail) + "/"
					+ iteration + " (" + pass + " pass / " + skip + " skip / "
					+ fail + " fail)");

			try {
				LOG.info("onPreCondition");
				if (onPreCondition().failed()) {
					assertMe("onPreCondition failed");

					LOG.info("onError");
					onError();

					++skip;

					LOG.info("TEST SKIPPED : ITERATION #" + i);
					continue;
				}

				LOG.info("onTest");
				if (onTest().failed()) {
					assertMe("onTest failed");

					LOG.info("onError");
					onError();

					++fail;

					LOG.error("TEST FAILED : ITERATION #" + i);
					continue;
				}

				LOG.info("onPostCondition");
				if (onPostCondition().failed()) {
					assertMe("onPostCondition failed");

					LOG.info("onError");
					onError();

					++fail;

					LOG.error("TEST FAILED : ITERATION #" + i);
					continue;
				}

				++pass;

				LOG.info("TEST PASSED : ITERATION #" + i);

			} catch (UnreachableBrowserException | TimeoutException e) {
				// screen shot is not possible since the browser is already dead
				assertMe(e.getClass().getSimpleName() + " received "
						+ " in Test Iteration # " + i);
				++fail;

				// try to recover
				Equipment browser = m_config.equipment(EquipmentType.BROWSER);
				browser.recover();

				// best effort to do cleanup, even after recovery
				LOG.info("onError");
				onError();
			} catch (TestNGRuntimeException e) {
				m_config.errorInfo();
				String msg = e.getClass().getSimpleName()
						+ " found with message " + e.getMessage();
				LOG.error("Cause: " + e.getCause());
				LOG.error(msg, ErrorType.UNVERIFIED);

				LOG.info("onError");
				onError();

				LOG.info("TEST FAILED BUT THE EXCEPTION SHOULD BE FIXED FIRST BECAUSE THIS SHOULD NOT HAPPEN");
			} catch (AssertionError e) {
				if (TestApiObject.instance().bool(
						TestApiParameters.API_NEGATIVE_TEST)) {
					LOG.info("Negative Test Case");
				} else {
					++fail;
					LOG.info("Usual TestNG Soft Assertion");
					throw e;
				}
			} catch (SkipException e) {
				assertMe("SkipException received" + " in Test Iteration # " + i);
				++skip;
				LOG.info("onError");
				onError();
			} catch (Exception e) {
				assertMe(e.getClass().getSimpleName() + " received "
						+ " in Test Iteration # " + i);
				++fail;

				LOG.info("onError");
				onError();
			}

			Utility.resetReferenceTime();
		}

		// final check of criteria, only possible if all iterations are executed
		if (criteriaFlag.equals(Result.UNKNOWN)) {
			criteriaFlag = criteria.checkCriteria(pass, fail, skip, iteration);
		}

		setTestInfo("TEST SUMMARY");
		LOG.info("TOTAL    : " + iteration);
		LOG.info("PASS     : " + pass);
		LOG.info("SKIP     : " + skip);
		LOG.info("FAIL     : " + fail);

		switch (criteriaFlag) {
		case FAIL:
			LOG.error(criteriaFlag + " Criteria met " + (pass + skip + fail)
					+ "/" + iteration + " (" + pass + " pass / " + skip
					+ " skip / " + fail + " fail)");
			m_softAssert.assertAll();
			break;

		case SKIP:
			String msg = criteriaFlag + " Criteria met " + (pass + skip + fail)
					+ "/" + iteration + " (" + pass + " pass / " + skip
					+ " skip / " + fail + " fail)";
			LOG.error(msg);
			throw new SkipException(msg);

		default:
			LOG.info(criteriaFlag + " Criteria met " + (pass + skip + fail)
					+ "/" + iteration + " (" + pass + " pass / " + skip
					+ " skip / " + fail + " fail)");
			break;
		}
	}

	protected abstract Result onPreCondition();

	protected abstract Result onTest();

	protected abstract Result onPostCondition();

	protected abstract void onError();

	protected abstract T onTestObjects() throws InstantiationException;

	protected abstract Result onTestParameters(T testObject);

	@AfterClass
	public void afterClass() {
		setTestStep("@AfterClass");
	}

	@AfterTest
	public void afterTest() {
		setTestStep("@AfterTest");
	}

	@AfterSuite
	public void afterSuite() {
		setTestStep("@AfterSuite");
		LOG.info("SUITE FINISHED!!!");
		String[] types = TestApiObject.instance()
				.get(TestApiParameters.API_CONTROLLERS).split(",");
		for (String type : types) {
			ControllerType controllerType = Utility.getEnum(type,
					ControllerType.class);
			if (type.equals(EquipmentType.UNKNOWN)) {
				continue;
			}
			UIController controller = (UIController) m_config
					.controller(controllerType);
			if (controller != null
					&& TestApiObject.instance().numeric(
							TestApiParameters.API_ELEMENT_RETRY_COUNT) > 0) {
				LOG.info("TOTAL ELEMENT RETRIES " + controller.totalRetries());
			}
		}
	}

	protected void assertMe(String message) {
		LOG.error(this.getClass().getSimpleName() + " Asserted with message "
				+ message);
		m_config.errorInfo();

		if (!m_asserted) {
			m_asserted = true;
			m_softAssert.fail(message);
		}
	}

	protected void assertThis(String message) {
		LOG.fatal(message);
		Assert.assertTrue(false, message);
	}

	protected void setTestCase(String name, String id) {

	}

	protected void setTestInfo(String message) {
		// add asterisk before and after the message and the whole line will
		// compose of 100 characters
		int dash = (100 - message.length()) / 2;
		String msg = StringUtils.repeat("-", dash)
				+ message
				+ StringUtils
						.repeat("-", dash + ((100 - message.length()) % 2));
		LOG.info(msg);
	}

	protected void setTestStep(String message) {
		// add asterisk before and after the message and the whole line will
		// compose of 100 characters
		int asterisk = (100 - message.length()) / 2;
		String msg = StringUtils.repeat("*", asterisk)
				+ message
				+ StringUtils.repeat("*", asterisk
						+ ((100 - message.length()) % 2));
		LOG.info(msg);
	}

	/**
	 * Simulate process wait in milliseconds
	 * 
	 * @param milli
	 *            Millisecond
	 */
	protected void sleep(long milli) {
		try {
			TimeUnit.MILLISECONDS.sleep(milli);
		} catch (InterruptedException e) {
			LOG.warn(
					e.getClass().getSimpleName() + " found with message "
							+ e.getMessage(), e);
		}
	}

	protected void pause() {
		pause("PAUSED... Press any button when ready.");
	}

	private void executeStubs(StubLocation location, ITestContext context) {
		for (Stub stub : FrameworkObject.instance().stub(location)) {
			LOG.info("Executing Stub " + location + " " + stub.name());
			if (stub instanceof ContextStub) {
				((ContextStub) stub).execute(context);
			} else {
				stub.execute();
			}
		}
	}

	protected void pause(String message) {
		LOG.info("***********************************************************************");
		LOG.info("***********************************************************************");
		LOG.info("***********************************************************************");
		LOG.info(message);
		LOG.info("*** PRESS <ENTER> to Continue ***");
		try {
			System.in.read();
		} catch (IOException e) {
			LOG.warn("System.in.read() UNEXPECTED ERROR " + e.getMessage());
		}
	}
}

package jp.co.rakuten.travel.framework.listeners;

import org.testng.IExecutionListener;
import org.testng.ISuiteListener;
import org.testng.ITestListener;

public interface ITestListeners extends IExecutionListener, ISuiteListener, ITestListener
{
    final String NOT_YET_IMPLEMENTED = "NOT yet implemented";
}

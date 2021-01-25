package jp.co.rakuten.travel.framework.stubs;

import org.testng.ITestResult;

public interface ResultStub extends Stub
{
    void execute( ITestResult result );
}

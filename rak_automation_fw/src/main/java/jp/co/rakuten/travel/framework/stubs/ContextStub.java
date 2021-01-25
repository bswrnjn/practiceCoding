package jp.co.rakuten.travel.framework.stubs;

import org.testng.ITestContext;

public interface ContextStub extends Stub
{
    void execute( ITestContext context );
}

package jp.co.rakuten.travel.framework.stubs;

import java.util.Map;

import org.testng.ITestContext;

import jp.co.rakuten.travel.framework.BaseTest;
import jp.co.rakuten.travel.framework.parameter.Parameters;

/**
 * Simple Stub to handle {@link BaseTest} additional external processes outside the actual test
 *
 */
public interface Stub
{
    /**
     * callback to start the stub process
     */
    void execute();

    /**
     * 
     * @return description or name of the stub
     */
    String name();

    /**
     * 
     * @return parameter to be used by the stub or provided by the stub
     */
    Map< String, String > parameters();

    /**
     * differentiation of stub
     *
     */
    public enum StubType implements Parameters
    {
        /**
         * Usual Stub with Map Parameters
         */
        SIMPLE_STUB,

        /**
         * Stub to make use of TestNG {@link ITestContext} as additional parameters
         */
        CONTEXT_STUB,

        UNKNOWN;

        @Override
        public String val()
        {
            return name();
        }

        @Override
        public Parameters unknown()
        {
            return UNKNOWN;
        }
    }

    /**
     * Location to fire the stub based on {@link BaseTest} API
     *
     */
    public enum StubLocation implements Parameters
    {
        /**
         * stub to fire before pre-condition
         */
        PRE_CONDITION,

        /**
         * stub to fire before test
         */
        TEST,

        /**
         * stub to fire before post-condition
         */
        POST_CONDITION,

        /**
         * stub to fire at the end of successful test
         */
        END,

        /**
         * stub to fire before clean-up process, usually during error
         */
        CLEAN_UP,

        UNKNOWN;

        @Override
        public String val()
        {
            return name();
        }

        @Override
        public Parameters unknown()
        {
            return UNKNOWN;
        }
    }
}

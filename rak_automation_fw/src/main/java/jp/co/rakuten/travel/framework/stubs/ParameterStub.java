package jp.co.rakuten.travel.framework.stubs;

import java.util.Map;

import jp.co.rakuten.travel.framework.parameter.Parameters;
import jp.co.rakuten.travel.framework.parameter.TestObjects;

/**
 * Usual test parameters are instantiated during @BeforeTest
 * this helps to do additional tasks during start of @Test
 */
public interface ParameterStub< T extends TestObjects > extends ResultStub
{
    void updateParameters( Map< Parameters, String > map );
}

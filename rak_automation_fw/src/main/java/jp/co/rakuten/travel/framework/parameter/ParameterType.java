package jp.co.rakuten.travel.framework.parameter;

public enum ParameterType
{
    //
    FRAMEWORK,
    TEST_API,
    TESTCASE;

    public static ParameterType getType( String param )
    {
        if( param.toLowerCase().startsWith( "fw_" ) )
        {
            return FRAMEWORK;
        }
        else if( param.toLowerCase().startsWith( "api_" ) )
        {
            return TEST_API;
        }

        return TESTCASE;
    }
}

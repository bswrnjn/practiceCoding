package jp.co.rakuten.travel.framework.tools.utility;

/**
 * Enum representing menu options shown at the time of overriding parameters
 * @version 1.0.0
 * @since 1.0.0  
 */
public enum Menu
{
    UPDATE_TESTCASE( "1" ), //
    UPDATE_SUITEPARAMETERS( "2" ), //
    UPDATE_TESTCASE_SUITEPARAMETERS( "3" );

    private String value;

    Menu( String value )
    {
        this.value = value;
    }

    public static Menu getMenu( String value )
    {
        for( Menu menu : Menu.values() )
        {
            if( menu.value.equals( value ) )
            {
                return menu;
            }
        }
        return null;
    }
}

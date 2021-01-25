package jp.co.rakuten.travel.framework.bug;

import java.util.HashMap;
import java.util.Map;

import jp.co.rakuten.travel.framework.parameter.TestApiObject;
import jp.co.rakuten.travel.framework.parameter.TestApiParameters;
import jp.co.rakuten.travel.framework.utility.Utility;

/**
 * Used to store bug information relate to {@link BugId}
 *
 */
public class BugMap
{
    private static BugMap        s_instance;
    private Map< BugId, String > m_bugMap;

    private static BugMap instance()
    {
        if( s_instance == null )
        {
            s_instance = new BugMap();
            s_instance.m_bugMap = new HashMap<>();

            populate();
        }

        return s_instance;
    }

    private static void populate()
    {
        String [] bugInfoList = TestApiObject.instance().get( TestApiParameters.API_IGNORE_BUG ).split( "," );
        if( bugInfoList != null )
        {
            for( String bugInfo : bugInfoList )
            {
                String [] bug = bugInfo.split( ":" );
                s_instance.m_bugMap.put( Utility.getEnum( bug[ 0 ], BugId.class ), bug.length < 2 ? "" : bug[ 1 ] );
            }
        }
    }

    /**
     * 
     * @param id {@link BugId}
     * @return <code>true</code> if 'id' is present in the map and <code>false</code> otherwise
     */
    public static boolean ignore( BugId id )
    {
        return instance().m_bugMap.containsKey( id );
    }

    /**
     * 
     * @param id
     * @return any specified {@link String} in relation to the give 'id', usually URL related info of the bug
     */
    public static String link( BugId id )
    {
        if( ignore( id ) )
        {
            return instance().m_bugMap.get( id );
        }

        return "";
    }
}

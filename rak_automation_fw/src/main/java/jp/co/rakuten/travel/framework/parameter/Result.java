package jp.co.rakuten.travel.framework.parameter;

public enum Result
{
    PASS( 1 ), //
    SKIP( 3 ),
    FAIL( 2 ),
    UNKNOWN( 0 );

    int m_val;

    Result( int val )
    {
        m_val = val;
    }

    public int val()
    {
        return m_val;
    }

    public boolean passed()
    {
        return m_val == 1;
    }

    public boolean failed()
    {
        return m_val != 1;
    }

    public boolean skipped()
    {
        return m_val == 2;
    }

    public static Result get( int result )
    {
        for( Result res : values() )
        {
            if( res.val() == result )
            {
                return res;
            }
        }
        return UNKNOWN;
    }
}

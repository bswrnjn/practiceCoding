package jp.co.rakuten.travel.framework.utility;

import java.util.ArrayList;
import java.util.List;

import jp.co.rakuten.travel.framework.logger.TestLogger;

import org.apache.log4j.Logger;

public class ThreadController
{
    private Logger                  LOG = TestLogger.getLogger( ThreadInstance.class );

    private List< ThreadEntry >     m_threadList;

    private static ThreadController s_instance;

    private ThreadController()
    {
        m_threadList = new ArrayList<>();
    }

    public static ThreadController instance()
    {
        if( s_instance == null )
        {
            s_instance = new ThreadController();
        }

        return s_instance;
    }

    public int size()
    {
        return m_threadList.size();
    }

    /**
     * clears and resets the test map
     */
    public void clearTests()
    {
        m_threadList.clear();
    }

    /**
     *
     * @param test {@link ThreadInstance}
     */
    public void addTest( ThreadInstance test )
    {
        m_threadList.add( new ThreadEntry( test, m_threadList.size() + 1 ) );
    }

    public boolean execute()
    {
        for( ThreadEntry entry : m_threadList )
        {
            Thread thread = new Thread( entry.getInstance() );
            entry.setThreadId( thread.getId() );
            thread.start();
            LOG.info( "Executing " + entry );
        }

        boolean ret = true;
        while( m_threadList.size() > 0 )
        {
            for( ThreadEntry entry : m_threadList )
            {
                if( entry.getInstance().isFinished() )
                {
                    LOG.info( "Thread " + entry + " ended successfully." );
                    if( !entry.getInstance().result() )
                    {
                        ret = false;
                    }
                    m_threadList.remove( entry );
                    break;
                }
            }
            sleep( 100 );
        }

        return ret;
    }

    public ThreadInstance getThreadInstance( long threadId )
    {
        for( ThreadEntry entry : m_threadList )
        {
            if( entry.getThreadId() == threadId )
            {
                return entry.getInstance();
            }
        }
        return null;
    }

    public int getThreadTask( long threadId )
    {
        for( ThreadEntry entry : m_threadList )
        {
            if( entry.getThreadId() == threadId )
            {
                return entry.getTask();
            }
        }

        return 1;
    }

    protected void sleep( long milli )
    {
        try
        {
            Thread.sleep( milli );
        }
        catch( InterruptedException e )
        {
            LOG.warn( e.getMessage() );
        }
    }
}

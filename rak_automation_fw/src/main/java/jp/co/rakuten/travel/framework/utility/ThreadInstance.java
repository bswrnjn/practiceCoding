package jp.co.rakuten.travel.framework.utility;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import jp.co.rakuten.travel.framework.logger.TestLogger;

public abstract class ThreadInstance implements Runnable
{
    protected Logger                LOG;

    /**
     * task number used for anything
     */
    protected int                   m_task;
    /**
     * delay in which the test will wait before is starts
     */
    protected long                  m_delay;
    /**
     * wait wime in which the test will wait before is starts
     */
    protected long                  m_wait;

    /**
     * flag if test is already started, after the delay
     */
    protected boolean               m_started;

    /**
     * flag if test is already finished and waiting for all children to be finished
     */
    protected boolean               m_finished;

    /**
     * test result
     */
    protected boolean               m_result;

    /**
     * test result
     */
    protected boolean               m_startFlag;

    /**
     * Parent thread if they are linked together
     */
    protected Set< ThreadInstance > m_children;

    public ThreadInstance()
    {
        LOG = TestLogger.getLogger( this.getClass() );
        m_children = new HashSet<>();
        m_startFlag = true;
    }

    public ThreadInstance( TestLogger logger )
    {
        LOG = logger;
        m_children = new HashSet<>();
        m_startFlag = true;
    }

    public void setTask( int task )
    {
        m_task = task;
    }

    public int getTask()
    {
        return m_task;
    }

    public void setDelay( long delay )
    {
        m_delay = delay;
    }

    public void setWait( long wait )
    {
        m_wait = wait;
    }

    public long getDelay()
    {
        return m_delay;
    }

    public boolean isStarted()
    {
        return m_started;
    }

    public boolean isFinished()
    {
        return m_finished;
    }

    public boolean result()
    {
        return m_result;
    }

    /**
     * delayed start initiated as a child entry
     * @param flag Start flag
     */
    public void start( boolean flag )
    {
        m_startFlag = flag;
    }

    public void addChild( ThreadInstance child )
    {
        m_children.add( child );
    }

    /**
      * call for the thread to execute
      */
    @Override
    public void run()
    {
        LOG.debug( "THREAD EXECUTION START for " + getName() );

        Thread.currentThread().setName( getName() );

        LOG.debug( "Thread ID : " + Thread.currentThread().getId() );

        // if delay is set, start the thread delayed
        if( m_delay > 0 )
        {
            LOG.debug( "Delay is introduced. Waiting for " + m_delay + " milliseconds to start" );
            sleep( m_delay );
        }

        // if the thread enabled a switch, wait for its toggle
        // wait only for 5 minutes
        if( !m_startFlag )
        {
            LOG.debug( "Waiting for start flag to be initiated" );
        }
        long timestamp = System.currentTimeMillis();
        while( !m_startFlag && ( (System.currentTimeMillis() - timestamp) < (5 * Time.MINUTE)) )
        {
            sleep( 5 );
        }
        if( (System.currentTimeMillis() - timestamp) > (5 * Time.MINUTE) )
        {
            LOG.error( "Start Flag not toggled. Test did not start" );
            m_started = false;
            m_result = false;
            m_finished = true;
            return;
        }

        m_started = true;
        m_result = true;
        if( !test() )
        {
            m_result = false;
        }
        m_finished = true;

        if( m_children.size() > 0 )
        {
            LOG.debug( "Waiting for Child Process to Finish" );
        }

        // if the thread has children, wait for them to finish first
        // wait only for 10 minutes
        timestamp = System.currentTimeMillis();
        while( ( (System.currentTimeMillis() - timestamp) < (10 * Time.MINUTE)) && m_children.size() > 0 )
        {
            for( ThreadInstance thread : m_children )
            {
                if( thread.isFinished() )
                {
                    LOG.debug( "Thread " + thread.getName() + " finished." );
                    if( !thread.result() )
                    {
                        LOG.debug( "[WARN] Thread " + thread.getName() + " : RESULT failed" );
                        m_result = false;
                    }
                    m_children.remove( thread );
                    break;
                }
            }
            if( m_wait > 0 )
            {
                sleep( m_wait );
            }
        }
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

    public String getName()
    {
        return this.getClass().getSimpleName();
    }

    /**
     * Main test
     * @return TRUE if successful and FALSE otherwise 
     */
    protected abstract boolean test();
}

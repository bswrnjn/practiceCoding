package jp.co.rakuten.travel.framework.utility;

public class ThreadEntry
{
    ThreadInstance m_instance;
    long           m_threadId;
    int            m_task;

    public ThreadEntry( ThreadInstance instance, int task )
    {
        m_instance = instance;
        m_task = task;
    }

    public ThreadInstance getInstance()
    {
        return m_instance;
    }

    public void setInstance( ThreadInstance instance )
    {
        m_instance = instance;
    }

    public long getThreadId()
    {
        return m_threadId;
    }

    public void setThreadId( long threadId )
    {
        m_threadId = threadId;
    }

    public int getTask()
    {
        return m_task;
    }

    public void setTask( int task )
    {
        m_task = task;
    }

    @Override
    public String toString()
    {
        return m_instance.getClass().getSimpleName() + " id:" + m_threadId + " task:" + m_task;
    }
}

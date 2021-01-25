package jp.co.rakuten.travel.framework.listeners;

import java.util.List;

public class TestListeners extends AbstractTestListeners
{
    protected final String INDEX_HTML = "index.html";
    protected final String RESULT_CSV = "result.lst";

    @Override
    public void addListeners( List< ITestListeners > listeners )
    {
        listeners.add( new HtmlListener( m_logPath, INDEX_HTML ) );
        listeners.add( new ConfigurationListener( m_logPath ) );
        listeners.add( new CsvListener( m_logPath, RESULT_CSV ) );
    }
}

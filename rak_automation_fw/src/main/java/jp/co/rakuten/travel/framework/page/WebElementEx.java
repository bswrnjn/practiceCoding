package jp.co.rakuten.travel.framework.page;

import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.utility.ByType;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

public abstract class WebElementEx
{
    protected Logger         LOG = TestLogger.getLogger( this.getClass() );

    private final WebElement m_element;
    private final ByType     m_type;
    private final String     m_id;

    public WebElementEx( WebElement element, ByType type, String id )
    {
        m_element = element;
        m_type = type;
        m_id = id;
    }

    public WebElementEx( WebElement element )
    {
        m_element = element;
        m_type = ByType.UNKNOWN;
        m_id = "";
    }

    public WebElement element()
    {
        return m_element;
    }

    protected ByType type()
    {
        return m_type;
    }

    protected String id()
    {
        return m_id;
    }
}

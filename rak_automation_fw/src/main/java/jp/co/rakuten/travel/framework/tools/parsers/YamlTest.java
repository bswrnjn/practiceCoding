package jp.co.rakuten.travel.framework.tools.parsers;

import java.util.List;
import java.util.Map;

import jp.co.rakuten.travel.framework.tools.constants.Constants;

/**
 * Represents TestCases from YAML
 * @version 1.0.0
 * @since 1.0.0  
 */
public class YamlTest
{
    private String                m_name;
    private List< YamlTestClass > m_classes;
    private Map< String, String > m_parameters;

    public String getName()
    {
        return m_name;
    }

    public void setName( String name )
    {
        this.m_name = name;
    }

    public List< YamlTestClass > getClasses()
    {
        return m_classes;
    }

    public void setClasses( List< YamlTestClass > classes )
    {
        this.m_classes = classes;
    }

    public Map< String, String > getParameters()
    {
        return m_parameters;
    }

    public void setParameters( Map< String, String > parameters )
    {
        this.m_parameters = parameters;
    }

    public String getDescription()
    {
        return this.m_parameters.get( Constants.DESCRIPTION );
    }
}

package jp.co.rakuten.travel.framework.tools.parsers;

import java.util.List;
import java.util.Map;

import org.dozer.Mapping;

import com.google.gson.annotations.SerializedName;

/**
 * Represents TestCases from MongoDB
 * @version 1.0.0
 * @since 1.0.0  
 */
public class TestCase
{
    @SerializedName( "name" )
    private String m_name;

    @SerializedName( "classes" )
    private List< String > m_classes;

    @SerializedName( "parameters" )
    private Map< String, String > m_parameters;

    @Mapping( "name" )
    public String getName()
    {
        return m_name;
    }

    public void setName( String name )
    {
        this.m_name = name;
    }

    @Mapping( "classes" )
    public List< String > getClasses()
    {
        return m_classes;
    }

    public void setClasses( List< String > classes )
    {
        this.m_classes = classes;
    }

    @Mapping( "parameters" )
    public Map< String, String > getParameters()
    {
        return m_parameters;
    }

    public void setParameters( Map< String, String > parameters )
    {
        this.m_parameters = parameters;
    }
}

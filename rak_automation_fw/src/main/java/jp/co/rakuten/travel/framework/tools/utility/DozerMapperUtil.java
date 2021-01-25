package jp.co.rakuten.travel.framework.tools.utility;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

/**
 * Utility to convert source object to destination object using Dozer
 * @version 1.0.0
 * @since 1.0.0  
 */
public class DozerMapperUtil
{
    static Mapper m_mapper;

    static
    {
        List< String > mappingFiles = new ArrayList< String >();
        mappingFiles.add( "dozer-mapping.xml" );
        m_mapper = new DozerBeanMapper( mappingFiles );
    }

    /**
     * Maps sourceObject to destinationObject using specified mappings and convertors
     * @param sourceObject Source
     * @param destinationObject Destination
     */
    public static void mapper( Object sourceObject, Object destinationObject )
    {
        m_mapper.map( sourceObject, destinationObject );
    }
}

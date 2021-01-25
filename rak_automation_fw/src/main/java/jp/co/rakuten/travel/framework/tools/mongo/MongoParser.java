package jp.co.rakuten.travel.framework.tools.mongo;

import java.util.List;

import org.testng.xml.XmlSuite;

/**
 * Interface need to implement by Mongo parser.
 * @version 1.0.0
 * @since 1.0.0  
 */
public interface MongoParser
{
    List< XmlSuite > parse( List< String > suiteName );
}

package jp.co.rakuten.travel.framework.connection;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

/**
 * Interface need to implement by MongoDao.
 * @version 1.0.0
 * @since 1.0.0  
 */
public interface MongoDao
{
    static final String COLLECTION_TEST_SUITE    = "test_suite";

    static final String COLLECTION_TEST_CASE     = "test_case";

    static final String COLLECTION_TEST_SCEANRIO = "test_scenario";

    static final String NAME                     = "name";

    static final String SUITE_ID                 = "suite_id";

    static final String SCENARIO_ID              = "scenario_id";

    List< Document > getSuites();

    List< Document > getSuites( List< String > suiteName );

    List< Document > getScenarios( String suiteId );

    List< ArrayList< Document > > getTestCases( String scenarioId );

    Document getDocumentByName( String name, String mongoCollection );

    void insertDocument( String json, String mongoCollection );

    void replaceDocument( String name, Document document, String mongoCollection );

    void removeDocument( String name, String mongoCollection );
}

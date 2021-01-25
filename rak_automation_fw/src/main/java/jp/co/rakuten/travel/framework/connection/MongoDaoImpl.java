package jp.co.rakuten.travel.framework.connection;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

/**
 * DAO class, Fetches the data from MongoDB 
 * @version 1.0.0
 * @since 1.0.0  
 *
 */
public class MongoDaoImpl implements MongoDao
{
    private DBParameter m_parameter;

    public MongoDaoImpl( DBParameter parameter )
    {
        this.m_parameter = parameter;
    }

    /**
     * Get the all suites from MongoDB
     * @return List List of Document
     */
    @Override
    public List< Document > getSuites()
    {
        ArrayList< Document > documents = null;
        MongoClient mongoClient = null;

        try
        {
            /*
             * Get the mongoclient connected to specified host
             */
            mongoClient = new MongoClient( m_parameter.getHost(), m_parameter.getPort() );

            /*
             * Get the required MongoDB database and collection
             */
            MongoDatabase database = mongoClient.getDatabase( m_parameter.getDatabase() );
            MongoCollection< Document > collection = database.getCollection( COLLECTION_TEST_SUITE );

            /*
             * Get the list of all Documents
             */
            documents = collection.find().into( new ArrayList< Document >() );
        }
        finally
        {
            if( mongoClient != null )
            {
                mongoClient.close();
            }
        }
        return documents;
    }

    /**
     * Get the suites from MongoDB, 
     * Checks that suiteName is valid/ invalid and make related entries in log file.
     * @param suiteName List of suite names
     * @return List List of Documents
     */
    @Override
    public List< Document > getSuites( List< String > suiteNames )
    {
        ArrayList< Document > documents = null;
        MongoClient mongoClient = null;

        try
        {
            /*
             * Get the mongoclient connected to specified host
             */
            mongoClient = new MongoClient( m_parameter.getHost(), m_parameter.getPort() );

            /*
             * Get the required MongoDB database and collection
             */
            MongoDatabase database = mongoClient.getDatabase( m_parameter.getDatabase() );
            MongoCollection< Document > collection = database.getCollection( COLLECTION_TEST_SUITE );

            /*
             * Get the list of Documents with matching suiteNames
             */
            documents = collection.find( in( NAME, suiteNames ) ).into( new ArrayList< Document >() );
        }
        finally
        {
            if( mongoClient != null )
            {
                mongoClient.close();
            }
        }
        return documents;
    }

    /**
     * Get Mongo document by suiteName 
     * @param name      :name of the suite to retrieve related Document from MongoDB
     * @return Document
     */
    @Override
    public Document getDocumentByName( String name, String mongoCollection )
    {
        MongoClient mongoClient = null;
        Document documents = null;
        try
        {
            /*
             * Get the mongoclient connected to specified host
             */
            mongoClient = new MongoClient( m_parameter.getHost(), m_parameter.getPort() );

            /*
             * Get the required MongoDB database and collection
             */
            MongoDatabase database = mongoClient.getDatabase( m_parameter.getDatabase() );
            MongoCollection< Document > collection = database.getCollection( mongoCollection );

            MongoCursor< Document > cursor = collection.find( eq( NAME, name ) ).iterator();
            while( cursor.hasNext() )
            {
                documents = cursor.next();
            }
        }
        finally
        {
            if( mongoClient != null )
            {
                mongoClient.close();
            }
        }

        return documents;
    }

    /**
     * Insert document into MongoDB
     * @param json : JSON formatted string which will be converted to Document by using parse() provided
     *               by the Document and inserted into MongoDB collection
     */
    @Override
    public void insertDocument( String json, String mongoCollection )
    {
        MongoClient mongoClient = null;
        try
        {
            /*
             * Get the mongoclient connected to specified host
             */
            mongoClient = new MongoClient( m_parameter.getHost(), m_parameter.getPort() );

            /*
             * Get the required MongoDB database and collection
             */
            MongoDatabase database = mongoClient.getDatabase( m_parameter.getDatabase() );
            MongoCollection< Document > collection = database.getCollection( mongoCollection );

            /*
             * Parse and insert document into mongo collection
             */
            Document document = Document.parse( json );
            collection.insertOne( document );
        }
        finally
        {
            if( mongoClient != null )
            {
                mongoClient.close();
            }
        }
    }

    /**
     * Replaces document present into MongoDB
     * @param name      : name of the suite, that will be replaced 
     * @param document  : new document that will replace present document into MongoDB
     */
    @Override
    public void replaceDocument( String name, Document document, String mongoCollection )
    {
        MongoClient mongoClient = null;
        try
        {
            /*
             * Get the mongoclient connected to specified host
             */
            mongoClient = new MongoClient( m_parameter.getHost(), m_parameter.getPort() );

            /*
             * Get the required MongoDB database and collection
             */
            MongoDatabase database = mongoClient.getDatabase( m_parameter.getDatabase() );
            MongoCollection< Document > collection = database.getCollection( mongoCollection );

            /*
             * Parse and insert document into mongo collection
             */
            Document filter = new Document( NAME, name );
            collection.replaceOne( filter, document );
        }
        finally
        {
            if( mongoClient != null )
            {
                mongoClient.close();
            }
        }
    }

    /**
     * Remove MongoDB document by suiteName
     * @param name   : remove the suite document from the MongoDB
     */
    @Override
    public void removeDocument( String name, String mongoCollection )
    {
        MongoClient mongoClient = null;

        try
        {
            /*
             * Get the mongoclient connected to specified host
             */
            mongoClient = new MongoClient( m_parameter.getHost(), m_parameter.getPort() );

            /*
             * Get the required MongoDB database and collection
             */
            MongoDatabase database = mongoClient.getDatabase( m_parameter.getDatabase() );
            MongoCollection< Document > collection = database.getCollection( mongoCollection );

            collection.deleteMany( eq( NAME, name ) );
        }
        finally
        {
            if( mongoClient != null )
            {
                mongoClient.close();
            }
        }
    }

    @Override
    public List< Document > getScenarios( String suiteId )
    {
        ArrayList< Document > documents = null;
        MongoClient mongoClient = null;

        try
        {
            /*
             * Get the mongoclient connected to specified host
             */
            mongoClient = new MongoClient( m_parameter.getHost(), m_parameter.getPort() );

            /*
             * Get the required MongoDB database and collection
             */
            MongoDatabase database = mongoClient.getDatabase( m_parameter.getDatabase() );
            MongoCollection< Document > collection = database.getCollection( COLLECTION_TEST_SCEANRIO );

            /*
             * Get the list of Documents with matching suiteId in test_scenario collection
             */
            documents = collection.find( new BasicDBObject( SUITE_ID, suiteId ) ).into( new ArrayList< Document >() );
        }
        finally
        {
            if( mongoClient != null )
            {
                mongoClient.close();
            }
        }
        return documents;

    }

    @Override
    public List< ArrayList< Document > > getTestCases( String scenarioId )
    {
        List< ArrayList< Document > > testCases = new ArrayList< ArrayList< Document > >();
        MongoClient mongoClient = null;

        try
        {
            /*
             * Get the mongoclient connected to specified host
             */
            mongoClient = new MongoClient( m_parameter.getHost(), m_parameter.getPort() );

            /*
             * Get the required MongoDB database and collection
             */
            MongoDatabase database = mongoClient.getDatabase( m_parameter.getDatabase() );
            MongoCollection< Document > collection = database.getCollection( COLLECTION_TEST_CASE );

            /*
             * Get the list of Documents with matching suiteId in test_scenario collection
             */
            ArrayList< Document > documents = collection.find( new BasicDBObject( SCENARIO_ID, scenarioId ) ).into( new ArrayList< Document >() );
            testCases.add( documents );
        }
        finally
        {
            if( mongoClient != null )
            {
                mongoClient.close();
            }
        }
        return testCases;
    }
}

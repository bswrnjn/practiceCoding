package jp.co.rakuten.travel.framework.database;

import java.util.Map;

import jp.co.rakuten.travel.MongoData.Collection;
import jp.co.rakuten.travel.MongoData.MongoCollectionFactory;
import jp.co.rakuten.travel.framework.configuration.Configuration;
import jp.co.rakuten.travel.framework.configuration.Equipment.EquipmentType;
import jp.co.rakuten.travel.framework.parameter.TestApiObject;
import jp.co.rakuten.travel.framework.parameter.TestApiParameters;
import jp.co.rakuten.travel.framework.parameter.WebDomain;

import com.mongodb.client.MongoDatabase;

/**
 * Controller returning dictionary map of a specific domain
 *
 */
public class DictionaryControllerImpl implements DictionaryController
{
    @Override
    public Dictionary get( WebDomain domain )
    {
        @SuppressWarnings( "unchecked" )
        Connection< MongoDatabase > db = (Connection< MongoDatabase >)Configuration.instance().equipment( EquipmentType.DICT_DB );

        MongoCollectionFactory factory = new MongoCollectionFactory();
        @SuppressWarnings( "rawtypes" )
        Collection< Map > collection = factory.get( db.connection(), TestApiObject.instance().get( TestApiParameters.API_DICT_COLLECTION ), Map.class );

        return new DictionaryImpl( collection, domain );
    }
}

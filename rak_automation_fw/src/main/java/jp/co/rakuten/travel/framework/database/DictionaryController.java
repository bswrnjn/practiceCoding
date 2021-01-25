package jp.co.rakuten.travel.framework.database;

import jp.co.rakuten.travel.framework.configuration.Controller;
import jp.co.rakuten.travel.framework.parameter.WebDomain;

/**
 * Interface returning Dictionary of a specific domain  
 *
 */
public interface DictionaryController extends Controller
{
    Dictionary get( WebDomain domain );
}

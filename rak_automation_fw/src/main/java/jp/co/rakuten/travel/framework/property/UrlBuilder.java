package jp.co.rakuten.travel.framework.property;

import jp.co.rakuten.travel.framework.parameter.Parameters;

public interface UrlBuilder< T extends Parameters >
{
    /**
     * Generic literals used in the URL. 
     */
    String URL_SEPARATOR   = "/";
    String HYPHEN          = "-";
    String BEGIN_ARGUMENTS = "?";
    String EQUALS          = "=";
    String AND             = "&";
    String UNDERSCORE      = "_";
    String COMMA           = ",";

    /**
     * 
     * @param searchResults
     * @return
     */
    String build( T searchResults );
}

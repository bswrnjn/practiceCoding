package jp.co.rakuten.travel.framework.http;

import java.text.ParseException;

import org.apache.http.HttpResponse;

/**
 * 
 * @param <T> actual request object
 * @param <U> actual response object
 */
public interface HttpMessage< T extends Object, U extends Object >
{
    T request();

    U response();

    void setResponse( HttpResponse response );

    String req() throws ParseException;

    String resp() throws ParseException;

    /**
     * to check successful default response
     * @return
     */
    boolean validate( U responseRef );

    /**
     * to check successful default response with internal message
     * @return
     */
    boolean validate();
}

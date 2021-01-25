package jp.co.rakuten.travel.framework.rest;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;

import jp.co.rakuten.travel.framework.http.HttpMessage;

public class RConnectRestControllerImpl extends RestControllerImpl implements RConnectRestController< HttpResponse >
{

    public < U extends Object, V extends Object > HttpResponse patch( HttpMessage< U, V > msg, URI uri ) throws IOException
    {
        LOG.info( "patch " + uri.getHost() + ":" + uri.getPort() );

        HttpPatch patch = new HttpPatch( uri.toString() );
        for( String headerKey : m_headers.keySet() )
        {
            patch.setHeader( headerKey, m_headers.get( headerKey ) );
        }
        HttpEntity entity;
        try
        {
            entity = new StringEntity( msg.req(), "UTF-8" );
        }
        catch( java.text.ParseException e )
        {
            throw new ParseException( e.getMessage() );
        }
        patch.setEntity( entity );

        printRequest( patch, entity );
        HttpResponse response = m_http.client().execute( patch );
        printResponse( response );
        return response;
    }
}

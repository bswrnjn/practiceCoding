package jp.co.rakuten.travel.framework.rest;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import jp.co.rakuten.travel.framework.configuration.Configuration;
import jp.co.rakuten.travel.framework.configuration.Equipment.EquipmentType;
import jp.co.rakuten.travel.framework.http.HttpMessage;
import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.parameter.TestApiObject;
import jp.co.rakuten.travel.framework.parameter.TestApiParameters;

public class RestControllerImpl implements RestController< HttpResponse >
{
    protected Logger                LOG       = TestLogger.getLogger( this.getClass() );

    protected Map< String, String > m_headers = new HashMap<>();

    protected Http                  m_http    = (HttpImpl)Configuration.instance().equipment( EquipmentType.HTTP );

    @Override
    public < U extends Object, V extends Object > HttpResponse post( HttpMessage< U, V > msg, URI uri ) throws IOException
    {
        LOG.info( "post " + uri );

        HttpHost target = new HttpHost( uri.getHost(), uri.getPort(), m_http.scheme() );

        HttpPost post = new HttpPost( uri );
        for( String headerKey : m_headers.keySet() )
        {
            post.setHeader( headerKey, m_headers.get( headerKey ) );
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
        post.setEntity( entity );

        printRequest( post, entity );
        post.setConfig(
                RequestConfig.custom().setProxy( new HttpHost( TestApiObject.instance().get( TestApiParameters.API_HTTP_PROXY ), Integer.valueOf( TestApiObject.instance().get( TestApiParameters.API_HTTP_PROXY_PORT ) ), "HTTP" ) ).build() );
        HttpResponse response = m_http.client().execute( target, post );
        printResponse( response );

        return response;
    }

    @Override
    public HttpResponse get( URI uri ) throws IOException
    {
        LOG.info( "get " + uri.getHost() + ":" + uri.getPort() );

        HttpGet get = new HttpGet( uri.toString() );

        for( String headerKey : m_headers.keySet() )
        {
            get.setHeader( headerKey, m_headers.get( headerKey ) );
        }

        return m_http.client().execute( get );
    }

    @Override
    public < U extends Object, V extends Object > HttpResponse put( HttpMessage< U, V > msg, URI uri ) throws IOException
    {
        LOG.info( "put " + uri.getHost() + ":" + uri.getPort() );

        HttpPut put = new HttpPut( uri.toString() );
        for( String headerKey : m_headers.keySet() )
        {
            put.setHeader( headerKey, m_headers.get( headerKey ) );
        }
        HttpEntity entity;
        try
        {
            entity = new StringEntity( msg.req() );
        }
        catch( java.text.ParseException e )
        {
            throw new ParseException( e.getMessage() );
        }
        put.setEntity( entity );

        return m_http.client().execute( put );
    }

    @Override
    public < U extends Object, V extends Object > HttpResponse delete( HttpMessage< U, V > msg, URI uri ) throws IOException
    {
        LOG.info( "delete " + uri.getHost() + ":" + uri.getPort() );

        HttpDelete delete = new HttpDelete( uri.toString() );

        for( String headerKey : m_headers.keySet() )
        {
            delete.setHeader( headerKey, m_headers.get( headerKey ) );
        }

        HttpResponse response = m_http.client().execute( delete );

        return response;
    }

    @Override
    public void setHeaders( Map< String, String > headers )
    {
        m_headers.putAll( headers );
    }

    protected void printRequest( final HttpRequestBase request, HttpEntity entity ) throws IOException
    {
        LOG.info( "REQUEST" );
        LOG.info( "Request line : " + request.getRequestLine() );
        Header [] headers = request.getAllHeaders();
        for( Header header : headers )
        {
            LOG.info( "HEADER : " + header.getName() + " : " + header.getValue() );
            for( HeaderElement element : header.getElements() )
            {
                for( NameValuePair pair : element.getParameters() )
                {
                    LOG.info( "ELEMENT : " + pair.getName() + " : " + pair.getValue() );
                }
            }
        }
        LOG.info( "CONTENT LENGTH: " + entity.getContentLength() + "\r\n" + EntityUtils.toString( entity ) );

    }

    protected void printResponse( HttpResponse response ) throws IOException
    {
        LOG.info( "RESPONSE" );
        LOG.info( "Response line : " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase() );
        Header [] headers = response.getAllHeaders();
        for( Header header : headers )
        {
            LOG.info( "HEADER : " + header.getName() + " : " + header.getValue() );
            for( HeaderElement element : header.getElements() )
            {
                for( NameValuePair pair : element.getParameters() )
                {
                    LOG.info( "ELEMENT : " + pair.getName() + " : " + pair.getValue() );
                }
            }
        }
        LOG.info( "Status Line    : " + response.getStatusLine() );
        LOG.info( "Status Code    : " + response.getStatusLine().getStatusCode() );
        LOG.info( "Reason Phrase  : " + response.getStatusLine().getReasonPhrase() );
        LOG.info( "Protocol       : " + response.getStatusLine().getProtocolVersion() );
    }
}

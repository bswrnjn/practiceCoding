package jp.co.rakuten.travel.framework.rest;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import jp.co.rakuten.travel.framework.configuration.Equipment;
import jp.co.rakuten.travel.framework.logger.TestLogger;
import jp.co.rakuten.travel.framework.parameter.TestApiObject;
import jp.co.rakuten.travel.framework.parameter.TestApiParameters;
import jp.co.rakuten.travel.framework.utility.Utility;

public class HttpImpl implements Equipment, Http
{
    protected Logger                LOG       = TestLogger.getLogger( this.getClass() );

    protected CloseableHttpClient   m_client;

    private String                  m_scheme  = "";

    protected Map< String, String > m_headers = new HashMap<>();

    @Override
    public void init()
    {
        LOG.info( "init" );

        boolean withSSL = TestApiObject.instance().bool( TestApiParameters.API_WITH_SSL );

        if( withSSL )
        {
            m_scheme = "https";
            initWithSSL();
        }
        else
        {
            m_scheme = "http";
            m_client = HttpClients.createDefault();
        }
    }

    private void initWithSSL()
    {
        try
        {
            SSLContext ssl = initSSLContext();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory( ssl, new String []
            { "TLSv1" }, null, hostnameVerifier() );

            m_client = HttpClientBuilder //
                    .create()
                    .setSSLContext( ssl )
                    .setSSLSocketFactory( sslsf )
                    .setProxy( new HttpHost( TestApiObject.instance().get( TestApiParameters.API_PROXY_HOST ), Utility.getInt( TestApiObject.instance().get( TestApiParameters.API_PROXY_PORT ) ) ) )
                    .disableCookieManagement()
                    // .setRoutePlanner( routePlanner )
                    // .setKeepAliveStrategy( myStrategy )
                    // .setSchemePortResolver( scheme )
                    // .setHostnameVerifier( SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER )
                    // .setProxyAuthenticationStrategy( AuthenticationStrategy )
                    // .setDefaultCredentialsProvider()
                    // .setConnectionReuseStrategy( new RestConnectionReuseStrategy( this ) )
                    .build();
        }
        catch( KeyManagementException | NoSuchAlgorithmException e )
        {
            throw new IllegalStateException( e.getClass().getSimpleName() + " Exception found. HttpClient builder encountered unknown problem. Message : " + e.getMessage(), e );
        }
    }

    private SSLContext initSSLContext() throws NoSuchAlgorithmException, KeyManagementException
    {
        SSLContext ssl = SSLContext.getInstance( "SSL" );
        X509TrustManager [] tm = new X509TrustManager []
        { new X509TrustManager()
        {
            @Override
            public void checkClientTrusted( X509Certificate [] chain, String authType ) throws CertificateException
            {}

            @Override
            public void checkServerTrusted( X509Certificate [] chain, String authType ) throws CertificateException
            {}

            @Override
            public X509Certificate [] getAcceptedIssuers()
            {
                return null;
            }
        } };
        ssl.init( null, tm, new java.security.SecureRandom() /* null */ );
        HttpsURLConnection.setDefaultHostnameVerifier( hostnameVerifier() );
        HttpsURLConnection.setDefaultSSLSocketFactory( ssl.getSocketFactory() );
        return ssl;
    }

    private HostnameVerifier hostnameVerifier()
    {
        return new HostnameVerifier()
        {
            @Override
            public boolean verify( String arg0, SSLSession arg1 )
            {
                return true;
            }
        };
    }

    @Override
    public void release()
    {
        LOG.info( "release" );
        try
        {
            m_client.close();
        }
        catch( IOException e )
        {
            LOG.warn( e.getClass().getSimpleName() + " found with message " + e.getMessage() );
        }
    }

    @Override
    public void recover()
    {
        LOG.info( "recover" );

        release();
        init();
    }

    @Override
    public void refresh()
    {}

    @Override
    public void errorInfo()
    {}

    @Override
    public CloseableHttpClient client()
    {
        return m_client;
    }

    @Override
    public String scheme()
    {
        return m_scheme;
    }

}

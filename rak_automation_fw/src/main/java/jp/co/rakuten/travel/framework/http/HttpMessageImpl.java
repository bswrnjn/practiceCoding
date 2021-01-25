package jp.co.rakuten.travel.framework.http;

public abstract class HttpMessageImpl< T extends Object, U extends Object > implements HttpMessage< T, U >
{
    protected T m_request;
    protected U m_response;

    @Override
    public T request()
    {
        return m_request;
    }

    @Override
    public U response()
    {
        if( m_response == null )
        {
            throw new NullPointerException( "Response NOT set" );
        }

        return m_response;
    }

}

package jp.co.rakuten.travel.framework.rest;

import org.apache.http.impl.client.CloseableHttpClient;

public interface Http
{
    CloseableHttpClient client();

    String scheme();
}

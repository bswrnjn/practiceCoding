package jp.co.rakuten.travel.framework.rest;

import java.io.IOException;
import java.net.URI;

import jp.co.rakuten.travel.framework.http.HttpMessage;

public interface RConnectRestController< T > extends RestController< T >
{
    /**
     * 
     * <br>The PATCH method requests that a set of changes described in the request entity be applied to the resource identified by the Request- URI. 
     * <br>Differs from the PUT method in the way the server processes the enclosed entity to modify the resource identified by the Request-URI.
     * <br>In a PUT request, the enclosed entity origin server, and the client is requesting that the stored version be replaced. 
     * <br>With PATCH, however, the enclosed entity contains a set of instructions describing how a resource currently residing on the origin server should be modified to produce a new version.
     * @param msg
     * @param uri
     * @return
     * @throws IOException
     */
    < U extends Object, V extends Object > T patch( HttpMessage< U, V > msg, URI uri ) throws IOException;
}

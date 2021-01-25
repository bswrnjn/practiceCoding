package jp.co.rakuten.travel.framework.rest;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import jp.co.rakuten.travel.framework.configuration.Controller;
import jp.co.rakuten.travel.framework.http.HttpMessage;

/**
 * 
 * @param <T>
 */
public interface RestController< T > extends Controller
{
    /**
     * 
     * <br>The POST verb is most-often utilized to **create** new resources. In particular, it's used to create subordinate resources. That is, subordinate to some other (e.g. parent) resource. In other words, when creating a new resource, POST to the parent and the service takes care of associating the new resource with the parent, assigning an ID (new resource URI), etc.
     * <br>On successful creation, return HTTP status 201, returning a Location header with a link to the newly-created resource with the 201 HTTP status.
     * <br>POST is neither safe nor idempotent. It is therefore recommended for non-idempotent resource requests. Making two identical POST requests will most-likely result in two resources containing the same information.
     * <br>Examples:
     * <br>POST http://www.example.com/customers
     * <br>POST http://www.example.com/customers/12345/orders
     * @param msg
     * @param uri
     * @return
     * @throws ParseException
     * @throws ClientProtocolException
     * @throws IOException
     */
    < U extends Object, V extends Object > T post( HttpMessage< U, V > msg, URI uri ) throws IOException;

    /**
     * 
     * <br>The HTTP GET method is used to **read** (or retrieve) a representation of a resource. In the “happy” (or non-error) path, GET returns a representation in XML or JSON and an HTTP response code of 200 (OK). In an error case, it most often returns a 404 (NOT FOUND) or 400 (BAD REQUEST).
     * <br>According to the design of the HTTP specification, GET (along with HEAD) requests are used only to read data and not change it. Therefore, when used this way, they are considered safe. That is, they can be called without risk of data modification or corruption—calling it once has the same effect as calling it 10 times, or none at all. Additionally, GET (and HEAD) is idempotent, which means that making multiple identical requests ends up having the same result as a single request.
     * <br>Do not expose unsafe operations via GET—it should never modify any resources on the server.
     * <br>Examples:
     * <br>GET http://www.example.com/customers/12345
     * <br>GET http://www.example.com/customers/12345/orders
     * <br>GET http://www.example.com/buckets/sample
     * @param uri
     * @return
     * @throws ParseException
     * @throws ClientProtocolException
     * @throws IOException
     */
    T get( URI uri ) throws IOException;

    /**
     * 
     * <br>PUT is most-often utilized for **update** capabilities, PUT-ing to a known resource URI with the request body containing the newly-updated representation of the original resource.
     * <br>However, PUT can also be used to create a resource in the case where the resource ID is chosen by the client instead of by the server. In other words, if the PUT is to a URI that contains the value of a non-existent resource ID. Again, the request body contains a resource representation. Many feel this is convoluted and confusing. Consequently, this method of creation should be used sparingly, if at all.
     * <br>Alternatively, use POST to create new resources and provide the client-defined ID in the body representation—presumably to a URI that doesn't include the ID of the resource (see POST below).
     * <br>On successful update, return 200 (or 204 if not returning any content in the body) from a PUT. If using PUT for create, return HTTP status 201 on successful creation. A body in the response is optional—providing one consumes more bandwidth. It is not necessary to return a link via a Location header in the creation case since the client already set the resource ID.
     * <br>PUT is not a safe operation, in that it modifies (or creates) state on the server, but it is idempotent. In other words, if you create or update a resource using PUT and then make that same call again, the resource is still there and still has the same state as it did with the first call.
     * <br>If, for instance, calling PUT on a resource increments a counter within the resource, the call is no longer idempotent. Sometimes that happens and it may be enough to document that the call is not idempotent. However, it's recommended to keep PUT requests idempotent. It is strongly recommended to use POST for non-idempotent requests.
     * <br>Examples:
     * <br>PUT http://www.example.com/customers/12345
     * <br>PUT http://www.example.com/customers/12345/orders/98765
     * <br>PUT http://www.example.com/buckets/secret_stuff
     * @param msg
     * @param uri
     * @return
     * @throws ParseException
     * @throws ClientProtocolException
     * @throws IOException
     */
    < U extends Object, V extends Object > T put( HttpMessage< U, V > msg, URI uri ) throws IOException;

    /**
     * 
     * <br>DELETE is pretty easy to understand. It is used to **delete** a resource identified by a URI.
     * <br>On successful deletion, return HTTP status 200 (OK) along with a response body, perhaps the representation of the deleted item (often demands too much bandwidth), or a wrapped response (see Return Values below). Either that or return HTTP status 204 (NO CONTENT) with no response body. In other words, a 204 status with no body, or the JSEND-style response and HTTP status 200 are the recommended responses.
     * <br>HTTP-spec-wise, DELETE operations are idempotent. If you DELETE a resource, it's removed. Repeatedly calling DELETE on that resource ends up the same: the resource is gone. If calling DELETE say, decrements a counter (within the resource), the DELETE call is no longer idempotent. As mentioned previously, usage statistics and measurements may be updated while still considering the service idempotent as long as no resource data is changed. Using POST for non-idempotent resource requests is recommended.
     * <br>There is a caveat about DELETE idempotence, however. Calling DELETE on a resource a second time will often return a 404 (NOT FOUND) since it was already removed and therefore is no longer findable. This, by some opinions, makes DELETE operations no longer idempotent, however, the end-state of the resource is the same. Returning a 404 is acceptable and communicates accurately the status of the call.
     * <br>Examples:
     * <br>DELETE http://www.example.com/customers/12345
     * <br>DELETE http://www.example.com/customers/12345/orders
     * <br>DELETE http://www.example.com/bucket/sample
     * @param msg
     * @param uri
     * @return
     * @throws ParseException
     * @throws ClientProtocolException
     * @throws IOException
     */
    < U extends Object, V extends Object > T delete( HttpMessage< U, V > msg, URI uri ) throws IOException;

    /**
     * <br>Setting of headers to be used byt the REST command
     * @param headers map of headers
     */
    void setHeaders( Map< String, String > headers );

}

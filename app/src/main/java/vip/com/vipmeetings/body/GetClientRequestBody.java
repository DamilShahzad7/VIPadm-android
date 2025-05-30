package vip.com.vipmeetings.body;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.GetClient;


/**
 * Created by Srinath on 26/03/18.
 */
@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetClientRequestBody {


    @Element(name = "GetClient", required = false)
    private GetClient getClient;

    public GetClient getGetClient() {
        return getClient;
    }

    public void setGetClient(GetClient getClient) {
        this.getClient = getClient;
    }
}

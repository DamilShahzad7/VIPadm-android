package vip.com.vipmeetings.body;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.GetAuthTokenData;

/**
 * Created by Srinath on 26/03/18.
 */
@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetAuthTokenRequestBody {


    @Element(name = "GetAuthToken",required = false)
    private GetAuthTokenData getAuthTokenData;

    public GetAuthTokenData getGetAuthTokenData() {
        return getAuthTokenData;
    }

    public void setGetAuthTokenData(GetAuthTokenData getAuthTokenData) {
        this.getAuthTokenData = getAuthTokenData;
    }
}

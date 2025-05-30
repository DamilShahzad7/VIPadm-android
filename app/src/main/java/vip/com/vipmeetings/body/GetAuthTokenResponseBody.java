package vip.com.vipmeetings.body;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.GetAuthTokenResult;


/**
 * Created by Srinath on 26/03/18.
 */
@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetAuthTokenResponseBody {


    @Element(name = "GetAuthTokenResponse", required = false)
    private GetAuthTokenResult getAuthTokenResult;


    public GetAuthTokenResult getGetAuthTokenResult() {
        return getAuthTokenResult;
    }

    public void setGetAuthTokenResult(GetAuthTokenResult getAuthTokenResult) {
        this.getAuthTokenResult = getAuthTokenResult;
    }
}

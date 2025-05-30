package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.models.Empty;
import vip.com.vipmeetings.models.Error;

@Root(name = "GetAuthTokenResponse", strict = false)
public class GetAuthTokenResult {


    @Element(name = "Error", required = false)
    private vip.com.vipmeetings.models.Error Error;

    @Element(name = "Empty", required = false)
    private Empty empty;


    @Element(name = "GetAuthTokenResult", required = false)
    private String GetAuthTokenResult;

    public String getGetAuthTokenResult() {
        return GetAuthTokenResult;
    }

    public void setGetAuthTokenResult(String getAuthTokenResult) {
        GetAuthTokenResult = getAuthTokenResult;
    }

    public vip.com.vipmeetings.models.Error getError() {
        return Error;
    }

    public void setError(vip.com.vipmeetings.models.Error error) {
        Error = error;
    }

    public Empty getEmpty() {
        return empty;
    }

    public void setEmpty(Empty empty) {
        this.empty = empty;
    }
}

package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetClientUrlRequest", strict = false)
public class GetClientUrlRequest {


    @Element(name = "sClientId", required = false)
    private String sClientId;
    @Element(name = "authToken", required = false)
    private String  authToken;

    public String getsClientId() {
        return sClientId;
    }

    public void setsClientId(String sClientId) {
        this.sClientId = sClientId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

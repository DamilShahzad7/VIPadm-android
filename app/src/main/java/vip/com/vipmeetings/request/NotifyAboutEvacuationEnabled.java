package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "NotifyAboutEvacuationEnabled", strict = false)
public class NotifyAboutEvacuationEnabled {

    @Element(name = "A_iPlaceId", required = false)
    private String A_iPlaceId;

    @Element(name = "authToken", required = false)
    private String authToken;

    public String getA_iPlaceId() {
        return A_iPlaceId;
    }

    public void setA_iPlaceId(String a_iPlaceId) {
        A_iPlaceId = a_iPlaceId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

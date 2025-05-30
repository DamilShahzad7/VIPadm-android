package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "NotifyAboutAreaClearence", strict = false)
public class NotifyAboutAreaClearence {


    @Element(name = "authToken", required = false)
    private String authToken;

    @Element(name = "A_iPlaceId", required = false)
    private String A_iPlaceId;

    @Element(name = "A_sEvacuationIds", required = false)
    private String A_sEvacuationIds;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getA_iPlaceId() {
        return A_iPlaceId;
    }

    public void setA_iPlaceId(String a_iPlaceId) {
        A_iPlaceId = a_iPlaceId;
    }

    public String getA_sEvacuationIds() {
        return A_sEvacuationIds;
    }

    public void setA_sEvacuationIds(String a_sEvacuationIds) {
        A_sEvacuationIds = a_sEvacuationIds;
    }
}



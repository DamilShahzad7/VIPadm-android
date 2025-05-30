package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetEvacuationStatus", strict = false)

public class GetEvacuationStatus {


    @Element(name = "A_sClientId", required = false)
    private String A_sClientId;

    @Element(name = "A_sPlaceId", required = false)
    private String A_sPlaceId;

    @Element(name = "authToken", required = false)
    private String authToken;


    public String getA_sClientId() {
        return A_sClientId;
    }

    public void setA_sClientId(String a_sClientId) {
        A_sClientId = a_sClientId;
    }

    public String getA_sPlaceId() {
        return A_sPlaceId;
    }

    public void setA_sPlaceId(String a_sPlaceId) {
        A_sPlaceId = a_sPlaceId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

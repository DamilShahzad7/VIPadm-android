package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetPlaceID", strict = false)
public class GetPlaceIDRequest {



    @Element(name = "A_sClientId", required = false)
    private String A_sClientId;
    @Element(name = "A_sPlaceName", required = false)
    private String A_sPlaceName;

    @Element(name = "authToken", required = false)
    private String authToken;


    public String getA_sClientId() {
        return A_sClientId;
    }

    public void setA_sClientId(String a_sClientId) {
        A_sClientId = a_sClientId;
    }

    public String getA_sPlaceName() {
        return A_sPlaceName;
    }

    public void setA_sPlaceName(String a_sPlaceName) {
        A_sPlaceName = a_sPlaceName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetEvacuation", strict = false)
public class GetEvacuationInformationRequest {


    @Element(name = "A_sCity", required = false)
    private String A_sCity;

    @Element(name = "authToken", required = false)
    private String authToken;

    public String getA_sCity() {       return A_sCity;    }
    public void setA_sCity(String a_sCity) {
        A_sCity = a_sCity;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

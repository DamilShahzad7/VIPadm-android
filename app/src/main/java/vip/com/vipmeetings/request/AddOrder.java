package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "AddOrder", strict = false)
public class AddOrder {


    @Element(name = "A_sMeetingId", required = false)
    private String A_sMeetingId;

    @Element(name = "authToken", required = false)
    private String authToken;

    @Element(name = "A_sProductData", required = false)
    private String A_sProductData;


    public String getA_sMeetingId() {
        return A_sMeetingId;
    }

    public void setA_sMeetingId(String a_sMeetingId) {
        A_sMeetingId = a_sMeetingId;
    }

    public String getA_sProductData() {
        return A_sProductData;
    }

    public void setA_sProductData(String a_sProductData) {
        A_sProductData = a_sProductData;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

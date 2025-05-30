package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetProducts", strict = false)
public class GetProducts {


    @Element(name = "A_sMeetingId", required = false)
    private String A_sMeetingId;

    @Element(name = "authToken", required = false)
    private String authToken;

    @Element(name = "A_sCategoryId", required = false)
    private String A_sCategoryId;

    @Element(name = "A_sCanteenId", required = false)
    private String A_sCanteenId;

    public String getA_sMeetingId() {
        return A_sMeetingId;
    }

    public void setA_sMeetingId(String a_sMeetingId) {
        A_sMeetingId = a_sMeetingId;
    }

    public String getA_sCategoryId() {
        return A_sCategoryId;
    }

    public void setA_sCategoryId(String a_sCategoryId) {
        A_sCategoryId = a_sCategoryId;
    }

    public String getA_sCanteenId() {
        return A_sCanteenId;
    }

    public void setA_sCanteenId(String a_sCanteenId) {
        A_sCanteenId = a_sCanteenId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

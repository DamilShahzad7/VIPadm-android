package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetMeetings", strict = false)
public class GetMeetingsRequest {


    @Element(name = "A_sUserEmail", required = false)
    private String A_sUserEmail;



    @Element(name = "authToken", required = false)
    private String authToken;


    public String getA_sUserEmail() {
        return A_sUserEmail;
    }

    public void setA_sUserEmail(String a_sUserEmail) {
        A_sUserEmail = a_sUserEmail;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

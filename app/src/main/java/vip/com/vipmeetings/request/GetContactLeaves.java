package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetContactLeaves", strict = false)
public class GetContactLeaves {


    @Element(name = "A_sContactEmail", required = false)
    private String A_sContactEmail;

    @Element(name = "authToken", required = false)
    private String authToken;

    public String getA_sContactEmail() {
        return A_sContactEmail;
    }

    public void setA_sContactEmail(String a_sContactEmail) {
        A_sContactEmail = a_sContactEmail;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetFormerVisitors", strict = false)
public class GetFormerVisitors {


    @Element(name = "A_sHostMail", required = false)
    private String A_sHostMail;


    @Element(name = "authToken", required = false)
    private String authToken;

    public String getA_sHostMail() {
        return A_sHostMail;
    }

    public void setA_sHostMail(String a_sHostMail) {
        A_sHostMail = a_sHostMail;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

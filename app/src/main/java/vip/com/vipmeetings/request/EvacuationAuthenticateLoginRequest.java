package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "AuthenticateLogin", strict = false)
public class EvacuationAuthenticateLoginRequest {


    @Element(name = "A_sClientId", required = false)
    private String A_sClientId;
    @Element(name = "authToken", required = false)
    private String authToken;
    @Element(name = "A_sMobileOrMail", required = false)
    private String A_sMobileOrMail;

    public String getA_sClientId() {
        return A_sClientId;
    }

    public void setA_sClientId(String a_sClientId) {
        A_sClientId = a_sClientId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getA_sMobileOrMail() {
        return A_sMobileOrMail;
    }

    public void setA_sMobileOrMail(String a_sMobileOrMail) {
        A_sMobileOrMail = a_sMobileOrMail;
    }
}

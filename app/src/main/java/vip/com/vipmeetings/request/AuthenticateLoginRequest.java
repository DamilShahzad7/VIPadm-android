package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "AuthenticateLogin", strict = false)
public class AuthenticateLoginRequest {


    @Element(name = "A_sEmail", required = false)
    private String A_sEmail;
    @Element(name = "authToken", required = false)
    private String authToken;
//    @Element(name = "clientid", required = false)
//    private String clientid;

    public String getA_sEmail() {
        return A_sEmail;
    }

    public void setA_sEmail(String a_sEmail) {
        A_sEmail = a_sEmail;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

//    public String getClientid() {
//        return clientid;
//    }
//
//    public void setClientid(String clientid) {
//        this.clientid = clientid;
//    }
}

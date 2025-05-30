package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "AuthenticateLoginResponse", strict = false)
//@Namespace(reference = "http://tempuri.org/")
public class AuthenticateLoginResponse {


    @Element(name = "AuthenticateLoginResult", required = false)
    private AuthenticateLoginResult authenticateLoginResult;

    public AuthenticateLoginResult getAuthenticateLoginResult() {
        return authenticateLoginResult;
    }

    public void setAuthenticateLoginResult(AuthenticateLoginResult authenticateLoginResult) {
        this.authenticateLoginResult = authenticateLoginResult;

    }
}

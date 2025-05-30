package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "AuthenticateLoginResponse", strict = false)
//@Namespace(reference = "http://tempuri.org/")
public class EvacuationAuthenticateLoginResponse {


    @Element(name = "AuthenticateLoginResult", required = false)
    private EvacuationAuthenticateLoginResult authenticateLoginResult;

    public EvacuationAuthenticateLoginResult getAuthenticateLoginResult() {
        return authenticateLoginResult;
    }

    public void setAuthenticateLoginResult(EvacuationAuthenticateLoginResult authenticateLoginResult) {
        this.authenticateLoginResult = authenticateLoginResult;
    }
}

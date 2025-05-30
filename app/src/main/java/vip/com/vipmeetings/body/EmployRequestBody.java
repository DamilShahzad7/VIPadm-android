package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.AuthenticateLoginRequest;
import vip.com.vipmeetings.request.GetClientUrlRequest;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class EmployRequestBody {



    @Element(name = "AuthenticateLogin", required = false)
    private AuthenticateLoginRequest authenticateLoginRequest;

    public AuthenticateLoginRequest getAuthenticateLoginRequest() {
        return authenticateLoginRequest;
    }

    public void setAuthenticateLoginRequest(AuthenticateLoginRequest authenticateLoginRequest) {
        this.authenticateLoginRequest = authenticateLoginRequest;
    }
}

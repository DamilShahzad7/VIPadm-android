package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.AuthenticateLoginRequest;
import vip.com.vipmeetings.request.EvacuationAuthenticateLoginRequest;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class EvacuationEmployRequestBody {



    @Element(name = "AuthenticateLogin", required = false)
    private EvacuationAuthenticateLoginRequest authenticateLoginRequest;

    public EvacuationAuthenticateLoginRequest getAuthenticateLoginRequest() {
        return authenticateLoginRequest;
    }

    public void setAuthenticateLoginRequest(EvacuationAuthenticateLoginRequest authenticateLoginRequest) {
        this.authenticateLoginRequest = authenticateLoginRequest;
    }
}

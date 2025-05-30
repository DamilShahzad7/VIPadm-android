package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.AuthenticateLoginResponse;
import vip.com.vipmeetings.response.EvacuationAuthenticateLoginResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class EvacuationEmployResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "AuthenticateLoginResponse", required = false)
    private EvacuationAuthenticateLoginResponse authenticateLoginResponse;

    public EvacuationAuthenticateLoginResponse getAuthenticateLoginResponse() {
        return authenticateLoginResponse;
    }

    public void setAuthenticateLoginResponse(EvacuationAuthenticateLoginResponse authenticateLoginResponse) {
        this.authenticateLoginResponse = authenticateLoginResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}

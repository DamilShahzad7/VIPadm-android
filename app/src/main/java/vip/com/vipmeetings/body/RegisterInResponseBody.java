package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.RegisterInResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class RegisterInResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    public RegisterInResponse getRegisterInResponse() {
        return registerInResponse;
    }

    public void setRegisterInResponse(RegisterInResponse registerInResponse) {
        this.registerInResponse = registerInResponse;
    }

    @Element(name = "RegisterInContactResponse", required = false)
    private RegisterInResponse registerInResponse;




}

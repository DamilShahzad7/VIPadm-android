package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.RegisterInRequest;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class RegisterInRequestBody {


    public RegisterInRequest getRegisterInRequest() {
        return registerInRequest;
    }

    public void setRegisterInRequest(RegisterInRequest registerInRequest) {
        this.registerInRequest = registerInRequest;
    }

    @Element(name = "RegisterInContact", required = false)
    private RegisterInRequest registerInRequest;


}

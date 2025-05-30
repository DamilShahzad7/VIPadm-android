package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.RegisterOutResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class RegisterOutResponseBody {





    public RegisterOutResponse getRegisterOutResponse() {
        return registerOutResponse;
    }

    public void setRegisterOutResponse(RegisterOutResponse registerOutResponse) {
        this.registerOutResponse = registerOutResponse;
    }

    @Element(name = "RegisterOutContactResponse", required = false)
    private RegisterOutResponse registerOutResponse;




}

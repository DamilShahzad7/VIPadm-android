package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.RegisterOutRequest;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class RegisterOutRequestBody {


    public RegisterOutRequest getRegisterOutRequest() {
        return registerOutRequest;
    }

    public void setRegisterOutRequest(RegisterOutRequest registerOutRequest) {
        this.registerOutRequest = registerOutRequest;
    }

    @Element(name = "RegisterOutContact", required = false)
    private RegisterOutRequest registerOutRequest;


}

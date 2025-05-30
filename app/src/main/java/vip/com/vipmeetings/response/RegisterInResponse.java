package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "RegisterInContactResponse", strict = false)
//@Namespace(reference = "http://tempuri.org/")
public class RegisterInResponse {


    public RegisterInResult getRegisterInResult() {
        return registerInResult;
    }

    public void setRegisterInResult(RegisterInResult RegisterInResult) {
        this.registerInResult = registerInResult;
    }

    @Element(name = "RegisterInContactResult", required = false)
    private RegisterInResult registerInResult;



}

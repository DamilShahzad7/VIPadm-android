package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "RegisterOutContactResponse", strict = false)
//@Namespace(reference = "http://tempuri.org/")
public class RegisterOutResponse {


    public RegisterOutResult getRegisterOutResult() {
        return registerOutResult;
    }

    public void setRegisterOutResult(RegisterOutResult registerOutResult) {
        this.registerOutResult = registerOutResult;
    }

    @Element(name = "RegisterOutContactResult", required = false)
    private RegisterOutResult registerOutResult;



}

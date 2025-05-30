package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "ValidateUserResponse", strict = false)
//@Namespace(reference = "http://tempuri.org/")
public class ValidateUserResponse {


    @Element(name = "ValidateUserResult", required = false)
    private ValidateUserResult validateUserResult;


    public ValidateUserResult getValidateUserResult() {
        return validateUserResult;
    }

    public void setValidateUserResult(ValidateUserResult validateUserResult) {
        this.validateUserResult = validateUserResult;
    }
}

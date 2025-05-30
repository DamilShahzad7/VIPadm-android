package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.ValidateUserResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class ValidateUserResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "ValidateUserResponse", required = false)
    private ValidateUserResponse validateUserResponse;

    public ValidateUserResponse getValidateUserResponse() {
        return validateUserResponse;
    }

    public void setValidateUserResponse(ValidateUserResponse validateUserResponse) {
        this.validateUserResponse = validateUserResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}

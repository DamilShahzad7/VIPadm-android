package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.GetLocationsRequest;
import vip.com.vipmeetings.request.ValidateUserRequest;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class ValidateUserRequestBody {



    @Element(name = "ValidateUser", required = false)
    private ValidateUserRequest validateUser;

    public ValidateUserRequest getValidateUser() {
        return validateUser;
    }

    public void setValidateUser(ValidateUserRequest validateUser) {
        this.validateUser = validateUser;
    }
}

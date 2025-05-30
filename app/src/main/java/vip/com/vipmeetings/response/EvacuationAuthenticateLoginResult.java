package vip.com.vipmeetings.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.models.AuthenticateEvacuationModel;
import vip.com.vipmeetings.models.Empty;

@Root(name = "AuthenticateLoginResult", strict = false)
public class EvacuationAuthenticateLoginResult {

    @Element(name = "Error", required = false)
    private vip.com.vipmeetings.models.Error Error;

    @Element(name = "Empty", required = false)
    private Empty empty;


    @Element(name = "EvacuationResponsible", required = false)
    private AuthenticateEvacuationModel Employee;


    public vip.com.vipmeetings.models.Error getError() {
        return Error;
    }

    public void setError(vip.com.vipmeetings.models.Error error) {
        Error = error;
    }

    public AuthenticateEvacuationModel getEmployee() {
        return Employee;
    }

    public void setEmployee(AuthenticateEvacuationModel employee) {
        Employee = employee;
    }

    public Empty getEmpty() {
        return empty;
    }

    public void setEmpty(Empty empty) {
        this.empty = empty;
    }
}

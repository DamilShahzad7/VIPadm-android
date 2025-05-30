package vip.com.vipmeetings.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.models.Employee;
import vip.com.vipmeetings.models.Empty;
import vip.com.vipmeetings.models.EvacuationResponsible;

@Root(name = "AuthenticateLoginResult", strict = false)
public class AuthenticateLoginResult {

    @Element(name = "Error", required = false)
    private vip.com.vipmeetings.models.Error Error;

    @Element(name = "Empty", required = false)
    private Empty empty;


    @Element(name = "Employee", required = false)
    private Employee Employee;

    @Element(name = "EvacuationResponsible", required = false)
    private EvacuationResponsible evacuationResponsible;

    public EvacuationResponsible getEvacuationResponsible() {
        return evacuationResponsible;
    }

    public void setEvacuationResponsible(EvacuationResponsible evacuationResponsible) {
        this.evacuationResponsible = evacuationResponsible;
    }

    public vip.com.vipmeetings.models.Error getError() {
        return Error;
    }

    public void setError(vip.com.vipmeetings.models.Error error) {
        Error = error;
    }

    public Employee getEmployee() {
        return Employee;
    }

    public void setEmployee(Employee employee) {
        Employee = employee;
    }

    public Empty getEmpty() {
        return empty;
    }

    public void setEmpty(Empty empty) {
        this.empty = empty;
    }
}

package vip.com.vipmeetings.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.models.Employee;
import vip.com.vipmeetings.models.Empty;

@Root(name = "RegisterInContactResult", strict = false)
public class RegisterInResult {

    @Element(name = "Error", required = false)
    private String Error;
    @Element(name = "status", required = false)
    private String STATUS;
    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }
}

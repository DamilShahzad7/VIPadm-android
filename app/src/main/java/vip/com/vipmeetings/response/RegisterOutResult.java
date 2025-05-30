package vip.com.vipmeetings.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "RegisterOutContactResult", strict = false)
public class RegisterOutResult {

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

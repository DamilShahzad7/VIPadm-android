package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "AddContactLeave", strict = false)
public class DeleteContactLeave {


    @Element(name = "A_sLeaveId", required = false)
    private String A_sLeaveId;

    @Element(name = "authToken", required = false)
    private String authToken;


    public String getA_sLeaveId() {
        return A_sLeaveId;
    }

    public void setA_sLeaveId(String a_sLeaveId) {
        A_sLeaveId = a_sLeaveId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "UpdateContactLeave", strict = false)
public class UpdateContactLeave {


    @Element(name = "A_sLeaveId", required = false)
    private String A_sLeaveId;


    @Element(name = "authToken", required = false)
    private String authToken;

    @Element(name = "A_sFromDateTimeYMD", required = false)
    private String A_sFromDateTimeYMD;

    @Element(name = "A_sToDateTimeYMD", required = false)
    private String A_sToDateTimeYMD;

    @Element(name = "A_sReasonForLeave", required = false)
    private String A_sReasonForLeave;

    public String getA_sLeaveId() {
        return A_sLeaveId;
    }

    public void setA_sLeaveId(String a_sLeaveId) {
        A_sLeaveId = a_sLeaveId;
    }

    public String getA_sFromDateTimeYMD() {
        return A_sFromDateTimeYMD;
    }

    public void setA_sFromDateTimeYMD(String a_sFromDateTimeYMD) {
        A_sFromDateTimeYMD = a_sFromDateTimeYMD;
    }

    public String getA_sToDateTimeYMD() {
        return A_sToDateTimeYMD;
    }

    public void setA_sToDateTimeYMD(String a_sToDateTimeYMD) {
        A_sToDateTimeYMD = a_sToDateTimeYMD;
    }

    public String getA_sReasonForLeave() {
        return A_sReasonForLeave;
    }

    public void setA_sReasonForLeave(String a_sReasonForLeave) {
        A_sReasonForLeave = a_sReasonForLeave;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

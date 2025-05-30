package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "AddContactLeave", strict = false)
public class AddContactLeave {


    @Element(name = "A_sContactEmail", required = false)
    private String A_sContactEmail;

    @Element(name = "authToken", required = false)
    private String authToken;

    @Element(name = "A_sFromDateTimeYMD", required = false)
    private String A_sFromDateTimeYMD;

    @Element(name = "A_sToDateTimeYMD", required = false)
    private String A_sToDateTimeYMD;

    @Element(name = "A_sReasonForLeave", required = false)
    private String A_sReasonForLeave;

    public String getA_sContactEmail() {
        return A_sContactEmail;
    }

    public void setA_sContactEmail(String a_sContactEmail) {
        A_sContactEmail = a_sContactEmail;
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

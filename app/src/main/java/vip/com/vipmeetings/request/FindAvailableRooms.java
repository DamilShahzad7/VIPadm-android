package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "FindAvailableRooms", strict = false)
public class FindAvailableRooms {


    @Element(name = "A_sCityCode", required = false)
    private String A_sCityCode;

    @Element(name = "A_sUserEmail", required = false)
    private String A_sUserEmail;

    @Element(name = "A_sFromDateYMD", required = false)
    private String A_sFromDateYMD;

    @Element(name = "A_sToDateYMD", required = false)
    private String A_sToDateYMD;

    @Element(name = "A_sFromTimeHM", required = false)
    private String A_sFromTimeHM;


    @Element(name = "A_sToTimeHM", required = false)
    private String A_sToTimeHM;



    @Element(name = "authToken", required = false)
    private String authToken;


    public String getA_sCityCode() {
        return A_sCityCode;
    }

    public void setA_sCityCode(String a_sCityCode) {
        A_sCityCode = a_sCityCode;
    }

    public String getA_sUserEmail() {
        return A_sUserEmail;
    }

    public void setA_sUserEmail(String a_sUserEmail) {
        A_sUserEmail = a_sUserEmail;
    }

    public String getA_sFromDateYMD() {
        return A_sFromDateYMD;
    }

    public void setA_sFromDateYMD(String a_sFromDateYMD) {
        A_sFromDateYMD = a_sFromDateYMD;
    }

    public String getA_sToDateYMD() {
        return A_sToDateYMD;
    }

    public void setA_sToDateYMD(String a_sToDateYMD) {
        A_sToDateYMD = a_sToDateYMD;
    }

    public String getA_sFromTimeHM() {
        return A_sFromTimeHM;
    }

    public void setA_sFromTimeHM(String a_sFromTimeHM) {
        A_sFromTimeHM = a_sFromTimeHM;
    }

    public String getA_sToTimeHM() {
        return A_sToTimeHM;
    }

    public void setA_sToTimeHM(String a_sToTimeHM) {
        A_sToTimeHM = a_sToTimeHM;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

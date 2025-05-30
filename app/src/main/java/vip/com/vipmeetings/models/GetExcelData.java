package vip.com.vipmeetings.models;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetExcelData", strict = false)
public class GetExcelData {

    @Element(name = "A_sFileCode", required = false)
    private String A_sFileCode;

    @Element(name = "authToken", required = false)
    private String authToken;

    public String getA_sFileCode() {
        return A_sFileCode;
    }

    public void setA_sFileCode(String a_sFileCode) {
        A_sFileCode = a_sFileCode;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


}

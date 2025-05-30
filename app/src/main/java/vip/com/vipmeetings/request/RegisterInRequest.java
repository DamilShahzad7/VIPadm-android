package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "RegisterInContact", strict = false)
public class RegisterInRequest {



    @Element(name = "A_sBarcode", required = false)
    private String A_sBarcode;
    @Element(name = "authToken", required = false)
    private String authToken;
    @Element(name = "A_sWorkingFrom", required = false)
    private String A_sWorkingFrom;
    public String getA_sWorkingFrom() {
        return A_sWorkingFrom;
    }

    public void setA_sWorkingFrom(String a_sWorkingFrom) {
        A_sWorkingFrom = a_sWorkingFrom;
    }

    public String getA_sBarcode() {
        return A_sBarcode;
    }

    public void setA_sBarcode(String a_sBarcode) {
        A_sBarcode = a_sBarcode;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

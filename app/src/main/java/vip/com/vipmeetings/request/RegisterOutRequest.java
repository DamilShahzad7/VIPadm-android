package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "RegisterOutContact", strict = false)
public class RegisterOutRequest {



    @Element(name = "A_sBarcode", required = false)
    private String A_sBarcode;
    @Element(name = "authToken", required = false)
    private String authToken;



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

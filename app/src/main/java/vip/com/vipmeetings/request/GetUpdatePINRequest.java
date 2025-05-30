package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "UpdateContactPin", strict = false)
public class GetUpdatePINRequest {


    @Element(name = "A_sBarcode", required = false)
    private String A_sBarcode;
    @Element(name = "authToken", required = false)
    private String authToken;

    @Element(name = "A_sPin", required = false)
    private String A_sPin;

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

    public String getA_sPin() {
        return A_sPin;
    }
    public void setA_sPin(String a_sPin) {
        A_sPin = a_sPin;
    }
}

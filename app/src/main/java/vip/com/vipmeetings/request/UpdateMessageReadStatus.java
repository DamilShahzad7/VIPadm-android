package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "UpdateMessageReadStatus", strict = false)
public class UpdateMessageReadStatus {


    @Element(name = "A_sClientId", required = false)
    private String A_sClientId;

    @Element(name = "authToken", required = false)
    private String authToken;

    @Element(name = "A_sEvacuationId", required = false)
    private String A_sEvacuationId;

    @Element(name = "A_sBarcode", required = false)
    private String A_sBarcode;

    @Element(name = "A_sAppName", required = false)
    private String A_sAppName;

    public String getA_sEvacuationId() {
        return A_sEvacuationId;
    }

    public void setA_sEvacuationId(String a_sEvacuationId) {
        A_sEvacuationId = a_sEvacuationId;
    }

    public String getA_sBarcode() {
        return A_sBarcode;
    }

    public void setA_sBarcode(String a_sBarcode) {
        A_sBarcode = a_sBarcode;
    }

    public String getA_sAppName() {
        return A_sAppName;
    }

    public void setA_sAppName(String a_sAppName) {
        A_sAppName = a_sAppName;
    }

    public String getA_sClientId() {
        return A_sClientId;
    }

    public void setA_sClientId(String a_sClientId) {
        A_sClientId = a_sClientId;
    }


    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

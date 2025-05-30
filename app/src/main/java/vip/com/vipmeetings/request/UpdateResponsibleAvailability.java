package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "UpdateResponsibleAvailability", strict = false)
public class UpdateResponsibleAvailability {


    @Element(name = "A_sClientId", required = false)
    private String A_sClientId;

    @Element(name = "authToken", required = false)
    private String authToken;

    @Element(name = "A_sAvailableStatus", required = false)
    private String A_sAvailableStatus;

    @Element(name = "A_sBarcode", required = false)
    private String A_sBarcode;

    public String getA_sClientId() {
        return A_sClientId;
    }

    public void setA_sClientId(String a_sClientId) {
        A_sClientId = a_sClientId;
    }

    public String getA_sAvailableStatus() {
        return A_sAvailableStatus;
    }

    public void setA_sAvailableStatus(String a_sAvailableStatus) {
        A_sAvailableStatus = a_sAvailableStatus;
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

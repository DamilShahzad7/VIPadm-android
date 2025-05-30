package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "SaveResponsiblePhoto", strict = false)
public class SaveResponsiblePhoto {

    @Element(name = "A_sPhotoCode", required = false)
    private String A_sPhotoCode;

    @Element(name = "A_sBarcode", required = false)
    private String A_sBarcode;

    @Element(name = "A_sClientId", required = false)
    private String A_sClientId;

    @Element(name = "authToken", required = false)
    private String authToken;

    public String getA_sPhotoCode() {
        return A_sPhotoCode;
    }

    public void setA_sPhotoCode(String a_sPhotoCode) {
        A_sPhotoCode = a_sPhotoCode;
    }

    public String getA_sBarcode() {
        return A_sBarcode;
    }

    public void setA_sBarcode(String a_sBarcode) {
        A_sBarcode = a_sBarcode;
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

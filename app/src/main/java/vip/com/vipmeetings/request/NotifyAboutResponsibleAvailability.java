package vip.com.vipmeetings.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "NotifyAboutResponsibleAvailability", strict = false)
public class NotifyAboutResponsibleAvailability {

    @Element(name = "A_iPlaceId", required = false)
    private String A_iPlaceId;

    @Element(name = "A_sBarcode", required = false)
    private String A_sBarcode;


    @Element(name = "authToken", required = false)
    private String authToken;


    public String getA_iPlaceId() {
        return A_iPlaceId;
    }

    public void setA_iPlaceId(String a_iPlaceId) {
        A_iPlaceId = a_iPlaceId;
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

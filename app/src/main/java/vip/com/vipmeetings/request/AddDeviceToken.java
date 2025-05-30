package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "AddDeviceToken", strict = false)
public class AddDeviceToken {


    @Element(name = "A_sUDID", required = false)
    private String A_sUDID;


    @Element(name = "authToken", required = false)
    private String authToken;

    @Element(name = "A_sAppBundleId", required = false)
    private String A_sAppBundleId;

    @Element(name = "A_siOsDeviceToken", required = false)
    private String A_siOsDeviceToken;

    @Element(name = "A_sAndroidToken", required = false)
    private String A_sAndroidToken;

    @Element(name = "A_sClientId", required = false)
    private String A_sClientId;


    @Element(name = "A_sPlaceId", required = false)
    private String A_sPlaceId;


    @Element(name = "A_sBarcode", required = false)
    private String A_sBarcode;

    public String getA_sUDID() {
        return A_sUDID;
    }

    public void setA_sUDID(String a_sUDID) {
        A_sUDID = a_sUDID;
    }

    public String getA_sAppBundleId() {
        return A_sAppBundleId;
    }

    public void setA_sAppBundleId(String a_sAppBundleId) {
        A_sAppBundleId = a_sAppBundleId;
    }

    public String getA_siOsDeviceToken() {
        return A_siOsDeviceToken;
    }

    public void setA_siOsDeviceToken(String a_siOsDeviceToken) {
        A_siOsDeviceToken = a_siOsDeviceToken;
    }

    public String getA_sAndroidToken() {
        return A_sAndroidToken;
    }

    public void setA_sAndroidToken(String a_sAndroidToken) {
        A_sAndroidToken = a_sAndroidToken;
    }

    public String getA_sClientId() {
        return A_sClientId;
    }

    public void setA_sClientId(String a_sClientId) {
        A_sClientId = a_sClientId;
    }

    public String getA_sPlaceId() {
        return A_sPlaceId;
    }

    public void setA_sPlaceId(String a_sPlaceId) {
        A_sPlaceId = a_sPlaceId;
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

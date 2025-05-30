package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "EvacuateContacts", strict = false)
public class UnRegisterOutRequest {



    @Element(name = "A_sBarcodes", required = false)
    private String A_sBarcode;
    @Element(name = "authToken", required = false)
    private String authToken;
    @Element(name = "A_sCheckedOutBy",required = false)
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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

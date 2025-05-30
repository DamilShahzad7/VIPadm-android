package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetInHouseContactsCount", strict = false)
public class GetInHouseContactsCountRequest {



    @Element(name = "A_sDateDMY", required = false)
    private String A_sDateDMY;

    @Element(name = "A_sCity", required = false)
    private String A_sCity;

    @Element(name = "A_sCompany", required = false)
    private String A_sCompany;

    @Element(name = "authToken", required = false)
    private String authToken;

    public String getA_sCity() {       return A_sCity;    }
    public void setA_sCity(String a_sCity) {
        A_sCity = a_sCity;
    }

    public String getA_sCompany() {
        return A_sCompany;
    }

    public void setA_sCompany(String a_sCompany) {
        A_sCompany = a_sCompany;
    }
    public String getA_sBarcode() {
        return A_sDateDMY;
    }

    public void setA_sDateDMY(String a_sDateDMY) {
        A_sDateDMY = a_sDateDMY;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

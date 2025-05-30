package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetRooms", strict = false)
public class GetRooms {


    @Element(name = "A_iCityCode", required = false)
    private String A_iCityCode;

    @Element(name = "authToken", required = false)
    private String authToken;

    @Element(name = "A_iCapacity", required = false)
    private String A_iCapacity;

    @Element(name = "A_iUserId", required = false)
    private String A_iUserId;

    public String getA_iCityCode() {
        return A_iCityCode;
    }

    public void setA_iCityCode(String a_iCityCode) {
        A_iCityCode = a_iCityCode;
    }

    public String getA_iCapacity() {
        return A_iCapacity;
    }

    public void setA_iCapacity(String a_iCapacity) {
        A_iCapacity = a_iCapacity;
    }

    public String getA_iUserId() {
        return A_iUserId;
    }

    public void setA_iUserId(String a_iUserId) {
        A_iUserId = a_iUserId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetRoomBookings", strict = false)
public class GetRoomBookings {


    @Element(name = "A_iRoomId", required = false)
    private String A_iRoomId;

    @Element(name = "authToken", required = false)
    private String authToken;

    public String getA_iRoomId() {
        return A_iRoomId;
    }

    public void setA_iRoomId(String a_iRoomId) {
        A_iRoomId = a_iRoomId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "DeleteMeeting", strict = false)
public class DeleteMeeting {


    @Element(name = "A_sMeetingId", required = false)
    private String A_sMeetingId;

    @Element(name = "authToken", required = false)
    private String authToken;

    public String getA_sMeetingId() {
        return A_sMeetingId;
    }

    public void setA_sMeetingId(String a_sMeetingId) {
        A_sMeetingId = a_sMeetingId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

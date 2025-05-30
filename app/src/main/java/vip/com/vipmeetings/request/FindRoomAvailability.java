package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "FindRoomAvailability", strict = false)
public class FindRoomAvailability {


    @Element(name = "A_sRoomId", required = false)
    private String A_sRoomId;

    @Element(name = "A_sRoomName", required = false)
    private String A_sRoomName;

    @Element(name = "A_sFromDateYMD", required = false)
    private String A_sFromDateYMD;

    @Element(name = "A_sToDateYMD", required = false)
    private String A_sToDateYMD;

    @Element(name = "A_sFromTime", required = false)
    private String A_sFromTime;


    @Element(name = "A_sToTime", required = false)
    private String A_sToTime;

    @Element(name = "A_sUserId", required = false)
    private String A_sUserId;

    @Element(name = "A_sMeetingId", required = false)
    private String A_sMeetingId;

    @Element(name = "A_sRoomSelectionId", required = false)
    private String A_sRoomSelectionId;


    @Element(name = "authToken", required = false)
    private String authToken;


    public String getA_sRoomId() {
        return A_sRoomId;
    }

    public void setA_sRoomId(String a_sRoomId) {
        A_sRoomId = a_sRoomId;
    }

    public String getA_sRoomName() {
        return A_sRoomName;
    }

    public void setA_sRoomName(String a_sRoomName) {
        A_sRoomName = a_sRoomName;
    }

    public String getA_sFromDateYMD() {
        return A_sFromDateYMD;
    }

    public void setA_sFromDateYMD(String a_sFromDateYMD) {
        A_sFromDateYMD = a_sFromDateYMD;
    }

    public String getA_sToDateYMD() {
        return A_sToDateYMD;
    }

    public void setA_sToDateYMD(String a_sToDateYMD) {
        A_sToDateYMD = a_sToDateYMD;
    }

    public String getA_sFromTime() {
        return A_sFromTime;
    }

    public void setA_sFromTime(String a_sFromTime) {
        A_sFromTime = a_sFromTime;
    }

    public String getA_sToTime() {
        return A_sToTime;
    }

    public void setA_sToTime(String a_sToTime) {
        A_sToTime = a_sToTime;
    }

    public String getA_sUserId() {
        return A_sUserId;
    }

    public void setA_sUserId(String a_sUserId) {
        A_sUserId = a_sUserId;
    }

    public String getA_sMeetingId() {
        return A_sMeetingId;
    }

    public void setA_sMeetingId(String a_sMeetingId) {
        A_sMeetingId = a_sMeetingId;
    }

    public String getA_sRoomSelectionId() {
        return A_sRoomSelectionId;
    }

    public void setA_sRoomSelectionId(String a_sRoomSelectionId) {
        A_sRoomSelectionId = a_sRoomSelectionId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

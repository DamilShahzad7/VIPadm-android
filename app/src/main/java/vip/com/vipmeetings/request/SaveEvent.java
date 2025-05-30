package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "SaveEvent", strict = false)
public class SaveEvent {


    @Element(name = "A_sUserEmail", required = false)
    private String A_sUserEmail;

    @Element(name = "authToken", required = false)
    private String authToken;

    @Element(name = "A_sDescription", required = false)
    private String A_sDescription;

    @Element(name = "A_sFromDateYMD", required = false)
    private String A_sFromDateYMD;

    @Element(name = "A_sToDateYMD", required = false)
    private String A_sToDateYMD;

    @Element(name = "A_sStartTime", required = false)
    private String A_sStartTime;


    @Element(name = "A_sEndTime", required = false)
    private String A_sEndTime;


    @Element(name = "A_sRoomId", required = false)
    private String A_sRoomId;

    @Element(name = "A_sLocation", required = false)
    private String A_sLocation;

    @Element(name = "A_sIsPrivate", required = false)
    private String A_sIsPrivate;

    @Element(name = "A_sShowMeetingsOnDisplay", required = false)
    private String A_sShowMeetingsOnDisplay;

    @Element(name = "A_sShowGuestsOnDisplay", required = false)
    private String  A_sShowGuestsOnDisplay;

    public String getA_sDescription() {
        return A_sDescription;
    }

    public void setA_sDescription(String a_sDescription) {
        A_sDescription = a_sDescription;
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

    public String getA_sStartTime() {
        return A_sStartTime;
    }

    public void setA_sStartTime(String a_sStartTime) {
        A_sStartTime = a_sStartTime;
    }

    public String getA_sEndTime() {
        return A_sEndTime;
    }

    public void setA_sEndTime(String a_sEndTime) {
        A_sEndTime = a_sEndTime;
    }

    public String getA_sRoomId() {
        return A_sRoomId;
    }

    public void setA_sRoomId(String a_sRoomId) {
        A_sRoomId = a_sRoomId;
    }

    public String getA_sLocation() {
        return A_sLocation;
    }

    public void setA_sLocation(String a_sLocation) {
        A_sLocation = a_sLocation;
    }

    public String getA_sUserEmail() {
        return A_sUserEmail;
    }

    public void setA_sUserEmail(String a_sUserEmail) {
        A_sUserEmail = a_sUserEmail;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getA_sIsPrivate() {
        return A_sIsPrivate;
    }

    public void setA_sIsPrivate(String a_sIsPrivate) {
        A_sIsPrivate = a_sIsPrivate;
    }

    public String getA_sShowMeetingsOnDisplay() {
        return A_sShowMeetingsOnDisplay;
    }

    public void setA_sShowMeetingsOnDisplay(String a_sShowMeetingsOnDisplay) {
        A_sShowMeetingsOnDisplay = a_sShowMeetingsOnDisplay;
    }

    public String getA_sShowGuestsOnDisplay() {
        return A_sShowGuestsOnDisplay;
    }

    public void setA_sShowGuestsOnDisplay(String a_sShowGuestsOnDisplay) {
        A_sShowGuestsOnDisplay = a_sShowGuestsOnDisplay;
    }
}

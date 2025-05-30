package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "SendEvacuationPush", strict = false)
public class SendEvacuationPush {


    @Element(name = "A_sClientId", required = false)
    private String A_sClientId;

    @Element(name = "authToken", required = false)
    private String authToken;

    @Element(name = "A_sPlaceId", required = false)
    private String A_sPlaceId;

    @Element(name = "A_sEvacuationIds", required = false)
    private String A_sEvacuationIds;

    @Element(name = "A_sPushMessage", required = false)
    private String A_sPushMessage;

    public String getA_sEvacuationIds() {
        return A_sEvacuationIds;
    }

    public void setA_sEvacuationIds(String a_sEvacuationIds) {
        A_sEvacuationIds = a_sEvacuationIds;
    }

    public String getA_sPushMessage() {
        return A_sPushMessage;
    }

    public void setA_sPushMessage(String a_sPushMessage) {
        A_sPushMessage = a_sPushMessage;
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


    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

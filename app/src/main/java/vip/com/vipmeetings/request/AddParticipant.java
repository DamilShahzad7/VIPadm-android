package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "AddParticipants", strict = false)
public class AddParticipant {


    @Element(name = "A_sUserEmail", required = false)
    private String A_sUserEmail;

    @Element(name = "authToken", required = false)
    private String authToken;

//    @Element(name = "VisitorType", required = false)
//    private String VisitorType;

    @Element(name = "A_sParticipantsText", required = false)
    private String A_sParticipantsText;


//    public String getVisitorType() {
//        return VisitorType;
//    }
//
//    public void setVisitorType(String visitorType) {
//        VisitorType = visitorType;
//    }

    public String getA_sUserEmail() {
        return A_sUserEmail;
    }

    public void setA_sUserEmail(String a_sUserEmail) {
        A_sUserEmail = a_sUserEmail;
    }

    public String getA_sParticipantsText() {
        return A_sParticipantsText;
    }

    public void setA_sParticipantsText(String a_sParticipantsText) {
        A_sParticipantsText = a_sParticipantsText;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

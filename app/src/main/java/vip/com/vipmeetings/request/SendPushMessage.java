package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "SendPushMessage", strict = false)
public class SendPushMessage {


    @Element(name = "A_sClientId", required = false)
    private String A_sClientId;

    @Element(name = "authToken", required = false)
    private String authToken;

    @Element(name = "A_sPlaceId", required = false)
    private String A_sPlaceId;

    @Element(name = "A_sEvacuationId", required = false)
    private String A_sEvacuationId;

    @Element(name = "A_sMessage", required = false)
    private String A_sMessage;

    @Element(name = "A_sTitle", required = false)
    private String A_sTitle;

    @Element(name = "A_sBarcode", required = false)
    private String A_sBarcode;

    @Element(name = "A_sReplyToMessageId", required = false)
    private String A_sReplyToMessageId;

    @Element(name = "A_sSendByApp", required = false)
    private String A_sSendByApp;

    @Element(name = "A_sReceivedByApp", required = false)
    private String A_sReceivedByApp;

    @Element(name = "A_sSenderId", required = false)
    private String A_sSenderId;


    public String getA_sMessage() {
        return A_sMessage;
    }

    public void setA_sMessage(String a_sMessage) {
        A_sMessage = a_sMessage;
    }

    public String getA_sTitle() {
        return A_sTitle;
    }

    public void setA_sTitle(String a_sTitle) {
        A_sTitle = a_sTitle;
    }

    public String getA_sBarcode() {
        return A_sBarcode;
    }

    public void setA_sBarcode(String a_sBarcode) {
        A_sBarcode = a_sBarcode;
    }

    public String getA_sReplyToMessageId() {
        return A_sReplyToMessageId;
    }

    public void setA_sReplyToMessageId(String a_sReplyToMessageId) {
        A_sReplyToMessageId = a_sReplyToMessageId;
    }

    public String getA_sSendByApp() {
        return A_sSendByApp;
    }

    public void setA_sSendByApp(String a_sSendByApp) {
        A_sSendByApp = a_sSendByApp;
    }

    public String getA_sReceivedByApp() {
        return A_sReceivedByApp;
    }

    public void setA_sReceivedByApp(String a_sReceivedByApp) {
        A_sReceivedByApp = a_sReceivedByApp;
    }

    public String getA_sSenderId() {
        return A_sSenderId;
    }

    public void setA_sSenderId(String a_sSenderId) {
        A_sSenderId = a_sSenderId;
    }

    public String getA_sEvacuationId() {
        return A_sEvacuationId;
    }

    public void setA_sEvacuationId(String a_sEvacuationId) {
        A_sEvacuationId = a_sEvacuationId;
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

package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetEvacuationResponsibles", strict = false)
public class GetEvacuationResponsibles {


    @Element(name = "A_sEvacuationId", required = false)
    private String A_sEvacuationId;

    @Element(name = "authToken", required = false)
    private String authToken;

    @Element(name = "A_sClientId", required = false)
    private String A_sClientId;

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

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

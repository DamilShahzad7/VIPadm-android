package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "RemoveClearTimeToEvacuation", strict = false)
public class RemoveClearTimeToEvacuation {


    @Element(name = "A_sClientId", required = false)
    private String A_sClientId;

    @Element(name = "authToken", required = false)
    private String authToken;

    @Element(name = "sEvacuationId", required = false)
    private String sEvacuationId;

    @Element(name = "sCleredBy", required = false)
    private String sCleredBy;

    public String getsCleredBy() {
        return sCleredBy;
    }

    public void setsCleredBy(String sCleredBy) {
        this.sCleredBy = sCleredBy;
    }

    public String getA_sClientId() {
        return A_sClientId;
    }

    public void setA_sClientId(String a_sClientId) {
        A_sClientId = a_sClientId;
    }

    public String getsEvacuationId() {
        return sEvacuationId;
    }

    public void setsEvacuationId(String sEvacuationId) {
        this.sEvacuationId = sEvacuationId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

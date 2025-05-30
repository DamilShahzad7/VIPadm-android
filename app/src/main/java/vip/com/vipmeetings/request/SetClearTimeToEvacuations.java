package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "SetClearTimeToEvacuations", strict = false)
public class SetClearTimeToEvacuations {

    //string A_sClientId,
    //      string A_sUpdateClearTimeEvacuationIds,
    //      string A_sRemoveClearTimeEvacuationIds,
    //      string sCleredBy,
    //

    @Element(name = "A_sClientId", required = false)
    private String A_sClientId;

    @Element(name = "sCleredBy", required = false)
    private String sCleredBy;



    @Element(name = "authToken", required = false)
    private String authToken;

    @Element(name = "A_sUpdateClearTimeEvacuationIds", required = false)
    private String A_sUpdateClearTimeEvacuationIds;

    @Element(name = "A_sRemoveClearTimeEvacuationIds", required = false)
    private String A_sRemoveClearTimeEvacuationIds;

    public String getA_sUpdateClearTimeEvacuationIds() {
        return A_sUpdateClearTimeEvacuationIds;
    }

    public void setA_sUpdateClearTimeEvacuationIds(String a_sUpdateClearTimeEvacuationIds) {
        A_sUpdateClearTimeEvacuationIds = a_sUpdateClearTimeEvacuationIds;
    }

    public String getA_sRemoveClearTimeEvacuationIds() {
        return A_sRemoveClearTimeEvacuationIds;
    }

    public void setA_sRemoveClearTimeEvacuationIds(String a_sRemoveClearTimeEvacuationIds) {
        A_sRemoveClearTimeEvacuationIds = a_sRemoveClearTimeEvacuationIds;
    }

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



    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

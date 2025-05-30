package vip.com.vipmeetings.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetAppointments", strict = false)
public class GetAppointmentsRequest {


    @Element(name = "A_sUserEmail", required = false)
    private String A_sUserEmail;



    @Element(name = "authToken", required = false)
    private String authToken;


    @Element(name = "A_sDateYMD", required = false)
    private String date;

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
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}

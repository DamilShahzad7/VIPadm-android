package vip.com.vipmeetings.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Created by Srinath on 26/03/18.
 */
@Root(name = "GetAuthToken", strict = false)
public class GetAuthTokenData {

    @Element(name = "sPasscode", required = false)
    private String sPasscode;
    @Element(name = "iUserID", required = false)
    private int iUserID;

    public String getsPasscode() {
        return sPasscode;
    }

    public void setsPasscode(String sPasscode) {
        this.sPasscode = sPasscode;
    }

    public int getiUserID() {
        return iUserID;
    }

    public void setiUserID(int iUserID) {
        this.iUserID = iUserID;
    }
}

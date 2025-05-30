package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "UpdateContactPinResponse", strict = false)
//@Namespace(reference = "http://tempuri.org/")
public class GetUpdatePINResponse {


    public String getGetUpdateContactPinResult() {
        return getUpdateContactPinResult;
    }

    public void setGetUpdateContactPinResult(String getUpdateContactPinResult) {
        this.getUpdateContactPinResult = getUpdateContactPinResult;
    }

    @Element(name = "UpdateContactPinResult", required = false)
    private String getUpdateContactPinResult;



}

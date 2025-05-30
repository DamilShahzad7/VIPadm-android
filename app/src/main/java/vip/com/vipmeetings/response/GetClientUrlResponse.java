package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetClientUrlResponse", strict = false)
public class GetClientUrlResponse {


    @Element(name = "GetClientUrlResult", required = false)
    private String GetClientUrlResult;

    public String getGetClientUrlResult() {
        return GetClientUrlResult;
    }

    public void setGetClientUrlResult(String getClientUrlResult) {
        GetClientUrlResult = getClientUrlResult;
    }
}

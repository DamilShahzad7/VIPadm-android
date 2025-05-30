package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetClientResponse", strict = false)
public class GetClientResponse {

    @Element(name = "GetClientResult", required = false)
    private GetClientResult getClientResult;


    public GetClientResult getGetClientResult() {
        return getClientResult;
    }

    public void setGetClientResult(GetClientResult getClientResult) {
        this.getClientResult = getClientResult;
    }
}

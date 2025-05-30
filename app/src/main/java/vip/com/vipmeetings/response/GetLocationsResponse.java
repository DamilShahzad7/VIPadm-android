package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetLocationsResponse", strict = false)
public class GetLocationsResponse {


    @Element(name = "GetLocationsResult", required = false)
    private GetLocationsResult getLocationsResult;

    public GetLocationsResult getGetLocationsResult() {
        return getLocationsResult;
    }

    public void setGetLocationsResult(GetLocationsResult getLocationsResult) {
        this.getLocationsResult = getLocationsResult;
    }
}

package vip.com.vipmeetings.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "GetPlacesResponse", strict = false)
public class GetPlacesResponse {

    @Element(name = "GetPlacesResult", required = false)
    private GetPlacesResult getPlacesResult;

    public GetPlacesResult getGetPlacesResult() {
        return getPlacesResult;
    }

    public void setGetPlacesResult(GetPlacesResult getPlacesResult) {
        this.getPlacesResult = getPlacesResult;
    }
}

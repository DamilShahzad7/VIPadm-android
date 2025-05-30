package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.GetPlaceIDResponse;
import vip.com.vipmeetings.response.GetPlaceIDResult;


@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetPlaceIDResponseBody {

    @Element(name = "GetPlaceIDResponse", required = false)
    private GetPlaceIDResponse getPlaceIDResponse;

    public GetPlaceIDResponse getGetPlaceIDResponse() {
        return getPlaceIDResponse;
    }

    public void setGetPlaceIDResponse(GetPlaceIDResponse getPlaceIDResponse) {
        this.getPlaceIDResponse = getPlaceIDResponse;
    }
}

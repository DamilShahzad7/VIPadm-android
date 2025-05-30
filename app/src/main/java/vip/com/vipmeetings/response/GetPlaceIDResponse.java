package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "GetPlaceIDResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetPlaceIDResponse {


    @Element(name = "GetPlaceIDResult", required = false)
    private GetPlaceIDResult getPlaceIDResult;

    public GetPlaceIDResult getGetPlaceIDResult() {
        return getPlaceIDResult;
    }

    public void setGetPlaceIDResult(GetPlaceIDResult getPlaceIDResult) {
        this.getPlaceIDResult = getPlaceIDResult;
    }
}

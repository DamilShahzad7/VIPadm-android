package vip.com.vipmeetings.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.models.Empty;
import vip.com.vipmeetings.models.Error;
import vip.com.vipmeetings.models.GetPlaceID;

@Root(name = "GetPlaceIDResult", strict = false)
public class GetPlaceIDResult {


    @Element(name = "Error", required = false)
    private vip.com.vipmeetings.models.Error Error;

    @Element(name = "Empty", required = false)
    private Empty empty;

    @Element(name = "GetPlaceID", required = false)
    private GetPlaceID getPlaceID;

    public GetPlaceID getGetPlaceID() {
        return getPlaceID;
    }

    public void setGetPlaceID(GetPlaceID getPlaceID) {
        this.getPlaceID = getPlaceID;
    }

    public vip.com.vipmeetings.models.Error getError() {
        return Error;
    }

    public void setError(vip.com.vipmeetings.models.Error error) {
        Error = error;
    }

    public Empty getEmpty() {
        return empty;
    }

    public void setEmpty(Empty empty) {
        this.empty = empty;
    }
}

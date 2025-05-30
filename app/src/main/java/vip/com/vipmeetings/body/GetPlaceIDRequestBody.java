package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.models.GetPlaceID;
import vip.com.vipmeetings.request.GetPlaceIDRequest;


@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetPlaceIDRequestBody {



    @Element(name = "GetPlaceID", required = false)
    private GetPlaceIDRequest getPlaceID;


    public GetPlaceIDRequest getGetPlaceID() {
        return getPlaceID;
    }

    public void setGetPlaceID(GetPlaceIDRequest getPlaceID) {
        this.getPlaceID = getPlaceID;
    }
}

package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.EvacuationAuthenticateLoginRequest;
import vip.com.vipmeetings.request.GetLocationsRequest;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class CitiesRequestBody {



    @Element(name = "GetLocations", required = false)
    private GetLocationsRequest getLocationsRequest;

    public GetLocationsRequest getGetLocationsRequest() {
        return getLocationsRequest;
    }

    public void setGetLocationsRequest(GetLocationsRequest getLocationsRequest) {
        this.getLocationsRequest = getLocationsRequest;
    }
}

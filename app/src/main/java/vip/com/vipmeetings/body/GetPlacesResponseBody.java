package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.GetPlacesResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetPlacesResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "GetPlacesResponse", required = false)
    private GetPlacesResponse getPlacesResponse;

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }

    public GetPlacesResponse getGetPlacesResponse() {
        return getPlacesResponse;
    }

    public void setGetPlacesResponse(GetPlacesResponse getPlacesResponse) {
        this.getPlacesResponse = getPlacesResponse;
    }
}




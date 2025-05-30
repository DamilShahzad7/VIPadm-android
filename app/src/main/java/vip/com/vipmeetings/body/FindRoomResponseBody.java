package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.AddDeviceTokenResponse;
import vip.com.vipmeetings.response.FindRoomAvailabilityResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class FindRoomResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "FindRoomAvailabilityResponse", required = false)
    private FindRoomAvailabilityResponse findRoomAvailabilityResponse;

    public FindRoomAvailabilityResponse getFindRoomAvailabilityResponse() {
        return findRoomAvailabilityResponse;
    }

    public void setFindRoomAvailabilityResponse(FindRoomAvailabilityResponse findRoomAvailabilityResponse) {
        this.findRoomAvailabilityResponse = findRoomAvailabilityResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}

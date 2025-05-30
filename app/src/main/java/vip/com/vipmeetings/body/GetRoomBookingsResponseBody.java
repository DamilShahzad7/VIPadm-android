package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.DeleteMeetingResponse;
import vip.com.vipmeetings.response.GetRoomBookingsResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetRoomBookingsResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "GetRoomBookingsResponse", required = false)
    private GetRoomBookingsResponse getRoomBookingsResponse;

    public GetRoomBookingsResponse getGetRoomBookingsResponse() {
        return getRoomBookingsResponse;
    }

    public void setGetRoomBookingsResponse(GetRoomBookingsResponse getRoomBookingsResponse) {
        this.getRoomBookingsResponse = getRoomBookingsResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}

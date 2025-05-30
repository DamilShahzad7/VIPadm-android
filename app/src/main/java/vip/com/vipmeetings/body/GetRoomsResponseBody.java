package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.DeleteMeetingResponse;
import vip.com.vipmeetings.response.GetRoomsResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetRoomsResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "GetRoomsResponse", required = false)
    private GetRoomsResponse getRoomsResponse;

    public GetRoomsResponse getGetRoomsResponse() {
        return getRoomsResponse;
    }

    public void setGetRoomsResponse(GetRoomsResponse getRoomsResponse) {
        this.getRoomsResponse = getRoomsResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}

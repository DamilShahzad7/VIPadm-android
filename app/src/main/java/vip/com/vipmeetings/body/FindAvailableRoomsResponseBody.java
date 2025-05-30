package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.AddContactLeaveResponse;
import vip.com.vipmeetings.response.FindAvailableRoomsResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class FindAvailableRoomsResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "FindAvailableRoomsResponse", required = false)
    private FindAvailableRoomsResponse addContactLeaveResponse;

    public FindAvailableRoomsResponse getAddContactLeaveResponse() {
        return addContactLeaveResponse;
    }

    public void setAddContactLeaveResponse(FindAvailableRoomsResponse addContactLeaveResponse) {
        this.addContactLeaveResponse = addContactLeaveResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}

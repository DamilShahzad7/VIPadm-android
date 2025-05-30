package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.DeleteMeetingResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class DeleteMeetingResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "DeleteMeetingResponse", required = false)
    private DeleteMeetingResponse deleteMeetingResponse;

    public DeleteMeetingResponse getDeleteMeetingResponse() {
        return deleteMeetingResponse;
    }

    public void setDeleteMeetingResponse(DeleteMeetingResponse deleteMeetingResponse) {
        this.deleteMeetingResponse = deleteMeetingResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}

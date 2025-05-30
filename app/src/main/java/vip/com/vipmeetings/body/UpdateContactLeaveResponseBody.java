package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.GetMeetingsResponse;
import vip.com.vipmeetings.response.UpdateContactLeaveResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class UpdateContactLeaveResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "UpdateContactLeaveResponse", required = false)
    private UpdateContactLeaveResponse updateContactLeaveResponse;

    public UpdateContactLeaveResponse getUpdateContactLeaveResponse() {
        return updateContactLeaveResponse;
    }

    public void setUpdateContactLeaveResponse(UpdateContactLeaveResponse updateContactLeaveResponse) {
        this.updateContactLeaveResponse = updateContactLeaveResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}

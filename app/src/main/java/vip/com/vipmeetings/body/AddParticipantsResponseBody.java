package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.AddParticipantsResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class AddParticipantsResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "AddParticipantsResponse", required = false)
    private AddParticipantsResponse addParticipantsResponse;

    public AddParticipantsResponse getAddParticipantsResponse() {
        return addParticipantsResponse;
    }

    public void setAddParticipantsResponse(AddParticipantsResponse addParticipantsResponse) {
        this.addParticipantsResponse = addParticipantsResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}

package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.SetClearTimeToEvacuationsResponse;
import vip.com.vipmeetings.response.UpdateMessageReadStatusResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class UpdateMessageReadStatusResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "UpdateMessageReadStatusResponse", required = false)
    private UpdateMessageReadStatusResponse updateMessageReadStatusResponse;

    public UpdateMessageReadStatusResponse getUpdateMessageReadStatusResponse() {
        return updateMessageReadStatusResponse;
    }

    public void setUpdateMessageReadStatusResponse(UpdateMessageReadStatusResponse updateMessageReadStatusResponse) {
        this.updateMessageReadStatusResponse = updateMessageReadStatusResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}

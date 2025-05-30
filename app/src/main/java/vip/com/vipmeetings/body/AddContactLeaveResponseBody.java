package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.AddContactLeaveResponse;
import vip.com.vipmeetings.response.UpdateContactLeaveResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class AddContactLeaveResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "AddContactLeaveResponse", required = false)
    private AddContactLeaveResponse addContactLeaveResponse;

    public AddContactLeaveResponse getAddContactLeaveResponse() {
        return addContactLeaveResponse;
    }

    public void setAddContactLeaveResponse(AddContactLeaveResponse addContactLeaveResponse) {
        this.addContactLeaveResponse = addContactLeaveResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}

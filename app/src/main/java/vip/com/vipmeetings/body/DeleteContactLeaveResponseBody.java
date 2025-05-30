package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.DeleteContactLeaveResponse;
import vip.com.vipmeetings.response.UpdateContactLeaveResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class DeleteContactLeaveResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "DeleteContactLeaveResponse", required = false)
    private DeleteContactLeaveResponse deleteContactLeaveResponse;

    public DeleteContactLeaveResponse getDeleteContactLeaveResponse() {
        return deleteContactLeaveResponse;
    }

    public void setDeleteContactLeaveResponse(DeleteContactLeaveResponse deleteContactLeaveResponse) {
        this.deleteContactLeaveResponse = deleteContactLeaveResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}

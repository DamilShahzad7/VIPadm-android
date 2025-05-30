package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.AddContactLeaveResponse;
import vip.com.vipmeetings.response.SaveEventResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class SaveEventResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "SaveEventResponse", required = false)
    private SaveEventResponse saveEventResponse;

    public SaveEventResponse getSaveEventResponse() {
        return saveEventResponse;
    }

    public void setSaveEventResponse(SaveEventResponse saveEventResponse) {
        this.saveEventResponse = saveEventResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}

package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.AddContactLeaveResponse;
import vip.com.vipmeetings.response.GetContactLeavesResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetContactLeaveResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "GetContactLeavesResponse", required = false)
    private GetContactLeavesResponse getContactLeavesResponse;

    public GetContactLeavesResponse getGetContactLeavesResponse() {
        return getContactLeavesResponse;
    }

    public void setGetContactLeavesResponse(GetContactLeavesResponse getContactLeavesResponse) {
        this.getContactLeavesResponse = getContactLeavesResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}

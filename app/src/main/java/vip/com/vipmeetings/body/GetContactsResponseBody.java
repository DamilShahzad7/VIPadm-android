package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.AddDeviceTokenResponse;
import vip.com.vipmeetings.response.GetContactsResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetContactsResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "GetContactsResponse", required = false)
    private GetContactsResponse getContactsResponse;

    public GetContactsResponse getGetContactsResponse() {
        return getContactsResponse;
    }

    public void setGetContactsResponse(GetContactsResponse getContactsResponse) {
        this.getContactsResponse = getContactsResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}

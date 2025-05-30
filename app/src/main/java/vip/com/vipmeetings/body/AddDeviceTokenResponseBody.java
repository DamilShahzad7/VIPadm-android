package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.AddContactLeaveResponse;
import vip.com.vipmeetings.response.AddDeviceTokenResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class AddDeviceTokenResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "AddDeviceTokenResponse", required = false)
    private AddDeviceTokenResponse addDeviceTokenResponse;

    public AddDeviceTokenResponse getAddDeviceTokenResponse() {
        return addDeviceTokenResponse;
    }

    public void setAddDeviceTokenResponse(AddDeviceTokenResponse addDeviceTokenResponse) {
        this.addDeviceTokenResponse = addDeviceTokenResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}

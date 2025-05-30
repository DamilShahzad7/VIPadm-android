package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.AddContactLeaveResponse;
import vip.com.vipmeetings.response.AddOrderResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class AddOrderResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "AddOrderResponse", required = false)
    private AddOrderResponse addOrderResponse;

    public AddOrderResponse getAddOrderResponse() {
        return addOrderResponse;
    }

    public void setAddOrderResponse(AddOrderResponse addOrderResponse) {
        this.addOrderResponse = addOrderResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}

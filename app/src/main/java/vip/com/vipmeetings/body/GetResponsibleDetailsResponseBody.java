package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.GetResponsibleDetailsResponse;
import vip.com.vipmeetings.response.ValidateUserResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetResponsibleDetailsResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "GetResponsibleDetailsResponse", required = false)
    private GetResponsibleDetailsResponse getResponsibleDetailsResponse;

    public GetResponsibleDetailsResponse getGetResponsibleDetailsResponse() {
        return getResponsibleDetailsResponse;
    }

    public void setGetResponsibleDetailsResponse(GetResponsibleDetailsResponse getResponsibleDetailsResponse) {
        this.getResponsibleDetailsResponse = getResponsibleDetailsResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}

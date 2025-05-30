package vip.com.vipmeetings.body;


import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.GetEvacuationsResponse;
import vip.com.vipmeetings.response.UpdateResponsibleAvailabilityResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class UpdateResponsibleAvailabilityResponseBody {


    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "UpdateResponsibleAvailabilityResponse", required = false)
    private UpdateResponsibleAvailabilityResponse updateResponsibleAvailabilityResponse;

    public UpdateResponsibleAvailabilityResponse getUpdateResponsibleAvailabilityResponse() {
        return updateResponsibleAvailabilityResponse;
    }

    public void setUpdateResponsibleAvailabilityResponse(UpdateResponsibleAvailabilityResponse updateResponsibleAvailabilityResponse) {
        this.updateResponsibleAvailabilityResponse = updateResponsibleAvailabilityResponse;
    }

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }
}

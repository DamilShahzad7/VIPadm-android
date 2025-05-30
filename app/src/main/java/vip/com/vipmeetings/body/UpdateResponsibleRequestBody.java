package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.EndEvacuation;
import vip.com.vipmeetings.request.UpdateResponsibleAvailability;
import vip.com.vipmeetings.response.UpdateResponsibleAvailabilityResult;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class UpdateResponsibleRequestBody {


    @Element(name = "UpdateResponsibleAvailability", required = false)
    private UpdateResponsibleAvailability updateResponsibleAvailability;

    public UpdateResponsibleAvailability getUpdateResponsibleAvailability() {
        return updateResponsibleAvailability;
    }

    public void setUpdateResponsibleAvailability(UpdateResponsibleAvailability updateResponsibleAvailability) {
        this.updateResponsibleAvailability = updateResponsibleAvailability;
    }
}

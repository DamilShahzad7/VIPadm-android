package vip.com.vipmeetings.body;

import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.RemoveResponsiblePhotoResponse;
import vip.com.vipmeetings.response.SaveEventResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class RemoveResponsiblePhotoResponseBody {

    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "RemoveResponsiblePhotoResponse", required = false)
    private RemoveResponsiblePhotoResponse removeResponsiblePhotoResponse;

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }

    public RemoveResponsiblePhotoResponse getRemoveResponsiblePhotoResponse() {
        return removeResponsiblePhotoResponse;
    }

    public void setRemoveResponsiblePhotoResponse(RemoveResponsiblePhotoResponse removeResponsiblePhotoResponse) {
        this.removeResponsiblePhotoResponse = removeResponsiblePhotoResponse;
    }
}

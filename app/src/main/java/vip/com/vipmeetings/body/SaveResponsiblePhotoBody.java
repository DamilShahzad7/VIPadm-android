package vip.com.vipmeetings.body;

import org.ksoap2.SoapFault;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.SaveResponsiblePhoto;
import vip.com.vipmeetings.response.SaveEventResponse;
@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class SaveResponsiblePhotoBody {



    @Element(name = "Fault", required = false)
    private SoapFault fault;


    @Element(name = "SaveResponsiblePhoto", required = false)
    private SaveResponsiblePhoto saveResponsiblePhoto;

    public SoapFault getFault() {
        return fault;
    }

    public void setFault(SoapFault fault) {
        this.fault = fault;
    }

    public SaveResponsiblePhoto getSaveResponsiblePhoto() {
        return saveResponsiblePhoto;
    }

    public void setSaveResponsiblePhoto(SaveResponsiblePhoto saveResponsiblePhoto) {
        this.saveResponsiblePhoto = saveResponsiblePhoto;
    }
}

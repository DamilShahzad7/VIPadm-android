package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.response.SaveResponsiblePhotoResponse;
import vip.com.vipmeetings.response.SendEvacuationPushResponse;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class SaveResponsiblePhotoResponseBody {


    @Element(name = "SaveResponsiblePhotoResponse", required = false)
    private SaveResponsiblePhotoResponse saveResponsiblePhotoResponse;

    public SaveResponsiblePhotoResponse getSaveResponsiblePhotoResponse() {
        return saveResponsiblePhotoResponse;
    }

    public void setSaveResponsiblePhotoResponse(SaveResponsiblePhotoResponse saveResponsiblePhotoResponse) {
        this.saveResponsiblePhotoResponse = saveResponsiblePhotoResponse;
    }
}

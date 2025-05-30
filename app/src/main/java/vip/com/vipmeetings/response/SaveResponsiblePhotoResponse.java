package vip.com.vipmeetings.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "SaveResponsiblePhotoResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class SaveResponsiblePhotoResponse {


    @Element(name = "SaveResponsiblePhotoResult", required = false)
    private SaveResponsiblePhotoResult saveResponsiblePhotoResult;

    public SaveResponsiblePhotoResult getSaveResponsiblePhotoResult() {
        return saveResponsiblePhotoResult;
    }

    public void setSaveResponsiblePhotoResult(SaveResponsiblePhotoResult saveResponsiblePhotoResult) {
        this.saveResponsiblePhotoResult = saveResponsiblePhotoResult;
    }
}

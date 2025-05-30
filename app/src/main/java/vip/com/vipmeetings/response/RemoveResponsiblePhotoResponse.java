package vip.com.vipmeetings.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "RemoveResponsiblePhotoResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class RemoveResponsiblePhotoResponse {

    @Element(name = "RemoveResponsiblePhotoResult", required = false)
    private RemoveResponsiblePhotoResult removeResponsiblePhotoResult;

    public RemoveResponsiblePhotoResult getRemoveResponsiblePhotoResult() {
        return removeResponsiblePhotoResult;
    }

    public void setRemoveResponsiblePhotoResult(RemoveResponsiblePhotoResult removeResponsiblePhotoResult) {
        this.removeResponsiblePhotoResult = removeResponsiblePhotoResult;
    }
}

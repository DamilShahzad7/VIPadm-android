package vip.com.vipmeetings.body;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.RemoveResponsiblePhoto;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class SoapRemoveRequestBody {

    @Element(name = "RemoveResponsiblePhoto", required = false)
    private RemoveResponsiblePhoto removeResponsiblePhoto;

    public RemoveResponsiblePhoto getRemoveResponsiblePhoto() {
        return removeResponsiblePhoto;
    }

    public void setRemoveResponsiblePhoto(RemoveResponsiblePhoto removeResponsiblePhoto) {
        this.removeResponsiblePhoto = removeResponsiblePhoto;
    }
}

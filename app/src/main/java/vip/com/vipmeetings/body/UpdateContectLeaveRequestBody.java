package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.GetMeetingsRequest;
import vip.com.vipmeetings.request.UpdateContactLeave;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class UpdateContectLeaveRequestBody {


    @Element(name = "UpdateContactLeave", required = false)
    private UpdateContactLeave updateContactLeave;

    public UpdateContactLeave getUpdateContactLeave() {
        return updateContactLeave;
    }

    public void setUpdateContactLeave(UpdateContactLeave updateContactLeave) {
        this.updateContactLeave = updateContactLeave;
    }
}

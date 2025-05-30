package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.SetClearTimeToEvacuations;
import vip.com.vipmeetings.request.UpdateMessageReadStatus;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class UpdateMessageReadStatusRequestBody {


    @Element(name = "UpdateMessageReadStatus", required = false)
    private UpdateMessageReadStatus updateMessageReadStatus;

    public UpdateMessageReadStatus getUpdateMessageReadStatus() {
        return updateMessageReadStatus;
    }

    public void setUpdateMessageReadStatus(UpdateMessageReadStatus updateMessageReadStatus) {
        this.updateMessageReadStatus = updateMessageReadStatus;
    }
}

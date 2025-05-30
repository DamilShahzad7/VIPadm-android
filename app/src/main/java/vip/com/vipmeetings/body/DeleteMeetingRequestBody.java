package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.DeleteMeeting;
import vip.com.vipmeetings.request.SetClearTimeToEvacuations;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class DeleteMeetingRequestBody {


    @Element(name = "DeleteMeeting", required = false)
    private DeleteMeeting deleteMeeting;

    public DeleteMeeting getDeleteMeeting() {
        return deleteMeeting;
    }

    public void setDeleteMeeting(DeleteMeeting deleteMeeting) {
        this.deleteMeeting = deleteMeeting;
    }
}

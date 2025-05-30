package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Srinath on 20/06/17.
 */

@Root(strict = false, name = "SaveMeetingStatus")
public class SaveMeetingStatus {


    @Element(name = "Status", required = false)
    public Status Status;

    @Element(name = "Error", required = false)
    public Error Error;

    @Element(name = "MeetingId", required = false)
    public MeetingId MeetingId;

    public vip.com.vipmeetings.models.Error getError() {
        return Error;
    }

    public void setError(vip.com.vipmeetings.models.Error error) {
        Error = error;
    }

    public Status getStatus() {
        return Status;
    }

    public void setStatus(Status status) {
        Status = status;
    }

    public MeetingId getMeetingId() {
        return MeetingId;
    }

    public void setMeetingId(MeetingId meetingId) {
        MeetingId = meetingId;
    }
}

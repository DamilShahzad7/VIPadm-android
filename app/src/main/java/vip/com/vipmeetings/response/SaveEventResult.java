package vip.com.vipmeetings.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.models.Empty;
import vip.com.vipmeetings.models.SaveMeetingStatus;
import vip.com.vipmeetings.models.Status;

@Root(name = "SaveEventResult", strict = false)
public class SaveEventResult {

    @Element(name = "Error", required = false)
    private vip.com.vipmeetings.models.Error Error;

    @Element(name = "Empty", required = false)
    private Empty empty;

    @Element(name = "SaveMeetingStatus", required = false)
    private SaveMeetingStatus status;

    public SaveMeetingStatus getStatus() {
        return status;
    }

    public void setStatus(SaveMeetingStatus status) {
        this.status = status;
    }

    public vip.com.vipmeetings.models.Error getError() {
        return Error;
    }

    public void setError(vip.com.vipmeetings.models.Error error) {
        Error = error;
    }

    public Empty getEmpty() {
        return empty;
    }

    public void setEmpty(Empty empty) {
        this.empty = empty;
    }
}

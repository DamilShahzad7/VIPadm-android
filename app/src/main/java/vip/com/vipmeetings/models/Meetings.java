package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Srinath on 30/05/17.
 */

@Root(strict = false)
public class Meetings {


    @Element(name = "Status",required=false)
    private Status Status;


    @ElementList(entry = "Meeting", inline = true,required = false)
    List<Meeting> Meeting;

    public List<vip.com.vipmeetings.models.Meeting> getMeeting() {
        return Meeting;
    }

    public void setMeeting(List<vip.com.vipmeetings.models.Meeting> meeting) {
        Meeting = meeting;
    }

    public Status getStatus() {
        return Status;
    }

    public void setStatus(Status status) {
        Status = status;
    }
}

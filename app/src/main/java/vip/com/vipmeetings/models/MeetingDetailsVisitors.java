package vip.com.vipmeetings.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Srinath on 04/07/17.
 */

@Root(strict = false)
public class MeetingDetailsVisitors {
    @ElementList(entry = "VISITOR", inline = true, required = false)
    List<MeetingDetailsVisitor> meetingDetailsVisitor;

    public List<MeetingDetailsVisitor> getMeetingDetailsVisitor() {
        return meetingDetailsVisitor;
    }

    public void setMeetingDetailsVisitor(List<MeetingDetailsVisitor> meetingDetailsVisitor) {
        this.meetingDetailsVisitor = meetingDetailsVisitor;
    }
}

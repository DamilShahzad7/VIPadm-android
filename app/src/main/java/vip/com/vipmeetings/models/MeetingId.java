package vip.com.vipmeetings.models;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * Created by Srinath on 20/06/17.
 */
@Root(name = "MeetingId",strict = false)
public class MeetingId {

    @Text
    private String _meetingid;

    public String get_meetingid() {
        return _meetingid;
    }

    public void set_meetingid(String _meetingid) {
        this._meetingid = _meetingid;
    }
}

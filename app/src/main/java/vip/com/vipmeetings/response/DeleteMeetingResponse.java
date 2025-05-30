package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "DeleteMeetingResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class DeleteMeetingResponse {


    @Element(name = "DeleteMeetingResult", required = false)
    private DeleteMeetingResult deleteMeetingResult;

    public DeleteMeetingResult getDeleteMeetingResult() {
        return deleteMeetingResult;
    }

    public void setDeleteMeetingResult(DeleteMeetingResult deleteMeetingResult) {
        this.deleteMeetingResult = deleteMeetingResult;
    }
}

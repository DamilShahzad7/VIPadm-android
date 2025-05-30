package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "AddParticipantsResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class AddParticipantsResponse {

    @Element(name = "Error", required = false)
    private Error error;

    @Element(name = "AddParticipantsResult", required = false)
    private AddParticipantsResult addParticipantsResult;

    public AddParticipantsResult getAddParticipantsResult() {
        return addParticipantsResult;
    }

    public void setAddParticipantsResult(AddParticipantsResult addParticipantsResult) {
        this.addParticipantsResult = addParticipantsResult;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}

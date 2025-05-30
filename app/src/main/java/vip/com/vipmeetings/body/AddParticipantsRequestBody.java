package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.AddParticipant;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class AddParticipantsRequestBody {


    @Element(name = "AddParticipants", required = false)
    private AddParticipant addParticipants;

    public AddParticipant getAddParticipants() {
        return addParticipants;
    }

    public void setAddParticipants(AddParticipant addParticipants) {
        this.addParticipants = addParticipants;
    }
}

package vip.com.vipmeetings.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.models.Empty;
import vip.com.vipmeetings.models.Meetings;
import vip.com.vipmeetings.models.Messages;

@Root(name = "GetMessagesResult", strict = false)
public class GetMessagesResult {

    @Element(name = "Error", required = false)
    private vip.com.vipmeetings.models.Error Error;


    @Element(name = "Empty", required = false)
    private Empty empty;


    @Element(name = "Messages", required = false)
    private Messages messages;

    public Empty getEmpty() {
        return empty;
    }

    public void setEmpty(Empty empty) {
        this.empty = empty;
    }

    public Messages getMessages() {
        return messages;
    }

    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    public vip.com.vipmeetings.models.Error getError() {
        return Error;
    }

    public void setError(vip.com.vipmeetings.models.Error error) {
        Error = error;
    }




}

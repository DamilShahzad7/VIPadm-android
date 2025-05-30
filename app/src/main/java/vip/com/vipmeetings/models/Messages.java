package vip.com.vipmeetings.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Srinath on 05/07/17.
 */


@Root(name = "Messages",strict = false)
public class Messages {

    @ElementList(name = "Message",required = false,inline = true)
    List<Message> Message;

    public List<Message> getMessage() {
        return Message;
    }

    public void setMessage(List<Message> message) {
        Message = message;
    }
}

package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.AddContactLeave;
import vip.com.vipmeetings.request.SaveEvent;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class SaveEventRequestBody {


    @Element(name = "SaveEvent", required = false)
    private SaveEvent saveEvent;

    public SaveEvent getSaveEvent() {
        return saveEvent;
    }

    public void setSaveEvent(SaveEvent saveEvent) {
        this.saveEvent = saveEvent;
    }
}

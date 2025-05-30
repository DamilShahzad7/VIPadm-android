package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "SaveEventResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class SaveEventResponse {


    @Element(name = "SaveEventResult", required = false)
    private SaveEventResult saveEventResult;

    public SaveEventResult getSaveEventResult() {
        return saveEventResult;
    }

    public void setSaveEventResult(SaveEventResult saveEventResult) {
        this.saveEventResult = saveEventResult;
    }
}

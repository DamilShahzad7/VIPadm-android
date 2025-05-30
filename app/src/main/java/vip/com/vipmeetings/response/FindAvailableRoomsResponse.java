package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "FindAvailableRoomsResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class FindAvailableRoomsResponse {


    @Element(name = "FindAvailableRoomsResult", required = false)
    private FindAvailableRoomsResult findAvailableRoomsResult;

    public FindAvailableRoomsResult getFindAvailableRoomsResult() {
        return findAvailableRoomsResult;
    }

    public void setFindAvailableRoomsResult(FindAvailableRoomsResult findAvailableRoomsResult) {
        this.findAvailableRoomsResult = findAvailableRoomsResult;
    }
}

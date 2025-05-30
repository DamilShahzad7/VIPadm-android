package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "FindRoomAvailabilityResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class FindRoomAvailabilityResponse {


    @Element(name = "FindRoomAvailabilityResult", required = false)
    private FindRoomAvailabilityResult findRoomAvailabilityResult;

    public FindRoomAvailabilityResult getFindRoomAvailabilityResult() {
        return findRoomAvailabilityResult;
    }

    public void setFindRoomAvailabilityResult(FindRoomAvailabilityResult findRoomAvailabilityResult) {
        this.findRoomAvailabilityResult = findRoomAvailabilityResult;
    }
}

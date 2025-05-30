package vip.com.vipmeetings.response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "GetRoomBookingsResponse", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetRoomBookingsResponse {


    @Element(name = "GetRoomBookingsResult", required = false)
    private GetRoomBookingsResult getRoomBookingsResult;

    public GetRoomBookingsResult getGetRoomBookingsResult() {
        return getRoomBookingsResult;
    }

    public void setGetRoomBookingsResult(GetRoomBookingsResult getRoomBookingsResult) {
        this.getRoomBookingsResult = getRoomBookingsResult;
    }
}

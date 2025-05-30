package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.DeleteMeeting;
import vip.com.vipmeetings.request.GetRoomBookings;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetRoomBookingsRequestBody {


    @Element(name = "GetRoomBookings", required = false)
    private GetRoomBookings getRoomBookings;

    public GetRoomBookings getGetRoomBookings() {
        return getRoomBookings;
    }

    public void setGetRoomBookings(GetRoomBookings getRoomBookings) {
        this.getRoomBookings = getRoomBookings;
    }
}

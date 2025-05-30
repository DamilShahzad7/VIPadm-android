package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.AddContactLeave;
import vip.com.vipmeetings.request.GetRooms;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class GetRoomsRequestBody {


    @Element(name = "GetRooms", required = false)
    private GetRooms getRooms;

    public GetRooms getGetRooms() {
        return getRooms;
    }

    public void setGetRooms(GetRooms getRooms) {
        this.getRooms = getRooms;
    }
}

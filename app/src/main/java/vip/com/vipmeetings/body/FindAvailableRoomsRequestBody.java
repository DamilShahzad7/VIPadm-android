package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.DeleteMeeting;
import vip.com.vipmeetings.request.FindAvailableRooms;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class FindAvailableRoomsRequestBody {


    @Element(name = "FindAvailableRooms", required = false)
    private FindAvailableRooms findAvailableRooms;

    public FindAvailableRooms getFindAvailableRooms() {
        return findAvailableRooms;
    }

    public void setFindAvailableRooms(FindAvailableRooms findAvailableRooms) {
        this.findAvailableRooms = findAvailableRooms;
    }
}

package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.AddContactLeave;
import vip.com.vipmeetings.request.FindRoomAvailability;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class FindRoomRequestBody {


    @Element(name = "FindRoomAvailability", required = false)
    private FindRoomAvailability findRoomAvailability;

    public FindRoomAvailability getFindRoomAvailability() {
        return findRoomAvailability;
    }

    public void setFindRoomAvailability(FindRoomAvailability findRoomAvailability) {
        this.findRoomAvailability = findRoomAvailability;
    }
}

package vip.com.vipmeetings.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.models.Empty;
import vip.com.vipmeetings.models.RoomAvailability;

@Root(name = "FindRoomAvailabilityResult", strict = false)
public class FindRoomAvailabilityResult {

    @Element(name = "Error", required = false)
    private vip.com.vipmeetings.models.Error Error;

    @Element(name = "Empty", required = false)
    private Empty empty;


    @Element(name = "RoomAvailability", required = false)
    private RoomAvailability roomAvailability;

    public RoomAvailability getRoomAvailability() {
        return roomAvailability;
    }

    public void setRoomAvailability(RoomAvailability roomAvailability) {
        this.roomAvailability = roomAvailability;
    }

    public vip.com.vipmeetings.models.Error getError() {
        return Error;
    }

    public void setError(vip.com.vipmeetings.models.Error error) {
        Error = error;
    }

    public Empty getEmpty() {
        return empty;
    }

    public void setEmpty(Empty empty) {
        this.empty = empty;
    }
}

package vip.com.vipmeetings.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import vip.com.vipmeetings.models.ArrayOfRoomProfile;
import vip.com.vipmeetings.models.Empty;
import vip.com.vipmeetings.models.RoomProfile;
import vip.com.vipmeetings.models.Rooms;
import vip.com.vipmeetings.models.Status;

@Root(name = "FindAvailableRoomsResult", strict = false)
public class FindAvailableRoomsResult {

    @Element(name = "Error", required = false)
    private vip.com.vipmeetings.models.Error Error;

    @Element(name = "Empty", required = false)
    private Empty empty;


    @ElementList(entry = "RoomProfile", inline = true,required = false)
    List<vip.com.vipmeetings.models.RoomProfile> RoomProfile;

    public List<RoomProfile> getRoomProfile() {
        return RoomProfile;
    }

    public void setRoomProfile(List<RoomProfile> roomProfile) {
        RoomProfile = roomProfile;
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

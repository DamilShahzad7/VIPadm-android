package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Srinath on 26/06/17.
 */
@Root(name = "RoomAvailability",strict = false)
public class RoomAvailability {

    @Element(name = "Status",required=false)
    public Status Status;
    @Element(name = "Error",required=false)
    public Error Error;
    @Element(name = "RoomSelectionId",required=false)
    public RoomSelectionId RoomSelectionId;


    public vip.com.vipmeetings.models.RoomSelectionId getRoomSelectionId() {
        return RoomSelectionId;
    }

    public void setRoomSelectionId(vip.com.vipmeetings.models.RoomSelectionId roomSelectionId) {
        RoomSelectionId = roomSelectionId;
    }

    public vip.com.vipmeetings.models.Status getStatus() {
        return Status;
    }

    public void setStatus(vip.com.vipmeetings.models.Status status) {
        Status = status;
    }

    public vip.com.vipmeetings.models.Error getError() {
        return Error;
    }

    public void setError(vip.com.vipmeetings.models.Error error) {
        Error = error;
    }
}

package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class RoomProfile {


    @Element(name = "Status", required = false)
    private String Status;
    @Element(name = "RoomId", required = false)
    private String RoomId;
    @Element(name = "Image", required = false)
    private String Image;
    @Element(name = "FloorNumber", required = false)
    private String FloorNumber;
    @Element(name = "RoomName", required = false)
    private String RoomName;
    @Element(name = "Capacity", required = false)
    private String Capacity;
    @Element(name = "Error", required = false)
    private String Error;
    @Element(name = "ClientID", required = false)
    private String ClientID;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public String getRoomId() {
        return RoomId;
    }

    public void setRoomId(String roomId) {
        RoomId = roomId;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getFloorNumber() {
        return FloorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        FloorNumber = floorNumber;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }

    public String getCapacity() {
        return Capacity;
    }

    public void setCapacity(String capacity) {
        Capacity = capacity;
    }
}
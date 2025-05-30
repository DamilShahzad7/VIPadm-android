package vip.com.vipmeetings.models;

import com.google.gson.annotations.Expose;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.List;

/**
 * Created by Srinath on 03/07/17.
 */

@Root(name = "Rooms",strict = false)
public class RoomsOccupancyList {

    @ElementList(entry = "Rooms", inline = true,required = false)
    List<RoomOccupancyModel> Rooms;

    @ElementList(entry = "Room", inline = true,required = false)
    List<RoomOccupancyModel> room;


    public List<RoomOccupancyModel> getRoom() {
        return room;
    }

    public void setRoom(List<RoomOccupancyModel> room) {
        this.room = room;
    }

    @Element(name = "Empty", required = false)
    private Empty empty;

    public Empty getEmpty() {
        return empty;
    }

    public void setEmpty(Empty empty) {
        this.empty = empty;
    }


    public List<RoomOccupancyModel> getRooms() {
        return Rooms;
    }

    public void setRooms(List<RoomOccupancyModel> rooms) {
        Rooms = rooms;
    }
}

package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;
@Root(strict = false)
public class Rooms
{

    @Element(required = false)
    Empty Empty;


    @ElementList(entry = "Room", inline = true,required = false)
    List<Room> room;

    public List<Room> getRoom() {
        return room;
    }

    public void setRoom(List<Room> room) {
        this.room = room;
    }

    public Empty getEmpty() {
        return Empty;
    }

    public void setEmpty(Empty empty) {
        Empty = empty;
    }
}
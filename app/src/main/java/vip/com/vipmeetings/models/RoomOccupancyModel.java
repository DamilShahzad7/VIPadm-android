package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Srinath on 03/07/17.
 */
@Root(name = "Room",strict = false)
public class RoomOccupancyModel {

    @Element(name = "RoomId", required = false)
    private String RoomId;
    @Element(name = "BookingDate", required = false)
    private String BookingDate;
    @Element(name = "Times", required = false)
    private String Times;
    @Element(name = "RoomName", required = false)
    private String RoomName;
    @Element(name = "HostName", required = false)
    private String HostName;
    @Element(name = "Occupancy", required = false)
    private String Occupancy;


    @Override
    public String toString() {
        return getTimes();
    }

    public String getRoomId ()
    {
        return RoomId;
    }

    public void setRoomId (String RoomId)
    {
        this.RoomId = RoomId;
    }

    public String getBookingDate ()
    {
        return BookingDate;
    }

    public void setBookingDate (String BookingDate)
    {
        this.BookingDate = BookingDate;
    }

    public String getTimes ()
    {
        return Times;
    }

    public void setTimes (String Times)
    {
        this.Times = Times;
    }

    public String getRoomName ()
    {
        return RoomName;
    }

    public void setRoomName (String RoomName)
    {
        this.RoomName = RoomName;
    }

    public String getHostName ()
    {
        return HostName;
    }

    public void setHostName (String HostName)
    {
        this.HostName = HostName;
    }

    public String getOccupancy ()
    {
        return Occupancy;
    }

    public void setOccupancy (String Occupancy)
    {
        this.Occupancy = Occupancy;
    }

}

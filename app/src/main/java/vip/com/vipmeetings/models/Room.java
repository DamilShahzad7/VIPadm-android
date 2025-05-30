package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Room
{
    @Element(name = "WiFi",required=false)
    private String WiFi;
    @Element(name = "SmartBoard",required=false)
    private String SmartBoard;
    @Element(name = "status",required=false)
    private String status;
    @Element(name = "priceperday",required=false)
    private String priceperday;
    @Element(name = "Image1",required=false)
    private String Image1;
    @Element(name = "RoomImage",required=false)
    private String RoomImage;
    @Element(name = "Image2",required=false)
    private String Image2;
    @Element(name = "priceperhour",required=false)
    private String priceperhour;
    @Element(name = "Projector",required=false)
    private String Projector;
    @Element(name = "DVD",required=false)
    private String DVD;
    @Element(name = "InternetCable",required=false)
    private String InternetCable;
    @Element(name = "RoomId",required=false)
    private String RoomId;
    @Element(name = "WhiteBoard",required=false)
    private String WhiteBoard;
    @Element(name = "VideoConference",required=false)
    private String VideoConference;
    @Element(name = "FlipOver",required=false)
    private String FlipOver;
    @Element(name = "FloorNumber",required=false)
    private String FloorNumber;
    @Element(name = "IntCable",required=false)
    private String IntCable;
    @Element(name = "RoomName",required=false)
    private String RoomName;
    @Element(name = "HostName",required=false)
    private String HostName;
    @Element(name = "BookingDate",required=false)
    private String BookingDate;

    @Element(name = "Times",required=false)
    private String Times;

    @Element(name = "Occupancy",required=false)
    private String Occupancy;

    @Element(name = "Capacity",required=false)
    private String Capacity;
    @Element(name = "Audio",required=false)
    private String Audio;
    @Element(name = "IntWireless",required=false)
    private String IntWireless;
    @Element(name = "ConferencePhone",required=false)
    private String ConferencePhone;
    @Element(name = "TV",required=false)
    private String TV;
    @Element(name = "PC",required=false)
    private String PC;

    public String getHostName() {
        return HostName;
    }

    public void setHostName(String hostName) {
        HostName = hostName;
    }

    public String getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(String bookingDate) {
        BookingDate = bookingDate;
    }

    public String getTimes() {
        return Times;
    }

    public void setTimes(String times) {
        Times = times;
    }

    public String getOccupancy() {
        return Occupancy;
    }

    public void setOccupancy(String occupancy) {
        Occupancy = occupancy;
    }

    public String getWiFi ()
    {
        return WiFi;
    }

    public void setWiFi (String WiFi)
    {
        this.WiFi = WiFi;
    }

    public String getSmartBoard ()
    {
        return SmartBoard;
    }

    public void setSmartBoard (String SmartBoard)
    {
        this.SmartBoard = SmartBoard;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getPriceperday ()
    {
        return priceperday;
    }

    public void setPriceperday (String priceperday)
    {
        this.priceperday = priceperday;
    }

    public String getImage1 ()
    {
        return Image1;
    }

    public void setImage1 (String Image1)
    {
        this.Image1 = Image1;
    }

    public String getRoomImage ()
    {
        return RoomImage;
    }

    public void setRoomImage (String RoomImage)
    {
        this.RoomImage = RoomImage;
    }

    public String getImage2 ()
    {
        return Image2;
    }

    public void setImage2 (String Image2)
    {
        this.Image2 = Image2;
    }

    public String getPriceperhour ()
    {
        return priceperhour;
    }

    public void setPriceperhour (String priceperhour)
    {
        this.priceperhour = priceperhour;
    }

    public String getProjector ()
    {
        return Projector;
    }

    public void setProjector (String Projector)
    {
        this.Projector = Projector;
    }

    public String getDVD ()
    {
        return DVD;
    }

    public void setDVD (String DVD)
    {
        this.DVD = DVD;
    }

    public String getInternetCable ()
    {
        return InternetCable;
    }

    public void setInternetCable (String InternetCable)
    {
        this.InternetCable = InternetCable;
    }

    public String getRoomId ()
    {
        return RoomId;
    }

    public void setRoomId (String RoomId)
    {
        this.RoomId = RoomId;
    }

    public String getWhiteBoard ()
    {
        return WhiteBoard;
    }

    public void setWhiteBoard (String WhiteBoard)
    {
        this.WhiteBoard = WhiteBoard;
    }

    public String getVideoConference ()
    {
        return VideoConference;
    }

    public void setVideoConference (String VideoConference)
    {
        this.VideoConference = VideoConference;
    }

    public String getFlipOver ()
    {
        return FlipOver;
    }

    public void setFlipOver (String FlipOver)
    {
        this.FlipOver = FlipOver;
    }

    public String getFloorNumber ()
    {
        return FloorNumber;
    }

    public void setFloorNumber (String FloorNumber)
    {
        this.FloorNumber = FloorNumber;
    }

    public String getIntCable ()
    {
        return IntCable;
    }

    public void setIntCable (String IntCable)
    {
        this.IntCable = IntCable;
    }

    public String getRoomName ()
    {
        return RoomName;
    }

    public void setRoomName (String RoomName)
    {
        this.RoomName = RoomName;
    }

    public String getCapacity ()
    {
        return Capacity;
    }

    public void setCapacity (String Capacity)
    {
        this.Capacity = Capacity;
    }

    public String getAudio ()
    {
        return Audio;
    }

    public void setAudio (String Audio)
    {
        this.Audio = Audio;
    }

    public String getIntWireless ()
    {
        return IntWireless;
    }

    public void setIntWireless (String IntWireless)
    {
        this.IntWireless = IntWireless;
    }

    public String getConferencePhone ()
    {
        return ConferencePhone;
    }

    public void setConferencePhone (String ConferencePhone)
    {
        this.ConferencePhone = ConferencePhone;
    }

    public String getTV ()
    {
        return TV;
    }

    public void setTV (String TV)
    {
        this.TV = TV;
    }

    public String getPC ()
    {
        return PC;
    }

    public void setPC (String PC)
    {
        this.PC = PC;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [WiFi = "+WiFi+", SmartBoard = "+SmartBoard+", status = "+status+", priceperday = "+priceperday+", Image1 = "+Image1+", RoomImage = "+RoomImage+", Image2 = "+Image2+", priceperhour = "+priceperhour+", Projector = "+Projector+", DVD = "+DVD+", InternetCable = "+InternetCable+", RoomId = "+RoomId+", WhiteBoard = "+WhiteBoard+", VideoConference = "+VideoConference+", FlipOver = "+FlipOver+", FloorNumber = "+FloorNumber+", IntCable = "+IntCable+", RoomName = "+RoomName+", Capacity = "+Capacity+", Audio = "+Audio+", IntWireless = "+IntWireless+", ConferencePhone = "+ConferencePhone+", TV = "+TV+", PC = "+PC+"]";
    }
}
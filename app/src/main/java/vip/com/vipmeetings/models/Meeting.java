package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Srinath on 30/05/17.
 */
@Root(strict = false,name = "Meeting")
public class Meeting {


    @Element(name = "ParticipantNames",required=false)
    private String ParticipantNames;

    @Element(name = "Participants",required=false)
    private String Participants;
    @Element(name = "MeetingId",required=false)
    private String MeetingId;
    @Element(name = "Description",required=false)
    private String Description;
    @Element(name = "ToDate",required=false)
    private String ToDate;
    @Element(name = "RoomId",required=false)
    private String RoomId;
    @Element(name = "ToTime",required=false)
    private String ToTime;
    @Element(name = "FromDate",required=false)
    private String FromDate;
    @Element(name = "FromTime",required=false)
    private String FromTime;
    @Element(name = "RoomImage",required=false)
    private String RoomImage;
    @Element(name = "RoomName",required=false)
    private String RoomName;
    @Element(name = "UserId",required=false)
    private String UserId;
    @Element(name = "HostName",required=false)
    private String HostName;
    @Element(name = "Location",required=false)
    private String Location;
    @Element(name = "CityName",required=false)
    private String CityName;
    @Element(name = "VisitorId",required = false)
    private String VisitorId;
    @Element(name = "Source",required = false)
    private String Source;
    @Element(name = "ConfirmedParticipants",required = false)
    private String ConfirmedParticipants;
    @Element(name = "CancelledParticipants",required = false)
    private String CancelledParticipants;
    @Element(name = "VisitorName",required = false)
    private String VisitorName;
    @Element(name = "VisitorMobile",required = false)
    private String VisitorMobile;
    @Element(name = "VisitorEmail",required = false)
    private String VisitorEmail;
    @Element(name = "VisitorCompany",required = false)
    private String VisitorCompany;
    @Element(name = "TeamsVisitors",required = false)
    private String TeamsVisitors;
    @Element(name = "GoogleVisitors",required = false)
    private String GoogleVisitors;
    @Element(name = "ClientID",required = false)
    private String ClientID;
    @Element(name = "UnNamedGuests",required = false)
    private String unNamedGuests;

    @Element(name = "InTime",required = false)
    private String InTime;
    @Element(name = "OutTime",required = false)
    private String OutTime;
    public String getOutTime() {
        return OutTime;
    }

    public void setOutTime(String outTime) {
        OutTime = outTime;
    }

    public String getInTime() {
        return InTime;
    }

    public void setInTime(String inTime) {
        InTime = inTime;
    }


    public String getUnNamedGuests() {
        return unNamedGuests;
    }

    public void setUnNamedGuests(String unNamedGuests) {
        this.unNamedGuests = unNamedGuests;
    }



    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getParticipantNames() {
        return ParticipantNames;
    }

    public void setParticipantNames(String participantNames) {
        ParticipantNames = participantNames;
    }

    public String getParticipants ()
    {
        return Participants;
    }

    public void setParticipants (String Participants)
    {
        this.Participants = Participants;
    }

    public String getMeetingId ()
    {
        return MeetingId;
    }

    public void setMeetingId (String MeetingId)
    {
        this.MeetingId = MeetingId;
    }

    public String getDescription ()
    {
        return Description;
    }

    public void setDescription (String Description)
    {
        this.Description = Description;
    }

    public String getToDate ()
    {
        return ToDate;
    }

    public void setToDate (String ToDate)
    {
        this.ToDate = ToDate;
    }

    public String getRoomId ()
    {
        return RoomId;
    }

    public void setRoomId (String RoomId)
    {
        this.RoomId = RoomId;
    }

    public String getToTime ()
    {
        return ToTime;
    }

    public void setToTime (String ToTime)
    {
        this.ToTime = ToTime;
    }

    public String getFromDate ()
    {
        return FromDate;
    }

    public void setFromDate (String FromDate)
    {
        this.FromDate = FromDate;
    }

    public String getFromTime ()
    {
        return FromTime;
    }

    public void setFromTime (String FromTime)
    {
        this.FromTime = FromTime;
    }

    public String getRoomImage ()
    {
        return RoomImage;
    }

    public void setRoomImage (String RoomImage)
    {
        this.RoomImage = RoomImage;
    }

    public String getRoomName ()
    {
        return RoomName;
    }

    public void setRoomName (String RoomName)
    {
        this.RoomName = RoomName;
    }

    public String getUserId ()
    {
        return UserId;
    }

    public void setUserId (String UserId)
    {
        this.UserId = UserId;
    }

    public String getHostName ()
    {
        return HostName;
    }

    public void setHostName (String HostName)
    {
        this.HostName = HostName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        this.CityName = cityName;
    }


    public String getVisitorId() {
        return VisitorId;
    }

    public void setVisitorId(String visitorId) {
        VisitorId = visitorId;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getConfirmedParticipants() {
        return ConfirmedParticipants;
    }

    public void setConfirmedParticipants(String confirmedParticipants) {
        ConfirmedParticipants = confirmedParticipants;
    }

    public String getCancelledParticipants() {
        return CancelledParticipants;
    }

    public void setCancelledParticipants(String cancelledParticipants) {
        CancelledParticipants = cancelledParticipants;
    }

    public String getVisitorName() {
        return VisitorName;
    }

    public void setVisitorName(String visitorName) {
        VisitorName = visitorName;
    }

    public String getVisitorMobile() {
        return VisitorMobile;
    }

    public void setVisitorMobile(String visitorMobile) {
        VisitorMobile = visitorMobile;
    }

    public String getVisitorEmail() {
        return VisitorEmail;
    }

    public void setVisitorEmail(String visitorEmail) {
        VisitorEmail = visitorEmail;
    }

    public String getVisitorCompany() {
        return VisitorCompany;
    }

    public void setVisitorCompany(String visitorCompany) {
        VisitorCompany = visitorCompany;
    }

        public String getTeamsVisitors() {
        return TeamsVisitors;
    }

    public void setTeamsVisitors(String teamsVisitors) {
        TeamsVisitors = teamsVisitors;
    }

    public String getGoogleVisitors() {
        return GoogleVisitors;
    }

    public void setGoogleVisitors(String googleVisitors) {
        GoogleVisitors = googleVisitors;
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }
}

package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "EvacuationResponsible", strict = false)
public class EvacuationResponsible {

    @Element(name = "EvacuationId", required = false)
    private String EvacuationId;
    @Element(name = "PlaceID", required = false)
    private String A_sPlaceId;


    //areaname name =not empty 6 buttons empty show 5 buttons
    // areaname = admin id= not empty=show 6
    //areaname empty admin id= empty show 5
    //areaname normal user
    //adminid normal user
    //
    @Element(name = "PlaceName", required = false)
    private String PlaceName;

    @Element(name = "AdminID", required = false)
    private String AdminID;

    @Element(name = "AreaName", required = false)
    private String AreaName;
    @Element(name = "Floor", required = false)
    private String Floor;
    @Element(name = "Company", required = false)
    private String Company;

    @Element(name = "FULLNAME", required = false)
    private String FULLNAME;
    @Element(name = "StartTime", required = false)
    private String StartTime;

    @Element(name = "ClearDate", required = false)
    private String ClearDate;

    @Element(name = "ClearTime", required = false)
    private String ClearTime;

    @Element(name = "Status", required = false)
    private String STATUS;

    @Element(name = "ClientId", required = false)
    private String ClientId;

    @Element(name = "ModifiedDate", required = false)
    private String ModifiedDate;

    @Element(name = "TotalResponsibles", required = false)
    private String TotalResponsibles;

    @Element(name = "Barcode", required = false)
    private String Barcode;

    @Element(name = "Name", required = false)
    private String Name;

    @Element(name = "Mobile", required = false)
    private String Mobile;

    @Element(name = "Email", required = false)
    private String Email;

    @Element(name = "Priority", required = false)
    private String Priority;

    @Element(name = "UserType", required = false)
    private String UserType;

    @Element(name = "UserCompany", required = false)
    private String UserCompany;

    @Element(name = "EVACUATIONSTATUS", required = false)
    private String EVACUATIONSTATUS;

    @Element(name = "STATUS", required = false)
    private Status_Evacuate status_status;


    @Element(name = "Location", required = false)
    private String Location;


    @Element(name = "OTP", required = false)
    private String OTP;


    @Element(name = "EndTime", required = false)
    private String EndTime;

    @Element(name = "UserAvailability", required = false)
    private String UserAvailability;

    @Element(name = "PhotoUrl", required = false)
    private String PhotoUrl;


    @Override
    public boolean equals(Object obj) {

        if (obj instanceof EvacuationResponsible) {

            if (getBarcode().equalsIgnoreCase(((EvacuationResponsible) obj).getBarcode())) {
                return true;
            }

        }

        return false;
    }

    @Override
    public int hashCode() {
        return getBarcode().hashCode();
    }

    public String getClearDate() {
        return ClearDate;
    }

    public void setClearDate(String clearDate) {
        ClearDate = clearDate;
    }

    @Override
    public String toString() {
        return getAreaName();
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public void setPlaceName(String placeName) {
        PlaceName = placeName;
    }

    public String getAdminID() {
        return AdminID;
    }

    public void setAdminID(String adminID) {
        AdminID = adminID;
    }

    public String getFULLNAME() {
        return FULLNAME;
    }

    public void setFULLNAME(String FULLNAME) {
        this.FULLNAME = FULLNAME;
    }

    public String getEVACUATIONSTATUS() {
        return EVACUATIONSTATUS;
    }

    public void setEVACUATIONSTATUS(String EVACUATIONSTATUS) {
        this.EVACUATIONSTATUS = EVACUATIONSTATUS;
    }

    public String getUserAvailability() {
        return UserAvailability;
    }

    public void setUserAvailability(String userAvailability) {
        UserAvailability = userAvailability;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getUserCompany() {
        return UserCompany;
    }

    public void setUserCompany(String userCompany) {
        UserCompany = userCompany;
    }

    public String getA_sPlaceId() {
        return A_sPlaceId;
    }

    public void setA_sPlaceId(String a_sPlaceId) {
        A_sPlaceId = a_sPlaceId;
    }

    public String getTotalResponsibles() {
        return TotalResponsibles;
    }

    public void setTotalResponsibles(String totalResponsibles) {
        TotalResponsibles = totalResponsibles;
    }

    public Status_Evacuate getStatus_status() {
        return status_status;
    }

    public void setStatus_status(Status_Evacuate status_status) {
        this.status_status = status_status;
    }

    public String getEvacuationId() {
        return EvacuationId;
    }

    public void setEvacuationId(String EvacuationId) {
        this.EvacuationId = EvacuationId;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String Barcode) {
        this.Barcode = Barcode;
    }

    public String getClearTime() {
        return ClearTime;
    }

    public void setClearTime(String ClearTime) {
        this.ClearTime = ClearTime;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getClientId() {
        return ClientId;
    }

    public void setClientId(String ClientId) {
        this.ClientId = ClientId;
    }

    public String getFloor() {
        return Floor;
    }

    public void setFloor(String Floor) {
        this.Floor = Floor;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String Priority) {
        this.Priority = Priority;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }


    public String getModifiedDate() {
        return ModifiedDate;
    }

    public void setModifiedDate(String ModifiedDate) {
        this.ModifiedDate = ModifiedDate;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String StartTime) {
        this.StartTime = StartTime;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String Company) {
        this.Company = Company;
    }

}

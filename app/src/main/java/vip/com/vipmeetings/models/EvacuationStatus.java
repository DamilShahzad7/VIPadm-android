package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "EvacuationStatus", strict = false)
public class EvacuationStatus {

    @Element(name = "EvacuationId", required = false)
    String EvacuationId;
    @Element(name = "PlaceID", required = false)
    String PlaceID;
    @Element(name = "AreaName", required = false)
    String AreaName;
    @Element(name = "Floor", required = false)
    String Floor;
    @Element(name = "Company", required = false)
    String Company;
    @Element(name = "StartTime", required = false)
    String StartTime;
    @Element(name = "EndTime", required = false)
    String EndTime;
    @Element(name = "ClearTime", required = false)
    String ClearTime;
    @Element(name = "Status", required = false)
    String Status;
    @Element(name = "ClientId", required = false)
    String ClientId;
    @Element(name = "ModifiedDate", required = false)
    String ModifiedDate;
    @Element(name = "TotalResponsibles", required = false)
    String TotalResponsibles;
    @Element(name = "Barcode", required = false)
    String Barcode;
    @Element(name = "Name", required = false)
    String Name;
    @Element(name = "Mobile", required = false)
    String Mobile;
    @Element(name = "Email", required = false)
    String Email;
    @Element(name = "Priority", required = false)
    String Priority;
    @Element(name = "UserType", required = false)
    String UserType;
    @Element(name = "UserCompany", required = false)
    String UserCompany;

    @Element(name = "UserAvailability", required = false)
    String UserAvailability;

    @Element(name = "PhotoUrl", required = false)
    String PhotoUrl;

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

    public String getEvacuationId() {
        return EvacuationId;
    }

    public void setEvacuationId(String evacuationId) {
        EvacuationId = evacuationId;
    }

    public String getPlaceID() {
        return PlaceID;
    }

    public void setPlaceID(String placeID) {
        PlaceID = placeID;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getFloor() {
        return Floor;
    }

    public void setFloor(String floor) {
        Floor = floor;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getClearTime() {
        return ClearTime;
    }

    public void setClearTime(String clearTime) {
        ClearTime = clearTime;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getClientId() {
        return ClientId;
    }

    public void setClientId(String clientId) {
        ClientId = clientId;
    }

    public String getModifiedDate() {
        return ModifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        ModifiedDate = modifiedDate;
    }

    public String getTotalResponsibles() {
        return TotalResponsibles;
    }

    public void setTotalResponsibles(String totalResponsibles) {
        TotalResponsibles = totalResponsibles;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
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
}

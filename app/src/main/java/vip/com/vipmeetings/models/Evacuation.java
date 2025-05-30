package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Srinath on 04/02/18.
 */
@Root(name = "Evacuation", strict = false)
public class Evacuation {


   transient public List<Evacuation> evacuationList = null;
    transient  public List<Evacuation> evacuationListOLD = null;

    @Element(name = "EvacuationStatus", required = false)
    EvacuationStatus EvacuationStatus;

    @Element(name = "ClearedByName", required = false)
    String ClearedByName;

    @Element(name = "AdminID", required = false)
    String AdminID;

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

    @Element(name = "ClearDate", required = false)
    String ClearDate;

    public String getAdminID() {
        return AdminID;
    }

    public void setAdminID(String adminID) {
        AdminID = adminID;
    }

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


    public List<Evacuation> getEvacuationList() {
        return evacuationList;
    }

    public void setEvacuationList(List<Evacuation> evacuationList) {
        this.evacuationList = evacuationList;
    }

    public List<Evacuation> getEvacuationListOLD() {
        return evacuationListOLD;
    }

    public void setEvacuationListOLD(List<Evacuation> evacuationListOLD) {
        this.evacuationListOLD = evacuationListOLD;
    }

    @Override
    public String toString() {
        if(getClearTime()!=null && getClearTime().trim().length()>0)
        return getClearTime();
        return super.toString();
    }

    public String getClearDate() {
        return ClearDate;
    }

    public void setClearDate(String clearDate) {
        ClearDate = clearDate;
    }

    public String getUserAvailability() {
        return UserAvailability;
    }

    public void setUserAvailability(String userAvailability) {
        UserAvailability = userAvailability;
    }

    public String getClearedByName() {
        return ClearedByName;
    }

    public void setClearedByName(String clearedByName) {
        ClearedByName = clearedByName;
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


    public vip.com.vipmeetings.models.EvacuationStatus getEvacuationStatus() {
        return EvacuationStatus;
    }

    public void setEvacuationStatus(vip.com.vipmeetings.models.EvacuationStatus evacuationStatus) {
        EvacuationStatus = evacuationStatus;
    }
}

package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Sekhar on 04/02/18.
 */
@Root(name = "Evacuation", strict = false)
public class EvacuationInformation {

   transient public List<EvacuationInformation> evacuationInformationList = null;
    transient  public List<EvacuationInformation> evacuationInformationListOLD = null;


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
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

    public String getStartBy() {
        return StartBy;
    }

    public void setStartBy(String startBy) {
        StartBy = startBy;
    }

    public String getEndBy() {
        return EndBy;
    }

    public void setEndBy(String endBy) {
        EndBy = endBy;
    }

    public String getRn() {
        return rn;
    }

    public void setRn(String rn) {
        this.rn = rn;
    }

    @Element(name = "ID", required = false)
    String ID;

    @Element(name = "City", required = false)
    String City;
    @Element(name = "StartTime", required = false)
    String StartTime;
    @Element(name = "EndTime", required = false)
    String EndTime;

    @Element(name = "Status", required = false)
    String Status;
    @Element(name = "ClientId", required = false)
    String ClientId;
    @Element(name = "ModifiedDate", required = false)
    String ModifiedDate;
    @Element(name = "StartBy", required = false)
    String StartBy;
    @Element(name = "EndBy", required = false)
    String EndBy;
    @Element(name = "rn", required = false)
    String rn;

}

package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Srinath on 07/02/18.
 */
@Root(name = "EvacuationResponsible", strict = false)
public class AuthenticateEvacuationModel {

    @Element(name = "AreaName", required = false)
    String AreaName;
    @Element(name = "STATUS", required = false)
    String STATUS;
    @Element(name = "EVACUATIONID", required = false)
    String EVACUATIONID;
    @Element(name = "LOCATION", required = false)
    String LOCATION;
    @Element(name = "COMPANY", required = false)
    String COMPANY;
    @Element(name = "FLOOR", required = false)
    String FLOOR;
    @Element(name = "STARTTIME", required = false)
    String STARTTIME;
    @Element(name = "ENDTIME", required = false)
    String ENDTIME;
    @Element(name = "CLEARTIME", required = false)
    String CLEARTIME;
    @Element(name = "EVACUATIONSTATUS", required = false)
    String EVACUATIONSTATUS;
    @Element(name = "CLIENTID", required = false)
    String CLIENTID;
    @Element(name = "BARCODE", required = false)
    String BARCODE;
    @Element(name = "FULLNAME", required = false)
    String FULLNAME;
    @Element(name = "MOBILE", required = false)
    String MOBILE;
    @Element(name = "EMAIL", required = false)
    String EMAIL;
    @Element(name = "PRIORITY", required = false)
    String PRIORITY;
    @Element(name = "OTP", required = false)
    String OTP;

    @Element(name = "PlaceID", required = false)
    private String A_sPlaceId;


    @Element(name = "UserType", required = false)
    private String UserType;

    @Element(name = "UserCompany", required = false)
    private String UserCompany;

    @Element(name = "UserAvailability", required = false)
    private String UserAvailability;

    public String getUserAvailability() {
        return UserAvailability;
    }

    public void setUserAvailability(String userAvailability) {
        UserAvailability = userAvailability;
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

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getA_sPlaceId() {
        return A_sPlaceId;
    }

    public void setA_sPlaceId(String a_sPlaceId) {
        A_sPlaceId = a_sPlaceId;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getEVACUATIONID() {
        return EVACUATIONID;
    }

    public void setEVACUATIONID(String EVACUATIONID) {
        this.EVACUATIONID = EVACUATIONID;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }

    public String getCOMPANY() {
        return COMPANY;
    }

    public void setCOMPANY(String COMPANY) {
        this.COMPANY = COMPANY;
    }

    public String getFLOOR() {
        return FLOOR;
    }

    public void setFLOOR(String FLOOR) {
        this.FLOOR = FLOOR;
    }

    public String getSTARTTIME() {
        return STARTTIME;
    }

    public void setSTARTTIME(String STARTTIME) {
        this.STARTTIME = STARTTIME;
    }

    public String getENDTIME() {
        return ENDTIME;
    }

    public void setENDTIME(String ENDTIME) {
        this.ENDTIME = ENDTIME;
    }

    public String getCLEARTIME() {
        return CLEARTIME;
    }

    public void setCLEARTIME(String CLEARTIME) {
        this.CLEARTIME = CLEARTIME;
    }

    public String getEVACUATIONSTATUS() {
        return EVACUATIONSTATUS;
    }

    public void setEVACUATIONSTATUS(String EVACUATIONSTATUS) {
        this.EVACUATIONSTATUS = EVACUATIONSTATUS;
    }

    public String getCLIENTID() {
        return CLIENTID;
    }

    public void setCLIENTID(String CLIENTID) {
        this.CLIENTID = CLIENTID;
    }

    public String getBARCODE() {
        return BARCODE;
    }

    public void setBARCODE(String BARCODE) {
        this.BARCODE = BARCODE;
    }

    public String getFULLNAME() {
        return FULLNAME;
    }

    public void setFULLNAME(String FULLNAME) {
        this.FULLNAME = FULLNAME;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getPRIORITY() {
        return PRIORITY;
    }

    public void setPRIORITY(String PRIORITY) {
        this.PRIORITY = PRIORITY;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }
}


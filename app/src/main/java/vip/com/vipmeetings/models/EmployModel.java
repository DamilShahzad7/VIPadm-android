package vip.com.vipmeetings.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Created by Srinath on 30/05/17.
 */

@Root(name = "Employee", strict = false)
@Namespace(reference = "http://tempuri.org/",prefix = "xmlns")
public class EmployModel {


    @Element(name = "Error", required = false)
    private String Error;
    @Element(name = "COMPANY", required = false)
    private String COMPANY;
    @Element(name = "COMPANYLOGO", required = false)
    private String COMPANYLOGO;
    @Element(name = "COUNTRY", required = false)
    private String COUNTRY;
    @Element(name = "EMPCODE", required = false)
    private String EMPCODE;
    @Element(name = "STATUS", required = false)

    private String STATUS;
    @Element(name = "OTP", required = false)

    private String OTP;
    @Element(name = "QRCODE", required = false)

    private String QRCODE;
    @Element(name = "FIRSTNAME", required = false)

    private String FIRSTNAME;
    @Element(name = "QRIMAGE", required = false)

    private String QRIMAGE;
    @Element(name = "FULLNAME", required = false)

    private String FULLNAME;
    @Element(name = "MOBILE", required = false)

    private String MOBILE;

    @Element(name = "CLIENTID", required = false)

    private String CLIENTID;

    @Element(name = "BARCODE", required = false)

    private String BARCODE;
    @Element(name = "FAMILYNAME", required = false)

    private String FAMILYNAME;
    @Element(name = "PIN", required = false)

    private String PIN;
    @Element(name = "EMAIL", required = false)

    private String EMAIL;
    @Element(name = "CITY", required = false)

    private String CITY;
    @Element(name = "ACTIVE", required = false)

    private String ACTIVE;
    @Element(name = "PHOTO", required = false)

    private String PHOTO;

    @Element(name = "USERID", required = false)
    private String USERID;

    @Element(name = "INTIME", required = false)
    private String INTIME;

    @Element(name = "OUTTIME", required = false)
    private String OUTTIME;

    public String getINTIME() {
        return INTIME;
    }

    public void setINTIME(String INTIME) {
        this.INTIME = INTIME;
    }
    public String getOUTTIME() {
        return OUTTIME;
    }

    public void setOUTTIME(String OUTTIME) {
        this.OUTTIME = OUTTIME;
    }

    @Attribute(required = false)
    private String xmlns;

    public String getXmlns() {
        return xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    public String getCLIENTID() {
        return CLIENTID;
    }

    public void setCLIENTID(String CLIENTID) {
        this.CLIENTID = CLIENTID;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public String getCOMPANY() {
        return COMPANY;
    }

    public void setCOMPANY(String COMPANY) {
        this.COMPANY = COMPANY;
    }

    public String getCOMPANYLOGO() {
        return COMPANYLOGO;
    }

    public void setCOMPANYLOGO(String COMPANYLOGO) {
        this.COMPANYLOGO = COMPANYLOGO;
    }

    public String getCOUNTRY() {
        return COUNTRY;
    }

    public void setCOUNTRY(String COUNTRY) {
        this.COUNTRY = COUNTRY;
    }

    public String getEMPCODE() {
        return EMPCODE;
    }

    public void setEMPCODE(String EMPCODE) {
        this.EMPCODE = EMPCODE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public String getQRCODE() {
        return QRCODE;
    }

    public void setQRCODE(String QRCODE) {
        this.QRCODE = QRCODE;
    }

    public String getFIRSTNAME() {
        return FIRSTNAME;
    }

    public void setFIRSTNAME(String FIRSTNAME) {
        this.FIRSTNAME = FIRSTNAME;
    }

    public String getQRIMAGE() {
        return QRIMAGE;
    }

    public void setQRIMAGE(String QRIMAGE) {
        this.QRIMAGE = QRIMAGE;
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

    public String getBARCODE() {
        return BARCODE;
    }

    public void setBARCODE(String BARCODE) {
        this.BARCODE = BARCODE;
    }

    public String getFAMILYNAME() {
        return FAMILYNAME;
    }

    public void setFAMILYNAME(String FAMILYNAME) {
        this.FAMILYNAME = FAMILYNAME;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getCITY() {
        return CITY;
    }

    public void setCITY(String CITY) {
        this.CITY = CITY;
    }

    public String getACTIVE() {
        return ACTIVE;
    }

    public void setACTIVE(String ACTIVE) {
        this.ACTIVE = ACTIVE;
    }

    public String getPHOTO() {
        return PHOTO;
    }

    public void setPHOTO(String PHOTO) {
        this.PHOTO = PHOTO;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }
}

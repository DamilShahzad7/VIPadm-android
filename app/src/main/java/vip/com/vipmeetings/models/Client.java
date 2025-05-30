package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import butterknife.BindView;

/**
 * Created by Srinath on 18/03/18.
 */
@Root(name = "Client", strict = false)
public class Client {

    @Element(name = "ClientName",required=false)
    private String ClientName;
    @Element(name = "VATNumber",required=false)
    private String VATNumber;
    @Element(name = "Address1",required=false)
    private String Address1;
    @Element(name = "Address2",required=false)
    private String Address2;
    @Element(name = "Country",required=false)
    private String Country;
    @Element(name = "City",required=false)
    private String City;
    @Element(name = "ZIPCode",required=false)
    private String ZIPCode;
    @Element(name = "Mobile",required=false)
    private String Mobile;
    @Element(name = "Email",required=false)
    private String Email;

    //1253
    @Element(name = "IsPatientSystem",required=false)
    private boolean IsPatientSystem;

    public boolean isPatientSystem() {
        return IsPatientSystem;
    }

    public void setPatientSystem(boolean patientSystem) {
        IsPatientSystem = patientSystem;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getVATNumber() {
        return VATNumber;
    }

    public void setVATNumber(String VATNumber) {
        this.VATNumber = VATNumber;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getZIPCode() {
        return ZIPCode;
    }

    public void setZIPCode(String ZIPCode) {
        this.ZIPCode = ZIPCode;
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
}

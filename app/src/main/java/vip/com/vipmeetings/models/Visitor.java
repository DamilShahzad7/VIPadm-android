package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Srinath on 04/07/17.
 */

@Root(strict = false)
public class Visitor {


    @Element(name = "Name", required = false)
    private String Name;
    @Element(name = "EMail", required = false)
    private String EMail;
    @Element(name = "Type", required = false)
    private String Type;
    @Element(name = "Mobile", required = false)
    private String Mobile;
    @Element(name = "FirstName", required = false)
    private String FirstName;
    @Element(name = "FamilyName", required = false)
    private String FamilyName;
    @Element(name = "CompanyName", required = false)
    private String CompanyName;
    @Element(name = "City", required = false)
    private String City;

    public String getName ()
    {
        return Name;
    }

    public void setName (String Name)
    {
        this.Name = Name;
    }

    public String getEMail ()
    {
        return EMail;
    }

    public void setEMail (String EMail)
    {
        this.EMail = EMail;
    }

    public String getType ()
    {
        return Type;
    }

    public void setType (String Type)
    {
        this.Type = Type;
    }

    public String getMobile ()
    {
        return Mobile;
    }

    public void setMobile (String Mobile)
    {
        this.Mobile = Mobile;
    }

    public String getFirstName ()
    {
        return FirstName;
    }

    public void setFirstName (String FirstName)
    {
        this.FirstName = FirstName;
    }

    public String getFamilyName ()
    {
        return FamilyName;
    }

    public void setFamilyName (String FamilyName)
    {
        this.FamilyName = FamilyName;
    }

    public String getCompanyName ()
    {
        return CompanyName;
    }

    public void setCompanyName (String CompanyName)
    {
        this.CompanyName = CompanyName;
    }

    public String getCity ()
    {
        return City;
    }

    public void setCity (String City)
    {
        this.City = City;
    }


}

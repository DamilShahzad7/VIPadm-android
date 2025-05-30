package vip.com.vipmeetings.models;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="Country",strict = false)
public class Country
{

    @Element(name = "CountryName",required=false)
    private String CountryName;
    @Element(name = "CountryCode",required=false)
    private String CountryCode;



    public String getCountryName ()
    {
        return CountryName;
    }

    public void setCountryName (String CountryName)
    {
        this.CountryName = CountryName;
    }

    public String getCountryCode ()
    {
        return CountryCode;
    }

    public void setCountryCode (String CountryCode)
    {
        this.CountryCode = CountryCode;
    }

    @Override
    public String toString()
    {
        return CountryName;
    }
}
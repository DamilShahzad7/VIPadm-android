package vip.com.vipmeetings.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name="Countries",strict = false)
public class Countries {


    @ElementList(entry = "Country", inline = true)
    List<Country> Country;


    public List<Country> getCountry() {
        return Country;
    }

    public void setCountry(List<Country> country) {
        Country = country;
    }
}
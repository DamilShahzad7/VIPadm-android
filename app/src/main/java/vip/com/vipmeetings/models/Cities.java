package vip.com.vipmeetings.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Srinath on 30/05/17.
 */
@Root(name = "Cities",strict = false)
public class Cities {

    @ElementList(entry = "City", inline = true,required = false)
    List<City> City;

    public List<City> getCity() {
        return City;
    }

    public void setCity(List<City> city) {
        City = city;
    }
}

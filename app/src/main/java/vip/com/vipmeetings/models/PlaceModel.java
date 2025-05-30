package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Place", strict = false)
public class PlaceModel {


    @Element(name = "PlaceID",required=false)
    String PlaceID;
    @Element(name = "Name",required=false)
    String Name;
    @Element(name = "AreasCount",required=false)
    String AreasCount;



    public String getPlaceID() {
        return PlaceID;
    }

    public void setPlaceID(String placeID) {
        PlaceID = placeID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAreasCount() {
        return AreasCount;
    }

    public void setAreasCount(String areasCount) {
        AreasCount = areasCount;
    }
}

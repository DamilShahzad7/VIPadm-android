package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root(name = "GetPlaceID", strict = false)
public class GetPlaceID {

    @Element(name = "Status", required = false)
    private String status;

    @Element(name = "PlaceID", required = false)
    private String placeID;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }
}

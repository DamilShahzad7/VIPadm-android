package vip.com.vipmeetings.models;


import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "Places", strict = false)
public class PlacesModel {


    @ElementList(name = "Place", inline = true, required = false)
    List<PlaceModel> placeModelList;

    public List<PlaceModel> getPlaceModelList() {
        return placeModelList;
    }

    public void setPlaceModelList(List<PlaceModel> placeModelList) {
        this.placeModelList = placeModelList;
    }
}

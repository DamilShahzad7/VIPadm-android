package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Srinath on 04/02/18.
 */

@Root(name = "Evacuations", strict = false)
public class Evacuations {

    @ElementList(name = "Evacuation", inline = true, required = false)
    List<Evacuation> Evacuation;


    @Element(name = "Empty", required = false)
    Empty Empty;

    public vip.com.vipmeetings.models.Empty getEmpty() {
        return Empty;
    }

    public void setEmpty(vip.com.vipmeetings.models.Empty empty) {
        Empty = empty;
    }

    public List<vip.com.vipmeetings.models.Evacuation> getEvacuation() {
        return Evacuation;
    }

    public void setEvacuation(List<vip.com.vipmeetings.models.Evacuation> evacuation) {
        Evacuation = evacuation;
    }
}
package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Sekhar on 04/02/18.
 */

@Root(name = "Evacuations", strict = false)
public class EvacuationsInformation {

    public List<EvacuationInformation> getEvacuationInformation() {
        return evacuationInformation;
    }

    public void setEvacuationInformation(List<EvacuationInformation> evacuationInformation) {
        this.evacuationInformation = evacuationInformation;
    }

    @ElementList(name = "Evacuation", inline = true, required = false)
    List<EvacuationInformation> evacuationInformation;


    @Element(name = "Empty", required = false)
    Empty Empty;

    public vip.com.vipmeetings.models.Empty getEmpty() {
        return Empty;
    }

    public void setEmpty(vip.com.vipmeetings.models.Empty empty) {
        Empty = empty;
    }


}
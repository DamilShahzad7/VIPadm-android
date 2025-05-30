package vip.com.vipmeetings.models;


import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class GetEvacuationStatusModel {


    @ElementList(entry = "EvacuationStatus", inline = true,required = false)
    public List<EvacuationStatus> evacuationStatuses;

    public List<EvacuationStatus> getEvacuationStatuses() {
        return evacuationStatuses;
    }

    public void setEvacuationStatuses(List<EvacuationStatus> evacuationStatuses) {
        this.evacuationStatuses = evacuationStatuses;
    }
}

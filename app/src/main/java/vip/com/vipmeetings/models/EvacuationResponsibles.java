package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Srinath on 06/07/17.
 */
@Root(name = "EvacuationResponsibles",strict = false)
public class EvacuationResponsibles {

    @ElementList(name = "EvacuationResponsible",inline = true, required = false)
    List<EvacuationResponsible> EvacuationResponsible;

    @Element(name = "Empty", required = false)
    Empty Empty;

    public vip.com.vipmeetings.models.Empty getEmpty() {
        return Empty;
    }

    public void setEmpty(vip.com.vipmeetings.models.Empty empty) {
        Empty = empty;
    }

    public List<vip.com.vipmeetings.models.EvacuationResponsible> getEvacuationResponsible() {
        return EvacuationResponsible;
    }

    public void setEvacuationResponsible(List<vip.com.vipmeetings.models.EvacuationResponsible> evacuationResponsible) {
        EvacuationResponsible = evacuationResponsible;
    }
}

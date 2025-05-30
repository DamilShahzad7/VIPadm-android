package vip.com.vipmeetings.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Srinath on 04/07/17.
 */

@Root(strict = false)
public class Visitors {

    @ElementList(entry = "Visitor", inline = true, required = false)
    List<Visitor> Visitor;

    public List<Visitor> getVisitor() {
        return Visitor;
    }

    public void setVisitor(List<Visitor> visitor) {
        Visitor = visitor;
    }
}

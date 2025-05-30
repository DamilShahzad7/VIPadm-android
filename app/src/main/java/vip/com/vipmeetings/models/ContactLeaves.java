package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Srinath on 30/05/17.
 */

@Root(strict = false)
public class ContactLeaves {

    @Element(name = "Empty",required=false)
    private Empty empty;

    @Element(name="Error",required = false)
    private Error error;


    @ElementList(entry = "Leave", inline = true,required = false)
    List<Leave> leaves;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Empty getEmpty() {
        return empty;
    }

    public void setEmpty(Empty empty) {
        this.empty = empty;
    }

    public List<Leave> getLeaves() {
        return leaves;
    }

    public void setLeaves(List<Leave> leaves) {
        this.leaves = leaves;
    }
}

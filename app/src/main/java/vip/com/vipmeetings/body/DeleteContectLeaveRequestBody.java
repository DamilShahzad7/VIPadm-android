package vip.com.vipmeetings.body;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import vip.com.vipmeetings.request.AddContactLeave;
import vip.com.vipmeetings.request.DeleteContactLeave;

@Root(name = "soap12:Body", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class DeleteContectLeaveRequestBody {


    @Element(name = "DeleteContactLeave", required = false)
    private DeleteContactLeave deleteContactLeave;

    public DeleteContactLeave getDeleteContactLeave() {
        return deleteContactLeave;
    }

    public void setDeleteContactLeave(DeleteContactLeave deleteContactLeave) {
        this.deleteContactLeave = deleteContactLeave;
    }
}

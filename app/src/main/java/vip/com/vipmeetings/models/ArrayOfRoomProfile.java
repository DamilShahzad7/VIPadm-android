package vip.com.vipmeetings.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Srinath on 25/06/17.
 */

@Root(name = "ArrayOfRoomProfile",strict = false)
@NamespaceList({
        @Namespace( prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
        @Namespace( prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
        @Namespace( prefix = "xmlns", reference = "http://tempuri.org/")
})
public class ArrayOfRoomProfile {



    @ElementList(entry = "RoomProfile", inline = true,required = false)
    List<RoomProfile> RoomProfile;

    public List<RoomProfile> getRoomProfile() {
        return RoomProfile;
    }

    public void setRoomProfile(List<RoomProfile> roomProfile) {
        RoomProfile = roomProfile;
    }
}

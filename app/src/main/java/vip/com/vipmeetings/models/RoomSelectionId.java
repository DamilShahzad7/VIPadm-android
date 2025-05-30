package vip.com.vipmeetings.models;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * Created by Srinath on 26/06/17.
 */


@Root(strict = false)
public class RoomSelectionId {

    @Text(required =false)
    private String _roomselectionid;

    public String get_roomselectionid() {
        return _roomselectionid;
    }

    public void set_roomselectionid(String _roomselectionid) {
        this._roomselectionid = _roomselectionid;
    }
}

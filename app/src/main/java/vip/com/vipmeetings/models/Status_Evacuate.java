package vip.com.vipmeetings.models;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * Created by Srinath on 30/05/17.
 */

@Root(name = "string" ,strict = false)
public class Status_Evacuate {


    @Text(required = false)
    private String _string;

    public String get_string() {
        return _string;
    }

    public void set_string(String _string) {
        this._string = _string;
    }
}

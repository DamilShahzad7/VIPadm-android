package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * Created by Srinath on 30/05/17.
 */

@Root(strict = false)
//@Namespace(reference = "http://tempuri.org/")
public class Status {


    @Text(required = false)
    private String _status;


    public String get_status() {
        return _status;
    }

    public void set_status(String _status) {
        this._status = _status;
    }
}

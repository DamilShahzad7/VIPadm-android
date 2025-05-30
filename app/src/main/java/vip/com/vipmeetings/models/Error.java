package vip.com.vipmeetings.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * Created by Srinath on 24/06/17.
 */
@Root(name = "Error", strict = false)
@Namespace(reference = "http://tempuri.org/")
public class Error {



    @Text
    private String _error;

    public String get_error() {
        return _error;
    }

    public void set_error(String _error) {
        this._error = _error;
    }


}

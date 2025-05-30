package vip.com.vipmeetings.models;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * Created by Srinath on 06/06/17.
 */

@Root(strict = false,name = "Empty")
public class Empty {

    @Text(required = false)
    private String _empty;

    public String get_empty() {
        return _empty;
    }

    public void set_empty(String _empty) {
        this._empty = _empty;
    }
}

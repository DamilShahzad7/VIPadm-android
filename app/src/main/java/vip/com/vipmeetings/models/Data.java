package vip.com.vipmeetings.models;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(name = "OTP", strict = false)
public class Data {

    @Text
    private String _OTP;

    public String getOTP() {
        return _OTP;
    }

    public void setOTP(String _OTP) {
        this._OTP = _OTP;
    }


}

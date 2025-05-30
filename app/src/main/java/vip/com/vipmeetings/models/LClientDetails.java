package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "LClientDetails", strict = false)
public class LClientDetails {
//,

    @Element(name = "ClientDetails", required = false)
    private ClientDetails ClientDetails;

    @Element(required = false)
    private Status _status;


    public Status get_status() {
        return _status;
    }

    public void set_status(Status _status) {
        this._status = _status;
    }

    public ClientDetails getClientDetails() {
        return ClientDetails;
    }

    public void setClientDetails(ClientDetails ClientDetails) {
        this.ClientDetails = ClientDetails;
    }


    @Override
    public String toString() {
        return ClientDetails.toString();
    }
}
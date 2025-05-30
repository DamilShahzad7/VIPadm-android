package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Srinath on 30/05/17.
 */

@Root(strict = false)
public class Leave  {

    @Element(name = "FromDateTimeYMDHM",required=false)
    private String FromDateTimeYMDHM;
    @Element(name = "BookedDateTime",required=false)
    private String BookedDateTime;
    @Element(name = "ReasonForLeave",required=false)
    private String ReasonForLeave;
    @Element(name = "P_LeaveId",required=false)
    private String P_LeaveId;
    @Element(name = "ToDateTimeYMDHM",required=false)
    private String ToDateTimeYMDHM;

    public String getFromDateTimeYMDHM ()
    {
        return FromDateTimeYMDHM;
    }

    public void setFromDateTimeYMDHM (String FromDateTimeYMDHM)
    {
        this.FromDateTimeYMDHM = FromDateTimeYMDHM;
    }

    public String getBookedDateTime ()
    {
        return BookedDateTime;
    }

    public void setBookedDateTime (String BookedDateTime)
    {
        this.BookedDateTime = BookedDateTime;
    }

    public String getReasonForLeave ()
    {
        return ReasonForLeave;
    }

    public void setReasonForLeave (String ReasonForLeave)
    {
        this.ReasonForLeave = ReasonForLeave;
    }

    public String getP_LeaveId ()
    {
        return P_LeaveId;
    }

    public void setP_LeaveId (String P_LeaveId)
    {
        this.P_LeaveId = P_LeaveId;
    }

    public String getToDateTimeYMDHM ()
    {
        return ToDateTimeYMDHM;
    }

    public void setToDateTimeYMDHM (String ToDateTimeYMDHM)
    {
        this.ToDateTimeYMDHM = ToDateTimeYMDHM;
    }

}

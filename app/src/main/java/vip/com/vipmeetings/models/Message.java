package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import vip.com.vipmeetings.evacuate.IpAddress;

/**
 * Created by Srinath on 05/07/17.
 */

@Root(name = "Message", strict = false)
public class Message {


    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(IpAddress.DATEMESSAGE);

    @Element(name = "EvacuationId", required = false)
    private String EvacuationId;
    @Element(name = "AreaName", required = false)
    private String AreaName;

    @Element(name = "Company", required = false)
    private String Company;
    @Element(name = "Floor", required = false)
    private String Floor;
    @Element(name = "MessageId", required = false)
    private String MessageId;

    @Element(name = "Message", required = false)
    private String Message;

    @Element(name = "SendByApp", required = false)
    private String SendByApp;

    @Element(name = "ReceivedByApp", required = false)
    private String ReceivedByApp;

    @Element(name = "Location", required = false)
    private String Location;

    @Element(name = "ReplyToMessageId", required = false)
    private String ReplyToMessageId;

    @Element(name = "Date", required = false)
    private String Date;

    @Element(name = "IsRead", required = false)
    private boolean IsRead;


    @Element(name = "Name", required = false)
    private String Name;

    @Element(name = "SenderName", required = false)
    private String SenderName;


    @Element(name = "Mobile", required = false)
    private String Mobile;

    @Element(name = "Email", required = false)
    private String Email;

    @Element(name = "Barcode", required = false)
    private String Barcode;
    @Element(name = "Priority", required = false)
    private String Priority;

    private boolean isDate;

    @Override
    public String toString() {
        return super.toString();
    }

    public java.util.Date getDateFormat() {
        try {
            return simpleDateFormat.parse(getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public boolean isRead() {
        return IsRead;
    }

    public void setRead(boolean read) {
        IsRead = read;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String Barcode) {
        this.Barcode = Barcode;
    }

    public String getEvacuationId() {
        return EvacuationId;
    }

    public void setEvacuationId(String EvacuationId) {
        this.EvacuationId = EvacuationId;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public String getSendByApp() {
        return SendByApp;
    }

    public void setSendByApp(String SendByApp) {
        this.SendByApp = SendByApp;
    }

    public String getReplyToMessageId() {
        return ReplyToMessageId;
    }

    public void setReplyToMessageId(String ReplyToMessageId) {
        this.ReplyToMessageId = ReplyToMessageId;
    }

    public String getFloor() {
        return Floor;
    }

    public void setFloor(String Floor) {
        this.Floor = Floor;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String Priority) {
        this.Priority = Priority;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getMessageId() {
        return MessageId;
    }

    public void setMessageId(String MessageId) {
        this.MessageId = MessageId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getReceivedByApp() {
        return ReceivedByApp;
    }

    public void setReceivedByApp(String ReceivedByApp) {
        this.ReceivedByApp = ReceivedByApp;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public boolean getIsRead() {
        return IsRead;
    }

    public void setIsRead(boolean IsRead) {
        this.IsRead = IsRead;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String Company) {
        this.Company = Company;
    }

    public boolean isDate() {
        return isDate;
    }

    public void setDate(boolean date) {
        isDate = date;
    }


}

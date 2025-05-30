package vip.com.vipmeetings.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Srinath on 04/07/17.
 */

@Root(strict = false)
public class MeetingDetailsVisitor {


    @Element(name = "MeetingId", required = false)
    private String meetingId;

    @Element(name = "VisitorId", required = false)
    private String visitorId;

    @Element(name = "SlNo", required = false)
    private String slNo;

    @Element(name = "_FullName", required = false)
    private String fullName;

    @Element(name = "CompanyName", required = false)
    private String companyName;

    @Element(name = "Mobile", required = false)
    private String mobile;

    @Element(name = "EMail", required = false)
    private String email;

    @Element(name = "BarCode", required = false)
    private String barCode;

    @Element(name = "VIn", required = false)
    private String vIn;

    @Element(name = "VOut", required = false)
    private String vOut;

    @Element(name = "RentalId", required = false)
    private String rentalId;

    @Element(name = "ProductId", required = false)
    private String productId;

    @Element(name = "Category", required = false)
    private String category;

    @Element(name = "Invitation", required = false)
    private String invitation;

    @Element(name = "InTime", required = false)
    private String InTime;

    @Element(name = "OutTime", required = false)
    private String OutTime;

    @Element(name = "ConfirmedOn", required = false)
    private String confirmedOn;

    @Element(name = "SendSmsInvitation", required = false)
    private String sendSmsInvitation;

    @Element(name = "SentInvitationOn", required = false)
    private String sentInvitationOn;

    @Element(name = "SendReminder", required = false)
    private String sendReminder;

    @Element(name = "SentRemiderOn", required = false)
    private String sentReminderOn;

    @Element(name = "MeetTime", required = false)
    private String meetTime;

    @Element(name = "ToTime", required = false)
    private String toTime;

    @Element(name = "IsConfirmed", required = false)
    public boolean isConfirmed;

    @Element(name = "IsCancelled", required = false)
    public boolean isCancelled;

    @Element(name = "CancelledReason", required = false)
    private String cancelledReason;

    @Element(name = "CancelledOn", required = false)
    private String cancelledOn;

    @Element(name = "RescheduleComments", required = false)
    private String rescheduleComments;

    @Element(name = "AlternateMobile", required = false)
    private String alternateMobile;

    @Element(name = "RegisteredBy", required = false)
    private String registeredBy;

    @Element(name = "RepliedBy", required = false)
    private String repliedBy;

    @Element(name = "CardNumber", required = false)
    private String cardNumber;

    @Element(name = "PinNumber", required = false)
    private String pinNumber;

    @Element(name = "IsApproved", required = false)
    public boolean isApproved;

    @Element(name = "ApprovedOn", required = false)
    private String approvedOn;

    @Element(name = "AddedToSiPass", required = false)
    private boolean addedToSiPass;

    @Element(name = "SiPassError", required = false)
    private String siPassError;

    @Element(name = "JoinOption", required = false)
    private String joinOption;

    @Element(name = "JoinFrom", required = false)
    private String joinFrom;


    // Getters and setters for all fields
    public String getMeetingId() { return meetingId; }
    public void setMeetingId(String meetingId) { this.meetingId = meetingId; }

    public String getVisitorId() { return visitorId; }
    public void setVisitorId(String visitorId) { this.visitorId = visitorId; }

    public String getSlNo() { return slNo; }
    public void setSlNo(String slNo) { this.slNo = slNo; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getBarCode() { return barCode; }
    public void setBarCode(String barCode) { this.barCode = barCode; }

    public String getVIn() { return vIn; }
    public void setVIn(String vIn) { this.vIn = vIn; }

    public String getVOut() { return vOut; }
    public void setVOut(String vOut) { this.vOut = vOut; }

    public String getRentalId() { return rentalId; }
    public void setRentalId(String rentalId) { this.rentalId = rentalId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getInvitation() { return invitation; }
    public void setInvitation(String invitation) { this.invitation = invitation; }



    public String getSendSmsInvitation() { return sendSmsInvitation; }
    public void setSendSmsInvitation(String sendSmsInvitation) { this.sendSmsInvitation = sendSmsInvitation; }

    public String getSentInvitationOn() { return sentInvitationOn; }
    public void setSentInvitationOn(String sentInvitationOn) { this.sentInvitationOn = sentInvitationOn; }

    public String getSendReminder() { return sendReminder; }
    public void setSendReminder(String sendReminder) { this.sendReminder = sendReminder; }

    public String getSentReminderOn() { return sentReminderOn; }
    public void setSentReminderOn(String sentReminderOn) { this.sentReminderOn = sentReminderOn; }

    public String getMeetTime() { return meetTime; }
    public void setMeetTime(String meetTime) { this.meetTime = meetTime; }

    public String getToTime() { return toTime; }
    public void setToTime(String toTime) { this.toTime = toTime; }

    public boolean getIsConfirmed() { return isConfirmed; }
    public void setIsConfirmed(boolean isConfirmed) { this.isConfirmed = isConfirmed; }

    public String getConfirmedOn() { return confirmedOn; }
    public void setConfirmedOn(String confirmedOn) { this.confirmedOn = confirmedOn; }

    public boolean getIsCancelled() { return isCancelled; }
    public void setIsCancelled(boolean isCancelled) { this.isCancelled = isCancelled; }

    public String getCancelledReason() { return cancelledReason; }
    public void setCancelledReason(String cancelledReason) { this.cancelledReason = cancelledReason; }

    public String getCancelledOn() { return cancelledOn; }
    public void setCancelledOn(String cancelledOn) { this.cancelledOn = cancelledOn; }

    public String getRescheduleComments() { return rescheduleComments; }
    public void setRescheduleComments(String rescheduleComments) { this.rescheduleComments = rescheduleComments; }

    public String getAlternateMobile() { return alternateMobile; }
    public void setAlternateMobile(String alternateMobile) { this.alternateMobile = alternateMobile; }

    public String getRegisteredBy() { return registeredBy; }
    public void setRegisteredBy(String registeredBy) { this.registeredBy = registeredBy; }

    public String getRepliedBy() { return repliedBy; }
    public void setRepliedBy(String repliedBy) { this.repliedBy = repliedBy; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getPinNumber() { return pinNumber; }
    public void setPinNumber(String pinNumber) { this.pinNumber = pinNumber; }

    public boolean getIsApproved() { return isApproved; }
    public void setIsApproved(Boolean isApproved) { this.isApproved = isApproved; }

    public String getApprovedOn() { return approvedOn; }
    public void setApprovedOn(String approvedOn) { this.approvedOn = approvedOn; }

    public boolean getAddedToSiPass() { return addedToSiPass; }
    public void setAddedToSiPass(boolean addedToSiPass) { this.addedToSiPass = addedToSiPass; }

    public String getSiPassError() { return siPassError; }
    public void setSiPassError(String siPassError) { this.siPassError = siPassError; }

    public String getJoinOption() { return joinOption; }
    public void setJoinOption(String joinOption) { this.joinOption = joinOption; }

    public String getJoinFrom() { return joinFrom; }
    public void setJoinFrom(String joinFrom) { this.joinFrom = joinFrom; }

    public String getOutTime() {
        return OutTime;
    }

    public void setOutTime(String outTime) {
        OutTime = outTime;
    }

    public String getInTime() {
        return InTime;
    }

    public void setInTime(String inTime) {
        InTime = inTime;
    }

}

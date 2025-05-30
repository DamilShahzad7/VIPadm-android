package vip.com.vipmeetings.models;

import java.util.Objects;

/**
 * Created by Srinath on 15/06/17.
 */

public class ContactManual {

    private String firstName = "''";
    private String surName = "''";
    private String company = "''";
    private String mobileNo = "''";
    boolean isPrivate;
    private String emailId = "''";
    private String meetingId = "''";
    private String city = "''";
    private String country = "''";
    private String sendInvitation = "''";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;
    private boolean hasCountryCode;


    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    private boolean isSelect;
    public boolean isHasCountryCode() {
        return hasCountryCode;
    }

    public void setHasCountryCode(boolean hasCountryCode) {
        this.hasCountryCode = hasCountryCode;
    }

    @Override
    public String toString() {

        if (firstName != null && firstName.length() > 0 && surName != null && surName.length() > 0
                && company != null && company.length() > 0) {

            return "Name='" + firstName + " " + surName + "',Mobile='" + mobileNo + "',Email='" + emailId + "'," +
                    "MeetingId='" + meetingId + "',City='" + city + "',Country='" + country
                    + "',SendInvitation='" + sendInvitation + "',Company='" + company + "'|";
        } else {


            return "Name='" + firstName + "',Mobile='" + mobileNo + "',Email='" + emailId + "'," +
                    "MeetingId='" + meetingId + "',City='" + city + "',Country='" + country
                    + "',SendInvitation='" + sendInvitation + "',Company='" + company + "'|";
        }

//        return "Name='" + firstName + " " + surName + "',Mobile='" + mobileNo + "',Email='" + emailId + "'," +
//                "MeetingId='" + meetingId + "',City='" + city + "',Country='" + country
//                + "',SendInvitation='" + sendInvitation + "',Company='" + company + "'|";
    }


    @Override
    public int hashCode() {
        return mobileNo.hashCode();
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ContactManual other = (ContactManual) obj;
        return Objects.equals(this.mobileNo, other.mobileNo) &&
                Objects.equals(this.emailId,other.emailId) &&
                Objects.equals(this.firstName, other.firstName) &&
                Objects.equals(this.surName, other.surName);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSendInvitation() {
        return sendInvitation;
    }

    public void setSendInvitation(String sendInvitation) {
        this.sendInvitation = sendInvitation;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}

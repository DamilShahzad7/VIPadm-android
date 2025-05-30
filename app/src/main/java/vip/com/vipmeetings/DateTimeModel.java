package vip.com.vipmeetings;

/**
 * Created by Srinath on 29/05/17.
 */

public class DateTimeModel {


    public int startHour;
    public int endHour;
    public int startMinute;
    public int endMinute;

    public String formatDate;
    public String countryCode;
    public String cityCode;

    public String subject;
    public String dateString;
    public String dateToString;
    public String formatToDate;
    public  boolean isBook;
    private boolean isToday;
    private String roomURL;

    boolean isSpecific;


    private String RoomName;
    private String FloorNumber;
    private String Image1;
    private String RoomId;
    private String Capacity;
    private boolean showDate;
    private boolean showTime;
    private boolean showBook;
    private String contactString;
    String times;
    private boolean invite;
    private boolean time;

    private  String securityInstruction;

    private String joinOption;

    public String getFormatToDate() {
        return formatToDate;
    }

    public void setFormatToDate(String formatToDate) {
        this.formatToDate = formatToDate;
    }

    public String getDateToString() {
        return dateToString;
    }

    public void setDateToString(String dateToString) {
        this.dateToString = dateToString;
    }

    public boolean isSpecific() {
        return isSpecific;
    }

    public void setSpecific(boolean specific) {
        isSpecific = specific;
    }

    public String getRoomURL() {
        return roomURL;
    }

    public void setRoomURL(String roomURL) {
        this.roomURL = roomURL;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }

    public String getFloorNumber() {
        return FloorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        FloorNumber = floorNumber;
    }

    public String getImage1() {
        return Image1;
    }

    public void setImage1(String image1) {
        Image1 = image1;
    }

    public String getRoomId() {
        return RoomId;
    }

    public void setRoomId(String roomId) {
        RoomId = roomId;
    }

    public String getCapacity() {
        return Capacity;
    }

    public void setCapacity(String capacity) {
        Capacity = capacity;
    }

    public boolean isShowDate() {
        return showDate;
    }

    public void setShowDate(boolean showDate) {
        this.showDate = showDate;
    }

    public boolean isShowTime() {
        return showTime;
    }

    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }

    public boolean isShowBook() {
        return showBook;
    }

    public void setShowBook(boolean showBook) {
        this.showBook = showBook;
    }

    public boolean isBook() {
        return isBook;
    }

    public void setBook(boolean book) {
        isBook = book;
    }

    public String getFormatDate() {
        return formatDate;
    }

    public void setFormatDate(String formatDate) {
        this.formatDate = formatDate;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public void setContactString(String contactString) {
        this.contactString = contactString;
    }

    public String getContactString() {
        return contactString;
    }

    public void setTimes(String times) {

        this.times=times;
    }

    public String getTimes() {
        return times;
    }

    public boolean isInvite() {
        return invite;
    }

    public void setInvite(boolean invite) {
        this.invite = invite;
    }

    public void setTime(boolean time) {
        this.time = time;
    }

    public boolean getTime() {
        return time;
    }
    public String getSecurityInstruction() {
        return securityInstruction;
    }

    public void setSecurityInstruction(String securityInstruction) {
        this.securityInstruction = securityInstruction;
    }
    public String getJoinOption() {
        return joinOption;
    }

    public void setJoinOption(String joinOption) {
        this.joinOption = joinOption;
    }
}

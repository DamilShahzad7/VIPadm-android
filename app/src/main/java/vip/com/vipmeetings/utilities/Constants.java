package vip.com.vipmeetings.utilities;

import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.multidex.BuildConfig;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.threeten.bp.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.models.Contact;

/**
 * Created by Srinath on 29/05/17.
 */

public class Constants {
    public static final String MAP_URL = "https://maps.googleapis.com/maps/api";
    public static final String ACTIVITY_MAIN = "mainactivity";
    public static final String ISPATIENT = "ISPATIENT";
    public static final String EDITCONTACTS = "edit";
    public static String IPADDRESS = "https://cloud.vipadm.com";
    public static String IPADDRESS2 = IPADDRESS;

    public static final String POSTDIRECTION = "/maps/api/directions/json";
    public static final String POSTGEOCODE = "/maps/api/geocode/json";

    //development
//    public static final String POSTMOBILE = "/WsVIPadmdev/MobileMeetings.asmx";
//    public static final String POSTCLIENT = "/WsVIPadmdev/clientsettings.asmx";
//    public static final String POSTEVACUATION = "/WsVIPadmdev/Evacuation.asmx";
//    public static final String POSTRECEPTION = "/WsVIPadmdev/reception.asmx";

    //production
    public static final String POSTMOBILE = "/WsVIPadm/MobileMeetings.asmx";
    public static final String POSTCLIENT = "/WsVIPadm/clientsettings.asmx";
    public static final String POSTEVACUATION = "/WsVIPadm/Evacuation.asmx";
    public static final String POSTRECEPTION = "/WsVIPadm/reception.asmx";

    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME = "com.google.android.gms.location.sample.locationaddress";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
    public static final Uri PHONEURI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    public static final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 101;
    public static final String SHAREDPREFE = "sharedpref";
    public static final String SUBJECT = "subject";
    public static final String EMAIL = "email";
    public static final String OTPSUCCESS = "otp";
    public static final String EMPLOY1 = "employ1";
    public static final String DATETIMEMODEL = "datetimemodel";
    public static final String LEAVE = "leave";
    public static final String MEETING = "meeting";
    public static final String SKIPROOM = "skiproom";
    public static final String CITYID = "cityid";
    public static final String BACK = "back";
    public static final String AVAILABLE = "AVAILABLE";
    public static final int REQUESTADD = 1;
    public static final String CONTACTLIST = "contactlist";
    public static final int REQUESTEDIT = 2;
    public static final String ADDCONTACT = "addContact";
    public static final String MEETINGID = "meetingid";
    public static final int EDITCONTACT = 3;
    public static final String POS = "pos";
    public static final String STARTTIME = "starttime";
    public static final String DELETE = "delete";
    public static final int REQDELETE = 4;
    public static final String BOOKROOM = "bookroom";
    public static final String BOOKROOMSPECIFIC = "BOOKSPECIFIC";
    public static final String ROOMSELECTIONID = "roomselectionid";
    public static final String DATEFORMAT = "dateformat";
    public static final String DATESTRING = "datestring";
    public static final String NOCONTACT = "nocontact";
    public static final String LOAD = "loadcity";
    public static final String NEWCITY = "newcity";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String INVITE = "invite";
    public static final String NOLOCATION = "nolocation";
    public static final String SKIPROOMS = "skiprooms";

    public static final String EVACUATION = "evacuation";
    public static final String EVACUATION_AUTHENTICATE = "evacuation_authenticate";
    public static final String BARCODE = "Barcode";
    public static final String ISREACHED = "isreached";
    public static final int EVACUATIONREFRESH = 12;
    public static final String EVACUATIONMODEL = "evacuationmodel";
    public static String CLIENT_TCPIP = "TCPIP";
    public static String CLIENT_HTTPS = "HTTPS";
    public static List<Contact> allContactList = null;
    public static List<Contact> allContactList1 = null;
    public static List<Contact> visContactList1 = null;
    public static List<Contact> phoneContactList1 = null;
    public static List<Contact> companyContactList1 = null;
    public static final String ClientID = "ClientId";
    public static String status = "ZERO_RESULTS";
    public static String DMY = "dd.MM.yyyy";
    public static String COMPANY_NAME = "ALL";

    public static void e(String error, String s) {

        if (BuildConfig.DEBUG) {
            Log.e(error, s);
        }
    }

    public static String getSelectedDate(Date date) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE d MMM yyyy");

        return simpleDateFormat.format(date);
    }

    public static Date formatGMT2(Date date) {

        try {

            DateTimeZone fromTimeZone = DateTimeZone.forID(TimeZone.getDefault().getID());

            DateTimeFormatter inputFormatter
                    = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZone(fromTimeZone);
            DateTime dateTimeFrom = new DateTime(date, fromTimeZone);


            DateTimeZone toTimeZone = DateTimeZone.forID("Europe/Oslo");

            DateTime dateTimeTo = new DateTime(dateTimeFrom.toDate(), toTimeZone);


            DateTimeFormatter outputFormatter
                    = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZone(toTimeZone);
            return dateTimeTo.toLocalDateTime().toDate();
        } catch (Exception e) {
            IpAddress.e("date", e.toString());
            return date;
        }
    }

    public static String formatDate(Date date) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return simpleDateFormat.format(date);
    }

    public static String formatDate(LocalDate date) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return simpleDateFormat.format(date.toString());
    }

    public static Date formatDateFromRoom(String date) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");

        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Date formatDateFromLocalDate(String format) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return simpleDateFormat.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static Date formatDateFromOccupancy(String format) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        try {
            return simpleDateFormat.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String formatDateFromDMY(String format) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            return simpleDateFormat.parse(format).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Date formatDateFromBooking(String format) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            return simpleDateFormat.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String formatDateFromMessage(String format) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            return simpleDateFormat2.format(simpleDateFormat.parse(format));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return format;
    }

    public static String formatDateFromMessage2(String format) {

        // Friday, October 19, 2018
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("EEEE, MMMM dd, yyyy");

        try {
            return simpleDateFormat2.format(simpleDateFormat.parse(format));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return format;
    }

    public static String formatDateFromMessage2Time(String format) {

        // Friday, October 19, 2018
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm:ss");

        try {
            return simpleDateFormat2.format(simpleDateFormat.parse(format));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return format;
    }


    public static String formatDateToken(Date format) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(format);
    }

    public static void noData(int pos) {

        if (BuildConfig.DEBUG) {
            Log.e("error", "nodata" + pos);
        }
    }


}

package vip.com.vipmeetings.evacuate;

import android.util.Log;

import androidx.multidex.BuildConfig;

import com.google.android.gms.location.LocationRequest;


/**
 * Created by Srinath on 23/03/17.
 */

public class IpAddress {

    public static final String USERTYPE_ACCESS = "ACCESS";
    public static final String USERTYPE_ADMIN = "ADMIN";
    public static final String USERTYPE_NORMAL = "NORMAL";
    public static final String USERTYPE_COMPANY = "COMPANY";

    public static final String UUID = "blunoUUID";
    public static final String USERNAME ="BLEUSERNAME" ;
    public static final String GATEOPENER ="gateopener" ;
    public static String LOGIN = "login";
    public static String mainscreen="mainscreen";
    public static String USERID = "userid";
    public static String IP = "http://52.74.92.30";


    public static final String BUNDLEID = "com.EgilMorner.VIPadm";
    public static final String STATUS_STARTED = "Started";
    public static final String STATUS_Ended = "ENDED";
    public static final int REQUESTMESSAGE = 1;
    public static final String UPDATE = "update";
    public static final String SUCCESS = "SUCCESS";
    public static final String Success = "Success";
    public static final String OTP = "OTP";
    public static final String OTP_ENTER = "OTPENTER";
    public static final int PRIORITY = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
    public static final String EVACUATION = "isevacuation";
    public static final String EXTERNALIP = "isexternalip";
    public static final String USERTYPE = "usertype";
    public static final int PADDINGMAP = 75;
    public static final String EMPTYLATLONG = "0.0,0.0";
    public static final int LOADMAP = 1111;
    public static final String LOADINGMAP = "load";
    public static final String TVSTARTEND = "tvstartend";
    public static final String DATEMESSAGE = "yyyy-MM-dd hh:mm:ss";
    public static final String AREALIST = "arealist";
    public static final String EVACUATIONID = "evacuationid";
    public static final String EVACUATIONMESSAGE = "evacuationmessage";
    public static final String NORMALUSER = "N";
    public static final String ADMINUSER = "A";
    public static final String CLIENTID = "clientid";
    public static final String ENTERCLIENTID = "Enterclientid";
    public static final String ENTEREMAIL = "Enteremail";
    public static final String OK = "OK";
    public static final String EVACUATION_NO = "No Evacuation has been enabled";
    public static final String EVACUATION_START = "Start evacuation";
    public static final String EVACUATION_DECLAREAREA = "Declare area as cleared";
    public static final String EVACUATION_ENABLED = "Evacuation has been enabled";
    public static final String EVACUATION_STARTED = "Evacuation started";
    public static final String EVACUATION_ENDED = "Evacuation ended";
    public static final String USERAVAILABILITY_TRUE = "True";
    public static final String USERAVAILABILITY_FALSE = "False";
    public static final float ZOOMCAMERA = 13.0f;
    public static final String EVACUATION_TIMECLEAR = "Evacuation time cleared";
    public static final int MARKER_HEIGHT = 60;
    public static final int MARKER_WIDTH = 60;
    public static final String CURRENT = "CURRENT RESPONSIBLE";
    public static final String CURRENT_AREA = "CURRENT AREA RESPONSIBLE";
    public static final String CURRENT_ME = "ME";
    public static final String NEXT = "NEXT AREA RESPONSIBLE";
    public static final String NEXT_YOU = "Co-Responsibles For this area :";
    public static final String NEXT_YOU_CO = "CO-RESPONSIBLES FOR THIS AREA";
    public static final String NEXT_YOU_STATUS = "STATUS OF YOUR CO-RESPONSIBLES :";
    public static final String SKIPLOCATION = "skiplocation";
    public static final String SERVICE = "service";
    public static final String PLACEID = "Placeid";
    public static final String NODATA = "nodata";
    public static final String PLACEID_NEW = "placeidnew";
    public static final String ERROR_TOKEN = "TOKENERROR";
    public static final String INTERFACE = "interface";
    public static final String ERROR = "error_webservice";
    public static final String CITY = "storecity";
    public static final String HOMESCREEN = "homescreen";
    public static final String AREALISTNORMAL = "arealistnormal";
    public static final String CITY_SELECTED = "city_selected";
    public static final String SKIPCONTACT = "skipcontact";
    public static final int TIMEOUT = 7000;
    public static final String CLEARTIMEEMPTY = "Declare area emptied for this area!";
    public static final String CLEARTIMEEMPTY2 = "Declare this area emptied";
    public static final String CLEARTIMEEMPTY_NOT = "Revoke clearence";
    public static final String CLEARTIMEEMPTY_MESSAGE = "Are you sure!, You want to Revoke clearence for this area!";
    public static final String CLEARTIMEEMPTY_NOT_MESSAGE = "Are you sure!, You want to Declare area!";
    public static final String BARCODE = "barCode";
    public static final String MESSAGEID = "MessageID";
    public static final String ADMINID = "adminID";
    public static final String ERROR_FAILED = "Google api client Failed to connect";
    public static final String ERROR_SUSPENDED = "Google api client suspended";
    public static final String SERVICE_CONTACT = "vip.com.vipmeetings.ContactService";
    public static final String REFRESHTIMERBAR = "refreshtimer";
    public static final String DATE_AUTHTOKEN_MOBILE = "date_mobile";
    public static final String DATE_AUTHTOKEN_EVACUATION = "date_evacuation";
    public static final float WIDTH_MAPLINE = 6;
    public static final String BACK = "backHidden";
    public static final String CURRENT_AREA_ME = "ME";

    public static final String CHANNELID = "vipmeetings";
    public static final String SEARCHLOC = "Search for locations...";
    public static final String EMAIL = "email";
    public static final String OTP_EMPLOY = "otpEmploy";
    public static final String FAILED = "FAILED";
    public static final String ENTEROTP = "enterotp";
    public static final String CALLED = "called";
    public static final String ENDTIME = "24.00";
    public static final String ENDTIME2 ="23.59" ;
    public static final String LOCATIONLAT ="location_lat" ;
    public static final String LOCATIONLON ="location_lon" ;
    public static String YES = "YES";
    public static String NO = "No";
    public static final String EVACUATION_START_MESSAGE = "Are you sure you want to Start evacuation?";
    public static final String EVACUATION_END_MESSAGE = "Are you sure you want to End evacuation?";
    public static final String CANCEL = "CANCEL";
    public static final String EVACUATION_END = "End evacuation";

    public static final String EVACUATION_ENABLE = "Evacuation has been enabled";
    public static final String EVACUATION_DISABLE = "No evacuation has been enabled";
    public static final String EVACUATION_ENDED1 = "Evacuation has been ended";
    public static final String EVACUATION_STARTED1 = "Evacuation has been started";
    public static final int IUSERID = 1;
    public static final String IPASSCODE = "gT7Fu8K=";
    public static final String AUTHTOKEN = "authToken";
    public static final String AUTHTOKEN_MOBILE = "authTokenMobile";
    public static final String AUTHTOKEN_EVACUATION = "authTokenEvacuation";
    public static final String LOCATIONAVAILABLE = "loc exist";
    public static final String LOCATIONAVAILABLE_NO = "loc not exist";


    public static final String EVACUATION_CLEAR1 = "Evacuation time has been cleared";
    public static final String EVACUATION_CLEAR2 = "Evacuation has been ended";


    public static String VIPadm = "VIPADM";
    public static String VIPRECEPTION = "VIPRECEPTION";
    public static String coresponse = "STATUS OF YOUR CO-RESPONSIBLES :";
    public static final int JOBID_PUSHSERVICE = 1250;
    public static final int JOBID_BROADCAST = 1252;
    public static final int JOBID_DEVICETOKEN = 1251;
    public static final int JOBID_CONTACTSERVICE = 1253;

    public static String WorkFromStatus = "CheckInDefault";

    public static void e(String data, String string) {


        if (BuildConfig.DEBUG) {
            Log.e(data + "", string + " nodata");
        }
    }

    public static void e(String msg) {


        if (BuildConfig.DEBUG) {
            Log.e( "bluno" , msg);
        }
    }

    public static void d(String timer, String s) {

        if (BuildConfig.DEBUG) {
            Log.d(timer + "", s + " nodata");
        }
    }

    public static boolean DEBUG() {

        return BuildConfig.DEBUG;
    }
}

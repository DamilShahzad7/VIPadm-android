package vip.com.vipmeetings;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.percentlayout.widget.PercentLayoutHelper;
import androidx.percentlayout.widget.PercentRelativeLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vip.com.vipmeetings.bluno.BlunoActivity;
import vip.com.vipmeetings.body.AppointmentRequestBody;
import vip.com.vipmeetings.body.AppointmentsResponseBody;
import vip.com.vipmeetings.body.GetEvacuationStatusRequestBody;
import vip.com.vipmeetings.body.GetResponsibleDetailsRequestBody;
import vip.com.vipmeetings.body.MeetingsRequestBody;
import vip.com.vipmeetings.body.MeetingsResponseBody;
import vip.com.vipmeetings.body.ValidateUserRequestBody;
import vip.com.vipmeetings.body.ValidateUserResponseBody;
import vip.com.vipmeetings.envelope.SoapAppointmentRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapAppointmentResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapEvacuationResponsibleDetailsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapEvacuationResponsibleDetailsResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetEvacuationStatusEnvelope;
import vip.com.vipmeetings.envelope.SoapGetEvacuationStatusResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapMeetingsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapMeetingsResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapValidateUserRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapValidateUserResponseEnvelope;
import vip.com.vipmeetings.evacuate.EvacuationListActivity;
import vip.com.vipmeetings.evacuate.EvacuationNormal2;
import vip.com.vipmeetings.evacuate.EvacuationNormalList;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.fcm.SendDeviceTokenService;
import vip.com.vipmeetings.interfaces.IGoogleApiClient;
import vip.com.vipmeetings.interfaces.MainApi;
import vip.com.vipmeetings.interfaces.MapDirection;
import vip.com.vipmeetings.models.City;
import vip.com.vipmeetings.models.Employee;
import vip.com.vipmeetings.models.EvacuationResponsible;
import vip.com.vipmeetings.models.EvacuationResponsibles;
import vip.com.vipmeetings.models.EvacuationStatus;
import vip.com.vipmeetings.models.Meeting;
import vip.com.vipmeetings.models.Meetings;
import vip.com.vipmeetings.qrcode.QRCode_generate;
import vip.com.vipmeetings.request.GetAppointmentsRequest;
import vip.com.vipmeetings.request.GetEvacuationStatus;
import vip.com.vipmeetings.request.GetMeetingsRequest;
import vip.com.vipmeetings.request.GetResponsibleDetails;
import vip.com.vipmeetings.request.ValidateUserRequest;
import vip.com.vipmeetings.response.GetEvacuationStatusResult;
import vip.com.vipmeetings.utilities.Constants;

import static vip.com.vipmeetings.utilities.Constants.allContactList;

/**
 * Created by Srinath on 29/05/17.
 */

public class HomeActivity extends BaseLocationActivity implements OnMapReadyCallback, IGoogleApiClient {


    private static final int REQUEST_REFRESH = 1001;

    @BindView(R.id.rlbook)
    ImageView rlbook;

    @BindView(R.id.ivdirection)
    ImageView ivdirection;


    @BindView(R.id.rlinvite)
    ImageView rlinvite;

    @BindView(R.id.rlaccess)
    ImageView rlaccess;

    @BindView(R.id.rlstatus)
    ImageView rlstatus;


    @BindView(R.id.rlevacuate)
    ConstraintLayout rlevacuate;

    @BindView(R.id.rllocation)
    RelativeLayout rllocation;

    @BindView(R.id.rlgate)
    RelativeLayout rlgate;

    @BindView(R.id.rlgate1)
    RelativeLayout rlgate1;


    @BindView(R.id.tvaddress)
    TextView tvaddress;

    City city_selected;

    @BindView(R.id.rvmeetings)
    RecyclerView rvmeetings;

    List<Meeting> meetingList;
    MeetingsAdapter meetingsAdapter;

    SupportMapFragment map_fragment;

    private LatLng pos_myloc, pos_desti;
    private Marker marker_desti;
    GoogleMap googleMap;
    PolylineOptions polylineOptions;
    Bitmap smallMarker;
    String message;

    @BindView(R.id.ivevacuationstatus)
    ImageView ivevacuationstatus;

    @BindView(R.id.root)
    PercentRelativeLayout root;
    @BindView(R.id.lltwo)
    LinearLayout lltwo;
    Bitmap bitmap;
    boolean getDirection = false;
    private List<EvacuationResponsible> evacuationResponsibleList;
    EvacuationResponsible evacuationResponsible;
    Double l1, l2, l3, l4;
    boolean isDirectionFound;
    private String zero_resultsstatus = "";

    String evacuationStatusNill = null;

    @BindView(R.id.tvwelcome)
    AppCompatTextView tvwelcome;
    @BindView(R.id.tvstatus)
    TextView tvstatus;
    @BindView(R.id.tvtimer)
    TextView tvtimer;

    @BindView(R.id.llevacuation)
    LinearLayout llevacuation;

    boolean isShow5OR6 = false;

    boolean isRunning = true;

    SimpleDateFormat simpleDateFormat;
    Long different;


    boolean flagNotify = false;
    private boolean isAdmin = false;
    boolean ismapLoaded;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    NumberFormat numberFormat;
    private boolean isLocationFound = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home1);
        numberFormat = new DecimalFormat("00");
        ButterKnife.bind(this);
        setClientCallbacks(this);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        meetingList = new ArrayList<>();
        meetingsAdapter = new MeetingsAdapter();
        rvmeetings.setAdapter(meetingsAdapter);
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.marker_myloc2);
        bitmap = bitmapdraw.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(bitmap, width, height, false);
        map_fragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        PushDownAnim.setPushDownAnimTo(rllocation);
        PushDownAnim.setPushDownAnimTo(rlaccess);
        PushDownAnim.setPushDownAnimTo(rlbook);
        PushDownAnim.setPushDownAnimTo(rlevacuate);
        PushDownAnim.setPushDownAnimTo(rlinvite);
        PushDownAnim.setPushDownAnimTo(rlstatus);
        show5();
        if (BuildConfig.DEBUG) {

            tvwelcome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mSharedPreferences.edit().clear().apply();
                    Intent intent = new Intent(HomeActivity.this, SplashScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();


                }
            });
        }

        setCrashlytics();
    }


    @Override
    protected void onStart() {
        super.onStart();
        regis(this);
    }

    @Override
    protected void onStop() {
        unregis(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEva(String update) {
        hidePD();
        deleteStiky(this);
        if (update != null && update.equalsIgnoreCase(IpAddress.UPDATE)) {
            refreshScreen();
        } else if (IpAddress.AUTHTOKEN.equalsIgnoreCase(update)) {
            refreshScreen();
        }
    }

    public void refreshScreen() {
        disableEvacuationTimer();
        checkInternalORExternal();
    }


    private void checkStatus() {

        if (mSharedPreferences.
                getString(IpAddress.PLACEID_NEW, null) != null
                && mSharedPreferences.
                getString(IpAddress.PLACEID_NEW, null).trim().length() > 0) {
            try {

                setRetrofit(getTcpIpEvacuationFalse());
                setService();
                IpAddress.e("PLACE ID", mSharedPreferences
                        .getString(IpAddress.PLACEID_NEW, null) + " empty");
                SoapGetEvacuationStatusEnvelope soapGetEvacuationStatusEnvelope =
                        new SoapGetEvacuationStatusEnvelope();

                GetEvacuationStatusRequestBody
                        getEvacuationStatusRequestBody =
                        new GetEvacuationStatusRequestBody();


                GetEvacuationStatus getEvacuationStatus = new GetEvacuationStatus();


                getEvacuationStatus.setA_sClientId(getClientID());
                getEvacuationStatus.setAuthToken(getAuthTokenPref_Evacuation());


                getEvacuationStatus.setA_sPlaceId(mSharedPreferences.
                        getString(IpAddress.PLACEID_NEW, null));


                getEvacuationStatusRequestBody.setGetEvacuationStatus(getEvacuationStatus);
                soapGetEvacuationStatusEnvelope.setBody(getEvacuationStatusRequestBody);
                Observable<SoapGetEvacuationStatusResponseEnvelope> lClientDetailsObservable =
                        service.getEvacuationStatus(soapGetEvacuationStatusEnvelope);

                lClientDetailsObservable.
                        subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onSuccessEvacuationStatus, this::onErrorEvacuationStatus);
            } catch (Exception e) {

                hidePD();
                IpAddress.e("STATUS", e.toString());
            }
        }
    }


    private void onErrorEvacuationStatus(Throwable throwable) {


        hidePD();
        showToast(throwable.toString());
    }

    private void onSuccessEvacuationStatus(SoapGetEvacuationStatusResponseEnvelope
                                                   soapGetEvacuationStatusResponseEnvelope) {


        try {
            GetEvacuationStatusResult
                    getEvacuationStatusResult = soapGetEvacuationStatusResponseEnvelope.getBody()
                    .getGetEvacuationStatusResponse()
                    .getGetEvacuationStatusResult();
            if (getEvacuationStatusResult != null && getEvacuationStatusResult.getEmpty() == null) {

                EvacuationStatus evacuationStatus1 = null;

                List<EvacuationStatus> evacuation = getEvacuationStatusResult
                        .getEvacuation().getEvacuationStatuses();

                for (EvacuationStatus evacuationStatus2 : evacuation) {

                    if (evacuationStatus2.getStatus() != null
                            && evacuationStatus2.getStatus().trim().length() > 0) {

                        evacuationStatus1 = evacuationStatus2;
                    }

                }

                if (evacuationStatus1 != null
                        && evacuationStatus1.getStatus() != null &&
                        evacuationStatus1.getStatus().trim().length() > 0) {

                    if (evacuationStatus1.getStatus().equalsIgnoreCase(IpAddress.STATUS_STARTED)) {

                        if (evacuationStatus1.getStartTime() != null
                                && evacuationStatus1.getStartTime().trim().length() > 0) {


                            colorStatusBar(true, getWindow(), this);
                            llevacuation.setVisibility(View.VISIBLE);
                            ivevacuationstatus.setVisibility(View.VISIBLE);
                            tvtimer.setVisibility(View.VISIBLE);
                            showTimer(evacuationStatus1.getStartTime());
                        }

                    } else if (evacuationStatus1.getStatus().equalsIgnoreCase(IpAddress.STATUS_Ended)) {

                        disableEvacuationTimer();

                    } else {

                        disableEvacuationTimer();
                    }


                } else {

                    disableEvacuationTimer();
                }
            } else {

                disableEvacuationTimer();
            }
        } catch (Exception e) {

            disableEvacuationTimer();


        }
    }

    private void disableEvacuationTimer() {

        if (timer != null) {
            timer.cancel();
            timer = null;

        }
        llevacuation.setVisibility(View.INVISIBLE);
        ivevacuationstatus.setVisibility(View.GONE);
        colorStatusBar(false, getWindow(), this, true);
    }

    private void showTimer(String startTime) {

        try {
            Date date1 = simpleDateFormat.parse(startTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date1);
            Date date2 = Constants.formatGMT2(new Date());
            long diffMs = date2.getTime() - date1.getTime();
            different = diffMs;
            runTimer(diffMs);

        } catch (Exception e) {
            tvtimer.setText("");
        }

    }

    Timer timer;

    private void runTimer(final long diffMs) {


        try {

            if (timer != null) {

                timer.cancel();
                timer = null;
            }
            timer = new Timer(Long.MAX_VALUE, 1000);
            timer.start();
        } catch (Exception e) {

            IpAddress.e("err", e.toString());
        }


    }

    long secondsInMilli = 1000;
    long minutesInMilli = secondsInMilli * 60;
    long hoursInMilli = minutesInMilli * 60;
    long daysInMilli = hoursInMilli * 24;

    public String CalculateTime(Long aLong) {


        long diff = aLong;

        String time = "";

        long elapsedDays = diff / daysInMilli;
        diff = diff % daysInMilli;

        long elapsedHours = diff / hoursInMilli;
        diff = diff % hoursInMilli;

        long elapsedMinutes = diff / minutesInMilli;
        diff = diff % minutesInMilli;

        long elapsedSeconds = diff / secondsInMilli;

        if (elapsedDays > 0) {
            time = numberFormat.format(elapsedDays) + ":" +
                    numberFormat.format(elapsedHours) + ":" +
                    numberFormat.format(elapsedMinutes) + ":" +
                    numberFormat.format(elapsedSeconds);
        } else if (elapsedHours > 0) {
            time = numberFormat.format(elapsedHours) + ":" +
                    numberFormat.format(elapsedMinutes) + ":" +
                    numberFormat.format(elapsedSeconds);
        } else if (elapsedMinutes > 0) {
            time = "00:" + numberFormat.format(elapsedMinutes) + ":" +
                    numberFormat.format(elapsedSeconds);

        } else if (elapsedSeconds > 0) {
            time = "00:00:" + numberFormat.format(elapsedSeconds) + "";
        }

        // IpAddress.e("TIME", time);

        return time;
    }

    public class Timer extends CountDownTimer {


//        long secondsInMilli = 1000;
//        long minutesInMilli = secondsInMilli * 60;
//        long hoursInMilli = minutesInMilli * 60;
//        long daysInMilli = hoursInMilli * 24;

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public Timer(long millisInFuture, long countDownInterval) {

            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {


            different += 1000L;
            tvtimer.setText(CalculateTime(different));


        }

        @Override
        public void onFinish() {

        }
    }


    boolean isChanged = false;

    private void checkInternalORExternal() {


        if (getApp().isReached()) {
            enable();
        } else {
            disable();
        }
        show6OR5();
        if (city_selected != null
                && getStoreCitySelect() != null
                && getStoreCitySelect().getCityName().equalsIgnoreCase(city_selected.getCityName())) {
            isChanged = true;
        } else {
            isLocationFound = true;
            marker_myloc = null;
            marker_desti = null;
            if (polyline != null)
                polyline.remove();
            isChanged = false;
            city_selected = getStoreCitySelect();
        }
        getDirection = false;
        flagNotify = false;
        isRunning = true;
        loc_show = true;
        isTrackingEnable = true;
        message = null;
        isDirectionFound = false;
        evacuationStatusNill = null;
        isReached = false;
        isAdmin = false;
        showPD();

        if (getApp().isStaticReached()) {

            isReached = getApp().isReached();
            updateUi(getApp().isReached());

        } else {

            Single.fromCallable(this::loadInBackground)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::updateUi, this::onError);
        }
    }


    @OnClick(R.id.llevacuation)
    public void onToolbar(View v) {

        llevacuation.setTag(true);
        getEvacuationDetails(getClientID(), getBarcode());

    }

    public void onError(Throwable throwable) {


        hidePD();
        isRunning = true;
        setLocation(getStoreCitySelect());
        loadMap();
        IpAddress.e("error", throwable.toString());

    }


    public void updateUi(boolean isReached) {


        this.isReached = isReached;
        if (this.isReached) {
            enable();
        } else {
            disable();
            ivdirection.setVisibility(View.GONE);
            rvmeetings.setVisibility(View.GONE);
        }
        isReachedorExternalIP();


    }

    private boolean isMyServiceRunning() {

        try {
            ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (IpAddress.SERVICE_CONTACT.equals(service.service.getClassName())) {
                    IpAddress.e("SERVICECONTACT", "TRUE");
                    return true;
                }
            }
        } catch (Exception e) {

            IpAddress.e("SERVICECONTACT", "TRUE" + e.toString());
        }
        IpAddress.e("SERVICECONTACT", "FALSE");
        return false;
    }


    //for both conditions
    public void isReachedorExternalIP() {
        if (googleMap != null && ismapLoaded) {
            onMapReady();
        } else {
            map_fragment.getMapAsync(this);
        }
    }

    //1253
    @Override
    public void onMapReady(GoogleMap googleMap) {

        ismapLoaded = true;
        if (this.googleMap != null)
            this.googleMap.clear();
        this.googleMap = googleMap;
        onMapReady();


    }

    private void onMapReady() {

        if (isReached) {
            if (isUserAccess) {
                onValidateUser();
            }
        } else {
            show6OR5();
            evacuationApis();
        }


    }

    private void evacuationApis() {
        setLocation(city_selected);
        getEvacuationDetails(getClientID(), getBarcode());
        checkStatus();
    }

    private void show6OR5() {

        if (isShow5OR6) {
            show6();
        } else {
            show5();
        }
        setGateOpener();
    }

    private void onValidateUser() {

        showPD();
        setOKHTTPAfterLogin();
        setRetrofit(getApp().getTcpIp());
        mainApi = retrofit.create(MainApi.class);
        SoapValidateUserRequestEnvelope soapValidateUserRequestEnvelope = new SoapValidateUserRequestEnvelope();
        ValidateUserRequestBody validateUserRequestBody = new ValidateUserRequestBody();
        ValidateUserRequest validateUserRequest = new ValidateUserRequest();
        validateUserRequest.setAuthToken(getAuthTokenPref_Mobile());
        validateUserRequest.setA_sEmail(getEmail());
        validateUserRequestBody.setValidateUser(validateUserRequest);
        soapValidateUserRequestEnvelope.setBody(validateUserRequestBody);

        Observable<SoapValidateUserResponseEnvelope> lClientDetailsObservable = mainApi.validateUser(soapValidateUserRequestEnvelope);
        lClientDetailsObservable.
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onValidate, this::onError);

    }


    boolean isUserAccess = true;

    private void onValidate(SoapValidateUserResponseEnvelope soapValidateUserResponseEnvelope) {

        hidePD();

        ValidateUserResponseBody validateUserResponseBody = soapValidateUserResponseEnvelope.getBody();

        try {
            if (validateUserResponseBody.getValidateUserResponse()
                    .getValidateUserResult().getError() != null) {


                isRunningToken = true;
                getAuthToken(false);
                return;
            }
        } catch (Exception e) {

            hidePD();
        }


        if (validateUserResponseBody != null
                && validateUserResponseBody.getValidateUserResponse() != null
                && validateUserResponseBody.getValidateUserResponse().getValidateUserResult() != null) {
            employModel = validateUserResponseBody
                    .getValidateUserResponse().getValidateUserResult().getEmployee();

            if (employModel != null) {


                if (employModel.getSTATUS() != null) {
                    if (employModel.getSTATUS().trim().equalsIgnoreCase("SUCCESS")) {

                        message = null;
                        storeEmp(mGson.toJson(employModel));


                        //  setGateOpener();

                        //ADMIN, COMPANY ADMIN AND NORMAL

                        if (employModel.getUSERTYPE() != null && employModel.getUSERTYPE().equalsIgnoreCase(IpAddress.USERTYPE_ACCESS)) {

                            isUserAccess = false;
                            Intent i = new Intent(this, QRCode_generate.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtra(IpAddress.GATEOPENER, true);
                            startActivity(i);
                            finish();
                            return;
                        } else {

                            evacuationApis();
                        }


                    } else if (employModel.getSTATUS().trim().equalsIgnoreCase("FAILED")) {

                        message = "Failed to get user details";

                    } else if (employModel.getSTATUS().trim().equalsIgnoreCase("INVALID")) {

                        message = "Invalid email ID";
                    } else if (employModel.getSTATUS().trim().equalsIgnoreCase("INACTIVE")) {

                        message = "Your mail is inactive \n Please contact admin.";
                    } else if (employModel.getSTATUS().trim().equalsIgnoreCase("UNREGD")) {

                        message = "Unregistered user \n please contact admin.";
                    }


                    if (message != null && message.trim().length() > 0) {

                        if (alertDialog != null && alertDialog.isShowing())
                            alertDialog.dismiss();

                        if (builder == null) {
                            builder = new AlertDialog.Builder(HomeActivity.this);
                        }

                        builder.setMessage(message);
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                refreshScreen();

                            }
                        });

                        alertDialog = builder.show();

                    }


                } else {
                    message = "Failed to get user details";
                    if (alertDialog != null && alertDialog.isShowing())
                        alertDialog.dismiss();

                    if (builder == null) {
                        builder = new AlertDialog.Builder(HomeActivity.this);
                    }
                    builder.setMessage(message);
                    builder.setCancelable(false);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            refreshScreen();

                        }
                    });
                    alertDialog = builder.show();

                }


            } else if (validateUserResponseBody.getValidateUserResponse()
                    .getValidateUserResult().getError() != null) {
                showMessage(validateUserResponseBody.getValidateUserResponse()
                        .getValidateUserResult().getError()
                        .get_error());

            }
        }
    }


    private void setGateOpener() {

        if (employModel.getUSERTYPE() != null && employModel.getUSERTYPE().equalsIgnoreCase(IpAddress.USERTYPE_ACCESS)) {
            rlgate.setVisibility(View.GONE);
        } else if (employModel.getUSERTYPE() != null &&
                (employModel.getUSERTYPE().equalsIgnoreCase(IpAddress.USERTYPE_ADMIN)
                        || employModel.getUSERTYPE().equalsIgnoreCase(IpAddress.USERTYPE_NORMAL) ||
                        employModel.getUSERTYPE().equalsIgnoreCase(IpAddress.USERTYPE_COMPANY))) {
            if (employModel.isOPENDOOR()) {

                if (isShow5OR6) {
                    rlgate.setVisibility(View.VISIBLE);
                    rlgate1.setVisibility(View.GONE);
                } else {

                    PercentLayoutHelper.PercentLayoutParams params =
                            (PercentRelativeLayout.LayoutParams) lltwo.getLayoutParams();
                    PercentLayoutHelper.PercentLayoutInfo info = params.getPercentLayoutInfo();
                    info.widthPercent = 1.0f;
                    lltwo.setLayoutParams((ViewGroup.LayoutParams) params);

                    rlgate.setVisibility(View.GONE);
                    rlgate1.setVisibility(View.VISIBLE);
                }
            } else
                rlgate.setVisibility(View.GONE);
        } else {
            rlgate.setVisibility(View.GONE);
        }
    }


    public void disable() {
        rlevacuate.setAlpha(1f);
        rlevacuate.setClickable(true);
        rllocation.setAlpha(1.0f);
        rllocation.setClickable(true);
        rlbook.setAlpha(0.4f);
        rlbook.setEnabled(false);
        rlaccess.setAlpha(0.4f);
        rlaccess.setEnabled(false);
        rlinvite.setAlpha(0.4f);
        rlinvite.setEnabled(false);
        rlstatus.setAlpha(0.4f);
        rlstatus.setEnabled(false);
    }


    public void enable() {
        rlevacuate.setAlpha(0.70f);
        rlevacuate.setEnabled(false);
        rllocation.setAlpha(1.0f);
        rllocation.setEnabled(true);

        rlbook.setAlpha(1f);
        rlbook.setEnabled(true);
        rlaccess.setAlpha(1f);
        rlaccess.setEnabled(true);
        rlinvite.setAlpha(1f);
        rlinvite.setEnabled(true);
        rlstatus.setAlpha(1f);
        rlstatus.setEnabled(true);

    }


    private void getEvacuationDetails(String clientid, String barcode) {


        if (clientid != null && barcode != null &&
                clientid.trim().length() > 0 && barcode.trim().length() > 0) {
            showPD();
            SoapEvacuationResponsibleDetailsRequestEnvelope soapEvacuationResponsibleDetailsRequestEnvelope =
                    new SoapEvacuationResponsibleDetailsRequestEnvelope();
            GetResponsibleDetailsRequestBody getResponsibleDetailsRequestBody =
                    new GetResponsibleDetailsRequestBody();
            GetResponsibleDetails getResponsibleDetails = new GetResponsibleDetails();
            getResponsibleDetails.setA_sBarcode(barcode);
            getResponsibleDetails.setA_sClientId(clientid);
            getResponsibleDetails.setAuthToken(getAuthTokenPref_Evacuation());
            getResponsibleDetailsRequestBody.setGetResponsibleDetails(getResponsibleDetails);
            soapEvacuationResponsibleDetailsRequestEnvelope.setBody(getResponsibleDetailsRequestBody);
            setRetrofit(getTcpIpEvacuationFalse());
            setService();
            Observable<SoapEvacuationResponsibleDetailsResponseEnvelope> lClientDetailsObservable =
                    service.getEvacuationResponsibleDetails(soapEvacuationResponsibleDetailsRequestEnvelope);
            lClientDetailsObservable.
                    subscribeOn(Schedulers.io())
                    .map(this::onMapEvacuation)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onEvacuation, this::onEvacuationError);
        }
    }

    private List<EvacuationResponsible> onMapEvacuation(SoapEvacuationResponsibleDetailsResponseEnvelope
                                                                soapEvacuationResponsibleDetailsResponseEnvelope) {


        boolean isShow6 = false;

        EvacuationResponsibles evacuationResponsibles =
                soapEvacuationResponsibleDetailsResponseEnvelope.getBody().getGetResponsibleDetailsResponse()
                        .getGetResponsibleDetailsResult().getEvacuationResponsibles();

        if (evacuationResponsibles != null) {

            evacuationResponsibleList = evacuationResponsibles.
                    getEvacuationResponsible();
            if (evacuationResponsibleList != null && evacuationResponsibleList.size() > 0) {

                for (int i = 0; i < evacuationResponsibleList.size(); i++) {

                    if (evacuationResponsibleList.get(i).getBarcode().equalsIgnoreCase(getBarcode())) {


                        evacuationResponsible = evacuationResponsibleList.get(i);
                        break;
                    }
                }


                if (evacuationResponsibleList.get(i).getA_sPlaceId() != null
                        && evacuationResponsibleList.get(i).getA_sPlaceId().trim().length() > 0
                        && mSharedPreferences.getString(IpAddress.PLACEID_NEW, null) != null) {
                    if (evacuationResponsibleList
                            .get(i).getA_sPlaceId()
                            .equalsIgnoreCase(mSharedPreferences
                                    .getString(IpAddress.PLACEID_NEW, null))) {

                        isShow5OR6 = true;
                        isShow6 = true;


                    } else {

                        //noplace
                        isShow6 = false;
                        isShow5OR6 = false;
                    }
                } else {
                    isShow6 = false;
                    isShow5OR6 = false;
                }

                if (isShow6) {
                    isShow5OR6 = true;
                } else {
                    isShow5OR6 = false;
                }

                //  setGateOpener();

                if (evacuationResponsible != null) {

                    if (evacuationResponsible.getAdminID() != null
                            && evacuationResponsible.getAdminID().trim().length() > 0) {

                        if (evacuationResponsible.getAreaName() != null
                                && evacuationResponsible.getAreaName().trim().length() > 0) {
                            isAdmin = true;

                        } else {

                            isAdmin = true;
                        }


                    } else if ((evacuationResponsible.getAdminID() == null
                            || evacuationResponsible.getAdminID().trim().length() == 0)
                            && evacuationResponsible.getAreaName() != null
                            && evacuationResponsible.getAreaName().trim().length() > 0) {

                        //yesno

                        if (evacuationResponsible.getSTATUS() != null
                                && evacuationResponsible.getSTATUS().trim().length() > 0) {
                            isAdmin = false;
                            evacuationStatusNill = "";

                        } else {

                            isAdmin = false;
                            evacuationStatusNill = null;
                        }


                    } else if (evacuationResponsible.getSTATUS() != null
                            && evacuationResponsible.getSTATUS().trim().length() > 0) {


                        if (evacuationResponsible.getSTATUS().equalsIgnoreCase(IpAddress.STATUS_STARTED)) {

                            isAdmin = false;
                            evacuationStatusNill = "";
                        } else {
                            isAdmin = false;
                            evacuationStatusNill = "";

                        }
                    } else {

                        isAdmin = false;
                        evacuationStatusNill = null;
                    }


                } else {
                    //no situation
                    isAdmin = false;
                    evacuationStatusNill = null;
                }
            } else {
                //nosituation
                isAdmin = false;
                evacuationStatusNill = null;
            }
        } else {
            //nosituation
            isAdmin = false;
            evacuationStatusNill = null;
        }

        if (isAdmin) {
            evacuationResponsible.setUserType(IpAddress.ADMINUSER);
        } else {
            evacuationResponsible.setUserType(IpAddress.NORMALUSER);
        }
        setEvacuationModel(mGson.toJson(evacuationResponsible));
        mSharedPreferences.edit().putBoolean(IpAddress.ADMINID, isAdmin).apply();
        addDeviceToken();
        return evacuationResponsibleList;
    }


    private void onEvacuation(List<EvacuationResponsible> evacuationResponsibles) {

        show6OR5();
        if (isReached) {
            checkContactPermission(getApp().isContactRequest());
        } else {
            loadMap();
        }
        if (isShow5OR6 && getIntent().hasExtra(IpAddress.EVACUATION)) {


            getIntent().removeExtra(IpAddress.EVACUATION);
            onEvaciationSuccess();

        }

        if (llevacuation.getTag() != null
                || rlevacuate.getTag() != null) {

            if (llevacuation.getTag().equals(true)
                    || rlevacuate.getTag().equals(true)) {

                llevacuation.setTag(false);
                rlevacuate.setTag(false);
                onEvaciationSuccess();
            }
        }

        hidePD();

    }

    public void onEvacuationError(Throwable throwable) {

        llevacuation.setTag(false);
        rlevacuate.setTag(false);
        if (isReached) {
            checkContactPermission(getApp().isContactRequest());
        } else {
            loadMap();
        }
        hidePD();
    }


    private void addDeviceToken() {

        Intent service = new Intent(this, SendDeviceTokenService.class);
        SendDeviceTokenService.enqueWork(getApplicationContext(), service);


    }

    private void show5() {

        rlevacuate.setVisibility(View.GONE);
        PercentLayoutHelper.PercentLayoutParams params =
                (PercentRelativeLayout.LayoutParams) lltwo.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo info = params.getPercentLayoutInfo();

        info.widthPercent = 0.6f;
        lltwo.setLayoutParams((ViewGroup.LayoutParams) params);

        IpAddress.e("SHOW", "5 " + mSharedPreferences.getString(IpAddress.PLACEID_NEW,
                "NO PLACE ID"));
    }

    private void show6() {


        rlevacuate.setVisibility(View.VISIBLE);
        PercentLayoutHelper.PercentLayoutParams params =
                (PercentRelativeLayout.LayoutParams) lltwo.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo info = params.getPercentLayoutInfo();
        info.widthPercent = 1.0f;
        lltwo.setLayoutParams((ViewGroup.LayoutParams) params);

        IpAddress.e("SHOW", "6 " + mSharedPreferences.getString(IpAddress.PLACEID_NEW, "NO PLACE ID"));

    }


    public void getMeetingsByMail() {
        showPD();
        setRetrofit(getApp().getTcpIp());
        mainApi = retrofit.create(MainApi.class);
        SoapAppointmentRequestEnvelope soapAppointmentRequestEnvelope = new SoapAppointmentRequestEnvelope();

        AppointmentRequestBody appointmentRequestBody = new AppointmentRequestBody();

        GetAppointmentsRequest getAppointmentsRequest = new GetAppointmentsRequest();

        getAppointmentsRequest.setA_sUserEmail(getEmail());
        getAppointmentsRequest.setAuthToken(getAuthTokenPref_Mobile());
        Date date = new Date();
        getAppointmentsRequest.setDate(Constants.formatDate(date));
        appointmentRequestBody.setGetAppointments(getAppointmentsRequest);
        soapAppointmentRequestEnvelope.setBody(appointmentRequestBody);

        Observable<SoapAppointmentResponseEnvelope> lClientDetailsObservable = mainApi.getAppointments(soapAppointmentRequestEnvelope);
        lClientDetailsObservable.
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onAppointments, this::onAppointmentsError);

    }

    public void onAppointmentsError(Throwable throwable) {

        hidePD();
        loadMap();
    }


    public void onAppointments(SoapAppointmentResponseEnvelope soapAppointmentResponseEnvelope) {


        hidePD();
        if (!isMyServiceRunning() && allContactList == null) {
            Intent intent = new Intent(this, ContactService.class);
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

            } else {
                intent.putExtra(IpAddress.SKIPCONTACT, true);
            }
            ContactService.enqueWork(getApplicationContext(), intent);
        } else {

            IpAddress.e("service", "running");
        }


        AppointmentsResponseBody appointmentsResponseBody = soapAppointmentResponseEnvelope.getBody();

        if (appointmentsResponseBody != null
                && appointmentsResponseBody.getAppointmentResponse() != null) {


            if (appointmentsResponseBody.getAppointmentResponse().
                    getGetResponsibleDetailsResult().getStatus() == null) {

                Meetings meetings = appointmentsResponseBody.getAppointmentResponse().
                        getGetResponsibleDetailsResult().getMeetings();

                if (meetings != null) {
                    if (meetings.getStatus() != null) {
                        if (meetings.getStatus().get_status().equalsIgnoreCase("NO MEETINGS")) {

                            loadMap();
                        } else if (meetings.getStatus().get_status().equalsIgnoreCase("AVAILABLE")) {

                            rvmeetings.setVisibility(View.VISIBLE);
                            meetingList.clear();
                            meetingList = meetings.getMeeting();
                            meetingsAdapter.notifyDataSetChanged();
                        }
                    } else {

                        loadMap();
                    }
                } else {

                    loadMap();
                }
            } else {

                loadMap();
            }
        } else {
            loadMap();
        }

    }

    private void loadMap() {


        ivdirection.setVisibility(View.VISIBLE);
        rvmeetings.setVisibility(View.GONE);
        meetingList.clear();
        meetingsAdapter.notifyDataSetChanged();

        if (getIntent().getBooleanExtra(IpAddress.SKIPLOCATION, false)
                || skipLocation) {

            hidePD();
        } else {

            IpAddress.e("ischanged", isChanged + "");
            if (!isChanged || marker_desti == null || marker_myloc == null) {
                showErrorDialog();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getApp().setContactRequest(false);
                    getMeetingsByMail();

                } else {

                    getApp().setContactRequest(false);
                    getMeetingsByMail();
                }
                break;

            case MY_PERMISSIONS_REQUEST_READ_LOCATION:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    skipLocation = false;
                    checkLocationSettings();

                } else {

                    skipLocation = true;
                    getLocation(mLocation_my);
                    // showErrorDialog();

                }

                break;


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void checkContactPermission(boolean flag) {

        if (flag) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {

            getMeetingsByMail();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nMgr.cancelAll();
        } catch (Exception e) {

        }


    }

    private void setLocation(City city) {


        if (city != null && city.isSelect()) {
            if (city.getCityName() != null && city.getCityName().trim().length() > 0) {
                if (tvaddress != null)
                    tvaddress.setText(city.getCityName());
            } else if (city.getAddress() != null
                    && !city.getAddress().trim().isEmpty()
                    && city.getAddress().length() > 0) {

                if (tvaddress != null)
                    tvaddress.setText(city.getAddress());
            }
        } else {
            if (tvaddress != null)
                tvaddress.setText("");
        }
    }


    //invite
    @OnClick(R.id.rlinvite)
    public void onInvite(View v) {

        if (city_selected != null && (city_selected.getCityName() != null ||
                city_selected.getAddress() != null || tvaddress.getText().toString().trim().isEmpty())) {

            Intent i = new Intent(this, BookMeetingsActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra(Constants.INVITE, true);
            startActivity(i);

        } else {

            showMessage(getString(R.string.nocityexist));
        }
    }


    //book
    @OnClick(R.id.rlbook)
    public void onBooking(View v) {
        if (city_selected != null) {
            if ((city_selected.getCityName() == null ||
                    city_selected.getRoomCount() == null ||
                    (city_selected.getSkipRoom() != null && city_selected.getSkipRoom().equalsIgnoreCase("TRUE"))
                    ||
                    city_selected.getRoomCount().equalsIgnoreCase("0") || !city_selected.isSelect())) {

                showMessage(getString(R.string.nocity));
            } else {
                Intent i = new Intent(this, BookMeetingsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra(Constants.BOOKROOM, true);
                //startActivityForResult(i, REQUEST_REFRESH);
                startActivity(i);
            }
        } else {

            showMessage(getString(R.string.nocity));
        }
    }


    @OnClick(R.id.rlstatus)
    public void onMyStatus(View v) {


        Intent i = new Intent(this, LeaveAddActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //startActivityForResult(i, REQUEST_REFRESH);
        startActivity(i);

    }

    @OnClick(R.id.rlevacuate)
    public void onEvacuate(View v) {

        rlevacuate.setTag(true);
        getEvacuationDetails(getClientID(), getBarcode());

    }

    private void onEvaciationSuccess() {

        startEvacuationNormal();
    }


    private void startEvacuationAdmin() {

        Intent intent = new Intent(this, EvacuationListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // startActivityForResult(intent, REQUEST_REFRESH);
        startActivity(intent);
    }


    //1253 normaluser
    private void startEvacuationNormal() {

        Intent i = null;

//        if (BuildConfig.DEBUG) {
//            isAdmin = false;
//            evacuationResponsible.setAdminID(null);
//            evacuationStatusNill = null;
//        }

        if (isAdmin || (evacuationResponsible.getAdminID() != null
                && evacuationResponsible.getAdminID().trim().length() > 0)) {
            startEvacuationAdmin();
        } else if (evacuationStatusNill == null) {
            i = new Intent(this, EvacuationNormalList.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // startActivityForResult(i, REQUEST_REFRESH);
            startActivity(i);
        } else {
            i = new Intent(this, EvacuationNormal2.class);
            i.putExtra(IpAddress.HOMESCREEN, true);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // startActivityForResult(i, REQUEST_REFRESH);
            startActivity(i);

        }
        flagNotify = false;
    }

    @OnClick(R.id.rlaccess)
    public void onAccess(View v) {

        Employee employee = getEmp();
        Intent i = new Intent(this, QRCode_generate.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra(IpAddress.GATEOPENER, false);
//        if(employModel.getUSERTYPE()!=null && employModel.getUSERTYPE().equalsIgnoreCase(IpAddress.USERTYPE_ACCESS))
//        {
//            i.putExtra(IpAddress.GATEOPENER,true);
//
//        }else if(employModel.isOPENDOOR())
//        {
//            i.putExtra(IpAddress.GATEOPENER,true);
//        }else
//        {
//            i.putExtra(IpAddress.GATEOPENER,false);
//        }


        startActivity(i);

    }

    @OnClick({R.id.rlgate, R.id.rlgate1})
    public void onGate(View v) {


        Employee employee = getEmp();
        Intent i = new Intent(this, BlunoActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra(IpAddress.USERID, employee.getUSERID());
        if (employee.getFULLNAME() != null && employee.getFULLNAME().trim().length() > 0)
            i.putExtra(IpAddress.USERNAME, employee.getFULLNAME());
        else if (employee.getFIRSTNAME() != null && employee.getFIRSTNAME().trim().length() > 0)
            i.putExtra(IpAddress.USERNAME, employee.getFIRSTNAME());
        startActivity(i);

    }

    @OnClick(R.id.rllocation)
    public void onSettings() {


        Intent i = new Intent(this, LocationActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("NewLoc", "Home");
        //startActivityForResult(i, REQUEST_REFRESH);
        startActivity(i);


    }

    public void directionIntent(double slat, double slan, double dlat, double dlan) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + slat + "," + slan + "&daddr=" +
                        dlat + "," + dlan));
        // startActivityForResult(intent, REQUEST_REFRESH);
        startActivity(intent);

    }


    @OnClick(R.id.ivdirection)
    public void onDirection(View v) {

        if (mLocation_my != null && mLocation_desti != null
                && mLocation_my.getAccuracy() > 0
                && mLocation_desti != null && mLocation_desti.getAccuracy() > 0) {
            directionIntent(mLocation_my.getLatitude(), mLocation_my.getLongitude(),
                    mLocation_desti.getLatitude(), mLocation_desti.getLongitude());
        } else {

        }
    }


    @Override
    public void getLocation(Location mSource) {

        if (mSource != null && mSource.getAccuracy() > 0) {

            if (isLocationFound) {
                isLocationFound = false;
                onMylocation();
            }
        } else {
            hidePD();
        }

    }

    private void onMylocation() {


        if (meetingList != null && meetingList.size() > 0) {

            hidePD();

        } else {

            onMapLoaded();
        }

    }


    private void onMapLoaded() {

        try {
            showPD();
            if (city_selected != null) {
                getLatLongCity(city_selected);
                setLocation(city_selected);
            } else if (marker_desti != null) {
                marker_desti.remove();
                marker_desti = null;
                setLocation(null);
            }
            if (mLocation_my != null
                    && mLocation_my.getAccuracy() > 0) {
                pos_myloc = new LatLng(mLocation_my.getLatitude(), mLocation_my.getLongitude());
                addMarker(pos_myloc, 1);

                if (marker_myloc != null && marker_desti != null) {

                    try {
                        if (pos_myloc != null && pos_desti != null) {

                            if (zero_resultsstatus != null &&
                                    zero_resultsstatus.equalsIgnoreCase("ZERO_RESULTS")) {

                                hidePD();
                                getDirection = false;
                                isTrackingEnable = true;
                            } else {

                                getDirection(pos_myloc, pos_desti);

                            }
                        }
                    } catch (Exception e) {

                        hidePD();
                        isTrackingEnable = true;
                        getDirection = false;
                    }
                } else {
                    isTrackingEnable = true;
                    hidePD();
                }
            } else {
                isTrackingEnable = true;
                hidePD();
            }

        } catch (Exception e) {

            hidePD();
            getDirection = false;
            isTrackingEnable = true;
            IpAddress.e("err", e.toString());
        }
    }

    private void getLatLongCity(City city) {


        if (city != null && city.getLatitude() != 0 && city.getLongitude() != 0) {


            mLocation_desti.setLatitude(city.getLatitude());
            mLocation_desti.setLongitude(city.getLongitude());
            if (mLocation_desti.getLongitude() != 0
                    && mLocation_desti.getLatitude() != 0) {
                pos_desti = new LatLng(mLocation_desti.getLatitude(),
                        mLocation_desti.getLongitude());
                addMarker(pos_desti, 2);
            }

        } else {
            mLocation_desti.setLatitude(getLatitude());
            mLocation_desti.setLongitude(getLongitude());

            if (mLocation_desti.getLongitude() != 0
                    && mLocation_desti.getLatitude() != 0) {

                pos_desti = new LatLng(mLocation_desti.getLatitude(),
                        mLocation_desti.getLongitude());
                addMarker(pos_desti, 2);
            }

        }
    }


    private void addMarker(LatLng pos_desti, int pos) {


        IpAddress.e("addmarker", pos + "");
        if (pos == 1) {

            if (marker_myloc == null) {

                marker_myloc = googleMap.
                        addMarker(new MarkerOptions()
                                .position(pos_desti)
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                .title("Current Address"));

            } else {
                marker_myloc.setPosition(pos_desti);
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos_desti,
                    IpAddress.ZOOMCAMERA));

        } else if (pos == 2) {

            if (marker_desti == null) {

                marker_desti = googleMap.
                        addMarker(new MarkerOptions()
                                .position(pos_desti)
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                .title(city_selected.getCityName() != null ? city_selected.getCityName() : "")
                                .snippet(city_selected.getAddress() != null ? city_selected.getAddress() : "")
                        );

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos_desti, IpAddress.ZOOMCAMERA));

            } else {
                marker_desti.setPosition(pos_desti);
            }


        }

        if (marker_desti != null && marker_myloc != null) {

        }
    }

    private void setBounds(boolean showSinglemarker) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                if (showSinglemarker && l3 > 0 && l4 > 0) {
                    LatLng pos_myloc = new LatLng(l3, l4);
                    cameraUpdate = CameraUpdateFactory.
                            newLatLngZoom(pos_myloc, IpAddress.ZOOMCAMERA);
                    googleMap.moveCamera(cameraUpdate);
                    if (marker_desti != null)
                        marker_desti.showInfoWindow();
                } else if (l1 > 0 && l2 > 0 && l3 > 0 && l4 > 0 && isValidLatLng(l1, l2)
                        && isValidLatLng(l3, l4)) {
                    mLatLngBounds = LatLngBounds.builder();
                    mLatLngBounds.include(new LatLng(l1, l2));
                    mLatLngBounds.include(new LatLng(l3, l4));
                    polyline = googleMap.addPolyline(polylineOptions);
                    cameraUpdate = CameraUpdateFactory.
                            newLatLngBounds(mLatLngBounds.build(), IpAddress.PADDINGMAP);
                    googleMap.moveCamera(cameraUpdate);
                }
            }
        });


    }


    private void setBounds() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (l1 > 0 && l2 > 0 && l3 > 0 && l4 > 0 && isValidLatLng(l1, l2)
                        && isValidLatLng(l3, l4)) {
                    mLatLngBounds = LatLngBounds.builder();
                    mLatLngBounds.include(new LatLng(l1, l2));
                    mLatLngBounds.include(new LatLng(l3, l4));
                    polyline = googleMap.addPolyline(polylineOptions);
                    cameraUpdate = CameraUpdateFactory.
                            newLatLngBounds(mLatLngBounds.build(), IpAddress.PADDINGMAP);

                }
            }
        });


    }

    public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsAdapter.VH> {


        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(getLayoutInflater().inflate(R.layout.inflate_meeting1, parent, false));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            Meeting meeting = meetingList.get(position);
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
                Date date = simpleDateFormat.parse(meeting.getFromDate());
                SimpleDateFormat outFormat = new SimpleDateFormat("EEE");
                String day = outFormat.format(date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                SimpleDateFormat month_date = new SimpleDateFormat("MMM");
                String month_name = month_date.format(calendar.getTime());


                if (meetingList.get(position).getParticipants() != null
                        && !meetingList.get(position).getParticipants().equalsIgnoreCase("0")) {
                    String orange = "0";
                    String white = meetingList.get(position).getParticipants();
                    if(meetingList.get(position).getUnNamedGuests()!= null) {
                        orange  = meetingList.get(position).getUnNamedGuests();
                    }
                    int count = Integer.valueOf(white) - Integer.valueOf(orange);
                    if(count==0){
                        holder.white.setVisibility(View.GONE);
                    }else{
                        holder.white.setVisibility(View.VISIBLE);
                        holder.white.setText(String.valueOf(count));
                    }
                } else {

                    holder.white.setVisibility(View.GONE);
                }
                if (meetingList.get(position).getUnNamedGuests() != null && !meetingList.get(position).getUnNamedGuests().isEmpty() && !meetingList.get(position).getUnNamedGuests().equalsIgnoreCase("0")) {
                        holder.tvOrange.setVisibility(View.VISIBLE);
                        holder.tvOrange.setBackground(getDrawable(R.drawable.rect_buttonorange));
                        holder.tvOrange.setText(meetingList.get(position).getUnNamedGuests());
                }
                else {
                        // Display the counts in the corresponding TextViews
                        holder.tvOrange.setVisibility(View.GONE);
                }


                if (position > 0) {
                    if (meetingList.get(position - 1).getFromDate().equalsIgnoreCase(

                            meetingList.get(position).getFromDate())) {

                        holder.tvday.setText("");
                        holder.tvdate.setText("");

                    } else {


                        holder.tvday.setText(day);

                        holder.tvdate.setText(calendar.get(Calendar.DAY_OF_MONTH) + "." + month_name);

                    }
                } else {
                    holder.tvday.setText(day);

                    holder.tvdate.setText(calendar.get(Calendar.DAY_OF_MONTH) + "." +
                            month_name);
                }


            } catch (Exception e) {


            }


            if (meeting.getSource() != null && meeting.getSource().trim().length() > 0) {
                holder.tv_source.setText(meeting.getDescription());
            }
            if (meeting.getGoogleVisitors() != null && !meeting.getGoogleVisitors().isEmpty() && !meeting.getGoogleVisitors().equalsIgnoreCase("0")) {
                holder.iv_meetingType.setVisibility(View.VISIBLE);
                holder.iv_meetingType.setBackgroundResource(R.drawable.google_image);
                holder.tv_visitors_count.setVisibility(View.VISIBLE);
                holder.tv_visitors_count.setText(meeting.getGoogleVisitors());
            } else if (meeting.getTeamsVisitors() != null && !meeting.getTeamsVisitors().isEmpty() && !meeting.getTeamsVisitors().equalsIgnoreCase("0")) {
                holder.iv_meetingType.setVisibility(View.VISIBLE);
                holder.iv_meetingType.setBackgroundResource(R.drawable.teams_meeting);
                holder.tv_visitors_count.setVisibility(View.VISIBLE);
                holder.tv_visitors_count.setText(meeting.getTeamsVisitors());
            } else {
                holder.iv_meetingType.setVisibility(View.GONE);
                holder.tv_visitors_count.setVisibility(View.GONE);
            }
            if (meeting.getConfirmedParticipants() != null && !meeting.getConfirmedParticipants().isEmpty() && !meeting.getConfirmedParticipants().equalsIgnoreCase("0")) {
                holder.tvGreen.setVisibility(View.VISIBLE);
                holder.tvGreen.setBackground(getDrawable(R.drawable.rect_buttongreen));
                holder.tvGreen.setText(meeting.getConfirmedParticipants());
            } else {
                holder.tvGreen.setVisibility(View.GONE);
            }
            if (meeting.getCancelledParticipants() != null && !meeting.getCancelledParticipants().isEmpty() && !meeting.getCancelledParticipants().equalsIgnoreCase("0")) {
                holder.tvRed.setVisibility(View.VISIBLE);
                holder.tvRed.setBackground(getDrawable(R.drawable.rect_buttonred));
                holder.tvRed.setText(meeting.getCancelledParticipants());
            } else {
                holder.tvRed.setVisibility(View.GONE);
            }

            if (meeting.getConfirmedParticipants() != null && !meeting.getConfirmedParticipants().isEmpty() && !meeting.getConfirmedParticipants().equalsIgnoreCase("0")) {
                holder.tv_visitors_count.setVisibility(View.VISIBLE);
                holder.tv_visitors_count.setText(meeting.getConfirmedParticipants());
            } else {
                holder.tv_visitors_count.setVisibility(View.GONE);
            }
            if (meeting.getRoomName() != null
                    && meeting.getRoomName().trim().length() > 0
                    && meeting.getCityName() != null
                    && meeting.getCityName().trim().length() > 0) {

                holder.tvinvite.setText(meeting.getRoomName() + ", " + meeting.getCityName());
            }
            /*else if (meeting.getDescription() != null
                    && meeting.getDescription().trim().length() > 0
                    && meeting.getCityName() != null
                    && meeting.getCityName().trim().length() > 0) {

                holder.tvinvite.setText(meeting.getDescription() + ", " + meeting.getCityName());
            } else if (meeting.getDescription() != null
                    && meeting.getDescription().trim().length() > 0) {

                holder.tvinvite.setText(meeting.getDescription());
            } */

            else if (meeting.getRoomName() != null
                    && meeting.getRoomName().trim().length() > 0) {

                holder.tvinvite.setText(meeting.getRoomName());
            } else {
                holder.tvinvite.setText(meeting.getCityName());
            }

            holder.tvtime.setText(meeting.getFromTime() + " - " + meeting.getToTime());

            holder.llmeeting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(HomeActivity.this, MyMeetingDetailsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra(Constants.MEETING, mGson.toJson(meetingList.get(holder.getAdapterPosition())));
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return meetingList.size();
        }

        public class VH extends RecyclerView.ViewHolder {


            @BindView(R.id.tvtime)
            TextView tvtime;

            @BindView(R.id.tvdate)
            TextView tvdate;


            @BindView(R.id.tvinvite)
            TextView tvinvite;

            @BindView(R.id.tvday)
            TextView tvday;

            @BindView(R.id.white)
            TextView white;
            @BindView(R.id.llmeeting)
            ViewGroup llmeeting;

            @BindView(R.id.tvGreen)
            TextView tvGreen;
            @BindView(R.id.tvRed)
            TextView tvRed;
            @BindView(R.id.tvOrange)
            TextView tvOrange;
            @BindView(R.id.iv_meetingType)
            ImageView iv_meetingType;
            @BindView(R.id.tv_source)
            TextView tv_source;
            @BindView(R.id.tv_visitors_count)
            TextView tv_visitors_count;

            public VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }


    }


    public void getDirection(LatLng pos_my, LatLng pos_desti) {

        // showDialog();
        polylineOptions = new PolylineOptions();
        l1 = pos_my.latitude;
        l2 = pos_my.longitude;
        l3 = pos_desti.latitude;
        l4 = pos_desti.longitude;
        origin = l1 + "," + l2;
        destination = l3 + "," + l4;

        latLngList = new ArrayList<>();
        MapDirection mapDirection = provideRetrofit(Constants.MAP_URL)
                .create(MapDirection.class);
        Observable<String>
                piingoNotify =
                mapDirection.piingCustomerDirection(origin, destination, false
                        , getString(R.string.apifor)
                );
        piingoNotify.subscribeOn
                        (Schedulers.newThread())
                .map(this::onMapDirection)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDirectionFound, this::onDirectionError);

    }

    private void onDirectionFound(String s1) {


        IpAddress.e("direction", l1 + "\n" + l2 + "\n" + l3 + "\n+l4" + " no");
        if (polyline != null) {
            polyline.remove();
        }


        if (!isDirectionFound) {

            setBounds(true);
        } else if (isValidLatLng(l1, l2)
                && isValidLatLng(l3, l4)) {
            getDirection = true;
            mLatLngBounds = LatLngBounds.builder();
            mLatLngBounds.include(new LatLng(l1, l2));
            mLatLngBounds.include(new LatLng(l3, l4));
            polyline = googleMap.addPolyline(polylineOptions);
            cameraUpdate = CameraUpdateFactory.
                    newLatLngBounds(mLatLngBounds.build(), IpAddress.PADDINGMAP);
            if (cameraUpdate != null) {
                googleMap.moveCamera(cameraUpdate);
            }
        } else {
            if (isValidLatLng(l1, l2)) {
                cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(l1, l2), IpAddress.ZOOMCAMERA);
            } else if (isValidLatLng(l3, l4)) {


                cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(l3, l4), IpAddress.ZOOMCAMERA);

            }
            getDirection = false;
            if (cameraUpdate != null) {
                googleMap.moveCamera(cameraUpdate);
            }
        }


        //  isDirectionFound = true;
        isTrackingEnable = true;
        hidePD();
        IpAddress.e("DIRECTION", "FOUND");
    }

    private void onDirectionError(Throwable throwable) {

        getDirection = false;
        hidePD();
        IpAddress.e("DIRECTION", "NOT FOUND");
        onDirectionFound(null);
    }

    private String onMapDirection(String jsonOutput) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonOutput);

            zero_resultsstatus = jsonObject.getString("status");

            // routesArray contains ALL routes
            JSONArray routesArray = jsonObject.getJSONArray("routes");
            // Grab the first route

            if (routesArray.length() > 0) {

                isDirectionFound = true;
                JSONObject route = routesArray.getJSONObject(0);
                JSONObject poly = route.getJSONObject("overview_polyline");
                String polyline = poly.getString("points");
                pontos = decodePoly(polyline);

                if (pontos != null) {

                    if (latLngList != null) {
                        latLngList.clear();
                    }


                    polylineOptions
                            .width(IpAddress.WIDTH_MAPLINE).color(
                                    ContextCompat.getColor(HomeActivity.this, R.color.bg))
                            .geodesic(true)
                            .addAll(pontos);
//                    for (int i = 0; i < pontos.size() - 1; i++) {
//                        LatLng src = pontos.get(i);
//                        LatLng dest = pontos.get(i + 1);
//                        try {
//
//                            latLngList.add(new LatLng(src.latitude, src.longitude));
//                            latLngList.add(new LatLng(dest.latitude, dest.longitude));
//                            polylineOptions.add(
//                                    new LatLng(src.latitude, src.longitude));
//                            polylineOptions.add(new LatLng(dest.latitude, dest.longitude));
//                            polylineOptions.width(6).color(
//                                    ContextCompat.getColor(HomeActivity.this, R.color.bg));
//                            polylineOptions.geodesic(true);
//                        } catch (NullPointerException e) {
//                            IpAddress.e("Error", "NullPointerException onPostExecute: " + e.toString());
//                        } catch (Exception e2) {
//                            IpAddress.e("Error", "Exception onPostExecute: " + e2.toString());
//                        }
//
//                    }
                }
            } else {
                isDirectionFound = false;
                setBounds(true);
            }
        } catch (JSONException e) {
            IpAddress.e("route", e.toString());
            isDirectionFound = false;
            setBounds(true);
        }
        return jsonOutput;
    }


    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }


}

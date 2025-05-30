package vip.com.vipmeetings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.facebook.drawee.view.SimpleDraweeView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vip.com.vipmeetings.body.FindRoomRequestBody;
import vip.com.vipmeetings.body.FindRoomResponseBody;
import vip.com.vipmeetings.body.GetClientRequestBody;
import vip.com.vipmeetings.body.SaveEventRequestBody;
import vip.com.vipmeetings.body.SaveEventResponseBody;
import vip.com.vipmeetings.envelope.SoapFindRoomRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapFindRoomResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetClient;
import vip.com.vipmeetings.envelope.SoapGetClientResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapSaveEventRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapSaveEventResponseEnvelope;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.interfaces.MainApi;
import vip.com.vipmeetings.interfaces.MapDirection;
import vip.com.vipmeetings.models.City;
import vip.com.vipmeetings.models.Client;
import vip.com.vipmeetings.models.RoomAvailability;
import vip.com.vipmeetings.models.SaveMeetingStatus;
import vip.com.vipmeetings.request.FindRoomAvailability;
import vip.com.vipmeetings.request.GetClient;
import vip.com.vipmeetings.request.SaveEvent;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 02/12/17.
 */

public class ConfirmationActivity extends BaseLocationActivity implements OnMapReadyCallback {


    @BindView(R.id.ivback)
    ImageView ivback;

    @BindView(R.id.ivhome)
    ImageView ivhome;

    @BindView(R.id.tvbook)
    TextView tvbook;

    @BindView(R.id.tvsubject)
    TextView tvsubject;

    @BindView(R.id.tvdate)
    TextView tvdate;

    @BindView(R.id.tvtime)
    TextView tvtime;

    @BindView(R.id.tvtime1)
    TextView tvtime1;

    @BindView(R.id.tvlocation)
    TextView tvlocation;

    @BindView(R.id.tvroom)
    TextView tvroom;

    @BindView(R.id.ivplaceholder)
    SimpleDraweeView ivplaceholder;

    @BindView(R.id.check_security)
    CheckBox checkSecurityInstruction;
    SupportMapFragment map_fragment;
    DateTimeModel dateTimeModel;
    Intent intent;
    City city;
    private GoogleMap googleMap;
    private LatLng pos_myloc, pos_desti;
    private Marker marker_desti;
    Bitmap smallMarker;

    Double l1, l2, l3, l4;
    boolean isDirectionFound;
    boolean getDirection;
    String checkboxStatusSecurity = "0";

    @BindView(R.id.btnnewlocation)
    Button newLocation;

    @BindView(R.id.cb_physical_meeting)
    CheckBox cb_physical_meeting;
    @BindView(R.id.cb_google_meeting)
    CheckBox cb_google_meeting;
    @BindView(R.id.cb_teams_meeting)
    CheckBox cb_teams_meeting;
    @BindView(R.id.llroom)
    LinearLayout llroom;
    @BindView(R.id.ll_show_subject)
    LinearLayout ll_show_subject;
    @BindView(R.id.checkboxParticipantsShowSubjectText)
    CheckBox checkboxParticipantsShowSubjectText;
    @BindView(R.id.checkboxParticipantsToRoomDisplay)
    CheckBox checkboxParticipantsToRoomDisplay;
    @BindView(R.id.checkboxIncludeMeetingToMonitor)
    CheckBox checkboxIncludeMeetingToMonitor;

    String  checkboxParticipantsShowSubjectValue = "False";
    String checkboxParticipantsToRoomDisplayValue= "False";
    String checkboxIncludeMeetingToMonitorValue = "False";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        ButterKnife.bind(this);
        intent = getIntent();
        dateTimeModel = mGson.fromJson(intent.getStringExtra(Constants.DATETIMEMODEL),
                DateTimeModel.class);

        city = getStoreCitySelect();
        int height = IpAddress.MARKER_HEIGHT;
        int width = IpAddress.MARKER_WIDTH;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.marker_myloc2);
        Bitmap b = bitmapdraw.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        checkSecurityInstruction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkboxStatusSecurity = "1";
                }
                else{
                    checkboxStatusSecurity = "0";
                }
            }
        });

        if (city != null && dateTimeModel != null) {

            if(dateTimeModel.getSubject()!=null) {
                tvsubject.setText(dateTimeModel.getSubject());
            }else
            {
                dateTimeModel.setSubject(getSubject());
                tvsubject.setText(getSubject());
            }
            tvdate.setText(dateTimeModel.getDateString());

            if (dateTimeModel.getTime()) {
                tvdate.setText(dateTimeModel.getDateString());

                if(dateTimeModel.getDateString()!=null) {
                    tvtime1.setText("To Date:");
                    tvtime.setText( dateTimeModel.getDateToString());
                }
                else {
                    tvtime.setText("");
                }

                ivplaceholder.setVisibility(View.GONE);
                ivplaceholder.setImageResource(0);
                llroom.setVisibility(View.GONE);
                tvroom.setVisibility(View.GONE);
                ll_show_subject.setVisibility(View.GONE);
                map_fragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map_fragment);
                tvlocation.setText(city.getCityName());

                dateTimeModel.setRoomURL(city.getCityMapUrl());

                map_fragment.getMapAsync(this);

            } else {

                tvtime1.setText("Time:");
                tvtime.setText(
                        String.format("%02d", dateTimeModel.getStartHour()) + "." +
                                String.format("%02d", dateTimeModel.getStartMinute()) + " - " +
                                String.format("%02d", dateTimeModel.getEndHour()) + "." +
                                String.format("%02d", dateTimeModel.getEndMinute()));

                tvlocation.setText(city.getCityName());

                map_fragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map_fragment);

                if (dateTimeModel != null && dateTimeModel.isInvite()) {
                    ivplaceholder.setVisibility(View.GONE);
                    ivplaceholder.setImageResource(0);
                    llroom.setVisibility(View.GONE);
                    tvroom.setVisibility(View.GONE);
                    ll_show_subject.setVisibility(View.GONE);
                    map_fragment.getMapAsync(this);
                } else {

                    try {
                        map_fragment.getView().setVisibility(View.GONE);
                    } catch (Exception e) {

                    }
                    newLocation.setVisibility(View.GONE);
                    tvroom.setText(dateTimeModel.getRoomName());
                    dateTimeModel.setRoomURL(city.getCityMapUrl());
                    ivplaceholder.setImageURI(dateTimeModel.getImage1());
                    ll_show_subject.setVisibility(View.VISIBLE);
                    setupCheckboxLogic();

                }
            }


        }
        cb_physical_meeting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    cb_google_meeting.setClickable(false);
                    cb_teams_meeting.setClickable(false);
                    cb_google_meeting.setChecked(false);
                    cb_teams_meeting.setChecked(false);
                } else{
                    cb_google_meeting.setClickable(true);
                    cb_teams_meeting.setClickable(true);
                }
            }
        });
        cb_teams_meeting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cb_google_meeting.setClickable(false);
                    cb_google_meeting.setChecked(false);
                }
                else{
                    cb_google_meeting.setClickable(true);
                    cb_google_meeting.setChecked(false);
                }
            }
        });
        cb_google_meeting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cb_teams_meeting.setClickable(false);
                    cb_teams_meeting.setChecked(false);
                }
                else{
                    cb_teams_meeting.setClickable(true);
                    cb_teams_meeting.setChecked(false);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dateTimeModel = mGson.fromJson(intent.getStringExtra(Constants.DATETIMEMODEL),
                DateTimeModel.class);

        city = getStoreCitySelect();
        tvlocation.setText(city.getCityName());
    }

    @OnClick(R.id.btnnewlocation)
    public void setNewLocation() {
        Intent i = new Intent(this, LocationActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("NewLoc","ConfirmBook");
        i.putExtra(Constants.DATETIMEMODEL, mGson.toJson(dateTimeModel));
        //startActivityForResult(i, REQUEST_REFRESH);
        startActivity(i);


    }
    private void setupCheckboxLogic() {
        // Check if ll_show_subject is visible
        if (ll_show_subject.getVisibility() == View.VISIBLE) {

            checkboxParticipantsToRoomDisplay.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Set value based on checkbox state
                checkboxParticipantsToRoomDisplayValue = isChecked ? "True" : "False";
            });

            checkboxIncludeMeetingToMonitor.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Set value based on checkbox state
                checkboxParticipantsToRoomDisplayValue = isChecked ? "True" : "False";
            });
            // Example logic for setting isPrivate
            checkboxParticipantsShowSubjectText.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Set value based on checkbox state
                checkboxParticipantsShowSubjectValue = isChecked ?  "True" : "False";
            });


        }
    }
    @Override
    public void getLocation(Location mSource) {
        if (mSource != null) {

            if (pos_myloc == null) {
                pos_myloc = new LatLng(mSource.getLatitude(), mSource.getLongitude());
                if (googleMap != null) {
                    addMarker(googleMap, pos_myloc, 1);

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos_myloc,
                            IpAddress.ZOOMCAMERA));
                    if (marker_myloc != null && marker_desti != null) {

                        if (!getDirection && pos_myloc != null && pos_desti != null) {
                            getDirection = true;
                            getDirection(marker_myloc.getPosition(), marker_desti.getPosition());
                        }

                    }
                }

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
        // if(latLngList==null) {
        latLngList = new ArrayList<>();
        MapDirection mapDirection = provideRetrofit(MAP_URL)
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
        // }

    }

    private void setBounds(boolean showSinglemarker) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(showSinglemarker && l3>0 && l4>0)
                {
                    LatLng  pos_myloc = new LatLng(l3, l4);
                    cameraUpdate = CameraUpdateFactory.
                            newLatLngZoom(pos_myloc, IpAddress.ZOOMCAMERA);
                    googleMap.moveCamera(cameraUpdate);
                    if(marker_desti!=null)
                        marker_desti.showInfoWindow();
                }else

                if (l1>0 && l2>0 && l3>0 && l4>0 && isValidLatLng(l1, l2)
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



    private void onDirectionFound(String s1) {


        IpAddress.e("direction", s1 + " no");
        if (polyline != null) {
            polyline.remove();
        }

        if(!isDirectionFound)
        {
            setBounds(true);
        }else

        if (isValidLatLng(l1, l2)
                && isValidLatLng(l3, l4)) {
            mLatLngBounds = LatLngBounds.builder();
            mLatLngBounds.include(new LatLng(l1, l2));
            mLatLngBounds.include(new LatLng(l3, l4));
            polyline = googleMap.addPolyline(polylineOptions);
            cameraUpdate = CameraUpdateFactory.
                    newLatLngBounds(mLatLngBounds.build(), IpAddress.PADDINGMAP);
            if (cameraUpdate != null)
                googleMap.moveCamera(cameraUpdate);
        } else {


            if (isValidLatLng(l1, l2)) {

                cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(l1, l2), IpAddress.ZOOMCAMERA);
            } else if (isValidLatLng(l3, l4)) {
                cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(l3, l4), IpAddress.ZOOMCAMERA);

            } else {
                // cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(l3, l4), IpAddress.ZOOMCAMERA);

            }
            if (cameraUpdate != null)
                googleMap.moveCamera(cameraUpdate);
        }

        getDirection = false;

    }

    private void onDirectionError(Throwable throwable) {

        getDirection = false;
        onDirectionFound(null);
        setBounds(true);
    }

    private String onMapDirection(String jsonOutput) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonOutput);


            // routesArray contains ALL routes
            JSONArray routesArray = jsonObject.getJSONArray("routes");
            // Grab the first route

            if (routesArray.length() > 0) {
                isDirectionFound=true;
                JSONObject route = routesArray.getJSONObject(0);
                JSONObject poly = route.getJSONObject("overview_polyline");
                String polyline = poly.getString("points");
                pontos = decodePoly(polyline);

                if (pontos != null) {

                    if (latLngList != null) {
                        latLngList.clear();
                    }

                    polylineOptions
                            .geodesic(true)
                            .addAll(pontos)
                            .width(6).color(
                                    ContextCompat.getColor(ConfirmationActivity.this, R.color.bg));


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
//                                    ContextCompat.getColor(ConfirmationActivity.this, R.color.bg));
//                            polylineOptions.geodesic(true);
//                        } catch (NullPointerException e) {
//                            IpAddress.e("Error", "NullPointerException onPostExecute: " + e.toString());
//                        } catch (Exception e2) {
//                            IpAddress.e("Error", "Exception onPostExecute: " + e2.toString());
//                        }
//
//                    }
                }
            }else
            {
                isDirectionFound=false;
            }
        } catch (JSONException e) {
            isDirectionFound=false;
            IpAddress.e("route", e.toString());
        }
        return jsonOutput;
    }


    private void addMarker(GoogleMap googleMap, LatLng pos_desti, int pos) {


        this.pos_desti = pos_desti;

        if (pos == 1 && googleMap != null) {

            marker_myloc = googleMap.
                    addMarker(new MarkerOptions()
                            .position(pos_desti)
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                            .title("My Loc")
                    );


        } else if (googleMap != null) {
            marker_desti = googleMap.
                    addMarker(new MarkerOptions()
                            .position(pos_desti)
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                            .title(city.getCityName()!=null?city.getCityName():"")
                            .snippet(city.getAddress()!=null?city.getAddress():"")
                    );
        }
    }


    @OnClick(R.id.tvbook)
    public void onBook(View v) {


        if (!dateTimeModel.isInvite()
                && dateTimeModel.getRoomId() != null && dateTimeModel.getRoomId()
                .trim().length() > 0) {

            findRoom();

        } else {
            bookRoom();
        }

    }

    private void findRoom() {

        setRetrofit(getApp().getTcpIp());
        MainApi mainApi = retrofit.create(MainApi.class);
        SoapFindRoomRequestEnvelope soapFindRoomRequestEnvelope = new SoapFindRoomRequestEnvelope();

        FindRoomRequestBody findRoomRequestBody = new FindRoomRequestBody();

        FindRoomAvailability findRoomAvailability = new FindRoomAvailability();
        findRoomAvailability.setAuthToken(getAuthTokenPref_Mobile());
        findRoomAvailability.setA_sRoomId(dateTimeModel.getRoomId());
        findRoomAvailability.setA_sRoomName(dateTimeModel.getRoomName());
        findRoomAvailability.setA_sFromDateYMD(dateTimeModel.getFormatDate());
        findRoomAvailability.setA_sToDateYMD(dateTimeModel.getFormatDate());

        findRoomAvailability.setA_sFromTime(String.format("%02d", dateTimeModel.getStartHour()) + "." +
                String.format("%02d", dateTimeModel.getStartMinute()));
        findRoomAvailability.setA_sToTime(String.format("%02d", dateTimeModel.getEndHour()) + "." +

                String.format("%02d", dateTimeModel.getEndMinute()));

        findRoomAvailability.setA_sUserId(employModel.getUSERID());
        findRoomAvailability.setA_sMeetingId("");
        findRoomAvailability.setA_sRoomSelectionId("");


        findRoomRequestBody.setFindRoomAvailability(findRoomAvailability);
        soapFindRoomRequestEnvelope.setBody(findRoomRequestBody);
        Observable<SoapFindRoomResponseEnvelope> lClientDetailsObservable =
                mainApi.FindRoomAvailability(
                        soapFindRoomRequestEnvelope);

        lClientDetailsObservable.
                subscribeOn(Schedulers.newThread())
                .map(this::onSoapFindRoom)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onfindRoom, this::onError);
    }

    public void onError(Throwable throwable) {


        hidePD();
        showToast(getString(R.string.someerror));
    }

    private RoomAvailability onSoapFindRoom(SoapFindRoomResponseEnvelope soapFindRoomResponseEnvelope) {


        FindRoomResponseBody findRoomResponseBody = soapFindRoomResponseEnvelope.getBody();

        return findRoomResponseBody.getFindRoomAvailabilityResponse()
                .getFindRoomAvailabilityResult().getRoomAvailability();
    }

    private void onfindRoom(RoomAvailability status) {


        if (status != null && status.getStatus() != null) {

            if (status.getStatus().get_status().equalsIgnoreCase("Success")) {


                if (status.getRoomSelectionId() != null) {

                    setRoomSelection(status.getRoomSelectionId().get_roomselectionid());
                    bookRoom();
                }


            } else if (status.getStatus().get_status().equalsIgnoreCase("FAILED")) {
                {

//                    if (status.Error != null && status.Error.get_error() != null) {
//                        showToast(status.Error.get_error() + "");
//
//                    } else
                    showToast(status.getError().get_error() + "");
                }
            }


        } else {

        }

    }


    @OnClick(R.id.ivback)
    public void onBack(View v) {
        onBackPressed();
    }

    @OnClick(R.id.ivhome)
    public void onHome(View v) {

        gotoHome();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (this.googleMap != null)
            this.googleMap.clear();
        this.googleMap = googleMap;
        Location mLocation = new Location("des");

        mLocation.setLatitude(Double.parseDouble(mSharedPreferences.getString(Constants.LAT,
                "0")));
        mLocation.setLongitude(Double.parseDouble(mSharedPreferences.getString(Constants.LON,
                "0")));

        if (mLocation.getLatitude() != 0 && mLocation.getLongitude() != 0) {

            ivplaceholder.setImageResource(0);
            pos_desti = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            if (marker_desti == null) {

                if (city != null)
                    if (city.getAddress() != null
                            && !city.getAddress().isEmpty()
                            && city.getAddress().trim().length() > 0) {
                        marker_desti = googleMap.
                                addMarker(new MarkerOptions()
                                        .position(pos_desti)
                                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                        .title(city.getCityName()!=null?city.getCityName():"")
                                        .snippet(city.getAddress()!=null?city.getAddress():"")
                                );
                    } else

                        marker_desti = googleMap.
                                addMarker(new MarkerOptions()
                                        .position(pos_desti)
                                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                        .title(city.getCityName()!=null? city.getCityName(): "")
                                        .snippet(city.getAddress()!=null ?city.getAddress():"")
                                );
            } else {
                marker_desti.setPosition(pos_desti);
                if (city != null)
                    if (city.getAddress() != null
                            && !city.getAddress().isEmpty()
                            && city.getAddress().trim().length() > 0) {
                        // marker_desti.setTitle(city.getAddress());

                        marker_desti.setTitle(city.getCityName()!=null? city.getCityName(): "");
                        marker_desti.setSnippet(city.getAddress()!=null ?city.getAddress():"");
                    } else {
                        marker_desti.setTitle(city.getCityName());
                    }
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos_desti,
                    IpAddress.ZOOMCAMERA));
        } else if (mSharedPreferences.contains(IpAddress.LOCATIONLON)) {
            mLocation_desti.setLatitude(Double.parseDouble
                    (mSharedPreferences.getString(IpAddress.LOCATIONLAT, "0")));
            mLocation_desti.setLongitude(Double.parseDouble
                    (mSharedPreferences.getString(IpAddress.LOCATIONLON, "0")));
            pos_desti = new LatLng(mLocation_desti.getLatitude(), mLocation_desti.getLongitude());
            addMarker(googleMap, pos_desti, 1);
            if (googleMap != null) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos_desti, IpAddress.ZOOMCAMERA));
            }
        }

        showErrorDialog();

    }


    private void bookRoom() {

        showPD();
        setRetrofit(getApp().getTcpIp());
        MainApi mainApi = retrofit.create(MainApi.class);
        Constants.e("GSON", mGson.toJson(dateTimeModel));
        Observable<SoapSaveEventResponseEnvelope> lClientDetailsObservable;

        String A_sLocaion = getAddress();
        String RoomId = null;
        if (dateTimeModel.getRoomId() != null && dateTimeModel.getRoomId().length() > 0) {

            A_sLocaion = "";
            RoomId = dateTimeModel.getRoomId();

        } else if (A_sLocaion != null && A_sLocaion.trim().length() > 0) {


            RoomId = "";

        } else {
            A_sLocaion = "";
            RoomId = "";
        }

        if (RoomId != null && A_sLocaion != null) {


            SoapSaveEventRequestEnvelope soapSaveEventRequestEnvelope = new SoapSaveEventRequestEnvelope();


            SaveEventRequestBody saveEventRequestBody = new SaveEventRequestBody();

            SaveEvent saveEvent = new SaveEvent();


            saveEvent.setA_sUserEmail(getEmail());
            saveEvent.setA_sDescription(dateTimeModel.getSubject());
            saveEvent.setA_sFromDateYMD(dateTimeModel.getFormatDate());

            if (dateTimeModel.getTime()) {
                saveEvent.setA_sToDateYMD(dateTimeModel.getFormatToDate());
                // 00:00 & 24:00

                dateTimeModel.setStartHour(0);
                dateTimeModel.setStartMinute(0);
                dateTimeModel.setEndHour(23);
                dateTimeModel.setEndMinute(59);


                saveEvent.setA_sStartTime(String.format("%02d", dateTimeModel.getStartHour()) + "." +
                        String.format("%02d", dateTimeModel.getStartMinute()));
                saveEvent.setA_sEndTime(String.format("%02d", dateTimeModel.getEndHour()) + "." +
                        String.format("%02d", dateTimeModel.getEndMinute()));


            } else {
                saveEvent.setA_sToDateYMD(dateTimeModel.getFormatDate());
                saveEvent.setA_sStartTime(String.format("%02d", dateTimeModel.getStartHour()) + "." +
                        String.format("%02d", dateTimeModel.getStartMinute()));
                saveEvent.setA_sEndTime(String.format("%02d", dateTimeModel.getEndHour()) + "." +
                        String.format("%02d", dateTimeModel.getEndMinute()));
            }


            if (saveEvent.getA_sEndTime() != null &&
                    saveEvent.getA_sEndTime().equalsIgnoreCase(IpAddress.ENDTIME)) {
                saveEvent.setA_sEndTime(IpAddress.ENDTIME2);
            }

            saveEvent.setA_sRoomId(RoomId);
            saveEvent.setA_sLocation(A_sLocaion);
            saveEvent.setAuthToken(getAuthTokenPref_Mobile());
            saveEvent.setA_sIsPrivate(checkboxParticipantsShowSubjectValue);
            saveEvent.setA_sShowGuestsOnDisplay(checkboxParticipantsToRoomDisplayValue);
            saveEvent.setA_sShowMeetingsOnDisplay(checkboxIncludeMeetingToMonitorValue);
            saveEventRequestBody.setSaveEvent(saveEvent);
            soapSaveEventRequestEnvelope.setBody(saveEventRequestBody);

            lClientDetailsObservable =
                    mainApi.saveEvent(soapSaveEventRequestEnvelope);

            lClientDetailsObservable.
                    subscribeOn(Schedulers.newThread())
                    .map(this::onSoapSaveEvent)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::saveMeeting, this::onError);
        }
    }

    private SaveMeetingStatus onSoapSaveEvent(SoapSaveEventResponseEnvelope soapSaveEventResponseEnvelope) {


        SaveEventResponseBody saveEventResponseBody = soapSaveEventResponseEnvelope.getBody();
        return saveEventResponseBody.getSaveEventResponse().getSaveEventResult().getStatus();
    }


    private void saveMeeting(SaveMeetingStatus saveMeetingStatus) {


        hidePD();
        if (saveMeetingStatus != null) {
            if (saveMeetingStatus.getStatus() != null)
                if (saveMeetingStatus.getStatus().get_status().equalsIgnoreCase("SUCCESS")) {

                    setMeetingId(saveMeetingStatus.getMeetingId().get_meetingid());
                    getClient();
                } else if (saveMeetingStatus.getStatus().get_status().equalsIgnoreCase("FAILED")) {


                    if(saveMeetingStatus.getError().get_error()!=null)
                        showMessage(saveMeetingStatus.getError().get_error());
                }
        }

    }

    private void getClient() {

        showPD();
        setRetrofit(getApp().getTcpIp());
        mainApi = retrofit.create(MainApi.class);

        SoapGetClient soapGetClient = new SoapGetClient();

        GetClientRequestBody getClientRequestBody = new GetClientRequestBody();

        GetClient getClient = new GetClient();

        getClient.setAuthToken(getAuthTokenPref_Mobile());
        getClient.setsClientID(getClientID());

        getClientRequestBody.setGetClient(getClient);

        soapGetClient.setBody(getClientRequestBody);


        Observable<SoapGetClientResponseEnvelope> lClientDetailsObservable =
                mainApi.getClient(soapGetClient);
        lClientDetailsObservable.
                subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetClient, this::onError);
    }

    private void onGetClient(SoapGetClientResponseEnvelope soapGetClientResponseEnvelope) {

        hidePD();

        try {

            Client client = soapGetClientResponseEnvelope.getBody().getGetClientResponse().getGetClientResult().getClient();
            if (client != null) {

                isPatient = client.isPatientSystem();

                storeIsPatient(isPatient);

                addParticipant();

            }
        } catch (Exception e) {

        }

    }


    public void addParticipant() {
        dateTimeModel.setSecurityInstruction(checkboxStatusSecurity);
        StringBuilder isJoined = new StringBuilder("");
        if(cb_physical_meeting.isChecked()){
            isJoined.append("LOCATION");
            if (cb_google_meeting.isChecked()) {
                isJoined.append(",GOOGLE");
            } else if (cb_teams_meeting.isChecked()) {
                isJoined.append(",TEAMS");
            }
        }
        else {
            isJoined = new StringBuilder("");
        }
        dateTimeModel.setJoinOption(String.valueOf(isJoined));

        if (!getIntent().hasExtra(Constants.BOOKROOM)) {


            if (isPatient()) {

                Intent i = new Intent(this, AddParticipants.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra(IpAddress.BACK, false);
                i.putExtra(Constants.DATETIMEMODEL, mGson.toJson(dateTimeModel));
                startActivity(i);

            } else {

                Intent i = new Intent(this, AddParticipantSplashActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra(Constants.DATETIMEMODEL, mGson.toJson(dateTimeModel));
                startActivity(i);
            }
        } else if (getIntent().hasExtra(Constants.BOOKROOM)) {

            Intent i = new Intent(this, AddParticipants.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            dateTimeModel.setBook(false);
            i.putExtra(IpAddress.BACK, false);
            i.putExtra(Constants.DATETIMEMODEL, mGson.toJson(dateTimeModel));
            startActivity(i);
        } else {

            roomList();
        }
    }

    public void roomList() {

        //skiproom no
        if (getStoreCitySelect() == null || getStoreCitySelect().getSkipRoom().equalsIgnoreCase("False")) {
            Intent i = new Intent(this, RoomListActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            dateTimeModel.setBook(true);
            i.putExtra(Constants.DATETIMEMODEL, mGson.toJson(dateTimeModel));
            startActivity(i);
        }
    }


    public class GetDirection extends AsyncTask<String, String, String> {


        Double l1, l2, l3, l4;
        ProgressDialog dialog;

        public GetDirection(LatLng pos_my, LatLng pos_desti) {
//            l1 = 17.4863276;
//            l2 = 78.5318644;
//
//            l3 = 17.3688;
//            l4 = 78.5247;

            l1 = pos_my.latitude;
            l2 = pos_my.longitude;

            l3 = pos_desti.latitude;
            l4 = pos_desti.longitude;

            latLngList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ConfirmationActivity.this);
            dialog.setMessage("please wait!");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        protected String doInBackground(String... args) {


            showPolyLine();
            return null;

        }

        private void showPolyLine() {


            destination = l1 + "," + l2;

            // origin = mLocation.getLatitude() + "," + mLocation.getLongitude();
            origin = l3 + "," + l4;
            // destination = geoLocation.latitude + "," + geoLocation.longitude;
            String stringUrl =
                    "http://maps.googleapis.com/maps/api/directions/json?origin="
                            + origin + "&destination=" + destination + "&sensor=false";
            Constants.e("urlconfirm", stringUrl);
            StringBuilder response = new StringBuilder();
            try {
                polylineOptions = new PolylineOptions();
                URL url = new URL(stringUrl);
                HttpURLConnection httpconn = (HttpURLConnection) url
                        .openConnection();
                if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader input = new BufferedReader(
                            new InputStreamReader(httpconn.getInputStream()),
                            8192);
                    String strLine = null;

                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();
                }
                String jsonOutput = response.toString();

                JSONObject jsonObject = new JSONObject(jsonOutput);

                // routesArray contains ALL routes
                JSONArray routesArray = jsonObject.getJSONArray("routes");
                // Grab the first route

                if (routesArray.length() > 0) {


                    JSONObject route = routesArray.getJSONObject(0);

                    JSONObject poly = route.getJSONObject("overview_polyline");
                    String polyline = poly.getString("points");
                    pontos = decodePoly(polyline);

                    if (pontos != null && !isCancelled()) {

                        if (latLngList != null) {
                            latLngList.clear();
                        }
                        for (int i = 0; i < pontos.size() - 1; i++) {
                            LatLng src = pontos.get(i);
                            LatLng dest = pontos.get(i + 1);
                            try {

                                latLngList.add(new LatLng(src.latitude, src.longitude));
                                latLngList.add(new LatLng(dest.latitude, dest.longitude));

                                if (polylineOptions != null) {
                                    polylineOptions.add(
                                            new LatLng(src.latitude, src.longitude));

                                    polylineOptions.add(new LatLng(dest.latitude, dest.longitude));
                                }

                            } catch (NullPointerException e) {
                                Log.e("Error", "NullPointerException onPostExecute: " + e.toString());
                            } catch (Exception e2) {
                                Log.e("Error", "Exception onPostExecute: " + e2.toString());
                            }

                        }
                    }


                }

            } catch (Exception e) {


                Log.e("err", e.toString());
            }
        }

        protected void onPostExecute(String file_url) {


            try {

                dialog.dismiss();
                if (polylineOptions != null) {
                    polylineOptions.width(7).color(
                            ContextCompat.getColor(ConfirmationActivity.this, R.color.bg));
                    polylineOptions.geodesic(true);
                    polyline = googleMap.addPolyline(polylineOptions);

                } else {
                    polyline.setPoints(latLngList);
                }
                mLatLngBounds = LatLngBounds.builder();
                mLatLngBounds.include(new LatLng(l1, l2));
                mLatLngBounds.include(new LatLng(l3, l4));
                cameraUpdate = CameraUpdateFactory.newLatLngBounds(mLatLngBounds.build(),
                        50, 50, 3);
                googleMap.moveCamera(cameraUpdate);
            } catch (Exception e) {
                Log.e("errr", e.toString());
            }

        }
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

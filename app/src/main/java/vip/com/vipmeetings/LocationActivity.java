package vip.com.vipmeetings;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import vip.com.vipmeetings.body.CitiesRequestBody;
import vip.com.vipmeetings.body.CitiesResponseBody;
import vip.com.vipmeetings.body.GetPlaceIDRequestBody;
import vip.com.vipmeetings.body.GetPlacesRequestBody;
import vip.com.vipmeetings.envelope.SoapCitiesRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapCitiesResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetPlaceIDRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetPlaceIDResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetPlacesRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetPlacesResponseEnvelope;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.fcm.SendDeviceTokenService;
import vip.com.vipmeetings.interfaces.IGoogleApiClient;
import vip.com.vipmeetings.interfaces.MainApi;
import vip.com.vipmeetings.interfaces.MapDirection;
import vip.com.vipmeetings.models.Cities;
import vip.com.vipmeetings.models.City;
import vip.com.vipmeetings.models.GetPlaceID;
import vip.com.vipmeetings.models.GetPlaces;
import vip.com.vipmeetings.models.PlaceModel;
import vip.com.vipmeetings.request.GetLocationsRequest;
import vip.com.vipmeetings.request.GetPlaceIDRequest;
import vip.com.vipmeetings.response.GetPlaceIDResult;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 01/09/17.
 */


public class LocationActivity extends BaseActivity implements IGoogleApiClient,
        OnMapReadyCallback {

    private static final String TAG = LocationActivity.class.getSimpleName();
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1253;
    private static final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 1200;
    GoogleApiAvailability mGoogleApiAvailability;
    Location mLocation_my, mLocation_desti;
    AutocompleteSupportFragment place_autocomplete_fragment;
    SupportMapFragment mapFragment;
    int i;
    GoogleMap googleMap;
    private LatLng pos_myloc, pos_desti;
    private Marker marker_myloc, marker_desti;
    List<City> mCitiesList, mCityListOld;
    @BindView(R.id.rvlocation)
    RecyclerView rvlocation;
    LocationAdapter mLocationAdapter;


    @BindView(R.id.ivleft)
    ImageView ivleft;

    @BindView(R.id.tvremove)
    AppCompatTextView tvremove;



    @BindView(R.id.ivright)
    ImageView ivright;

    List<LatLng> pontos;
    String origin, destination;
    LatLngBounds.Builder mLatLngBounds;
    PolylineOptions polylineOptions;
    Polyline polyline;
    CameraUpdate cameraUpdate;
    List<LatLng> latLngList;
    City city_selected;
    private String formatted;
    Bitmap smallMarker;
    boolean flag;
    BitmapDrawable bitmapdraw;
    Bitmap b;
    private AlertDialog alertDialog;

    List<City> cities_local;
    Double l1, l2, l3, l4;
    private boolean getDirection;

    LocationChange locationChange;
    FusedLocationProviderClient fusedLocationProviderClient;
    private boolean getCurrentAddress;

    boolean skipLocation;

    boolean isTrackingEnable = true;
    private int adapterPosition = -1;

    HashMap<String, Location> locationHashMap;
    boolean isMapLoded = false;
    boolean isDirectionFound=false;

    PlacesClient placesClient;

    List<com.google.android.libraries.places.api.model.Place.Field> placeFields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.ADDRESS);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        setClientCallbacks(this);
        ButterKnife.bind(this);


        if(mSharedPreferencesLogin.contains(IpAddress.UUID))
        {
            tvremove.setVisibility(View.VISIBLE);

        }else
        {
            tvremove.setVisibility(View.GONE);
        }

        if (!eventBus.isRegistered(this))
            eventBus.register(this);
        if (getIntent().hasExtra(Constants.ACTIVITY_MAIN)) {

            ivleft.setVisibility(View.INVISIBLE);
        }

        locationChange = new LocationChange();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        buildLocationSettingsRequest(createLocationRequest());

        mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map_fragment);

        initPlaces();
        placeEnable();


        locationHashMap = new HashMap<>();
        mCitiesList = new ArrayList<>();
        mCityListOld = new ArrayList<>();

        mLocationAdapter = new LocationAdapter();
        rvlocation.setLayoutManager(new LinearLayoutManager(this));
        rvlocation.setAdapter(mLocationAdapter);


        bitmapdraw = (BitmapDrawable)
                getResources().getDrawable(R.mipmap.marker_myloc2);
        b = bitmapdraw.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        cities_local = new ArrayList<>();
        onCreateonNewIntent();
    }
    private void initPlaces() {
        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.apifor));
        }
        placesClient = Places.createClient(this);
    }

    @OnClick(R.id.tvremove)
    public  void onRemove(View v)
    {

        mSharedPreferencesLogin.edit().remove(IpAddress.UUID).apply();
        tvremove.setVisibility(View.GONE);
    }


    private void onCreateonNewIntent() {

        showPD();
        skipLocation = false;
        getCurrentAddress = false;
        isMapLoded = false;
        mCitiesList.clear();
        mCityListOld.clear();
        mLocationAdapter.notifyDataSetChanged();
        cities_local.clear();
        List<City> allCities = getAllCityStored();
        city_selected = getStoreCitySelect();
        if (allCities != null && allCities.size() > 0) {
            for (City city : allCities) {
                if (city != null && city.isLocal()) {
                    cities_local.add(city);
                }
            }
        }
        storeAllCityClear();

        if (isGooglePlayServicesAvailable(this)) {

            locationEnable();

        } else {
            showErrorDialog();

        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        onCreateonNewIntent();

    }


    public void checkInternal() {

        if (getApp().isStaticReached()) {

            isReached = getApp().isReached();
            updateUi(getApp().isReached());


        } else {
            Single.fromCallable(this::loadInBackground)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::updateUi, this::Error);
        }
    }

    private void Error(Throwable throwable) {

        hidePD();
        showToast(getString(R.string.someerror));
    }


    //1253
    private void getPlaceIDByPlaceName(String placeName) {
        try {

            showPD();
            IpAddress.e("PLACENAME", placeName + " empty");
            if (placeName != null && placeName.trim().length() > 0) {
                SoapGetPlaceIDRequestEnvelope
                        soapSendEvaciationPushRequestEnvelope = new SoapGetPlaceIDRequestEnvelope();

                GetPlaceIDRequestBody getPlaceIDRequestBody = new
                        GetPlaceIDRequestBody();


                GetPlaceIDRequest getPlaceIDRequest = new GetPlaceIDRequest();


                getPlaceIDRequest.setA_sClientId(getClientID());
                getPlaceIDRequest.setAuthToken(getAuthTokenPref_Evacuation());
                getPlaceIDRequest.setA_sPlaceName(placeName);


                getPlaceIDRequestBody.setGetPlaceID(getPlaceIDRequest);

                soapSendEvaciationPushRequestEnvelope.setBody(getPlaceIDRequestBody);

                Observable<SoapGetPlaceIDResponseEnvelope> lClientDetailsObservable =
                        service.getPlaceID(soapSendEvaciationPushRequestEnvelope);

                lClientDetailsObservable.
                        subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onSuccessPlaceID, this::onErrorPlaceID);
            } else {
                showToast(getString(R.string.empty_cityname));
            }
        } catch (Exception e) {


            onPlaceID();
            hidePD();

        }

    }

    private void onPlaceID() {

        if (adapterPosition != -1) {
            isTrackingEnable = true;
            city_selected = mCitiesList.get(adapterPosition);
            city_selected.setSelect(true);
            storeSelectedCity(city_selected);
            storeAllCity(mCitiesList);
            adapterPosition = -1;
            addDeviceToken();


            if (getIntent().hasExtra(Constants.ACTIVITY_MAIN)) {
                onright();
            } else
                notifyDataAdapter();
        }
    }

    private void onErrorPlaceID(Throwable throwable) {

        hidePD();
        onPlaceID();
    }

    private void onSuccessPlaceID(SoapGetPlaceIDResponseEnvelope soapGetPlaceIDResponseEnvelope) {


        if (soapGetPlaceIDResponseEnvelope != null) {

            if (soapGetPlaceIDResponseEnvelope.getBody() != null) {
                GetPlaceIDResult getPlaceIDResult = soapGetPlaceIDResponseEnvelope.getBody()
                        .getGetPlaceIDResponse().getGetPlaceIDResult();

                if (getPlaceIDResult != null) {

                    if (getPlaceIDResult.getGetPlaceID() != null) {

                        GetPlaceID getPlaceID = getPlaceIDResult.getGetPlaceID();

                        if (getPlaceID.getStatus() != null
                                && getPlaceID.getStatus().equalsIgnoreCase(IpAddress.SUCCESS)) {


                            mSharedPreferences.edit().putString(IpAddress.PLACEID_NEW,
                                    getPlaceID.getPlaceID()).apply();
                            city_selected.setPlaceID(getPlaceID.getPlaceID());

                            storeSelectedCity(city_selected);

                        } else {
                            storeSelectedCity(city_selected);
                            mSharedPreferences.edit().putString(IpAddress.PLACEID_NEW, null).apply();
                        }
                    } else {
                        storeSelectedCity(city_selected);
                        mSharedPreferences.edit().putString(IpAddress.PLACEID_NEW, null).apply();
                    }
                } else {
                    storeSelectedCity(city_selected);
                    mSharedPreferences.edit().putString(IpAddress.PLACEID_NEW, null).apply();
                }
            }
        }


        onPlaceID();
        hidePD();
    }


    public void updateUi(boolean isReached) {


        if (isReached) {
            //get locations
            //ip connect avuthe
            getCountries();

        } else {
            getPlaces();
        }
    }


    private void addDeviceToken() {

        Intent service = new Intent(this, SendDeviceTokenService.class);
        SendDeviceTokenService.enqueWork(getApplicationContext(), service);

    }

    private void getPlaces() {

        showPD();
        setOKHTTPAfterLogin();
        setRetrofit(Constants.IPADDRESS2);
        setService();
        SoapGetPlacesRequestEnvelope soapGetPlacesRequestEnvelope = new SoapGetPlacesRequestEnvelope();
        GetPlaces getPlaces = new GetPlaces();

        getPlaces.setAuthToken(getAuthTokenPref_Evacuation());
        getPlaces.setA_sClientId(getClientID());


        GetPlacesRequestBody getPlacesRequestBody = new GetPlacesRequestBody();
        getPlacesRequestBody.setGetPlaces(getPlaces);

        soapGetPlacesRequestEnvelope.setBody(getPlacesRequestBody);


        Observable<SoapGetPlacesResponseEnvelope> lClientDetailsObservable =
                service.getPlaces(soapGetPlacesRequestEnvelope);


        lClientDetailsObservable.
                subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetPlaces, this::onError);
    }

    private void onGetPlaces(SoapGetPlacesResponseEnvelope soapGetPlacesResponseEnvelope) {


        if (soapGetPlacesResponseEnvelope != null) {


            if (soapGetPlacesResponseEnvelope.getBody() != null) {

                List<PlaceModel> placeModels = soapGetPlacesResponseEnvelope.getBody()
                        .getGetPlacesResponse().getGetPlacesResult()
                        .getPlaces().get(0).getPlaceModelList();

                mCityListOld.clear();

                if (placeModels != null && placeModels.size() > 0) {

                    mCitiesList.clear();
                    for (PlaceModel placeModel : placeModels) {
                        City city = new City();
                        city.setCityName(placeModel.getName());
                        city.setPlaceID(placeModel.getPlaceID());
                        mCityListOld.add(city);
                    }
                }


                if (cities_local != null && cities_local.size() > 0) {

                    for (City cityOLD : cities_local) {

                        if (!mCitiesList.contains(cityOLD)) {

                            mCitiesList.add(cityOLD);
                        }
                    }
                    //  mCitiesList.addAll(cities_local);
                }
                if (mCityListOld != null && mCityListOld.size() > 0
                        && mCitiesList != null && mCitiesList.size() > 0) {
                    for (City cityOLD : mCityListOld) {

                        if (!mCitiesList.contains(cityOLD)) {

                            mCitiesList.add(cityOLD);
                        }
                    }
                } else {
                    mCitiesList.addAll(mCityListOld);
                }

                if (city_selected != null
                        && city_selected.isLocal() && !mCitiesList.contains(city_selected))
                    mCitiesList.add(city_selected);


                notifyDataAdapter();


            }

        }
        hidePD();
    }


    @OnClick(R.id.tvcurrentloc)
    public void onCurrentLoc(View v) {


        showPD();
        getCurrentAddress = true;
        checkLocationPermission();


    }


    public void onCurrentAddress() {


        if (getCurrentAddress && mLocation_my != null && mLocation_my.getAccuracy() > 0) {

            getLocationFromAddress_LATLANG(mLocation_my.getLatitude(),
                    mLocation_my.getLongitude());
        } else {
            showToast(getString(R.string.nolocation));
        }

    }

    public void placeEnable() {


        try {
            place_autocomplete_fragment=(AutocompleteSupportFragment)getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
            place_autocomplete_fragment.setPlaceFields(placeFields);
//        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
//                .setCountry("Norway")
//                .build();
//
//        autocompleteFragment.setFilter(typeFilter);

            place_autocomplete_fragment.setHint(IpAddress.SEARCHLOC);


            View clearBtn = place_autocomplete_fragment.getView()
                    .findViewById(com.google.android.libraries.places.R.id.places_autocomplete_clear_button);

            clearBtn.setOnClickListener(v -> {
                place_autocomplete_fragment.setText("");
                clearBtn.setVisibility(View.GONE);
            });


            AppCompatEditText editText = place_autocomplete_fragment.getView()
                    .findViewById(com.google.android.libraries.places.R.id.places_autocomplete_search_input);


            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    switch (actionId) {

                        case EditorInfo.IME_ACTION_SEARCH:

                            getLocationFromAddress2(v.getText().toString());
                            return true;
                    }

                    return false;
                }
            });
        } catch (Exception e) {

            IpAddress.e("errr", e.toString());
        }

// Construct a request object, passing the place ID and fields array.

        place_autocomplete_fragment
                .setOnPlaceSelectedListener(new PlaceSelectionListener() {

                    @Override
                    public void onPlaceSelected(@NonNull com.google.android.libraries.places.api.model.Place place) {
                        IpAddress.e(TAG, "Place: " + place.getName() +
                                place.getAddress());
// Get the address from the Place object
                        String address = place.getAddress();

// Use Geocoder to get latitude and longitude from the address
                        Geocoder geocoder = new Geocoder(LocationActivity.this);
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocationName(address, 1);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        if (addresses != null && addresses.size() > 0) {
                            double latitude = addresses.get(0).getLatitude();
                            double longitude = addresses.get(0).getLongitude();

                            // Now you can use latitude and longitude values as needed
                            showDialogwithAddress(place, latitude, longitude);
                        } else {
                            // Handle case where latitude and longitude couldn't be obtained
                            // For example, display an error message or log a warning
                            IpAddress.e(TAG, "Unable to get latitude and longitude for the address: " + address);
                        }
                    }

                    @Override
                    public void onError(Status status) {
                        try {
                            showToast(status.getStatusMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void showDialogwithAddress(com.google.android.libraries.places.api.model.Place place,Double latitude, Double longitude) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);

        View view = getLayoutInflater().inflate(R.layout.inflate_dialog, null);

        TextView tvlocation = view.findViewById(R.id.tvlocation);
        tvlocation.setText(place.getName() + " " + place.getAddress());
        Button btcancel = view.findViewById(R.id.btcancel);
        Button btok = view.findViewById(R.id.btok);
        EditText etidentity = view.findViewById(R.id.etidentity);

        btok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (etidentity.getText().toString().trim().length() > 0) {

                    if (place_autocomplete_fragment != null) {
                        place_autocomplete_fragment.setText("");
                    }

                    if (alertDialog != null &&
                            alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                    hideKeyboard(etidentity);

                    addCity(place.getName().toString(),
                            place.getName().toString() + " " +
                                    place.getAddress().toString(), true, true,
                            "0", "0", "False", "True",
                            etidentity.getText().toString().trim(), "",
                            latitude, longitude);

                    if (googleMap != null) {

                        // pos_desti = place.getLatLng();
                        mLocation_desti = new Location("desti");
                        mLocation_desti.setLatitude(latitude);
                        mLocation_desti.setLongitude(longitude);
                        storeLatLng(mLocation_desti.getLatitude(), mLocation_desti.getLongitude());
                        mSharedPreferences.edit().putString(IpAddress.PLACEID_NEW, null).apply();
                        onright();
                    }

                } else {
                    etidentity.setError("Enter location identity");
                }
            }
        });

        btcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (alertDialog != null &&
                        alertDialog.isShowing()) {

                    if (place_autocomplete_fragment != null) {
                        place_autocomplete_fragment.setText("");
                    }
                    alertDialog.dismiss();


                }
            }
        });

        builder.setView(view);
        alertDialog = builder.show();

    }


    private void getCountries() {

        showPD();
        setOKHTTPAfterLogin();
        setRetrofit(getApp().getTcpIp());
        mainApi = retrofit.create(MainApi.class);
        setService();
        SoapCitiesRequestEnvelope soapCitiesRequestEnvelope = new SoapCitiesRequestEnvelope();
        GetLocationsRequest getLocationsRequest = new GetLocationsRequest();
        getLocationsRequest.setAuthToken(getAuthTokenPref_Mobile());
        CitiesRequestBody citiesRequestBody = new CitiesRequestBody();
        citiesRequestBody.setGetLocationsRequest(getLocationsRequest);

        soapCitiesRequestEnvelope.setBody(citiesRequestBody);
        Observable<SoapCitiesResponseEnvelope> lClientDetailsObservable =
                mainApi.getLocations(soapCitiesRequestEnvelope);
        lClientDetailsObservable.
                subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCities, this::onLocationError);
    }

    private void onLocationError(Throwable throwable) {

        notifyDataAdapter();
        hidePD();
    }


    public void onCities(SoapCitiesResponseEnvelope soapCitiesResponseEnvelope) {

        CitiesResponseBody citiesResponseBody = soapCitiesResponseEnvelope.getBody();
        Cities cities = citiesResponseBody.getGetLocationsResponse()
                .getGetLocationsResult().getCities();
        mCityListOld.clear();
        if (cities != null) {

            addCities(cities.getCity());
        } else {
            Constants.noData(1);
        }

        hidePD();
    }

    private void addCities(List<City> city) {

        mCitiesList.clear();
        mCityListOld.addAll(city);

//        if (cities_local != null && cities_local.size() > 0) {
//            mCitiesList.addAll(cities_local);
//        }
        if (cities_local != null && cities_local.size() > 0) {

            for (City cityOLD : cities_local) {

                if (!mCitiesList.contains(cityOLD)) {

                    mCitiesList.add(cityOLD);
                }
            }
            //  mCitiesList.addAll(cities_local);
        }
        if (mCityListOld != null && mCityListOld.size() > 0
                && mCitiesList != null && mCitiesList.size() > 0) {
            for (City cityOLD : mCityListOld) {

                if (!mCitiesList.contains(cityOLD)) {

                    mCitiesList.add(cityOLD);
                }
            }
        } else {
            mCitiesList.addAll(mCityListOld);
        }
        if (city_selected != null && city_selected.isLocal() && !mCitiesList.contains(city_selected))
            mCitiesList.add(city_selected);

        notifyDataAdapter();
    }


    @OnClick(R.id.ivdirection)
    public void onDirection(View v) {

        if (mLocation_my != null && mLocation_desti != null) {
            directionIntent(mLocation_my.getLatitude(), mLocation_my.getLongitude(),
                    mLocation_desti.getLatitude(), mLocation_desti.getLongitude());
        } else {

        }
    }


    public void directionIntent(double slat, double slan, double dlat, double dlan) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + slat + "," + slan + "&daddr=" +
                        dlat + "," + dlan));
        startActivity(intent);
    }

//    @OnClick(R.id.ivright)
//    public void onRight(View v) {
//
//        onright();
//
//    }
//
//    private void onright() {
//
//        try {
//
//            if (isReached) {
//                if (getIntent().getBooleanExtra(Constants.BACK, false)) {
//
//                    Intent i = new Intent(this, HomeActivity.class);
//                    if (city_selected != null) {
//                        i.putExtra(Constants.SKIPROOM, city_selected.getSkipRoom());
//                    }
//
//                    i.putExtra(IpAddress.SKIPLOCATION, skipLocation);
//                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(i);
//                    finish();
//
//                } else {
//
//                    Intent intent = new Intent();
//                    intent.putExtra(IpAddress.PLACEID_NEW, true);
//                    setResult(RESULT_OK, intent);
//                    finish();
//                }
//
//            } else if (city_selected != null) {
//
//                if (getIntent().hasExtra(Constants.HOME)
//                        || getIntent().hasExtra(Constants.SKIPROOMS)) {
//
//                    onBackPressed();
//                } else {
//
//                    Intent i = new Intent(this, HomeActivity.class);
//                    i.putExtra(Constants.SKIPROOM, city_selected.getSkipRoom());
//                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                    i.putExtra(IpAddress.SKIPLOCATION, skipLocation);
//                    startActivity(i);
//
//                    finish();
//                }
//            } else {
//                Toast.makeText(this,
//                        "Please select any location to proceed....", Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e) {
//
//            IpAddress.e("err", e.toString());
//        }
//    }

//    @OnClick(R.id.ivleft)
//    public void onLeft(View v) {
//
//        onBackPressed();
//    }

//    @Override
//    public void onBackPressed() {
//
//
//        if (getIntent().hasExtra(Constants.ACTIVITY_MAIN)) {
//
//
//        } else if (isReached || city_selected != null) {
//
//            Intent intent = getIntent();
//            intent.putExtra(IpAddress.SKIPLOCATION, skipLocation);
//            intent.putExtra(IpAddress.PLACEID_NEW, true);
//            setResult(RESULT_OK, intent);
//            finish();
//        } else {
//            Toast.makeText(this, "Please select any location to proceed....", Toast.LENGTH_SHORT).show();
//
//        }
//
//
//    }

    @Override
    public void onBackPressed() {

        storeAllCity(mCitiesList);
        hideSoft();
        if (getIntent().hasExtra(Constants.SKIPROOMS)) {

            setResult(RESULT_OK);
            finish();

        } else if (getIntent().hasExtra(Constants.ACTIVITY_MAIN)) {


        } else if (city_selected != null && city_selected.isSelect()) {

            Intent intent = getIntent();
            intent.putExtra(IpAddress.SKIPLOCATION, skipLocation);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Please select any location to proceed....", Toast.LENGTH_SHORT).show();

        }


    }


    @OnClick(R.id.ivleft)
    public void onLeft(View v) {

        onBackPressed();
    }

//    @Override
//    public void onBackPressed() {
//
//
//        if (getIntent().hasExtra(Constants.BACK)) {
//
//
//        } else if (city_selected != null) {
//
//            Intent intent = new Intent(this, HomeActivity.class);
//            intent.putExtra(IpAddress.SKIPLOCATION, skipLocation);
//            setResult(RESULT_OK, intent);
//            finish();
//        } else {
//            Toast.makeText(this, "Please select any location to proceed....", Toast.LENGTH_SHORT).show();
//
//        }
//
//
//    }


    @OnClick({R.id.ivright, R.id.tvnext})
    public void onRight(View v) {

        onright();

    }

    private void onright() {

        storeAllCity(mCitiesList);
        hideSoft();
        String source = getIntent().getStringExtra("NewLoc");
        // Handle navigation based on source

        if (getIntent().hasExtra(Constants.SKIPROOMS)) {

            setResult(RESULT_OK);
            finish();

        }
        else if(source != null && source.equalsIgnoreCase("EditBook")){
            onBackPressed();
        }
        else if(source != null && source.equalsIgnoreCase("ConfirmBook")){
            City city = getStoreCitySelect();
            onBackPressed();
        }

        else if (city_selected != null && city_selected.isSelect()) {

            Intent i = new Intent(this, HomeActivity.class);
            i.putExtra(Constants.SKIPROOM, city_selected.getSkipRoom());
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra(IpAddress.SKIPLOCATION, skipLocation);
            startActivity(i);
            finish();

        }
        else {
            Toast.makeText(this,
                    "Please select any location to proceed....", Toast.LENGTH_SHORT).show();
        }


    }

    private void navigateFromSource(String source) {
        Intent intent = new Intent();

        switch (source) {
            case "EditBook":
                intent = new Intent(this, BookMeetingsActivity.class);
                break;
            case "ConfirmBook":
                intent = new Intent(this, ConfirmationActivity.class);
                break;
            default:
                // Handle default case or error condition
                break;
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    public String onLocationMap(String response) {

        String  formatted = "";
        try {
            JSONObject jsonObject = new JSONObject(response);

            String status_address = jsonObject.getString("status");
            if (status_address.equalsIgnoreCase("OK")) {

                JSONArray jsonArray = jsonObject.getJSONArray("results");

                if (jsonArray.length() > 0) {

                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                    JSONObject location = jsonObject1.getJSONObject("geometry")
                            .getJSONObject("location");

                    formatted = jsonObject1.getString("formatted_address");
                    IpAddress.e("format", formatted);


                }
            }
        } catch (Exception e) {

            IpAddress.e("format", formatted + e.toString());
            setBounds(true);
        }
        return formatted;


    }

    public void onLocationFound(String formatted) {

        this.formatted=formatted;
        IpAddress.e("DIRECTION", "onLocationFound");
        if (getCurrentAddress && formatted != null && mLocation_my != null
                && mLocation_my.getAccuracy() > 0
                && formatted.length() > 0) {


            getCurrentAddress = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
            View view = getLayoutInflater().inflate(R.layout.inflate_dialog, null);
            TextView tvlocation = view.findViewById(R.id.tvlocation);
            tvlocation.setText(formatted);
            Button btcancel = view.findViewById(R.id.btcancel);
            Button btok = view.findViewById(R.id.btok);
            EditText etidentity = view.findViewById(R.id.etidentity);

            btok.setOnClickListener(view1 -> {


                if (etidentity.getText().toString().trim().length() > 0) {

                    hideKeyboard(etidentity);

                    if (alertDialog != null && alertDialog.isShowing()) {
                        alertDialog.getWindow().setSoftInputMode
                                (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        alertDialog.dismiss();
                    }
                    storeLatLng(mLocation_my.getLatitude(), mLocation_my.getLongitude());
                    addCity(formatted,
                            formatted, true, true,
                            "0", "0", "False", "True",
                            etidentity.getText().toString().trim(), "",
                            mLocation_my.getLatitude(), mLocation_my.getLongitude());

                    mSharedPreferences.edit().putString(IpAddress.PLACEID_NEW, null).apply();

                    if (googleMap != null) {

                        afterLocation();
                        onright();

                    }
                } else {
                    etidentity.setError("Enter location identity");
                }
            });

            btcancel.setOnClickListener(view12 -> {


                if (alertDialog != null &&
                        alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            });

            builder.setView(view);
            alertDialog = builder.show();

        } else {
            showToast(getString(R.string.nolocation));
        }
        hidePD();
    }


    public void onLocationErr(Throwable throwable) {

        IpAddress.e("DIRECTION", "onLocationErr");
        showToast(throwable.toString());
        hidePD();
    }

    public void getCurrentLocationFromLATLANG(double lati, double longi) {



        MapDirection mapDirection = provideRetrofit(Constants.MAP_URL)
                .create(MapDirection.class);

        Observable<String>
                piingoNotify =
                mapDirection.piingAddressByLocation(
                        lati + "," + longi
                        , getString(R.string.apifor));
        piingoNotify.subscribeOn
                        (Schedulers.newThread())
                .map(this::onLocationMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCurrentLocationFound, this::onCurrentLocationErr);

    }

    private void onCurrentLocationErr(Throwable throwable) {

        hidePD();
        setBounds(true);
    }

    private void onCurrentLocationFound(String s) {

        hidePD();

        IpAddress.e("formatted2",s);

        if(marker_myloc!=null)
        {
            marker_myloc.setTitle("Current Address");
            marker_myloc.setSnippet(s);
        }

        if(isDirectionFound)
        {

        }else
        {
            setBounds(true);
        }


    }

    public void getLocationFromAddress_LATLANG(double lati, double longi) {


        MapDirection mapDirection = provideRetrofit(Constants.MAP_URL)
                .create(MapDirection.class);

        Observable<String>
                piingoNotify =
                mapDirection.piingAddressByLocation(
                        lati + "," + longi
                        , getString(R.string.apifor));
        piingoNotify.subscribeOn
                        (Schedulers.newThread())
                .map(this::onLocationMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLocationFound, this::onLocationErr);

    }


    public Location onAddressMap(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status_address = jsonObject.getString("status");

            if (status_address.equalsIgnoreCase("OK")) {

                JSONArray jsonArray = jsonObject.getJSONArray("results");

                if (jsonArray.length() > 0) {

                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                    JSONObject loc = jsonObject1.getJSONObject("geometry")
                            .getJSONObject("location");

                    mLocation_desti = new Location("desti");
                    mLocation_desti.setLongitude(loc.getDouble("lng"));
                    mLocation_desti.setLatitude(loc.getDouble("lat"));

                    if (city_selected != null && city_selected.isSelect()) {
                        city_selected.setLatitude(mLocation_desti.getLatitude());
                        city_selected.setLongitude(mLocation_desti.getLongitude());
                    }

                    storeLatLng(mLocation_desti.getLatitude(), mLocation_desti.getLongitude());

                }

                return createNewLocation(mLocation_desti.getLongitude(), mLocation_desti.getLatitude());
            }


        } catch (JSONException e) {

        }

        return createNewLocation(0, 0);


    }

    public void onAddressFound(Location location) {


        mLocation_desti = location;
        if (location != null && location.getAccuracy() > 0) {


            storeLatLng(location.getLatitude(),
                    location.getLongitude());

        }
        IpAddress.e("DIRECTION", "onAddressFound");
        afterLocation();
    }

    public void onAddressError(Throwable throwable) {

        IpAddress.e("DIRECTION", "onAddressError");
        afterLocation();
        hidePD();
    }


    public void getLocationFromAddress2(String strAddress) {


        try {

            MapDirection mapDirection = provideRetrofit(Constants.MAP_URL)
                    .create(MapDirection.class);

            Observable<String>
                    piingoNotify =
                    mapDirection.piinggetLocationByNameorCityName(strAddress, getString(R.string.apifor));
            piingoNotify.subscribeOn
                            (Schedulers.newThread())
                    .map(this::onAddressMap)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onAddressFound, this::onAddressError);


        } catch (Exception ex) {

            Constants.e("errrr", ex.toString());
        }
    }

    private void storeLatLng(double latitude, double longitude) {

        IpAddress.e("latlong", latitude + " " + longitude + "");

        mSharedPreferences.edit().putString(Constants.LAT, String.valueOf(latitude)).apply();
        mSharedPreferences.edit().putString(Constants.LON, String.valueOf(longitude)).apply();
        if (city_selected != null) {

            if (city_selected.getCityName() != null) {

                locationHashMap.put(city_selected.getCityName(),
                        createNewLocation(latitude, longitude));
            }

            IpAddress.e("hashmap", locationHashMap.toString());

            city_selected.setLatitude(latitude);
            city_selected.setLongitude(longitude);
            storeSelectedCity(city_selected);
        }
    }


    private void showErrorDialog() {

        mGoogleApiAvailability = GoogleApiAvailability.getInstance();
        i = mGoogleApiAvailability.isGooglePlayServicesAvailable(getApplicationContext());
        if (i == ConnectionResult.SUCCESS) {

            locationEnable();

        } else if (mGoogleApiAvailability.isUserResolvableError(i)) {


            mGoogleApiAvailability.getErrorDialog(this, i,
                    REQUEST_GOOGLE_PLAY_SERVICES, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {


                            showErrorDialog();

                        }
                    }).show();

        } else {

            hidePD();
            showToast(mGoogleApiAvailability.getErrorString(REQUEST_GOOGLE_PLAY_SERVICES));
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    Location createNewLocation(double longitude, double latitude) {


        Location location = new Location(IpAddress.VIPRECEPTION);
        location.setLongitude(longitude);
        location.setLatitude(latitude);
        return location;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IpAddress.e("req", requestCode + " " + resultCode + " ");

        String address = null;

// Ensure the AutocompleteSupportFragment is not null
        if (place_autocomplete_fragment != null) {
            // Get the fragment's view
            View fragmentView = place_autocomplete_fragment.getView();

            // Ensure the fragment's view is not null
            if (fragmentView != null) {
                // Find the EditText by its ID
                EditText editText = fragmentView.findViewById(
                        com.google.android.libraries.places.R.id.places_autocomplete_search_input
                );

                // Ensure the EditText is not nullpackage vip.com.vipmeetings;
                //
                //import android.Manifest;
                //import android.app.Activity;
                //import android.content.DialogInterface;
                //import android.content.Intent;
                //import android.content.IntentSender;
                //import android.content.pm.PackageManager;
                //import android.graphics.Bitmap;
                //import android.graphics.drawable.BitmapDrawable;
                //import android.location.Address;
                //import android.location.Geocoder;
                //import android.location.Location;
                //import android.net.Uri;
                //import android.os.Bundle;
                //import android.os.Looper;
                //import android.util.Log;
                //import android.view.KeyEvent;
                //import android.view.View;
                //import android.view.ViewGroup;
                //import android.view.WindowManager;
                //import android.view.inputmethod.EditorInfo;
                //import android.widget.Button;
                //import android.widget.EditText;
                //import android.widget.ImageView;
                //import android.widget.RelativeLayout;
                //import android.widget.TextView;
                //import android.widget.Toast;
                //
                //import com.google.android.gms.common.ConnectionResult;
                //import com.google.android.gms.common.GoogleApiAvailability;
                //import com.google.android.gms.common.api.ApiException;
                //import com.google.android.gms.common.api.ResolvableApiException;
                //import com.google.android.gms.common.api.Status;
                //import com.google.android.gms.location.FusedLocationProviderClient;
                //import com.google.android.gms.location.LocationRequest;
                //import com.google.android.gms.location.LocationServices;
                //import com.google.android.gms.location.LocationSettingsRequest;
                //import com.google.android.gms.location.LocationSettingsResponse;
                //import com.google.android.gms.location.LocationSettingsStates;
                //import com.google.android.gms.location.LocationSettingsStatusCodes;
                //import com.google.android.gms.maps.CameraUpdate;
                //import com.google.android.gms.maps.CameraUpdateFactory;
                //import com.google.android.gms.maps.GoogleMap;
                //import com.google.android.gms.maps.OnMapReadyCallback;
                //import com.google.android.gms.maps.SupportMapFragment;
                //import com.google.android.gms.maps.model.BitmapDescriptorFactory;
                //import com.google.android.gms.maps.model.LatLng;
                //import com.google.android.gms.maps.model.LatLngBounds;
                //import com.google.android.gms.maps.model.Marker;
                //import com.google.android.gms.maps.model.MarkerOptions;
                //import com.google.android.gms.maps.model.Polyline;
                //import com.google.android.gms.maps.model.PolylineOptions;
                //import com.google.android.gms.tasks.OnCompleteListener;
                //import com.google.android.gms.tasks.OnFailureListener;
                //import com.google.android.gms.tasks.Task;
                //import com.google.android.libraries.places.api.Places;
                //import com.google.android.libraries.places.api.net.PlacesClient;
                //import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
                //import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
                //
                //import org.greenrobot.eventbus.Subscribe;
                //import org.json.JSONArray;
                //import org.json.JSONException;
                //import org.json.JSONObject;
                //
                //import java.io.IOException;
                //import java.util.ArrayList;
                //import java.util.Arrays;
                //import java.util.HashMap;
                //import java.util.List;
                //
                //import androidx.annotation.NonNull;
                //import androidx.annotation.Nullable;
                //import androidx.appcompat.app.AlertDialog;
                //import androidx.appcompat.widget.AppCompatEditText;
                //import androidx.appcompat.widget.AppCompatTextView;
                //import androidx.core.app.ActivityCompat;
                //import androidx.core.content.ContextCompat;
                //import androidx.recyclerview.widget.LinearLayoutManager;
                //import androidx.recyclerview.widget.RecyclerView;
                //import butterknife.BindView;
                //import butterknife.ButterKnife;
                //import butterknife.OnClick;
                //import io.reactivex.Observable;
                //import io.reactivex.Single;
                //import io.reactivex.android.schedulers.AndroidSchedulers;
                //import io.reactivex.schedulers.Schedulers;
                //import okhttp3.Call;
                //import okhttp3.Callback;
                //import okhttp3.OkHttpClient;
                //import okhttp3.Request;
                //import okhttp3.Response;
                //import vip.com.vipmeetings.body.CitiesRequestBody;
                //import vip.com.vipmeetings.body.CitiesResponseBody;
                //import vip.com.vipmeetings.body.GetPlaceIDRequestBody;
                //import vip.com.vipmeetings.body.GetPlacesRequestBody;
                //import vip.com.vipmeetings.envelope.SoapCitiesRequestEnvelope;
                //import vip.com.vipmeetings.envelope.SoapCitiesResponseEnvelope;
                //import vip.com.vipmeetings.envelope.SoapGetPlaceIDRequestEnvelope;
                //import vip.com.vipmeetings.envelope.SoapGetPlaceIDResponseEnvelope;
                //import vip.com.vipmeetings.envelope.SoapGetPlacesRequestEnvelope;
                //import vip.com.vipmeetings.envelope.SoapGetPlacesResponseEnvelope;
                //import vip.com.vipmeetings.evacuate.IpAddress;
                //import vip.com.vipmeetings.fcm.SendDeviceTokenService;
                //import vip.com.vipmeetings.interfaces.IGoogleApiClient;
                //import vip.com.vipmeetings.interfaces.MainApi;
                //import vip.com.vipmeetings.interfaces.MapDirection;
                //import vip.com.vipmeetings.models.Cities;
                //import vip.com.vipmeetings.models.City;
                //import vip.com.vipmeetings.models.GetPlaceID;
                //import vip.com.vipmeetings.models.GetPlaces;
                //import vip.com.vipmeetings.models.PlaceModel;
                //import vip.com.vipmeetings.request.GetLocationsRequest;
                //import vip.com.vipmeetings.request.GetPlaceIDRequest;
                //import vip.com.vipmeetings.response.GetPlaceIDResult;
                //import vip.com.vipmeetings.utilities.Constants;
                //
                ///**
                // * Created by Srinath on 01/09/17.
                // */
                //
                //
                //public class LocationActivity extends BaseActivity implements IGoogleApiClient,
                //        OnMapReadyCallback {
                //
                //    private static final String TAG = LocationActivity.class.getSimpleName();
                //    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
                //    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1253;
                //    private static final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 1200;
                //    GoogleApiAvailability mGoogleApiAvailability;
                //    Location mLocation_my, mLocation_desti;
                //    AutocompleteSupportFragment place_autocomplete_fragment;
                //    SupportMapFragment mapFragment;
                //    int i;
                //    GoogleMap googleMap;
                //    private LatLng pos_myloc, pos_desti;
                //    private Marker marker_myloc, marker_desti;
                //    List<City> mCitiesList, mCityListOld;
                //    @BindView(R.id.rvlocation)
                //    RecyclerView rvlocation;
                //    LocationAdapter mLocationAdapter;
                //
                //
                //    @BindView(R.id.ivleft)
                //    ImageView ivleft;
                //
                //    @BindView(R.id.tvremove)
                //    AppCompatTextView tvremove;
                //
                //
                //
                //    @BindView(R.id.ivright)
                //    ImageView ivright;
                //
                //    List<LatLng> pontos;
                //    String origin, destination;
                //    LatLngBounds.Builder mLatLngBounds;
                //    PolylineOptions polylineOptions;
                //    Polyline polyline;
                //    CameraUpdate cameraUpdate;
                //    List<LatLng> latLngList;
                //    City city_selected;
                //    private String formatted;
                //    Bitmap smallMarker;
                //    boolean flag;
                //    BitmapDrawable bitmapdraw;
                //    Bitmap b;
                //    private AlertDialog alertDialog;
                //
                //    List<City> cities_local;
                //    Double l1, l2, l3, l4;
                //    private boolean getDirection;
                //
                //    LocationChange locationChange;
                //    FusedLocationProviderClient fusedLocationProviderClient;
                //    private boolean getCurrentAddress;
                //
                //    boolean skipLocation;
                //
                //    boolean isTrackingEnable = true;
                //    private int adapterPosition = -1;
                //
                //    HashMap<String, Location> locationHashMap;
                //    boolean isMapLoded = false;
                //    boolean isDirectionFound=false;
                //
                //    PlacesClient placesClient;
                //
                //    List<com.google.android.libraries.places.api.model.Place.Field> placeFields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.ADDRESS);
                //
                //    @Override
                //    protected void onCreate(@Nullable Bundle savedInstanceState) {
                //        super.onCreate(savedInstanceState);
                //        setContentView(R.layout.activity_location);
                //        setClientCallbacks(this);
                //        ButterKnife.bind(this);
                //
                //
                //        if(mSharedPreferencesLogin.contains(IpAddress.UUID))
                //        {
                //            tvremove.setVisibility(View.VISIBLE);
                //
                //        }else
                //        {
                //            tvremove.setVisibility(View.GONE);
                //        }
                //
                //        if (!eventBus.isRegistered(this))
                //            eventBus.register(this);
                //        if (getIntent().hasExtra(Constants.ACTIVITY_MAIN)) {
                //
                //            ivleft.setVisibility(View.INVISIBLE);
                //        }
                //
                //        locationChange = new LocationChange();
                //        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                //        buildLocationSettingsRequest(createLocationRequest());
                //
                //        mapFragment = (SupportMapFragment)
                //                getSupportFragmentManager().findFragmentById(R.id.map_fragment);
                //
                //        initPlaces();
                //        placeEnable();
                //
                //
                //        locationHashMap = new HashMap<>();
                //        mCitiesList = new ArrayList<>();
                //        mCityListOld = new ArrayList<>();
                //
                //        mLocationAdapter = new LocationAdapter();
                //        rvlocation.setLayoutManager(new LinearLayoutManager(this));
                //        rvlocation.setAdapter(mLocationAdapter);
                //
                //
                //        bitmapdraw = (BitmapDrawable)
                //                getResources().getDrawable(R.mipmap.marker_myloc2);
                //        b = bitmapdraw.getBitmap();
                //        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                //        cities_local = new ArrayList<>();
                //        onCreateonNewIntent();
                //    }
                //    private void initPlaces() {
                //        // Setup Places Client
                //        if (!Places.isInitialized()) {
                //            Places.initialize(getApplicationContext(), getString(R.string.apifor));
                //        }
                //        placesClient = Places.createClient(this);
                //    }
                //
                //    @OnClick(R.id.tvremove)
                //    public  void onRemove(View v)
                //    {
                //
                //        mSharedPreferencesLogin.edit().remove(IpAddress.UUID).apply();
                //        tvremove.setVisibility(View.GONE);
                //    }
                //
                //
                //    private void onCreateonNewIntent() {
                //
                //        showPD();
                //        skipLocation = false;
                //        getCurrentAddress = false;
                //        isMapLoded = false;
                //        mCitiesList.clear();
                //        mCityListOld.clear();
                //        mLocationAdapter.notifyDataSetChanged();
                //        cities_local.clear();
                //        List<City> allCities = getAllCityStored();
                //        city_selected = getStoreCitySelect();
                //        if (allCities != null && allCities.size() > 0) {
                //            for (City city : allCities) {
                //                if (city != null && city.isLocal()) {
                //                    cities_local.add(city);
                //                }
                //            }
                //        }
                //        storeAllCityClear();
                //
                //        if (isGooglePlayServicesAvailable(this)) {
                //
                //            locationEnable();
                //
                //        } else {
                //            showErrorDialog();
                //
                //        }
                //    }
                //
                //
                //    @Override
                //    protected void onNewIntent(Intent intent) {
                //        super.onNewIntent(intent);
                //
                //        onCreateonNewIntent();
                //
                //    }
                //
                //
                //    public void checkInternal() {
                //
                //        if (getApp().isStaticReached()) {
                //
                //            isReached = getApp().isReached();
                //            updateUi(getApp().isReached());
                //
                //
                //        } else {
                //            Single.fromCallable(this::loadInBackground)
                //                    .subscribeOn(Schedulers.io())
                //                    .observeOn(AndroidSchedulers.mainThread())
                //                    .subscribe(this::updateUi, this::Error);
                //        }
                //    }
                //
                //    private void Error(Throwable throwable) {
                //
                //        hidePD();
                //        showToast(getString(R.string.someerror));
                //    }
                //
                //
                //    //1253
                //    private void getPlaceIDByPlaceName(String placeName) {
                //        try {
                //
                //            showPD();
                //            IpAddress.e("PLACENAME", placeName + " empty");
                //            if (placeName != null && placeName.trim().length() > 0) {
                //                SoapGetPlaceIDRequestEnvelope
                //                        soapSendEvaciationPushRequestEnvelope = new SoapGetPlaceIDRequestEnvelope();
                //
                //                GetPlaceIDRequestBody getPlaceIDRequestBody = new
                //                        GetPlaceIDRequestBody();
                //
                //
                //                GetPlaceIDRequest getPlaceIDRequest = new GetPlaceIDRequest();
                //
                //
                //                getPlaceIDRequest.setA_sClientId(getClientID());
                //                getPlaceIDRequest.setAuthToken(getAuthTokenPref_Evacuation());
                //                getPlaceIDRequest.setA_sPlaceName(placeName);
                //
                //
                //                getPlaceIDRequestBody.setGetPlaceID(getPlaceIDRequest);
                //
                //                soapSendEvaciationPushRequestEnvelope.setBody(getPlaceIDRequestBody);
                //
                //                Observable<SoapGetPlaceIDResponseEnvelope> lClientDetailsObservable =
                //                        service.getPlaceID(soapSendEvaciationPushRequestEnvelope);
                //
                //                lClientDetailsObservable.
                //                        subscribeOn(Schedulers.io())
                //                        .observeOn(AndroidSchedulers.mainThread())
                //                        .subscribe(this::onSuccessPlaceID, this::onErrorPlaceID);
                //            } else {
                //                showToast(getString(R.string.empty_cityname));
                //            }
                //        } catch (Exception e) {
                //
                //
                //            onPlaceID();
                //            hidePD();
                //
                //        }
                //
                //    }
                //
                //    private void onPlaceID() {
                //
                //        if (adapterPosition != -1) {
                //            isTrackingEnable = true;
                //            city_selected = mCitiesList.get(adapterPosition);
                //            city_selected.setSelect(true);
                //            storeSelectedCity(city_selected);
                //            storeAllCity(mCitiesList);
                //            adapterPosition = -1;
                //            addDeviceToken();
                //
                //
                //            if (getIntent().hasExtra(Constants.ACTIVITY_MAIN)) {
                //                onright();
                //            } else
                //                notifyDataAdapter();
                //        }
                //    }
                //
                //    private void onErrorPlaceID(Throwable throwable) {
                //
                //        hidePD();
                //        onPlaceID();
                //    }
                //
                //    private void onSuccessPlaceID(SoapGetPlaceIDResponseEnvelope soapGetPlaceIDResponseEnvelope) {
                //
                //
                //        if (soapGetPlaceIDResponseEnvelope != null) {
                //
                //            if (soapGetPlaceIDResponseEnvelope.getBody() != null) {
                //                GetPlaceIDResult getPlaceIDResult = soapGetPlaceIDResponseEnvelope.getBody()
                //                        .getGetPlaceIDResponse().getGetPlaceIDResult();
                //
                //                if (getPlaceIDResult != null) {
                //
                //                    if (getPlaceIDResult.getGetPlaceID() != null) {
                //
                //                        GetPlaceID getPlaceID = getPlaceIDResult.getGetPlaceID();
                //
                //                        if (getPlaceID.getStatus() != null
                //                                && getPlaceID.getStatus().equalsIgnoreCase(IpAddress.SUCCESS)) {
                //
                //
                //                            mSharedPreferences.edit().putString(IpAddress.PLACEID_NEW,
                //                                    getPlaceID.getPlaceID()).apply();
                //                            city_selected.setPlaceID(getPlaceID.getPlaceID());
                //
                //                            storeSelectedCity(city_selected);
                //
                //                        } else {
                //                            storeSelectedCity(city_selected);
                //                            mSharedPreferences.edit().putString(IpAddress.PLACEID_NEW, null).apply();
                //                        }
                //                    } else {
                //                        storeSelectedCity(city_selected);
                //                        mSharedPreferences.edit().putString(IpAddress.PLACEID_NEW, null).apply();
                //                    }
                //                } else {
                //                    storeSelectedCity(city_selected);
                //                    mSharedPreferences.edit().putString(IpAddress.PLACEID_NEW, null).apply();
                //                }
                //            }
                //        }
                //
                //
                //        onPlaceID();
                //        hidePD();
                //    }
                //
                //
                //    public void updateUi(boolean isReached) {
                //
                //
                //        if (isReached) {
                //            //get locations
                //            //ip connect avuthe
                //            getCountries();
                //
                //        } else {
                //            getPlaces();
                //        }
                //    }
                //
                //
                //    private void addDeviceToken() {
                //
                //        Intent service = new Intent(this, SendDeviceTokenService.class);
                //        SendDeviceTokenService.enqueWork(getApplicationContext(), service);
                //
                //    }
                //
                //    private void getPlaces() {
                //
                //        showPD();
                //        setOKHTTPAfterLogin();
                //        setRetrofit(Constants.IPADDRESS2);
                //        setService();
                //        SoapGetPlacesRequestEnvelope soapGetPlacesRequestEnvelope = new SoapGetPlacesRequestEnvelope();
                //        GetPlaces getPlaces = new GetPlaces();
                //
                //        getPlaces.setAuthToken(getAuthTokenPref_Evacuation());
                //        getPlaces.setA_sClientId(getClientID());
                //
                //
                //        GetPlacesRequestBody getPlacesRequestBody = new GetPlacesRequestBody();
                //        getPlacesRequestBody.setGetPlaces(getPlaces);
                //
                //        soapGetPlacesRequestEnvelope.setBody(getPlacesRequestBody);
                //
                //
                //        Observable<SoapGetPlacesResponseEnvelope> lClientDetailsObservable =
                //                service.getPlaces(soapGetPlacesRequestEnvelope);
                //
                //
                //        lClientDetailsObservable.
                //                subscribeOn(Schedulers.newThread())
                //                .observeOn(AndroidSchedulers.mainThread())
                //                .subscribe(this::onGetPlaces, this::onError);
                //    }
                //
                //    private void onGetPlaces(SoapGetPlacesResponseEnvelope soapGetPlacesResponseEnvelope) {
                //
                //
                //        if (soapGetPlacesResponseEnvelope != null) {
                //
                //
                //            if (soapGetPlacesResponseEnvelope.getBody() != null) {
                //
                //                List<PlaceModel> placeModels = soapGetPlacesResponseEnvelope.getBody()
                //                        .getGetPlacesResponse().getGetPlacesResult()
                //                        .getPlaces().get(0).getPlaceModelList();
                //
                //                mCityListOld.clear();
                //
                //                if (placeModels != null && placeModels.size() > 0) {
                //
                //                    mCitiesList.clear();
                //                    for (PlaceModel placeModel : placeModels) {
                //                        City city = new City();
                //                        city.setCityName(placeModel.getName());
                //                        city.setPlaceID(placeModel.getPlaceID());
                //                        mCityListOld.add(city);
                //                    }
                //                }
                //
                //
                //                if (cities_local != null && cities_local.size() > 0) {
                //
                //                    for (City cityOLD : cities_local) {
                //
                //                        if (!mCitiesList.contains(cityOLD)) {
                //
                //                            mCitiesList.add(cityOLD);
                //                        }
                //                    }
                //                    //  mCitiesList.addAll(cities_local);
                //                }
                //                if (mCityListOld != null && mCityListOld.size() > 0
                //                        && mCitiesList != null && mCitiesList.size() > 0) {
                //                    for (City cityOLD : mCityListOld) {
                //
                //                        if (!mCitiesList.contains(cityOLD)) {
                //
                //                            mCitiesList.add(cityOLD);
                //                        }
                //                    }
                //                } else {
                //                    mCitiesList.addAll(mCityListOld);
                //                }
                //
                //                if (city_selected != null
                //                        && city_selected.isLocal() && !mCitiesList.contains(city_selected))
                //                    mCitiesList.add(city_selected);
                //
                //
                //                notifyDataAdapter();
                //
                //
                //            }
                //
                //        }
                //        hidePD();
                //    }
                //
                //
                //    @OnClick(R.id.tvcurrentloc)
                //    public void onCurrentLoc(View v) {
                //
                //
                //        showPD();
                //        getCurrentAddress = true;
                //        checkLocationPermission();
                //
                //
                //    }
                //
                //
                //    public void onCurrentAddress() {
                //
                //
                //        if (getCurrentAddress && mLocation_my != null && mLocation_my.getAccuracy() > 0) {
                //
                //            getLocationFromAddress_LATLANG(mLocation_my.getLatitude(),
                //                    mLocation_my.getLongitude());
                //        } else {
                //            showToast(getString(R.string.nolocation));
                //        }
                //
                //    }
                //
                //    public void placeEnable() {
                //
                //
                //        try {
                //            place_autocomplete_fragment=(AutocompleteSupportFragment)getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
                //            place_autocomplete_fragment.setPlaceFields(placeFields);
                ////        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                ////                .setCountry("Norway")
                ////                .build();
                ////
                ////        autocompleteFragment.setFilter(typeFilter);
                //
                //            place_autocomplete_fragment.setHint(IpAddress.SEARCHLOC);
                //
                //
                //            View clearBtn = place_autocomplete_fragment.getView()
                //                    .findViewById(com.google.android.libraries.places.R.id.places_autocomplete_clear_button);
                //
                //            clearBtn.setOnClickListener(v -> {
                //                place_autocomplete_fragment.setText("");
                //                clearBtn.setVisibility(View.GONE);
                //            });
                //
                //
                //            AppCompatEditText editText = place_autocomplete_fragment.getView()
                //                    .findViewById(com.google.android.libraries.places.R.id.places_autocomplete_search_input);
                //
                //
                //            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                //                @Override
                //                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //
                //                    switch (actionId) {
                //
                //                        case EditorInfo.IME_ACTION_SEARCH:
                //
                //                            getLocationFromAddress2(v.getText().toString());
                //                            return true;
                //                    }
                //
                //                    return false;
                //                }
                //            });
                //        } catch (Exception e) {
                //
                //            IpAddress.e("errr", e.toString());
                //        }
                //
                //// Construct a request object, passing the place ID and fields array.
                //
                //        place_autocomplete_fragment
                //                .setOnPlaceSelectedListener(new PlaceSelectionListener() {
                //
                //                    @Override
                //                    public void onPlaceSelected(@NonNull com.google.android.libraries.places.api.model.Place place) {
                //                        IpAddress.e(TAG, "Place: " + place.getName() +
                //                                place.getAddress());
                //// Get the address from the Place object
                //                        String address = place.getAddress();
                //
                //// Use Geocoder to get latitude and longitude from the address
                //                        Geocoder geocoder = new Geocoder(LocationActivity.this);
                //                        List<Address> addresses = null;
                //                        try {
                //                            addresses = geocoder.getFromLocationName(address, 1);
                //                        } catch (IOException e) {
                //                            throw new RuntimeException(e);
                //                        }
                //
                //                        if (addresses != null && addresses.size() > 0) {
                //                            double latitude = addresses.get(0).getLatitude();
                //                            double longitude = addresses.get(0).getLongitude();
                //
                //                            // Now you can use latitude and longitude values as needed
                //                            showDialogwithAddress(place, latitude, longitude);
                //                        } else {
                //                            // Handle case where latitude and longitude couldn't be obtained
                //                            // For example, display an error message or log a warning
                //                            IpAddress.e(TAG, "Unable to get latitude and longitude for the address: " + address);
                //                        }
                //                    }
                //
                //                    @Override
                //                    public void onError(Status status) {
                //                        try {
                //                            showToast(status.getStatusMessage());
                //                        } catch (Exception e) {
                //
                //                        }
                //                    }
                //                });
                //    }
                //
                //    private void showDialogwithAddress(com.google.android.libraries.places.api.model.Place place,Double latitude, Double longitude) {
                //        AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
                //
                //        View view = getLayoutInflater().inflate(R.layout.inflate_dialog, null);
                //
                //        TextView tvlocation = view.findViewById(R.id.tvlocation);
                //        tvlocation.setText(place.getName() + " " + place.getAddress());
                //        Button btcancel = view.findViewById(R.id.btcancel);
                //        Button btok = view.findViewById(R.id.btok);
                //        EditText etidentity = view.findViewById(R.id.etidentity);
                //
                //        btok.setOnClickListener(new View.OnClickListener() {
                //            @Override
                //            public void onClick(View view) {
                //
                //
                //                if (etidentity.getText().toString().trim().length() > 0) {
                //
                //                    if (place_autocomplete_fragment != null) {
                //                        place_autocomplete_fragment.setText("");
                //                    }
                //
                //                    if (alertDialog != null &&
                //                            alertDialog.isShowing()) {
                //                        alertDialog.dismiss();
                //                    }
                //                    hideKeyboard(etidentity);
                //
                //                    addCity(place.getName().toString(),
                //                            place.getName().toString() + " " +
                //                                    place.getAddress().toString(), true, true,
                //                            "0", "0", "False", "True",
                //                            etidentity.getText().toString().trim(), "",
                //                            latitude, longitude);
                //
                //                    if (googleMap != null) {
                //
                //                        // pos_desti = place.getLatLng();
                //                        mLocation_desti = new Location("desti");
                //                        mLocation_desti.setLatitude(latitude);
                //                        mLocation_desti.setLongitude(longitude);
                //                        storeLatLng(mLocation_desti.getLatitude(), mLocation_desti.getLongitude());
                //                        mSharedPreferences.edit().putString(IpAddress.PLACEID_NEW, null).apply();
                //                        onright();
                //                    }
                //
                //                } else {
                //                    etidentity.setError("Enter location identity");
                //                }
                //            }
                //        });
                //
                //        btcancel.setOnClickListener(new View.OnClickListener() {
                //            @Override
                //            public void onClick(View view) {
                //
                //
                //                if (alertDialog != null &&
                //                        alertDialog.isShowing()) {
                //
                //                    if (place_autocomplete_fragment != null) {
                //                        place_autocomplete_fragment.setText("");
                //                    }
                //                    alertDialog.dismiss();
                //
                //
                //                }
                //            }
                //        });
                //
                //        builder.setView(view);
                //        alertDialog = builder.show();
                //
                //    }
                //
                //
                //    private void getCountries() {
                //
                //        showPD();
                //        setOKHTTPAfterLogin();
                //        setRetrofit(getApp().getTcpIp());
                //        mainApi = retrofit.create(MainApi.class);
                //        setService();
                //        SoapCitiesRequestEnvelope soapCitiesRequestEnvelope = new SoapCitiesRequestEnvelope();
                //        GetLocationsRequest getLocationsRequest = new GetLocationsRequest();
                //        getLocationsRequest.setAuthToken(getAuthTokenPref_Mobile());
                //        CitiesRequestBody citiesRequestBody = new CitiesRequestBody();
                //        citiesRequestBody.setGetLocationsRequest(getLocationsRequest);
                //
                //        soapCitiesRequestEnvelope.setBody(citiesRequestBody);
                //        Observable<SoapCitiesResponseEnvelope> lClientDetailsObservable =
                //                mainApi.getLocations(soapCitiesRequestEnvelope);
                //        lClientDetailsObservable.
                //                subscribeOn(Schedulers.newThread())
                //                .observeOn(AndroidSchedulers.mainThread())
                //                .subscribe(this::onCities, this::onLocationError);
                //    }
                //
                //    private void onLocationError(Throwable throwable) {
                //
                //        notifyDataAdapter();
                //        hidePD();
                //    }
                //
                //
                //    public void onCities(SoapCitiesResponseEnvelope soapCitiesResponseEnvelope) {
                //
                //        CitiesResponseBody citiesResponseBody = soapCitiesResponseEnvelope.getBody();
                //        Cities cities = citiesResponseBody.getGetLocationsResponse()
                //                .getGetLocationsResult().getCities();
                //        mCityListOld.clear();
                //        if (cities != null) {
                //
                //            addCities(cities.getCity());
                //        } else {
                //            Constants.noData(1);
                //        }
                //
                //        hidePD();
                //    }
                //
                //    private void addCities(List<City> city) {
                //
                //        mCitiesList.clear();
                //        mCityListOld.addAll(city);
                //
                ////        if (cities_local != null && cities_local.size() > 0) {
                ////            mCitiesList.addAll(cities_local);
                ////        }
                //        if (cities_local != null && cities_local.size() > 0) {
                //
                //            for (City cityOLD : cities_local) {
                //
                //                if (!mCitiesList.contains(cityOLD)) {
                //
                //                    mCitiesList.add(cityOLD);
                //                }
                //            }
                //            //  mCitiesList.addAll(cities_local);
                //        }
                //        if (mCityListOld != null && mCityListOld.size() > 0
                //                && mCitiesList != null && mCitiesList.size() > 0) {
                //            for (City cityOLD : mCityListOld) {
                //
                //                if (!mCitiesList.contains(cityOLD)) {
                //
                //                    mCitiesList.add(cityOLD);
                //                }
                //            }
                //        } else {
                //            mCitiesList.addAll(mCityListOld);
                //        }
                //        if (city_selected != null && city_selected.isLocal() && !mCitiesList.contains(city_selected))
                //            mCitiesList.add(city_selected);
                //
                //        notifyDataAdapter();
                //    }
                //
                //
                //    @OnClick(R.id.ivdirection)
                //    public void onDirection(View v) {
                //
                //        if (mLocation_my != null && mLocation_desti != null) {
                //            directionIntent(mLocation_my.getLatitude(), mLocation_my.getLongitude(),
                //                    mLocation_desti.getLatitude(), mLocation_desti.getLongitude());
                //        } else {
                //
                //        }
                //    }
                //
                //
                //    public void directionIntent(double slat, double slan, double dlat, double dlan) {
                //        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                //                Uri.parse("http://maps.google.com/maps?saddr=" + slat + "," + slan + "&daddr=" +
                //                        dlat + "," + dlan));
                //        startActivity(intent);
                //    }
                //
                ////    @OnClick(R.id.ivright)
                ////    public void onRight(View v) {
                ////
                ////        onright();
                ////
                ////    }
                ////
                ////    private void onright() {
                ////
                ////        try {
                ////
                ////            if (isReached) {
                ////                if (getIntent().getBooleanExtra(Constants.BACK, false)) {
                ////
                ////                    Intent i = new Intent(this, HomeActivity.class);
                ////                    if (city_selected != null) {
                ////                        i.putExtra(Constants.SKIPROOM, city_selected.getSkipRoom());
                ////                    }
                ////
                ////                    i.putExtra(IpAddress.SKIPLOCATION, skipLocation);
                ////                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ////                    startActivity(i);
                ////                    finish();
                ////
                ////                } else {
                ////
                ////                    Intent intent = new Intent();
                ////                    intent.putExtra(IpAddress.PLACEID_NEW, true);
                ////                    setResult(RESULT_OK, intent);
                ////                    finish();
                ////                }
                ////
                ////            } else if (city_selected != null) {
                ////
                ////                if (getIntent().hasExtra(Constants.HOME)
                ////                        || getIntent().hasExtra(Constants.SKIPROOMS)) {
                ////
                ////                    onBackPressed();
                ////                } else {
                ////
                ////                    Intent i = new Intent(this, HomeActivity.class);
                ////                    i.putExtra(Constants.SKIPROOM, city_selected.getSkipRoom());
                ////                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                ////                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ////
                ////                    i.putExtra(IpAddress.SKIPLOCATION, skipLocation);
                ////                    startActivity(i);
                ////
                ////                    finish();
                ////                }
                ////            } else {
                ////                Toast.makeText(this,
                ////                        "Please select any location to proceed....", Toast.LENGTH_SHORT).show();
                ////            }
                ////        } catch (Exception e) {
                ////
                ////            IpAddress.e("err", e.toString());
                ////        }
                ////    }
                //
                ////    @OnClick(R.id.ivleft)
                ////    public void onLeft(View v) {
                ////
                ////        onBackPressed();
                ////    }
                //
                ////    @Override
                ////    public void onBackPressed() {
                ////
                ////
                ////        if (getIntent().hasExtra(Constants.ACTIVITY_MAIN)) {
                ////
                ////
                ////        } else if (isReached || city_selected != null) {
                ////
                ////            Intent intent = getIntent();
                ////            intent.putExtra(IpAddress.SKIPLOCATION, skipLocation);
                ////            intent.putExtra(IpAddress.PLACEID_NEW, true);
                ////            setResult(RESULT_OK, intent);
                ////            finish();
                ////        } else {
                ////            Toast.makeText(this, "Please select any location to proceed....", Toast.LENGTH_SHORT).show();
                ////
                ////        }
                ////
                ////
                ////    }
                //
                //    @Override
                //    public void onBackPressed() {
                //
                //        storeAllCity(mCitiesList);
                //        hideSoft();
                //        if (getIntent().hasExtra(Constants.SKIPROOMS)) {
                //
                //            setResult(RESULT_OK);
                //            finish();
                //
                //        } else if (getIntent().hasExtra(Constants.ACTIVITY_MAIN)) {
                //
                //
                //        } else if (city_selected != null && city_selected.isSelect()) {
                //
                //            Intent intent = getIntent();
                //            intent.putExtra(IpAddress.SKIPLOCATION, skipLocation);
                //            setResult(RESULT_OK, intent);
                //            finish();
                //        } else {
                //            Toast.makeText(this, "Please select any location to proceed....", Toast.LENGTH_SHORT).show();
                //
                //        }
                //
                //
                //    }
                //
                //
                //    @OnClick(R.id.ivleft)
                //    public void onLeft(View v) {
                //
                //        onBackPressed();
                //    }
                //
                ////    @Override
                ////    public void onBackPressed() {
                ////
                ////
                ////        if (getIntent().hasExtra(Constants.BACK)) {
                ////
                ////
                ////        } else if (city_selected != null) {
                ////
                ////            Intent intent = new Intent(this, HomeActivity.class);
                ////            intent.putExtra(IpAddress.SKIPLOCATION, skipLocation);
                ////            setResult(RESULT_OK, intent);
                ////            finish();
                ////        } else {
                ////            Toast.makeText(this, "Please select any location to proceed....", Toast.LENGTH_SHORT).show();
                ////
                ////        }
                ////
                ////
                ////    }
                //
                //
                //    @OnClick({R.id.ivright, R.id.tvnext})
                //    public void onRight(View v) {
                //
                //        onright();
                //
                //    }
                //
                //    private void onright() {
                //
                //        storeAllCity(mCitiesList);
                //        hideSoft();
                //        String source = getIntent().getStringExtra("NewLoc");
                //        // Handle navigation based on source
                //
                //        if (getIntent().hasExtra(Constants.SKIPROOMS)) {
                //
                //            setResult(RESULT_OK);
                //            finish();
                //
                //        }
                //        else if(source != null && source.equalsIgnoreCase("EditBook")){
                //            onBackPressed();
                //        }
                //        else if(source != null && source.equalsIgnoreCase("ConfirmBook")){
                //            City city = getStoreCitySelect();
                //            onBackPressed();
                //        }
                //
                //        else if (city_selected != null && city_selected.isSelect()) {
                //
                //            Intent i = new Intent(this, HomeActivity.class);
                //            i.putExtra(Constants.SKIPROOM, city_selected.getSkipRoom());
                //            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //            i.putExtra(IpAddress.SKIPLOCATION, skipLocation);
                //            startActivity(i);
                //            finish();
                //
                //        }
                //        else {
                //            Toast.makeText(this,
                //                    "Please select any location to proceed....", Toast.LENGTH_SHORT).show();
                //        }
                //
                //
                //    }
                //
                //    private void navigateFromSource(String source) {
                //        Intent intent = new Intent();
                //
                //        switch (source) {
                //            case "EditBook":
                //                intent = new Intent(this, BookMeetingsActivity.class);
                //                break;
                //            case "ConfirmBook":
                //                intent = new Intent(this, ConfirmationActivity.class);
                //                break;
                //            default:
                //                // Handle default case or error condition
                //                break;
                //        }
                //
                //        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //        startActivity(intent);
                //        finish();
                //    }
                //
                //
                //    public String onLocationMap(String response) {
                //
                //        String  formatted = "";
                //        try {
                //            JSONObject jsonObject = new JSONObject(response);
                //
                //            String status_address = jsonObject.getString("status");
                //            if (status_address.equalsIgnoreCase("OK")) {
                //
                //                JSONArray jsonArray = jsonObject.getJSONArray("results");
                //
                //                if (jsonArray.length() > 0) {
                //
                //                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                //
                //                    JSONObject location = jsonObject1.getJSONObject("geometry")
                //                            .getJSONObject("location");
                //
                //                    formatted = jsonObject1.getString("formatted_address");
                //                    IpAddress.e("format", formatted);
                //
                //
                //                }
                //            }
                //        } catch (Exception e) {
                //
                //            IpAddress.e("format", formatted + e.toString());
                //            setBounds(true);
                //        }
                //        return formatted;
                //
                //
                //    }
                //
                //    public void onLocationFound(String formatted) {
                //
                //        this.formatted=formatted;
                //        IpAddress.e("DIRECTION", "onLocationFound");
                //        if (getCurrentAddress && formatted != null && mLocation_my != null
                //                && mLocation_my.getAccuracy() > 0
                //                && formatted.length() > 0) {
                //
                //
                //            getCurrentAddress = false;
                //            AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
                //            View view = getLayoutInflater().inflate(R.layout.inflate_dialog, null);
                //            TextView tvlocation = view.findViewById(R.id.tvlocation);
                //            tvlocation.setText(formatted);
                //            Button btcancel = view.findViewById(R.id.btcancel);
                //            Button btok = view.findViewById(R.id.btok);
                //            EditText etidentity = view.findViewById(R.id.etidentity);
                //
                //            btok.setOnClickListener(view1 -> {
                //
                //
                //                if (etidentity.getText().toString().trim().length() > 0) {
                //
                //                    hideKeyboard(etidentity);
                //
                //                    if (alertDialog != null && alertDialog.isShowing()) {
                //                        alertDialog.getWindow().setSoftInputMode
                //                                (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                //                        alertDialog.dismiss();
                //                    }
                //                    storeLatLng(mLocation_my.getLatitude(), mLocation_my.getLongitude());
                //                    addCity(formatted,
                //                            formatted, true, true,
                //                            "0", "0", "False", "True",
                //                            etidentity.getText().toString().trim(), "",
                //                            mLocation_my.getLatitude(), mLocation_my.getLongitude());
                //
                //                    mSharedPreferences.edit().putString(IpAddress.PLACEID_NEW, null).apply();
                //
                //                    if (googleMap != null) {
                //
                //                        afterLocation();
                //                        onright();
                //
                //                    }
                //                } else {
                //                    etidentity.setError("Enter location identity");
                //                }
                //            });
                //
                //            btcancel.setOnClickListener(view12 -> {
                //
                //
                //                if (alertDialog != null &&
                //                        alertDialog.isShowing()) {
                //                    alertDialog.dismiss();
                //                }
                //            });
                //
                //            builder.setView(view);
                //            alertDialog = builder.show();
                //
                //        } else {
                //            showToast(getString(R.string.nolocation));
                //        }
                //        hidePD();
                //    }
                //
                //
                //    public void onLocationErr(Throwable throwable) {
                //
                //        IpAddress.e("DIRECTION", "onLocationErr");
                //        showToast(throwable.toString());
                //        hidePD();
                //    }
                //
                //    public void getCurrentLocationFromLATLANG(double lati, double longi) {
                //
                //
                //
                //        MapDirection mapDirection = provideRetrofit(Constants.MAP_URL)
                //                .create(MapDirection.class);
                //
                //        Observable<String>
                //                piingoNotify =
                //                mapDirection.piingAddressByLocation(
                //                        lati + "," + longi
                //                        , getString(R.string.apifor));
                //        piingoNotify.subscribeOn
                //                        (Schedulers.newThread())
                //                .map(this::onLocationMap)
                //                .observeOn(AndroidSchedulers.mainThread())
                //                .subscribe(this::onCurrentLocationFound, this::onCurrentLocationErr);
                //
                //    }
                //
                //    private void onCurrentLocationErr(Throwable throwable) {
                //
                //        hidePD();
                //        setBounds(true);
                //    }
                //
                //    private void onCurrentLocationFound(String s) {
                //
                //        hidePD();
                //
                //        IpAddress.e("formatted2",s);
                //
                //        if(marker_myloc!=null)
                //        {
                //            marker_myloc.setTitle("Current Address");
                //            marker_myloc.setSnippet(s);
                //        }
                //
                //        if(isDirectionFound)
                //        {
                //
                //        }else
                //        {
                //            setBounds(true);
                //        }
                //
                //
                //    }
                //
                //    public void getLocationFromAddress_LATLANG(double lati, double longi) {
                //
                //
                //        MapDirection mapDirection = provideRetrofit(Constants.MAP_URL)
                //                .create(MapDirection.class);
                //
                //        Observable<String>
                //                piingoNotify =
                //                mapDirection.piingAddressByLocation(
                //                        lati + "," + longi
                //                        , getString(R.string.apifor));
                //        piingoNotify.subscribeOn
                //                        (Schedulers.newThread())
                //                .map(this::onLocationMap)
                //                .observeOn(AndroidSchedulers.mainThread())
                //                .subscribe(this::onLocationFound, this::onLocationErr);
                //
                //    }
                //
                //
                //    public Location onAddressMap(String response) {
                //        try {
                //            JSONObject jsonObject = new JSONObject(response);
                //            String status_address = jsonObject.getString("status");
                //
                //            if (status_address.equalsIgnoreCase("OK")) {
                //
                //                JSONArray jsonArray = jsonObject.getJSONArray("results");
                //
                //                if (jsonArray.length() > 0) {
                //
                //                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                //
                //                    JSONObject loc = jsonObject1.getJSONObject("geometry")
                //                            .getJSONObject("location");
                //
                //                    mLocation_desti = new Location("desti");
                //                    mLocation_desti.setLongitude(loc.getDouble("lng"));
                //                    mLocation_desti.setLatitude(loc.getDouble("lat"));
                //
                //                    if (city_selected != null && city_selected.isSelect()) {
                //                        city_selected.setLatitude(mLocation_desti.getLatitude());
                //                        city_selected.setLongitude(mLocation_desti.getLongitude());
                //                    }
                //
                //                    storeLatLng(mLocation_desti.getLatitude(), mLocation_desti.getLongitude());
                //
                //                }
                //
                //                return createNewLocation(mLocation_desti.getLongitude(), mLocation_desti.getLatitude());
                //            }
                //
                //
                //        } catch (JSONException e) {
                //
                //        }
                //
                //        return createNewLocation(0, 0);
                //
                //
                //    }
                //
                //    public void onAddressFound(Location location) {
                //
                //
                //        mLocation_desti = location;
                //        if (location != null && location.getAccuracy() > 0) {
                //
                //
                //            storeLatLng(location.getLatitude(),
                //                    location.getLongitude());
                //
                //        }
                //        IpAddress.e("DIRECTION", "onAddressFound");
                //        afterLocation();
                //    }
                //
                //    public void onAddressError(Throwable throwable) {
                //
                //        IpAddress.e("DIRECTION", "onAddressError");
                //        afterLocation();
                //        hidePD();
                //    }
                //
                //
                //    public void getLocationFromAddress2(String strAddress) {
                //
                //
                //        try {
                //
                //            MapDirection mapDirection = provideRetrofit(Constants.MAP_URL)
                //                    .create(MapDirection.class);
                //
                //            Observable<String>
                //                    piingoNotify =
                //                    mapDirection.piinggetLocationByNameorCityName(strAddress, getString(R.string.apifor));
                //            piingoNotify.subscribeOn
                //                            (Schedulers.newThread())
                //                    .map(this::onAddressMap)
                //                    .observeOn(AndroidSchedulers.mainThread())
                //                    .subscribe(this::onAddressFound, this::onAddressError);
                //
                //
                //        } catch (Exception ex) {
                //
                //            Constants.e("errrr", ex.toString());
                //        }
                //    }
                //
                //    private void storeLatLng(double latitude, double longitude) {
                //
                //        IpAddress.e("latlong", latitude + " " + longitude + "");
                //
                //        mSharedPreferences.edit().putString(Constants.LAT, String.valueOf(latitude)).apply();
                //        mSharedPreferences.edit().putString(Constants.LON, String.valueOf(longitude)).apply();
                //        if (city_selected != null) {
                //
                //            if (city_selected.getCityName() != null) {
                //
                //                locationHashMap.put(city_selected.getCityName(),
                //                        createNewLocation(latitude, longitude));
                //            }
                //
                //            IpAddress.e("hashmap", locationHashMap.toString());
                //
                //            city_selected.setLatitude(latitude);
                //            city_selected.setLongitude(longitude);
                //            storeSelectedCity(city_selected);
                //        }
                //    }
                //
                //
                //    private void showErrorDialog() {
                //
                //        mGoogleApiAvailability = GoogleApiAvailability.getInstance();
                //        i = mGoogleApiAvailability.isGooglePlayServicesAvailable(getApplicationContext());
                //        if (i == ConnectionResult.SUCCESS) {
                //
                //            locationEnable();
                //
                //        } else if (mGoogleApiAvailability.isUserResolvableError(i)) {
                //
                //
                //            mGoogleApiAvailability.getErrorDialog(this, i,
                //                    REQUEST_GOOGLE_PLAY_SERVICES, new DialogInterface.OnCancelListener() {
                //                        @Override
                //                        public void onCancel(DialogInterface dialogInterface) {
                //
                //
                //                            showErrorDialog();
                //
                //                        }
                //                    }).show();
                //
                //        } else {
                //
                //            hidePD();
                //            showToast(mGoogleApiAvailability.getErrorString(REQUEST_GOOGLE_PLAY_SERVICES));
                //        }
                //    }
                //
                //
                //    @Override
                //    protected void onStart() {
                //        super.onStart();
                //
                //    }
                //
                //    Location createNewLocation(double longitude, double latitude) {
                //
                //
                //        Location location = new Location(IpAddress.VIPRECEPTION);
                //        location.setLongitude(longitude);
                //        location.setLatitude(latitude);
                //        return location;
                //    }
                //
                //    @Override
                //    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                //
                //        IpAddress.e("req", requestCode + " " + resultCode + " ");
                //
                //        String address = null;
                //
                //// Ensure the AutocompleteSupportFragment is not null
                //        if (place_autocomplete_fragment != null) {
                //            // Get the fragment's view
                //            View fragmentView = place_autocomplete_fragment.getView();
                //
                //            // Ensure the fragment's view is not null
                //            if (fragmentView != null) {
                //                // Find the EditText by its ID
                //                EditText editText = fragmentView.findViewById(R.id.places_autocomplete_search_input);
                //
                //                // Ensure the EditText is not null
                //                if (editText != null) {
                //                    // Retrieve the text from the EditText
                //                    address = editText.getText().toString().trim();
                //                } else {
                //                    // EditText is null
                //                    Log.e(TAG, "EditText is null");
                //                }
                //            } else {
                //                // Fragment's view is null
                //                Log.e(TAG, "Fragment's view is null");
                //            }
                //        } else {
                //            // AutocompleteSupportFragment is null
                //            Log.e(TAG, "AutocompleteSupportFragment is null");
                //        }
                //
                //// Check if the address is not null and not empty
                //        if (address != null && !address.isEmpty()) {
                //            getLocationFromAddress2(address);
                //        } else {
                //            // Address is null or empty
                //            Log.e(TAG, "Address is null or empty");
                //        }
                //
                //        switch (requestCode) {
                //
                //
                //            case REQUEST_GOOGLE_PLAY_SERVICES:
                //                if (resultCode == Activity.RESULT_OK) {
                //
                //                    locationEnable();
                //
                //                }
                //                break;
                //            case REQUEST_CHECK_SETTINGS:
                //                LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
                //                switch (resultCode) {
                //                    case Activity.RESULT_OK:
                //                        IpAddress.e(TAG, "User agreed to make required location settings changes.");
                //                        getLastKnownLocation();
                //                        break;
                //                    case Activity.RESULT_CANCELED:
                //                        IpAddress.e(TAG, "User chose not to make required location settings changes.");
                //                        break;
                //                }
                //                break;
                //
                //            default:
                //                super.onActivityResult(requestCode, resultCode, data);
                //                break;
                //        }
                //    }
                //
                //    private void addCity(String name, String address, boolean isLocal, boolean isSelect, String cityCode,
                //                         String roomCount, String useCatering, String skipRoom,
                //                         String etIdentity, String mapUrl, double latitude, double longitude) {
                //
                //        // Deselect all other cities
                //        for (City city : mCitiesList) {
                //            city.setSelect(false);
                //        }
                //
                //        // Create a new city object
                //        City newCity = new City();
                //        newCity.setAddress(address);
                //        newCity.setCityCode(cityCode);
                //        newCity.setCityName(name);
                //        newCity.setLocal(isLocal);
                //        newCity.setSelect(true); // Set the new city as selected
                //        newCity.setRoomCount(roomCount);
                //        newCity.setSkipRoom(skipRoom);
                //        newCity.setUseCatering(useCatering);
                //        newCity.setCityMapUrl(mapUrl);
                //        newCity.setIdentity(etIdentity);
                //        newCity.setLongitude(longitude);
                //        newCity.setLatitude(latitude);
                //
                //        // Deselect the previously selected city if it exists
                //        if (city_selected != null) {
                //            city_selected.setSelect(false);
                //        }
                //
                //        // Add the new city to the list
                //        mCitiesList.add(0, newCity); // Add to the beginning of the list
                //
                //        // Store the selected city (the newly added one)
                //        city_selected = newCity;
                //        storeSelectedCity(city_selected);
                //        // Store all cities
                //        storeAllCity(mCitiesList);
                //
                //        // Notify the adapter of the data change
                //        notifyDataAdapter();
                //    }
                //
                //    private void notifyDataAdapter() {
                //
                //        if (mCitiesList != null && mCitiesList.size() > 0) {
                //
                //            rvlocation.setVisibility(View.VISIBLE);
                //        } else {
                //            rvlocation.setVisibility(View.GONE);
                //        }
                //        mLocationAdapter.notifyDataSetChanged();
                //        if (city_selected != null && city_selected.isSelect()) {
                //
                //            if(city_selected.getAddress()!=null)
                //
                //                getLocationFromAddress2(city_selected.getAddress());
                //
                //            else
                //                getLocationFromAddress2(city_selected.getCityName());
                //
                //        }
                //
                //    }
                //
                //
                //    private void locationEnable() {
                //
                //
                //        showPD();
                //        buildGoogleApiClient(connectionCallback, connectionFailed);
                //
                //
                //    }
                //
                //
                //    @Override
                //    protected void onStop() {
                //        if (flag)
                //            removeGoogle();
                //        super.onStop();
                //
                //    }
                //
                //
                //    @Override
                //    protected void onResume() {
                //        super.onResume();
                //        if (flag)
                //            reqGoogle();
                //    }
                //
                //    @Override
                //    protected void onDestroy() {
                //        removeGoogle();
                //        super.onDestroy();
                //
                //    }
                //
                //
                //    private void removeGoogle() {
                //
                //        try {
                //            fusedLocationProviderClient.removeLocationUpdates(locationChange);
                //            if (mGoogleApiClient != null &&
                //                    mGoogleApiClient.isConnected()) {
                //                mGoogleApiClient.disconnect();
                //
                //            }
                //            if (eventBus != null && eventBus.isRegistered(this))
                //                eventBus.unregister(this);
                //
                //        } catch (Exception e) {
                //
                //        }
                //    }
                //
                //
                //    private void checkLocationPermission() {
                //
                //        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                //                Manifest.permission.ACCESS_FINE_LOCATION);
                //        int permissionCheck2 = ContextCompat.checkSelfPermission(getApplicationContext(),
                //                Manifest.permission.ACCESS_COARSE_LOCATION);
                //
                //        if (permissionCheck == PackageManager.PERMISSION_GRANTED &&
                //                permissionCheck2 == PackageManager.PERMISSION_GRANTED) {
                //
                //            checkLocationSettings();
                //
                //        } else {
                //
                //
                //            hidePD();
                //            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                //                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                //
                //
                //                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                //                        android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //
                //                    AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
                //                    builder.setMessage("Please enable location permission");
                //                    builder.setCancelable(false);
                //                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                //                        @Override
                //                        public void onClick(DialogInterface dialogInterface, int i) {
                //
                //                            ActivityCompat.requestPermissions(LocationActivity.this,
                //                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                //                                            Manifest.permission.ACCESS_COARSE_LOCATION},
                //                                    MY_PERMISSIONS_REQUEST_READ_LOCATION);
                //                        }
                //                    });
                //                    builder.show();
                //                } else {
                //
                //                    // No explanation needed, we can request the permission.
                //
                //                    ActivityCompat.requestPermissions(this,
                //                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                //                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                //                            MY_PERMISSIONS_REQUEST_READ_LOCATION);
                //                }
                //            } else {
                //
                //                ActivityCompat.requestPermissions(this,
                //                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                //                                Manifest.permission.ACCESS_COARSE_LOCATION},
                //                        MY_PERMISSIONS_REQUEST_READ_LOCATION);
                //            }
                //        }
                //
                //
                //    }
                //
                //    private void getLastKnownLocation() {
                //
                //        flag = true;
                //        reqGoogle();
                //    }
                //
                //    private void afterLocationFinding() {
                //
                //
                //        showPD();
                //        checkInternal();
                //    }
                //
                //    private void reqGoogle() {
                //
                //        try {
                //
                //
                //            if (mLocation_my != null && mLocation_my.getAccuracy() > 0 && getCurrentAddress) {
                //
                //                onCurrentAddress();
                //
                //            } else if (mGoogleApiClient != null &&
                //                    mGoogleApiClient.isConnected()) {
                //                fusedLocationProviderClient.getLastLocation()
                //                        .addOnCompleteListener(new OnCompleteListener<Location>() {
                //                            @Override
                //                            public void onComplete(@NonNull Task<Location> task) {
                //
                //
                //                                try {
                //
                //                                    mLocation_my = task.getResult(ApiException.class);
                //                                    if (mLocation_my != null) {
                //                                        onLocChange();
                //                                    }
                //                                } catch (ApiException e) {
                //                                    hidePD();
                //                                    e.printStackTrace();
                //                                }
                //
                //                            }
                //                        })
                //                        .addOnFailureListener(new OnFailureListener() {
                //                            @Override
                //                            public void onFailure(@NonNull Exception e) {
                //
                //                                hidePD();
                //                                showToast(e.toString());
                //                            }
                //                        });
                //
                //
                //                fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationChange
                //                        , Looper.getMainLooper());
                //
                //                if (flag) {
                //                    afterLocationFinding();
                //                }
                //
                //
                //            } else {
                //                hidePD();
                //            }
                //
                //
                //        } catch (SecurityException se) {
                //
                //
                //        }
                //    }
                //
                //
                //    //1253
                //    @Subscribe
                //    public void onLocationChanged(Location location) {
                //
                //        mLocation_my = location;
                //        if (mLocation_my == null || mLocation_my.getAccuracy() == 0) {
                //            onLocChange();
                //            getCurrentLocationFromLATLANG(mLocation_my.getLatitude(),mLocation_my.getLongitude());
                //        }
                //    }
                //
                //    private void onLocChange() {
                //
                //
                //        if (googleMap != null && isMapLoded) {
                //
                //            afterLocation();
                //
                //        } else {
                //            mapFragment.getMapAsync(this);
                //
                //        }
                //    }
                //
                //    @Override
                //    public void onMapReady(GoogleMap googleMap) {
                //        if (this.googleMap != null) {
                //            this.googleMap.clear();
                //
                //        }
                //
                //        isMapLoded = true;
                //        this.googleMap = googleMap;
                //        try {
                //            this.googleMap.setBuildingsEnabled(true);
                //            this.googleMap.getUiSettings().setAllGesturesEnabled(true);
                //            this.googleMap.getUiSettings().setMapToolbarEnabled(false);
                //            enableListeners(this.googleMap);
                //            afterLocation();
                //        } catch (SecurityException e) {
                //
                //            Constants.noData(2);
                //        }
                //    }
                //
                //    @Override
                //    public void onRequestPermissionsResult(int requestCode,
                //                                           String permissions[], int[] grantResults) {
                //
                //        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                //        switch (requestCode) {
                //            case MY_PERMISSIONS_REQUEST_READ_LOCATION: {
                //                // If request is cancelled, the result arrays are empty.
                //                if (grantResults.length > 0
                //                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //
                //                    // permission was granted, yay! Do the
                //                    // contacts-related task you need to do.
                //                    skipLocation = false;
                //                    checkLocationSettings();
                //
                //                } else {
                //
                //                    //location permission disabled
                //
                //                    skipLocation = true;
                //                    afterLocationFinding();
                //                }
                //
                //            }
                //            break;
                //            // other 'case' lines to check for other
                //            // permissions this app might request
                //        }
                //    }
                //
                //
                //    private void afterLocation() {
                //
                //        if (googleMap != null) {
                //            if (polyline != null) {
                //                polyline.remove();
                //            }
                //            if (mLocation_desti != null && city_selected != null && city_selected.isSelect()) {
                //
                //
                //                if (mLocation_desti.getLatitude() != 0
                //                        && mLocation_desti.getLongitude() != 0) {
                //
                //                    storeLatLng(mLocation_desti.getLatitude(),
                //                            mLocation_desti.getLongitude());
                //                    pos_desti = new LatLng(mLocation_desti.getLatitude(), mLocation_desti.getLongitude());
                //                    if (marker_desti == null) {
                //
                //                        addMarker(pos_desti, 2);
                //                    } else {
                //
                //                        marker_desti.setTitle("");
                //                        marker_desti.setSnippet("");
                //                        marker_desti.setPosition(pos_desti);
                //                    }
                //                    marker_desti.setTitle
                //                            (city_selected.getCityName());
                //                    if(city_selected.getAddress()!=null) {
                //                        marker_desti.setTitle(city_selected.getCityName());
                //                        marker_desti.setSnippet(city_selected.getAddress());
                //                    }
                //                }
                //
                //            }
                //
                //
                //            if (mLocation_my != null
                //                    && mLocation_my.getLatitude() != 0
                //                    && mLocation_my.getLongitude() != 0) {
                //
                //                pos_myloc = new LatLng(mLocation_my.getLatitude(), mLocation_my.getLongitude());
                //                if (marker_myloc == null) {
                //
                //                    addMarker(pos_myloc, 1);
                //                } else {
                //
                //                    marker_myloc.setTitle("");
                //                    marker_myloc.setSnippet("");
                //                    marker_myloc.setPosition(pos_myloc);
                //                }
                //                marker_myloc.setTitle("Current Location");
                //
                //            }
                //
                //
                //            if (mLocation_desti == null) {
                //
                //                if (marker_desti != null) {
                //                    marker_desti.remove();
                //                    marker_desti = null;
                //                }
                //            }
                //
                //            if (marker_myloc != null && marker_desti != null
                //                    && pos_myloc != null && pos_desti != null) {
                //
                //                if (!getDirection) {
                //                    getDirection = true;
                //                    getDirection(marker_myloc.getPosition(), marker_desti.getPosition());
                //                } else {
                //
                //                    setBounds(true);
                //                }
                //
                //            } else {
                //
                //                if (marker_desti == null && pos_myloc != null)
                //                    googleMap.moveCamera(CameraUpdateFactory.
                //                            newLatLngZoom(pos_myloc, IpAddress.ZOOMCAMERA));
                //                else if (marker_desti != null && pos_myloc == null) {
                //                    googleMap.moveCamera(CameraUpdateFactory.
                //                            newLatLngZoom(pos_desti, IpAddress.ZOOMCAMERA));
                //                }
                //            }
                //        } else {
                //
                //            mapFragment.getMapAsync(this);
                //        }
                //    }
                //
                //    private void addMarker(LatLng pos_desti, int pos) {
                //
                //        if (pos == 1) {
                //
                //            marker_myloc = googleMap.
                //                    addMarker(new MarkerOptions()
                //                            .position(pos_desti)
                //                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                //                            .title(formatted));
                //        } else if (pos == 2) {
                //
                //
                //            marker_desti = googleMap.
                //                    addMarker(new MarkerOptions()
                //                            .position(pos_desti)
                //                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                //                            .title(getStoreCitySelect().getCityName()));
                //
                //            if (marker_myloc == null) {
                //
                //                googleMap.moveCamera(CameraUpdateFactory.
                //                        newLatLngZoom(pos_desti, IpAddress.ZOOMCAMERA));
                //            }
                //
                //        }
                //
                //    }
                //
                //
                //    protected void buildLocationSettingsRequest(LocationRequest mLocationRequest) {
                //        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
                //        builder.addLocationRequest(mLocationRequest);
                //        mLocationSettingsRequest = builder.build();
                //    }
                //
                //
                //    protected void checkLocationSettings() {
                //
                //        if (mGoogleApiClient != null) {
                //
                //
                //            Task<LocationSettingsResponse> result =
                //                    LocationServices.getSettingsClient(this).checkLocationSettings(
                //                            mLocationSettingsRequest);
                //
                //            result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                //                @Override
                //                public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                //
                //                    try {
                //                        LocationSettingsResponse response = task.getResult(ApiException.class);
                //                        IpAddress.e("COMPLETE onComplete", "TRUE");
                //                        getLastKnownLocation();
                //                    } catch (ApiException exception) {
                //                        switch (exception.getStatusCode()) {
                //                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //                                // Location settings are not satisfied. But could be fixed by showing the
                //                                // user a dialog.
                //                                try {
                //                                    // Cast to a resolvable exception.
                //                                    ResolvableApiException resolvable = (ResolvableApiException) exception;
                //                                    // Show the dialog by calling startResolutionForResult(),
                //                                    // and check the result in onActivityResult().
                //                                    resolvable.startResolutionForResult(
                //                                            LocationActivity.this,
                //                                            REQUEST_CHECK_SETTINGS);
                //                                } catch (IntentSender.SendIntentException e) {
                //                                    // Ignore the error.
                //                                } catch (ClassCastException e) {
                //                                    // Ignore, should be an impossible error.
                //                                }
                //                                break;
                //                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                //                                // Location settings are not satisfied. However, we have no way to fix the
                //                                // settings so we won't show the dialog.
                //                                break;
                //                        }
                //                    }
                //                }
                //            });
                //
                //        } else {
                //            hidePD();
                //        }
                //    }
                //
                //
                //    private void enableListeners(GoogleMap googleMap) {
                //
                //
                //        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                //            @Override
                //            public boolean onMyLocationButtonClick() {
                //
                //
                //                return false;
                //            }
                //        });
                //
                //    }
                //
                //    @Override
                //    public void onConnected() {
                //
                //
                //        checkLocationPermission();
                //    }
                //
                //    @Override
                //    public void onFailed() {
                //
                //        hidePD();
                //        showToast(IpAddress.ERROR_FAILED);
                //    }
                //
                //    @Override
                //    public void onSuspended() {
                //
                //        hidePD();
                //        showToast(IpAddress.ERROR_SUSPENDED);
                //    }
                //
                //    public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationVH> {
                //
                //
                //        @Override
                //        public LocationVH onCreateViewHolder(ViewGroup parent, int viewType) {
                //            return new LocationVH(getLayoutInflater().inflate(R.layout.location_inflate, parent, false));
                //        }
                //        @Override
                //        public void onBindViewHolder(LocationVH holder, int position) {
                //            City city = mCitiesList.get(position);
                //
                //            // Bind city data to the view holder
                //            holder.tvlocation.setText(city.getCityName());
                //
                //            if (city.getAddress() != null && !city.getAddress().trim().isEmpty()) {
                //                holder.tvidentity.setVisibility(View.VISIBLE);
                //                holder.tvidentity.setText(city.getAddress());
                //            } else {
                //                holder.tvidentity.setVisibility(View.GONE);
                //                holder.tvidentity.setText("");
                //            }
                //
                //            holder.tvroomcount.setVisibility(View.GONE); // Hide room count by default
                //            if (city.getRoomCount() != null && !city.getRoomCount().equalsIgnoreCase("0")) {
                //                holder.tvroomcount.setVisibility(View.VISIBLE);
                //                holder.tvroomcount.setText("Rooms: " + city.getRoomCount());
                //            }
                //
                //            // Set selection status
                //            if (city_selected != null && city_selected.equals(city)) {
                //                city.setSelect(true);
                //            } else {
                //                city.setSelect(false);
                //            }
                //
                //            // Update selection indicator
                //            if (city.isSelect()) {
                //                holder.ivselect.setImageResource(R.mipmap.ic_true);
                //            } else {
                //                holder.ivselect.setImageResource(R.mipmap.ic_false);
                //            }
                //
                //            // Enable/disable and set click listener for removing local cities
                //            if (city.isLocal()) {
                //                holder.ivremove.setEnabled(true);
                //                holder.ivremove.setVisibility(View.VISIBLE);
                //                holder.ivremove.setOnClickListener(view -> {
                //                    if (!city.isSelect()) {
                //                        mCitiesList.remove(city);
                //                        notifyDataSetChanged();
                //                    }
                //                });
                //            } else {
                //                holder.ivremove.setEnabled(false);
                //                holder.ivremove.setOnClickListener(null);
                //                holder.ivremove.setVisibility(View.INVISIBLE);
                //            }
                //
                //            // Set click listener for city item
                //            holder.rlclick.setOnClickListener(view -> {
                //                onSelected(holder.getAdapterPosition());
                //            });
                //        }
                //
                //
                //
                //        @Override
                //        public int getItemCount() {
                //            return mCitiesList.size();
                //        }
                //
                //        public class LocationVH extends RecyclerView.ViewHolder {
                //
                //            @BindView(R.id.ivselect)
                //            ImageView ivselect;
                //            @BindView(R.id.tvlocation)
                //            AppCompatTextView tvlocation;
                //            @BindView(R.id.tvroomcount)
                //            AppCompatTextView tvroomcount;
                //
                //            @BindView(R.id.tvidentity)
                //            AppCompatTextView tvidentity;
                //
                //            @BindView(R.id.ivremove)
                //            ImageView ivremove;
                //
                //            @BindView(R.id.rlclick)
                //            RelativeLayout rlclick;
                //
                //
                //            public LocationVH(View itemView) {
                //                super(itemView);
                //                ButterKnife.bind(this, itemView);
                //            }
                //        }
                //
                //    }
                //
                //    private void onSelected(int pos) {
                //        City clickedCity = mCitiesList.get(pos);
                //
                //        // Deselect all cities
                //        for (City city : mCitiesList) {
                //            city.setSelect(false);
                //        }
                //
                //        // Select the clicked city
                //        clickedCity.setSelect(true);
                //        city_selected = clickedCity;
                //
                //        // Update UI and other necessary actions
                //        notifyDataAdapter();
                //
                //        // Perform additional actions based on whether the city is local or from a service
                //        if (clickedCity.isLocal()) {
                //            // Handle local city selection
                //            handleLocalCitySelection(clickedCity);
                //        } else if (mCitiesList.get(pos).getPlaceID() != null
                //                && mCitiesList.get(pos).getPlaceID().trim().length() > 0) {
                //
                //            mSharedPreferences.edit().putString(IpAddress.PLACEID_NEW,
                //                    mCitiesList.get(pos).getPlaceID()).apply();
                //            onPlaceID();
                //        } else {
                //            // Handle service city selection
                //            handleServiceCitySelection(clickedCity,pos);
                //        }
                //    }
                //
                //    // Method to handle actions for local city selection
                //    private void handleLocalCitySelection(City localCity) {
                //        // Example: Display a message indicating that a local city is selected
                //        //  Toast.makeText(this, "Local city selected: " + localCity.getCityName(), Toast.LENGTH_SHORT).show();
                //        // Add your custom logic here for local city selection
                //        // For example, you can perform some actions specific to local cities
                //
                //
                //        mSharedPreferences.edit().putString(IpAddress.PLACEID_NEW,
                //                null).apply();
                //        onPlaceID();
                //    }
                //
                //    // Method to handle actions for service city selection
                //    private void handleServiceCitySelection(City serviceCity, int pos) {
                //        // Example: Display a message indicating that a service city is selected
                //        //  Toast.makeText(this, "Service city selected: " + serviceCity.getCityName(), Toast.LENGTH_SHORT).show();
                //        // Add your custom logic here for service city selection
                //        // For example, you can perform some actions specific to service cities
                //
                //        getPlaceIDByPlaceName(mCitiesList.get(pos).getCityName());
                //
                //
                //    }
                //
                //
                //    public void getDirection2(LatLng pos_my, LatLng pos_desti) {
                //
                //        destination = pos_desti.latitude + "," + pos_desti.longitude;
                //        origin = pos_my.latitude + "," + pos_my.longitude;
                //        String stringUrl =
                //                "http://maps.googleapis.com/maps/api/directions/json?origin="
                //                        + origin + "&destination=" + destination + "&sensor=false" + "&key="
                //                        + getString(R.string.apifor);
                //        Constants.e("url", stringUrl);
                //        OkHttpClient okHttpClient = new OkHttpClient();
                //        Request mRequest = new Request.Builder().url(stringUrl).build();
                //        okHttpClient.newCall(mRequest).enqueue(new Callback() {
                //            @Override
                //            public void onFailure(Call call, IOException e) {
                //
                //
                //            }
                //
                //            @Override
                //            public void onResponse(Call call, Response response) throws IOException {
                //
                //
                //            }
                //        });
                //
                //    }
                //
                //
                //    public void getDirection(LatLng pos_my, LatLng pos_desti) {
                //
                //
                //        showPD(this);
                //        polylineOptions = new PolylineOptions();
                //        l1 = pos_my.latitude;
                //        l2 = pos_my.longitude;
                //        l3 = pos_desti.latitude;
                //        l4 = pos_desti.longitude;
                //        origin = l1 + "," + l2;
                //        destination = l3 + "," + l4;
                //        latLngList = new ArrayList<>();
                //        MapDirection mapDirection = provideRetrofit(Constants.MAP_URL)
                //                .create(MapDirection.class);
                //
                //        Observable<String>
                //                piingoNotify =
                //                mapDirection.piingCustomerDirection(origin, destination, false
                //                        , getString(R.string.apifor));
                //        piingoNotify.subscribeOn
                //                        (Schedulers.newThread())
                //                .map(this::onMapDirection)
                //                .observeOn(AndroidSchedulers.mainThread())
                //                .subscribe(this::onDirectionFound, this::onDirectionError);
                //
                //
                //    }
                //
                //    private void onDirectionFound(String s1) {
                //
                //
                //        if (polyline != null) {
                //            polyline.remove();
                //        }
                //
                //        if (isValidLatLng(l1, l2)
                //                && isValidLatLng(l3, l4)) {
                //            mLatLngBounds = LatLngBounds.builder();
                //            mLatLngBounds.include(new LatLng(l1, l2));
                //            mLatLngBounds.include(new LatLng(l3, l4));
                //            polyline = googleMap.addPolyline(polylineOptions);
                //            cameraUpdate = CameraUpdateFactory.
                //                    newLatLngBounds(mLatLngBounds.build(), IpAddress.PADDINGMAP);
                //
                //        } else {
                //            if (isValidLatLng(l1, l2)) {
                //
                //                cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(l1, l2), IpAddress.ZOOMCAMERA);
                //            } else if (isValidLatLng(l3, l4)) {
                //                cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(l3, l4), IpAddress.ZOOMCAMERA);
                //
                //            } else {
                //                // cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(l3, l4), IpAddress.ZOOMCAMERA);
                //
                //            }
                //        }
                //        if (cameraUpdate != null)
                //            googleMap.moveCamera(cameraUpdate);
                //        getDirection = false;
                //        isTrackingEnable = false;
                //        hidePD();
                //
                //        IpAddress.e("DIRECTION", "onDirectionFound");
                //        if(pos_myloc!=null && s1!=null && s1.equalsIgnoreCase(Constants.status))
                //        {
                //            getCurrentLocationFromLATLANG(pos_myloc.latitude,pos_myloc.longitude);
                //        }
                //
                //    }
                //
                //    private void onDirectionError(Throwable throwable) {
                //
                //        getDirection = false;
                //        hidePD();
                //        IpAddress.e("DIRECTION", "onDirectionError");
                //        onDirectionFound(null);
                //    }
                //
                //    private String onMapDirection(String jsonOutput) {
                //
                //        JSONObject jsonObject = null;
                //        String status="OK";
                //        try {
                //            jsonObject = new JSONObject(jsonOutput);
                //
                //            // "status" : "ZERO_RESULTS"
                //
                //
                //            // routesArray contains ALL routes
                //            JSONArray routesArray = jsonObject.getJSONArray("routes");
                //
                //            status=jsonObject.getString("status");
                //
                //            // Grab the first route
                //
                //            if (routesArray.length() > 0) {
                //
                //                isDirectionFound=true;
                //                JSONObject route = routesArray.getJSONObject(0);
                //                JSONObject poly = route.getJSONObject("overview_polyline");
                //                String polyline = poly.getString("points");
                //                pontos = decodePoly(polyline);
                //
                //                if (pontos != null) {
                //
                //                    if (latLngList != null) {
                //                        latLngList.clear();
                //                    }
                //
                //                    polylineOptions
                //                            .addAll(pontos)
                //                            .geodesic(true)
                //                            .width(IpAddress.WIDTH_MAPLINE).color(
                //                                    ContextCompat.getColor(LocationActivity.this, R.color.bg));
                ////                    for (int i = 0; i < pontos.size() - 1; i++) {
                ////                        LatLng src = pontos.get(i);
                ////                        LatLng dest = pontos.get(i + 1);
                ////                        try {
                ////
                ////                            latLngList.add(new LatLng(src.latitude, src.longitude));
                ////                            latLngList.add(new LatLng(dest.latitude, dest.longitude));
                ////                            polylineOptions.add(
                ////                                    new LatLng(src.latitude, src.longitude));
                ////                            polylineOptions.add(new LatLng(dest.latitude, dest.longitude));
                ////                            polylineOptions.width(6).color(
                ////                                    ContextCompat.getColor(LocationActivity.this, R.color.bg));
                ////                            polylineOptions.geodesic(true);
                ////                        } catch (NullPointerException e) {
                ////                            Log.e("Error", "NullPointerException onPostExecute: " + e.toString());
                ////                        } catch (Exception e2) {
                ////                            Log.e("Error", "Exception onPostExecute: " + e2.toString());
                ////                        }
                ////
                ////                    }
                //                }
                //            }else
                //            {
                //                isDirectionFound = false;
                //                setBounds(true);
                //            }
                //        } catch (JSONException e) {
                //            isDirectionFound = false;
                //            e.printStackTrace();
                //            setBounds(false);
                //        }
                //        return status;
                //    }
                //
                //    private void setBounds() {
                //
                //        runOnUiThread(new Runnable() {
                //            @Override
                //            public void run() {
                //
                //                if (l1>0 && l2>0 && l3>0 && l4>0 && isValidLatLng(l1, l2)
                //                        && isValidLatLng(l3, l4)) {
                //                    mLatLngBounds = LatLngBounds.builder();
                //                    mLatLngBounds.include(new LatLng(l1, l2));
                //                    mLatLngBounds.include(new LatLng(l3, l4));
                //                    polyline = googleMap.addPolyline(polylineOptions);
                //                    cameraUpdate = CameraUpdateFactory.
                //                            newLatLngBounds(mLatLngBounds.build(), IpAddress.PADDINGMAP);
                //
                //                }
                //            }
                //        });
                //
                //
                //    }
                //
                //    private void setBounds(boolean showSinglemarker) {
                //
                //        runOnUiThread(new Runnable() {
                //            @Override
                //            public void run() {
                //
                //                if(showSinglemarker && l3>0 && l4>0)
                //                {
                //                    LatLng  pos_myloc = new LatLng(l3, l4);
                //                    cameraUpdate = CameraUpdateFactory.
                //                            newLatLngZoom(pos_myloc, IpAddress.ZOOMCAMERA);
                //                    googleMap.moveCamera(cameraUpdate);
                //
                //                    if(marker_desti!=null)
                //                    {
                //                        marker_desti.showInfoWindow();
                //                    }
                //                }else
                //
                //                if (l1>0 && l2>0 && l3>0 && l4>0 && isValidLatLng(l1, l2)
                //                        && isValidLatLng(l3, l4)) {
                //                    mLatLngBounds = LatLngBounds.builder();
                //                    mLatLngBounds.include(new LatLng(l1, l2));
                //                    mLatLngBounds.include(new LatLng(l3, l4));
                //                    polyline = googleMap.addPolyline(polylineOptions);
                //                    cameraUpdate = CameraUpdateFactory.
                //                            newLatLngBounds(mLatLngBounds.build(), IpAddress.PADDINGMAP);
                //
                //                    googleMap.moveCamera(cameraUpdate);
                //
                //                }
                //            }
                //        });
                //
                //
                //    }
                //
                //    private List<LatLng> decodePoly(String encoded) {
                //
                //        List<LatLng> poly = new ArrayList<LatLng>();
                //        int index = 0, len = encoded.length();
                //        int lat = 0, lng = 0;
                //
                //        while (index < len) {
                //            int b, shift = 0, result = 0;
                //            do {
                //                b = encoded.charAt(index++) - 63;
                //                result |= (b & 0x1f) << shift;
                //                shift += 5;
                //            } while (b >= 0x20);
                //            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                //            lat += dlat;
                //
                //            shift = 0;
                //            result = 0;
                //            do {
                //                b = encoded.charAt(index++) - 63;
                //                result |= (b & 0x1f) << shift;
                //                shift += 5;
                //            } while (b >= 0x20);
                //            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                //            lng += dlng;
                //
                //            LatLng p = new LatLng((((double) lat / 1E5)),
                //                    (((double) lng / 1E5)));
                //            poly.add(p);
                //        }
                //
                //        return poly;
                //    }
                //}
                if (editText != null) {
                    // Retrieve the text from the EditText
                    address = editText.getText().toString().trim();
                } else {
                    // EditText is null
                    Log.e(TAG, "EditText is null");
                }
            } else {
                // Fragment's view is null
                Log.e(TAG, "Fragment's view is null");
            }
        } else {
            // AutocompleteSupportFragment is null
            Log.e(TAG, "AutocompleteSupportFragment is null");
        }

// Check if the address is not null and not empty
        if (address != null && !address.isEmpty()) {
            getLocationFromAddress2(address);
        } else {
            // Address is null or empty
            Log.e(TAG, "Address is null or empty");
        }

        switch (requestCode) {


            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == Activity.RESULT_OK) {

                    locationEnable();

                }
                break;
            case REQUEST_CHECK_SETTINGS:
                LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        IpAddress.e(TAG, "User agreed to make required location settings changes.");
                        getLastKnownLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        IpAddress.e(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void addCity(String name, String address, boolean isLocal, boolean isSelect, String cityCode,
                         String roomCount, String useCatering, String skipRoom,
                         String etIdentity, String mapUrl, double latitude, double longitude) {

        // Deselect all other cities
        for (City city : mCitiesList) {
            city.setSelect(false);
        }

        // Create a new city object
        City newCity = new City();
        newCity.setAddress(address);
        newCity.setCityCode(cityCode);
        newCity.setCityName(name);
        newCity.setLocal(isLocal);
        newCity.setSelect(true); // Set the new city as selected
        newCity.setRoomCount(roomCount);
        newCity.setSkipRoom(skipRoom);
        newCity.setUseCatering(useCatering);
        newCity.setCityMapUrl(mapUrl);
        newCity.setIdentity(etIdentity);
        newCity.setLongitude(longitude);
        newCity.setLatitude(latitude);

        // Deselect the previously selected city if it exists
        if (city_selected != null) {
            city_selected.setSelect(false);
        }

        // Add the new city to the list
        mCitiesList.add(0, newCity); // Add to the beginning of the list

        // Store the selected city (the newly added one)
        city_selected = newCity;
        storeSelectedCity(city_selected);
        // Store all cities
        storeAllCity(mCitiesList);

        // Notify the adapter of the data change
        notifyDataAdapter();
    }

    private void notifyDataAdapter() {

        if (mCitiesList != null && mCitiesList.size() > 0) {

            rvlocation.setVisibility(View.VISIBLE);
        } else {
            rvlocation.setVisibility(View.GONE);
        }
        mLocationAdapter.notifyDataSetChanged();
        if (city_selected != null && city_selected.isSelect()) {

            if(city_selected.getAddress()!=null)

                getLocationFromAddress2(city_selected.getAddress());

            else
                getLocationFromAddress2(city_selected.getCityName());

        }

    }


    private void locationEnable() {


        showPD();
        buildGoogleApiClient(connectionCallback, connectionFailed);


    }


    @Override
    protected void onStop() {
        if (flag)
            removeGoogle();
        super.onStop();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (flag)
            reqGoogle();
    }

    @Override
    protected void onDestroy() {
        removeGoogle();
        super.onDestroy();

    }


    private void removeGoogle() {

        try {
            fusedLocationProviderClient.removeLocationUpdates(locationChange);
            if (mGoogleApiClient != null &&
                    mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();

            }
            if (eventBus != null && eventBus.isRegistered(this))
                eventBus.unregister(this);

        } catch (Exception e) {

        }
    }


    private void checkLocationPermission() {

        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheck2 = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED &&
                permissionCheck2 == PackageManager.PERMISSION_GRANTED) {

            checkLocationSettings();

        } else {


            hidePD();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {


                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
                    builder.setMessage("Please enable location permission");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            ActivityCompat.requestPermissions(LocationActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_READ_LOCATION);
                        }
                    });
                    builder.show();
                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST_READ_LOCATION);
                }
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_LOCATION);
            }
        }


    }

    private void getLastKnownLocation() {

        flag = true;
        reqGoogle();
    }

    private void afterLocationFinding() {


        showPD();
        checkInternal();
    }

    private void reqGoogle() {

        try {


            if (mLocation_my != null && mLocation_my.getAccuracy() > 0 && getCurrentAddress) {

                onCurrentAddress();

            } else if (mGoogleApiClient != null &&
                    mGoogleApiClient.isConnected()) {
                fusedLocationProviderClient.getLastLocation()
                        .addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {


                                try {

                                    mLocation_my = task.getResult(ApiException.class);
                                    if (mLocation_my != null) {
                                        onLocChange();
                                    }
                                } catch (ApiException e) {
                                    hidePD();
                                    e.printStackTrace();
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                hidePD();
                                showToast(e.toString());
                            }
                        });


                fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationChange
                        , Looper.getMainLooper());

                if (flag) {
                    afterLocationFinding();
                }


            } else {
                hidePD();
            }


        } catch (SecurityException se) {


        }
    }


    //1253
    @Subscribe
    public void onLocationChanged(Location location) {

        mLocation_my = location;
        if (mLocation_my == null || mLocation_my.getAccuracy() == 0) {
            onLocChange();
            getCurrentLocationFromLATLANG(mLocation_my.getLatitude(),mLocation_my.getLongitude());
        }
    }

    private void onLocChange() {


        if (googleMap != null && isMapLoded) {

            afterLocation();

        } else {
            mapFragment.getMapAsync(this);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (this.googleMap != null) {
            this.googleMap.clear();

        }

        isMapLoded = true;
        this.googleMap = googleMap;
        try {
            this.googleMap.setBuildingsEnabled(true);
            this.googleMap.getUiSettings().setAllGesturesEnabled(true);
            this.googleMap.getUiSettings().setMapToolbarEnabled(false);
            enableListeners(this.googleMap);
            afterLocation();
        } catch (SecurityException e) {

            Constants.noData(2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    skipLocation = false;
                    checkLocationSettings();

                } else {

                    //location permission disabled

                    skipLocation = true;
                    afterLocationFinding();
                }

            }
            break;
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void afterLocation() {

        if (googleMap != null) {
            if (polyline != null) {
                polyline.remove();
            }
            if (mLocation_desti != null && city_selected != null && city_selected.isSelect()) {


                if (mLocation_desti.getLatitude() != 0
                        && mLocation_desti.getLongitude() != 0) {

                    storeLatLng(mLocation_desti.getLatitude(),
                            mLocation_desti.getLongitude());
                    pos_desti = new LatLng(mLocation_desti.getLatitude(), mLocation_desti.getLongitude());
                    if (marker_desti == null) {

                        addMarker(pos_desti, 2);
                    } else {

                        marker_desti.setTitle("");
                        marker_desti.setSnippet("");
                        marker_desti.setPosition(pos_desti);
                    }
                    marker_desti.setTitle
                            (city_selected.getCityName());
                    if(city_selected.getAddress()!=null) {
                        marker_desti.setTitle(city_selected.getCityName());
                        marker_desti.setSnippet(city_selected.getAddress());
                    }
                }

            }


            if (mLocation_my != null
                    && mLocation_my.getLatitude() != 0
                    && mLocation_my.getLongitude() != 0) {

                pos_myloc = new LatLng(mLocation_my.getLatitude(), mLocation_my.getLongitude());
                if (marker_myloc == null) {

                    addMarker(pos_myloc, 1);
                } else {

                    marker_myloc.setTitle("");
                    marker_myloc.setSnippet("");
                    marker_myloc.setPosition(pos_myloc);
                }
                marker_myloc.setTitle("Current Location");

            }


            if (mLocation_desti == null) {

                if (marker_desti != null) {
                    marker_desti.remove();
                    marker_desti = null;
                }
            }

            if (marker_myloc != null && marker_desti != null
                    && pos_myloc != null && pos_desti != null) {

                if (!getDirection) {
                    getDirection = true;
                    getDirection(marker_myloc.getPosition(), marker_desti.getPosition());
                } else {

                    setBounds(true);
                }

            } else {

                if (marker_desti == null && pos_myloc != null)
                    googleMap.moveCamera(CameraUpdateFactory.
                            newLatLngZoom(pos_myloc, IpAddress.ZOOMCAMERA));
                else if (marker_desti != null && pos_myloc == null) {
                    googleMap.moveCamera(CameraUpdateFactory.
                            newLatLngZoom(pos_desti, IpAddress.ZOOMCAMERA));
                }
            }
        } else {

            mapFragment.getMapAsync(this);
        }
    }

    private void addMarker(LatLng pos_desti, int pos) {

        if (pos == 1) {

            marker_myloc = googleMap.
                    addMarker(new MarkerOptions()
                            .position(pos_desti)
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                            .title(formatted));
        } else if (pos == 2) {


            marker_desti = googleMap.
                    addMarker(new MarkerOptions()
                            .position(pos_desti)
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                            .title(getStoreCitySelect().getCityName()));

            if (marker_myloc == null) {

                googleMap.moveCamera(CameraUpdateFactory.
                        newLatLngZoom(pos_desti, IpAddress.ZOOMCAMERA));
            }

        }

    }


    protected void buildLocationSettingsRequest(LocationRequest mLocationRequest) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }


    protected void checkLocationSettings() {

        if (mGoogleApiClient != null) {


            Task<LocationSettingsResponse> result =
                    LocationServices.getSettingsClient(this).checkLocationSettings(
                            mLocationSettingsRequest);

            result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                @Override
                public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);
                        IpAddress.e("COMPLETE onComplete", "TRUE");
                        getLastKnownLocation();
                    } catch (ApiException exception) {
                        switch (exception.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the
                                // user a dialog.
                                try {
                                    // Cast to a resolvable exception.
                                    ResolvableApiException resolvable = (ResolvableApiException) exception;
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    resolvable.startResolutionForResult(
                                            LocationActivity.this,
                                            REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                } catch (ClassCastException e) {
                                    // Ignore, should be an impossible error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.
                                break;
                        }
                    }
                }
            });

        } else {
            hidePD();
        }
    }


    private void enableListeners(GoogleMap googleMap) {


        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {


                return false;
            }
        });

    }

    @Override
    public void onConnected() {


        checkLocationPermission();
    }

    @Override
    public void onFailed() {

        hidePD();
        showToast(IpAddress.ERROR_FAILED);
    }

    @Override
    public void onSuspended() {

        hidePD();
        showToast(IpAddress.ERROR_SUSPENDED);
    }

    public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationVH> {


        @Override
        public LocationVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LocationVH(getLayoutInflater().inflate(R.layout.location_inflate, parent, false));
        }
        @Override
        public void onBindViewHolder(LocationVH holder, int position) {
            City city = mCitiesList.get(position);

            // Bind city data to the view holder
            holder.tvlocation.setText(city.getCityName());

            if (city.getAddress() != null && !city.getAddress().trim().isEmpty()) {
                holder.tvidentity.setVisibility(View.VISIBLE);
                holder.tvidentity.setText(city.getAddress());
            } else {
                holder.tvidentity.setVisibility(View.GONE);
                holder.tvidentity.setText("");
            }

            holder.tvroomcount.setVisibility(View.GONE); // Hide room count by default
            if (city.getRoomCount() != null && !city.getRoomCount().equalsIgnoreCase("0")) {
                holder.tvroomcount.setVisibility(View.VISIBLE);
                holder.tvroomcount.setText("Rooms: " + city.getRoomCount());
            }

            // Set selection status
            if (city_selected != null && city_selected.equals(city)) {
                city.setSelect(true);
            } else {
                city.setSelect(false);
            }

            // Update selection indicator
            if (city.isSelect()) {
                holder.ivselect.setImageResource(R.mipmap.ic_true);
            } else {
                holder.ivselect.setImageResource(R.mipmap.ic_false);
            }

            // Enable/disable and set click listener for removing local cities
            if (city.isLocal()) {
                holder.ivremove.setEnabled(true);
                holder.ivremove.setVisibility(View.VISIBLE);
                holder.ivremove.setOnClickListener(view -> {
                    if (!city.isSelect()) {
                        mCitiesList.remove(city);
                        notifyDataSetChanged();
                    }
                });
            } else {
                holder.ivremove.setEnabled(false);
                holder.ivremove.setOnClickListener(null);
                holder.ivremove.setVisibility(View.INVISIBLE);
            }

            // Set click listener for city item
            holder.rlclick.setOnClickListener(view -> {
                onSelected(holder.getAdapterPosition());
            });
        }



        @Override
        public int getItemCount() {
            return mCitiesList.size();
        }

        public class LocationVH extends RecyclerView.ViewHolder {

            @BindView(R.id.ivselect)
            ImageView ivselect;
            @BindView(R.id.tvlocation)
            AppCompatTextView tvlocation;
            @BindView(R.id.tvroomcount)
            AppCompatTextView tvroomcount;

            @BindView(R.id.tvidentity)
            AppCompatTextView tvidentity;

            @BindView(R.id.ivremove)
            ImageView ivremove;

            @BindView(R.id.rlclick)
            RelativeLayout rlclick;


            public LocationVH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }

    private void onSelected(int pos) {
        City clickedCity = mCitiesList.get(pos);

        // Deselect all cities
        for (City city : mCitiesList) {
            city.setSelect(false);
        }

        // Select the clicked city
        clickedCity.setSelect(true);
        city_selected = clickedCity;

        // Update UI and other necessary actions
        notifyDataAdapter();

        // Perform additional actions based on whether the city is local or from a service
        if (clickedCity.isLocal()) {
            // Handle local city selection
            handleLocalCitySelection(clickedCity);
        } else if (mCitiesList.get(pos).getPlaceID() != null
                && mCitiesList.get(pos).getPlaceID().trim().length() > 0) {

            mSharedPreferences.edit().putString(IpAddress.PLACEID_NEW,
                    mCitiesList.get(pos).getPlaceID()).apply();
            onPlaceID();
        } else {
            // Handle service city selection
            handleServiceCitySelection(clickedCity,pos);
        }
    }

    // Method to handle actions for local city selection
    private void handleLocalCitySelection(City localCity) {
        // Example: Display a message indicating that a local city is selected
        //  Toast.makeText(this, "Local city selected: " + localCity.getCityName(), Toast.LENGTH_SHORT).show();
        // Add your custom logic here for local city selection
        // For example, you can perform some actions specific to local cities


        mSharedPreferences.edit().putString(IpAddress.PLACEID_NEW,
                null).apply();
        onPlaceID();
    }

    // Method to handle actions for service city selection
    private void handleServiceCitySelection(City serviceCity, int pos) {
        // Example: Display a message indicating that a service city is selected
        //  Toast.makeText(this, "Service city selected: " + serviceCity.getCityName(), Toast.LENGTH_SHORT).show();
        // Add your custom logic here for service city selection
        // For example, you can perform some actions specific to service cities

        getPlaceIDByPlaceName(mCitiesList.get(pos).getCityName());


    }


    public void getDirection2(LatLng pos_my, LatLng pos_desti) {

        destination = pos_desti.latitude + "," + pos_desti.longitude;
        origin = pos_my.latitude + "," + pos_my.longitude;
        String stringUrl =
                "http://maps.googleapis.com/maps/api/directions/json?origin="
                        + origin + "&destination=" + destination + "&sensor=false" + "&key="
                        + getString(R.string.apifor);
        Constants.e("url", stringUrl);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request mRequest = new Request.Builder().url(stringUrl).build();
        okHttpClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


            }
        });

    }


    public void getDirection(LatLng pos_my, LatLng pos_desti) {


        showPD(this);
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
                        , getString(R.string.apifor));
        piingoNotify.subscribeOn
                        (Schedulers.newThread())
                .map(this::onMapDirection)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDirectionFound, this::onDirectionError);


    }

    private void onDirectionFound(String s1) {


        if (polyline != null) {
            polyline.remove();
        }

        if (isValidLatLng(l1, l2)
                && isValidLatLng(l3, l4)) {
            mLatLngBounds = LatLngBounds.builder();
            mLatLngBounds.include(new LatLng(l1, l2));
            mLatLngBounds.include(new LatLng(l3, l4));
            polyline = googleMap.addPolyline(polylineOptions);
            cameraUpdate = CameraUpdateFactory.
                    newLatLngBounds(mLatLngBounds.build(), IpAddress.PADDINGMAP);

        } else {
            if (isValidLatLng(l1, l2)) {

                cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(l1, l2), IpAddress.ZOOMCAMERA);
            } else if (isValidLatLng(l3, l4)) {
                cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(l3, l4), IpAddress.ZOOMCAMERA);

            } else {
                // cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(l3, l4), IpAddress.ZOOMCAMERA);

            }
        }
        if (cameraUpdate != null)
            googleMap.moveCamera(cameraUpdate);
        getDirection = false;
        isTrackingEnable = false;
        hidePD();

        IpAddress.e("DIRECTION", "onDirectionFound");
        if(pos_myloc!=null && s1!=null && s1.equalsIgnoreCase(Constants.status))
        {
            getCurrentLocationFromLATLANG(pos_myloc.latitude,pos_myloc.longitude);
        }

    }

    private void onDirectionError(Throwable throwable) {

        getDirection = false;
        hidePD();
        IpAddress.e("DIRECTION", "onDirectionError");
        onDirectionFound(null);
    }

    private String onMapDirection(String jsonOutput) {

        JSONObject jsonObject = null;
        String status="OK";
        try {
            jsonObject = new JSONObject(jsonOutput);

            // "status" : "ZERO_RESULTS"


            // routesArray contains ALL routes
            JSONArray routesArray = jsonObject.getJSONArray("routes");

            status=jsonObject.getString("status");

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
                            .addAll(pontos)
                            .geodesic(true)
                            .width(IpAddress.WIDTH_MAPLINE).color(
                                    ContextCompat.getColor(LocationActivity.this, R.color.bg));
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
//                                    ContextCompat.getColor(LocationActivity.this, R.color.bg));
//                            polylineOptions.geodesic(true);
//                        } catch (NullPointerException e) {
//                            Log.e("Error", "NullPointerException onPostExecute: " + e.toString());
//                        } catch (Exception e2) {
//                            Log.e("Error", "Exception onPostExecute: " + e2.toString());
//                        }
//
//                    }
                }
            }else
            {
                isDirectionFound = false;
                setBounds(true);
            }
        } catch (JSONException e) {
            isDirectionFound = false;
            e.printStackTrace();
            setBounds(false);
        }
        return status;
    }

    private void setBounds() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (l1>0 && l2>0 && l3>0 && l4>0 && isValidLatLng(l1, l2)
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
                    {
                        marker_desti.showInfoWindow();
                    }
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

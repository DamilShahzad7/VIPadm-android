package vip.com.vipmeetings;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.interfaces.IGoogleApiClient;
import vip.com.vipmeetings.interfaces.MapDirection;
import vip.com.vipmeetings.utilities.Constants;
import static vip.com.vipmeetings.utilities.Constants.MAP_URL;



/**
 * Created by Srinath on 02/12/17.
 */

public abstract class BaseLocationActivity extends BaseActivity implements IGoogleApiClient {

    private static final String TAG = LocationActivity.class.getSimpleName();
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1253;
    public static final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 1200;
    GoogleApiAvailability mGoogleApiAvailability;
    Location mLocation_my, mLocation_desti;
    LocationRequest mLocationRequest;
    LocationSettingsRequest mLocationSettingsRequest;
    List<LatLng> pontos;
    String origin, destination;
    LatLngBounds.Builder mLatLngBounds;
    PolylineOptions polylineOptions;
    Polyline polyline;
    CameraUpdate cameraUpdate;
    List<LatLng> latLngList;

    int i;
    private boolean flag;

    boolean loc_show = true;
    LocationChange locationChange;
    FusedLocationProviderClient fusedLocationProviderClient;
    boolean skipLocation;

    boolean isTrackingEnable = true;
    private String formatted;

    public Marker marker_myloc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setClientCallbacks(this);
        locationChange = new LocationChange();
        createLocationRequest();
        buildLocationSettingsRequest();
        mLocation_desti = new Location("des");

    }


    @Override
    protected void onStart() {
        super.onStart();
        regis(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregis(this);
    }

    public void showErrorDialog() {

        if (mGoogleApiAvailability == null)
            mGoogleApiAvailability = GoogleApiAvailability.getInstance();
        i = mGoogleApiAvailability.isGooglePlayServicesAvailable(getApplicationContext());
        if (i == ConnectionResult.SUCCESS) {


            locationEnable();

        } else if (mGoogleApiAvailability.isUserResolvableError(i)) {

            mGoogleApiAvailability.getErrorDialog(this, i,
                    REQUEST_GOOGLE_PLAY_SERVICES, dialogInterface -> {

                        hidePD();
                        showErrorDialog();

                    });

        } else {

            showToast(mGoogleApiAvailability.getErrorString(REQUEST_GOOGLE_PLAY_SERVICES));
        }
    }

    public void locationEnable() {

        buildGoogleApiClient(connectionCallback, connectionFailed);
    }

    public void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }


    public void checkLocationSettings() {


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
                                        BaseLocationActivity.this,
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
//        PendingResult<LocationSettingsResult> result =
//                LocationServices.SettingsApi.checkLocationSettings(
//                        mGoogleApiClient,
//                        mLocationSettingsRequest
//                );
//        result.setResultCallback(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
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
        }

    }


    //    @Override
//    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
//
//
//        final Status status = locationSettingsResult.getStatus();
//        switch (status.getStatusCode()) {
//            case LocationSettingsStatusCodes.SUCCESS:
//                Log.e(TAG, "All location settings are satisfied.");
//
//                getLastKnownLocation();
//
//                break;
//            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                Log.e(TAG, "Location settings are not satisfied. Show the user a dialog to" +
//                        "upgrade location settings ");
//                try {
//                    // Show the dialog by calling startResolutionForResult(), and check the result
//                    // in onActivityResult().
//                    status.startResolutionForResult(BaseLocationActivity.this, REQUEST_CHECK_SETTINGS);
//                } catch (IntentSender.SendIntentException e) {
//                    Log.e(TAG, "PendingIntent unable to execute request.");
//                }
//                break;
//            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                Log.e(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
//                        "not created.");
//                break;
//        }
//    }


    @Override
    protected void onPause() {
        if (flag)
            removeGoogle();
        super.onPause();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (flag)
            reqGoogle();
    }

    private void reqGoogle() {

        try {

            if (mGoogleApiClient != null && mGoogleApiClient.isConnected() &&
                    mLocationRequest != null) {

                fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationChange
                        , Looper.getMainLooper());
            }

        } catch (SecurityException se) {


        }
    }

    @Override
    protected void onDestroy() {

        removeGoogle();
        super.onDestroy();

    }


    private void removeGoogle() {

        try {
            fusedLocationProviderClient.removeLocationUpdates(locationChange);
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        } catch (Exception e) {

        }
    }


    @Override
    public void onConnected() {

        if (skipLocation) {

        }
        if (getIntent().hasExtra(Constants.LOAD) && loc_show) {

            loc_show = false;
            checkLocationPermission();
        } else {
            loc_show = false;
            checkLocationPermission();
        }
        hidePD();

    }

    @Override
    public void onSuspended() {

        mGoogleApiClient.connect();

    }

    @Override
    public void onFailed() {

//        Toast.makeText(this, "Connection failed: ConnectionResult.getErrorCode() = " +
//                +connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();

        hidePD();
    }

    private void checkLocationPermission() {

        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {


            skipLocation = false;
            checkLocationSettings();

        } else {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {


                AlertDialog.Builder builder = new AlertDialog.Builder(BaseLocationActivity.this);
                builder.setMessage(getString(R.string.permissionlocation));
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ActivityCompat.requestPermissions(BaseLocationActivity.this,
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                        android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                MY_PERMISSIONS_REQUEST_READ_LOCATION);
                    }
                });
                builder.show();
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_LOCATION);
            }
        }


    }


    private void getLastKnownLocation() {

        try {

            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {


                flag = true;


                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

                fusedLocationProviderClient.getLastLocation().
                        addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {


                                if (location!=null && location.getAccuracy()>0)
                                onLocationChanged(location);

                            }
                        });


                reqGoogle();
            }

        } catch (SecurityException se) {


        }
    }


    //1253 callback
    private void onLocChange(Location mLocation_my) {


        getLocation(mLocation_my);

    }

    @Subscribe
    public void onLocationChanged(Location location) {

        if(location!=null && location.getAccuracy()>0) {

            mSharedPreferences.edit().putString(IpAddress.LOCATIONLAT, String.valueOf(location.getLatitude())).apply();
            mSharedPreferences.edit().putString(IpAddress.LOCATIONLON, String.valueOf(location.getLongitude())).apply();

            if (mLocation_my == null) {
                mLocation_my = location;
                onLocChange(mLocation_my);

                getCurrentLocationFromLATLANG(mLocation_my.getLatitude(), mLocation_my.getLongitude());

            } else {
                mLocation_my = location;
                onLocChange(mLocation_my);
            }
        }
    }

    public String onLocationMap(String response) {

        formatted = "";
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
        }
        return formatted;


    }

    private void onCurrentLocationFound(String s) {

        hidePD();

        IpAddress.e("formatted2",s);

        if(marker_myloc!=null)
        {
            marker_myloc.setSnippet(s);
        }


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
                .subscribe(this::onCurrentLocationFound, this::onLocationErr);

    }

    public void onLocationErr(Throwable throwable) {

        IpAddress.e("DIRECTION", "onLocationErr");
        showToast(throwable.toString());
        hidePD();
    }


    public abstract void getLocation(Location mSource);


}

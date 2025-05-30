package vip.com.vipmeetings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import vip.com.vipmeetings.body.GetAuthTokenRequestBody;
import vip.com.vipmeetings.envelope.SoapAuthTokenRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapAuthTokenResponseEnvelope;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.interfaces.IEvacuationMobileToken;
import vip.com.vipmeetings.interfaces.LoginInterface;
import vip.com.vipmeetings.interfaces.MainApi;
import vip.com.vipmeetings.models.AuthenticateEvacuationModel;
import vip.com.vipmeetings.models.City;
import vip.com.vipmeetings.models.Employee;
import vip.com.vipmeetings.models.EvacuationResponsible;
import vip.com.vipmeetings.request.GetAuthTokenData;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 29/05/17.
 */

public class BaseActivity extends MainBaseActivity {

    public static final String MAP_URL = "https://maps.googleapis.com";
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1201;
    public Employee employModel;
    public int height = 75;
    public int width = 60;
    IEvacuationMobileToken iEvacuationMobileToken;
    boolean isRunningToken = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getEmp();
        setOKHTTPAfterLogin();

    }


    public CalendarDay nextYear() {
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 100);
        int year = nextYear.get(Calendar.YEAR);
        int month = nextYear.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = nextYear.get(Calendar.DAY_OF_MONTH);

        return CalendarDay.from(year, month, day);
    }


    public void setService() {


        service = retrofit.create(LoginInterface.class);
    }

    public void setiEvacuationMobileToken(Context context) {
        iEvacuationMobileToken = (IEvacuationMobileToken) context;
    }

    public void removeTrust() {

        try {

            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {

                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {

        }
    }


    public void getAuthToken(boolean isRunToken) {

        if (isRunToken) {
            isRunningToken = false;
            hidePD();
            eventBus.postSticky(IpAddress.AUTHTOKEN);
        } else {
            showPD();
            isRunningToken = true;
            getEvacuationTokenObservable().
                    subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onToken_Evacuation, this::onAuthEvaError);

        }

    }

    private void onAuthEvaError(Throwable throwable) {

        hidePD();
        isRunningToken = false;
        hidePD();
        showMessage(throwable.toString());


    }

    public Observable<SoapAuthTokenResponseEnvelope> getMobileMettingsObservable() {


        setOKHTTPAfterLogin();
        if (getApp().isReached()) {
            setRetrofitNoToken(getApp().getTcpIp());
        }
        mainApi = retrofit.create(MainApi.class);
        SoapAuthTokenRequestEnvelope soapAuthTokenEnvelope = new SoapAuthTokenRequestEnvelope();
        GetAuthTokenData getAuthTokenData = new GetAuthTokenData();
        getAuthTokenData.setiUserID(IpAddress.IUSERID);
        getAuthTokenData.setsPasscode(IpAddress.IPASSCODE);
        GetAuthTokenRequestBody getAuthTokenBody = new GetAuthTokenRequestBody();
        getAuthTokenBody.setGetAuthTokenData(getAuthTokenData);
        soapAuthTokenEnvelope.setBody(getAuthTokenBody);
        Observable<SoapAuthTokenResponseEnvelope> lClientDetailsObservable = mainApi.
                getAuthTokenMobileMeetings(soapAuthTokenEnvelope);
        return lClientDetailsObservable;
    }


    public Observable<SoapAuthTokenResponseEnvelope> getEvacuationTokenObservable() {

        setOKHTTPAfterLogin();
        setRetrofitNoToken(getTcpIpEvacuationFalse());
        mainApi = retrofit.create(MainApi.class);
        SoapAuthTokenRequestEnvelope soapAuthTokenEnvelope = new SoapAuthTokenRequestEnvelope();
        GetAuthTokenData getAuthTokenData = new GetAuthTokenData();
        getAuthTokenData.setiUserID(IpAddress.IUSERID);
        getAuthTokenData.setsPasscode(IpAddress.IPASSCODE);
        GetAuthTokenRequestBody getAuthTokenBody = new GetAuthTokenRequestBody();
        getAuthTokenBody.setGetAuthTokenData(getAuthTokenData);
        soapAuthTokenEnvelope.setBody(getAuthTokenBody);
        Observable<SoapAuthTokenResponseEnvelope> lClientDetailsObservable = mainApi.
                getAuthTokenClientSettings(soapAuthTokenEnvelope);
        return lClientDetailsObservable;
    }


    private boolean isURLValid() {

        try {
            URL url = new URL(getApp().getTcpIp());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return getApp().isReached();
    }


    public VIPmeetings getApp() {

        return (VIPmeetings) getApplicationContext();
    }


    public void setRetrofit(String ip) {


        try {
            setOKHTTPAfterLogin();
            Strategy strategy = new AnnotationStrategy();
            Serializer serializer = new Persister(strategy);
            retrofit = new Retrofit.Builder()
                    .baseUrl(ip)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(SimpleXmlConverterFactory.create(serializer))
                    .client(client)
                    .build();
        } catch (Exception e) {
            Constants.e("URL SET RETROFIT", e.toString());
        }

    }


    public void setRetrofitNoToken(String ip) {


        //  setOKHTTPBeforeLogin();
        Strategy strategy = new AnnotationStrategy();
        Serializer serializer = new Persister(strategy);
        retrofit = new Retrofit
                .Builder()
                .baseUrl(ip)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create(serializer))
                .client(client)
                .build();

    }


    private void onAuthError(Throwable throwable) {

        isRunningToken = false;
        hidePD();
        showMessage(throwable.toString());



    }


    private void onToken_Mobile(SoapAuthTokenResponseEnvelope getAuthTokenResponse) {

        //isreached and url exist
        isRunningToken = false;
        hidePD();

        saveAuthTokenPref_Mobile(getAuthTokenResponse.getBody()
                .getGetAuthTokenResult().getGetAuthTokenResult());
        if (iEvacuationMobileToken != null) {

            if (getApp().isReached()) {
                iEvacuationMobileToken.onTokenMobile(getAuthTokenResponse.getBody()
                        .getGetAuthTokenResult().getGetAuthTokenResult());
            } else

                iEvacuationMobileToken.onTokenEvacuation(getAuthTokenResponse.getBody()
                        .getGetAuthTokenResult().getGetAuthTokenResult());

        } else {
            getAuthToken(true);
        }

    }

    private void onToken_Evacuation(SoapAuthTokenResponseEnvelope getAuthTokenResponse) {

        isRunningToken = false;
        saveAuthTokenPref_Evacuation(getAuthTokenResponse.getBody().
                getGetAuthTokenResult().getGetAuthTokenResult());

        if (getApp().isReached() &&
                getApp().getTcpIp() != null &&
                getApp().getTcpIp().trim().length() > 1 &&
                !(getApp().getTcpIp().trim()
                        .substring(0, getApp().getTcpIp().trim().length() - 1)
                        .equalsIgnoreCase(Constants.IPADDRESS))) {

            getMobileMettingsObservable().
                    subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onToken_Mobile, this::onAuthError);

        } else {

            if (iEvacuationMobileToken != null) {

                if (isApisorEqual()) {
                    saveAuthTokenPref_Mobile(getAuthTokenResponse.getBody()
                            .getGetAuthTokenResult().getGetAuthTokenResult());
                }
                if (getApp().isReached()) {

                    iEvacuationMobileToken.onTokenMobile(getAuthTokenResponse.getBody()
                            .getGetAuthTokenResult().getGetAuthTokenResult());
                } else

                iEvacuationMobileToken.onTokenEvacuation(getAuthTokenResponse.getBody()
                        .getGetAuthTokenResult().getGetAuthTokenResult());
            } else {
                getAuthToken(true);

            }

        }
    }


    public void gotoIntent(boolean flag) {

        Intent i;
        if (mSharedPreferences.contains(Constants.OTPSUCCESS)
                && getStoreCitySelect() != null) {
            i = new Intent(this, HomeActivity.class);
            if (flag) {
                i.putExtra(IpAddress.EVACUATION, true);
            }
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra(Constants.LOAD, true);
            startActivity(i);
            finish();

        } else if (mSharedPreferences.contains(Constants.OTPSUCCESS)) {


            if (getEvacuationModel() != null) {
                i = new Intent(this, HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (flag) {
                    i.putExtra(IpAddress.EVACUATION, true);
                }
                i.putExtra(Constants.LOAD, true);
                startActivity(i);
                finish();
            } else {

                i = new Intent(this, LocationActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra(Constants.NOLOCATION, true);
                startActivity(i);
                finish();
            }

        } else {
            i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }

        hidePD();

    }


    public void saveAuthTokenPref_Mobile(String authToken) {

        if (isApisorEqual()) {
            mSharedPreferences.edit().putString(IpAddress.AUTHTOKEN_EVACUATION, authToken).apply();
        }

        mSharedPreferences.edit().putString(IpAddress.AUTHTOKEN_MOBILE, authToken).apply();
    }

    public String getAuthTokenPref_Mobile() {

        return mSharedPreferences.getString(IpAddress.AUTHTOKEN_MOBILE, "CC484588-C0B4-4777-B6C6-3F149728FC49");
    }


    private void saveAuthTokenPref_Evacuation(String authToken) {

        if (getApp().isReached() && getApp().getTcpIp() != null
                && getApp().getTcpIp().trim()
                .substring(0, getApp().getTcpIp().trim().length() - 1)
                .equalsIgnoreCase(Constants.IPADDRESS)) {
            mSharedPreferences.edit().putString(IpAddress.AUTHTOKEN_MOBILE, authToken).apply();
        }
        mSharedPreferences.edit().putString(IpAddress.AUTHTOKEN_EVACUATION, authToken).apply();
    }

    public String getAuthTokenPref_Evacuation() {

        return mSharedPreferences.getString(IpAddress.AUTHTOKEN_EVACUATION, "CC484588-C0B4-4777-B6C6-3F149728FC49");
    }


    public boolean isGooglePlayServicesAvailable(Activity activity) {


        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (status != ConnectionResult.SUCCESS) {

            hidePD();
//            if (googleApiAvailability.isUserResolvableError(status)) {
//                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
//            }
            return false;
        }
        return true;
    }


    public void hideKeyboard(View v) {

        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void setRoomSelection(String roomselectionid) {

        mSharedPreferences.edit().putString(Constants.ROOMSELECTIONID, roomselectionid).apply();

    }


    public void setMeetingId(String meetingId) {

        mSharedPreferences.edit().putString(Constants.MEETINGID, meetingId).apply();

    }


    public Double getLongitude() {

        return Double.parseDouble(mSharedPreferences.getString(Constants.LON,
                "0"));
    }


    public Double getLatitude() {
        return Double.parseDouble(mSharedPreferences.getString(Constants.LAT,
                "0"));
    }

    public void storeAllCityClear() {

        mSharedPreferences.edit().remove(Constants.NEWCITY).apply();
    }


    public void storeAllCity(List<City> s) {


        mSharedPreferences.edit().putString(Constants.NEWCITY, mGson.toJson(s)).apply();
    }

    public List<City> getAllCityStored() {


        mGson.serializeNulls();
        return mGson.fromJson(mSharedPreferences.getString(Constants.NEWCITY, null),
                new TypeToken<List<City>>() {
                }.getType());
    }

    public void storeSelectedCity(City city) {

        try {
            if (city != null) {
                city.setSelect(true);

                mSharedPreferences.edit().putString(IpAddress.CITY_SELECTED, mGson.toJson(city)).apply();
            }
            IpAddress.e("CITY STORED", mGson.toJson(city));
        }catch (Exception e)
        {

        }

    }

    public void removeSelectedCity() {

        IpAddress.e("CITY REMOVED", "DELETE");
        mSharedPreferences.edit().remove(IpAddress.CITY_SELECTED).apply();


    }


    public City getStoreCitySelect() {

        mGson.serializeNulls();
        return mGson.fromJson(mSharedPreferences.getString(IpAddress.CITY_SELECTED,
                null), City.class);


    }


    public String getMeetingId() {

        return mSharedPreferences.getString(Constants.MEETINGID, "");

    }


    public String getStarttime() {

        return mSharedPreferences.getString(Constants.STARTTIME, "");
    }

    public void setStarttime(String starttime) {


        mSharedPreferences.edit().putString(Constants.STARTTIME, starttime).apply();
    }


    public String getCityName_Address() {

        mGson.serializeNulls();
        return getStoreCitySelect().getCityName();
    }

    public String getAddress() {

        mGson.serializeNulls();
        return getStoreCitySelect().getAddress();
    }


    public void storeAuthenticateEvacuationModel(String s) {

        mSharedPreferences.edit().putString(Constants.EVACUATION_AUTHENTICATE, s).apply();
    }

    public AuthenticateEvacuationModel getAuthenticateEvacuationModel() {

        String evacuation = mSharedPreferences.getString(Constants.EVACUATION_AUTHENTICATE,
                null);
        AuthenticateEvacuationModel authenticateEvacuationModel =
                mGson.fromJson(evacuation, AuthenticateEvacuationModel.class);

        return authenticateEvacuationModel;

    }

    public void setEvacuationModel(String evacuationModel) {


        mSharedPreferences.edit().putString(Constants.EVACUATION, evacuationModel).apply();
    }


    public EvacuationResponsible getEvacuationModel() {

        String evacuation = mSharedPreferences.getString(Constants.EVACUATION, null);

        return mGson.fromJson(evacuation, EvacuationResponsible.class);

    }


//    public void storeEmpModel(String s) {
//
//        mSharedPreferences.edit().putString(Constants.EMPLOY1, s).apply();
//    }
//
//
//    public void getEmpModel() {
//
//        employModel = mGson.fromJson(mSharedPreferences.getString(Constants.EMPLOY1,
//                null), Employee.class);
//
//        if (employModel != null) {
//            if (employModel.getBARCODE() != null && employModel.getBARCODE().trim().length() > 0) {
//                setBarcode(employModel.getBARCODE());
//            }
//        }
//    }


    public void storeEmp(String s) {

        mSharedPreferences.edit().putString(Constants.EMPLOY1, s).apply();
    }

    public Employee getEmp() {

        employModel = mGson.fromJson(mSharedPreferences.getString(Constants.EMPLOY1,
                null), Employee.class);
        return employModel;

    }


    public String getSubject() {

        return mSharedPreferences.getString(Constants.SUBJECT, "");
    }

    public void setSubject(String subject) {

        mSharedPreferences.edit().putString(Constants.SUBJECT, subject).apply();
    }

    public void removeSubject(){
        mSharedPreferences.edit().remove(Constants.SUBJECT).apply();
    }

    public void gotoHome() {


        Intent i = new Intent(this, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }


    public void setEmail(String email) {

        mSharedPreferences.edit()
                .putString(Constants.EMAIL, email).apply();

    }


    public String getTcpIpEvacuationFalse() {


        return Constants.IPADDRESS2;
    }


    public void setClientID(String clientId) {

        if (clientId != null && clientId.trim().length() > 0)
            mSharedPreferences.edit().putString(Constants.ClientID, clientId).apply();
    }

    public void setBarcode(String barcode) {

        if (barcode != null && barcode.trim().length() > 0)
            mSharedPreferences.edit().putString(Constants.BARCODE, barcode).apply();
    }


    public boolean hasNetwork() {

        boolean flag = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            flag = (networkInfo != null && networkInfo.isAvailable() && (
                    networkInfo.isConnectedOrConnecting() || networkInfo.isConnected()));


        }
        if (flag) {
            return flag;
        } else {
            Toast.makeText(this, getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
        }

        return flag;


    }


    public void onError(Throwable throwable) {


        hidePD();

    }


}

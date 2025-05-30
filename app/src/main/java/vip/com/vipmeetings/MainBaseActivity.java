package vip.com.vipmeetings;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.chuckerteam.chucker.api.ChuckerInterceptor;//raparthy
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.gson.Gson;
//import com.readystatesoftware.chuck.ChuckInterceptor;//raparthy

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.interfaces.ContactsApi;
import vip.com.vipmeetings.interfaces.IGoogleApiClient;
import vip.com.vipmeetings.interfaces.ITokenRefresh;
import vip.com.vipmeetings.interfaces.LoginInterface;
import vip.com.vipmeetings.interfaces.MainApi;
import vip.com.vipmeetings.models.AuthenticateEvacuationModel;
import vip.com.vipmeetings.models.City;
import vip.com.vipmeetings.models.EvacuationResponsible;
import vip.com.vipmeetings.models.TimerModel;
import vip.com.vipmeetings.utilities.Constants;
import vip.com.vipmeetings.utilities.TrustManagerManipulator;

public class MainBaseActivity extends AppCompatActivity {

    public static final int REQUEST_PHONE_CALL = 1111;
    public static final int REQUESt_GALLERY = 1112;
    public static final int REQUEST_CAPTURE = 1113;
    public static final int REQUEST_CAMEAR = 1114;
    boolean isPatient = false;
    public SharedPreferences mSharedPreferences;
    public SharedPreferences.OnSharedPreferenceChangeListener listener;
    TimerBroadcastReceiver timerBroadcastReceiver;
    public GoogleApiClient mGoogleApiClient;
    ConnectionFailed connectionFailed;
    ConnectionCallback connectionCallback;
    IGoogleApiClient iGoogleApiClient;
    public Long countDown = 45 * 60 * 1000L;
    public EventBus eventBus;
    public Gson mGson;
    public ConnectivityManager connectivityManager;
    public InputMethodManager inputMethodManager;
    LocationRequest mLocationRequest;
    LocationSettingsRequest mLocationSettingsRequest;
    ITokenRefresh iTokenRefresh;
    OkHttpClient client;
    boolean isReached;
    ProgressDialog pd;
    public Retrofit retrofit;
    public LoginInterface service;
    public MainApi mainApi;
    public ContactsApi contactsApi;
    public EvacuationResponsible evacuationResponsible;
    public AuthenticateEvacuationModel authenticateEvacuationModel;

    HttpsURLConnection httpsURLConnection;
    HttpURLConnection httpURLConnection;
    TrustManagerManipulator trustManagerManipulator;

    String email;

    public SharedPreferences mSharedPreferencesLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGson = new Gson();
        eventBus = EventBus.getDefault();

        mSharedPreferencesLogin = getSharedPreferences(IpAddress.LOGIN, MODE_PRIVATE);
        mSharedPreferences = getSharedPreferences(Constants.SHAREDPREFE, MODE_PRIVATE);
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        timerBroadcastReceiver = new TimerBroadcastReceiver();
        connectionCallback = new ConnectionCallback();
        connectionFailed = new ConnectionFailed();
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                // Implementation
                IpAddress.e("SHARED KEY", key + "");
                Map<String, ?> allEntries = mSharedPreferences.getAll();
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    IpAddress.e("SHARED VALUES", entry.getKey() + ": " +
                            entry.getValue().toString());
                }
            }
        };
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public static boolean isValidPhone(String target) {
        return  phoneRegexValidation(target);
    }
    public static boolean phoneRegexValidation(String s)
    {

        // The given argument to compile() method
        // is regular expression. With the help of
        // regular expression we can validate mobile
        // number.
        // The number should be of 10 digits.
        String pattern = "(\\(?([\\d \\-\\)\\–\\+\\/\\(]+)\\)?([ .\\-–\\/]?)([\\d]+))";
        // Creating a Pattern class object
        Pattern p = Pattern.compile(pattern);

        // Pattern class contains matcher() method
        // to find matching between given number
        // and regular expression for which
        // object of Matcher class is created
        Matcher m = p.matcher(s);

        // Returning boolean value
        return (m.matches());
    }
    public boolean isApisorEqual() {

//        return getApp().getTcpIp() != null &&
//                getApp().getTcpIp().trim().length() > 1 &&
//                getApp().getTcpIp().trim()
//                        .substring(0, getApp().getTcpIp().trim().length() - 1)
//                        .equalsIgnoreCase(Constants.IPADDRESS);

        return getApp().isReached() && getApp().getTcpIp() != null
                && (getApp().getTcpIp().trim()
                .substring(0, getApp().getTcpIp().trim().length() - 1)
                .equalsIgnoreCase(Constants.IPADDRESS));

    }

    public void setFabricIdentifier(String clientID) {


    }

    public void setFabricEmail(String email) {


    }

    public void setFabricOTP(String otp) {


    }


    public VIPmeetings getApp() {

        return (VIPmeetings) getApplication();
    }


    public void showPD() {


        if (pd != null && pd.isShowing()) {
            //  pd.dismiss();
        } else if (pd == null) {
            createPD(null);
        }
        if (!isFinishing() && pd != null && !pd.isShowing()) {
            pd.show();
        }

    }


    public void showPD(Activity activity) {

        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        } else if (pd == null) {
            createPD(activity);
        }
        if (!isFinishing() && pd != null && !pd.isShowing())
            pd.show();
    }

    private void createPD(Activity activity) {

        if (pd == null && !isFinishing()) {
            if (activity != null) {
                pd = new ProgressDialog(activity, R.style.MyTheme);
            } else {
                pd = new ProgressDialog(this, R.style.MyTheme);
            }
            pd.setCancelable(false);
            pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        }
    }

    public void showMessage(String string) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(string!=null)
            builder.setMessage(string);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    public void showMessage(String title,String string) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(string);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }


    public void hidePD() {


        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {

        }
    }


    public String getEmail() {

        return mSharedPreferences.getString(Constants.EMAIL, null);

    }

    public void storeIsPatient(boolean isPatient) {


        mSharedPreferences.edit().putBoolean(Constants.ISPATIENT, isPatient).apply();
    }

    public boolean isPatient() {


        return mSharedPreferences.getBoolean(Constants.ISPATIENT, false);
    }

    public String getIsPatient() {


        if (mSharedPreferences.getBoolean(Constants.ISPATIENT, false)) {

            IpAddress.e("ispatient", "PATIENT");
            return "PATIENT";

        } else {
            IpAddress.e("ispatient", "VISITOR");
            return "VISITOR";
        }
    }


    public String getClientID() {
        return mSharedPreferences.getString(Constants.ClientID, null);
    }


    public String getBarcode() {


        return mSharedPreferences.getString(Constants.BARCODE, null);
    }

    public void setCrashlytics() {

        if (getBarcode() != null) {

        }
        if (getEmail() != null) {

        }
    }

    public void unregis(Activity activity) {

        if (eventBus != null && eventBus.isRegistered(activity))
            eventBus.unregister(activity);
    }


    public void regis(Activity activity) {

        if (eventBus != null && !eventBus.isRegistered(activity))
            eventBus.register(activity);
    }


    public boolean isValidLatLng(double lat, double lng) {
        if (lat < -90 || lat > 90) {
            return false;
        } else if (lng < -180 || lng > 180) {
            return false;
        }
        return true;
    }

    public void setRetrofit(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .client(setOKHTTPBeforeLogin())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(baseUrl + "/")
                .build();
    }


    public void colorNavigationBarBottom(boolean isshow, Window window, Activity activity) {

        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                if (isshow) {
                    window.setNavigationBarColor(ContextCompat.getColor(activity,
                            R.color.color_redd));
                } else {

                    window.setNavigationBarColor
                            (ContextCompat.getColor(activity,
                                    R.color.color_gradient_top));
                }
            }
        } catch (Exception e) {

        }
    }

    public void colorStatusBar(boolean isshow, Window window, Activity activity) {

        try {

            if (isshow) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(ContextCompat.getColor(activity, R.color.color_redd));

                } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(ContextCompat.getColor(activity, R.color.md_black_1000));
                } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                    // clear FLAG_TRANSLUCENT_STATUS flag:
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }
            }
        } catch (Exception e) {

        }
    }

    public void colorStatusBar(boolean isshow, Window window, Activity activity, boolean isHome) {

        try {

            if (isshow) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(ContextCompat.getColor(activity, R.color.color_redd));

                } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(ContextCompat.getColor(activity, R.color.md_white_1000));
                } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                    // clear FLAG_TRANSLUCENT_STATUS flag:
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }
            }
        } catch (Exception e) {

        }
    }

    public void colorStatusBar(boolean isshow, Window window, Activity activity, int colorCode) {

        try {

            if (isshow) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(ContextCompat.getColor(activity, R.color.color_redd));

                } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(colorCode);
                } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {

                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }
            }
        } catch (Exception e) {

        }
    }

    public Boolean loadInBackground() {


        isReached = false;
        String ip = getApp().getTcpIp();
        if (ip == null) {
            setReached(isReached);
            isReached = false;
            return false;
        }
        try {
            IpAddress.e("url", ip + "");
            if (ip.endsWith("/")) {
                ip = ip.substring(0, ip.length() - 1);
            }
            IpAddress.e("url", ip + "");
            if (ip.trim().length() > 0) {
                if (ip.contains("https")) {
                    if (trustManagerManipulator == null) {
                        trustManagerManipulator = new TrustManagerManipulator();
                        trustManagerManipulator.allowAllSSL();
                    }
                    if (httpsURLConnection == null) {
                        httpsURLConnection = (HttpsURLConnection) new URL(ip).openConnection();
                        httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String hostname, SSLSession session) {
                                return true;
                            }
                        });
                        httpsURLConnection.setSSLSocketFactory(setCertificate().getSocketFactory());
                        httpsURLConnection.setConnectTimeout(IpAddress.TIMEOUT);
                        httpsURLConnection.setReadTimeout(IpAddress.TIMEOUT);
                        httpsURLConnection.setDoInput(true);
                        httpsURLConnection.setDoOutput(true);

                    }
                    httpsURLConnection.connect();
                    isReached = true;
                    getApp().setStaticReached(isReached);
                } else if (ip.contains("http")) {

                    if (httpURLConnection == null) {
                        httpURLConnection = (HttpURLConnection) new URL(ip).openConnection();
                        httpURLConnection.setConnectTimeout(IpAddress.TIMEOUT);
                        httpURLConnection.setReadTimeout(IpAddress.TIMEOUT);
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);
                    }
                    httpURLConnection.connect();
                    isReached = true;
                    getApp().setStaticReached(isReached);
                } else {
                    isReached = false;
                    getApp().setStaticReached(isReached);
                }
            } else {
                isReached = false;
                setReached(isReached);
            }
        } catch (IOException io) {
            IpAddress.e(IpAddress.ERROR, io.toString());
            isReached = false;
            getApp().setStaticReached(isReached);
        } catch (Exception e) {
            IpAddress.e(IpAddress.ERROR, e.toString());
            isReached = false;
            getApp().setStaticReached(isReached);
        }
        setReached(isReached);
        if (email != null) {


        } else if (getEmail() != null && getClientID() != null)


            logSend("OTP REQUESTING2", "OTPCALLWITHEMAIL", "SUCCESS_2" + isReached,
                    "ISREACHED", String.valueOf(isReached));
        return isReached;
    }

    public void logSend(String key, String attribute1, String attribute2) {

    }

    public void logSend(String key, String attribute1, String attribute2, String att3, String attr4) {

        if (key != null && attribute1 != null && attribute2 != null && att3 != null && attr4 != null)
        {

        }

    }


    public void setReached(boolean isReached) {

        // getApp().isReached()

        mSharedPreferences.edit().putBoolean(Constants.ISREACHED, isReached).apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        regis(this);
    }

    public Retrofit provideRetrofit(String MAP_URL) {


        return new Retrofit.Builder().
                client(setOKHTTPBeforeLogin())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(MAP_URL + "/").build();
    }

    public OkHttpClient setOKHTTPBeforeLogin() {

        setOKHTTPClient();
        return client;
    }

    public HttpLoggingInterceptor getLogging() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {

                IpAddress.e("okhttp", message);
                // logSend("OKHTTP","MESSAGE",message);
            }
        });

        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        return interceptor;
    }

    public OkHttpClient setOKHTTPAfterLogin() {


        try {

            if (BuildConfig.DEBUG) {
                client = addInterceptor()
                        //   .addInterceptor(new ChuckInterceptor(this))//raparthy
                        .addInterceptor(new ChuckerInterceptor(this))//raparthy

                        .sslSocketFactory(setCertificate().getSocketFactory(), getTrustManager())
                        .writeTimeout(0, TimeUnit.SECONDS)
                        .addNetworkInterceptor(new StethoInterceptor())
                        .readTimeout(0, TimeUnit.SECONDS)
                        .connectTimeout(0, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .addInterceptor(getLogging())
                        .build();

            } else

                client = addInterceptor()
                        .sslSocketFactory(setCertificate().getSocketFactory(), getTrustManager())
                        .writeTimeout(0, TimeUnit.SECONDS)
                        .readTimeout(0, TimeUnit.SECONDS)
                        .connectTimeout(0, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .addInterceptor(getLogging())
                        .build();
        } catch (Exception e) {
            IpAddress.e("errorssl", e.toString() + "");
            client = addInterceptor()
                    .writeTimeout(0, TimeUnit.SECONDS)
                    .readTimeout(0, TimeUnit.SECONDS)
                    .connectTimeout(0, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(getLogging())
                    .build();
        }

        return client;
    }


    public OkHttpClient.Builder addInterceptor() {

        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();

        httpClient.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        if (getClientID() != null && getClientID().trim().length() > 0) {

            httpClient.addInterceptor(chain -> {
                Request original = chain.request();

                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl
                        .newBuilder()
                        .addEncodedQueryParameter
                                (IpAddress.CLIENTID, getClientID())
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original
                        .newBuilder()
                        .url(url);

                Request request = requestBuilder
                        .build();
                return chain.proceed(request);
            });
        }

        return httpClient;
    }


    public SSLContext setCertificate() {

        SSLContext sc = null;
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {

                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }

                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                            //No need to implement.
                        }

                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                            //No need to implement.
                        }
                    }
            };
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            return sc;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public X509TrustManager getTrustManager() {
        X509TrustManager trustManager = null;
        try {
            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" +
                        Arrays.toString(trustManagers));
            }
            trustManager = (X509TrustManager) trustManagers[0];
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return trustManager;
    }


    private void setOKHTTPClient() {


        try {

            if (BuildConfig.DEBUG) {
                client = new
                        OkHttpClient.Builder()
//                    .hostnameVerifier(new HostnameVerifier() {
//                        @Override
//                        public boolean verify(String hostname, SSLSession session) {
//                            return true;
//                        }
//                    })
                        .addInterceptor(getLogging())
                        //  .addInterceptor(new ChuckInterceptor(this))//raparthy
                        .addInterceptor(new ChuckerInterceptor(this))//raparthy

                        .addNetworkInterceptor(new StethoInterceptor())
                        .sslSocketFactory(setCertificate().getSocketFactory(), getTrustManager())
                        .writeTimeout(0, TimeUnit.SECONDS)
                        .readTimeout(0, TimeUnit.SECONDS)
                        .connectTimeout(0, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .build();

            } else
                client = addInterceptor()
//                    .hostnameVerifier(new HostnameVerifier() {
//                        @Override
//                        public boolean verify(String hostname, SSLSession session) {
//                            return true;
//                        }
//                    })
                        .addInterceptor(getLogging())
                        .sslSocketFactory(setCertificate().getSocketFactory(), getTrustManager())
                        .writeTimeout(0, TimeUnit.SECONDS)
                        .readTimeout(0, TimeUnit.SECONDS)
                        .connectTimeout(0, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .build();
        } catch (Exception e) {
            IpAddress.e("errorssl", e.toString() + "");
            client = new
                    OkHttpClient.Builder()
//                    .hostnameVerifier(new HostnameVerifier() {
//                        @Override
//                        public boolean verify(String hostname, SSLSession session) {
//                            return true;
//                        }
//                    })
                    .addInterceptor(getLogging())
                    .writeTimeout(0, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .readTimeout(0, TimeUnit.SECONDS)
                    .connectTimeout(0, TimeUnit.SECONDS)
                    .build();
        }
    }


    public City getStoreCitySelect() {

        if (mGson == null) {
            mGson = new Gson();
        }
        mGson.serializeNulls();
        return mGson.fromJson(mSharedPreferences.getString(IpAddress.CITY_SELECTED,
                null), City.class);
    }


    public LocationRequest createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(IpAddress.PRIORITY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);

        return mLocationRequest;
    }


    public void logEvent() {
        // Answers.getInstance().
    }

    public void hideKeyboard(View v) {

        if (v != null && inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStop() {
        unregis(this);
        super.onStop();


    }

    public void deleteStiky(Activity activity) {

        try {
            eventBus.removeAllStickyEvents();
        } catch (Exception e) {

        }
    }

    public void showToast(String s) {

        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }


    public void setToken(Context context) {
        iTokenRefresh = (ITokenRefresh) context;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onTokenRefresh(TimerModel timerModel) {
        //IpAddress.e("TIMER", mGson.toJson(timerModel));

        if (iTokenRefresh != null) {

            if (timerModel.isRefreshMobile()) {

                if (BuildConfig.DEBUG) {
                    showToast("Token Updated");

                } else
                    deleteStiky(this);

                iTokenRefresh.onMobileRefresh();
            }

            if (timerModel.isRefreshEvacuation()) {

                if (BuildConfig.DEBUG) {
                    showToast("Token Updated");

                } else
                    deleteStiky(this);

                iTokenRefresh.onEvacuationRefresh();
            }
        }

        if (BuildConfig.DEBUG) {
            if (timerModel.isRefreshEvacuation() || timerModel.isRefreshMobile()) {


            } else
                deleteStiky(this);
        } else {
            deleteStiky(this);
        }
    }

    public boolean hasNetwork() {


        return connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isAvailable() &&
                (connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting() ||
                        connectivityManager.getActiveNetworkInfo().isConnected());
    }

    public void setClientCallbacks(Context context) {

        iGoogleApiClient = (IGoogleApiClient) context;
    }

    protected synchronized void buildGoogleApiClient(
            GoogleApiClient.ConnectionCallbacks callbacks,
            GoogleApiClient.OnConnectionFailedListener failureListener
    ) {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(callbacks)
                    .addOnConnectionFailedListener(failureListener)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    public class ConnectionFailed implements GoogleApiClient.OnConnectionFailedListener {


        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


            iGoogleApiClient.onFailed();

        }
    }


    public class ConnectionCallback implements GoogleApiClient.ConnectionCallbacks {


        @Override
        public void onConnected(@Nullable Bundle bundle) {


            if (iGoogleApiClient != null) {
                iGoogleApiClient.onConnected();
            }

        }

        @Override
        public void onConnectionSuspended(int i) {

            if (iGoogleApiClient != null)
                iGoogleApiClient.onSuspended();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (timerBroadcastReceiver != null) {

//                LocalBroadcastManager.getInstance(this).
//                        registerReceiver(timerBroadcastReceiver, new IntentFilter(BroadcastService.
//                                COUNTDOWN_BR));

                // registerReceiver(timerBroadcastReceiver, new IntentFilter(BroadcastService.COUNTDOWN_BR));

            }
            if (listener != null) {
                mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
            }
            //refresh All notificationscreens
            if (!isFinishing())
                eventBus.post(IpAddress.UPDATE);
        } catch (Exception e) {
            IpAddress.e(getLocalClassName(), e.toString());
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (timerBroadcastReceiver != null) {
                // unregisterReceiver(timerBroadcastReceiver);
//                LocalBroadcastManager.getInstance(this).
//                        unregisterReceiver(timerBroadcastReceiver);
            }
            if (listener != null)
                mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
        } catch (Exception e) {

            IpAddress.e(getLocalClassName(), e.toString());
        }
    }

    public class TimerBroadcastReceiver extends BroadcastReceiver {

        public TimerBroadcastReceiver() {


        }

        @Override
        public void onReceive(Context context, Intent intent) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    updateGUI(intent); // or whatever method used to update your GUI fields
                }
            });


        }
    }


    private void updateGUI(Intent intent) {

        if (intent != null && intent.hasExtra("countdown")) {

            countDown = intent.getLongExtra("countdown", 0);
            //  Timber.d("TIMER" + countDown + "timer");
        }


    }


    public void hideSoft() {

        try {

            View view = this.getCurrentFocus();
            if (view != null && inputMethodManager != null) {

                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {

        }
    }
}

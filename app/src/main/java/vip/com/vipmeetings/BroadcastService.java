package vip.com.vipmeetings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

//import com.chuckerteam.chucker.api.ChuckerInterceptor;//raparthy
import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.facebook.stetho.okhttp3.StethoInterceptor;
//import com.readystatesoftware.chuck.ChuckInterceptor;//raparthy

import org.greenrobot.eventbus.EventBus;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import vip.com.vipmeetings.body.GetAuthTokenRequestBody;
import vip.com.vipmeetings.envelope.SoapAuthTokenRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapAuthTokenResponseEnvelope;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.interfaces.MainApi;
import vip.com.vipmeetings.models.TimerModel;
import vip.com.vipmeetings.request.GetAuthTokenData;
import vip.com.vipmeetings.utilities.Constants;
import vip.com.vipmeetings.utilities.TrustManagerManipulator;

public class BroadcastService extends JobIntentService {

    private final static String TAG = "BroadcastService";

    public static final String COUNTDOWN_BR = "vip.com.vipmeetings.Timer";
    Intent sendItent;
    public Retrofit retrofit;
    MainApi mainApi;
    CountDownTimer countDownTimer = null;
    OkHttpClient client;
    SharedPreferences mSharedPreferences;

    long miilisinFuture1 = 45 * 1000 * 60;
    long miilisinFuture;
    EventBus eventBus;

    public BroadcastService() {
        super();


    }


    private void onHandle() {


        mSharedPreferences = getSharedPreferences(Constants.SHAREDPREFE, MODE_PRIVATE);
        if (BuildConfig.DEBUG) {

            miilisinFuture1 = 1000 * 20;
            miilisinFuture = miilisinFuture1;
        } else {
            miilisinFuture1 = 45 * 1000 * 60;
            miilisinFuture = miilisinFuture1;
        }

        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

        try {
            Looper.prepare();
            countDownTimer = new CountDownTimer(miilisinFuture, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {


                    sendItent.putExtra("countdown", millisUntilFinished);

                    IpAddress.e("timer service", millisUntilFinished + "");
                    sendBroadcast(false, false, millisUntilFinished / 1000);

                }

                @Override
                public void onFinish() {

                    IpAddress.e(TAG, "Timer finished");
                    readToken();
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        countDownTimer.start();
                    }

                }
            };

            countDownTimer.start();
            Looper.loop();

        } catch (Exception e) {

        }
    }

    private void sendBroadcast(boolean isMobileRefresh, boolean isEvacuation, long time) {


        TimerModel timerModel = new TimerModel();
        timerModel.setRefreshMobile(isMobileRefresh);
        timerModel.setRefreshEvacuation(isEvacuation);
        timerModel.setTimeRemaining(time);
        eventBus.post(timerModel);
    }

    public static void enqueWork(Context context, Intent intent) {


        enqueueWork(context, BroadcastService.class, IpAddress.JOBID_BROADCAST, intent);
    }


    public String getTcpIpEvacuationFalse() {


        return Constants.IPADDRESS2;
    }

    public String getClientID() {
        return mSharedPreferences.getString(Constants.ClientID, null);
    }

    public OkHttpClient.Builder addInterceptor() {

        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();

//        httpClient.hostnameVerifier(new HostnameVerifier() {
//            @Override
//            public boolean verify(String hostname, SSLSession session) {
//                return true;
//            }
//        });
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

        return httpClient;
    }

    public SSLContext setCertificate() {

        SSLContext sc = null;
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {

                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
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


    public OkHttpClient setOKHTTPAfterLogin() {

        try {

            if (BuildConfig.DEBUG) {
                client = addInterceptor()
                        //.addInterceptor(new ChuckInterceptor(this))//raparthy
                        .addInterceptor(new ChuckerInterceptor(this))//raparthy

                        .addNetworkInterceptor(new StethoInterceptor())
                        .sslSocketFactory(setCertificate().getSocketFactory(), getTrustManager())
                        .writeTimeout(0, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .readTimeout(0, TimeUnit.SECONDS)
                        .connectTimeout(0, TimeUnit.SECONDS)
                        .addInterceptor(getLogging())
                        .build();
            } else

                client = addInterceptor()
                        .sslSocketFactory(setCertificate().getSocketFactory(), getTrustManager())
                        .writeTimeout(0, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .readTimeout(0, TimeUnit.SECONDS)
                        .connectTimeout(0, TimeUnit.SECONDS)
                        .addInterceptor(getLogging())
                        .build();
        } catch (Exception e) {
            IpAddress.e("errorssl", e.toString() + "");
            client = addInterceptor()
                    .writeTimeout(0, TimeUnit.SECONDS)
                    .readTimeout(0, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .connectTimeout(0, TimeUnit.SECONDS)
                    .addInterceptor(getLogging())
                    .build();
        }

        return client;
    }


    private boolean isURLValid() {

        try {

            URL url = new URL(getApp().getTcpIp());
            return isReached();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void getAuthToken_Mobile() {

        try {

            IpAddress.e(IpAddress.ERROR_TOKEN, "DIFFER");
            setOKHTTPAfterLogin();
            setRetrofitNoToken(getApp().getTcpIp());
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

            lClientDetailsObservable.
                    subscribeOn(Schedulers.newThread())
                    .subscribe(this::onToken_Mobile, this::onAuthError);
        } catch (Exception e) {


        }


    }


    public VIPmeetings getApp() {

        return (VIPmeetings) getApplication();
    }

    private void saveAuthTokenPref_Mobile(String authToken) {

        mSharedPreferences.edit().putString(IpAddress.AUTHTOKEN_MOBILE, authToken).apply();
    }

    private void onToken_Mobile(SoapAuthTokenResponseEnvelope getAuthTokenResponse) {

        saveAuthTokenPref_Mobile(getAuthTokenResponse.getBody().getGetAuthTokenResult().getGetAuthTokenResult());
        sendItent.putExtra("countdown", 0);
        sendBroadcast(true, true, 0);
    }

    private void saveAuthTokenPref_Evacuation(String authToken) {

        mSharedPreferences.edit().putString(IpAddress.AUTHTOKEN_EVACUATION, authToken).apply();
        TimerModel timerModel = new TimerModel();
        timerModel.setRefreshMobile(false);
        timerModel.setRefreshEvacuation(true);
        timerModel.setTimeRemaining(0);
        eventBus.post(timerModel);
    }


    public Boolean loadInBackground() {

        boolean isReached = false;

        String ip = mSharedPreferences.getString(Constants.CLIENT_TCPIP, null);

        if (ip == null) {


            return isReached;
        }

        try {
            if (ip != null) {

                if (ip.contains("https")) {
                    new TrustManagerManipulator().allowAllSSL();
                    HttpsURLConnection connection = (HttpsURLConnection) new URL(ip).openConnection();
                    connection.setSSLSocketFactory(setCertificate().getSocketFactory());
                    connection.setConnectTimeout(IpAddress.TIMEOUT);
                    connection.setReadTimeout(IpAddress.TIMEOUT);
                    connection.setDoOutput(true);
                    connection.connect();
                } else {
                    HttpURLConnection connection = (HttpURLConnection) new URL(ip).openConnection();
                    connection.setConnectTimeout(IpAddress.TIMEOUT);
                    connection.setReadTimeout(IpAddress.TIMEOUT);
                    connection.setDoOutput(true);
                    connection.connect();
                }

                isReached = true;
            }
        } catch (IOException io) {
            isReached = false;

        }
        setReached(isReached);
        return isReached;
    }

    public boolean isReached() {

        return mSharedPreferences.getBoolean(Constants.ISREACHED, false);
    }

    public void setReached(boolean isReached) {

        mSharedPreferences.edit().putBoolean(Constants.ISREACHED, isReached).apply();
    }

    public void updateUi(boolean isReached) {
        setReached(isReached);
        getAuthToken();
    }


    public void readToken() {

        Single.fromCallable(this::loadInBackground)
                .subscribeOn(Schedulers.io())
                .subscribe(this::updateUi, this::onError);


    }

    private void onError(Throwable throwable) {
    }

    public HttpLoggingInterceptor getLogging() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {

                IpAddress.e("okhttp", message);
            }
        });

        if (BuildConfig.DEBUG)
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        else
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

        return interceptor;
    }

    public OkHttpClient setOKHTTPBeforeLogin() {


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
                        //.addInterceptor(new ChuckInterceptor(this))//raparthy
                        .addInterceptor(new ChuckerInterceptor(this))//raparthy

                        .addNetworkInterceptor(new StethoInterceptor())
                        .sslSocketFactory(setCertificate().getSocketFactory(), getTrustManager())
                        .writeTimeout(0, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .readTimeout(0, TimeUnit.SECONDS)
                        .connectTimeout(0, TimeUnit.SECONDS)
                        .build();

            } else
                client = new
                        OkHttpClient.Builder()
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
                        .retryOnConnectionFailure(true)
                        .connectTimeout(0, TimeUnit.SECONDS)
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
                    .retryOnConnectionFailure(true)
                    .writeTimeout(0, TimeUnit.SECONDS)
                    .readTimeout(0, TimeUnit.SECONDS)
                    .connectTimeout(0, TimeUnit.SECONDS)
                    .build();
        }
        //  removeTrust();
        return client;
    }

    public void setRetrofitNoToken(String ip) {


        try {
            setOKHTTPAfterLogin();
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
        } catch (Exception e) {
            Constants.e("URL SET RETROFIT", e.toString());
        }

    }


    public void getAuthToken() {
        setRetrofitNoToken(Constants.IPADDRESS2);
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

        lClientDetailsObservable.
                subscribeOn(Schedulers.newThread())
                .subscribe(this::onToken, this::onAuthError);


    }

    private void onAuthError(Throwable throwable) {

        IpAddress.e(IpAddress.SERVICE, throwable.toString());

        TimerModel timerModel = new TimerModel();
        timerModel.setRefreshMobile(false);
        timerModel.setRefreshEvacuation(false);
        timerModel.setTimeRemaining(0);
        eventBus.post(timerModel);
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer.start();
        }
    }

    private void onToken(SoapAuthTokenResponseEnvelope getAuthTokenResponse) {


        saveAuthTokenPref_Evacuation(getAuthTokenResponse.getBody().
                getGetAuthTokenResult().getGetAuthTokenResult());

        if (getApp().isReached() &&
                getApp().getTcpIp() != null &&
                getApp().getTcpIp().trim().length() > 1 &&
                !(getApp().getTcpIp().trim()
                        .substring(0, getApp().getTcpIp().trim().length() - 1)
                        .equalsIgnoreCase(Constants.IPADDRESS))) {

            getAuthToken_Mobile();

        } else if (isApisorEqual()) {
            saveAuthTokenPref_Mobile(getAuthTokenResponse.getBody()
                    .getGetAuthTokenResult().getGetAuthTokenResult());
        } else {

        }


    }

    public boolean isApisorEqual() {

        return getApp().isReached() && getApp().getTcpIp() != null
                && (getApp().getTcpIp().trim()
                .substring(0, getApp().getTcpIp().trim().length() - 1)
                .equalsIgnoreCase(Constants.IPADDRESS));

    }

    @Override
    public void onDestroy() {

        try {
            countDownTimer.cancel();
            countDownTimer = null;
            IpAddress.e(TAG, "Timer cancelled");
            IpAddress.e("BROADCAST", "SERVICE");
        } catch (Exception e) {

        }
        super.onDestroy();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        sendItent = new Intent(COUNTDOWN_BR);
        eventBus = EventBus.getDefault();
        setOKHTTPAfterLogin();
        onHandle();

    }
}
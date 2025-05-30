package vip.com.vipmeetings.fcm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import androidx.annotation.NonNull;

import androidx.core.app.JobIntentService;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
//import com.readystatesoftware.chuck.ChuckInterceptor;//raparthy
import com.chuckerteam.chucker.api.ChuckerInterceptor;//raparthy

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import vip.com.vipmeetings.VIPmeetings;
import vip.com.vipmeetings.body.AddDeviceTokenRequestBody;
import vip.com.vipmeetings.body.AddDeviceTokenResponseBody;
import vip.com.vipmeetings.envelope.SoapAddDeviceTokenRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapAddDeviceTokenResponseEnvelope;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.interfaces.LoginInterface;
import vip.com.vipmeetings.request.AddDeviceToken;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 05/07/17.
 */

public class SendDeviceTokenService extends JobIntentService {


    Retrofit retrofit2;
    OkHttpClient client;
    SharedPreferences mSharedPreferences;
    Gson mGson;
    FirebaseInstanceId firebaseInstanceId;

    String placeId = "";
    Intent intent;

    public SendDeviceTokenService() {
        super();


    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        this.intent = intent;
        send(intent);
    }

    public static void enqueWork(Context context, Intent intent) {
        enqueueWork(context, SendDeviceTokenService.class, IpAddress.JOBID_DEVICETOKEN, intent);
    }


    private VIPmeetings getApp() {

        return (VIPmeetings) getApplicationContext();
    }


    public String getBarcode() {
        return mSharedPreferences.getString(Constants.BARCODE, null);
    }

    public String getClientID() {
        return mSharedPreferences.getString(Constants.ClientID, null);
    }


    private void send(Intent intent) {

        try {
            FirebaseMessaging.getInstance().setAutoInitEnabled(true);
            firebaseInstanceId = FirebaseInstanceId.getInstance();
            mSharedPreferences = getSharedPreferences(Constants.SHAREDPREFE, MODE_PRIVATE);
            mGson = new Gson();
            buildRetrofit2();
            placeId = mSharedPreferences.getString(IpAddress.PLACEID_NEW, "");
            firebaseInstanceId.getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {


                    SoapAddDeviceTokenRequestEnvelope soapAddDeviceTokenRequestEnvelope =
                            new SoapAddDeviceTokenRequestEnvelope();


                    AddDeviceTokenRequestBody addDeviceTokenRequestBody = new AddDeviceTokenRequestBody();
                    String android_id = Settings.Secure.getString(getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    AddDeviceToken addDeviceToken = new AddDeviceToken();
                    addDeviceToken.setA_sAndroidToken(instanceIdResult.getToken());
                    addDeviceToken.setA_sUDID(android_id);
                    addDeviceToken.setA_siOsDeviceToken("");
                    addDeviceToken.setAuthToken(getApp().getAuthTokenPref_Evacuation());
                    addDeviceToken.setA_sPlaceId(placeId);
                    addDeviceToken.setA_sAppBundleId(IpAddress.BUNDLEID);
                    addDeviceToken.setA_sClientId(getClientID());
                    addDeviceToken.setA_sBarcode(getBarcode());
                    addDeviceTokenRequestBody.setAddDeviceToken(addDeviceToken);
                    soapAddDeviceTokenRequestEnvelope.setBody(addDeviceTokenRequestBody);

                    LoginInterface service = retrofit2.create(LoginInterface.class);
                    Observable<SoapAddDeviceTokenResponseEnvelope> lClientDetailsObservable =
                            service.addDeviceToken(soapAddDeviceTokenRequestEnvelope);

                    lClientDetailsObservable.
                            subscribeOn(Schedulers.io())
                            .subscribe(SendDeviceTokenService.this::onDeviceToken,
                                    SendDeviceTokenService.this::onError);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


        } catch (Exception e) {

            IpAddress.e("onhandle", "error");
        }
    }


    @Override
    public void onDestroy() {

        IpAddress.e("DEVICETOKEN", "SERVICE");
        super.onDestroy();
    }

    private void onError(Throwable throwable) {

        Constants.e("error", throwable.toString());

        stopSelf();
    }


    private void onDeviceToken(SoapAddDeviceTokenResponseEnvelope soapAddDeviceTokenResponseEnvelope) {

        AddDeviceTokenResponseBody addDeviceTokenResponseBody = soapAddDeviceTokenResponseEnvelope.getBody();

        if (addDeviceTokenResponseBody != null
                && addDeviceTokenResponseBody.getAddDeviceTokenResponse().getAddDeviceTokenResult() != null) {

            String str = addDeviceTokenResponseBody.getAddDeviceTokenResponse().getAddDeviceTokenResult().get_string();

        }
        stopSelf();


    }

    private OkHttpClient setOKHTTPAfterLogin() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {

                IpAddress.e("okhttp", message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        try {

            client = addInterceptor()
                    //   .addInterceptor(new ChuckInterceptor(this))//raparthy
                    .addInterceptor(new ChuckerInterceptor(this))//raparthy

                    .sslSocketFactory(setCertificate().getSocketFactory(), getTrustManager())
                    .writeTimeout(0, TimeUnit.SECONDS)
                    .readTimeout(0, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .connectTimeout(0, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build();
        } catch (Exception e) {
            IpAddress.e("errorssl", e.toString() + "");
            client = addInterceptor()
                    //.addInterceptor(new ChuckInterceptor(this))//raparthy
                    .addInterceptor(new ChuckerInterceptor(this))//raparthy

                    .writeTimeout(0, TimeUnit.SECONDS)
                    .readTimeout(0, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .connectTimeout(0, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build();
        }

        return client;
    }


    public OkHttpClient.Builder addInterceptor() {

        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addEncodedQueryParameter
                                (IpAddress.CLIENTID, getClientID())
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        return httpClient;
    }


    public SSLContext setCertificate() {

        SSLContext sc = null;
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {

                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
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


    public Retrofit buildRetrofit2() {


        retrofit2 = new Retrofit.Builder()
                .baseUrl(Constants.IPADDRESS2)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(setOKHTTPAfterLogin())
                .build();

        return retrofit2;
    }


}

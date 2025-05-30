package vip.com.vipmeetings.evacuate;

import android.os.Bundle;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import vip.com.vipmeetings.MainBaseActivity;
import vip.com.vipmeetings.interfaces.LoginInterface;
import vip.com.vipmeetings.models.AuthenticateEvacuationModel;
import vip.com.vipmeetings.models.Employee;
import vip.com.vipmeetings.models.EvacuationResponsible;
import vip.com.vipmeetings.service.PushService;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 29/03/17.
 */

public class BaseActivity extends MainBaseActivity {



    PushService pushService;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getEvacuationResponsible();
        setOKHTTPAfterLogin();
        setRetrofit(getTcpIpEvacuationFalse());
        setService();

        try {
            pushService=getApp().getPushService();
            pushService.startPush();
        } catch (Exception e) {
            IpAddress.e("service", e.toString());
        }

    }


    public void setRetrofit(String ip) {
        retrofit = new Retrofit.Builder()
                .baseUrl(ip)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(setOKHTTPAfterLogin())
                .build();


    }

    public void setService() {
        service = retrofit.create(LoginInterface.class);
    }


    public AuthenticateEvacuationModel getAuthenticateEvacuationModel() {

        String evacuation = mSharedPreferences.getString(Constants.EVACUATION_AUTHENTICATE,
                null);
        AuthenticateEvacuationModel authenticateEvacuationModel =
                mGson.fromJson(evacuation, AuthenticateEvacuationModel.class);
        IpAddress.e("data_authenticate1", evacuation + "");
        return authenticateEvacuationModel;

    }


    public String getTcpIpEvacuationFalse() {


        return Constants.IPADDRESS2;
    }


    public String ordinal(int i) {
        String[] sufixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + sufixes[i % 10];

        }
    }


    public void setEvacuationModel(String evacuationModel) {


        mSharedPreferences.edit().putString(Constants.EVACUATION, evacuationModel).apply();
    }


    public Employee getEmp() {

        return mGson.fromJson(mSharedPreferences.getString(Constants.EMPLOY1,
                null), Employee.class);

    }

    public void getEvacuationResponsible() {

        authenticateEvacuationModel = getAuthenticateEvacuationModel();
        evacuationResponsible = mGson.fromJson(mSharedPreferences.getString(Constants.EVACUATION, null),
                EvacuationResponsible.class);
        if (authenticateEvacuationModel != null
                && authenticateEvacuationModel.getCLIENTID() != null
                && authenticateEvacuationModel.getBARCODE() != null) {

            IpAddress.e("LOGIN", "AUTH 1");

        } else if (evacuationResponsible != null) {

            if (getClientID() != null)
                evacuationResponsible.setClientId(getClientID());
            if (getBarcode() != null)
                evacuationResponsible.setBarcode(getBarcode());

            if (evacuationResponsible.getAdminID() == null ||
                    evacuationResponsible.getAdminID().trim().length() == 0) {
                evacuationResponsible.setUserType("N");
                IpAddress.e("LOGIN", "NORMAL");
            } else {
                IpAddress.e("LOGIN", "ADMIN");
                evacuationResponsible.setUserType("A");
            }

            IpAddress.e("LOGIN", "AUTH 2" + mGson.toJson(evacuationResponsible));
        } else if (getClientID() != null && getBarcode() != null) {

            IpAddress.e("LOGIN", "AUTH 3");

        } else {
            Employee employee = getEmp();
            if (employee != null && employee.getBARCODE() != null
                    && employee.getCLIENTID() != null) {

                IpAddress.e("LOGIN", "AUTH 4");

            }
        }

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
            sc.init(null, trustAllCerts, new SecureRandom());
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


    public void onError(Throwable throwable) {


        hidePD();


    }


    public String getAuthTokenPref_Evacuation() {

        return mSharedPreferences.getString(IpAddress.AUTHTOKEN_EVACUATION, "");
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


}

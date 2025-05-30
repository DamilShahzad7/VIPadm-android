package vip.com.vipmeetings.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.widget.Toast;

import com.google.gson.Gson;

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
import vip.com.vipmeetings.R;
import vip.com.vipmeetings.VIPmeetings;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.interfaces.ContactsApi;
import vip.com.vipmeetings.models.Employee;
import vip.com.vipmeetings.utilities.Constants;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Srinath on 13/06/17.
 */

public class BaseFragment extends Fragment {

    Retrofit retrofit;
    OkHttpClient client;
    SharedPreferences mSharedPreferences;
    ProgressDialog pd;
    Gson mGson;
    ContactsApi contactsApi;

    Employee employModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();
    }

    public String getMeetingId() {

        return mSharedPreferences.getString(Constants.MEETINGID, "");

    }


    public String getEmail() {

        return mSharedPreferences.getString(Constants.EMAIL, null);

    }

    public String getClientID() {
        return mSharedPreferences.getString(Constants.ClientID, "");
    }

    private void initialize() {

        mGson = new Gson();
        mSharedPreferences = getActivity().getSharedPreferences(Constants.SHAREDPREFE, MODE_PRIVATE);
        setOKHTTPAfterLogin();
        pd = new ProgressDialog(getActivity(), R.style.MyTheme);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        getEmpModel();
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
                        //  .addQueryParameter(IpAddress.AUTHTOKEN, getAuthTokenPref())
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
                    .sslSocketFactory(setCertificate().getSocketFactory(), getTrustManager())
                    .writeTimeout(0, TimeUnit.SECONDS)
                    .readTimeout(0, TimeUnit.SECONDS)
                    .connectTimeout(0, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build();
        } catch (Exception e) {
            IpAddress.e("errorssl", e.toString() + "");
            client = addInterceptor()
                    .writeTimeout(0, TimeUnit.SECONDS)
                    .readTimeout(0, TimeUnit.SECONDS)
                    .connectTimeout(0, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build();
        }

        return client;
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


//    private void setOKHttp() {
//
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        try {
//            client = new
//                    OkHttpClient.Builder()
//                    .sslSocketFactory(setCertificate().getSocketFactory(), getTrustManager())
//                    .writeTimeout(0, TimeUnit.SECONDS)
//                    .readTimeout(0, TimeUnit.SECONDS)
//                    .connectTimeout(0, TimeUnit.SECONDS)
//                    .addInterceptor(interceptor)
//                    .build();
//        }catch (Exception e)
//        {
//            IpAddress.e("errorssl",e.toString()+"");
//            client = new
//                    OkHttpClient.Builder()
//                    // .sslSocketFactory(setCertificate().getSocketFactory(), getTrustManager())
//                    .writeTimeout(0, TimeUnit.SECONDS)
//                    .readTimeout(0, TimeUnit.SECONDS)
//                    .connectTimeout(0, TimeUnit.SECONDS)
//                    .addInterceptor(interceptor)
//                    .build();
//        }
//    }


    public void getEmpModel() {
        if (mSharedPreferences.contains(Constants.EMPLOY1)) {
            employModel = mGson.fromJson(mSharedPreferences.getString(Constants.EMPLOY1, null),
                    Employee.class);
        }
    }

    public VIPmeetings getApp() {

        return (VIPmeetings) getActivity().getApplication();
    }


    public void showPD() {
        if (pd != null && !pd.isShowing()) {
            pd.show();
        }
    }

    public void showPD(Activity activity) {

        if (!isRemoving()) {
            if (pd == null) {
                pd = new ProgressDialog(activity, R.style.MyTheme);
                pd.setCancelable(false);
                pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            }
            if (pd != null && !pd.isShowing())
                pd.show();
        }
    }

    public void hidePD() {

        try {
            if (pd != null)
                pd.dismiss();
        } catch (Exception e) {

        }
    }

    public void setRetrofit(String ip) {

        retrofit = new Retrofit.Builder()
                .baseUrl(ip)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                // .addConverterFactory(JaxbConverterFactory.create())
                .client(setOKHTTPAfterLogin())
                .build();

    }

    public void onError(Throwable throwable) {


        hidePD();
        Constants.e("error", throwable.toString());

        Toast.makeText(getContext(), getString(R.string.someerror), Toast.LENGTH_SHORT).show();
    }
}

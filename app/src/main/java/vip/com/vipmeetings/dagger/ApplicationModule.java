package vip.com.vipmeetings.dagger;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import vip.com.vipmeetings.BuildConfig;
import vip.com.vipmeetings.VIPmeetings;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.utilities.Constants;

import static android.content.Context.MODE_PRIVATE;

@Module
public class ApplicationModule {

    private final VIPmeetings mApplication;

    public ApplicationModule(VIPmeetings app) {
        mApplication = app;
    }

    @Provides
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    Gson providesGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    SharedPreferences providesShared() {
        return mApplication.getSharedPreferences(Constants.SHAREDPREFE, MODE_PRIVATE);
    }


    public SSLContext setCertificate() {

        SSLContext sc = null;
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {

                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
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

    public String getClientID(SharedPreferences mSharedPreferences) {
        return mSharedPreferences.getString(Constants.ClientID, null);
    }


    public OkHttpClient.Builder addInterceptor(SharedPreferences mSharedPreferences) {

        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            HttpUrl originalHttpUrl = original.url();

            HttpUrl url = originalHttpUrl
                    .newBuilder()
                    .addEncodedQueryParameter
                            (IpAddress.CLIENTID, getClientID(mSharedPreferences))
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


    public class ForceCacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            builder.cacheControl(CacheControl.FORCE_CACHE);
            return chain.proceed(builder.build());
        }
    }


    public String getTcpIp(SharedPreferences mSharedPreferences) {


        IpAddress.e("VIPMEETINGS CLIENT_TCPIP", mSharedPreferences.getString(Constants.CLIENT_TCPIP, ""));

        return mSharedPreferences.getString(Constants.CLIENT_TCPIP, "") + "/";


    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient, SharedPreferences mSharedPreferences) {


        return new Retrofit.Builder().
                client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(getTcpIp(mSharedPreferences)).build();
    }


    @Singleton
    @Provides
    HttpLoggingInterceptor loggingInterceptor()

    {
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

    @Singleton
    @Provides
    OkHttpClient okHttpClient(HttpLoggingInterceptor interceptor) {


        try {
            return new
                    OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .sslSocketFactory(setCertificate().getSocketFactory(), getTrustManager())
                    .writeTimeout(0, TimeUnit.SECONDS)
                    .readTimeout(0, TimeUnit.SECONDS)
                    .connectTimeout(0, TimeUnit.SECONDS)
                    .build();
        } catch (Exception e) {
            IpAddress.e("errorssl", e.toString() + "");
            return new
                    OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .writeTimeout(0, TimeUnit.SECONDS)
                    .readTimeout(0, TimeUnit.SECONDS)
                    .connectTimeout(0, TimeUnit.SECONDS)
                    .build();
        }
    }


}

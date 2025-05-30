package vip.com.vipmeetings;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.multidex.MultiDexApplication;

import android.util.Log;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.jakewharton.threetenabp.AndroidThreeTen;
//import com.squareup.leakcanary.LeakCanary;//Raparthy Commented for leakage alerts in Mobile when installed with debug apk version

import java.io.IOException;
import java.net.SocketException;

import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import vip.com.vipmeetings.dagger.ApplicationComponent;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.service.PushService;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 30/05/17.
 */

public class VIPmeetings extends MultiDexApplication implements LifecycleObserver {


    SharedPreferences mSharedPreferences;

    boolean isContact = true;

    ApplicationComponent applicationComponent;
    private boolean mServiceBound;
    private PushService pushService;

    boolean isForeground = false;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        certificatePin();
     /*
     //Raparthy Commented for leakage alerts in Mobile when installed with debug apk version
      if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
            LeakCanary.install(this);
        }*/
        // Normal app init code...
        Fresco.initialize(this);
        mSharedPreferences = getSharedPreferences(Constants.SHAREDPREFE, MODE_PRIVATE);


        startPushService();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        errorHandler();


    }

    private void certificatePin() {

        CertificatePinner certificatePinner = new CertificatePinner.Builder()
                .add("*.vipadm.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
                .build();//https:// for vipadm.com  is removed by praveen for sdk31 updgrade
        OkHttpClient client = new OkHttpClient.Builder()
                .certificatePinner(certificatePinner)
                .build();
    }

    private void errorHandler() {

        RxJavaPlugins.setErrorHandler(e -> {
            if (e instanceof UndeliverableException) {
                e = e.getCause();
            }
            if ((e instanceof IOException) || (e instanceof SocketException)) {
                // fine, irrelevant network problem or API that throws on cancellation
                return;
            }
            if (e instanceof InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return;
            }
            if ((e instanceof NullPointerException) || (e instanceof IllegalArgumentException)) {
                // that's likely a bug in the application
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                return;
            }
            if (e instanceof IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().getUncaughtExceptionHandler()
                        .uncaughtException(Thread.currentThread(), e);
                return;
            }
            Log.e("RXJAVA", e.toString());
        });
    }


    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public void setApplicationComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }

    public String getAuthTokenPref_Evacuation() {

        return mSharedPreferences.getString(IpAddress.AUTHTOKEN_EVACUATION, "CC484588-C0B4-4777-B6C6-3F149728FC49");
    }

    public boolean isForeground() {
        return isForeground;
    }

    public void setForeground(boolean foreground) {
        isForeground = foreground;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        //App in background
        IpAddress.e("App", "BACKGROUND");
        isForeground = false;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {
        // App in foreground
        IpAddress.e("App", "FOREGROUND");
        isForeground = true;

    }


    public boolean isReached() {

        return mSharedPreferences.getBoolean(Constants.ISREACHED, false);
    }

    public String getTcpIp() {


        //mSharedPreferences.getString(Constants.CLIENT_TCPIP, null)
        IpAddress.e("VIPMEETINGS CLIENT_TCPIP", mSharedPreferences.getString(Constants.CLIENT_TCPIP, ""));

        if (BuildConfig.DEBUG) {
            // return "http://139.112.8.80/";
        }
        return mSharedPreferences.getString(Constants.CLIENT_TCPIP, "") + "/".trim();


    }


    public void setContactRequest(boolean flag) {
        isContact = flag;
    }

    public boolean isContactRequest() {

        return isContact;
    }

    public PushService getPushService() {
        return pushService;
    }

    public void startPushService() {
        Intent svc = new Intent(getApplicationContext(), PushService.class);
        // 1) start the service so onCreate/onStartCommand fire immediately
        startService(svc);
        // 2) then bind, so your ServiceConnection gets the Binder
        bindService(svc, mServiceConnection, Context.BIND_AUTO_CREATE);
    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PushService.PushBinder myBinder = (PushService.PushBinder) service;
            pushService = myBinder.getPushService();
            mServiceBound = true;
            Log.d("VIPmeetings", "PushService bound; initializing…");
            pushService.initializeData();

        }
    };

    public String isStaticReached;

//    public boolean isStaticReached() {
//
//        return isStaticReached!=null && isStaticReached.equalsIgnoreCase("true");
//
//    }

    public boolean isStaticReached() {

        return isStaticReached != null;

    }

    public void setStaticReached(boolean isReached) {

        isStaticReached = String.valueOf(isReached);
    }
}

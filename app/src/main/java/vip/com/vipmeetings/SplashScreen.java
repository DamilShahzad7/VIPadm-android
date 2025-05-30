package vip.com.vipmeetings;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vip.com.vipmeetings.evacuate.IpAddress;

/**
 * Created by Srinath on 29/05/17.
 */

public class SplashScreen extends BaseActivity {


    Intent service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Handle the splash screen transition.
       // SplashScreen splashScreen = SplashScreen.installSplashScreen(this);//raparthy
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_splash);
        if (hasNetwork()) {
            showPD();
            Single.fromCallable(this::loadInBackground)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::updateUi, this::onError);
        } else {
            showMessage(getString(R.string.nointernet));
        }

    }

    public void updateUi(boolean isReached) {


        getAuthToken(false);

        try {
            service = new Intent(this, BroadcastService.class);
            BroadcastService.enqueWork(getApplicationContext(), service);
        } catch (Exception e) {
            IpAddress.e(IpAddress.SERVICE, e.toString());
        }
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String token) {

        eventBus.removeAllStickyEvents();

        if (token != null) {
            if (token.equalsIgnoreCase(IpAddress.AUTHTOKEN)) {
                eventBus.unregister(this);
                if (getIntent() != null && getIntent().hasExtra(IpAddress.EVACUATION)) {
                    gotoIntent(true);
                } else {
                    gotoIntent(false);
                }
                getIntent().removeExtra(IpAddress.EVACUATION);
            } else if (token.equalsIgnoreCase(IpAddress.ERROR_TOKEN)) {
                showToast(getString(R.string.someerror));
            }
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        regis(this);
    }

    @Override
    protected void onStop() {
        unregis(this);
        super.onStop();


    }


}

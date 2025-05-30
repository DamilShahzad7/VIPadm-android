package vip.com.vipmeetings.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.HashMap;

import vip.com.vipmeetings.MainActivity;
import vip.com.vipmeetings.R;

/**
 * Created by admin on 27-Mar-16.
 */
public final class QRSplashScreen extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 2500;
    SessionManager session;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);
        if (name != null) {
            Intent mainIntent = new Intent(QRSplashScreen.this, QRCode_generate.class);
            startActivity(mainIntent);
            finish();
        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(QRSplashScreen.this, MainActivity.class);
                    QRSplashScreen.this.startActivity(mainIntent);
                    QRSplashScreen.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }


    }


}
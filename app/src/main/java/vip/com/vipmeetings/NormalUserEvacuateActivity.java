package vip.com.vipmeetings;

import android.os.Bundle;
import androidx.annotation.Nullable;

import butterknife.ButterKnife;

/**
 * Created by Srinath on 12/02/18.
 */

public class NormalUserEvacuateActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normaluser);
        ButterKnife.bind(this);
    }
}

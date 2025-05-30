package vip.com.vipmeetings;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 26/06/17.
 */

public class AddParticipantSplashActivity extends BaseActivity {


    @BindView(R.id.tvcontinue)
    TextView tvcontinue;

    @BindView(R.id.tvlater)
    TextView tvlater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addparticipantsplash);
        ButterKnife.bind(this);

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }

    @OnClick(R.id.tvcontinue)
    public void onContinue(View v) {


        Intent i = new Intent(this, AddParticipants.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra(Constants.DATETIMEMODEL, getIntent().getStringExtra(Constants.DATETIMEMODEL));
        startActivity(i);

    }


    @OnClick(R.id.tvlater)
    public void onLater(View v) {


        if (getStoreCitySelect() != null &&
                getStoreCitySelect().getUseCatering() != null
                &&
                getStoreCitySelect().getUseCatering().equalsIgnoreCase("True")) {
            Intent i = new Intent(this, OrderFoodActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra(Constants.DATETIMEMODEL, getIntent().getStringExtra(Constants.DATETIMEMODEL));
            startActivity(i);
        } else {
            Intent i = new Intent(this, SkipRoomActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra(Constants.DATETIMEMODEL, getIntent().getStringExtra(Constants.DATETIMEMODEL));
            startActivity(i);
        }


    }


}

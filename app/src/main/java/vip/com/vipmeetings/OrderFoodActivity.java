package vip.com.vipmeetings;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 30/05/17.
 */

public class OrderFoodActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderfood);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tvorder)
    public void onOrder(View v) {


    }


    @Override
    public void onBackPressed() {
       // super.onBackPressed();
    }

    @OnClick(R.id.tvcontinue)
    public void onContinue(View v) {

        Intent intent = new Intent(this, AddOrderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.DATETIMEMODEL,getIntent().getStringExtra(Constants.DATETIMEMODEL));
        startActivity(intent);

    }

    @OnClick(R.id.tvlater)
    public void onLater(View v) {

        Intent intent = new Intent(this, SkipRoomActivity.class);
        intent.putExtra(Constants.DATETIMEMODEL,getIntent().getStringExtra(Constants.DATETIMEMODEL));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

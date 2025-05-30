package vip.com.vipmeetings;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 03/07/17.
 */

public class BookSpecificTimerActivity extends BaseActivity implements TimePicker.OnTimeChangedListener {


    @BindView(R.id.lltime)
    LinearLayout lltime;

    @BindView(R.id.tvsubject)
    TextView tvsubject;

    @BindView(R.id.tvdate)
    TextView tvdate;

    @BindView(R.id.tvtimes)
    TextView tvtimes;

    @BindView(R.id.tvstarttime)
    TextView tvstarttime;

    @BindView(R.id.tvendtime)
    TextView tvendtime;

    @BindView(R.id.tvroomname)
    TextView tvroomname;

    @BindView(R.id.tvfloors)
    TextView tvfloors;

    @BindView(R.id.ivnext)
    ImageView ivnext;


    @BindView(R.id.tvseats)
    TextView tvseats;

    @BindView(R.id.ivroom)
    SimpleDraweeView ivroom;

    boolean isStartTime, isEndTime;

    int currentHour = -1, currentMinute = -1;

    int endHour = -1, endMinute = -1;

    @BindView(R.id.timepicker)
    TimePicker timePicker;


    DateTimeModel bookSpecificModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookspecificroomtime);
        ButterKnife.bind(this);
        bookSpecificModel = mGson.fromJson(getIntent().getStringExtra(Constants.BOOKROOMSPECIFIC),
                DateTimeModel.class);


        if (bookSpecificModel != null) {

            String text1 = "<font color=#64829a> Floor : " + "</font> <font color=#000000>" + bookSpecificModel.getFloorNumber() + "</font>";
            tvfloors.setText(Html.fromHtml(text1));
           // tvfloors.setText("Floor : " + bookSpecificModel.getFloorNumber());
            //tvseats.setText("Seats : " + bookSpecificModel.getCapacity());
            String text2 = "<font color=#64829a> Seats : " + "</font> <font color=#000000>" + bookSpecificModel.getCapacity() + "</font>";
            tvseats.setText(Html.fromHtml(text2));



            tvroomname.setText(bookSpecificModel.getRoomName());
            ivroom.setImageURI(bookSpecificModel.getImage1());
            tvsubject.setText(bookSpecificModel.getSubject());
            tvdate.setText(bookSpecificModel.getFormatDate());

            if (bookSpecificModel.getTimes() != null && bookSpecificModel.getTimes().length() > 0) {
                tvtimes.setText("Booked: " + bookSpecificModel.getTimes());
            } else {
                tvtimes.setText("Slots are not yet booked");
            }

            if (bookSpecificModel.isToday()) {


            } else {
                if (Build.VERSION.SDK_INT >= 23) {
                    timePicker.setHour(0);
                    timePicker.setMinute(0);
                } else {
                    timePicker.setCurrentHour(0);
                    timePicker.setCurrentMinute(0);
                }
            }

        }

        isStartTime = true;
        isEndTime = false;
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(this);
        getFirstTime();
    }

    @OnClick(R.id.root)
    public void onRoot(View v) {

        hideSoft();
    }


    @OnClick(R.id.tvback)
    public void onBack(View v) {
        onBackPressed();
    }


    @OnClick(R.id.tvstarttime)
    public void onStartTime(View v) {

        timePicker.setVisibility(View.VISIBLE);
        isStartTime = true;
        isEndTime = false;
        if (currentMinute != -1 && currentHour != -1) {

            try {

                if (Build.VERSION.SDK_INT >= 23) {
                    timePicker.setHour(currentHour);
                    timePicker.setMinute(currentMinute);
                } else {
                    timePicker.setCurrentHour(currentHour);
                    timePicker.setCurrentMinute(currentMinute);
                }
                tvendtime.setText("");
            } catch (Exception e) {

            }
        }

        changeBG();

    }


    @OnClick({R.id.tvnext, R.id.ivnext})
    public void onNext(View v) {
        if (endHour != -1 && endMinute != -1) {
            if (bookSpecificModel.getEndHour() > 0 && bookSpecificModel.getEndMinute() >= 0) {

                if (bookSpecificModel.getStartHour() >= 0 && bookSpecificModel.getStartMinute() >= 0) {

                    Intent i = new Intent(this, ConfirmationActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra(Constants.DATETIMEMODEL, mGson.toJson(bookSpecificModel));
                    startActivity(i);

                } else {
                    showToast(getString(R.string.someerror));
                }
            } else {
                showToast(getString(R.string.someerror));
            }
        } else {
            showToast("Select end time");
        }
    }


    private void changeBG() {


        if (isStartTime) {

            tvstarttime.setBackgroundResource(R.drawable.recttvet);
            tvendtime.setBackgroundResource(R.drawable.recttime);

        } else if (isEndTime) {
            tvendtime.setBackgroundResource(R.drawable.recttvet);
            tvstarttime.setBackgroundResource(R.drawable.recttime);
        }
    }

    @OnClick(R.id.tvendtime)
    public void onEndTime(View v) {

        timePicker.setVisibility(View.VISIBLE);
        isStartTime = false;
        isEndTime = true;
        if (currentHour != -1 && currentMinute != -1) {

            try {
                if (Build.VERSION.SDK_INT >= 23) {

                    if (currentHour <= 23) {
                        timePicker.setHour(currentHour + 1);
                        timePicker.setMinute(currentMinute);
                    } else {


                    }
                } else {
                    timePicker.setCurrentHour(currentHour + 1);
                    timePicker.setCurrentMinute(currentMinute);
                }
            } catch (Exception e) {

            }
        }

        changeBG();

    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int i, int i1) {


        if (isStartTime) {
            getFirstTime();
        } else if (isEndTime) {


            if (Build.VERSION.SDK_INT >= 23) {

                if (timePicker.getHour() >= (currentHour + 1)) {

                    endHour = timePicker.getHour();
                    endMinute = timePicker.getMinute();
                    bookSpecificModel.setEndHour(endHour);
                    bookSpecificModel.setEndMinute(endMinute);

                    setTime(String.format("%02d", endHour) + "." +
                            String.format("%02d", endMinute));

                } else {

                    endHour = -1;
                    endMinute = -1;
                    bookSpecificModel.setEndHour(0);
                    bookSpecificModel.setEndMinute(0);
                    tvendtime.setText("");
                }
            } else {

                if (timePicker.getCurrentHour() >= (currentHour + 1)) {
                    endHour = timePicker.getCurrentHour();
                    endMinute = timePicker.getCurrentMinute();
                    bookSpecificModel.setEndHour(endHour);
                    bookSpecificModel.setEndMinute(endMinute);
                    setTime(String.format("%02d", endHour) + "." +
                            String.format("%02d", endMinute));

                } else {

                    endHour = -1;
                    endMinute = -1;
                    bookSpecificModel.setEndHour(0);
                    bookSpecificModel.setEndMinute(0);
                    tvendtime.setText("");

                }
            }

            ((TextView) findViewById(R.id.tvnext)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tvnext)).setText("Book");

        }
    }

    private void getFirstTime() {

        boolean flag = true;
        Calendar datetime = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        if (bookSpecificModel.isToday()) {
            if (Build.VERSION.SDK_INT >= 23) {

                int currentHour = timePicker.getHour();
                int currentMinute = timePicker.getMinute();
                datetime.set(Calendar.HOUR_OF_DAY, currentHour);
                datetime.set(Calendar.MINUTE, currentMinute);
            } else {
                int currentHour = timePicker.getCurrentHour();
                int currentMinute = timePicker.getCurrentMinute();
                datetime.set(Calendar.HOUR_OF_DAY, currentHour);
                datetime.set(Calendar.MINUTE, currentMinute);
            }
            if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
                //it's after current
                flag = true;
            } else {
                flag = false;
            }
        }

        if (flag) {

            if (Build.VERSION.SDK_INT >= 23) {


                currentHour = timePicker.getHour();
                currentMinute = timePicker.getMinute();

                bookSpecificModel.setStartHour(currentHour);
                bookSpecificModel.setStartMinute(currentMinute);
                setTime(String.format("%02d", currentHour) + "." +
                        String.format("%02d", currentMinute));
            } else {
                currentHour = timePicker.getCurrentHour();
                currentMinute = timePicker.getCurrentMinute();
                bookSpecificModel.setStartHour(currentHour);
                bookSpecificModel.setStartMinute(currentMinute);

                setTime(String.format("%02d", currentHour) + "." +
                        String.format("%02d", currentMinute));
            }
        }


    }

    private void setTime(String s) {


        if (isStartTime) {

            tvstarttime.setText(s);

        } else {

            if(endHour!=-1 && endMinute!=-1) {

                tvendtime.setText(s);
            }else
            {
                tvendtime.setText("");
            }

        }
    }


}

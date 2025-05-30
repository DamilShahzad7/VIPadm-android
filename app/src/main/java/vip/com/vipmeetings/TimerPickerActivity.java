package vip.com.vipmeetings;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vip.com.vipmeetings.interfaces.OnTimeSelected;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 29/05/17.
 */

public class TimerPickerActivity extends BaseActivity implements TimePicker.OnTimeChangedListener,
        OnTimeSelected {


    @BindView(R.id.tvdate)
    TextView tvdate;

    @BindView(R.id.tvsubject)
    TextView tvsubject;

    @BindView(R.id.timepicker)
    TimePicker timePicker;

    @BindView(R.id.tvstarttime)
    TextView tvstarttime;

    @BindView(R.id.tvendtime)
    TextView tvendtime;

    public boolean isStartTime;
    public boolean isEndTime;

    public int currentHour = -1;
    public int currentMinute = -1;

    public int endHour = -1;
    public int endMinute = -1;

    Intent intent;
    DateTimeModel dateTimeModel;
    @BindView(R.id.fltime)
    FrameLayout fltime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timepicker);
        ButterKnife.bind(this);

        Calendar nextYear = Calendar.getInstance();
        intent = getIntent();

        dateTimeModel = mGson.fromJson(intent.getStringExtra(Constants.DATETIMEMODEL), DateTimeModel.class);
        if (dateTimeModel != null) {

            if (dateTimeModel.isToday()) {

            } else {
                if (Build.VERSION.SDK_INT >= 23) {


                    timePicker.setHour(0);
                    timePicker.setMinute(0);

                } else {
                    timePicker.setCurrentHour(0);
                    timePicker.setCurrentMinute(0);
                }
            }

            tvdate.setText(dateTimeModel.getDateString());
            tvsubject.setText(dateTimeModel.getSubject());
        }
        isStartTime = true;
        isEndTime = false;
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(this);
        getFirstTime();
    }


    @OnClick(R.id.ivhome)
    public void onHome(View v) {

        gotoHome();

    }

    @OnClick(R.id.tvback)
    public void onBack(View v) {

        onBackPressed();
    }

    @OnClick(R.id.tvstarttime)
    public void onStartTime(View v) {

        isStartTime = true;
        isEndTime = false;
        if (currentMinute != -1 && currentHour != -1) {

            if (Build.VERSION.SDK_INT >= 23) {
                timePicker.setHour(currentHour);
                timePicker.setMinute(currentMinute);
            } else {
                timePicker.setCurrentHour(currentHour);
                timePicker.setCurrentMinute(currentMinute);
            }

            tvendtime.setText("");
            endMinute = -1;
            endHour = -1;
        }


        changeBG();
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

    @OnClick(R.id.tvnext)
    public void onNext(View v) {

        if (endHour != -1 && endMinute != -1) {

            if (dateTimeModel.isInvite()) {

                bookRoom2();
            } else if (getIntent().hasExtra(Constants.BOOKROOM)) {
                if ((getStoreCitySelect() != null
                        && (getStoreCitySelect().getSkipRoom() != null
                        && getStoreCitySelect().getSkipRoom().equalsIgnoreCase("True"))

                        || (getStoreCitySelect().getRoomCount() == null
                        || getStoreCitySelect().getRoomCount().equalsIgnoreCase("0")))) {

                    bookRoom2();
                } else {

                    roomList();
                }

            } else {
                bookRoom2();
            }
        } else {
            showToast("Please select end time");
        }
    }

    private void bookRoom2() {

        Intent i = new Intent(this, ConfirmationActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        dateTimeModel.setStartHour(currentHour);
        dateTimeModel.setEndHour(endHour);
        dateTimeModel.setStartMinute(currentMinute);
        dateTimeModel.setEndMinute(endMinute);
        if (getIntent().hasExtra(Constants.BOOKROOM))
            i.putExtra(Constants.BOOKROOM, getIntent().getStringExtra(Constants.BOOKROOM));
        i.putExtra(Constants.DATETIMEMODEL, mGson.toJson(dateTimeModel));
        startActivity(i);
    }


    public void roomList() {

        //skiproom no
        if (getStoreCitySelect() != null
                && getStoreCitySelect().getSkipRoom().equalsIgnoreCase("False")) {
            dateTimeModel.setStartHour(currentHour);
            dateTimeModel.setEndHour(endHour);
            dateTimeModel.setStartMinute(currentMinute);
            dateTimeModel.setEndMinute(endMinute);

            setStarttime(String.format("%02d", currentHour) + "." +
                    String.format("%02d", currentMinute));
            Intent i = new Intent(this, RoomListActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            dateTimeModel.setBook(true);
            i.putExtra(Constants.DATETIMEMODEL, mGson.toJson(dateTimeModel));
            startActivity(i);
        } else if (getStoreCitySelect().getSkipRoom().equalsIgnoreCase("True")) {


        }
    }


    @OnClick(R.id.tvendtime)
    public void onEndTime(View v) {

        isStartTime = false;
        isEndTime = true;
        if (currentHour != -1 && currentMinute != -1) {

            endHour = currentHour + 1;
            endMinute = currentMinute;

            // customTimePickerFragment.setEndTime(endHour,endMinute);

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
        }
        changeBG();

    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int i, int i1) {

        if (isStartTime) {

            getFirstTime(i);
        } else if (isEndTime) {


            if (Build.VERSION.SDK_INT >= 23) {

                if (timePicker.getHour() >= (currentHour + 1)) {

                    endHour = timePicker.getHour();
                  //  endHour = i;
                    endMinute = timePicker.getMinute();

                    dateTimeModel.setEndHour(endHour);
                    dateTimeModel.setEndMinute(endMinute);
                    setTime(String.format("%02d", timePicker.getHour()) + "." +
                            String.format("%02d", timePicker.getMinute()));
                } else {


                    endHour = -1;
                    endMinute = -1;
                    dateTimeModel.setEndHour(0);
                    dateTimeModel.setEndMinute(0);
                    tvendtime.setText("");

                }
            } else {

                if (timePicker.getCurrentHour() >= (currentHour + 1)) {
                    endHour = timePicker.getCurrentHour();
                   // endHour = i;
                    endMinute = timePicker.getCurrentMinute();
                    dateTimeModel.setEndHour(endHour);
                    dateTimeModel.setEndMinute(endMinute);
                    setTime(String.format("%02d", timePicker.getCurrentHour()) + "." +
                            String.format("%02d", timePicker.getCurrentMinute()));
                } else {
                    endHour = -1;
                    endMinute = -1;
                    dateTimeModel.setEndHour(0);
                    dateTimeModel.setEndMinute(0);
                    tvendtime.setText("");
                }
            }


        }
    }

    private void getFirstTime() {

        boolean flag=true;
        Calendar datetime = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        if (dateTimeModel.isToday()) {
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
                flag=true;
            } else {
                flag=false;
            }
        }

        if(flag) {


            if (Build.VERSION.SDK_INT >= 23) {

                currentHour = timePicker.getHour();
                currentMinute = timePicker.getMinute();
                dateTimeModel.setStartHour(currentHour);
                dateTimeModel.setStartMinute(currentMinute);
                setTime(String.format("%02d", timePicker.getHour()) + "." +
                        String.format("%02d", timePicker.getMinute()));
            } else {
                currentHour = timePicker.getCurrentHour();
                currentMinute = timePicker.getCurrentMinute();
                dateTimeModel.setStartHour(currentHour);
                dateTimeModel.setStartMinute(currentMinute);
                setTime(String.format("%02d", timePicker.getCurrentHour()) + "." +
                        String.format("%02d", timePicker.getCurrentMinute()));
            }
        }
    }

    private void getFirstTime(int i) {

        boolean flag=true;
        Calendar datetime = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        if (dateTimeModel.isToday()) {
            if (Build.VERSION.SDK_INT >= 23) {

               int currentHour = timePicker.getHour();
              //  int currentHour = i;
                int currentMinute = timePicker.getMinute();
                datetime.set(Calendar.HOUR_OF_DAY, currentHour);
                datetime.set(Calendar.MINUTE, currentMinute);
            } else {
               int currentHour = timePicker.getCurrentHour();
             //   int currentHour =i;
                int currentMinute = timePicker.getCurrentMinute();
                datetime.set(Calendar.HOUR_OF_DAY, currentHour);
                datetime.set(Calendar.MINUTE, currentMinute);
            }
            if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
                //it's after current
                flag=true;
            } else {
                flag=false;
            }
        }

        if(flag) {


            if (Build.VERSION.SDK_INT >= 23) {

               currentHour = timePicker.getHour();
               //  currentHour = i;
                currentMinute = timePicker.getMinute();
                dateTimeModel.setStartHour(currentHour);
                dateTimeModel.setStartMinute(currentMinute);
                setTime(String.format("%02d", timePicker.getHour()) + "." +
                        String.format("%02d", timePicker.getMinute()));
            } else {
                currentHour = timePicker.getCurrentHour();
                //currentHour = i;
                currentMinute = timePicker.getCurrentMinute();
                dateTimeModel.setStartHour(currentHour);
                dateTimeModel.setStartMinute(currentMinute);
                setTime(String.format("%02d", timePicker.getCurrentHour()) + "." +
                        String.format("%02d", timePicker.getCurrentMinute()));
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

    @Override
    public void onMinute(String hour, String minute) {

        try {

            if (isStartTime) {

                currentMinute = Integer.parseInt(minute);
                tvstarttime.setText(hour + "." + minute);

            } else {
                endMinute = Integer.parseInt(minute);
                tvendtime.setText(hour + "." + minute);

            }
        } catch (Exception e) {
            Constants.e("err0", e.toString());
        }
    }

    @Override
    public void onHour(String hour, String minute) {


        try {

            if (isStartTime) {

                currentMinute = Integer.parseInt(minute);
                currentHour = Integer.parseInt(hour);
                tvstarttime.setText(hour + "." + minute);
            } else {
                endHour = Integer.parseInt(hour);
                endMinute = Integer.parseInt(minute);
                tvendtime.setText(hour + "." + minute);
            }
        } catch (Exception e) {
            Constants.e("err1", e.toString());
        }

    }
}

package vip.com.vipmeetings;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DecimalFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.interfaces.OnTimeSelected;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 29/05/17.
 */

public class TimePickerActivity extends BaseActivity implements TimePicker.OnTimeChangedListener,
        OnTimeSelected {


    @BindView(R.id.tvdate)
    TextView tvdate;

    @BindView(R.id.tvsubject)
    TextView tvsubject;

    @BindView(R.id.timepicker)
    TimePicker timePicker;

//    @BindView(R.id.timepicker)
//    CustomTimePickerDialog timePicker;

    @BindView(R.id.tvstarttime)
    TextView tvstarttime;

    @BindView(R.id.tvendtime)
    TextView tvendtime;

    public boolean isStartTime;
    public boolean isEndTime;

    public int currentHour = -1;
    public int currentMinute = -1;

    public int oldcurrentHour = -1;
    public int oldcurrentMinute = -1;

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
        intent = getIntent();
        dateTimeModel = mGson.fromJson(intent.getStringExtra(Constants.DATETIMEMODEL), DateTimeModel.class);
        if (dateTimeModel != null) {

            setSeondsPicker();
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
        getFirstTime(true);

    }

    private static final int INTERVAL = 30;
    private static final DecimalFormat FORMATTER = new DecimalFormat("00");
    private NumberPicker minutePicker;

    public void setSeondsPicker() {

        try {
            int numValues = 60 / INTERVAL;
            String[] displayedValues = new String[numValues];
            for (int i = 0; i < numValues; i++) {
                displayedValues[i] = FORMATTER.format(i * INTERVAL);
            }

            View minute = timePicker.findViewById(Resources.getSystem()
                    .getIdentifier("minute", "id", "android"));
            if ((minute != null) && (minute instanceof NumberPicker)) {
                minutePicker = (NumberPicker) minute;
                minutePicker.setDisplayedValues(null);
                minutePicker.setMinValue(0);
                minutePicker.setMaxValue(numValues - 1);
                minutePicker.setDisplayedValues(displayedValues);
                minutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                        if (isStartTime) {

                            if (newVal == 0) {
                                currentMinute = 0;
                            } else {
                                currentMinute = 30;
                            }
                            setTime(String.format("%02d", currentHour) + "." +
                                    String.format("%02d", currentMinute));
                        } else if (isEndTime) {

                            if (newVal == 0) {
                                endMinute = 0;
                            } else {
                                endMinute = 30;
                            }

                            if (getHour(timePicker) < currentHour) {
                                setEndEmpty();

                            } else if (getHour(timePicker) <= currentHour && (endMinute == 0
                                    || endMinute < currentMinute)) {

                                setEndEmpty();

                            } else if (getHour(timePicker) <= currentHour && endMinute == currentMinute) {
                                setEndEmpty();
                            } else {

                                endHour = getHour(timePicker);
                                if (newVal == 0)
                                    endMinute = 0;
                                else
                                    endMinute = 30;
                                setTime(String.format("%02d", endHour) + "." +
                                        String.format("%02d", endMinute));
                            }
                        }
                    }
                });
            }
        } catch (Exception e) {
            IpAddress.e("time", e.toString());
        }
    }

    public void updatePickerValues(String[] newValues, NumberPicker picker) {
        picker.setDisplayedValues(null);
        picker.setMinValue(0);
        picker.setMaxValue(newValues.length - 1);
        picker.setWrapSelectorWheel(false);
        picker.setDisplayedValues(newValues);
    }

    public int getMinute() {
        if (minutePicker != null) {
            return (minutePicker.getValue() * INTERVAL);
        } else {


            if (Build.VERSION.SDK_INT >= 23) {

                return timePicker.getMinute();
            } else

                return timePicker.getCurrentMinute();
        }
    }

    public int getMinute(TimePicker timePicker) {
        if (minutePicker != null) {

            return (minutePicker.getValue() * INTERVAL);
        } else {


            if (Build.VERSION.SDK_INT >= 23) {

                return timePicker.getMinute();
            } else

                return timePicker.getCurrentMinute();
        }
    }

    public int getMinute(TimePicker timePicker, int minute) {
        if (minutePicker != null) {

            if (minute == 0) {
                return 0;
            } else
                return 30;

            //  return (minutePicker.getValue() * INTERVAL);
        } else {


            if (Build.VERSION.SDK_INT >= 23) {

                return timePicker.getMinute();
            } else

                return timePicker.getCurrentMinute();
        }
    }


    public int getHour() {
        if (Build.VERSION.SDK_INT >= 23) {

            return timePicker.getHour();
        } else {

            return timePicker.getCurrentHour();
//            if (isStartTime) {
//                if (currentHour == timePicker.getCurrentHour()) {
//                    return timePicker.getCurrentHour();
//                } else {
//                    return timePicker.getCurrentHour() - 1;
//                }
//            } else {
//                return timePicker.getCurrentHour() - 1;
//            }


        }
    }


    public int getHour(TimePicker timePicker) {
        if (Build.VERSION.SDK_INT >= 23) {

            return timePicker.getHour();
        } else {

            return timePicker.getCurrentHour();
//            if (isStartTime) {
//                if (currentHour == timePicker.getCurrentHour()) {
//                    return timePicker.getCurrentHour();
//                } else {
//                    return timePicker.getCurrentHour();
//                }
//            } else {
//                return timePicker.getCurrentHour();
//            }


        }
    }


    @OnClick(R.id.ivhome)
    public void onHome(View v) {

        gotoHome();

    }

    @OnClick(R.id.tvback)
    public void onBack(View v) {

        onBackPressed();
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

        if (endHour != -1 && endMinute != -1 && tvendtime.getText().length() > 0) {

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

        if (endHour != -1 && endMinute != -1 && tvendtime.getText().length() > 0) {
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
        } else {
            showToast("Select end time");
        }
    }


    public void roomList() {

        //skiproom no
        if (getStoreCitySelect() != null
                && getStoreCitySelect().getSkipRoom().equalsIgnoreCase("False")) {

            if (endHour != -1 && endMinute != -1 && tvendtime.getText().length() > 0) {
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
            } else {
                showToast("Select end time");
            }
        } else if (getStoreCitySelect().getSkipRoom().equalsIgnoreCase("True")) {


        }
    }

    @OnClick(R.id.tvstarttime)
    public void onStartTime(View v) {

        isStartTime = true;
        isEndTime = false;
        if (currentMinute != -1 && currentHour != -1) {


            getFirstTime(false);

        }
        tvendtime.setText("");
        endMinute = -1;
        endHour = -1;
        changeBG();
    }


    @OnClick(R.id.tvendtime)
    public void onEndTime(View v) {

        isStartTime = false;
        isEndTime = true;
        if (currentHour != -1 && currentMinute != -1) {
            if (currentHour < 24) {
                if (currentMinute >= 30) {

                    endMinute = 0;
                    endHour = currentHour + 1;
                } else {
                    endMinute = 30;
                    endHour = currentHour;
                }

                timePicker.setOnTimeChangedListener(null);

                if (Build.VERSION.SDK_INT >= 23) {

                    if (currentHour <= 23) {

                        timePicker.setHour(endHour);
                        timePicker.setMinute(endMinute);

                    } else {


                    }
                } else {
                    timePicker.setCurrentHour(endHour);
                    timePicker.setCurrentMinute(endMinute);
                }
                timePicker.setOnTimeChangedListener(this);
            }
            setTime(String.format("%02d", endHour) + "." +
                    String.format("%02d", endMinute));
        }

        changeBG();

    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int i, int i1) {

        IpAddress.e("time", i + "  time " + i1);
        if (isStartTime) {
            if (getHour(timePicker) > oldcurrentHour) {
                // currentHour = getHour(timePicker);
                currentHour = i;
                currentMinute = getMinute(timePicker, i1);
                setTime(String.format("%02d", currentHour) + "." +
                        String.format("%02d", currentMinute));

            } else if (getHour(timePicker) == oldcurrentHour) {
                currentHour = i;
                currentMinute = getMinute(timePicker, i1);
                setTime(String.format("%02d", currentHour) + "." +
                        String.format("%02d", currentMinute));

//                if (getMinute(timePicker) <= oldcurrentMinute) {
//
//                    if (getMinute(timePicker) <= 30) {
//                        currentMinute = 30;
//                        //currentHour = getHour(timePicker);
//                        currentHour=i;
//                        setTime(String.format("%02d", currentHour) + "." +
//                                String.format("%02d", currentMinute));
//
//                    } else if (getMinute(timePicker) > 30) {
//
//                         currentMinute = getMinute(timePicker);
//                        //currentMinute = currentMinute - 30;
//                        //currentHour = getHour(timePicker) ;
//                        currentHour=i;
//                        setTime(String.format("%02d", currentHour) + "." +
//                                String.format("%02d", currentMinute));
//                   }

                //              }
            }
        } else if (isEndTime) {

            if (getHour(timePicker) > currentHour) {

                //endHour = getHour(timePicker);
                endHour = i;
                endMinute = getMinute(timePicker, i1);
                setTime(String.format("%02d", endHour) + "." +
                        String.format("%02d", endMinute));
            } else if (getHour() == currentHour) {

                if (getMinute(timePicker, i1) > currentMinute) {

                    endHour = i;
                    endMinute = getMinute(timePicker, i1);
                    setTime(String.format("%02d", endHour) + "." +
                            String.format("%02d", endMinute));

                } else {
                    setEndEmpty();

                }


                //  if (currentMinute <= getMinute(timePicker)) {


//                    if (getMinute(timePicker) <= 30) {
//
//
//                        endHour=i;
//                       // endHour = getHour(timePicker) ;
//                        endMinute = getMinute(timePicker,i1);
//                        setTime(String.format("%02d", endHour) + "." +
//                                String.format("%02d", endMinute));
//
//                    } else if (getMinute(timePicker) > 30) {
//
//                        endHour=i;
//                         //  endHour = getHour(timePicker);
//                           endMinute = getMinute(timePicker);
//                       // endMinute = endMinute - 30;
//                        setTime(String.format("%02d", endHour) + "." +
//                                String.format("%02d", endMinute));
//                    }
            } else {
                setEndEmpty();
            }
        }


    }

    private void setEndEmpty() {

        endMinute = -1;
        endHour = -1;
        tvendtime.setText("");
    }

    Calendar starttime;

    private void getFirstTime(boolean flagFirst) {

        timePicker.setOnTimeChangedListener(null);
        Calendar datetime = Calendar.getInstance();
        starttime = Calendar.getInstance();
        int month = starttime.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = starttime.get(Calendar.DAY_OF_MONTH);
        int hour = starttime.get(Calendar.HOUR_OF_DAY);
        int minute = starttime.get(Calendar.MINUTE);
        if (dateTimeModel.isToday()) {
            oldcurrentHour = hour;
            oldcurrentMinute = minute;
            currentHour = hour;
            currentMinute = minute;

            if (currentMinute <= 30) {

                currentMinute = 30;
                oldcurrentMinute = 30;

            } else {

                currentMinute = 0;
                oldcurrentMinute = 0;
                currentHour += 1;
            }
            setHour(currentHour);
            setMinute(currentMinute);
            setTime(String.format("%02d", currentHour) + "." +
                    String.format("%02d", currentMinute));
            datetime.set(Calendar.HOUR_OF_DAY, currentHour);
            datetime.set(Calendar.MINUTE, currentMinute);
        } else {
            oldcurrentHour = 0;
            oldcurrentMinute = 0;
            currentHour = 0;
            currentMinute = 0;
            setHour(currentHour);
            setMinute(currentMinute);
            setTime(String.format("%02d", currentHour) + "." +
                    String.format("%02d", currentMinute));

        }
        timePicker.setOnTimeChangedListener(this);
    }

    private void setHour(int currentHour) {

        if (Build.VERSION.SDK_INT >= 23) {

            timePicker.setHour(currentHour);
        } else {

            timePicker.setCurrentHour(currentHour);
        }

    }

    private void setMinute(int currentMinute) {

        if (Build.VERSION.SDK_INT >= 23) {

            timePicker.setMinute(currentMinute);
        } else {

            timePicker.setCurrentMinute(currentMinute);
        }

    }

    private void setTime(String s) {


        if (isStartTime) {


            tvstarttime.setText(s);

        } else {


            if (endHour != -1 && endMinute != -1) {

                tvendtime.setText(s);
            } else {
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

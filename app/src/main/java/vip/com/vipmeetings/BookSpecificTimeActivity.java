package vip.com.vipmeetings;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.facebook.drawee.view.SimpleDraweeView;

import java.text.DecimalFormat;
import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 03/07/17.
 */

public class BookSpecificTimeActivity extends BaseActivity implements TimePicker.OnTimeChangedListener {


    @BindView(R.id.lltime)
    LinearLayout lltime;

    @BindView(R.id.tvsubject)
    AppCompatTextView tvsubject;

    @BindView(R.id.tvdate)
    AppCompatTextView tvdate;

    @BindView(R.id.tvtimes)
    AppCompatTextView tvtimes;

    @BindView(R.id.tvstarttime)
    AppCompatTextView tvstarttime;

    @BindView(R.id.tvendtime)
    AppCompatTextView tvendtime;

    @BindView(R.id.tvroomname)
    AppCompatTextView tvroomname;

    @BindView(R.id.tvfloors)
    AppCompatTextView tvfloors;

    @BindView(R.id.ivnext)
    AppCompatImageView ivnext;


    @BindView(R.id.tvseats)
    AppCompatTextView tvseats;

    @BindView(R.id.ivroom)
    SimpleDraweeView ivroom;

    boolean isStartTime, isEndTime;

    int currentHour = -1, currentMinute = -1;

    public int oldcurrentHour = -1;
    public int oldcurrentMinute = -1;

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


        setSeondsPicker();

        if (bookSpecificModel != null) {

            IpAddress.e("bookspe", mGson.toJson(bookSpecificModel));

//            if (bookSpecificModel.getFloorNumber() != null)
//
//                tvfloors.setText("Floor : " + bookSpecificModel.getFloorNumber());
//            else
//                tvfloors.setText("Floor : ");

            if (bookSpecificModel.getFloorNumber() != null) {
                String text = "<font color=#64829a> Floor : " + "</font> <font color=#000000>" + bookSpecificModel.getFloorNumber() + "</font>";
                tvfloors.setText(Html.fromHtml(text));
            }else
            {
                tvfloors.setText("Floor : ");
            }

            if (bookSpecificModel.getCapacity() != null) {
                String text = "<font color=#64829a> Seats : " + "</font> <font color=#000000>" + bookSpecificModel.getFloorNumber() + "</font>";
                tvseats.setText(Html.fromHtml(text));
            }else
            {
                tvseats.setText("Seats : ");
            }

//            if (bookSpecificModel.getCapacity() != null)
//                tvseats.setText("Seats : " + bookSpecificModel.getCapacity());
//            else
//                tvseats.setText("Seats : ");

            if (bookSpecificModel.getRoomName() != null)
                tvroomname.setText(bookSpecificModel.getRoomName());
            else
                tvroomname.setText("");

            ivroom.setImageURI(bookSpecificModel.getImage1());
            tvsubject.setText(bookSpecificModel.getSubject());
            tvdate.setText(bookSpecificModel.getFormatDate());

            tvtimes.setMovementMethod(new ScrollingMovementMethod());

            if (bookSpecificModel.getTimes() != null && bookSpecificModel.getTimes().length() > 0) {
                tvtimes.setText("Booked: " + bookSpecificModel.getTimes());
            } else {
                tvtimes.setText("Slots are not yet booked.");
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
        getFirstTime(true);

    }

//    @OnClick(R.id.llroom)
//    public void onRoom(View v) {
//        onBackPressed();
//    }

    @OnClick(R.id.ivhome)
    public void onHome(View v) {

        gotoHome();

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


    @OnClick(R.id.root)
    public void onRoot(View v) {

        hideSoft();
    }


    @OnClick(R.id.tvback)
    public void onBack(View v) {
        onBackPressed();
    }


    @OnClick({R.id.tvnext, R.id.ivnext})
    public void onNext(View v) {


        if (endHour != -1 && endMinute != -1 && tvendtime.getText().length() > 0) {

            bookSpecificModel.setStartHour(currentHour);
            bookSpecificModel.setStartMinute(currentMinute);
            bookSpecificModel.setEndHour(endHour);
            bookSpecificModel.setEndMinute(endMinute);

            if (bookSpecificModel.getStartHour() >= 0 && bookSpecificModel.getStartMinute() >= 0) {

                Intent i = new Intent(this, ConfirmationActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra(Constants.DATETIMEMODEL, mGson.toJson(bookSpecificModel));
                startActivity(i);

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


//    @OnClick(R.id.tvstarttime)
//    public void onStartTime(View v) {
//
//        timePicker.setVisibility(View.VISIBLE);
//        isStartTime = true;
//        isEndTime = false;
//        if (currentMinute != -1 && currentHour != -1) {
//
//            try {
//
//                if (Build.VERSION.SDK_INT >= 23) {
//                    timePicker.setHour(currentHour);
//                    timePicker.setMinute(currentMinute);
//                } else {
//                    timePicker.setCurrentHour(currentHour);
//                    timePicker.setCurrentMinute(currentMinute);
//                }
//                tvendtime.setText("");
//            } catch (Exception e) {
//
//            }
//        }
//
//        changeBG();
//
//    }
//    @OnClick(R.id.tvendtime)
//    public void onEndTime(View v) {
//
//        timePicker.setVisibility(View.VISIBLE);
//        isStartTime = false;
//        isEndTime = true;
//        if (currentHour != -1 && currentMinute != -1) {
//
//            try {
//
//
//                if(currentMinute>=30) {
//                    endMinute =0;
//                    endHour = currentHour+1;
//                }else
//                {
//                    endMinute =30;
//                    endHour=currentHour;
//                }
//                endMinute = currentMinute+30;
//                if (Build.VERSION.SDK_INT >= 23) {
//
//                    if (currentHour <= 23) {
//
////                        timePicker.setHour(currentHour + 1);
////                        timePicker.setMinute(currentMinute);
//
//                        timePicker.setHour(currentHour);
//                        timePicker.setMinute(currentMinute+30);
//
//                    } else {
//
//
//                    }
//                } else {
////                    timePicker.setCurrentHour(currentHour + 1);
////                    timePicker.setCurrentMinute(currentMinute);
//
//                    timePicker.setCurrentHour(currentHour);
//                    timePicker.setCurrentMinute(currentMinute+30);
//                }
//            } catch (Exception e) {
//
//            }
//        }
//
//        changeBG();
//
//    }
//
//    @Override
//    public void onTimeChanged(TimePicker timePicker, int i, int i1) {
//
//
//        if (isStartTime) {
//            getFirstTime(false);
//        } else if (isEndTime) {
//
//
//            if (Build.VERSION.SDK_INT >= 23) {
//
//                if (endHour >= (currentHour)) {
//
//                    if (endMinute >= (30)) {
//
//                        endHour = timePicker.getHour();
//                        endMinute = getMinute();
//                        bookSpecificModel.setEndHour(endHour);
//                        bookSpecificModel.setEndMinute(endMinute);
//
//                        setTime(String.format("%02d", endHour) + "." +
//                                String.format("%02d", endMinute));
//                    }else
//                    {
//
//                        if(currentMinute>=30) {
//
//                            endMinute =0;
//                            endHour = currentHour+1;
//                        }else
//                        {
//                            endMinute = 30;
//                            endHour=currentHour;
//                        }
//
//                        bookSpecificModel.setEndHour(endHour);
//                        bookSpecificModel.setEndMinute(endMinute);
//                        setTime(String.format("%02d", endHour) + "." +
//                                String.format("%02d",endMinute));
//                    }
//
//                } else {
//
//                    endHour = -1;
//                    endMinute = -1;
//                    bookSpecificModel.setEndHour(0);
//                    bookSpecificModel.setEndMinute(0);
//                    tvendtime.setText("");
//                }
//            } else {
//
//                if (timePicker.getCurrentHour() >= (currentHour)) {
//
//                    if (endMinute >= (30)) {
//                        endHour = timePicker.getCurrentHour();
//                        endMinute = getMinute();
//                        bookSpecificModel.setEndHour(endHour);
//                        bookSpecificModel.setEndMinute(endMinute);
//                        setTime(String.format("%02d", endHour) + "." +
//                                String.format("%02d", endMinute));
//                    }else
//                    {
//                        if(currentMinute>=30) {
//
//                            endMinute =0;
//                            endHour = currentHour+1;
//                        }else
//                        {
//                            endMinute = 30;
//                            endHour=currentHour;
//                        }
//
//
//                        bookSpecificModel.setEndHour(endHour);
//                        bookSpecificModel.setEndMinute(endMinute);
//                        setTime(String.format("%02d", endHour) + "." +
//                                String.format("%02d",endMinute));
//                    }
//
//                } else {
//
//                    endHour = -1;
//                    endMinute = -1;
//                    bookSpecificModel.setEndHour(0);
//                    bookSpecificModel.setEndMinute(0);
//                    tvendtime.setText("");
//
//                }
//            }
//
//            ((TextView) findViewById(R.id.tvnext)).setVisibility(View.GONE);
//            ((TextView) findViewById(R.id.tvnext)).setText("Book");
//
//        }
//    }
//
//    private void getFirstTime(boolean flagFirst) {
//
//        boolean flag = true;
//        Calendar datetime = Calendar.getInstance();
//        Calendar c = Calendar.getInstance();
//        if (bookSpecificModel.isToday()) {
//            if (Build.VERSION.SDK_INT >= 23) {
//
//                 currentHour = timePicker.getHour();
//                 currentMinute = getMinute();
//
//
//                datetime.set(Calendar.HOUR_OF_DAY, currentHour);
//                datetime.set(Calendar.MINUTE, currentMinute);
//                if(flagFirst) {
//
//                    if(currentMinute<=30)
//                    {
//                        currentMinute=30;
//
//                    }else
//                    {
//                        currentHour+=1;
//                        currentMinute=0;
//                    }
//                    timePicker.setHour(currentHour);
//                    timePicker.setMinute(currentMinute);
//                }
//                setTime(String.format("%02d", currentHour) + "." +
//                        String.format("%02d", currentMinute));
//
//
//
//            } else {
//                 currentHour = timePicker.getCurrentHour();
//                 currentMinute = getMinute();
//
//
//
//                datetime.set(Calendar.HOUR_OF_DAY, currentHour);
//                datetime.set(Calendar.MINUTE, currentMinute);
//
//                if(flagFirst) {
//
//                    if(currentMinute<=30)
//                    {
//                        currentMinute=30;
//
//                    }else
//                    {
//                        currentHour+=1;
//                        currentMinute=0;
//                    }
//
//                    timePicker.setCurrentHour(currentHour);
//                    timePicker.setCurrentMinute(currentMinute);
//                }
//                setTime(String.format("%02d", currentHour) + "." +
//                        String.format("%02d", currentMinute));
//
//
//            }
//            if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
//                //it's after current
//                flag = true;
//            } else {
//                flag = false;
//            }
//        }
//
//        if (flag) {
//
//            if (Build.VERSION.SDK_INT >= 23) {
//
//
//                currentHour = timePicker.getHour();
//                currentMinute = getMinute();
//
//                if(flagFirst) {
//
//                    timePicker.setHour(currentHour);
//                    timePicker.setMinute(currentMinute);
//                }
//
//                bookSpecificModel.setStartHour(currentHour);
//                bookSpecificModel.setStartMinute(currentMinute);
//                setTime(String.format("%02d", currentHour) + "." +
//                        String.format("%02d", currentMinute));
//            } else {
//                currentHour = timePicker.getCurrentHour();
//                currentMinute = getMinute();
//
//                if(flagFirst) {
//
//                    timePicker.setCurrentHour(currentHour);
//                    timePicker.setCurrentMinute(currentMinute);
//                }
//
//                bookSpecificModel.setStartHour(currentHour);
//                bookSpecificModel.setStartMinute(currentMinute);
//
//                setTime(String.format("%02d", currentHour) + "." +
//                        String.format("%02d", currentMinute));
//            }
//        }
//
//        endHour=currentHour;
//        endMinute=currentMinute+30;
//    }


    @OnClick(R.id.tvstarttime)
    public void onStartTime(View v) {

        isStartTime = true;
        isEndTime = false;
        if (currentMinute != -1 && currentHour != -1) {

            timePicker.setOnTimeChangedListener(null);

            if (Build.VERSION.SDK_INT >= 23) {

                if (currentHour <= 23) {

                    timePicker.setHour(currentHour);
                    timePicker.setMinute(currentMinute);

                }
            } else {
                timePicker.setCurrentHour(currentHour);
                timePicker.setCurrentMinute(currentMinute);
            }
            setTime(String.format("%02d", currentHour) + "." +
                    String.format("%02d", currentMinute));

            timePicker.setOnTimeChangedListener(this);
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
        }
        setTime(String.format("%02d", endHour) + "." +
                String.format("%02d", endMinute));
        timePicker.setOnTimeChangedListener(this);
        changeBG();

    }

//    @Override
//    public void onTimeChanged(TimePicker timePicker, int i, int i1) {
//
//        if (isStartTime) {
//            if (getHour() > oldcurrentHour) {
//
//                currentHour = getHour();
//                currentMinute = getMinute();
//                setTime(String.format("%02d", currentHour) + "." +
//                        String.format("%02d", currentMinute));
//
//            } else if (getHour() == oldcurrentHour) {
//                if (oldcurrentMinute <= getMinute()) {
//
//                    currentHour = getHour();
//                    currentMinute = getMinute();
//                    setTime(String.format("%02d", currentHour) + "." +
//                            String.format("%02d", currentMinute));
//                }else{
//
//
//                    getFirstTime(false);
//                }
//            }else
//            {
//                setTime(String.format("%02d", currentHour) + "." +
//                        String.format("%02d", currentMinute));
//            }
//        } else if (isEndTime) {
//
//            if (getHour() > currentHour) {
//
//                endHour = getHour();
//                endMinute = getMinute();
//                setTime(String.format("%02d", endHour) + "." +
//                        String.format("%02d", endMinute));
//            } else if (getHour() == currentHour) {
//                if (currentMinute <=getMinute()) {
//
//                    endHour = getHour();
//                    endMinute = getMinute();
//                    setTime(String.format("%02d", endHour) + "." +
//                            String.format("%02d", endMinute));
//                }else
//                {
//
//                }
//            }
//        }
//
//
//    }

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


    public int getHour() {
        if (Build.VERSION.SDK_INT >= 23) {

            return timePicker.getHour();
        } else

            return timePicker.getCurrentHour();
    }

    Calendar starttime;

    private void getFirstTime(boolean flagFirst) {


        Calendar datetime = Calendar.getInstance();
        starttime = Calendar.getInstance();
        int month = starttime.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = starttime.get(Calendar.DAY_OF_MONTH);
        int hour = starttime.get(Calendar.HOUR_OF_DAY);
        int minute = starttime.get(Calendar.MINUTE);
        if (bookSpecificModel.isToday()) {


            oldcurrentHour = getHour();
            oldcurrentMinute = getMinute();
            currentHour = getHour();
            currentMinute = getMinute();

            if (minute <= 30) {
                setHour(hour);
                oldcurrentHour = hour;
                currentMinute = 30;
                oldcurrentMinute = 30;
                setMinute(oldcurrentMinute);
            } else {
                oldcurrentHour = hour + 1;
                currentHour = hour + 1;
                setHour(hour + 1);
                currentMinute = 0;
                oldcurrentMinute = 0;
                setMinute(oldcurrentMinute);
            }
            setTime(String.format("%02d", currentHour) + "." +
                    String.format("%02d", currentMinute));
            datetime.set(Calendar.HOUR_OF_DAY, currentHour);
            datetime.set(Calendar.MINUTE, currentMinute);
        } else {
            oldcurrentHour = 0;
            oldcurrentMinute = 0;
            currentHour = 0;
            currentMinute = 0;

//            setHour(hour);
//            oldcurrentHour = hour;
//            currentMinute = 30;
//            oldcurrentMinute = 30;
//            setMinute(oldcurrentMinute);

            if (getHour() >= oldcurrentHour) {

                if (getHour() == currentHour) {
                    if (getMinute() >= 30) {
                        // currentHour += 1;
                        currentMinute = 0;
                    }
                }

                currentHour = getHour();
                currentMinute = getMinute();

            }
            setHour(currentHour);
            setMinute(currentMinute);
            setTime(String.format("%02d", currentHour) + "." +
                    String.format("%02d", currentMinute));

        }
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


}

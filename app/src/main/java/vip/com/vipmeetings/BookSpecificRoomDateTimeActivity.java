package vip.com.vipmeetings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vip.com.vipmeetings.body.GetRoomBookingsRequestBody;
import vip.com.vipmeetings.body.GetRoomBookingsResponseBody;
import vip.com.vipmeetings.envelope.SoapGetRoomBookingsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetRoomBookingsResponseEnvelope;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.interfaces.MainApi;
import vip.com.vipmeetings.models.RoomOccupancyModel;
import vip.com.vipmeetings.models.RoomsOccupancyList;
import vip.com.vipmeetings.request.GetRoomBookings;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 26/06/17.
 */

public class BookSpecificRoomDateTimeActivity extends BaseActivity implements
        OnDateSelectedListener {


    @BindView(R.id.tvroomname)
    TextView tvroomname;

    @BindView(R.id.tvfloors)
    TextView tvfloors;

    @BindView(R.id.tvseats)
    TextView tvseats;

    @BindView(R.id.ivroom)
    SimpleDraweeView ivroom;

    @BindView(R.id.tvstartdate)
    TextView tvstartdate;

    @BindView(R.id.etsubject)
    EditText etsubject;


    @BindView(R.id.llsubject)
    LinearLayout llsubject;

    @BindView(R.id.lldate)
    LinearLayout lldate;

    DateTimeModel bookSpecificModel;
    RoomsOccupancyList roomsOccupancyList;
    List<RoomOccupancyModel> roomOccupancyModelList;
    private Date today_date;


    @BindView(R.id.calendarView)
    MaterialCalendarView calendarView;
    OneDayDecorator_RED3 oneDayDecorator_red3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookspecificroomdate);
        ButterKnife.bind(this);
        bookSpecificModel = mGson.fromJson(getIntent().getStringExtra(Constants.BOOKROOMSPECIFIC),
                DateTimeModel.class);
        oneDayDecorator_red3 = new OneDayDecorator_RED3(this);

        if(getSubject()!=null) {
            etsubject.setText(getSubject());
        }else if(bookSpecificModel!=null && bookSpecificModel.getSubject()!=null)
        {
            etsubject.setText(bookSpecificModel.getSubject());
        }
        etsubject.setSelection(etsubject.getText().length());

        if (bookSpecificModel != null) {

            if (bookSpecificModel.getFloorNumber() != null) {

                String text = "<font color=#64829a> Floor : " + "</font> <font color=#000000>" + bookSpecificModel.getFloorNumber() + "</font>";
                tvfloors.setText(Html.fromHtml(text));
                // tvfloors.setText("Floor : " + bookSpecificModel.getFloorNumber());
            } else {
                tvfloors.setText("Floor : ");
            }

            if (bookSpecificModel.getCapacity() != null) {

                String text = "<font color=#64829a> Seats : " + "</font> <font color=#000000>" + bookSpecificModel.getCapacity() + "</font>";
                tvseats.setText(Html.fromHtml(text));

                // tvseats.setText("Seats : " + bookSpecificModel.getCapacity());
            } else {
                tvseats.setText("Seats : ");
            }

            if (bookSpecificModel.getRoomName() != null) {

//                String text = "<font color=#64829a> Seats : " + "</font> <font color=#000000>" + bookSpecificModel.getCapacity()+"</font>";
//                tvseats.setText(Html.fromHtml(text));
                tvroomname.setText(bookSpecificModel.getRoomName());
            } else
                tvroomname.setText("");
            ivroom.setImageURI(bookSpecificModel.getImage1());
            Constants.e("Room", bookSpecificModel.getRoomId());


            setMaterialCalendar();

            etsubject.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {


                    if (i == EditorInfo.IME_ACTION_DONE) {

                        hideKeyboard(etsubject);
                        return true;
                    }

                    return false;
                }
            });

            getRoomBookings();
        }
    }

    @OnClick(R.id.root)
    public void onRoot(View v) {

        hideSoft();
    }

    @OnClick(R.id.llroom)
    public void onRoom(View v) {
        onBackPressed();
    }

    private void setMaterialCalendar() {


        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 100);
        int year = nextYear.get(Calendar.YEAR);
        int month = nextYear.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = nextYear.get(Calendar.DAY_OF_MONTH);

        calendarView
                .state()
                .edit()
                .setFirstDayOfWeek(DayOfWeek.MONDAY)
                .setMinimumDate(CalendarDay.today())
                .setMaximumDate(CalendarDay.from(year, month, day))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();


        calendarView.removeDecorators();
        calendarView.addDecorators(oneDayDecorator_red3, new OneDayDecorator_RED4(this));
        calendarView.invalidateDecorators();
        calendarView.setOnDateChangedListener(this);
    }


    private void getRoomBookings() {

        showPD(this);
        setRetrofit(getApp().getTcpIp());
        MainApi mainApi = retrofit.create(MainApi.class);
        SoapGetRoomBookingsRequestEnvelope
                soapGetRoomBookingsRequestEnvelope = new SoapGetRoomBookingsRequestEnvelope();

        GetRoomBookingsRequestBody getRoomBookingsRequestBody = new GetRoomBookingsRequestBody();

        GetRoomBookings getRoomBookings = new GetRoomBookings();

        getRoomBookings.setAuthToken(getAuthTokenPref_Mobile());
        getRoomBookings.setA_iRoomId(bookSpecificModel.getRoomId());

        getRoomBookingsRequestBody.setGetRoomBookings(getRoomBookings);
        soapGetRoomBookingsRequestEnvelope.setBody(getRoomBookingsRequestBody);


        Observable<SoapGetRoomBookingsResponseEnvelope> lClientDetailsObservable =
                mainApi.getRoomBookings(soapGetRoomBookingsRequestEnvelope);

        lClientDetailsObservable.
                subscribeOn(Schedulers.newThread())
                .map(this::onSoapGetRoomBook)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onRoomsOccupancy, this::onError);
    }

    private RoomsOccupancyList onSoapGetRoomBook(SoapGetRoomBookingsResponseEnvelope soapGetRoomBookingsResponseEnvelope) {


        GetRoomBookingsResponseBody getRoomBookingsResponseBody = soapGetRoomBookingsResponseEnvelope.getBody();


        RoomsOccupancyList roomsOccupancyList =
                getRoomBookingsResponseBody.getGetRoomBookingsResponse().getGetRoomBookingsResult().getRoomsOccupancyList();

        if (roomsOccupancyList == null)
            return new RoomsOccupancyList();
        return roomsOccupancyList;
    }

    private void onRoomsOccupancy(RoomsOccupancyList roomsOccupancyList1) {

        hidePD();
        this.roomsOccupancyList = roomsOccupancyList1;
        if (roomsOccupancyList != null && roomsOccupancyList.getRoom() != null) {

            roomOccupancyModelList = roomsOccupancyList.getRoom();

            if (roomOccupancyModelList != null && roomOccupancyModelList.size() > 0) {


                for (RoomOccupancyModel roomsOccupancyList2 : roomOccupancyModelList) {

                    calendarView.invalidateDecorators();
                    String string = roomsOccupancyList2.getBookingDate();

                    Date date = Constants.formatDateFromOccupancy(string);

                    Calendar nextYear = Calendar.getInstance();

                    nextYear.setTime(date);

                    int year = nextYear.get(Calendar.YEAR);
                    int month = nextYear.get(Calendar.MONTH) + 1; // Note: zero based!
                    int day = nextYear.get(Calendar.DAY_OF_MONTH);

                    LocalDate localDate = LocalDate.of(year, month, day);
                    OneDayDecoratorGREEN oneDayDecorator2 = new OneDayDecoratorGREEN
                            (BookSpecificRoomDateTimeActivity.this);
                    calendarView.addDecorator(oneDayDecorator2);
                    calendarView.invalidateDecorators();

                    if (Float.parseFloat(roomsOccupancyList2.getOccupancy()) > 95) {

                        //red
                        OneDayDecorator_RED oneDayDecorator_red = new OneDayDecorator_RED
                                (BookSpecificRoomDateTimeActivity.this);


                        oneDayDecorator_red.setDate(CalendarDay.from(localDate));
                        calendarView.addDecorator(oneDayDecorator_red);
                        calendarView.setDateSelected(CalendarDay.from(localDate), true);

                    } else {
                        //green
                        OneDayDecorator oneDayDecorator = new OneDayDecorator
                                (BookSpecificRoomDateTimeActivity.this);

                        oneDayDecorator.setDate(CalendarDay.from(localDate));

                        calendarView.addDecorator(oneDayDecorator);
                        calendarView.setDateSelected(CalendarDay.from(localDate), true);
                    }

                }
                calendarView.invalidateDecorators();
            } else {
                OneDayDecoratorGREEN oneDayDecorator = new OneDayDecoratorGREEN
                        (BookSpecificRoomDateTimeActivity.this);
                calendarView.addDecorator(oneDayDecorator);
                calendarView.invalidateDecorators();
                roomOccupancyModelList = null;
            }
        } else {

            OneDayDecoratorGREEN oneDayDecorator = new OneDayDecoratorGREEN
                    (BookSpecificRoomDateTimeActivity.this);
            calendarView.addDecorator(oneDayDecorator);
            calendarView.invalidateDecorators();
            roomOccupancyModelList = null;
        }
    }


    @OnClick(R.id.ivhome)
    public void onHome(View v) {

        gotoHome();

    }


    @OnClick({R.id.fldateselect})
    public void onDate() {

        hideSoft();
        calendarView.setVisibility(View.VISIBLE);
        llsubject.setVisibility(View.GONE);
    }


    @OnClick(R.id.tvnext)
    public void onNext(View v) {


        if (bookSpecificModel.getDateString() != null) {

            if (etsubject.getText().toString().trim().length() > 0) {

                bookSpecificModel.setSubject(etsubject.getText().toString().trim());
                Intent i = new Intent(this, BookSpecificTimeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                //     Intent i = new Intent(this, BookSpecificTimerActivity.class);
                //   i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                if (roomOccupancyModelList != null && roomOccupancyModelList.size() > 0) {


                    if (Constants.formatDate(today_date).
                            equalsIgnoreCase(Constants.formatDate(new Date()))) {
                        bookSpecificModel.setToday(true);
                    } else {
                        bookSpecificModel.setToday(false);
                    }

                    bookSpecificModel.setTimes(null);

                    StringBuilder stringBuilder = new StringBuilder();
                    IpAddress.e("bookspe", mGson.toJson(bookSpecificModel));
                    for (RoomOccupancyModel roomOccupancyModel : roomOccupancyModelList) {
                        //2019.03.19

                        if (roomOccupancyModel.getBookingDate() != null
                                && bookSpecificModel.getFormatDate() != null
                                && roomOccupancyModel.getBookingDate().equalsIgnoreCase(bookSpecificModel
                                .getFormatDate().replace("-", "."))) {

                            stringBuilder.append(roomOccupancyModel.getTimes() + ",");

                        }
                    }


                    if (stringBuilder != null && stringBuilder.length() > 0)
                        bookSpecificModel.setTimes(stringBuilder.toString().substring(0, stringBuilder.length() - 1));
//                    bookSpecificModel.setTimes(
//                            roomOccupancyModelList.toString().substring(1,
//                                    roomOccupancyModelList.toString().length() - 1));
                }
                i.putExtra(Constants.BOOKROOMSPECIFIC, mGson.toJson(bookSpecificModel));
                startActivity(i);
            } else {
                calendarView.setVisibility(View.GONE);
                llsubject.setVisibility(View.VISIBLE);
                showToast("Enter subject");
            }
        } else {

            calendarView.setVisibility(View.VISIBLE);
            llsubject.setVisibility(View.GONE);
            showToast("Enter date");
        }


    }

    DateSeletedDecorator oneDayDecorator_red;


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {


        today_date = Constants.formatDateFromLocalDate(date.getDate().toString());
        bookSpecificModel.setFormatDate(date.getDate().toString());

        bookSpecificModel.setDateString(Constants
                .getSelectedDate(Constants.formatDateFromLocalDate(date.getDate().toString())));

        tvstartdate.setText(date.getDate().toString());
        calendarView.setVisibility(View.GONE);
        llsubject.setVisibility(View.VISIBLE);
        Date date1 = new Date();
        if (Constants.formatDate(date1).
                equalsIgnoreCase(Constants.formatDate(Constants.formatDateFromLocalDate(date.getDate().toString())))) {
            bookSpecificModel.setToday(true);
        } else {
            bookSpecificModel.setToday(false);
        }
        if (oneDayDecorator_red != null) {
            calendarView.removeDecorator(oneDayDecorator_red);
        }
        oneDayDecorator_red = new DateSeletedDecorator(this);
        oneDayDecorator_red.setDate(CalendarDay.from(date.getDate()));
        calendarView.addDecorator(oneDayDecorator_red);
        calendarView.invalidateDecorators();
        etsubject.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }


    @OnClick(R.id.tvback)
    public void onBack(View v) {
        onBackPressed();
    }
}

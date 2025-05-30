package vip.com.vipmeetings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vip.com.vipmeetings.body.GetClientRequestBody;
import vip.com.vipmeetings.body.GetRoomsRequestBody;
import vip.com.vipmeetings.body.GetRoomsResponseBody;
import vip.com.vipmeetings.envelope.SoapGetClient;
import vip.com.vipmeetings.envelope.SoapGetClientResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetRoomsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetRoomsResponseEnvelope;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.interfaces.MainApi;
import vip.com.vipmeetings.models.City;
import vip.com.vipmeetings.models.Client;
import vip.com.vipmeetings.models.Meeting;
import vip.com.vipmeetings.models.Rooms;
import vip.com.vipmeetings.request.GetClient;
import vip.com.vipmeetings.request.GetRooms;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 29/05/17.
 */

//invite
public class BookMeetingsActivity extends BaseActivity implements OnDateSelectedListener {


    @BindView(R.id.etsubject)
    AppCompatEditText etsubject;

    @BindView(R.id.btspecific)
    AppCompatTextView btspecific;

    @BindView(R.id.tvfromdate)
    AppCompatTextView tvfromdate;

    @BindView(R.id.tvtodate)
    AppCompatTextView tvtodate;


    @BindView(R.id.bttodate)
    AppCompatTextView bttodate;

    @BindView(R.id.llroot)
    LinearLayout llroot;


    Date date, toDate;
    DateTimeModel dateTimeModel;
    OneDayDecorator_RED3 oneDayDecorator_red3;
    @BindView(R.id.calendarView)
    MaterialCalendarView calendarView;

    DateSeletedDecorator dateSeletedDecorator;

    boolean isOnlyDate;
    @BindView(R.id.tv_edit_location)
    AppCompatTextView tvEditLocation;

    @BindView(R.id.ll_edit_location)
    LinearLayout llEditLocation;

    @BindView(R.id.img_edit)
    ImageView imgEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmeetings);
        ButterKnife.bind(this);
        City city = getStoreCitySelect();

        if (getIntent().hasExtra(Constants.BOOKROOM)) {
            btspecific.setVisibility(View.VISIBLE);
            llEditLocation.setVisibility(View.GONE);
        } else {
            btspecific.setVisibility(View.GONE);
            llEditLocation.setVisibility(View.VISIBLE);
        }

        dateTimeModel = new DateTimeModel();

        if (getIntent().hasExtra(Constants.INVITE)) {
            dateTimeModel.setInvite(true);
        } else {
            dateTimeModel.setInvite(false);
        }
        if(city!= null){
            //1253
            tvEditLocation.setText(city.getCityName());
        }else
        {
            tvEditLocation.setText("");
        }
        etsubject.setText(getSubject());
        etsubject.setSelection(etsubject.getText().length());
        this.date = new Date();
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 100);
        calendarView.state().edit()
                .setFirstDayOfWeek(DayOfWeek.MONDAY)
                .setMinimumDate(CalendarDay.today())
                .setMaximumDate(nextYear())
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();


        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);

        oneDayDecorator_red3 = new OneDayDecorator_RED3(this);
        oneDayDecorator_red3.setDate(CalendarDay.today());
        //  oneDayDecorator_red = new OneDayDecorator_RED2(this);

//        calendarView.addDecorators(
//                oneDayDecorator_red3,
//                oneDayDecorator_red, new OneDayDecorator_RED4(this));

        dateSeletedDecorator = new DateSeletedDecorator(this);

        calendarView.addDecorators(
                oneDayDecorator_red3, new OneDayDecorator_RED4(this), dateSeletedDecorator);

        calendarView.invalidateDecorators();
        calendarView.setOnDateChangedListener(this);


        tvfromdate.setVisibility(View.GONE);
        tvtodate.setVisibility(View.GONE);

        tvfromdate.setText("");
        tvtodate.setText("");


        if (calendarView.getSelectedDate() != null)
            setCurrentDate(calendarView.getSelectedDate());

       // tvtodate.setVisibility(View.GONE);

        getClient();
    }
    @OnClick(R.id.img_edit)
    public void onEditClick() {


        Intent i = new Intent(this, LocationActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("NewLoc","EditBook");
        //startActivityForResult(i, REQUEST_REFRESH);
        startActivity(i);


    }

    @Override
    protected void onResume() {
        super.onResume();
        City city = getStoreCitySelect();
        if(city!= null){
            //1253
            tvEditLocation.setText(city.getCityName());
        }else
        {
            tvEditLocation.setText("");
        }
    }

    private void getClient() {

        showPD();
        setRetrofit(getApp().getTcpIp());
        mainApi = retrofit.create(MainApi.class);

        SoapGetClient soapGetClient = new SoapGetClient();

        GetClientRequestBody getClientRequestBody = new GetClientRequestBody();

        GetClient getClient = new GetClient();

        getClient.setAuthToken(getAuthTokenPref_Mobile());
        getClient.setsClientID(getClientID());

        getClientRequestBody.setGetClient(getClient);

        soapGetClient.setBody(getClientRequestBody);


        Observable<SoapGetClientResponseEnvelope> lClientDetailsObservable =
                mainApi.getClient(soapGetClient);
        lClientDetailsObservable.
                subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetClient, this::onError);
    }

    private void onGetClient(SoapGetClientResponseEnvelope soapGetClientResponseEnvelope) {

        hidePD();

        try {

            Client client = soapGetClientResponseEnvelope.getBody()
                    .getGetClientResponse().getGetClientResult().getClient();
            if (client != null) {

                isPatient = client.isPatientSystem();
                storeIsPatient(isPatient);
                if (isPatient) {

                    bttodate.setVisibility(View.GONE);
                } else {


                    bttodate.setVisibility(View.VISIBLE);
                }

            }
        } catch (Exception e) {

        }

    }


    @OnClick(R.id.llroot)
    public void onRoot(View v) {

        hideSoft();
    }


    @Override
    public void onBackPressed() {

        if (isOnlyDate) {

            if (dateTimeModel.getDateToString() != null) {

                toDate = null;
                dateTimeModel.setFormatToDate(null);
                dateTimeModel.setDateToString(null);

                tvtodate.setVisibility(View.GONE);
                tvtodate.setText("");
                tvfromdate.setVisibility(View.VISIBLE);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                // setFromDate();


                setFromDate(date);

            } else if (dateTimeModel.getDateString() != null) {

                date = null;
                dateTimeModel.setFormatDate(null);
                dateTimeModel.setDateString(null);
                tvtodate.setVisibility(View.GONE);
                tvtodate.setText("");
                tvfromdate.setText("");
                tvfromdate.setVisibility(View.GONE);
                setFromDate(null);
                isOnlyDate = false;
                bttodate.setVisibility(View.VISIBLE);

                if(getIntent().hasExtra(Constants.INVITE))
                {
                    btspecific.setVisibility(View.GONE);
                }else
                btspecific.setVisibility(View.VISIBLE);

            } else {
                dateTimeModel.setFormatDate(null);
                dateTimeModel.setDateString(null);
                dateTimeModel.setFormatToDate(null);
                dateTimeModel.setDateToString(null);
                tvfromdate.setVisibility(View.GONE);
                tvtodate.setVisibility(View.GONE);
                tvtodate.setText("");
                tvtodate.setText("");
                date = null;
                toDate = null;
            }




        } else
            super.onBackPressed();
    }

    @OnClick(R.id.tvback)
    public void onBack(View v) {

        hideSoft();
        onBackPressed();
    }

    @OnClick(R.id.ivhome)
    public void onHome(View v) {
        onBackPressed();

    }

    private void onRooms(Rooms rooms) {


        hidePD();
        if (rooms != null && rooms.getRoom() != null) {

            Intent i = new Intent(this, BookSpecificRoomActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra(Constants.BOOKROOMSPECIFIC, true);
            startActivity(i);

        } else {

            showToast(getString(R.string.noroom));
        }

    }


    private void getRooms() {

        showPD();
        setRetrofit(getApp().getTcpIp());
        mainApi = retrofit.create(MainApi.class);

        SoapGetRoomsRequestEnvelope soapGetRoomsRequestEnvelope = new SoapGetRoomsRequestEnvelope();

        GetRoomsRequestBody getRoomsRequestBody = new GetRoomsRequestBody();

        GetRooms getRooms = new GetRooms();
        getRooms.setAuthToken(getAuthTokenPref_Mobile());
        if (getStoreCitySelect() != null && getStoreCitySelect().getCityCode() != null)
            getRooms.setA_iCityCode(getStoreCitySelect().getCityCode());
        else
            getRooms.setA_iCityCode("");
        getRooms.setA_iCapacity("0");
        getRooms.setA_iUserId(employModel.getUSERID());
        getRoomsRequestBody.setGetRooms(getRooms);
        soapGetRoomsRequestEnvelope.setBody(getRoomsRequestBody);

        Observable<SoapGetRoomsResponseEnvelope> lClientDetailsObservable =
                mainApi.getRooms(soapGetRoomsRequestEnvelope);
        lClientDetailsObservable.
                subscribeOn(Schedulers.newThread())
                .map(this::onSoapRooms)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onRooms, this::onError);

    }

    private Rooms onSoapRooms(SoapGetRoomsResponseEnvelope soapGetRoomsResponseEnvelope) {


        GetRoomsResponseBody getRoomsResponseBody = soapGetRoomsResponseEnvelope.getBody();

        return getRoomsResponseBody.getGetRoomsResponse().getGetRoomsResult().getRooms();
    }

    @OnClick(R.id.btspecific)
    public void onSpecific(View v) {

        getRooms();
    }


    @OnClick(R.id.bttodate)
    public void onToDate(View v) {

        isOnlyDate = true;

        bttodate.setVisibility(View.GONE);
        btspecific.setVisibility(View.GONE);
        if (dateTimeModel.getDateString() != null) {
            setOnlyDate();
        } else {

            setFromDate(null);
            tvfromdate.setVisibility(View.VISIBLE);
            tvfromdate.setText("From Date: " + dateTimeModel.getDateString());

            tvtodate.setVisibility(View.VISIBLE);
            tvtodate.setText("To Date: ");
        }

    }

    private void setFromDate(Date date) {

        if (date == null) {

            Calendar calendar = Calendar.getInstance();
            calendarView.setDateSelected(CalendarDay.from(LocalDate.now()), false);
            this.date = Constants.formatDateFromLocalDate(CalendarDay.from(LocalDate.now()).toString());
            dateTimeModel.setDateString(Constants.getSelectedDate(this.date));
            dateTimeModel.setFormatDate(Constants.formatDate(this.date));

            if (dateSeletedDecorator != null) {
                calendarView.removeDecorator(dateSeletedDecorator);
            }
            if (dateSeletedDecorator == null)
                dateSeletedDecorator = new DateSeletedDecorator(BookMeetingsActivity.this);
            dateSeletedDecorator.setDate(CalendarDay.from(LocalDate.now()));
            calendarView.addDecorator(dateSeletedDecorator);
            calendarView.invalidateDecorators();
            tvtodate.setVisibility(View.GONE);
            tvtodate.setText("");
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendarView.setDateSelected(CalendarDay.from(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)), false);
            this.date = Constants.formatDateFromLocalDate(CalendarDay.from(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).toString());
            dateTimeModel.setDateString(Constants.getSelectedDate(this.date));
            dateTimeModel.setFormatDate(Constants.formatDate(this.date));

            if (dateSeletedDecorator != null) {
                calendarView.removeDecorator(dateSeletedDecorator);
            }
            if (dateSeletedDecorator == null)
                dateSeletedDecorator = new DateSeletedDecorator(BookMeetingsActivity.this);
            dateSeletedDecorator.setDate(CalendarDay.from(LocalDate.now()));
            calendarView.addDecorator(dateSeletedDecorator);
            calendarView.invalidateDecorators();
            tvtodate.setVisibility(View.GONE);
            tvtodate.setText("");

        }

    }

    private void setOnlyDate() {

        if (dateTimeModel != null) {

            if (dateTimeModel.getDateString() != null) {
                tvfromdate.setVisibility(View.VISIBLE);
                tvfromdate.setText("From Date: " + dateTimeModel.getDateString());

                tvtodate.setVisibility(View.VISIBLE);
                tvtodate.setText("To Date:");

            } else {
                if (calendarView.getSelectedDate() != null) {
                    setCurrentDate(calendarView.getSelectedDate());
                } else if (calendarView.getCurrentDate() != null) {
                    setCurrentDate(calendarView.getCurrentDate());
                } else if (calendarView.getMinimumDate() != null) {
                    setCurrentDate(calendarView.getMinimumDate());
                }

                tvfromdate.setText("From Date: " + dateTimeModel.getDateString());

            }

            if (dateTimeModel.getDateToString() != null) {
                tvtodate.setVisibility(View.VISIBLE);
                tvtodate.setText("To Date: " + dateTimeModel.getDateToString());
            }
        }

        if (etsubject.getText().toString().trim().length() > 0) {

            setSubject(etsubject.getText().toString().trim());
            dateTimeModel.setSubject(etsubject.getText().toString().trim());
        }


    }


    @OnClick(R.id.tvnext)
    public void onNext(View v) {



        if (etsubject.getText().toString().trim().length() > 0) {

            setSubject(etsubject.
                    getText().toString().trim());

            if (!isOnlyDate) {

                if (date != null) {

                    Intent i = new Intent(this, TimePickerActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    dateTimeModel.setDateString(Constants.getSelectedDate(date));
                    dateTimeModel.setFormatDate(Constants.formatDate(date));
                    dateTimeModel.setSubject(etsubject.getText().toString().trim());


                    if (dateTimeModel.getFormatDate().
                            equalsIgnoreCase(Constants.formatDate(new Date()))) {
                        dateTimeModel.setToday(true);
                    } else {
                        dateTimeModel.setToday(false);
                    }
                    i.putExtra(Constants.DATETIMEMODEL, mGson.toJson(dateTimeModel));
                    i.putExtra(Constants.BOOKROOM, getIntent().getBooleanExtra(Constants.BOOKROOM, false));
                    startActivity(i);
                } else {
                    showToast("Select date");
                }
            } else {
                if (dateTimeModel.isInvite()) {

                    if(dateTimeModel!=null)
                    dateTimeModel.setSubject(etsubject.getText().toString().trim());

                    Intent i = new Intent(this, ConfirmationActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    if (getIntent().hasExtra(Constants.BOOKROOM))
                        i.putExtra(Constants.BOOKROOM, getIntent().getStringExtra(Constants.BOOKROOM));
                    i.putExtra(Constants.DATETIMEMODEL, mGson.toJson(dateTimeModel));
                    startActivity(i);
                } else if (getStoreCitySelect() != null
                        && getStoreCitySelect().getSkipRoom().equalsIgnoreCase("False")) {
                    Intent i = new Intent(this, RoomListActivity.class);

                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    dateTimeModel.setBook(true);
                    i.putExtra(Constants.DATETIMEMODEL, mGson.toJson(dateTimeModel));
                    startActivity(i);
                } else {

                    if(dateTimeModel!=null)
                    dateTimeModel.setSubject(etsubject.getText().toString().trim());

                    Intent i = new Intent(this, ConfirmationActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra(Constants.DATETIMEMODEL, mGson.toJson(dateTimeModel));
                    startActivity(i);
                }

            }
        } else {

            showToast("Enter subject");
        }

    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget,
                               @NonNull CalendarDay date, boolean selected) {


        if (isOnlyDate) {

            if (dateTimeModel.getDateString() != null) {
                Date todate = Constants.formatDateFromLocalDate(date.getDate().toString());
                if (this.date.before(todate)) {

                    this.toDate = Constants.formatDateFromLocalDate(date.getDate().toString());
                    dateTimeModel.setDateToString(Constants.getSelectedDate(this.toDate));
                    dateTimeModel.setFormatToDate(Constants.formatDate(this.toDate));

                    if (dateSeletedDecorator != null) {
                        calendarView.removeDecorator(dateSeletedDecorator);
                    }
                    if (dateSeletedDecorator == null)
                        dateSeletedDecorator = new DateSeletedDecorator(BookMeetingsActivity.this);
                    dateSeletedDecorator.setDate(date);
                    calendarView.addDecorator(dateSeletedDecorator);
                    calendarView.invalidateDecorators();

                    tvtodate.setVisibility(View.GONE);

                    tvtodate.setText("");
                    setOnlyDate();
                    //1253

                    if (getIntent().hasExtra(Constants.INVITE))
                        dateTimeModel.setInvite(true);
                    else
                        dateTimeModel.setInvite(false);

                    dateTimeModel.setTime(true);

                    if (dateTimeModel.getSubject() == null || dateTimeModel.getSubject().trim().length() == 0) {
                        showMessage("Please enter subject.");

                    } else {

                    }
                }
            }
        } else {
            setCurrentDate(date);
        }

    }

    private void setCurrentDate(CalendarDay date) {

        if (date.isBefore(CalendarDay.from(LocalDate.now()))) {

            IpAddress.e("date", date.getDate().toString() + "");


        } else {

            this.date = Constants.formatDateFromLocalDate(date.getDate().toString());
            Date date1 = new Date();
            if (Constants.formatDate(date1).
                    equalsIgnoreCase(Constants.formatDate(this.date))) {
                dateTimeModel.setToday(true);

            } else {
                dateTimeModel.setToday(false);

            }

            dateTimeModel.setDateString(Constants.getSelectedDate(this.date));
            dateTimeModel.setFormatDate(Constants.formatDate(this.date));

            if (dateSeletedDecorator != null) {
                calendarView.removeDecorator(dateSeletedDecorator);
            }
            if (dateSeletedDecorator == null)
                dateSeletedDecorator = new DateSeletedDecorator(BookMeetingsActivity.this);
            dateSeletedDecorator.setDate(date);
            calendarView.addDecorator(dateSeletedDecorator);
            calendarView.invalidateDecorators();

            if (isOnlyDate) {

                tvfromdate.setVisibility(View.VISIBLE);
                tvfromdate.setText("From Date: " + dateTimeModel.getDateString());
                tvtodate.setVisibility(View.VISIBLE);
                tvtodate.setText("To Date: ");
            }
        }
    }
}

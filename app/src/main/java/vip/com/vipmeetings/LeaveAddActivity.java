package vip.com.vipmeetings;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vip.com.vipmeetings.body.AddContactLeaveResponseBody;
import vip.com.vipmeetings.body.AddContectLeaveRequestBody;
import vip.com.vipmeetings.body.DeleteContectLeaveRequestBody;
import vip.com.vipmeetings.body.GetContactLeaveResponseBody;
import vip.com.vipmeetings.body.GetContectLeaveRequestBody;
import vip.com.vipmeetings.body.UpdateContactLeaveResponseBody;
import vip.com.vipmeetings.body.UpdateContectLeaveRequestBody;
import vip.com.vipmeetings.envelope.SoapAddContactLeaveRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapAddContactLeaveResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapDeleteContactLeaveRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapDeleteContactLeaveResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetContactLeaveRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetContactLeaveResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapUpdateContactLeaveRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapUpdateContactLeaveResponseEnvelope;
import vip.com.vipmeetings.interfaces.MainApi;
import vip.com.vipmeetings.models.ContactLeaves;
import vip.com.vipmeetings.models.Leave;
import vip.com.vipmeetings.models.Status;
import vip.com.vipmeetings.request.AddContactLeave;
import vip.com.vipmeetings.request.DeleteContactLeave;
import vip.com.vipmeetings.request.GetContactLeaves;
import vip.com.vipmeetings.request.UpdateContactLeave;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 29/05/17.
 */

public class LeaveAddActivity extends BaseActivity implements
        OnDateSelectedListener {


    @BindView(R.id.tvnext)
    TextView tvnext;


    @BindView(R.id.ivhome)
    ImageView ivhome;

    @BindView(R.id.tvstartdate)
    TextView tvstartdate;

    @BindView(R.id.tvenddate)
    TextView tvenddate;

    @BindView(R.id.tvreasonleave)
    TextView tvreasonleave;


    @BindView(R.id.rvleave)
    RecyclerView rvleave;

    @BindView(R.id.rvleave1)
    RecyclerView rvleave1;

    @BindView(R.id.clleave)
    ConstraintLayout clleave;

    @BindView(R.id.tvedit)
    TextView tvedit;

    @BindView(R.id.calendarView)
    MaterialCalendarView calendarView;

    DateSeletedDecorator oneDayDecorator_red;
    boolean startDate = true;
    boolean endDate = false;

    String[] leaves = new String[]{"Holiday", "Personal Leave", "Sick Leave", "Busy", "Meeting", "Others"};
    String leaveReason;
    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat simpleDateFormat2;

    String start_Date, end_Date;

    Date today_date, last_date;

    Leave leave;
    OneDayDecorator_RED3 oneDayDecorator_red3;

    LocalDate localDate;

    List<Leave> leaveList;
    LeaveAdapter2 leaveAdapter2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaveadd);
        ButterKnife.bind(this);
        setData(getIntent());
        rvleave1.setLayoutManager(new LinearLayoutManager(this));
        leaveList = new ArrayList<>();
        leaveAdapter2 = new LeaveAdapter2();
        rvleave1.setAdapter(leaveAdapter2);
        getLeaves();
    }

    private void setData(Intent intent) {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:[:ss]");
        oneDayDecorator_red3 = new OneDayDecorator_RED3(this);

        leave = mGson.fromJson(intent.getStringExtra(Constants.LEAVE), Leave.class);
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);


        calendarView.setOnDateChangedListener(this);

        if (leave != null) {
            tvnext.setText("Update");

            start_Date = leave.getFromDateTimeYMDHM();
            end_Date = leave.getToDateTimeYMDHM();
            leaveReason = leave.getReasonForLeave();

            if (start_Date != null) {

                try {
                    tvstartdate.setText(simpleDateFormat.format(start_Date));
                } catch (Exception e) {
                    try {
                        tvstartdate.setText(simpleDateFormat2.format(start_Date));
                    } catch (Exception ee) {
                        tvstartdate.setText(start_Date);
                    }
                }


                try {
                    setMaterialDate(simpleDateFormat.parse(start_Date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (end_Date != null) {
                tvenddate.setText(end_Date);
            }

            if (leave.getToDateTimeYMDHM() != null) {
                try {
                    tvenddate.setText(simpleDateFormat.format(leave.getToDateTimeYMDHM()));
                } catch (Exception e) {
                    try {
                        tvenddate.setText(simpleDateFormat2.format(leave.getToDateTimeYMDHM()));
                    } catch (Exception ee) {
                        tvenddate.setText(leave.getToDateTimeYMDHM());
                    }
                }
                end_Date = tvenddate.getText().toString().trim();
            }

            if (leaveReason != null)
                tvreasonleave.setText(leaveReason);


        } else {
            tvnext.setText("Save");
            setMaterialDate(null);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        clleave.setVisibility(View.VISIBLE);
        rvleave1.setVisibility(View.GONE);
        setData(intent);

    }

    public Bitmap rotateBitmap(int source, float angle) {
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), source);
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public LocalDate fromDate(Date date) {
        Calendar nextYear1 = Calendar.getInstance();
        nextYear1.setTime(date);
        int year = nextYear1.get(Calendar.YEAR);
        int month = nextYear1.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = nextYear1.get(Calendar.DAY_OF_MONTH);

        return LocalDate.of(year, month, day);
    }

    public Date fromToLocalDate(LocalDate date) {

        if (startDate) {
            localDate = date;
        }

        return Constants.formatDateFromLocalDate(date.toString());
    }


    private void setMaterialDate(Date date) {


        if (oneDayDecorator_red != null) {
            calendarView.removeDecorator(oneDayDecorator_red);
        }
        if (today_date != null && oneDayDecorator_red != null) {

            Calendar nextYear1 = Calendar.getInstance();
            nextYear1.setTime(today_date);
            int year = nextYear1.get(Calendar.YEAR);
            int month = nextYear1.get(Calendar.MONTH) + 1; // Note: zero based!
            int day = nextYear1.get(Calendar.DAY_OF_MONTH);
            LocalDate localDate = LocalDate.of(year, month, day);
            OneDayDecorator_TRANSPARENT oneDayDecorator_transparent = new
                    OneDayDecorator_TRANSPARENT(this);
            oneDayDecorator_transparent.setDate(CalendarDay.from(localDate));
            calendarView.addDecorator(oneDayDecorator_transparent);
        }
        if (last_date != null && oneDayDecorator_red != null) {
            Calendar nextYear1 = Calendar.getInstance();
            nextYear1.setTime(last_date);
            int year = nextYear1.get(Calendar.YEAR);
            int month = nextYear1.get(Calendar.MONTH) + 1; // Note: zero based!
            int day = nextYear1.get(Calendar.DAY_OF_MONTH);
            LocalDate localDate = LocalDate.of(year, month, day);

            OneDayDecorator_TRANSPARENT oneDayDecorator_transparent = new
                    OneDayDecorator_TRANSPARENT(this);
            oneDayDecorator_transparent.setDate(CalendarDay.from(localDate));
            calendarView.addDecorator(oneDayDecorator_transparent);

        }


        if (date == null) {

            if (startDate) {
                today_date = new Date();
            } else {

                last_date = null;
            }

        } else {

            if (startDate) {
                today_date = date;
            } else {
                last_date = date;
            }
        }


        if (startDate) {


            calendarView.state().edit()
                    .setFirstDayOfWeek(DayOfWeek.MONDAY)
                    .setMinimumDate(CalendarDay.today())
                    .setMaximumDate(nextYear())
                    .setCalendarDisplayMode(CalendarMode.MONTHS)
                    .commit();


            tvreasonleave.setBackgroundResource(R.drawable.recttime);
            rvleave.setVisibility(View.GONE);
            startDate = true;
            endDate = false;
            last_date = null;
            tvenddate.setText("");
            calendarView.setVisibility(View.VISIBLE);
            end_Date = null;
            tvenddate.setText("");
            tvstartdate.setBackgroundResource(R.drawable.recttvet);
            tvenddate.setBackgroundResource(R.drawable.recttime);


            tvenddate.setText("");


            if (today_date != null) {
                start_Date = simpleDateFormat.format(today_date);
                tvstartdate.setText(simpleDateFormat.format(today_date));
                oneDayDecorator_red = new DateSeletedDecorator(this);

                Calendar nextYear1 = Calendar.getInstance();

                nextYear1.setTime(today_date);

                int year = nextYear1.get(Calendar.YEAR);
                int month = nextYear1.get(Calendar.MONTH) + 1; // Note: zero based!
                int day = nextYear1.get(Calendar.DAY_OF_MONTH);

                LocalDate localDate = LocalDate.of(year, month, day);

                oneDayDecorator_red.setDate(CalendarDay.from(localDate));
                calendarView.addDecorators(
                        oneDayDecorator_red3, oneDayDecorator_red, new OneDayDecorator_RED4(this));
            }


        } else if (endDate) {

            calendarView.state().edit()
                    .setFirstDayOfWeek(DayOfWeek.SUNDAY)
                    .setMinimumDate(localDate)
                    .setMaximumDate(nextYear())
                    .setCalendarDisplayMode(CalendarMode.MONTHS)
                    .commit();


            tvreasonleave.setBackgroundResource(R.drawable.recttime);
            rvleave.setVisibility(View.GONE);
            calendarView.setVisibility(View.VISIBLE);
            tvenddate.setBackgroundResource(R.drawable.recttvet);
            tvstartdate.setBackgroundResource(R.drawable.recttime);
            if (last_date != null) {
                end_Date = simpleDateFormat.format(last_date);
                tvenddate.setText(simpleDateFormat.format(last_date));
                oneDayDecorator_red = new DateSeletedDecorator(this);

                Calendar nextYear1 = Calendar.getInstance();

                nextYear1.setTime(last_date);

                int year = nextYear1.get(Calendar.YEAR);
                int month = nextYear1.get(Calendar.MONTH) + 1; // Note: zero based!
                int day = nextYear1.get(Calendar.DAY_OF_MONTH);

                LocalDate localDate = LocalDate.of(year, month, day);

                oneDayDecorator_red.setDate(CalendarDay.from(localDate));
                calendarView.addDecorators(
                        oneDayDecorator_red3, oneDayDecorator_red,
                        new OneDayDecorator_RED4(this));
            }

        }
        calendarView.invalidateDecorators();
    }


    private void onAllLeaves(SoapGetContactLeaveResponseEnvelope soapGetContactLeaveResponseEnvelope) {


        GetContactLeaveResponseBody getContactLeaveResponseBody =
                soapGetContactLeaveResponseEnvelope.getBody();

        if (getContactLeaveResponseBody != null
                && getContactLeaveResponseBody.getGetContactLeavesResponse() != null
                && getContactLeaveResponseBody.getGetContactLeavesResponse().getGetContactLeavesResult() != null) {

            ContactLeaves contactLeaves = getContactLeaveResponseBody.getGetContactLeavesResponse()
                    .getGetContactLeavesResult().getContactLeaves();
            leaveList.clear();
            if (contactLeaves != null) {
                if (contactLeaves.getLeaves() != null
                        && contactLeaves.getLeaves().size() > 0) {

                    leaveList = contactLeaves.getLeaves();


//                    Intent i = new Intent(this, LeaveListActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);
                } else if (contactLeaves.getEmpty() != null) {

                    //  showToast(getString(R.string.empty_leaves));
                } else if (contactLeaves.getError() != null) {

                    // showToast(getString(R.string.empty_leaves));
                }
            } else {
                // showToast(getString(R.string.empty_leaves));
            }
        } else {
            //  showToast(getString(R.string.empty_leaves));
        }

        if (leaveList != null && leaveList.size() > 0) {
            rvleave1.setVisibility(View.VISIBLE);
            clleave.setVisibility(View.GONE);

        } else {
            rvleave1.setVisibility(View.GONE);
            clleave.setVisibility(View.VISIBLE);
        }
        leaveAdapter2.notifyDataSetChanged();
        hidePD();
    }

    private void getLeaves() {


        showPD();
        setRetrofit(getApp().getTcpIp());
        mainApi = retrofit.create(MainApi.class);
        SoapGetContactLeaveRequestEnvelope soapGetContactLeaveRequestEnvelope = new SoapGetContactLeaveRequestEnvelope();
        GetContactLeaves getContactLeaves = new GetContactLeaves();
        getContactLeaves.setAuthToken(getAuthTokenPref_Mobile());
        getContactLeaves.setA_sContactEmail(getEmail());
        GetContectLeaveRequestBody getContectLeaveRequestBody = new GetContectLeaveRequestBody();
        getContectLeaveRequestBody.setGetContactLeaves(getContactLeaves);
        soapGetContactLeaveRequestEnvelope.setBody(getContectLeaveRequestBody);

        Observable<SoapGetContactLeaveResponseEnvelope> lClientDetailsObservable = mainApi.
                getContactLeaves(soapGetContactLeaveRequestEnvelope);

        lClientDetailsObservable.
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onAllLeaves, this::onError);

    }

    public void onError(Throwable throwable) {

        clleave.setVisibility(View.VISIBLE);
        rvleave1.setVisibility(View.GONE);
        hidePD();

    }


    @OnClick(R.id.tvstartdate)
    public void onStartDate(View v) {


        rvleave1.setVisibility(View.GONE);
        clleave.setVisibility(View.VISIBLE);
        startDate = true;
        endDate = false;
        if (today_date != null) {
            setMaterialDate(today_date);
        } else
            setMaterialDate(null);


    }

    @OnClick(R.id.tvenddate)
    public void onEndDate(View v) {

        rvleave1.setVisibility(View.GONE);
        clleave.setVisibility(View.VISIBLE);
        startDate = false;
        endDate = true;
        setMaterialDate(last_date);

    }

    @OnClick(R.id.tvreasonleave)
    public void onLeave(View v) {

        rvleave1.setVisibility(View.GONE);
        clleave.setVisibility(View.VISIBLE);
        tvenddate.setBackgroundResource(R.drawable.recttime);
        tvstartdate.setBackgroundResource(R.drawable.recttime);

        tvreasonleave.setBackgroundResource(R.drawable.recttvet);
        calendarView.setVisibility(View.GONE);
        rvleave.setVisibility(View.VISIBLE);

        LeaveAdapter leaveAdapter = new LeaveAdapter();
        rvleave.setAdapter(leaveAdapter);

    }


    @OnClick(R.id.tvnext)
    public void onNext(View v) {


        if (start_Date != null) {


            if (end_Date != null) {

                if (leaveReason != null) {


                    if (hasNetwork()) {
                        if (leave == null) {

                            addLeave();
                        } else {

                            updateLeave();
                        }
                    }


                } else {
                    showToast(getString(R.string.selectReason));
                }
            } else {
                showToast(getString(R.string.selectenddate));
            }


        } else {
            showToast(getString(R.string.selectstartdate));
        }


    }

    private void updateLeave() {

        showPD();
        setRetrofit(getApp().getTcpIp());
        mainApi = retrofit.create(MainApi.class);

        if (leave != null && leave.getP_LeaveId() != null) {
            SoapUpdateContactLeaveRequestEnvelope soapUpdateContactLeaveRequestEnvelope = new
                    SoapUpdateContactLeaveRequestEnvelope();

            UpdateContactLeave updateContactLeave = new UpdateContactLeave();

            updateContactLeave.setA_sLeaveId(leave.getP_LeaveId());
            updateContactLeave.setA_sFromDateTimeYMD(start_Date);
            updateContactLeave.setA_sToDateTimeYMD(end_Date);
            updateContactLeave.setAuthToken(getAuthTokenPref_Mobile());
            updateContactLeave.setA_sReasonForLeave(leaveReason);

            UpdateContectLeaveRequestBody updateContectLeaveRequestBody = new UpdateContectLeaveRequestBody();

            updateContectLeaveRequestBody.setUpdateContactLeave(updateContactLeave);

            soapUpdateContactLeaveRequestEnvelope.setBody(updateContectLeaveRequestBody);


            Observable<SoapUpdateContactLeaveResponseEnvelope> lClientDetailsObservable = mainApi.
                    updateContactLeave(soapUpdateContactLeaveRequestEnvelope);

            lClientDetailsObservable.
                    subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onAddLeave, this::onError);
        }
    }

    private void addLeave() {


        showPD();
        setRetrofit(getApp().getTcpIp());
        mainApi = retrofit.create(MainApi.class);

        SoapAddContactLeaveRequestEnvelope soapUpdateContactLeaveRequestEnvelope = new
                SoapAddContactLeaveRequestEnvelope();
        AddContactLeave updateContactLeave = new AddContactLeave();

        updateContactLeave.setA_sContactEmail(getEmail());
        updateContactLeave.setA_sFromDateTimeYMD(start_Date);
        updateContactLeave.setA_sToDateTimeYMD(end_Date);
        updateContactLeave.setAuthToken(getAuthTokenPref_Mobile());
        updateContactLeave.setA_sReasonForLeave(leaveReason);

        AddContectLeaveRequestBody updateContectLeaveRequestBody = new AddContectLeaveRequestBody();

        updateContectLeaveRequestBody.setAddContactLeave(updateContactLeave);

        soapUpdateContactLeaveRequestEnvelope.setBody(updateContectLeaveRequestBody);


        Observable<SoapAddContactLeaveResponseEnvelope> lClientDetailsObservable = mainApi.
                addContactLeave(soapUpdateContactLeaveRequestEnvelope);

        lClientDetailsObservable.
                subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onAddLeave2, this::onError);
    }

    private void onAddLeave(SoapUpdateContactLeaveResponseEnvelope soapUpdateContactLeaveResponseEnvelope) {

        hidePD();
        UpdateContactLeaveResponseBody updateContactLeaveResponseBody =
                soapUpdateContactLeaveResponseEnvelope.getBody();
        if (updateContactLeaveResponseBody != null
                && updateContactLeaveResponseBody.getUpdateContactLeaveResponse() != null
                && updateContactLeaveResponseBody.getUpdateContactLeaveResponse().getUpdateContactLeaveResult() != null) {
            Status status = updateContactLeaveResponseBody.getUpdateContactLeaveResponse().getUpdateContactLeaveResult()
                    .getStatus();
            if (status != null && status.get_status().equalsIgnoreCase("SUCCESS")) {


            }
        }
        startDate = true;
        endDate = false;

        setMaterialDate(null);

        tvreasonleave.setText("");
        tvenddate.setText("");
        tvnext.setText("Save");
        getLeaves();
    }

    private void onAddLeave2(SoapAddContactLeaveResponseEnvelope soapAddContactLeaveResponseEnvelope) {


        hidePD();
        AddContactLeaveResponseBody updateContactLeaveResponseBody =
                soapAddContactLeaveResponseEnvelope.getBody();

        if (updateContactLeaveResponseBody != null
                && updateContactLeaveResponseBody.getAddContactLeaveResponse() != null
                && updateContactLeaveResponseBody.getAddContactLeaveResponse()
                .getAddContactLeaveResult() != null)

        {
            Status status = updateContactLeaveResponseBody.getAddContactLeaveResponse()
                    .getAddContactLeaveResult()
                    .getStatus();
            if (status != null && status.get_status().equalsIgnoreCase("SUCCESS")) {




            }

        }
        startDate = true;
        endDate = false;
        tvenddate.setText("");
        tvreasonleave.setText("");
        setMaterialDate(null);
        tvnext.setText("Save");
        getLeaves();
    }

    @OnClick(R.id.ivhome)
    public void onHome(View v) {

        gotoHome();

    }

    @OnClick(R.id.tvback)
    public void onBack(View v) {

        onBackPressed();
    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {


        if (startDate) {

            today_date = fromToLocalDate(date.getDate());
            start_Date = simpleDateFormat.format(today_date);
            setMaterialDate(today_date);
        } else {
            last_date = fromToLocalDate(date.getDate());
            end_Date = simpleDateFormat.format(last_date);
            setMaterialDate(last_date);
        }


    }


    public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.VH> {


        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(getLayoutInflater().inflate(R.layout.inflate_addleave, parent, false));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {


            holder.tvleave.setText(leaves[position]);


            if (leaveReason != null)
                if (leaveReason.equalsIgnoreCase(leaves[position])) {

                }


            holder.clleave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    leaveReason = leaves[holder.getAdapterPosition()];
                    tvreasonleave.setText(leaveReason);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return leaves.length;
        }

        public class VH extends RecyclerView.ViewHolder {
            @BindView(R.id.tvleave)
            TextView tvleave;
            @BindView(R.id.cleave)
            ConstraintLayout clleave;

            public VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }


    }

    public class LeaveAdapter2 extends RecyclerView.Adapter<LeaveAdapter2.VH> {


        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {

            return new VH(getLayoutInflater().inflate(R.layout.inflate_leave, parent, false));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {


            Leave leave = leaveList.get(position);

            if (position == 0) {
                holder.tvfuture.setVisibility(View.VISIBLE);
            } else {
                holder.tvfuture.setVisibility(View.GONE);
            }

            holder.tvreason.setText(leave.getReasonForLeave());
            holder.tvdate.setText(leave.getFromDateTimeYMDHM() + " to " + leave.getToDateTimeYMDHM());

            holder.btdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(LeaveAddActivity.this);


                    builder.setMessage(getString(R.string.delete_leave));
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                            onDeleteLeave(leaveList.get(holder.getAdapterPosition()).getP_LeaveId());

                        }
                    });

                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                        }
                    });

                    builder.show();

                }
            });

            holder.btedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(LeaveAddActivity.this,
                            LeaveAddActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    i.putExtra(Constants.LEAVE, mGson.toJson(leaveList.get(holder.getAdapterPosition())));
                    startActivity(i);
                }
            });

        }

        @Override
        public int getItemCount() {
            return leaveList.size();
        }

        public class VH extends RecyclerView.ViewHolder {


            @BindView(R.id.tvreason)
            TextView tvreason;
            @BindView(R.id.tvdate)
            TextView tvdate;

            @BindView(R.id.tvfuture)
            TextView tvfuture;


            @BindView(R.id.btdelete)
            ImageView btdelete;

            @BindView(R.id.btedit)
            ImageView btedit;

            @BindView(R.id.llupdate)
            ViewGroup llupdate;

            public VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }


    }

    private void onDeleteLeave(String p_leaveId) {


        showPD();
        setRetrofit(getApp().getTcpIp());
        mainApi = retrofit.create(MainApi.class);

        SoapDeleteContactLeaveRequestEnvelope soapDeleteContactLeaveRequestEnvelope = new SoapDeleteContactLeaveRequestEnvelope();

        DeleteContactLeave deleteContactLeave = new DeleteContactLeave();
        deleteContactLeave.setAuthToken(getAuthTokenPref_Mobile());
        deleteContactLeave.setA_sLeaveId(p_leaveId);

        DeleteContectLeaveRequestBody deleteContectLeaveRequestBody = new DeleteContectLeaveRequestBody();
        deleteContectLeaveRequestBody.setDeleteContactLeave(deleteContactLeave);
        soapDeleteContactLeaveRequestEnvelope.setBody(deleteContectLeaveRequestBody);

        //getClientID(), p_leaveId
        //14DNC0
        Observable<SoapDeleteContactLeaveResponseEnvelope> lClientDetailsObservable = mainApi.deleteContactLeave(
                soapDeleteContactLeaveRequestEnvelope);

        lClientDetailsObservable.
                subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDelete, this::onError);


    }

    private void onDelete(SoapDeleteContactLeaveResponseEnvelope soapDeleteContactLeaveResponseEnvelope) {

        hidePD();
        Status status = soapDeleteContactLeaveResponseEnvelope.getBody()
                .getDeleteContactLeaveResponse().getDeleteContactLeaveResult()
                .getStatus();
        if (status != null && status.get_status().equalsIgnoreCase("SUCCESS"))

            showToast("Leave deleted successfully");

        getLeaves();
    }

}

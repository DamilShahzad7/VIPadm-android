package vip.com.vipmeetings;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vip.com.vipmeetings.body.MeetingsRequestBody;
import vip.com.vipmeetings.body.MeetingsResponseBody;
import vip.com.vipmeetings.envelope.SoapMeetingsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapMeetingsResponseEnvelope;
import vip.com.vipmeetings.interfaces.MainApi;
import vip.com.vipmeetings.models.Meeting;
import vip.com.vipmeetings.models.Meetings;
import vip.com.vipmeetings.request.GetMeetingsRequest;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 29/05/17.
 */

public class MyMeetingsActivity extends BaseActivity {


    @BindView(R.id.rvmeetings)
    RecyclerView rvmeetings;

    @BindView(R.id.tvnext)
    ImageView tvnext;

    @BindView(R.id.ivhome)
    ImageView ivhome;

    List<Meeting> meetingList;
    MeetingsAdapter meetingsAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymeetings);
        ButterKnife.bind(this);
        tvnext.setVisibility(View.GONE);
        meetingList = new ArrayList<>();

        if (getIntent().hasExtra(Constants.MEETING)) {

            meetingList = mGson.fromJson(getIntent().getStringExtra(Constants.MEETING),
                    new TypeToken<List<Meeting>>() {
                    }.getType());

        }

        meetingsAdapter = new MeetingsAdapter();
        rvmeetings.setAdapter(meetingsAdapter);
    }


    private void getMeetings() {


        showPD();
        setRetrofit(getApp().getTcpIp());
        mainApi = retrofit.create(MainApi.class);


        SoapMeetingsRequestEnvelope soapMeetingsRequestEnvelope = new SoapMeetingsRequestEnvelope();

        MeetingsRequestBody meetingsRequestBody = new MeetingsRequestBody();

        GetMeetingsRequest getMeetingsRequest = new GetMeetingsRequest();

        getMeetingsRequest.setA_sUserEmail(getEmail());
        getMeetingsRequest.setAuthToken(getAuthTokenPref_Mobile());

        meetingsRequestBody.setGetMeetings(getMeetingsRequest);
        soapMeetingsRequestEnvelope.setBody(meetingsRequestBody);

        //14DNC0
        Observable<SoapMeetingsResponseEnvelope> lClientDetailsObservable = mainApi.getMeetings(soapMeetingsRequestEnvelope);

        lClientDetailsObservable.
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onMeetings, this::onError);

    }

    private void onMeetings(SoapMeetingsResponseEnvelope soapMeetingsResponseEnvelope) {

        hidePD();
        MeetingsResponseBody meetingsResponseBody = soapMeetingsResponseEnvelope.getBody();

        if (meetingsResponseBody != null
                && meetingsResponseBody.getGetMeetingsResponse() != null) {

            Meetings meetings = meetingsResponseBody.getGetMeetingsResponse().
                    getGetResponsibleDetailsResult().getMeetings();

            if (meetings != null) {
                if (meetings.getStatus() != null) {

                    if (meetings.getStatus().get_status().equalsIgnoreCase("NO Meetings")) {

                        meetingList.clear();
                        meetingsAdapter.notifyDataSetChanged();
                        finish();
                    } else if (meetings.getStatus().get_status().equalsIgnoreCase("AVAILABLE")) {

                        meetingList.clear();
                        meetingList.addAll(meetings.getMeeting());
                        meetingsAdapter.notifyDataSetChanged();


                    }
                    //AVAILABLE
                }
            } else {
                meetingList.clear();
                meetingsAdapter.notifyDataSetChanged();
            }


        } else {
            meetingList.clear();
            meetingsAdapter.notifyDataSetChanged();

        }
    }


    @OnClick(R.id.tvnext)
    public void onNext(View v) {


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.ivhome)
    public void onHome(View v) {


        onBackPressed();

    }

    @OnClick(R.id.tvback)
    public void onBack(View v) {


        onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case Constants.REQDELETE:

                    if (data != null && data.hasExtra(Constants.DELETE)) {
                        getMeetings();
                    }

                    break;

            }

        }
    }

    public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsAdapter.VH> {


        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(getLayoutInflater().inflate(R.layout.inflate_meeting, null));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {


            Meeting meeting = meetingList.get(position);

            holder.tvreason.setText(meeting.getHostName());

            holder.tvdate.setText(meeting.getFromDate() + " | " + meeting.getFromTime() + " - " +
                    meeting.getToTime() + " | " + meeting.getDescription());

            holder.llmeeting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(MyMeetingsActivity.this, MyMeetingDetailsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra(Constants.MEETING, mGson.toJson(meetingList.get(holder.getAdapterPosition())));
                    startActivityForResult(i, Constants.REQDELETE);
                }
            });
        }

        @Override
        public int getItemCount() {
            return meetingList.size();
        }

        public class VH extends RecyclerView.ViewHolder {


            @BindView(R.id.tvreason)
            TextView tvreason;
            @BindView(R.id.tvdate)
            TextView tvdate;
            @BindView(R.id.llmeeting)
            ViewGroup llmeeting;


            public VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }


    }

}

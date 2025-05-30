package vip.com.vipmeetings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vip.com.vipmeetings.body.DeleteMeetingRequestBody;
import vip.com.vipmeetings.body.DeleteMeetingResponseBody;
import vip.com.vipmeetings.body.GetMeetingDetailsVisitorsRequestBody;
import vip.com.vipmeetings.body.GetMeetingDetailsVisitorsResponseBody;
import vip.com.vipmeetings.envelope.SoapDeleteMeetingRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapDeleteMeetingResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetMeetingDetailsVisitorsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetMeetingDetailsVisitorsResponseEnvelope;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.interfaces.MainApi;
import vip.com.vipmeetings.models.Meeting;
import vip.com.vipmeetings.models.MeetingDetailsVisitor;
import vip.com.vipmeetings.models.MeetingDetailsVisitors;
import vip.com.vipmeetings.request.DeleteMeeting;
import vip.com.vipmeetings.request.GetMeetingDetailsVisitors;
import vip.com.vipmeetings.response.GetMeetingDetailsVisitorsResponse;
import vip.com.vipmeetings.response.GetMeetingDetailsVisitorsResult;
import vip.com.vipmeetings.utilities.Constants;

public class MyMeetingDetailsActivity extends BaseActivity {

    @BindView(R.id.btdelete)
    Button btdelete;

    @BindView(R.id.ivhome)
    ImageView ivhome;

    @BindView(R.id.tvdate)
    TextView tvdate;

    @BindView(R.id.tvtime)
    TextView tvtime;

    @BindView(R.id.tvsubject)
    TextView tvsubject;

    @BindView(R.id.tvroomname)
    TextView tvroomname;

    @BindView(R.id.tvparticipants)
    TextView tvparticipants;

    @BindView(R.id.tvlocation)
    TextView tvlocation;

    @BindView(R.id.ivimage)
    SimpleDraweeView ivimage;

    @BindView(R.id.tvroom)
    TextView tvroom;

    @BindView(R.id.listview_participants)
    ListView listViewParticipants;

    Meeting meeting;
    public MainApi mainApi;
    List<MeetingDetailsVisitor> meetingDetailsVisitor = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvitity_mymeetingdetails);
        ButterKnife.bind(this);

        meeting = mGson.fromJson(getIntent().getStringExtra(Constants.MEETING), Meeting.class);

        if (meeting != null) {
            tvdate.setText(meeting.getFromDate());
            tvtime.setText(meeting.getFromTime() + " - " + meeting.getToTime());
            tvsubject.setText(meeting.getDescription());
            tvroomname.setText(meeting.getRoomName());
            if(meeting.getMeetingId()==null || meeting.getMeetingId().isEmpty()){
                MeetingDetailsVisitor visitor = new MeetingDetailsVisitor();
                visitor.setFullName(meeting.getDescription());
                visitor.setInTime(meeting.getInTime());
                visitor.setOutTime(meeting.getOutTime());
                meetingDetailsVisitor.add(visitor);
                updateParticipantsList();
            }else{
                getMeetingVisitorsDetails();
            }


            if (meeting.getParticipants() != null && !meeting.getParticipants().equalsIgnoreCase("0")) {
                tvparticipants.setText("Participants count :  " + meeting.getParticipants());
            } else {
                tvparticipants.setText("");
            }

            if (meeting.getLocation() != null) {
                if (meeting.getLocation().trim().length() > 30) {
                    tvlocation.setGravity(Gravity.CENTER);
                } else {
                    tvlocation.setGravity(Gravity.START | Gravity.LEFT);
                }
                tvlocation.setText(meeting.getLocation());
            } else if (meeting.getCityName() != null) {
                if (meeting.getCityName().trim().length() > 30) {
                    tvlocation.setGravity(Gravity.CENTER);
                } else {
                    tvlocation.setGravity(Gravity.START | Gravity.LEFT);
                }
                tvlocation.setText(meeting.getCityName());
            } else {
                tvlocation.setText("");
            }

            if (meeting.getRoomName() != null && meeting.getRoomName().trim().length() > 0
                    && meeting.getRoomImage() != null && meeting.getRoomImage().trim().length() > 0) {
                tvroom.setVisibility(View.VISIBLE);
                tvroomname.setText(meeting.getRoomName());
                ivimage.setImageURI(meeting.getRoomImage());
                ivimage.setVisibility(View.VISIBLE);
            } else {
                tvroom.setVisibility(View.GONE);
                ivimage.setVisibility(View.GONE);
            }
        } else {
            tvroom.setVisibility(View.GONE);
            ivimage.setVisibility(View.GONE);
        }

        // Enable scrolling for participants TextView if needed
        //  tvparticipantname.setMovementMethod(new ScrollingMovementMethod());
    }

    private void getMeetingVisitorsDetails() {
        setOKHTTPAfterLogin();
        setRetrofit(getApp().getTcpIp());
        mainApi = retrofit.create(MainApi.class);
        SoapGetMeetingDetailsVisitorsRequestEnvelope soapGetMeetingDetailsVisitorsRequestEnvelope = new SoapGetMeetingDetailsVisitorsRequestEnvelope();

        GetMeetingDetailsVisitorsRequestBody getMeetingDetailsVisitorsRequestBody = new GetMeetingDetailsVisitorsRequestBody();

        GetMeetingDetailsVisitors getMeetingDetailsVisitors = new GetMeetingDetailsVisitors();
        getMeetingDetailsVisitors.setAuthToken(getAuthTokenPref_Mobile());
        getMeetingDetailsVisitors.setA_sMeetingId(meeting.getMeetingId());
        getMeetingDetailsVisitorsRequestBody.setGetMeetingDetailsVisitors(getMeetingDetailsVisitors);
        soapGetMeetingDetailsVisitorsRequestEnvelope.setBody(getMeetingDetailsVisitorsRequestBody);
        Observable<SoapGetMeetingDetailsVisitorsResponseEnvelope> lClientDetailsObservable =
                mainApi.getMeetingDetailsVisitors(soapGetMeetingDetailsVisitorsRequestEnvelope);
        lClientDetailsObservable.
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetMeetingDetailsVisitors, this::onError);
    }

    private void onGetMeetingDetailsVisitors(SoapGetMeetingDetailsVisitorsResponseEnvelope soapGetMeetingDetailsVisitorsResponseEnvelope) {
        showPD();
        GetMeetingDetailsVisitorsResponseBody getMeetingDetailsVisitorsResponseBody = soapGetMeetingDetailsVisitorsResponseEnvelope.getBody();

        if (getMeetingDetailsVisitorsResponseBody != null
                && getMeetingDetailsVisitorsResponseBody.getGetMeetingDetailsVisitorsResponse() != null
                && getMeetingDetailsVisitorsResponseBody.getGetMeetingDetailsVisitorsResponse() != null) {


            if (getMeetingDetailsVisitorsResponseBody
                    .getGetMeetingDetailsVisitorsResponse() != null) {
                GetMeetingDetailsVisitorsResponse getMeetingDetailsVisitorsResponse = getMeetingDetailsVisitorsResponseBody.getGetMeetingDetailsVisitorsResponse();
                if (getMeetingDetailsVisitorsResponse.getGetMeetingDetailsVisitorsResult() != null) {
                    GetMeetingDetailsVisitorsResult getMeetingDetailsVisitorsResult = getMeetingDetailsVisitorsResponse.getGetMeetingDetailsVisitorsResult();
                    if (getMeetingDetailsVisitorsResult.getMeetingDetailsVisitors() != null) {
                        MeetingDetailsVisitors getMeetingDetailsVisitors = getMeetingDetailsVisitorsResult.getMeetingDetailsVisitors();
                        if (getMeetingDetailsVisitors.getMeetingDetailsVisitor() != null) {
                            meetingDetailsVisitor = getMeetingDetailsVisitors.getMeetingDetailsVisitor();
                            // Update ListView with meetingDetailsVisitor

                            if(meetingDetailsVisitor != null && meetingDetailsVisitor.size()>0){
                                showPD();
                                updateParticipantsList();
                            }
                            else{
                                hidePD();
                            }
                        }
                        else{
                            hidePD();
                        }
                    }
                    else{
                        hidePD();
                    }
                }
                else{
                    hidePD();
                }
            }
        }
    }

    private void updateParticipantsList() {
        ParticipantsListAdapter adapter = new ParticipantsListAdapter(this, meetingDetailsVisitor);
        listViewParticipants.setAdapter(adapter);
        hidePD();
    }

    @OnClick(R.id.btdelete)
    public void onDelete(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyMeetingDetailsActivity.this);
        builder.setMessage(getString(R.string.delete_meeting));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (meeting != null && meeting.getMeetingId() != null) {
                    showPD();
                    setRetrofit(getApp().getTcpIp());
                    mainApi = retrofit.create(MainApi.class);
                    SoapDeleteMeetingRequestEnvelope soapDeleteMeetingRequestEnvelope =
                            new SoapDeleteMeetingRequestEnvelope();
                    DeleteMeetingRequestBody deleteMeetingRequestBody =
                            new DeleteMeetingRequestBody();
                    DeleteMeeting deleteMeeting = new DeleteMeeting();
                    deleteMeeting.setA_sMeetingId(meeting.getMeetingId());
                    deleteMeeting.setAuthToken(getAuthTokenPref_Mobile());
                    deleteMeetingRequestBody.setDeleteMeeting(deleteMeeting);
                    soapDeleteMeetingRequestEnvelope.setBody(deleteMeetingRequestBody);

                    Observable<SoapDeleteMeetingResponseEnvelope> lClientDetailsObservable = mainApi.deleteMeeting(soapDeleteMeetingRequestEnvelope);

                    lClientDetailsObservable
                            .subscribeOn(Schedulers.newThread())
                            .map(MyMeetingDetailsActivity.this::onSoapDeleteMeeting)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(MyMeetingDetailsActivity.this::onDeleteMeeting,
                                    MyMeetingDetailsActivity.this::onError);
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do nothing on cancel
            }
        });

        builder.show();
    }

    public void onError(Throwable throwable) {
        hidePD();
        showToast(getString(R.string.someerror));
    }

    private String onSoapDeleteMeeting(SoapDeleteMeetingResponseEnvelope soapDeleteMeetingResponseEnvelope) {
        DeleteMeetingResponseBody deleteMeetingResponseBody = soapDeleteMeetingResponseEnvelope.getBody();

        if (deleteMeetingResponseBody != null && deleteMeetingResponseBody.getDeleteMeetingResponse() != null
                && deleteMeetingResponseBody.getDeleteMeetingResponse().getDeleteMeetingResult() != null) {
            if (deleteMeetingResponseBody.getDeleteMeetingResponse().getDeleteMeetingResult().getStatus() != null) {
                return deleteMeetingResponseBody.getDeleteMeetingResponse().getDeleteMeetingResult().getStatus().get_status();
            } else if (deleteMeetingResponseBody.getDeleteMeetingResponse().getDeleteMeetingResult().getError() != null) {
                return deleteMeetingResponseBody.getDeleteMeetingResponse().getDeleteMeetingResult().getError().get_error();
            }
        }
        return null;
    }

    private void onDeleteMeeting(String status) {
        hidePD();
        if (status != null && status.equalsIgnoreCase(IpAddress.SUCCESS)) {
            Intent intent = getIntent();
            intent.putExtra(Constants.DELETE, true);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(status != null ? status : "");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Do nothing on OK
                }
            });
            builder.show();
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

    public class ParticipantsListAdapter extends ArrayAdapter<MeetingDetailsVisitor> {

        private Context mContext;
        private List<MeetingDetailsVisitor> mParticipantsList;

        public ParticipantsListAdapter(@NonNull Context context, @NonNull List<MeetingDetailsVisitor> participantsList) {
            super(context, 0, participantsList);
            mContext = context;
            mParticipantsList = participantsList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_participant, parent, false);
                holder = new ViewHolder();
                holder.tvFullNameMobileEmail = convertView.findViewById(R.id.tv_full_name_mobile_email);
                holder.tvCompanyName = convertView.findViewById(R.id.tv_company_name);
                holder.tvSentInvitationOn = convertView.findViewById(R.id.tv_sent_invitation_on);
                holder.buttonStatus = convertView.findViewById(R.id.button_status);
                holder.ivJoinType = convertView.findViewById(R.id.iv_join_type);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            MeetingDetailsVisitor participant = mParticipantsList.get(position);
            formatParticipantInfo(participant, holder);
            setButtonStatus(participant, holder);
            setJoinTypeImage(participant, holder.ivJoinType);

            return convertView;
        }

        private void formatParticipantInfo(MeetingDetailsVisitor participant, ViewHolder holder) {
            StringBuilder infoBuilder = new StringBuilder();

            // Format Full Name or Phone/Email
            if (!TextUtils.isEmpty(participant.getFullName())) {
                infoBuilder.append(participant.getFullName());
                if (!TextUtils.isEmpty(participant.getMobile())) {
                    infoBuilder.append(" - ").append(participant.getMobile());
                } else if (!TextUtils.isEmpty(participant.getEmail())) {
                    infoBuilder.append(" - ").append(participant.getEmail());
                }
            } else if (!TextUtils.isEmpty(participant.getMobile())) {
                infoBuilder.append(participant.getMobile());
            } else if (!TextUtils.isEmpty(participant.getEmail())) {
                infoBuilder.append(participant.getEmail());
            }

            holder.tvFullNameMobileEmail.setText(infoBuilder.toString());

            // Company Name
            if (!TextUtils.isEmpty(participant.getCompanyName())) {
                holder.tvCompanyName.setVisibility(View.VISIBLE);
                holder.tvCompanyName.setText(participant.getCompanyName());
            } else {
                holder.tvCompanyName.setVisibility(View.INVISIBLE);
            }

            // Sent Invitation On (Date Time)
            if (!TextUtils.isEmpty(participant.getInTime()) && !participant.getInTime().equalsIgnoreCase("00:00")) {
                String outTime = participant.getOutTime();
                if(outTime==null || outTime.isEmpty()){
                   // outTime = "";
                    holder.tvSentInvitationOn.setText("Check In: " + participant.getInTime());
                }
                else{
                    outTime = participant.getOutTime();
                    holder.tvSentInvitationOn.setText("Check In: " + participant.getInTime() + " Check out: " + outTime);
                }

            } else if (!TextUtils.isEmpty(participant.getCancelledOn())) {
                holder.tvSentInvitationOn.setText("Cancelled: " + participant.getCancelledOn());
            } else if (!TextUtils.isEmpty(participant.getConfirmedOn())) {
                holder.tvSentInvitationOn.setText("Confirmed: " + participant.getConfirmedOn());
            }  else {
                holder.tvSentInvitationOn.setVisibility(View.INVISIBLE);

            }

           /* if (!TextUtils.isEmpty(participant.getSentInvitationOn())) {
                holder.tvSentInvitationOn.setVisibility(View.VISIBLE);
                holder.tvSentInvitationOn.setText(participant.getSentInvitationOn());
            } else {
                holder.tvSentInvitationOn.setVisibility(View.GONE);
            }*/
        }

        private void setButtonStatus(MeetingDetailsVisitor participant, ViewHolder holder) {
            if (participant != null) {
                if (participant.isCancelled) {
                    holder.buttonStatus.setVisibility(View.VISIBLE);
                    holder.buttonStatus.setBackground(getDrawable(R.drawable.rect_buttonred));
                    holder.buttonStatus.setText("Not Accepted");
                    holder.buttonStatus.setOnClickListener(v -> showPopup(participant));
                } else if (participant.isConfirmed) {
                    holder.buttonStatus.setVisibility(View.VISIBLE);
                    holder.buttonStatus.setBackground(getDrawable(R.drawable.rect_buttongreen));
                    holder.buttonStatus.setText("Accepted");
                    holder.buttonStatus.setOnClickListener(null); // Clear onClick listener if already accepted
                } else {
                    holder.buttonStatus.setVisibility(View.GONE);
                }
            } else {
                holder.buttonStatus.setVisibility(View.GONE);
            }
        }


        private void setJoinTypeImage(MeetingDetailsVisitor participant, ImageView ivJoinType) {
            String joinFrom = participant.getJoinFrom();
            if (!TextUtils.isEmpty(joinFrom) && (joinFrom.equalsIgnoreCase("GOOGLE") || joinFrom.equalsIgnoreCase("TEAMS"))) {
                ivJoinType.setVisibility(View.VISIBLE);
                // Set appropriate image based on joinFrom value
                if (joinFrom.equalsIgnoreCase("GOOGLE")) {
                    ivJoinType.setImageResource(R.drawable.google_meeting);
                } else if (joinFrom.equalsIgnoreCase("TEAMS")) {
                    ivJoinType.setImageResource(R.drawable.teams_meeting);
                }
            } else {
                ivJoinType.setVisibility(View.GONE);
            }
        }

        private void showPopup(MeetingDetailsVisitor participant) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Status: Not Accepted");

            // Prepare message content based on participant details
            StringBuilder message = new StringBuilder();
            if (participant.getCancelledReason() != null) {
                message.append("Cancelled Reason: ").append(participant.getCancelledReason()).append("\n\n");
            }
            if (participant.getRescheduleComments() != null) {
                message.append("Draft to New Date: ").append(participant.getRescheduleComments());
            }

            builder.setMessage(message.toString());

            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            builder.show();
        }

         class ViewHolder {
            TextView tvFullNameMobileEmail;
            TextView tvCompanyName;
            TextView tvSentInvitationOn;
            Button buttonStatus;
            ImageView ivJoinType;
        }
    }
}
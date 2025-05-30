package vip.com.vipmeetings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vip.com.vipmeetings.body.DeleteContectLeaveRequestBody;
import vip.com.vipmeetings.body.GetContactLeaveResponseBody;
import vip.com.vipmeetings.body.GetContectLeaveRequestBody;
import vip.com.vipmeetings.envelope.SoapDeleteContactLeaveRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapDeleteContactLeaveResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetContactLeaveRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetContactLeaveResponseEnvelope;
import vip.com.vipmeetings.interfaces.MainApi;
import vip.com.vipmeetings.models.ContactLeaves;
import vip.com.vipmeetings.models.Leave;
import vip.com.vipmeetings.models.Status;
import vip.com.vipmeetings.request.DeleteContactLeave;
import vip.com.vipmeetings.request.GetContactLeaves;

/**
 * Created by Srinath on 29/05/17.
 */

public class LeaveListActivity extends BaseActivity {


    @BindView(R.id.rvleave)
    RecyclerView rvleave;

    @BindView(R.id.ivhome)
    ImageView ivhome;


    List<Leave> leaveList;
    LeaveAdapter leaveAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leavelist);
        ButterKnife.bind(this);
        rvleave.setLayoutManager(new LinearLayoutManager(this));
        leaveList = new ArrayList<>();
        leaveAdapter = new LeaveAdapter();
        rvleave.setAdapter(leaveAdapter);
        getLeaves();
    }

    @OnClick(R.id.ivhome)
    public void onHome(View v) {

        gotoHome();

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

    private void onAllLeaves(SoapGetContactLeaveResponseEnvelope soapGetContactLeaveResponseEnvelope) {


        hidePD();

        GetContactLeaveResponseBody getContactLeaveResponseBody =
                soapGetContactLeaveResponseEnvelope.getBody();

        if (getContactLeaveResponseBody != null
                && getContactLeaveResponseBody.getGetContactLeavesResponse() != null
                && getContactLeaveResponseBody.getGetContactLeavesResponse().getGetContactLeavesResult() != null) {

            ContactLeaves contactLeaves = getContactLeaveResponseBody.getGetContactLeavesResponse()
                    .getGetContactLeavesResult().getContactLeaves();
            if (contactLeaves != null) {
                if (contactLeaves.getLeaves() != null) {


                    leaveList = contactLeaves.getLeaves();
                    leaveAdapter.notifyDataSetChanged();
                } else if (contactLeaves.getEmpty() != null) {

                    showToast(contactLeaves.getEmpty().get_empty());
                } else if (contactLeaves.getError() != null) {

                    showToast(contactLeaves.getError().get_error());
                }
            }
        } else {
            showToast(getString(R.string.someerror));
        }
    }


    @OnClick(R.id.tvnext)
    public void onNext(View v) {


        Intent i = new Intent(this, LeaveAddActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }


    @OnClick(R.id.tvback)
    public void onBack(View v) {

        onBackPressed();
    }


    public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.VH> {


        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {

            return new VH(getLayoutInflater().inflate(R.layout.inflate_leave, null));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {


            Leave leave = leaveList.get(position);

            holder.tvreason.setText(leave.getReasonForLeave());
            holder.tvdate.setText(leave.getFromDateTimeYMDHM() + " to " + leave.getToDateTimeYMDHM());

            holder.btdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(LeaveListActivity.this);


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

            holder.llupdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


//                    Intent i = new Intent(LeaveListActivity.this, LeaveAddActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    i.putExtra(Constants.LEAVE, mGson.toJson(leaveList.get(holder.getAdapterPosition())));
//                    startActivity(i);
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

            @BindView(R.id.btdelete)
            Button btdelete;

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
        finish();
    }


}

package vip.com.vipmeetings.evacuate;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vip.com.vipmeetings.R;
import vip.com.vipmeetings.body.ClearTimeRequestBody;
import vip.com.vipmeetings.body.ClearTimeResponseBody;
import vip.com.vipmeetings.body.GetResponsibleResponseBody;
import vip.com.vipmeetings.body.GetResponsiblesRequestBody;
import vip.com.vipmeetings.body.SendEvacuationPushResponseBody;
import vip.com.vipmeetings.body.SoapNotifyAboutAreaClearenceRequestBody;
import vip.com.vipmeetings.envelope.SoapClearTimeResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetEvacuationResponsiblesRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetEvacuationResponsiblesResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapNotifyAboutAreaClearence;
import vip.com.vipmeetings.envelope.SoapNotifyAboutAreaClearenceResponse;
import vip.com.vipmeetings.envelope.SoapSendEvacuationPushResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapSetClearTimeRequestEnvelope;
import vip.com.vipmeetings.models.EvacuationResponsible;
import vip.com.vipmeetings.models.EvacuationResponsibles;
import vip.com.vipmeetings.request.GetEvacuationResponsibles;
import vip.com.vipmeetings.request.NotifyAboutAreaClearence;
import vip.com.vipmeetings.request.SetClearTimeToEvacuations;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 11/02/18.
 */

public class EvacuateAdminActivty extends BaseActivity {

    private String mobile = "";
    private static final int REQUEST_PHONE_CALL = 1111;
    List<EvacuationResponsible> evacuationResponsibleList;
    @BindView(R.id.rvadmin)
    RecyclerView rvadmin;

    @BindView(R.id.rltoolbar)
    RelativeLayout rltoolbar;

    @BindView(R.id.ivback)
    ImageView ivback;

    @BindView(R.id.tvstartendevacuation)
    AppCompatTextView tvstartendevacuation;

    @BindView(R.id.tvstartend)
    AppCompatTextView tvstartend;

    AdapterAdmin adapterAdmin;
    String update, clear;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        tvstartend.setText(getIntent().getStringExtra(IpAddress.TVSTARTEND));
        evacuationResponsibleList = new ArrayList<>();
        adapterAdmin = new AdapterAdmin();
        rvadmin.setLayoutManager(new LinearLayoutManager(this));
        rvadmin.setAdapter(adapterAdmin);

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEva(String update) {

        refreshScreen();
    }

    public void refreshScreen() {

       // clearFresco();
        deleteStiky(this);
        getEva();
    }

    @Override
    protected void onStart() {
        super.onStart();
        regis(this);
    }

    @Override
    protected void onStop() {
        unregis(this);
        super.onStop();
    }


    @OnClick(R.id.ivback)
    public void onBack(View v) {
        onBackPressed();
    }


    @OnClick(R.id.tvstartendevacuation)
    public void onStartendEvacuation(View v) {


        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        if (tvstartendevacuation.getText()
                .toString().trim().equalsIgnoreCase(IpAddress.CLEARTIMEEMPTY2)) {

            builder.setTitle(IpAddress.CLEARTIMEEMPTY2);
            builder.setMessage(IpAddress.CLEARTIMEEMPTY_NOT_MESSAGE);
        } else {
            builder.setTitle(IpAddress.CLEARTIMEEMPTY_NOT);
            builder.setMessage(IpAddress.CLEARTIMEEMPTY_MESSAGE);
        }
        builder.setPositiveButton(IpAddress.YES, (dialogInterface, i) -> StartEndEvacuation());

        builder.setNegativeButton(IpAddress.CANCEL, (dialogInterface, i) -> {

            dialogInterface.dismiss();
        });

        alertDialog = builder.show();

    }

    private void setclearTime(String clientid, String update, String clear) {


        this.update = update;
        this.clear = clear;

        showPD(this);

        SoapSetClearTimeRequestEnvelope soapSetClearTimeRequestEnvelope =
                new SoapSetClearTimeRequestEnvelope();


        ClearTimeRequestBody clearTimeRequestBody = new ClearTimeRequestBody();

        SetClearTimeToEvacuations setClearTimeToEvacuations = new SetClearTimeToEvacuations();

        setClearTimeToEvacuations.setA_sClientId(clientid);
        setClearTimeToEvacuations.setsCleredBy(getBarcode());
        setClearTimeToEvacuations.setAuthToken(getAuthTokenPref_Evacuation());

        setClearTimeToEvacuations.setA_sRemoveClearTimeEvacuationIds(clear);

        setClearTimeToEvacuations.setA_sUpdateClearTimeEvacuationIds(update);
        clearTimeRequestBody.setSetClearTimeToEvacuations(setClearTimeToEvacuations);

        soapSetClearTimeRequestEnvelope.setBody(clearTimeRequestBody);
        Observable<SoapClearTimeResponseEnvelope> lClientDetailsObservable =
                service.setClearTimeToEvacuations(soapSetClearTimeRequestEnvelope);

        lClientDetailsObservable.
                subscribeOn(Schedulers.io())
                .map(this::onSoapSetClearTime)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSetClear, this::onError);

    }

    private String onSoapSetClearTime(SoapClearTimeResponseEnvelope soapClearTimeResponseEnvelope) {


        ClearTimeResponseBody clearTimeResponseBody = soapClearTimeResponseEnvelope.getBody();

        return clearTimeResponseBody.getSetClearTimeToEvacuationsResponse().getSetClearTime();
    }

    private void onSetClear(String messages) {


        if (messages != null && messages.equalsIgnoreCase(IpAddress.SUCCESS)) {
            Observable<SoapSendEvacuationPushResponseEnvelope> lClientDetailsObservable;

            String startSTop = null;

            if (this.update != null && this.update.length() > 0) {

                startSTop = IpAddress.EVACUATION_CLEAR2;
            } else if (this.clear != null && this.clear.length() > 0) {

                startSTop = IpAddress.EVACUATION_CLEAR1;
            }

            update = null;
            clear = null;

            // notifyaboutAreaClearence(getIntent().getStringExtra(IpAddress.PLACEID),
            //       getIntent().getStringExtra(IpAddress.EVACUATIONID));

            onPush(IpAddress.SUCCESS);
            try {

                pushService.notifyaboutAreaClearence(getIntent().getStringExtra(IpAddress.PLACEID),
                        getIntent().getStringExtra(IpAddress.EVACUATIONID));
            } catch (Exception e) {

            }

        } else {
            showToast(messages + "");
        }

    }

    private void notifyaboutAreaClearence(String placeid, String evaid) {

        try {


            SoapNotifyAboutAreaClearence soapNotifyAboutAreaClearence =
                    new SoapNotifyAboutAreaClearence();

            SoapNotifyAboutAreaClearenceRequestBody notifyAboutAreaClearenceRequestBody =
                    new SoapNotifyAboutAreaClearenceRequestBody();


            NotifyAboutAreaClearence notifyAboutAreaClearence = new NotifyAboutAreaClearence();


            notifyAboutAreaClearence.setA_iPlaceId(placeid);

            notifyAboutAreaClearence.setAuthToken(getAuthTokenPref_Evacuation());

            notifyAboutAreaClearence.setA_sEvacuationIds(evaid);

            notifyAboutAreaClearenceRequestBody.setNotifyAboutAreaClearence(notifyAboutAreaClearence);

            soapNotifyAboutAreaClearence.setBody(notifyAboutAreaClearenceRequestBody);

            Observable<SoapNotifyAboutAreaClearenceResponse> lClientDetailsObservable =
                    service.notifyAboutAreaClearence(soapNotifyAboutAreaClearence);

            lClientDetailsObservable.
                    subscribeOn(Schedulers.io())
                    .map(this::onSoapEvacuationPush)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onPush, this::onPushError);
        } catch (Exception e) {

        }

    }

    public void onPushError(Throwable throwable) {


        hidePD();
        IpAddress.e("error", throwable.toString());
        if (throwable.getMessage() != null && throwable.getMessage().trim().length() > 0) {
            Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();

        } else
            Toast.makeText(this, getString(R.string.someerror), Toast.LENGTH_SHORT).show();
    }

    private String onSoapEvacuationPush(SoapNotifyAboutAreaClearenceResponse soapNotifyAboutAreaClearenceResponse) {


        try {

            return soapNotifyAboutAreaClearenceResponse.getBody()
                    .getNotifyAboutAreaClearenceResponse().getNotifyAboutAreaClearenceResult().get_string();
        } catch (Exception e) {
            return "";
        }

    }


    private String onSoapEvacuationPush(SoapSendEvacuationPushResponseEnvelope soapSendEvacuationPushResponseEnvelope) {


        SendEvacuationPushResponseBody sendEvacuationPushResponseBody =
                soapSendEvacuationPushResponseEnvelope.getBody();

        return sendEvacuationPushResponseBody.getSendEvacuationPushResponse().getSendEvacuationPush();

    }


    private void StartEndEvacuation() {


        if (evacuationResponsible != null) {


            if (evacuationResponsible.getClearTime() == null
                    ||
                    evacuationResponsible.getClearTime().trim().isEmpty()) {

                setclearTime(getClientID(),
                        getIntent().getStringExtra(IpAddress.EVACUATIONID),
                        "");

            } else {

                setclearTime(getClientID(),
                        "", getIntent().getStringExtra(IpAddress.EVACUATIONID));

            }
        }
    }


    private void getEva() {

        if (hasNetwork()) {
            getEvacuationDetails();
        } else {
            showToast(getString(R.string.nointernet));
        }
    }


    private void onPush(String status) {


        getEva();
    }


    public void getEvacuationDetails() {

        try {

            showPD(this);
            SoapGetEvacuationResponsiblesRequestEnvelope soapGetEvacuationResponsiblesRequestEnvelope =
                    new SoapGetEvacuationResponsiblesRequestEnvelope();

            GetResponsiblesRequestBody getResponsiblesRequestBody = new GetResponsiblesRequestBody();
            GetEvacuationResponsibles getEvacuationResponsibles = new GetEvacuationResponsibles();
            getEvacuationResponsibles.setA_sClientId(getIntent().getStringExtra(IpAddress.CLIENTID));
            getEvacuationResponsibles.setAuthToken(getAuthTokenPref_Evacuation());
            getEvacuationResponsibles.setA_sEvacuationId(getIntent().getStringExtra(IpAddress.EVACUATIONID));
            getResponsiblesRequestBody.setGetEvacuationResponsibles(getEvacuationResponsibles);
            soapGetEvacuationResponsiblesRequestEnvelope.setBody(getResponsiblesRequestBody);

            Observable<SoapGetEvacuationResponsiblesResponseEnvelope> lClientDetailsObservable =
                    service.getEvacuationResponsibles(soapGetEvacuationResponsiblesRequestEnvelope);
            lClientDetailsObservable.
                    subscribeOn(Schedulers.io())
                    .map(this::onSoapGetEvacuation)
                    .map(this::onMap)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onEvacuation, this::onEvacuationError);
        } catch (Exception e) {
            Constants.e("err", e.toString());
            hidePD();
        }
    }

    private EvacuationResponsibles onSoapGetEvacuation(SoapGetEvacuationResponsiblesResponseEnvelope soapGetEvacuationResponsiblesResponseEnvelope) {


        GetResponsibleResponseBody getResponsibleResponseBody =
                soapGetEvacuationResponsiblesResponseEnvelope.getBody();


        return getResponsibleResponseBody.getGetEvacuationResponsiblesResponse().
                getGetEvacuationResponsiblesResult().getGetEvacuationResponsibles();
    }

    private EvacuationResponsibles onMap(EvacuationResponsibles evacuationResponsibles) {

        evacuationResponsibleList.clear();
        evacuationResponsibleList.addAll(evacuationResponsibles.getEvacuationResponsible());
        if (evacuationResponsibleList != null && evacuationResponsibleList.size() > 0) {

            Collections.sort(evacuationResponsibleList, (object1, object2) ->
                    object1.getPriority().trim().compareTo(object2.getPriority().trim()));
            evacuationResponsible = evacuationResponsibleList.get(0);
        }

        return evacuationResponsibles;
    }

    private void onEvacuationError(Throwable throwable) {

        hidePD();
        IpAddress.e("error", throwable.toString());
    }


    private void onEvacuation(EvacuationResponsibles evacuationResponsibles) {


        if (evacuationResponsible != null) {

            setEvacuationDetails(evacuationResponsible);
        }
        adapterAdmin.notifyDataSetChanged();
        hidePD();

    }

    private void setEvacuationDetails(EvacuationResponsible evacuationResponsible) {


        tvstartend.setVisibility(View.VISIBLE);
        if (evacuationResponsible.getEndTime() != null
                && evacuationResponsible.getEndTime().trim().length() > 0) {

            tvstartend.setText(IpAddress.EVACUATION_NO);
            disableEvacuation();
        } else if (evacuationResponsible.getStartTime() != null
                && evacuationResponsible.getStartTime().trim().length() > 0) {

            tvstartend.setText(IpAddress.EVACUATION_ENABLED);
            enableEvacation();
        }


        if (evacuationResponsible.getClearTime() == null
                || evacuationResponsible.getClearTime().trim().isEmpty()) {


            tvstartendevacuation.setText(IpAddress.CLEARTIMEEMPTY2);
            tvstartendevacuation.setBackgroundColor(ContextCompat.getColor(this, R.color.color_red));
            tvstartendevacuation.setTextColor(ContextCompat.getColor(this, R.color.md_white_1000));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                getWindow().setNavigationBarColor
                        (getResources().getColor(R.color.color_red));

            } else {

            }

        } else {

            tvstartendevacuation.setBackgroundResource(R.drawable.gradient_tv_nocorenr);
            tvstartendevacuation.setTextColor(ContextCompat.getColor(this, R.color.md_grey_700));
            tvstartendevacuation.setText(IpAddress.CLEARTIMEEMPTY_NOT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                getWindow().setNavigationBarColor
                        (getResources().getColor(R.color.bg));

            } else {

            }
        }


    }

    public void enableEvacation() {

        rltoolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_red));
        tvstartend.setTextColor(ContextCompat.getColor(EvacuateAdminActivty.this, R.color.md_white_1000));

        colorStatusBar(true, getWindow(), this);
    }

    public void disableEvacuation() {

        tvstartend.setTextColor(ContextCompat.getColor(EvacuateAdminActivty.this, R.color.color_useravailability_empty));
        rltoolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        colorStatusBar(false, getWindow(), this,
                ContextCompat.getColor(this, R.color.bg));
    }

    private void clearFresco() {

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearMemoryCaches();
        imagePipeline.clearDiskCaches();

    }


    public class AdapterAdmin extends RecyclerView.Adapter<AdapterAdmin.VH> {


        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == 0) {

                return new VH(getLayoutInflater().inflate(R.layout.inflate_adapteradmin_0, parent, false));
            } else {
                return new VH(getLayoutInflater().inflate(R.layout.inflate_adapteradmin, parent, false));

            }

//            VH vh=new VH(getLayoutInflater().inflate(R.layout.inflate_adapteradmin_row0, parent, false));
//            if (viewType == 0) {
//
//                int height = parent.getMeasuredHeight() / 6;
//                int width = parent.getMeasuredWidth();
//
//                vh.itemView.setLayoutParams(new RecyclerView.LayoutParams(width, height));
//                return vh ;
//            } else {
//
//                int height = parent.getMeasuredHeight() / 2;
//                int width = parent.getMeasuredWidth();
//
//                vh.itemView.setLayoutParams(new RecyclerView.LayoutParams(width, height));
//
//                return vh;
//
//            }
        }


        @Override
        public int getItemViewType(int position) {

            if (position == 0) {
                return 0;
            }

            return 1;
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {


            EvacuationResponsible evacuation = evacuationResponsibleList.get(position);


            switch (position) {


                case 0:

                    holder.tvarea.setVisibility(View.VISIBLE);
                    holder.tvtype.setText(IpAddress.CURRENT_AREA);
                    holder.tvtype.setVisibility(View.VISIBLE);
                    break;

                case 1:
                    holder.tvarea.setText("");
                    holder.tvarea.setVisibility(View.GONE);
                    holder.tvtype.setText(IpAddress.NEXT);
                    holder.tvtype.setVisibility(View.VISIBLE);
                    break;
                default:
                    holder.tvtype.setText("");
                    holder.tvarea.setText("");
                    holder.tvarea.setVisibility(View.GONE);
                    holder.tvtype.setVisibility(View.GONE);
                    break;
            }


            if (evacuation.getSTATUS() == null
                    || evacuation.getSTATUS().trim().isEmpty()) {

                holder.ivcount.setImageResource(0);
                holder.ivcount_image.setImageResource(0);
                holder.ivcount_image_actual.setImageResource(0);
            } else {
                if (evacuation.getUserAvailability() != null &&
                        evacuation.getUserAvailability().equalsIgnoreCase(IpAddress.USERAVAILABILITY_TRUE)) {
                    holder.tvstatus.setText("");
                    holder.ivcount.setImageResource(R.drawable.circlegreen);
                    holder.ivcount_image.setImageResource(R.drawable.circlegreen);
                    holder.ivcount_image_actual.setImageResource(R.drawable.circlegreen);
                    holder.tvstatus.setTextColor(ContextCompat.getColor(EvacuateAdminActivty.this,
                            R.color.color_useravailability_no));
                } else if (evacuation.getUserAvailability() != null &&
                        evacuation.getUserAvailability().equalsIgnoreCase(IpAddress.USERAVAILABILITY_FALSE)) {

                    holder.ivcount.setImageResource(R.drawable.circlered);
                    holder.ivcount_image.setImageResource(R.drawable.circlered);
                    holder.tvstatus.setText("(Not in the building)");
                    holder.tvstatus.setTextColor(ContextCompat.getColor(EvacuateAdminActivty.this,
                            R.color.textred));
                    holder.ivcount_image_actual.setImageResource(R.drawable.circlered);
                } else {

                    holder.tvstatus.setText("");
                    holder.ivcount.setImageResource(R.drawable.circleyellow);
                    holder.ivcount_image.setImageResource(R.drawable.circleyellow);
                    holder.ivcount_image_actual.setImageResource(R.drawable.circle_normal_yellow);
                    holder.tvstatus.setTextColor(ContextCompat.getColor(EvacuateAdminActivty.this,
                            R.color.color_useravailability_no));
                }

            }

            if (evacuation.getSTATUS() == null
                    || evacuation.getSTATUS().trim().isEmpty()) {

                holder.ivcount.setImageResource(0);
            } else {

                if (evacuation.getClearTime() != null &&
                        !evacuation.getClearTime().trim().isEmpty()) {
                    holder.ivcount.setImageResource(R.drawable.officegreen);


                } else {
                    holder.ivcount.setImageResource(R.drawable.officered);
                }
            }


            if (evacuation.getFloor() != null
                    && evacuation.getAreaName() != null) {
                try {
                    holder.tvarea.setText(ordinal(Integer.parseInt(evacuationResponsible.getFloor()))
                            + " floor, " + evacuationResponsible.getAreaName());
                } catch (Exception e) {
                    holder.tvarea.setText(evacuationResponsible.getFloor() + " floor, "
                            + evacuationResponsible.getAreaName());

                }
            } else
                holder.tvarea.setText("");


            if (evacuation.getName() != null) {
                holder.tvname.setText(evacuation.getName());
            } else if (evacuation.getName() != null
                    && evacuation.getCompany() != null) {
                holder.tvname.setText(evacuation.getName() + " (" + evacuation.getPriority() + "), " +
                        evacuation.getFloor() + ", " + evacuation.getCompany());
            } else {
                holder.tvname.setText("");
            }
            if (evacuation.getMobile() != null)
                holder.tvmobile.setText(evacuation.getMobile());
            else
                holder.tvmobile.setText("");


            holder.tvmobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    callMobile(evacuationResponsibleList.get(holder.getAdapterPosition()).getMobile());
                }
            });

            if (evacuation.getPhotoUrl() != null) {
                ImageRequest request;
                if (position == 0) {

                    request = ImageRequestBuilder.newBuilderWithSource
                            (Uri.parse(evacuation.getPhotoUrl().trim()))
                            .setResizeOptions(new ResizeOptions(
                                    getResources().getDimensionPixelSize(R.dimen.ivwidth100),
                                    getResources().getDimensionPixelSize(R.dimen.ivwidth100)))
                            .build();
                } else {
                    request = ImageRequestBuilder.newBuilderWithSource
                            (Uri.parse(evacuation.getPhotoUrl().trim()))
                            .setResizeOptions(new ResizeOptions(
                                    getResources().getDimensionPixelSize(R.dimen.ivwidth48),
                                    getResources().getDimensionPixelSize(R.dimen.ivwidth48)))
                            .build();
                }

                holder.ivimage.setController(
                        Fresco.newDraweeControllerBuilder()
                                .setOldController(holder.ivimage.getController())
                                .setAutoPlayAnimations(false)
                                .setImageRequest(request)
                                .setTapToRetryEnabled(true)
                                .setControllerListener(new ControllerListener<ImageInfo>() {
                                    @Override
                                    public void onSubmit(String id, Object callerContext) {

                                    }

                                    @Override
                                    public void onFinalImageSet(String id, @javax.annotation.Nullable ImageInfo imageInfo,
                                                                @javax.annotation.Nullable Animatable animatable) {

                                        if (holder.getAdapterPosition() == 0) {

                                            holder.ivcount_image_actual.setVisibility(View.VISIBLE);
                                            holder.ivcount_image.setVisibility(View.GONE);


                                        } else {
                                            holder.ivcount_image_actual.setVisibility(View.VISIBLE);
                                            holder.ivcount_image.setVisibility(View.GONE);
                                        }

                                    }

                                    @Override
                                    public void onIntermediateImageSet(String id, @javax.annotation.Nullable ImageInfo imageInfo) {

                                    }

                                    @Override
                                    public void onIntermediateImageFailed(String id, Throwable throwable) {

                                    }

                                    @Override
                                    public void onFailure(String id, Throwable throwable) {

                                        if (holder.getAdapterPosition() == 0) {

                                            holder.ivcount_image_actual.setVisibility(View.GONE);
                                            holder.ivcount_image.setVisibility(View.VISIBLE);


                                        } else {
                                            holder.ivcount_image_actual.setVisibility(View.GONE);
                                            holder.ivcount_image.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onRelease(String id) {

                                    }
                                })
                                .setLowResImageRequest(request)
                                .build());

            } else {
                holder.ivimage.setImageResource(R.mipmap.ic_profile);
            }
            if (evacuation.getEmail() != null)
                holder.tvemail.setText(evacuation.getEmail().trim());
            else
                holder.tvemail.setText("");

            if (evacuation.getUserAvailability() == null || evacuation.getUserAvailability().isEmpty()) {
                holder.tvstatus.setText("(Unverified position)");
            } else if (evacuation.getUserAvailability().equalsIgnoreCase("True")) {
                holder.tvstatus.setText("(Verified position)");
            } else {
                holder.tvstatus.setText("(Not in the building)");
            }

            holder.tvemail.setPaintFlags(holder.tvemail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            holder.tvmobile.setPaintFlags(holder.tvemail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


            holder.ivmessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(EvacuateAdminActivty.this,
                            EvacuationMessagesActivity.class);
                    intent.putExtra(IpAddress.BARCODE, evacuationResponsibleList.get(holder.
                            getAdapterPosition()).getBarcode());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(IpAddress.EVACUATIONID, evacuationResponsibleList.get(holder.
                            getAdapterPosition()).getEvacuationId());
                    intent.putExtra(IpAddress.EVACUATIONMESSAGE, evacuationResponsibleList.get(holder.
                            getAdapterPosition()).getAreaName() + "-" +
                            evacuationResponsibleList.get(holder.
                                    getAdapterPosition()).getFloor());
                    startActivityForResult(intent, Constants.EVACUATIONREFRESH);
                }
            });


        }

        @Override
        public int getItemCount() {
            return evacuationResponsibleList.size();
        }

        public class VH extends RecyclerView.ViewHolder {

            @BindView(R.id.tvname)
            AppCompatTextView tvname;

            @BindView(R.id.tvmobile)
            AppCompatTextView tvmobile;

            @BindView(R.id.tvtype)
            AppCompatTextView tvtype;

            @BindView(R.id.tvemail)
            AppCompatTextView tvemail;

            @BindView(R.id.tvstatus)
            AppCompatTextView tvstatus;

            @BindView(R.id.ivimage)
            SimpleDraweeView ivimage;

            @BindView(R.id.ivcount_image)
            ImageView ivcount_image;


            @BindView(R.id.tvarea)
            AppCompatTextView tvarea;

            @BindView(R.id.tvcount)
            AppCompatTextView tvcount;
            @BindView(R.id.ivcount)
            ImageView ivcount;

            @BindView(R.id.ivcount_image_actual)
            ImageView ivcount_image_actual;
            @BindView(R.id.ivmessage)
            ImageView ivmessage;


            public VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }

    private void callMobile(String mobile) {
        this.mobile = mobile;
        if (ContextCompat.checkSelfPermission(EvacuateAdminActivty.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EvacuateAdminActivty.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        } else {
            call();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {

                }
                return;
            }
        }
    }

    private void call() {

        try {
            Intent intent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + mobile));
            startActivity(intent);
        } catch (SecurityException e) {

        }
    }

}

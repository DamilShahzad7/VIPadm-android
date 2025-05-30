package vip.com.vipmeetings.evacuate;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.TransitionManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vip.com.vipmeetings.R;
import vip.com.vipmeetings.SplashScreen;
import vip.com.vipmeetings.body.GetResponsibleDetailsRequestBody;
import vip.com.vipmeetings.body.GetResponsibleResponseBody;
import vip.com.vipmeetings.body.GetResponsiblesRequestBody;
import vip.com.vipmeetings.body.GetUnReadMessageCountResponseBody;
import vip.com.vipmeetings.body.NotifyAboutResponsibleAvailabilityBody;
import vip.com.vipmeetings.body.NotifyAboutResponsibleAvailabilityResponseBody;
import vip.com.vipmeetings.body.RemoveClearTimeRequestBody;
import vip.com.vipmeetings.body.RemoveClearTimeResponseBody;
import vip.com.vipmeetings.body.SaveResponsiblePhotoBody;
import vip.com.vipmeetings.body.SendEvacuationPushResponseBody;
import vip.com.vipmeetings.body.SoapNotifyAboutAreaClearenceRequestBody;
import vip.com.vipmeetings.body.SoapRemoveRequestBody;
import vip.com.vipmeetings.body.UnreadMessageCountRequestBody;
import vip.com.vipmeetings.body.UpdateClearTimeRequestBody;
import vip.com.vipmeetings.body.UpdateClearTimeResponseBody;
import vip.com.vipmeetings.body.UpdateResponsibleAvailabilityResponseBody;
import vip.com.vipmeetings.body.UpdateResponsibleRequestBody;
import vip.com.vipmeetings.envelope.SaveResponsiblePhotoResponseEnevelope;
import vip.com.vipmeetings.envelope.SoapEvacuationResponsibleDetailsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapEvacuationResponsibleDetailsResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetEvacuationResponsiblesRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetEvacuationResponsiblesResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetUnreadMessageCountResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapNotifyAboutAreaClearence;
import vip.com.vipmeetings.envelope.SoapNotifyAboutAreaClearenceResponse;
import vip.com.vipmeetings.envelope.SoapNotifyAboutResponsibleAvailability;
import vip.com.vipmeetings.envelope.SoapNotifyAboutResponsibleAvailabilityResponse;
import vip.com.vipmeetings.envelope.SoapRemoveClearTimeRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapRemoveClearTimeResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapRemoveResponsiblePhotoEnvelope;
import vip.com.vipmeetings.envelope.SoapRemoveResponsiblePhotoResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapSaveResponsiblePhotoEnvelope;
import vip.com.vipmeetings.envelope.SoapSendEvacuationPushResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapUnreadMessageCountRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapUpdateClearTimeRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapUpdateClearTimeResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapUpdateResponsibleRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapUpdateResponsibleResponseEnvelope;
import vip.com.vipmeetings.models.EvacuationResponsible;
import vip.com.vipmeetings.models.EvacuationResponsibles;
import vip.com.vipmeetings.request.GetEvacuationResponsibles;
import vip.com.vipmeetings.request.GetResponsibleDetails;
import vip.com.vipmeetings.request.GetUnReadMessageCount;
import vip.com.vipmeetings.request.NotifyAboutAreaClearence;
import vip.com.vipmeetings.request.NotifyAboutResponsibleAvailability;
import vip.com.vipmeetings.request.RemoveClearTimeToEvacuation;
import vip.com.vipmeetings.request.RemoveResponsiblePhoto;
import vip.com.vipmeetings.request.SaveResponsiblePhoto;
import vip.com.vipmeetings.request.UpdateClearTimeToEvacuation;
import vip.com.vipmeetings.request.UpdateResponsibleAvailability;
import vip.com.vipmeetings.utilities.Constants;

public class EvacuationNormal2 extends BaseActivity {


    @BindView(R.id.btstatus)
    AppCompatTextView btstatus;

    @BindView(R.id.rbyes)
    AppCompatTextView rbyes;

    @BindView(R.id.rbno)
    AppCompatTextView rbno;

    @BindView(R.id.tvcount)
    TextView tvcount;

    @BindView(R.id.ivcount)
    ImageView ivcount;

    @BindView(R.id.tvtext)
    TextView tvtext;

    @BindView(R.id.tvno)
    AppCompatTextView tvno;

    @BindView(R.id.rgbuilding)
    LinearLayout rgbuilding;

    @BindView(R.id.llyes)
    LinearLayout llyes;

    @BindView(R.id.llyesno)
    LinearLayout llyesno;

    @BindView(R.id.tvevacuation)
    TextView tvevacuation;


    @BindView(R.id.tvtimercenter)
    TextView tvtimercenter;

    @BindView(R.id.tvcountertop)
    TextView tvcountertop;

    @BindView(R.id.tvtimediff)
    TextView tvtimediff;
    @BindView(R.id.ivimage)
    SimpleDraweeView ivimage;

    @BindView(R.id.ivcount_image)
    ImageView ivcount_image;

    @BindView(R.id.ivback)
    ImageView ivback;


    @BindView(R.id.sparea)
    AppCompatSpinner sparea;

    @BindView(R.id.cvyes)
    CardView cvyes;


    @BindView(R.id.cvno)
    CardView cvno;


    @BindView(R.id.tvtext2)
    TextView tvtext2;

    @BindView(R.id.tvtext1)
    TextView tvtext1;

    @BindView(R.id.tvadmin)
    TextView tvadmin;

    @BindView(R.id.tvstatus)
    TextView tvstatus;

    @BindView(R.id.rltoolbar)
    RelativeLayout rltoolbar;

    EvacuationResponsible evacuationResponsibleNew;
    private String flag = "";
    private List<EvacuationResponsible> evacuationResponsibleModelList;

    List<EvacuationResponsible> evacuationResponsibleList;
    AdapterAdmin adapterAdmin;

    @BindView(R.id.rvlist)
    RecyclerView rvlist;

    SimpleDateFormat simpleDateFormat;
    Long different;
    Timer timer;
    private String areaName = "";

    @BindView(R.id.clnormal)
    ConstraintLayout clnormal;

    @BindView(R.id.climage)
    ConstraintLayout climage;

    int pos = 0;
    NumberFormat numberFormat;
    private boolean flag_clickabale_revokeclear = true;

    AlertDialog alertDialog;
    private Uri imageUri = null;
    private String base64 = "";

    boolean isImageFull;
    ConstraintSet constraintSet;


    @BindView(R.id.ivimagecenter)
    SimpleDraweeView ivimagecenter;

    @BindView(R.id.ivcamera)
    AppCompatImageView ivcamera;


    @BindView(R.id.ivcameraactual)
    AppCompatImageView ivcameraactual;

    @BindView(R.id.tvclose)
    AppCompatTextView tvclose;


    boolean isRefersh = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evacuatenormalnew);
        ButterKnife.bind(this);
        numberFormat = new DecimalFormat("00");
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        evacuationResponsibleList = new ArrayList<>();
        adapterAdmin = new AdapterAdmin();
        rvlist.setAdapter(adapterAdmin);
        chageImage();
    }

    private void chageImage() {

        ivimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
                roundingParams.setRoundAsCircle(true);
                ivimagecenter.getHierarchy().setRoundingParams(roundingParams);

                ViewGroup.MarginLayoutParams marginLayoutParams =
                        (ViewGroup.MarginLayoutParams) climage.getLayoutParams();
                marginLayoutParams.topMargin = (int) getResources().getDimension(R.dimen.margintop);
                climage.setLayoutParams(marginLayoutParams);
                isRefersh = false;
                climage.setVisibility(View.VISIBLE);
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource
                        (Uri.parse(evacuationResponsibleNew.getPhotoUrl().trim()))
                        .setResizeOptions(new ResizeOptions(
                                getResources().getDimensionPixelSize(R.dimen.ivwidth48),
                                getResources().getDimensionPixelSize(R.dimen.ivwidth48)))
                        .build();
                ivimagecenter.setController(
                        Fresco.newDraweeControllerBuilder()
                                .setAutoPlayAnimations(false)
                                .setTapToRetryEnabled(true)
                                .setImageRequest(request)
                                .setControllerListener(new ControllerListener<ImageInfo>() {
                                    @Override
                                    public void onSubmit(String id, Object callerContext) {

                                    }

                                    @Override
                                    public void onFinalImageSet(String id, @javax.annotation.Nullable ImageInfo imageInfo,
                                                                @javax.annotation.Nullable Animatable animatable) {


//                                        ivcameraactual.setVisibility(View.VISIBLE);
//                                        ivcamera.setVisibility(View.GONE);

                                    }

                                    @Override
                                    public void onIntermediateImageSet(String id, @javax.annotation.Nullable ImageInfo imageInfo) {

                                    }

                                    @Override
                                    public void onIntermediateImageFailed(String id, Throwable throwable) {

                                    }

                                    @Override
                                    public void onFailure(String id, Throwable throwable) {


//                                        ivcameraactual.setVisibility(View.GONE);
//                                        ivcamera.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onRelease(String id) {

                                    }
                                })
                                .setLowResImageRequest(request)
                                .build());

                clnormal.setVisibility(View.GONE);
            }
        });
    }


    private void clearFresco() {

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearMemoryCaches();
        imagePipeline.clearDiskCaches();

    }

    @OnClick(R.id.tvclose)
    public void onClose(View v) {

        isRefersh = true;
        isImageFull = false;
        climage.setVisibility(View.GONE);
        clnormal.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.ivcameraactual, R.id.ivcamera, R.id.tvedit})
    public void onImage(View v) {

        capturePhoto();
    }


    @Override
    public void onBackPressed() {


        if (climage.getVisibility() == View.VISIBLE) {

            if (isImageFull) {

                isImageFull = false;
                RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
                roundingParams.setRoundAsCircle(true);
                ivimagecenter.getHierarchy().setRoundingParams(roundingParams);

                ViewGroup.MarginLayoutParams marginLayoutParams =
                        (ViewGroup.MarginLayoutParams) climage.getLayoutParams();
                marginLayoutParams.topMargin = (int) getResources().getDimension(R.dimen.margintop);
                climage.setLayoutParams(marginLayoutParams);

                constraintSet.constrainWidth(R.id.ivimagecenter,
                        (int) getResources().getDimension(R.dimen.ivwidth100));
                constraintSet.constrainHeight(R.id.ivimagecenter,
                        (int) getResources().getDimension(R.dimen.ivwidth100));

                constraintSet.applyTo(climage);

            } else {

                isImageFull = true;
                climage.setVisibility(View.GONE);
                clnormal.setVisibility(View.VISIBLE);
            }

        } else if (getIntent().hasExtra(IpAddress.HOMESCREEN)) {
            setResult(RESULT_OK);
            finish();
        } else {

            Intent intent = new Intent(this, SplashScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
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

    public void refreshScreen() {

        hidePD();
        if (eventBus != null)
            deleteStiky(this);

        if (isRefersh) {

            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            readEva();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEva(String update) {

        if (!isFinishing()) {
            refreshScreen();
        }
    }

    @OnClick(R.id.rbyes)
    public void onYes(View v) {

        if (evacuationResponsibleNew != null
                && evacuationResponsibleNew.getSTATUS() != null &&
                evacuationResponsibleNew.getSTATUS().equalsIgnoreCase(IpAddress.STATUS_STARTED)) {


            onYesButton();
        }
    }

    private void onYesButton() {


        if (evacuationResponsibleNew != null) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }

            showPD(this);
            flag = "1";
            onYesNo("1");
        }
    }


    @OnClick(R.id.rbno)
    public void onNo(View v) {

        if (evacuationResponsibleNew != null
                && evacuationResponsibleNew.getSTATUS() != null &&
                evacuationResponsibleNew.getSTATUS()
                        .equalsIgnoreCase(IpAddress.STATUS_STARTED)) {


            if (evacuationResponsibleNew.getUserAvailability() != null
                    && evacuationResponsibleNew.getUserAvailability()
                    .equalsIgnoreCase(IpAddress.USERAVAILABILITY_FALSE)) {

            } else

                onNoButton();
        }
    }

    private void onNoButton() {


        if (evacuationResponsibleNew != null) {

            if (timer != null) {
                timer.cancel();
                timer = null;
            }

            showPD(this);
            flag = "0";
            onYesNo("0");
        }
    }

    private void onYesNo(String isYesNo) {

        if (hasNetwork()) {


            //   onSuccessRadioButtonClicked(IpAddress.SUCCESS);
//            getApp().getPushService().onYesNoPush(isYesNo,
//                    evacuationResponsibleNew.getBarcode(), evacuationResponsibleNew.getClientId());

            onYesNoPush(isYesNo);


        } else {
            hidePD();
            showToast(getString(R.string.nointernet));
        }

    }

    private void onYesNoPush(String isYesNo) {

        SoapUpdateResponsibleRequestEnvelope soapUpdateResponsibleRequestEnvelope = new SoapUpdateResponsibleRequestEnvelope();

        UpdateResponsibleRequestBody updateResponsibleRequestBody = new UpdateResponsibleRequestBody();

        UpdateResponsibleAvailability updateResponsibleAvailability = new UpdateResponsibleAvailability();

        updateResponsibleAvailability.setA_sAvailableStatus(isYesNo);
        updateResponsibleAvailability.setAuthToken(getAuthTokenPref_Evacuation());
        updateResponsibleAvailability.setA_sBarcode(evacuationResponsibleNew.getBarcode());
        updateResponsibleAvailability.setA_sClientId(evacuationResponsibleNew.getClientId());
        updateResponsibleRequestBody.setUpdateResponsibleAvailability(updateResponsibleAvailability);
        soapUpdateResponsibleRequestEnvelope.setBody(updateResponsibleRequestBody);

        Observable<SoapUpdateResponsibleResponseEnvelope> lClientDetailsObservable =
                service.updateResponsibleAvailability(soapUpdateResponsibleRequestEnvelope);

        lClientDetailsObservable.
                subscribeOn(Schedulers.io())
                .map(this::onSoapUpdate)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessRadioButtonClicked, this::onError);
    }

    private String onSoapUpdate(SoapUpdateResponsibleResponseEnvelope soapUpdateResponsibleResponseEnvelope) {


        UpdateResponsibleAvailabilityResponseBody updateResponsibleAvailabilityResponseBody = soapUpdateResponsibleResponseEnvelope.getBody();
        return updateResponsibleAvailabilityResponseBody.getUpdateResponsibleAvailabilityResponse().getUpDateResponsible();
    }

    private void onSuccessRadioButtonClicked(String status_evacuate) {

        if (status_evacuate != null
                && status_evacuate.equalsIgnoreCase(IpAddress.SUCCESS)) {


            oYesNoUpdate();
        } else {
            hidePD();
        }
    }

    AlertDialog alertDialog_share = null;

    public void showYesNoDialog() {

        if (alertDialog_share != null && alertDialog_share.isShowing()) {
            alertDialog_share.dismiss();
        }

        AlertDialog.Builder builder_share = new AlertDialog.Builder(
                EvacuationNormal2.this, R.style.PauseDialog);
        View v = getLayoutInflater().inflate(R.layout.dialog_share, null);
        builder_share.setView(v);
        TextView tvcancel = (TextView) v.findViewById(R.id.tvcancel);
        TextView tvatwork = (TextView) v.findViewById(R.id.tvatwork);
        TextView tvnotatwork = (TextView) v.findViewById(R.id.tvnotatwork);

        tvcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (alertDialog_share != null)
                    alertDialog_share.dismiss();
            }
        });

        tvatwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (alertDialog_share != null)
                    alertDialog_share.dismiss();

                onYesButton();
            }
        });

        tvnotatwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (alertDialog_share != null)
                    alertDialog_share.dismiss();

                onNoButton();

            }
        });

        alertDialog_share = builder_share.create();
        WindowManager.LayoutParams wlmp = alertDialog_share.getWindow().getAttributes();
        wlmp.gravity = Gravity.BOTTOM;
        alertDialog_share.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        alertDialog_share.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog_share.show();
    }


    private void oYesNoUpdate() {

        if (flag.equalsIgnoreCase("1")) {
            //sendPushMessage();

            try {
                pushService.sendPushMessage(evacuationResponsibleNew.getA_sPlaceId(), getBarcode());
            } catch (Exception e) {

            }

        } else if (flag.equalsIgnoreCase("0")) {
            //sendPushMessage();
            try {
                pushService.sendPushMessage(evacuationResponsibleNew.getA_sPlaceId(), getBarcode());
            } catch (Exception e) {

            }
        }

        onPush(IpAddress.SUCCESS);

    }


    @OnClick({R.id.ivmessage, R.id.tvadmin})
    public void onContact(View v) {

        if (evacuationResponsibleModelList != null
                && evacuationResponsibleModelList.size() > 0) {

            showPD(this);
            Single.fromCallable(this::loadInBackgroundEvacuation)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::updateUi, this::onError);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//
//            case EVACUATIONREFRESH:
//                //readEva();
//                break;
//        }
//    }

    private void updateUi(String str) {

        hidePD();

        if (evacuationResponsibleNew != null) {
            Intent intent = new Intent(this, EvacuationMessagesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(IpAddress.MESSAGEID, "");
            intent.putExtra(IpAddress.BARCODE, getBarcode());
            intent.putExtra(IpAddress.EVACUATIONID, evacuationResponsibleNew.getEvacuationId());
            intent.putExtra(IpAddress.EVACUATIONMESSAGE, evacuationResponsibleNew.getAreaName() + "-" +
                    evacuationResponsibleNew.getFloor());
            startActivity(intent);
        }


//        hidePD();
//        Intent intent = new Intent(this, MessageSingleActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        if (str != null)
//            intent.putExtra(IpAddress.AREALISTNORMAL, str);
//        startActivityForResult(intent, IpAddress.REQUESTMESSAGE);
    }

    private String loadInBackgroundEvacuation() {


        if (evacuationResponsibleModelList != null
                && evacuationResponsibleModelList.size() > 0) {
            HashMap<String, EvacuationResponsible> stringStringHashMap = new HashMap<>();

            for (EvacuationResponsible evacuation : evacuationResponsibleModelList)
                stringStringHashMap.put(evacuation.getAreaName(), evacuation);

            String str = mGson.toJson(stringStringHashMap);
            IpAddress.e("hasmap", str);
            return str;
        }
        return "";

    }


    public void enableEvacation() {

        rltoolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_red));
        tvstatus.setText("Evacuation has been enabled");
        tvevacuation.setText("Evacuation has been enabled");
        colorStatusBar(true, getWindow(), this);
    }

    public void disableEvacuation() {

        rltoolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        tvstatus.setText("No evacuation has been enabled");
        tvevacuation.setText("");
        tvtimercenter.setText("");
        colorStatusBar(false, getWindow(), this,
                ContextCompat.getColor(this, R.color.bg));
    }

    GetEvacuationResponsibles getEvacuationResponsibles;
    String evaId = null;
    String placeID = null;

    public void getEvacuationDetailsByEvacuationID(EvacuationResponsible evacuationResponsible) {

        try {

            showPD(this);
            SoapGetEvacuationResponsiblesRequestEnvelope soapGetEvacuationResponsiblesRequestEnvelope =
                    new SoapGetEvacuationResponsiblesRequestEnvelope();

            GetResponsiblesRequestBody getResponsiblesRequestBody = new GetResponsiblesRequestBody();
            GetEvacuationResponsibles getEvacuationResponsibles = new GetEvacuationResponsibles();
            getEvacuationResponsibles.setA_sClientId(evacuationResponsible.getClientId());
            getEvacuationResponsibles.setAuthToken(getAuthTokenPref_Evacuation());
            evaId = evacuationResponsible.getEvacuationId();
            placeID = evacuationResponsible.getA_sPlaceId();
            getEvacuationResponsibles.setA_sEvacuationId(evaId);
            getResponsiblesRequestBody.setGetEvacuationResponsibles(getEvacuationResponsibles);
            soapGetEvacuationResponsiblesRequestEnvelope.setBody(getResponsiblesRequestBody);

            Observable<SoapGetEvacuationResponsiblesResponseEnvelope> lClientDetailsObservable =
                    service.getEvacuationResponsibles(soapGetEvacuationResponsiblesRequestEnvelope);
            lClientDetailsObservable.
                    subscribeOn(Schedulers.io())
                    .map(this::onSoapGetEvacuation)
                    .map(this::onMap)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onEvacuationSuccess, this::onEvacuationError);
        } catch (Exception e) {
            Constants.e("err", e.toString());
        }
    }

    private void onEvacuationError(Throwable throwable) {

        hidePD();
        IpAddress.e("error", throwable.toString());
    }


    private void onEvacuationSuccess(EvacuationResponsibles evacuationResponsibles) {

        if (flag_clickabale_revokeclear) {
            btstatus.setEnabled(true);
            btstatus.setClickable(true);
            IpAddress.e("Button", "click true enable");
        } else {
            btstatus.setClickable(false);
            btstatus.setEnabled(false);
            IpAddress.e("Button", "click false disable");
        }

        LinearLayoutManager mLayoutManager =
                (LinearLayoutManager) rvlist.getLayoutManager();
        if (evacuationResponsibleList != null
                && evacuationResponsibleList.size() > 1) {

//            mLayoutManager.setStackFromEnd(false);
//            mLayoutManager.setReverseLayout(false);
        } else {

//            mLayoutManager.setStackFromEnd(false);
//            mLayoutManager.setReverseLayout(false);
        }
        rvlist.setLayoutManager(mLayoutManager);
        adapterAdmin.notifyDataSetChanged();
        changeEvacuationDetails();
        hidePD();
        getUnreadMessages();

    }

    private void getUnreadMessages() {

        showPD(this);
        String barcode = null;
        String appName = null;
        Observable<SoapGetUnreadMessageCountResponseEnvelope> lClientDetailsObservable;
        //  if (evacuationResponsible.getUserType() != null
        //        && evacuationResponsible.getUserType().equalsIgnoreCase(IpAddress.NORMALUSER)) {
        barcode = getBarcode();
        appName = IpAddress.VIPadm;
        // evaId = evacuationResponsible.getEvacuationId();
        if (getEvacuationResponsibles != null)
            evaId = getEvacuationResponsibles.getA_sEvacuationId();
//        } else {
//
//            barcode = getBarcode();
//            appName = IpAddress.VIPRECEPTION;
//            evaId = "";
//        }
        if (barcode != null && evaId != null && appName != null) {

            SoapUnreadMessageCountRequestEnvelope soapUnreadMessageCountRequestEnvelope =
                    new SoapUnreadMessageCountRequestEnvelope();


            UnreadMessageCountRequestBody unreadMessageCountRequestBody = new UnreadMessageCountRequestBody();

            GetUnReadMessageCount getUnReadMessageCount = new GetUnReadMessageCount();

            getUnReadMessageCount.setA_sAppName(appName);
            getUnReadMessageCount.setA_sBarcode(barcode);
            getUnReadMessageCount.setA_sEvacuationId(evaId);

            getUnReadMessageCount.setA_sClientId(getClientID());
            getUnReadMessageCount.setA_sPlaceId(placeID);
            getUnReadMessageCount.setAuthToken(getAuthTokenPref_Evacuation());
            unreadMessageCountRequestBody.setGetUnReadMessageCount(getUnReadMessageCount);
            soapUnreadMessageCountRequestEnvelope.setBody(unreadMessageCountRequestBody);

            lClientDetailsObservable =
                    service.getResposibleUnReadEvacuationMessagesCount(soapUnreadMessageCountRequestEnvelope);
            lClientDetailsObservable.
                    subscribeOn(Schedulers.io())
                    .map(this::onSoapCount)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onCount, this::onCountError);
        }

    }

    public void onCountError(Throwable throwable) {


        hidePD();


    }


    private String onSoapCount(SoapGetUnreadMessageCountResponseEnvelope soapGetUnreadMessageCountResponseEnvelope) {

        GetUnReadMessageCountResponseBody getUnReadMessageCountResponseBody =
                soapGetUnreadMessageCountResponseEnvelope.getBody();

        return getUnReadMessageCountResponseBody.getGetUnReadMessageCountResponse().getUnreadResult();
    }


    private void onCount(String status) {


        hidePD();
        if (status != null) {
            try {
                int count = Integer.parseInt(status.trim());
                if (count <= 0) {
                    tvcount.setBackgroundResource(0);
                    tvcount.setText("");
                    ivcount.setImageResource(0);
                } else {
                    tvcount.setBackgroundResource(R.drawable.circle);
                    tvcount.setText(status.trim());
                    ivcount.setImageResource(R.drawable.circleunreadarea);

                }

            } catch (Exception e) {

                ivcount.setImageResource(0);
                tvcount.setBackgroundResource(0);
                tvcount.setText("");
            }

        } else {
            showToast(getString(R.string.someerror));
        }

    }


    private void changeEvacuationDetails() {

        if (evacuationResponsibleNew != null) {
            setBtStatus();
        }
    }

    private EvacuationResponsibles onMap(EvacuationResponsibles evacuationResponsibles) {

        evacuationResponsibleList.clear();


        for (EvacuationResponsible evacuationResponsible : evacuationResponsibles.getEvacuationResponsible()) {
            if (areaName != null &&
                    areaName.equalsIgnoreCase(evacuationResponsible.getAreaName())) {

                if (evacuationResponsible.getBarcode().equalsIgnoreCase(getBarcode()))

                    evacuationResponsibleNew = evacuationResponsible;
                break;
            }
        }

        EvacuationResponsible evacuationResponsibleBarcode = null;
        if (evacuationResponsibles != null && evacuationResponsibles.getEvacuationResponsible() != null)

            for (EvacuationResponsible evacuationResponsible1 : evacuationResponsibles.getEvacuationResponsible()) {
                if (evacuationResponsible1.getBarcode() != null
                        && getBarcode() != null
                        && evacuationResponsible1.getBarcode().equalsIgnoreCase(getBarcode())) {

                    evacuationResponsibleBarcode = evacuationResponsible1;
                    evacuationResponsibleNew = evacuationResponsibleBarcode;
                    evacuationResponsibleList.add(evacuationResponsible1);
                    break;
                }
            }

        for (EvacuationResponsible evacuationResponsible1 : evacuationResponsibles.getEvacuationResponsible()) {
            if (evacuationResponsible1.getBarcode() != null
                    && getBarcode() != null && evacuationResponsible1.getBarcode().
                    equalsIgnoreCase(getBarcode())) {
            } else {
                evacuationResponsibleList.add(evacuationResponsible1);
            }
        }

//        Collections.sort(evacuationResponsibleList, new Comparator<EvacuationResponsible>() {
//            @Override
//            public int compare(EvacuationResponsible o1, EvacuationResponsible o2) {
//                return o1.getPriority().compareTo(o2.getPriority());
//            }
//        });


        flag_clickabale_revokeclear = true;
        boolean flag_clickabale = true;
        for (EvacuationResponsible evacuationResponsible :
                evacuationResponsibles.getEvacuationResponsible()) {
            if (!getBarcode().equalsIgnoreCase(evacuationResponsible.getBarcode())) {

                if (evacuationResponsibleNew.getUserAvailability() != null
                        && evacuationResponsibleNew.getUserAvailability()
                        .equalsIgnoreCase("True")) {
                    if (evacuationResponsibleNew.getPriority()
                            .compareTo(evacuationResponsible.getPriority()) < 0) {

                        flag_clickabale = true;
                        break;
                    } else {
                        flag_clickabale = false;
                    }

                } else {
                    flag_clickabale = false;
                }

            } else if (getBarcode().equalsIgnoreCase(evacuationResponsible.getBarcode())) {

                flag_clickabale = true;
            } else {
                flag_clickabale = false;
            }
        }

        flag_clickabale_revokeclear = flag_clickabale;


        return evacuationResponsibles;
    }

    private EvacuationResponsibles onSoapGetEvacuation(SoapGetEvacuationResponsiblesResponseEnvelope
                                                               soapGetEvacuationResponsiblesResponseEnvelope) {


        GetResponsibleResponseBody getResponsibleResponseBody =
                soapGetEvacuationResponsiblesResponseEnvelope.getBody();


        return getResponsibleResponseBody.getGetEvacuationResponsiblesResponse().
                getGetEvacuationResponsiblesResult().getGetEvacuationResponsibles();
    }


    @OnClick(R.id.ivback)
    public void onBack(View v) {
        onBackPressed();
    }


    private void readEva() {

        // clearFresco();
        if (hasNetwork()) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            showPD(this);
            getEvacuationDetails();
        } else {
            hidePD();
            showToast(getString(R.string.nointernet));
        }
    }


    private void updateCleartime() {

        showPD(this);
        SoapUpdateClearTimeRequestEnvelope soapUpdateClearTimeRequestEnvelope =
                new SoapUpdateClearTimeRequestEnvelope();
        UpdateClearTimeRequestBody updateClearTimeRequestBody = new UpdateClearTimeRequestBody();

        UpdateClearTimeToEvacuation updateClearTimeToEvacuation = new UpdateClearTimeToEvacuation();
        updateClearTimeToEvacuation.setAuthToken(getAuthTokenPref_Evacuation());
        updateClearTimeToEvacuation.setA_sClientId(getClientID());
        updateClearTimeToEvacuation.setsEvacuationId(evacuationResponsibleNew.getEvacuationId());

        updateClearTimeToEvacuation.setsCleredBy(getBarcode());

        updateClearTimeRequestBody.setUpdateClearTimeToEvacuation(updateClearTimeToEvacuation);
        soapUpdateClearTimeRequestEnvelope.setBody(updateClearTimeRequestBody);
        Observable<SoapUpdateClearTimeResponseEnvelope> lClientDetailsObservable =
                service.updateClearTimetoEvacuation(soapUpdateClearTimeRequestEnvelope);
        lClientDetailsObservable.
                subscribeOn(Schedulers.io())
                .map(this::onSoapUpdateClearTime)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onUpate, this::onError);


    }

    private void onUpate(String status) {


        // notifyaboutAreaClearence();
        try {
            pushService.notifyaboutAreaClearence(evacuationResponsibleNew.getA_sPlaceId(),
                    evacuationResponsibleNew.getEvacuationId());
        } catch (Exception e) {

        }
        onPush(IpAddress.SUCCESS);

    }

    private void notifyaboutAreaClearence() {


        SoapNotifyAboutAreaClearence soapNotifyAboutAreaClearence =
                new SoapNotifyAboutAreaClearence();

        SoapNotifyAboutAreaClearenceRequestBody notifyAboutAreaClearenceRequestBody =
                new SoapNotifyAboutAreaClearenceRequestBody();


        NotifyAboutAreaClearence notifyAboutAreaClearence = new NotifyAboutAreaClearence();


        notifyAboutAreaClearence.setA_iPlaceId(evacuationResponsibleNew.getA_sPlaceId());

        notifyAboutAreaClearence.setAuthToken(getAuthTokenPref_Evacuation());

        notifyAboutAreaClearence.setA_sEvacuationIds(evacuationResponsibleNew.getEvacuationId());

        notifyAboutAreaClearenceRequestBody.setNotifyAboutAreaClearence(notifyAboutAreaClearence);

        soapNotifyAboutAreaClearence.setBody(notifyAboutAreaClearenceRequestBody);

        Observable<SoapNotifyAboutAreaClearenceResponse> lClientDetailsObservable =
                service.notifyAboutAreaClearence(soapNotifyAboutAreaClearence);

        lClientDetailsObservable.
                subscribeOn(Schedulers.io())
                .map(this::onSoapEvacuationPush)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPush, this::onPushError);

    }

    private String onSoapEvacuationPush(SoapNotifyAboutAreaClearenceResponse soapNotifyAboutAreaClearenceResponse) {


        try {

            return soapNotifyAboutAreaClearenceResponse.getBody()
                    .getNotifyAboutAreaClearenceResponse().getNotifyAboutAreaClearenceResult().get_string();
        } catch (Exception e) {
            return "";
        }

    }


    private String onSoapUpdateClearTime(SoapUpdateClearTimeResponseEnvelope soapUpdateClearTimeResponseEnvelope) {

        try {
            UpdateClearTimeResponseBody updateClearTimeResponseBody = soapUpdateClearTimeResponseEnvelope.getBody();

            return updateClearTimeResponseBody.getUpdateClearTimeToEvacuationResponse().getUpdateClearTime();
        } catch (Exception e) {
            return "";
        }
    }


    @OnClick(R.id.btstatus)
    public void onbtstatusUndo(View v) {


        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (alertDialog_share != null && alertDialog_share.isShowing())
            alertDialog_share.dismiss();
        if (evacuationResponsibleNew != null
                && evacuationResponsibleNew.getSTATUS() != null &&
                evacuationResponsibleNew.getSTATUS().equalsIgnoreCase(IpAddress.STATUS_STARTED)) {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Evacuation");


            if (evacuationResponsibleNew.getClearTime() == null
                    || evacuationResponsibleNew.getClearTime().trim().isEmpty())

                if (evacuationResponsibleNew.getUserAvailability() != null
                        && evacuationResponsibleNew.getUserAvailability()
                        .equalsIgnoreCase("True")) {
                    builder.setMessage("Are you sure '" + evacuationResponsibleNew.getAreaName()
                            + "'" + " is empty of people?");

                } else {
                    builder.setMessage("Are you sure you want to revoke the clearence of " +
                            "'" + evacuationResponsibleNew.getAreaName() + "'" + " is empty of people?");
                }
            else
                builder.setMessage("Are you sure you want to revoke the clearence of " +
                        "'" + evacuationResponsibleNew.getAreaName() + "'" + " is empty of people?");


            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    oldBtStatus();
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alertDialog_share = builder.show();
        }

    }


    public void oldBtStatus() {
        if (evacuationResponsibleNew != null) {

            if (evacuationResponsibleNew.getSTATUS() != null) {
                if (evacuationResponsibleNew.getSTATUS().equalsIgnoreCase(IpAddress.STATUS_STARTED)) {


                    if (evacuationResponsibleNew.getClearTime() == null
                            || evacuationResponsibleNew.getClearTime().trim().isEmpty()) {

                        if (evacuationResponsibleNew.getUserAvailability() != null
                                && evacuationResponsibleNew.getUserAvailability()
                                .equalsIgnoreCase("True"))


                            updateCleartime();

                    } else {

                        if (evacuationResponsibleNew.getUserAvailability() != null
                                && evacuationResponsibleNew.getUserAvailability()
                                .equalsIgnoreCase("True"))

                            removeClearTime();

                    }
                }
            }
        }

    }


    private void removeClearTime() {


        showPD(this);
        SoapRemoveClearTimeRequestEnvelope soapRemoveClearTimeRequestEnvelope =
                new SoapRemoveClearTimeRequestEnvelope();

        RemoveClearTimeRequestBody removeClearTimeRequestBody = new RemoveClearTimeRequestBody();

        RemoveClearTimeToEvacuation removeClearTimeToEvacuation = new RemoveClearTimeToEvacuation();
        removeClearTimeToEvacuation.setA_sClientId(getClientID());
        removeClearTimeToEvacuation.setAuthToken(getAuthTokenPref_Evacuation());
        removeClearTimeToEvacuation.setsEvacuationId(evacuationResponsibleNew.getEvacuationId());
        removeClearTimeToEvacuation.setsCleredBy(getBarcode());
        removeClearTimeRequestBody.setSetClearTimeToEvacuations(removeClearTimeToEvacuation);
        soapRemoveClearTimeRequestEnvelope.setBody(removeClearTimeRequestBody);

        Observable<SoapRemoveClearTimeResponseEnvelope> lClientDetailsObservable =
                service.removeClearTimetoEvacuation(soapRemoveClearTimeRequestEnvelope);

        lClientDetailsObservable.
                subscribeOn(Schedulers.io())
                .map(this::onSoapRemove)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onRemove, this::onError);

    }

    private void onRemove(String status) {

        if (status != null) {
            // notifyaboutAreaClearence();
            onPush(IpAddress.SUCCESS);

            try {
                pushService.notifyaboutAreaClearence(evacuationResponsibleNew.getA_sPlaceId(),
                        evacuationResponsibleNew.getEvacuationId());
            } catch (Exception e) {

            }


        } else {
            showToast(getString(R.string.someerror));
        }

    }


    //1253
    private void sendPushMessage() {

        SoapNotifyAboutResponsibleAvailability soapNotifyAboutResponsibleAvailability =
                new SoapNotifyAboutResponsibleAvailability();

        NotifyAboutResponsibleAvailabilityBody notifyAboutResponsibleAvailabilityBody =
                new NotifyAboutResponsibleAvailabilityBody();


        NotifyAboutResponsibleAvailability notifyAboutResponsibleAvailability = new NotifyAboutResponsibleAvailability();

        notifyAboutResponsibleAvailability.setA_iPlaceId(evacuationResponsibleNew.getA_sPlaceId());
        notifyAboutResponsibleAvailability.setA_sBarcode(getBarcode());
        notifyAboutResponsibleAvailability.setAuthToken(getAuthTokenPref_Evacuation());

        notifyAboutResponsibleAvailabilityBody.setNotifyAboutResponsibleAvailability(notifyAboutResponsibleAvailability);

        soapNotifyAboutResponsibleAvailability.setBody(notifyAboutResponsibleAvailabilityBody);

        Observable<SoapNotifyAboutResponsibleAvailabilityResponse> lClientDetailsObservable =
                service.notifyAboutResponsibleAvailability(soapNotifyAboutResponsibleAvailability);

        lClientDetailsObservable.
                subscribeOn(Schedulers.io())
                .map(this::onSoapEvacuationPush)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPush, this::onPushError);
    }


    private String onSoapEvacuationPush(SoapNotifyAboutResponsibleAvailabilityResponse soapSendEvacuationPushResponseEnvelope) {


        try {
            NotifyAboutResponsibleAvailabilityResponseBody soapNotifyAboutAreaClearenceResponse =
                    soapSendEvacuationPushResponseEnvelope.getBody();

            return soapNotifyAboutAreaClearenceResponse.getNotifyAboutResponsibleAvailabilityResponse()
                    .getNotifyAboutResponsibleAvailabilityResult().get_string();
        } catch (Exception e) {
            return "";
        }

    }


    private String onSoapEvacuationPush(SoapSendEvacuationPushResponseEnvelope soapSendEvacuationPushResponseEnvelope) {


        try {
            SendEvacuationPushResponseBody sendEvacuationPushResponseBody =
                    soapSendEvacuationPushResponseEnvelope.getBody();

            return sendEvacuationPushResponseBody.getSendEvacuationPushResponse().getSendEvacuationPush();
        } catch (Exception e) {
            return "";
        }

    }

    public void onPushError(Throwable throwable) {


        readEva();

    }

    private void onPush(String status) {

        flag = "";
        readEva();

    }


    private String onSoapRemove(SoapRemoveClearTimeResponseEnvelope soapRemoveClearTimeResponseEnvelope) {

        RemoveClearTimeResponseBody removeClearTimeResponseBody = soapRemoveClearTimeResponseEnvelope.getBody();

        return removeClearTimeResponseBody.getRemoveClearTimeToEvacuationResponse().getRemoveClearTime();

    }


    private void getEvacuationDetails() {

        try {

            tvcount.setText("");
            ivcount.setImageResource(0);
            SoapEvacuationResponsibleDetailsRequestEnvelope soapEvacuationResponsibleDetailsRequestEnvelope =
                    new SoapEvacuationResponsibleDetailsRequestEnvelope();

            GetResponsibleDetailsRequestBody getResponsibleDetailsRequestBody =
                    new GetResponsibleDetailsRequestBody();

            GetResponsibleDetails getResponsibleDetails = new GetResponsibleDetails();
            getResponsibleDetails.setA_sBarcode(getBarcode());
            getResponsibleDetails.setA_sClientId(getClientID());
            getResponsibleDetails.setAuthToken(getAuthTokenPref_Evacuation());

            getResponsibleDetailsRequestBody.setGetResponsibleDetails(getResponsibleDetails);

            soapEvacuationResponsibleDetailsRequestEnvelope.setBody(getResponsibleDetailsRequestBody);

            Observable<SoapEvacuationResponsibleDetailsResponseEnvelope> lClientDetailsObservable =
                    service.getEvacuationResponsibleDetails(soapEvacuationResponsibleDetailsRequestEnvelope);

            lClientDetailsObservable.
                    subscribeOn(Schedulers.io())
                    .map(this::onEvacuationSort)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onEvacuation, this::onError);


        } catch (Exception e) {
            hidePD();
            showToast(getString(R.string.someerror));
        }
    }


    private List<EvacuationResponsible> onEvacuationSort(SoapEvacuationResponsibleDetailsResponseEnvelope
                                                                 soapEvacuationResponsibleDetailsResponseEnvelope) {

        EvacuationResponsibles evacuationResponsibles =
                soapEvacuationResponsibleDetailsResponseEnvelope.getBody()
                        .getGetResponsibleDetailsResponse()
                        .getGetResponsibleDetailsResult().getEvacuationResponsibles();
        evacuationResponsibleModelList = new ArrayList<>();

        if (evacuationResponsibles != null) {

            evacuationResponsibleModelList.addAll(evacuationResponsibles.getEvacuationResponsible());

            List<EvacuationResponsible> evacuationResponsibleList = evacuationResponsibleModelList;


            Collections.sort(evacuationResponsibleList, new Comparator<EvacuationResponsible>() {
                @Override
                public int compare(EvacuationResponsible o1, EvacuationResponsible o2) {
                    return o1.getPriority().compareTo(o2.getPriority());
                }
            });
            for (EvacuationResponsible evacuationResponsible : evacuationResponsibleList) {
                if (getBarcode().equalsIgnoreCase(evacuationResponsible.getBarcode())) {

                    evacuationResponsibleNew = evacuationResponsible;
                    break;
                }
            }
//            flag_clickabale_revokeclear=false;
//            boolean flag_clickabale=false;
//            for (EvacuationResponsible evacuationResponsible : evacuationResponsibleList) {
//                if (!getBarcode().equalsIgnoreCase(evacuationResponsible.getBarcode())) {
//
//                    if (evacuationResponsible.getUserAvailability() != null
//                            && evacuationResponsible.getUserAvailability()
//                            .equalsIgnoreCase("True"))
//                    {
//                        //flag_clickabale=true;
//                        if(evacuationResponsible.getPriority()
//                                .compareTo(evacuationResponsibleNew.getPriority())<0)
//                        {
//
//                           flag_clickabale=true;
//                            break;
//                        }
//
//                    }
//
//                }
//            }
//
//            flag_clickabale_revokeclear=flag_clickabale;
//

        }

        return evacuationResponsibleModelList;
    }

    private void onEvacuation(List<EvacuationResponsible> evacuationResponsibleModelList) {

        setImageResource();
        setSpiiner();

    }

    private void setImageResource() {

        if (evacuationResponsibleNew != null) {
            if (evacuationResponsibleNew.getUserAvailability() != null &&
                    evacuationResponsibleNew.getUserAvailability()
                            .equalsIgnoreCase(IpAddress.USERAVAILABILITY_TRUE)) {

                tvno.setVisibility(View.GONE);
                ivcount_image.setImageResource(R.drawable.circleyes);
            } else if (evacuationResponsibleNew.getUserAvailability() != null &&
                    evacuationResponsibleNew.getUserAvailability()
                            .equalsIgnoreCase(IpAddress.USERAVAILABILITY_FALSE)) {

                tvno.setVisibility(View.VISIBLE);
                ivcount_image.setImageResource(R.drawable.circleempty);
            } else {
                tvno.setVisibility(View.GONE);
                ivcount_image.setImageResource(R.drawable.circleno);

            }


            if (evacuationResponsibleNew.getPhotoUrl() != null
                    && evacuationResponsibleNew.getPhotoUrl().trim().length() > 0) {

                ivimage.setImageURI(evacuationResponsibleNew.getPhotoUrl());
                ivimagecenter.setImageURI(evacuationResponsibleNew.getPhotoUrl());
            } else {
                ivimage.setImageResource(R.mipmap.ic_profile);
                ivimagecenter.setImageResource(R.mipmap.ic_profile);
            }


            tvtext2.setText(evacuationResponsibleNew.getName());
        }


    }

    private void setBtStatus() {


        if (evacuationResponsibleNew.getUserAvailability() == null
                || evacuationResponsibleNew.getUserAvailability().trim().isEmpty()
                || evacuationResponsibleNew.getUserAvailability().equalsIgnoreCase("False")) {


            if (evacuationResponsibleNew.getUserAvailability() != null
                    && evacuationResponsibleNew.getUserAvailability()
                    .trim().length() > 0 && evacuationResponsibleNew.getUserAvailability()
                    .equalsIgnoreCase("False")) {


                // llyesno.setBackgroundResource(R.drawable.gradient_tv_yes);
                cvno.setBackgroundResource(R.drawable.gradient_darkno_2);
                //  cvyes.setBackgroundResource(R.drawable.gradient_darkyes_1);
                ViewCompat.setElevation(cvyes, 0);
                ViewCompat.setElevation(cvno, 0);

            } else {
                cvno.setCardBackgroundColor(Color.TRANSPARENT);
                cvno.setCardElevation(0);
                cvno.setBackgroundResource(0);
                cvyes.setBackgroundResource(0);
                ViewCompat.setElevation(cvyes, 0);
                ViewCompat.setElevation(cvno, 0);
                llyesno.setBackgroundResource(R.drawable.gradient_tv);
            }


            //show yes no

            llyesno.setVisibility(View.VISIBLE);
            tvtext.setVisibility(View.VISIBLE);
            tvtext.setText("Are you in the building?");
            llyes.setVisibility(View.VISIBLE);
            btstatus.setVisibility(View.INVISIBLE);
            tvcountertop.setVisibility(View.INVISIBLE);
            tvtimercenter.setVisibility(View.VISIBLE);

            ivimage.setOnClickListener(null);
            chageImage();
            tvtext1.setText(getString(R.string.yesno));


            if (evacuationResponsibleNew.getSTATUS() != null) {
                if (evacuationResponsibleNew.getSTATUS().equalsIgnoreCase(IpAddress.STATUS_STARTED)) {
                    enableEvacation();
                    tvstatus.setTextColor(ContextCompat.getColor(EvacuationNormal2.this, R.color.md_white_1000));
                    rgbuilding.setAlpha(1f);

                } else if (evacuationResponsibleNew.getSTATUS().equalsIgnoreCase(IpAddress.STATUS_Ended)) {

                    disableEvacuation();
                    tvstatus.setTextColor(ContextCompat.getColor(EvacuationNormal2.this, R.color.color_yellow));
                    rgbuilding.setAlpha(0.5f);
                    tvtimercenter.setVisibility(View.INVISIBLE);
                }
            }

            timerShow(evacuationResponsibleNew.getStartTime(), evacuationResponsibleNew.getEndTime(),
                    true);
        } else {

            tvtext1.setText("");
            rgbuilding.setAlpha(1f);
            llyes.setVisibility(View.INVISIBLE);
            llyesno.setVisibility(View.GONE);

            btstatus.setVisibility(View.VISIBLE);

            // tvtext.setVisibility(View.INVISIBLE);
            tvtext.setVisibility(View.VISIBLE);

            ivimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (evacuationResponsibleNew.getSTATUS() != null)
                        if (evacuationResponsibleNew.getSTATUS().equalsIgnoreCase(IpAddress.STATUS_STARTED)) {

                            showYesNoDialog();
                        }
                }
            });


            tvcountertop.setVisibility(View.VISIBLE);
            tvtimercenter.setVisibility(View.INVISIBLE);

            if (evacuationResponsibleNew.getSTATUS() != null) {
                if (evacuationResponsibleNew.getSTATUS().equalsIgnoreCase(IpAddress.STATUS_STARTED)) {

                    enableEvacation();
                    tvstatus.setTextColor(ContextCompat.getColor(EvacuationNormal2.this, R.color.md_white_1000));


                } else if (evacuationResponsibleNew.getSTATUS().equalsIgnoreCase(IpAddress.STATUS_Ended)) {

                    disableEvacuation();
                    tvstatus.setTextColor(ContextCompat.getColor(EvacuationNormal2.this, R.color.color_yellow));


                }
            }


            if (evacuationResponsibleNew.getSTATUS() == null
                    || evacuationResponsibleNew.getSTATUS().trim().
                    equalsIgnoreCase(IpAddress.STATUS_Ended)) {

                tvcountertop.setVisibility(View.VISIBLE);
                tvtimercenter.setVisibility(View.INVISIBLE);
            }


            if (evacuationResponsibleNew.getClearTime() == null ||
                    evacuationResponsibleNew.getClearTime().trim().isEmpty()) {

                btstatus.setTextColor(ContextCompat.getColor(EvacuationNormal2.this, R.color.md_black_1000));
                btstatus.setText(getString(R.string.declarearea));
                btstatus.setBackgroundResource(R.drawable.gradient_tv);
                //  tvtext1.setText(getString(R.string.let));

                // tvtext1.setText("");
                tvtext.setText(getString(R.string.let));
            } else {

                btstatus.setTextColor(ContextCompat.getColor(EvacuationNormal2.this, R.color.md_black_1000));
                btstatus.setText("Revoke clearance");
                btstatus.setBackgroundResource(R.drawable.gradient_tv);
            }
            timerShow(evacuationResponsibleNew.getStartTime(), evacuationResponsibleNew.getEndTime(), false);

        }


        if (evacuationResponsibleNew.getSTATUS() != null
                && evacuationResponsibleNew.getSTATUS().equalsIgnoreCase(IpAddress.STATUS_Ended)) {


            if (evacuationResponsibleNew.getClearTime() == null
                    || evacuationResponsibleNew.getClearTime().trim().isEmpty()) {

                tvcountertop.setVisibility(View.GONE);
                if (timer != null) {

                    timer.cancel();
                    timer = null;
                }

            }
        }

        hidePD();
    }


    private void runTimer(final long diffMs) {


        try {

            if (timer != null) {

                timer.cancel();
                timer = null;
                timer = new Timer(Long.MAX_VALUE, 1000);
                timer.start();
            } else {
                timer = new Timer(Long.MAX_VALUE, 1000);
                timer.start();

            }

        } catch (Exception e) {

        }


    }

    long secondsInMilli = 1000;
    long minutesInMilli = secondsInMilli * 60;
    long hoursInMilli = minutesInMilli * 60;
    long daysInMilli = hoursInMilli * 24;


    public String CalculateTime(Long aLong) {


        long diff = aLong;

        String time = "";

        long elapsedDays = diff / daysInMilli;
        diff = diff % daysInMilli;

        long elapsedHours = diff / hoursInMilli;
        diff = diff % hoursInMilli;

        long elapsedMinutes = diff / minutesInMilli;
        diff = diff % minutesInMilli;

        long elapsedSeconds = diff / secondsInMilli;

        if (elapsedDays > 0) {
            time = numberFormat.format(elapsedDays) + ":" +
                    numberFormat.format(elapsedHours) + ":" +
                    numberFormat.format(elapsedMinutes) + ":" +
                    numberFormat.format(elapsedSeconds);
        } else if (elapsedHours > 0) {
            time = numberFormat.format(elapsedHours) + ":" +
                    numberFormat.format(elapsedMinutes) + ":" +
                    numberFormat.format(elapsedSeconds);
        } else if (elapsedMinutes > 0) {
            time = "00:" + numberFormat.format(elapsedMinutes) + ":" +
                    numberFormat.format(elapsedSeconds);

        } else if (elapsedSeconds > 0) {
            time = "00:00:" + numberFormat.format(elapsedSeconds) + "";
        }

        //  IpAddress.e("TIME", time);

        return time;
    }

    public class Timer extends CountDownTimer {


//        long secondsInMilli = 1000;
//        long minutesInMilli = secondsInMilli * 60;
//        long hoursInMilli = minutesInMilli * 60;
//        long daysInMilli = hoursInMilli * 24;

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public Timer(long millisInFuture, long countDownInterval) {

            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            different += 1000L;
            String time = CalculateTime(different);

            tvcountertop.setText(time);
            tvtimercenter.setText(time);


        }

        @Override
        public void onFinish() {

        }
    }


    private void timerShow(String starttime, String endTime, boolean isYesNo) {

        if (starttime != null && starttime.trim().length() > 0) {

            try {
                Date date1 = simpleDateFormat.parse(starttime);

                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(date1);

                Date date2 = null;
                if (endTime != null && endTime.trim().length() > 0) {


                    date2 = simpleDateFormat.parse(endTime);

                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(date2);

                    different = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();

                    long diff = different;

                    String time = "";

                    long elapsedDays = diff / daysInMilli;
                    diff = diff % daysInMilli;

                    long elapsedHours = diff / hoursInMilli;
                    diff = diff % hoursInMilli;

                    long elapsedMinutes = diff / minutesInMilli;
                    diff = diff % minutesInMilli;

                    long elapsedSeconds = diff / secondsInMilli;

                    if (elapsedDays > 0) {
                        time = elapsedDays + ":" + elapsedHours + ":" + elapsedMinutes + ":" + elapsedSeconds;
                    } else if (elapsedHours > 0) {
                        time = elapsedHours + ":" + elapsedMinutes + ":" + elapsedSeconds;
                    } else if (elapsedMinutes > 0) {
                        time = "00:" + elapsedMinutes + ":" + elapsedSeconds;

                    } else if (elapsedSeconds > 0) {
                        time = "00:00:" + elapsedSeconds + "";
                    }
                    tvtimediff.setText(time);
                } else {
                    tvtimediff.setText("");
                }


                if (evacuationResponsibleNew.getClearDate() != null &&
                        !evacuationResponsibleNew.getClearDate().trim().isEmpty()) {


                    date2 = simpleDateFormat.parse(evacuationResponsibleNew.getClearDate());
                    different = date2.getTime() - calendar1.getTimeInMillis();

                    long diff = different;

                    String time = "";

                    long elapsedDays = diff / daysInMilli;
                    diff = diff % daysInMilli;

                    long elapsedHours = diff / hoursInMilli;
                    diff = diff % hoursInMilli;

                    long elapsedMinutes = diff / minutesInMilli;
                    diff = diff % minutesInMilli;

                    long elapsedSeconds = diff / secondsInMilli;

                    if (elapsedDays > 0) {
                        time = numberFormat.format(elapsedDays) + " days " +
                                numberFormat.format(elapsedHours) + " hour " +
                                numberFormat.format(elapsedMinutes) + " minuts " +
                                numberFormat.format(elapsedSeconds) + " seconds ";
                    } else if (elapsedHours > 0) {

                        time = numberFormat.format(elapsedHours) + " hour " +
                                numberFormat.format(elapsedMinutes) + " minuts " +
                                numberFormat.format(elapsedSeconds) + " seconds";
                    } else if (elapsedMinutes > 0) {
                        time =
                                numberFormat.format(elapsedMinutes) + " minuts " +
                                        numberFormat.format(elapsedSeconds) + " seconds ";

                    } else if (elapsedSeconds > 0) {
                        time = numberFormat.format(elapsedSeconds) + " seconds ";
                    }
                    if (isYesNo) {

                        int leftMargin = (int) TypedValue.applyDimension
                                (TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
                        ViewGroup.MarginLayoutParams lpt = (ViewGroup.MarginLayoutParams)
                                tvtext.getLayoutParams();
                        lpt.setMargins(leftMargin, leftMargin, leftMargin, leftMargin);

                    } else {
                        int Margin = (int) TypedValue.applyDimension
                                (TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
                        tvtext.setText("This area was cleared within \n" +
                                time);
                        float leftMargin = TypedValue.applyDimension
                                (TypedValue.COMPLEX_UNIT_DIP, 32, getResources().getDisplayMetrics());
                        ViewGroup.MarginLayoutParams lpt = (ViewGroup.MarginLayoutParams)
                                tvtext.getLayoutParams();
                        lpt.setMargins((int) leftMargin, Margin, Margin, Margin);

                    }

                    if (llyesno.getVisibility() == View.VISIBLE) {

                    } else {
                        tvtext1.setText("");
                    }

                    if (elapsedDays > 0) {
                        time = numberFormat.format(elapsedDays) + ":" + numberFormat.format(elapsedHours) + ":" +
                                numberFormat.format(elapsedMinutes) + ":" + numberFormat.format(elapsedSeconds);
                    } else if (elapsedHours > 0) {
                        time = numberFormat.format(elapsedHours) + ":" + numberFormat.format(elapsedMinutes) + ":" +
                                numberFormat.format(elapsedSeconds);
                    } else if (elapsedMinutes > 0) {
                        time = "00:" + numberFormat.format(elapsedMinutes) + ":" + numberFormat.format(elapsedSeconds);

                    } else if (elapsedSeconds > 0) {
                        time = "00:00:" + numberFormat.format(elapsedSeconds) + "";
                    }


                    if (isYesNo) {
                        tvcountertop.setText("");
                        tvtimercenter.setText(time);
                    } else {

                        tvtimercenter.setText("");
                        tvcountertop.setText(time);

                    }


                    if (timer != null) {
                        timer.cancel();
                        timer = null;

                    } else {

                    }

                } else {

                    showTimer(evacuationResponsibleNew.getStartTime().trim());
                }

            } catch (Exception e) {
            }
        }

    }

    private void showTimer(String startTime) {

        try {
            Date date1 = simpleDateFormat.parse(startTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date1);
            Date date2 = Constants.formatGMT2(new Date());
            long diffMs = date2.getTime() - date1.getTime();
            different = diffMs;
            runTimer(diffMs);

        } catch (Exception e) {

            tvcountertop.setText("");
            tvtimercenter.setText("");
        }

    }


    public class SpinnerAdapter extends ArrayAdapter<EvacuationResponsible> {


        public SpinnerAdapter() {

            super(EvacuationNormal2.this, 0, evacuationResponsibleModelList);
        }


        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


            EvacuationResponsible evacuationResponsible = evacuationResponsibleModelList.get(position);
            convertView = LayoutInflater.from(EvacuationNormal2.this).inflate(
                    R.layout.inflate_evacationnormal2, parent, false
            );
            TextView tv2 = convertView.findViewById(R.id.text2);
            ConstraintLayout clbg = convertView.findViewById(R.id.clbg);
            tv2.setText(evacuationResponsible.getPriority().toUpperCase() + " responsible ".toUpperCase());

            TextView tv = convertView.findViewById(R.id.text1);
            try {
                tv.setText(ordinal(Integer.parseInt(evacuationResponsible.getFloor())) + " Floor, " +
                        evacuationResponsible.getAreaName() + " ");
            } catch (Exception e) {
                tv.setText(evacuationResponsible.getFloor() + " Floor, " +
                        evacuationResponsible.getAreaName() + " ");
            }

            if (sparea.getSelectedItemPosition() == position) {
                clbg.setBackgroundResource(R.drawable.spselected);

                tv2.setTextColor(ContextCompat.getColor(EvacuationNormal2.this, R.color.md_white_1000));
                tv.setTextColor(ContextCompat.getColor(EvacuationNormal2.this, R.color.md_white_1000));
            } else {
                tv2.setTextColor(ContextCompat.getColor(EvacuationNormal2.this, R.color.md_black_1000));
                tv.setTextColor(ContextCompat.getColor(EvacuationNormal2.this, R.color.md_black_1000));
                clbg.setBackgroundResource(R.drawable.spunselected);
            }

            return convertView;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


            EvacuationResponsible evacuationResponsible = evacuationResponsibleModelList.get(position);
            convertView = LayoutInflater.from(EvacuationNormal2.this).inflate(
                    R.layout.inflate_evacationnormalyesno1, parent, false
            );

            TextView tv = convertView.findViewById(R.id.text1);


            if (evacuationResponsible.getPriority() != null
                    && evacuationResponsible.getAreaName() != null)
                tv.setText(evacuationResponsible.getPriority().toUpperCase() + " responsible  -  ".toUpperCase() +
                        evacuationResponsible.getAreaName());
            else {
                tv.setText(evacuationResponsible.getPriority() + " responsible  -  ".toUpperCase() +
                        evacuationResponsible.getAreaName());
            }


            return convertView;
        }
    }


    private void setSpiiner() {


        sparea.setAdapter(new SpinnerAdapter());
        sparea.setSelection(pos);
        sparea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                pos = position;
                areaName = evacuationResponsibleModelList.get(position).getAreaName();
                getEvacuationDetailsByEvacuationID(evacuationResponsibleModelList.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public class AdapterAdmin extends RecyclerView.Adapter<AdapterAdmin.VH> {


        @Override
        public AdapterAdmin.VH onCreateViewHolder(ViewGroup parent, int viewType) {


            if (viewType == 1)
                return new AdapterAdmin.VH(getLayoutInflater()
                        .inflate(R.layout.inflate_adapternormallistheader, parent, false));


            return new AdapterAdmin.VH(getLayoutInflater()
                    .inflate(R.layout.inflate_adapternormallist, parent, false));
        }

        @Override
        public int getItemViewType(int position) {

            if (position == 0) {
                return 1;
            } else

                return 2;
        }

        @Override
        public void onBindViewHolder(AdapterAdmin.VH holder, int position) {


            EvacuationResponsible evacuation = evacuationResponsibleList.get(position);


            switch (getItemViewType(position)) {

                case 1:
                    holder.tvarea.setVisibility(View.INVISIBLE);
                    holder.tvtype.setText(IpAddress.NEXT_YOU_STATUS);
                    holder.tvtype.setVisibility(View.VISIBLE);
                    holder.vline.setVisibility(View.VISIBLE);
                    break;
                default:
                    holder.tvtype.setText("");
                    holder.tvarea.setText("");
                    holder.tvarea.setVisibility(View.INVISIBLE);
                    holder.tvtype.setVisibility(View.INVISIBLE);
                    break;
            }

            if (getItemViewType(position) == 2) {

                if (evacuation.getSTATUS() == null
                        || evacuation.getSTATUS().trim().isEmpty()) {

                    holder.ivcount.setImageResource(0);
                    holder.ivcount_image.setImageResource(0);


                } else {
                    if (evacuation.getUserAvailability() != null &&
                            evacuation.getUserAvailability().equalsIgnoreCase(IpAddress.USERAVAILABILITY_TRUE)) {

                        holder.ivcount.setImageResource(R.drawable.circlegreen);
                        holder.ivcount_image.setImageResource(R.drawable.circlegreen);
                        holder.tvmobile.setText("(In the building)");

                        holder.tvmobile.setTextColor(ContextCompat.getColor(EvacuationNormal2.this,
                                R.color.color_green));

                    } else if (evacuation.getUserAvailability() != null &&
                            evacuation.getUserAvailability().equalsIgnoreCase(IpAddress.USERAVAILABILITY_FALSE)) {

                        holder.ivcount.setImageResource(R.drawable.circlered);
                        holder.ivcount_image.setImageResource(R.drawable.circlered);
                        holder.tvmobile.setText("(Not in the building)");

                        holder.tvmobile.setTextColor(ContextCompat.getColor(EvacuationNormal2.this,
                                R.color.textred));
                    } else {

                        holder.ivcount.setImageResource(R.drawable.circleyellow);
                        holder.ivcount_image.setImageResource(R.drawable.circleyellow);
                        holder.tvmobile.setText("(Unverified position)");

                        holder.tvmobile.setTextColor(ContextCompat.getColor(EvacuationNormal2.this,
                                R.color.color_yellow));
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
                        && evacuation.getAreaName() != null)
                    holder.tvarea.setText(evacuation.getFloor().trim() + "," + evacuation.getAreaName().trim());
                else
                    holder.tvarea.setText("");
                if (evacuation.getName() != null)
                    holder.tvname.setText(evacuation.getName());

                else
                    holder.tvname.setText("");


                if (evacuation.getPhotoUrl() != null)
                    holder.ivimage.setImageURI(evacuation.getPhotoUrl());
                else
                    holder.ivimage.setImageResource(R.mipmap.ic_profile);
                if (evacuation.getEmail() != null)
                    holder.tvemail.setText(evacuation.getEmail().trim());
                else
                    holder.tvemail.setText("");


                holder.tvstatus.setText(evacuation.getPriority().toUpperCase() + " responsible");
            } else {

                holder.tvtype.setText(IpAddress.NEXT_YOU_STATUS);
                holder.tvarea.setText("");
                holder.ivimage.setImageResource(R.mipmap.ic_profile);
                holder.ivcount_image.setImageResource(0);
                holder.tvname.setText("");
                holder.tvemail.setText("");
                holder.tvmobile.setText("");
                holder.tvstatus.setText("");
            }

        }

        @Override
        public int getItemCount() {
            return evacuationResponsibleList.size();
        }

        public class VH extends RecyclerView.ViewHolder {

            @BindView(R.id.tvname)
            TextView tvname;

            @BindView(R.id.tvmobile)
            TextView tvmobile;

            @BindView(R.id.tvtype)
            TextView tvtype;

            @BindView(R.id.tvemail)
            TextView tvemail;

            @BindView(R.id.tvstatus)
            TextView tvstatus;

            @BindView(R.id.ivimage)
            SimpleDraweeView ivimage;

            @BindView(R.id.ivcount_image)
            ImageView ivcount_image;


            @BindView(R.id.tvarea)
            TextView tvarea;

            @BindView(R.id.tvcount)
            TextView tvcount;
            @BindView(R.id.ivcount)
            ImageView ivcount;
            @BindView(R.id.ivmessage)
            ImageView ivmessage;

            @BindView(R.id.vline)
            View vline;


            public VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }


    private void capturePhoto() {


        AlertDialog.Builder alertDialogLayout = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.inflate_photo, null);
        AppCompatTextView tvtakephoto = view.findViewById(R.id.tvtakephoto);
        AppCompatTextView tvviewphoto = view.findViewById(R.id.tvviewphoto);
        AppCompatTextView tvchoosephoto = view.findViewById(R.id.tvchoosephoto);
        AppCompatTextView tvremovephoto = view.findViewById(R.id.tvremovephoto);

        tvremovephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                removePhoto();

            }
        });

        tvviewphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                viewPhoto();

            }
        });

        tvtakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cameraPermission();
            }
        });

        tvchoosephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent();
                    File mCurrentPhotoFile = new File(
                            getExternalCacheDir(), "vipadm");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0使用FileProvider
                        imageUri = FileProvider.getUriForFile(EvacuationNormal2.this,
                                "vip.com.vipmeetings.fileprovider", mCurrentPhotoFile);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } else {
                        imageUri = Uri.fromFile(mCurrentPhotoFile);
                    }
                    intent.setAction(android.content.Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUESt_GALLERY);
                } catch (Exception e) {
                    IpAddress.e("err", e.toString());
                }
            }
        });

        alertDialogLayout.setView(view);
        alertDialog = alertDialogLayout.show();

    }


    private void viewPhoto() {


        constraintSet = new ConstraintSet();
        constraintSet.clone(climage);
        TransitionManager.beginDelayedTransition(climage);

        ConstraintSet constraintSet2 = new ConstraintSet();
        //constraintSet2.clone(this,R.layout.imagelayoutnormal);

        if (isImageFull) {
            isImageFull = false;

            constraintSet.constrainWidth(R.id.ivimagecenter,
                    (int) getResources().getDimension(R.dimen.ivwidth100));
            constraintSet.constrainHeight(R.id.ivimagecenter,
                    (int) getResources().getDimension(R.dimen.ivwidth100));

            constraintSet.applyTo(climage);
        } else {

            tvclose.setVisibility(View.GONE);
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
            roundingParams.setRoundAsCircle(false);
            ivimagecenter.getHierarchy().setRoundingParams(roundingParams);
            isImageFull = true;
            ivcameraactual.setVisibility(View.GONE);

            ConstraintLayout.MarginLayoutParams marginLayoutParams =
                    (ConstraintLayout.MarginLayoutParams) climage.getLayoutParams();
            marginLayoutParams.topMargin = 0;

            climage.setLayoutParams(marginLayoutParams);

            ivcamera.setVisibility(View.GONE);
            constraintSet2.connect(R.id.ivimagecenter, ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet2.connect(R.id.ivimagecenter, ConstraintSet.END,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);


            constraintSet2.connect(R.id.ivimagecenter, ConstraintSet.RIGHT,
                    ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);


            constraintSet2.connect(R.id.ivimagecenter, ConstraintSet.LEFT,
                    ConstraintSet.PARENT_ID, ConstraintSet.LEFT);

            constraintSet2.connect(R.id.ivimagecenter, ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID, ConstraintSet.TOP);

            constraintSet2.connect(R.id.ivimagecenter, ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

            constraintSet2.constrainWidth(R.id.ivimagecenter, ConstraintSet.MATCH_CONSTRAINT);
            constraintSet2.constrainHeight(R.id.ivimagecenter, ConstraintSet.MATCH_CONSTRAINT);


            ivimagecenter.setImageURI(
                    evacuationResponsibleList.get(0).getPhotoUrl());
            constraintSet2.applyTo(climage);
        }

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }


    }

    private void cameraPermission() {


        if (ContextCompat.checkSelfPermission(EvacuationNormal2.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMEAR);

        } else {

            afterCamera();


        }
    }

    private void afterCamera() {

        try {
            File mCurrentPhotoFile = new File(
                    getExternalCacheDir(), "vipadm");

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                imageUri = FileProvider.getUriForFile(EvacuationNormal2.this,
                        "vip.com.vipmeetings.fileprovider", mCurrentPhotoFile);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                imageUri = Uri.fromFile(mCurrentPhotoFile);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_CAPTURE);
        } catch (Exception e) {
            IpAddress.e("err", e.toString());
        }
    }

    public byte[] getBytes(InputStream inputStream) {

        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        try {
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
        } catch (Exception e) {

        }
        return byteBuffer.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        isRefersh = false;
        if (resultCode == RESULT_OK)
            switch (requestCode) {


                case REQUEST_CAMEAR:
                    afterCamera();
                    break;

                case REQUESt_GALLERY:

                    InputStream iStream = null;
                    try {
                        showPD();
                        imageUri = data.getData();
                        iStream = getContentResolver().openInputStream(data.getData());
                        byte[] inputData = getBytes(iStream);
                        base64 = org.kobjects.base64.Base64.encode(inputData);
                        savePhoto();
                    } catch (FileNotFoundException e) {
                        hidePD();
                        IpAddress.e("err", e.toString());
                    }

                    break;

                case REQUEST_CAPTURE:

                    try {
                        showPD();
                        iStream = getContentResolver().openInputStream(imageUri);
                        byte[] inputData = getBytes(iStream);
                        base64 = org.kobjects.base64.Base64.encode(inputData);
                        savePhoto();

                    } catch (FileNotFoundException e) {
                        hidePD();
                        IpAddress.e("err", e.toString());
                    }

                    break;


                default:
                    break;
            }
    }

    private void savePhoto() {

        dismissDialogLayout();
        SoapSaveResponsiblePhotoEnvelope soapRemoveResponsiblePhotoEnvelope =
                new SoapSaveResponsiblePhotoEnvelope();

        SaveResponsiblePhotoBody soapRemoveRequestBody =
                new SaveResponsiblePhotoBody();

        SaveResponsiblePhoto removeResponsiblePhoto = new SaveResponsiblePhoto();
        removeResponsiblePhoto.setA_sBarcode(getBarcode());
        removeResponsiblePhoto.setA_sClientId(getClientID());
        removeResponsiblePhoto.setA_sPhotoCode(base64);
        removeResponsiblePhoto.setAuthToken(getAuthTokenPref_Evacuation());

        soapRemoveRequestBody.setSaveResponsiblePhoto(removeResponsiblePhoto);

        soapRemoveResponsiblePhotoEnvelope.setBody(soapRemoveRequestBody);

        Observable<SaveResponsiblePhotoResponseEnevelope> lClientDetailsObservable =
                service.saveResponsiblePhoto(soapRemoveResponsiblePhotoEnvelope);

        lClientDetailsObservable.
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSavePhotoSuccess, this::onRemoveError);

    }

    private void onSavePhotoSuccess(SaveResponsiblePhotoResponseEnevelope
                                            saveResponsiblePhotoResponseEnevelope) {

        dismissDialogLayout();

        isRefersh = true;
        clearFresco();
        //  ivimagecenter.setImageURI(imageUri);
        //   ivcameraactual.setVisibility(View.VISIBLE);
        ivcamera.setVisibility(View.GONE);
        // ivimage.setImageURI(imageUri);

//        ivimage.getHierarchy().setPlaceholderImage(0);
//        ivimagecenter.getHierarchy().setPlaceholderImage(0);
        ivimage.getHierarchy().setPlaceholderImage(android.R.color.transparent);
        ivimagecenter.getHierarchy().setPlaceholderImage(android.R.color.transparent);


        setImageURI();

        // hidePD();
        // refreshScreen();


    }

    private void setImageURI() {

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource
                (imageUri)
                .setResizeOptions(new ResizeOptions(
                        getResources().getDimensionPixelSize(R.dimen.ivwidth48),
                        getResources().getDimensionPixelSize(R.dimen.ivwidth48)))
                .build();

        ImageRequest request1 = ImageRequestBuilder.newBuilderWithSource
                (imageUri)
                .setResizeOptions(new ResizeOptions(
                        getResources().getDimensionPixelSize(R.dimen.ivwidth150),
                        getResources().getDimensionPixelSize(R.dimen.ivwidth150)))
                .build();

        ivimage.setController(
                Fresco.newDraweeControllerBuilder()
                        .setAutoPlayAnimations(false)
                        .setTapToRetryEnabled(true)
                        .setImageRequest(request)
                        .setControllerListener(new ControllerListener<ImageInfo>() {
                            @Override
                            public void onSubmit(String id, Object callerContext) {

                            }

                            @Override
                            public void onFinalImageSet(String id, @javax.annotation.Nullable ImageInfo imageInfo,
                                                        @javax.annotation.Nullable Animatable animatable) {


//                                        ivcameraactual.setVisibility(View.VISIBLE);
//                                        ivcamera.setVisibility(View.GONE);
                                hidePD();

                            }

                            @Override
                            public void onIntermediateImageSet(String id, @javax.annotation.Nullable ImageInfo imageInfo) {

                            }

                            @Override
                            public void onIntermediateImageFailed(String id, Throwable throwable) {

                            }

                            @Override
                            public void onFailure(String id, Throwable throwable) {


//                                        ivcameraactual.setVisibility(View.GONE);
//                                        ivcamera.setVisibility(View.VISIBLE);
                                hidePD();
                            }

                            @Override
                            public void onRelease(String id) {

                            }
                        })
                        .setRetainImageOnFailure(true)
                        .build());


        ivimagecenter.setController(
                Fresco.newDraweeControllerBuilder()
                        .setAutoPlayAnimations(false)
                        .setTapToRetryEnabled(true)
                        .setImageRequest(request1)
                        .setControllerListener(new ControllerListener<ImageInfo>() {
                            @Override
                            public void onSubmit(String id, Object callerContext) {

                            }

                            @Override
                            public void onFinalImageSet(String id, @javax.annotation.Nullable ImageInfo imageInfo,
                                                        @javax.annotation.Nullable Animatable animatable) {


//                                        ivcameraactual.setVisibility(View.VISIBLE);
//                                        ivcamera.setVisibility(View.GONE);

                                hidePD();

                            }

                            @Override
                            public void onIntermediateImageSet(String id, @javax.annotation.Nullable ImageInfo imageInfo) {

                            }

                            @Override
                            public void onIntermediateImageFailed(String id, Throwable throwable) {

                            }

                            @Override
                            public void onFailure(String id, Throwable throwable) {


//                                        ivcameraactual.setVisibility(View.GONE);
//                                        ivcamera.setVisibility(View.VISIBLE);

                                hidePD();
                            }

                            @Override
                            public void onRelease(String id) {

                            }
                        })
                        .build());

    }

    private void dismissDialogLayout() {

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    private void removePhoto() {


        showPD(this);
        SoapRemoveResponsiblePhotoEnvelope soapRemoveResponsiblePhotoEnvelope =
                new SoapRemoveResponsiblePhotoEnvelope();

        SoapRemoveRequestBody soapRemoveRequestBody =
                new SoapRemoveRequestBody();

        RemoveResponsiblePhoto removeResponsiblePhoto = new RemoveResponsiblePhoto();
        removeResponsiblePhoto.setA_sBarcode(getBarcode());
        removeResponsiblePhoto.setA_sClientId(getClientID());
        removeResponsiblePhoto.setAuthToken(getAuthTokenPref_Evacuation());

        soapRemoveRequestBody.setRemoveResponsiblePhoto(removeResponsiblePhoto);

        soapRemoveResponsiblePhotoEnvelope.setBody(soapRemoveRequestBody);

        Observable<SoapRemoveResponsiblePhotoResponseEnvelope> lClientDetailsObservable =
                service.removeResponsiblePhoto(soapRemoveResponsiblePhotoEnvelope);

        lClientDetailsObservable.
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onRemovePhoto, this::onRemoveError);
    }

    private void onRemoveError(Throwable throwable) {

        dismissDialogLayout();
        hidePD();
        isRefersh = true;
        //  refreshScreen();
    }

    private void onRemovePhoto(SoapRemoveResponsiblePhotoResponseEnvelope
                                       soapRemoveResponsiblePhotoResponseEnvelope) {

        clearFresco();
        dismissDialogLayout();
        hidePD();
        isRefersh = true;
        ivimagecenter.setImageResource(R.mipmap.ic_profile);
        ivcameraactual.setVisibility(View.GONE);
        // ivcamera.setVisibility(View.VISIBLE);
        ivimage.setImageResource(R.mipmap.ic_profile);
        imageUri = null;
        // refreshScreen();
    }

//    private void callMobile(String mobile) {
//        this.mobile = mobile;
//        if (ContextCompat.checkSelfPermission(EvacuationNormalList.this,
//                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(EvacuationNormalList.this,
//                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
//        } else {
//            call();
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {


            case REQUEST_CAMEAR:
                afterCamera();
                break;

            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    // call();
                } else {

                }
                return;
            }
        }
    }

//    private void call() {
//
//        try {
//            Intent intent = new Intent(Intent.ACTION_DIAL,
//                    Uri.parse("tel:" + mobile));
//            startActivity(intent);
//        } catch (SecurityException e) {
//            Crashlytics.logException(e.getCause());
//        }
//    }

}

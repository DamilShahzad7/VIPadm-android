package vip.com.vipmeetings.evacuate;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
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
import com.google.common.collect.Iterables;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vip.com.vipmeetings.R;
import vip.com.vipmeetings.body.ClearTimeRequestBody;
import vip.com.vipmeetings.body.ClearTimeResponseBody;
import vip.com.vipmeetings.body.EndEvacuationRequestBody;
import vip.com.vipmeetings.body.EndEvacuationResponseBody;
import vip.com.vipmeetings.body.GetEvacuationsRequestBody;
import vip.com.vipmeetings.body.GetEvacuationsResponseBody;
import vip.com.vipmeetings.body.GetResponsiblePhotoUrlRequestBody;
import vip.com.vipmeetings.body.GetUnReadMessageCountResponseBody;
import vip.com.vipmeetings.body.NotifyAboutEvacuationEnabledResponseBody;
import vip.com.vipmeetings.body.NotifyAboutEvacuationEndedResponseBody;
import vip.com.vipmeetings.body.SaveResponsiblePhotoBody;
import vip.com.vipmeetings.body.SendEvacuationPushRequestBody;
import vip.com.vipmeetings.body.SendEvacuationPushResponseBody;
import vip.com.vipmeetings.body.SoapNotifyAboutEvacuationEnabledRequestBody;
import vip.com.vipmeetings.body.SoapNotifyAboutEvacuationEndedBody;
import vip.com.vipmeetings.body.SoapRemoveRequestBody;
import vip.com.vipmeetings.body.StartEvacuationRequestBody;
import vip.com.vipmeetings.body.StartEvacuationResponseBody;
import vip.com.vipmeetings.body.UnreadMessageCountRequestBody;
import vip.com.vipmeetings.envelope.SaveResponsiblePhotoResponseEnevelope;
import vip.com.vipmeetings.envelope.SoapClearTimeResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapEndEvacuationRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapEndEvacuationResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetEvacuationsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetEvacuationsResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetResponsiblePhotoUrlRequest;
import vip.com.vipmeetings.envelope.SoapGetResponsiblePhotoUrlResponse;
import vip.com.vipmeetings.envelope.SoapGetUnreadMessageCountResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapNotifyAboutEvacuationEnabledRequest;
import vip.com.vipmeetings.envelope.SoapNotifyAboutEvacuationEnabledResponse;
import vip.com.vipmeetings.envelope.SoapNotifyAboutEvacuationEndedRequest;
import vip.com.vipmeetings.envelope.SoapNotifyAboutEvacuationEndedResponse;
import vip.com.vipmeetings.envelope.SoapRemoveResponsiblePhotoEnvelope;
import vip.com.vipmeetings.envelope.SoapRemoveResponsiblePhotoResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapSaveResponsiblePhotoEnvelope;
import vip.com.vipmeetings.envelope.SoapSendEvaciationPushRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapSendEvacuationPushResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapSetClearTimeRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapStartEvacuationRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapStartEvacuationResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapUnreadMessageCountRequestEnvelope;
import vip.com.vipmeetings.models.Evacuation;
import vip.com.vipmeetings.models.Evacuations;
import vip.com.vipmeetings.request.EndEvacuation;
import vip.com.vipmeetings.request.GetEvacuations;
import vip.com.vipmeetings.request.GetResponsiblePhotoUrl;
import vip.com.vipmeetings.request.GetUnReadMessageCount;
import vip.com.vipmeetings.request.NotifyAboutEvacuationEnabled;
import vip.com.vipmeetings.request.NotifyAboutEvacuationEnded;
import vip.com.vipmeetings.request.RemoveResponsiblePhoto;
import vip.com.vipmeetings.request.SaveResponsiblePhoto;
import vip.com.vipmeetings.request.SendEvacuationPush;
import vip.com.vipmeetings.request.SetClearTimeToEvacuations;
import vip.com.vipmeetings.request.StartEvacuation;
import vip.com.vipmeetings.utilities.Constants;
import vip.com.vipmeetings.view.MySpinner;

/**
 * Created by Srinath on 10/02/18.
 */

public class EvacuationListActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {


    @BindArray(R.array.sort_area_company)
    String[] sort_area_company;

    @BindView(R.id.rvevacuation)
    RecyclerView rvevacuation;
    private List<Evacuation> evacuationList, evacuationListOLD;
    @BindView(R.id.ivback)
    ImageView ivback;
    @BindView(R.id.tvstartend)
    TextView tvstartend;

    @BindView(R.id.tvtimer)
    TextView tvtimer;

    @BindView(R.id.rltoolbar)
    RelativeLayout rltoolbar;

    @BindView(R.id.tvstartendevacuation)
    TextView tvstartendevacuation;

    @BindView(R.id.ivcount)
    ImageView ivcount;

    @BindView(R.id.tvcount)
    TextView tvcount;
    @BindView(R.id.etsearch)
    EditText etsearch;


    @BindView(R.id.tvlocationname)
    TextView tvlocationname;


    @BindView(R.id.ivmessage)
    ImageView ivmessage;

    EvacuationAdapter evacuationAdapter;
    String update, clear;

    @BindView(R.id.spsort)
    MySpinner spsort;

    int pos = 0;
    SimpleDateFormat simpleDateFormat;
    Long different;
    Timer timer;

    boolean isRefresh;
    NumberFormat numberFormat;
    AlertDialog.Builder builder;

    AlertDialog alertDialog;
    private List<Evacuation> evacuationSearchList;

    boolean isAscending = true;

    @BindView(R.id.ivimagecenter)
    SimpleDraweeView ivimagecenter;

    @BindView(R.id.clnormal)
    ConstraintLayout clnormal;

    @BindView(R.id.ivimage)
    SimpleDraweeView ivimage;

    @BindView(R.id.climage)
    ConstraintLayout climage;

    @BindView(R.id.ivcamera)
    AppCompatImageView ivcamera;


    @BindView(R.id.ivcameraactual)
    AppCompatImageView ivcameraactual;

    @BindView(R.id.tvclose)
    AppCompatTextView tvclose;


    private Uri imageUri;
    private String base64 = "";

    boolean isImageFull;
    ConstraintSet constraintSet;
    String url;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evacuationlistnew);
        ButterKnife.bind(this);
        numberFormat = new DecimalFormat("00");
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        evacuationList = new ArrayList<>();
        evacuationListOLD = new ArrayList<>();
        evacuationAdapter = new EvacuationAdapter();
        rvevacuation.setAdapter(evacuationAdapter);
        climage.setVisibility(View.GONE);
        clnormal.setVisibility(View.VISIBLE);
        getImageRequest();
    }

    private void getImageRequest() {

        showPD();
        SoapGetResponsiblePhotoUrlRequest soapGetResponsiblePhotoUrlRequest =
                new SoapGetResponsiblePhotoUrlRequest();

        GetResponsiblePhotoUrlRequestBody getResponsiblePhotoUrlRequestBody = new GetResponsiblePhotoUrlRequestBody();

        GetResponsiblePhotoUrl getResponsiblePhotoUrl = new GetResponsiblePhotoUrl();
        getResponsiblePhotoUrl.setA_sClientId(getClientID());
        getResponsiblePhotoUrl.setA_sBarcode(getBarcode());
        getResponsiblePhotoUrl.setAuthToken(getAuthTokenPref_Evacuation());


        getResponsiblePhotoUrlRequestBody.setGetResponsiblePhotoUrl(getResponsiblePhotoUrl);


        soapGetResponsiblePhotoUrlRequest.setBody(getResponsiblePhotoUrlRequestBody);
        Observable<SoapGetResponsiblePhotoUrlResponse> lClientDetailsObservable =
                service.getResponsiblePhotoUrl(soapGetResponsiblePhotoUrlRequest);

        lClientDetailsObservable.
                subscribeOn(Schedulers.io())
                .map(this::onPhotoMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPhotoSuccess, this::onError);


    }


    @OnClick(R.id.tvclose)
    public void onClose(View v) {


        isImageFull = false;
        climage.setVisibility(View.GONE);
        clnormal.setVisibility(View.VISIBLE);
    }

    private void chageImage(String url) {


        ivimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
                roundingParams.setRoundAsCircle(true);
                ivimagecenter.getHierarchy().setRoundingParams(roundingParams);
                climage.setVisibility(View.VISIBLE);
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource
                        (Uri.parse(url))
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
                                // .setLowResImageRequest(request)
                                .build());

                clnormal.setVisibility(View.GONE);
            }
        });
    }


    private String onPhotoMap(SoapGetResponsiblePhotoUrlResponse soapGetResponsiblePhotoUrlResponse) {

        if (soapGetResponsiblePhotoUrlResponse.getBody() != null
                && soapGetResponsiblePhotoUrlResponse.getBody().getGetResponsiblePhotoUrlResponse() != null) {

            if (soapGetResponsiblePhotoUrlResponse.getBody().getGetResponsiblePhotoUrlResponse() != null) {

                if (soapGetResponsiblePhotoUrlResponse.getBody().getGetResponsiblePhotoUrlResponse().getEndEvacuationResult() != null)

                    return soapGetResponsiblePhotoUrlResponse.getBody().getGetResponsiblePhotoUrlResponse().getEndEvacuationResult();

                return "";
            }

        }

        return "";
    }

    private void onPhotoSuccess(String url) {

        this.url = url;
        if (url != null && !url.isEmpty()) {
            ivimage.setImageURI(url);
            ivimagecenter.setImageURI(url);

        } else {
            ivimage.setImageResource(R.mipmap.ic_profile);
            ivimagecenter.setImageResource(R.mipmap.ic_profile);

        }
        chageImage(url);


    }

    @OnClick({R.id.ivcameraactual, R.id.ivcamera, R.id.tvedit})
    public void onImage(View v) {

        capturePhoto();
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
                        imageUri = FileProvider.getUriForFile(EvacuationListActivity.this,
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

    private void cameraPermission() {


        if (ContextCompat.checkSelfPermission(EvacuationListActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMEAR);

        } else {

            afterCamera();


        }
    }

//    private void afterCamera() {
//
//        try {
//            File mCurrentPhotoFile = new File(
//                    getExternalCacheDir(), "vipadm");
//
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                imageUri = FileProvider.getUriForFile(EvacuationListActivity.this,
//                        "vip.com.vipmeetings.fileprovider", mCurrentPhotoFile);
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            } else {
//                imageUri = Uri.fromFile(mCurrentPhotoFile);
//            }
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//            startActivityForResult(intent, REQUEST_CAPTURE);
//        } catch (Exception e) {
//            IpAddress.e("err", e.toString());
//            Crashlytics.logException(e.getCause());
//        }
//    }

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


            ivimagecenter.setImageURI(url);
            constraintSet2.applyTo(climage);
        }

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }


    }

    private void removePhoto() {


        showPD();
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
    }

    private void onRemovePhoto(SoapRemoveResponsiblePhotoResponseEnvelope
                                       soapRemoveResponsiblePhotoResponseEnvelope) {

        clearFresco();
        dismissDialogLayout();

        ivimagecenter.setImageResource(R.mipmap.ic_profile);
        //  ivcameraactual.setVisibility(View.GONE);
        // ivcamera.setVisibility(View.VISIBLE);
        ivimage.setImageResource(R.mipmap.ic_profile);
        // refreshScreen();
        hidePD();
    }

    private void clearFresco() {

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearMemoryCaches();
        imagePipeline.clearDiskCaches();

    }

    private void dismissDialogLayout() {

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    private void afterCamera() {

        try {
            File mCurrentPhotoFile = new File(
                    getExternalCacheDir(), "vipadm");

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                imageUri = FileProvider.getUriForFile(EvacuationListActivity.this,
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        clearFresco();
        // ivimagecenter.setImageURI(imageUri);
        //  ivcameraactual.setVisibility(View.VISIBLE);
        ivcamera.setVisibility(View.GONE);
        // ivimage.setImageURI(imageUri);
        // refreshScreen();

        ivimage.getHierarchy().setPlaceholderImage(android.R.color.transparent);
        ivimagecenter.getHierarchy().setPlaceholderImage(android.R.color.transparent);


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
                            }

                            @Override
                            public void onRelease(String id) {

                            }
                        })
                        .setRetainImageOnFailure(true)
                        .setImageRequest(request)
                        .build());


        ivimagecenter.setController(
                Fresco.newDraweeControllerBuilder()
                        .setAutoPlayAnimations(false)
                        .setTapToRetryEnabled(true)
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
                        .setImageRequest(request1)
                        .build());


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvacuationFromPushService(Evacuation evacuation) {

        IpAddress.e(IpAddress.CALLED, "FROM PUSH SERVICE");
        hidePD();
       // if(evacuation!=null) {
            evacuationList.clear();
            evacuationListOLD.clear();
            evacuationList.addAll(evacuation.getEvacuationList());
            evacuationListOLD.addAll(evacuation.getEvacuationListOLD());
            onEvacuations(evacuation);
       // }
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

        etsearch.getText().clear();
        evacuationSearchList = null;
        hideSoft();
        sortByPos(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class Adapter extends ArrayAdapter<String> {


        public Adapter(@NonNull Context context) {
            super(context, R.layout.sp_area, R.id.text1, getResources().getStringArray(R.array.sort_area_company));
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View view = getLayoutInflater().inflate(R.layout.sp_area, parent, false);

            AppCompatTextView text1 = view.findViewById(R.id.text1);


            if (position == 0) {
                text1.setText("");
            } else if (position == spsort.getSelectedItemPosition()) {
                text1.setText("");
            } else

                text1.setText(sort_area_company[position]);


            return view;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.spiinerdropdown, parent, false);

            TextView text1 = view.findViewById(R.id.text1);
            text1.setText(sort_area_company[position]);
            if (spsort.getSelectedItemPosition() == position) {
                text1.setBackgroundResource(R.drawable.rectspselected);
            } else {

                text1.setBackgroundResource(R.drawable.rectsp);
            }

            return view;
        }
    }


    private void sortpos0() {

        showPD();
        Single.fromCallable(this::loadInBackground0)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateUi0, this::onError);
    }


    @Override
    public void onError(Throwable throwable) {
        hidePD();
        evacuationSearchList = null;
        IpAddress.e("err", throwable.toString());
    }

    private boolean loadInBackground0() {


        List<Evacuation> evacuationList = null;
        if (evacuationSearchList != null) {
            evacuationList = evacuationSearchList;
        } else {
            evacuationList = new ArrayList<>();
            evacuationList.addAll(evacuationListOLD);
        }


        if (evacuationList != null && evacuationList.size() > 0) {
            if (isAscending) {

                Collections.sort(evacuationList, (m1, m2) ->
                        m1.getCompany().compareTo(m2.getCompany()));
            } else {
                Collections.sort(evacuationList, (m1, m2) ->
                        m2.getCompany().compareTo(m1.getCompany()));
            }
        }

        isAscending = !isAscending;
        iterator(evacuationList);

        return true;
    }

    private void iterator(List<Evacuation> evacuationList) {

        List<Evacuation> userEvacuationsEmpty1 = new ArrayList<>();
        List<Evacuation> userEvacuationsTrue1 = new ArrayList<>();
        List<Evacuation> userEvacuationsFalse1 = new ArrayList<>();

        List<Evacuation> userEvacuationsEmpty2 = new ArrayList<>();
        List<Evacuation> userEvacuationsTrue2 = new ArrayList<>();
        List<Evacuation> userEvacuationsFalse2 = new ArrayList<>();
        if (evacuationList != null && evacuationList.size() > 0) {

            for (Evacuation evacuation : Iterables.filter(evacuationList, object ->
                    object != null && (object.getClearTime() == null ||
                            object.getClearTime().isEmpty()))) {

                if (evacuation.getUserAvailability() != null
                        && evacuation.getUserAvailability().equalsIgnoreCase(IpAddress.USERAVAILABILITY_FALSE)) {
                    userEvacuationsFalse1.add(evacuation);

                } else if (evacuation.getUserAvailability() == null
                        || evacuation.getUserAvailability().isEmpty()) {
                    userEvacuationsEmpty1.add(evacuation);
                } else if (evacuation.getUserAvailability() != null
                        && evacuation.getUserAvailability().equalsIgnoreCase(IpAddress.USERAVAILABILITY_TRUE)) {

                    userEvacuationsTrue1.add(evacuation);
                }


            }


            for (Evacuation evacuation : Iterables.filter(evacuationList, object ->
                    object != null && object.getClearTime() != null &&
                            !object.getClearTime().isEmpty())) {

                if (evacuation.getUserAvailability() != null
                        && evacuation.getUserAvailability().equalsIgnoreCase(IpAddress.USERAVAILABILITY_FALSE)) {
                    userEvacuationsFalse2.add(evacuation);

                } else if (evacuation.getUserAvailability() == null
                        || evacuation.getUserAvailability().isEmpty()) {
                    userEvacuationsEmpty2.add(evacuation);
                } else if (
                        evacuation.getUserAvailability() != null
                                && evacuation.getUserAvailability().equalsIgnoreCase(IpAddress.USERAVAILABILITY_TRUE)) {

                    userEvacuationsTrue2.add(evacuation);
                }

            }
        }



        if (!isFinishing()) {
            this.evacuationList.clear();
            if (userEvacuationsFalse1.size() > 0 || userEvacuationsEmpty1.size() > 0
                    || userEvacuationsTrue1.size() > 0) {

                for (Evacuation evacuation1 : userEvacuationsFalse2) {

                    evacuation1.setFloor("UNCLEARED AREAS");
                    this.evacuationList.add(evacuation1);
                }

                this.evacuationList.addAll(userEvacuationsFalse1);
                this.evacuationList.addAll(userEvacuationsEmpty1);
                this.evacuationList.addAll(userEvacuationsTrue1);
            }

            if (userEvacuationsFalse2.size() > 0 || userEvacuationsEmpty2.size() > 0
                    || userEvacuationsTrue2.size() > 0) {

                for (Evacuation evacuation1 : userEvacuationsFalse2) {

                    evacuation1.setFloor("CLEARED AREAS");
                    this.evacuationList.add(evacuation1);
                }
                this.evacuationList.addAll(userEvacuationsFalse2);
                this.evacuationList.addAll(userEvacuationsEmpty2);
                this.evacuationList.addAll(userEvacuationsTrue2);
            }
        }
    }

    private boolean loadInBackground1() {
        List<Evacuation> evacuationList;
        if (evacuationSearchList != null) {
            evacuationList = evacuationSearchList;
        } else {
            evacuationList = new ArrayList<>();
            evacuationList.addAll(evacuationListOLD);
        }

        if (isAscending) {
            Collections.sort(evacuationList, (m1, m2) ->
                    m1.getAreaName().compareTo(m2.getAreaName()));
        } else {
            Collections.sort(evacuationList, (m1, m2) ->
                    m2.getAreaName().compareTo(m1.getAreaName()));
        }

        isAscending = !isAscending;


        iterator(evacuationList);
        return true;
    }

    private void updateUi0(boolean flag) {


        hidePD();
        if (!isFinishing() && rvevacuation != null && evacuationAdapter != null) {
            evacuationAdapter.notifyDataSetChanged();
        }
        evacuationSearchList = null;

    }

    private void updateUi1(boolean flag) {


        hidePD();
        if (!isFinishing() && rvevacuation != null && evacuationAdapter != null) {
            evacuationAdapter.notifyDataSetChanged();
        }
        evacuationSearchList = null;
    }


    private void sortpos1() {

        showPD();
        Single.fromCallable(this::loadInBackground1)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateUi1, this::onError);
    }


    private void sortByPos(int pos) {

        this.pos = pos;
        if (pos == 0) {
            sortpos0();
        } else {
            sortpos1();
        }


    }

    private void sortByPos(int pos, List<Evacuation> evacuationList) {

        this.evacuationSearchList = evacuationList;
        this.pos = pos;
        if (pos == 0) {
            sortpos0();
        } else {
            sortpos1();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void refreshScreen() {

        hidePD();
        deleteStiky(this);
        getEvacuations();

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEva(String update) {

        if (!isFinishing()) {

            initializeEva();
            if (pushService != null) {
                evacuationAdapter.notifyDataSetChanged();
                deleteStiky(this);

                if(evacuationResponsible!=null
                && evacuationResponsible.getA_sPlaceId()!=null)
                pushService.refreshEvacuationList(evacuationResponsible.getA_sPlaceId());

            } else {
                refreshScreen();
            }
        }


    }

    private void getEvacuations() {

        if (hasNetwork()) {
            showPD();
            if (timer != null) {
                timer.cancel();
                timer = null;
                tvtimer.setText("");
            }
            getEvacuationDetails();
        } else {
            hidePD();
            showToast(getString(R.string.nointernet));
        }
    }

    private void initializeEva() {


        tvcount.setText("");
        ivcount.setImageResource(0);
        if (getStoreCitySelect() != null && getStoreCitySelect().getCityName() != null)
            tvlocationname.setText(getStoreCitySelect().getCityName() + "");

        if (evacuationResponsible != null
                && evacuationResponsible.getUserType() != null
                && (evacuationResponsible.getUserType().trim().equalsIgnoreCase("A"))) {

            tvstartendevacuation.setVisibility(View.VISIBLE);
            rltoolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.red));

            colorStatusBar(true, getWindow(), this);
            colorNavigationBarBottom(true, getWindow(), this);


        } else {

            rltoolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.bg));
            tvstartendevacuation.setVisibility(View.GONE);
            tvcount.setVisibility(View.GONE);
            ivmessage.setVisibility(View.GONE);
            tvstartend.setVisibility(View.GONE);

            colorStatusBar(false, getWindow(), this,
                    ContextCompat.getColor(this, R.color.bg));
            colorNavigationBarBottom(false, getWindow(), this);
        }

        etsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (charSequence.length() > 0) {
                    searchList(charSequence.toString().trim());

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


                if (editable.length() == 0) {

                    try {
                        sortByPos(pos);
                        hideKeyboard(etsearch);
                    } catch (Exception e) {

                    }
                }
            }
        });

        etsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    searchList(etsearch.getText().toString().trim());
                }

                return false;
            }
        });


        Adapter arrayAdapter = new Adapter(this);
        spsort.setAdapter(arrayAdapter);
        spsort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                etsearch.getText().clear();
                evacuationSearchList = null;
                hideSoft();
                sortByPos(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void searchList(String trim) {

        try {

            if (evacuationAdapter != null
                    && trim != null && !trim.isEmpty()) {

                evacuationAdapter.getFilter().filter(trim.trim());
            } else {

                sortByPos(pos);

            }
        } catch (Exception e) {

        }
    }


    @OnClick(R.id.root)
    public void onContainer(View v) {

        hideKeyboard(etsearch);
    }


    @OnClick(R.id.tvstartendevacuation)
    public void onStartEndEvacuation(View v) {


        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        if (tvstartendevacuation.getText().toString().trim().equalsIgnoreCase(IpAddress.EVACUATION_START)) {

            builder.setMessage(IpAddress.EVACUATION_START_MESSAGE);
            builder.setPositiveButton(IpAddress.YES, (dialogInterface, i) -> StartEndEvacuation(true));
        } else {
            builder.setMessage(IpAddress.EVACUATION_END_MESSAGE);
            builder.setPositiveButton(IpAddress.YES, (dialogInterface, i) -> StartEndEvacuation(false));
        }


        builder.setNegativeButton(IpAddress.CANCEL, (dialogInterface, i) -> {

            dialogInterface.dismiss();
        });

        alertDialog = builder.show();

    }

    private void StartEndEvacuation(boolean isStart) {


        if (tvstartendevacuation.getText().toString().trim().equalsIgnoreCase(IpAddress.EVACUATION_START)) {

            isRefresh = true;
            showPD();
            SoapStartEvacuationRequestEnvelope soapStartEvacuationRequestEnvelope =
                    new SoapStartEvacuationRequestEnvelope();

            StartEvacuationRequestBody startEvacuationRequestBody = new StartEvacuationRequestBody();

            StartEvacuation startEvacuation = new StartEvacuation();
            startEvacuation.setA_sClientId(getClientID());
            startEvacuation.setA_sPlaceId(mSharedPreferences.getString(IpAddress.PLACEID_NEW, ""));
            startEvacuation.setAuthToken(getAuthTokenPref_Evacuation());
            startEvacuationRequestBody.setStartEvacuation(startEvacuation);
            soapStartEvacuationRequestEnvelope.setBody(startEvacuationRequestBody);


            Observable<SoapStartEvacuationResponseEnvelope> lClientDetailsObservable =
                    service.startEvacuation(soapStartEvacuationRequestEnvelope);
            lClientDetailsObservable.
                    subscribeOn(Schedulers.io())
                    .map(this::onSoapStart)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onStartEndEvacuation, this::onError);


        } else {

            isRefresh = false;
            showPD();
            SoapEndEvacuationRequestEnvelope soapEndEvacuationRequestEnvelope = new SoapEndEvacuationRequestEnvelope();
            EndEvacuationRequestBody endEvacuationRequestBody = new EndEvacuationRequestBody();

            EndEvacuation endEvacuation = new EndEvacuation();
            endEvacuation.setA_sClientId(getClientID());
            endEvacuation.setAuthToken(getAuthTokenPref_Evacuation());
            endEvacuation.setA_sPlaceId(mSharedPreferences.getString(IpAddress.PLACEID_NEW, ""));
            endEvacuationRequestBody.setEndEvacuation(endEvacuation);
            soapEndEvacuationRequestEnvelope.setBody(endEvacuationRequestBody);


            Observable<SoapEndEvacuationResponseEnvelope> lClientDetailsObservable =
                    service.endEvacuation(soapEndEvacuationRequestEnvelope);
            lClientDetailsObservable.
                    subscribeOn(Schedulers.io())
                    .map(this::onSoapEnd)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onStartEndEvacuation, this::onError);
        }
    }


    private String onSoapEnd(SoapEndEvacuationResponseEnvelope soapEndEvacuationResponseEnvelope) {

        EndEvacuationResponseBody endEvacuationResponseBody = soapEndEvacuationResponseEnvelope.getBody();

        return endEvacuationResponseBody.getEndEvacuationResponse().getEndEvacuationResult();
    }

    private String onSoapEnd(SoapStartEvacuationResponseEnvelope soapStartEvacuationResponseEnvelope) {

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        StartEvacuationResponseBody startEvacuationResponseBody =
                soapStartEvacuationResponseEnvelope.getBody();
        return startEvacuationResponseBody.getStartEvacuationResponse().getStartEvacuationResult();
    }

    private String onSoapStart(SoapStartEvacuationResponseEnvelope soapStartEvacuationResponseEnvelope) {

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        StartEvacuationResponseBody startEvacuationResponseBody =
                soapStartEvacuationResponseEnvelope.getBody();
        return startEvacuationResponseBody.getStartEvacuationResponse().getStartEvacuationResult();
    }

    private String onSoapStart(SoapNotifyAboutEvacuationEnabledResponse soapStartEvacuationResponseEnvelope) {

        try {
            NotifyAboutEvacuationEnabledResponseBody startEvacuationResponseBody =
                    soapStartEvacuationResponseEnvelope.getBody();
            return startEvacuationResponseBody.getNotifyAboutEvacuationEnabledResponse()
                    .getNotifyAboutEvacuationEnabledResult().getStatus().get_status();
        } catch (Exception e) {
            return "";
        }
    }


    private void onStartEndEvacuation(String status) {

        if (status != null && status.equalsIgnoreCase(IpAddress.SUCCESS)) {

            if (tvstartendevacuation.getText().toString().trim().equalsIgnoreCase(IpAddress.EVACUATION_START)) {


                //  startAdminEvacuationStart();

                try {

                    pushService.startAdminEvacuationStart();
                } catch (Exception e) {

                }
                onPushSuccess(IpAddress.SUCCESS);
            } else {

                // endEvacauationStart();
                try {
                    pushService.endEvacuationStart();
                } catch (Exception e) {

                }
                onPushSuccess(IpAddress.SUCCESS);
            }


        } else {
            showToast(getString(R.string.someerror));
        }
    }

    private void endEvacauationStart() {

        SoapNotifyAboutEvacuationEndedRequest soapNotifyAboutEvacuationEndedRequest =
                new SoapNotifyAboutEvacuationEndedRequest();

        SoapNotifyAboutEvacuationEndedBody soapNotifyAboutEvacuationEndedBody =
                new SoapNotifyAboutEvacuationEndedBody();


        NotifyAboutEvacuationEnded notifyAboutEvacuationEnded = new NotifyAboutEvacuationEnded();


        notifyAboutEvacuationEnded.setA_iPlaceId(mSharedPreferences
                .getString(IpAddress.PLACEID_NEW, ""));
        notifyAboutEvacuationEnded.setAuthToken(getAuthTokenPref_Evacuation());

        soapNotifyAboutEvacuationEndedBody.setNotifyAboutEvacuationEnded(notifyAboutEvacuationEnded);

        soapNotifyAboutEvacuationEndedRequest.setBody(soapNotifyAboutEvacuationEndedBody);


        Observable<SoapNotifyAboutEvacuationEndedResponse> lClientDetailsObservable =
                service.notifyAboutEvacuationEnded(soapNotifyAboutEvacuationEndedRequest);
        lClientDetailsObservable.
                subscribeOn(Schedulers.io())
                .map(this::onSoapStart)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPushSuccess, this::onError);
    }

    private void startAdminEvacuationStart() {

        SoapNotifyAboutEvacuationEnabledRequest soapNotifyAboutEvacuationEnabledRequest =
                new SoapNotifyAboutEvacuationEnabledRequest();

        SoapNotifyAboutEvacuationEnabledRequestBody startEvacuationRequestBody =
                new SoapNotifyAboutEvacuationEnabledRequestBody();

        NotifyAboutEvacuationEnabled notifyAboutEvacuationEnabled = new NotifyAboutEvacuationEnabled();
        notifyAboutEvacuationEnabled.setA_iPlaceId(mSharedPreferences
                .getString(IpAddress.PLACEID_NEW, ""));
        notifyAboutEvacuationEnabled.setAuthToken(getAuthTokenPref_Evacuation());
        startEvacuationRequestBody.setNotifyAboutEvacuationEnabled(notifyAboutEvacuationEnabled);


        soapNotifyAboutEvacuationEnabledRequest.setBody(startEvacuationRequestBody);

        Observable<SoapNotifyAboutEvacuationEnabledResponse> lClientDetailsObservable =
                service.notifyAboutEvacuationEnabled(soapNotifyAboutEvacuationEnabledRequest);
        lClientDetailsObservable.
                subscribeOn(Schedulers.io())
                .map(this::onSoapStart)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPushSuccess, this::onError);
    }

    private String onSoapStart(SoapNotifyAboutEvacuationEndedResponse soapNotifyAboutEvacuationEndedResponse) {

        try {
            NotifyAboutEvacuationEndedResponseBody notifyAboutEvacuationEndedResponseBody =
                    soapNotifyAboutEvacuationEndedResponse.getBody();
            return notifyAboutEvacuationEndedResponseBody.getNotifyAboutEvacuationEndedResponse()
                    .getNotifyAboutEvacuationEndedResult().get_string();
        } catch (Exception e) {
            return "";
        }
    }


    public void onPushError(Throwable throwable) {

        if (timer != null) {
            timer.cancel();
            timer = null;
            tvtimer.setText("");
        }
        hidePD();
        getEvacuationDetails();

    }

    private String onSoapEvacuationPush(SoapSendEvacuationPushResponseEnvelope soapSendEvacuationPushResponseEnvelope) {


        SendEvacuationPushResponseBody sendEvacuationPushResponseBody =
                soapSendEvacuationPushResponseEnvelope.getBody();

        return sendEvacuationPushResponseBody.getSendEvacuationPushResponse().getSendEvacuationPush();

    }


    private void onPushSuccess(String status) {


        if (timer != null) {
            timer.cancel();
            timer = null;
            tvtimer.setText("");
        }
        getEvacuationDetails();
        hidePD();
    }


    @Override
    public void onBackPressed() {
        hideKeyboard(etsearch);

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

        } else if (isRefresh) {
            Intent intent = getIntent();
            intent.putExtra(IpAddress.REFRESHTIMERBAR, isRefresh);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            setResult(RESULT_OK);
            finish();
        }

    }

    @OnClick(R.id.ivback)
    public void onBack(View v) {
        onBackPressed();
    }


    @OnClick(R.id.ivmessage)
    public void onMessages(View v) {


        IpAddress.e("iv","called1");
        if (evacuationList != null
                && evacuationList.size() > 0) {
            IpAddress.e("iv","called2");
            showPD();
            Single.fromCallable(this::loadInBackgroundEvacuation)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::updateUiData, this::Error);


        }else {
            IpAddress.e("iv","called3");
            hidePD();
        }


    }


    public void Error(Throwable throwable) {



        IpAddress.e("onerror",throwable.toString());
        showMessage(throwable.toString());
        hidePD();
    }

    public void updateUiData(String str) {

        hidePD();
        hideSoft();
        Intent intent = new Intent(EvacuationListActivity.this, MessageSingleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (str != null && str.trim().length()>0) {
            intent.putExtra(IpAddress.AREALIST, str);
        }
        startActivityForResult(intent, IpAddress.REQUESTMESSAGE);
        etsearch.getText().clear();
        evacuationSearchList = null;

        IpAddress.e("onerror","success");
    }


    public String loadInBackgroundEvacuation() {


        HashMap<String, Evacuation> stringStringHashMap = new HashMap<>();
        String str = "";

        List<Evacuation> evacuationList1=new ArrayList<>();
        evacuationList1.addAll(evacuationList);

        if (evacuationList1 != null && evacuationList1.size() > 0) {
            for (Evacuation evacuation : evacuationList1) {

                if (evacuation != null && evacuation.getAreaName() != null &&
                        evacuation.getAreaName().trim().length()>0)

                    stringStringHashMap.put(evacuation.getAreaName(), evacuation);
            }

            str = mGson.toJson(stringStringHashMap);

        }
        return str;

    }


    private void getEvacuationDetails() {

        if (evacuationResponsible != null) {
            String barcode = null;
            if (evacuationResponsible.getUserType() != null)
                if (evacuationResponsible.getUserType().equalsIgnoreCase(IpAddress.NORMALUSER)) {

                    barcode = getBarcode();
                } else {

                    barcode = "";
                }

            if (barcode != null) {

                evacuationList.clear();
                evacuationListOLD.clear();
                evacuationAdapter.notifyDataSetChanged();
                SoapGetEvacuationsRequestEnvelope soapGetEvacuationsRequestEnvelope = new
                        SoapGetEvacuationsRequestEnvelope();

                GetEvacuationsRequestBody getEvacuationsRequestBody = new GetEvacuationsRequestBody();

                GetEvacuations getEvacuations = new GetEvacuations();
                getEvacuations.setA_sBarcode(barcode);

                getEvacuations.setA_sClientId(getClientID());
                getEvacuations.setA_sPlaceId(evacuationResponsible.getA_sPlaceId());
                getEvacuations.setAuthToken(getAuthTokenPref_Evacuation());
                getEvacuationsRequestBody.setGetEvacuations(getEvacuations);
                soapGetEvacuationsRequestEnvelope.setBody(getEvacuationsRequestBody);

                Observable<SoapGetEvacuationsResponseEnvelope> lClientDetailsObservable =
                        service.getEvacuations(soapGetEvacuationsRequestEnvelope);
                lClientDetailsObservable.
                        subscribeOn(Schedulers.io())
                        .map(this::onSoapMap)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onEvacuations, this::onError);
            }


        } else {
            hidePD();
        }
    }

    private Evacuation onSoapMap(SoapGetEvacuationsResponseEnvelope soapGetEvacuationsResponseEnvelope) {

        IpAddress.e(IpAddress.CALLED, "FROM EVACUATION");
        GetEvacuationsResponseBody getEvacuationsResponseBody = soapGetEvacuationsResponseEnvelope.getBody();
        Evacuations evacuations = getEvacuationsResponseBody.getGetEvacuationsResponse()
                .getGetEvacuationsResult().getEvacuations();

        Evacuation evacuation1 = new Evacuation();
        evacuation1 = setEvacuationForOLDandnew(evacuations);
        return evacuation1;

    }

    private Evacuation setEvacuationForOLDandnew(Evacuations evacuations) {


        if (evacuations != null && evacuations.getEvacuation() != null) {
            if (evacuationResponsible.getUserType() != null) {

                evacuationList.addAll(evacuations.getEvacuation());
                evacuationListOLD.addAll(evacuationList);
                return evacuationList.get(0);
            }

        }
        return null;

    }


    private void showTimer(String startTime) {

        try {

            Date date1 = simpleDateFormat.parse(startTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date1);
            Date date2 = Constants.formatGMT2(new Date());
            different = date2.getTime() - date1.getTime();
            runTimer(different);

        } catch (Exception e) {
            IpAddress.e("TIME", e.toString());
            tvtimer.setText("");
        }

    }


    private void runTimer(final long diffMs) {


        try {

            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            timer = new Timer(Long.MAX_VALUE, 1000);
            timer.start();
        } catch (Exception e) {

            IpAddress.e("err", e.toString());
        }


    }

    long secondsInMilli = 1000;
    long minutesInMilli = secondsInMilli * 60;
    long hoursInMilli = minutesInMilli * 60;
    long daysInMilli = hoursInMilli * 24;
    String time = "";

    public String CalculateTime(Long aLong) {


        long diff = aLong;


        time = "";

        long elapsedDays = diff / daysInMilli;
        diff = diff % daysInMilli;

        long elapsedHours = diff / hoursInMilli;
        diff = diff % hoursInMilli;

        long elapsedMinutes = diff / minutesInMilli;
        diff = diff % minutesInMilli;

        long elapsedSeconds = diff / secondsInMilli;

        if (elapsedDays > 0) {
            time =
                    numberFormat.format(elapsedDays) + ":" +
                            numberFormat.format(elapsedHours) + ":" +
                            numberFormat.format(elapsedMinutes) + ":" +
                            numberFormat.format(elapsedSeconds);
        } else if (elapsedHours > 0) {
            time =
                    numberFormat.format(elapsedHours) + ":" +
                            numberFormat.format(elapsedMinutes) + ":" +
                            numberFormat.format(elapsedSeconds);
        } else if (elapsedMinutes > 0) {
            time = "00:" +
                    numberFormat.format(elapsedMinutes) + ":" +
                    numberFormat.format(elapsedSeconds);

        } else if (elapsedSeconds > 0) {
            time = "00:00:" +
                    numberFormat.format(elapsedSeconds) + "";
        }

        // IpAddress.e("TIME", time);

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

            tvtimer.setText(CalculateTime(different));


        }

        @Override
        public void onFinish() {

        }
    }

    public void enableEvacation() {

        tvstartendevacuation.setBackgroundResource(R.drawable.gradient_tv_nocorenr);
        tvstartendevacuation.setTextColor(ContextCompat.getColor(this, R.color.md_grey_700));
        rltoolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_red));
        tvstartend.setTextColor(ContextCompat.getColor(EvacuationListActivity.this, R.color.md_white_1000));
        colorStatusBar(false, getWindow(), this);

        colorNavigationBarBottom(false, getWindow(), this);
    }

    public void disableEvacuation() {

        tvstartendevacuation.setBackgroundColor(ContextCompat.getColor(this, R.color.color_red));
        tvstartendevacuation.setTextColor(ContextCompat.getColor(this, R.color.md_white_1000));
        rltoolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        colorStatusBar(false, getWindow(), this,
                ContextCompat.getColor(this, R.color.bg));
        colorNavigationBarBottom(true, getWindow(), this);
        tvstartend.setTextColor(ContextCompat.getColor(EvacuationListActivity.this, R.color.color_useravailability_empty));
    }


    private void onEvacuations(Evacuation evacuation1) {


        if (evacuationList != null) {


            if (evacuation1.getStatus() == null || evacuation1.getStatus().trim().isEmpty()) {

                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }

                tvstartendevacuation.setText(IpAddress.EVACUATION_START);
                tvstartend.setText(IpAddress.EVACUATION_DISABLE);
                disableEvacuation();
            } else if (evacuation1.getStatus().equalsIgnoreCase(IpAddress.STATUS_STARTED)) {

                tvstartendevacuation.setText(IpAddress.EVACUATION_END);
                tvstartend.setText(IpAddress.EVACUATION_ENABLE);
                enableEvacation();

            } else {
                tvstartendevacuation.setText(IpAddress.EVACUATION_START);
                tvstartend.setText(IpAddress.EVACUATION_DISABLE);
                disableEvacuation();
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }

            }

            if (evacuation1 != null
                    && evacuation1.getStatus() != null)
                if (evacuation1.getStatus().equalsIgnoreCase(IpAddress.STATUS_Ended)
                        && evacuation1.getEndTime() != null
                        && evacuation1.getStartTime() != null) {

                    DateTimeFormatter outputFormatter
                            = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                    DateTime dt1 = new DateTime(outputFormatter.parseDateTime(evacuation1.getStartTime()));
                    DateTime dt2 = new DateTime(outputFormatter.parseDateTime(evacuation1.getEndTime()));

                    //  Duration duration1 = new Interval(dt1, dt2).toDurationMillis();

                    long diff = new Interval(dt1, dt2).toPeriod(PeriodType.millis()).getMillis();
                    long elapsedDays = diff / daysInMilli;
                    diff = diff % daysInMilli;

                    long elapsedHours = diff / hoursInMilli;
                    diff = diff % hoursInMilli;

                    long elapsedMinutes = diff / minutesInMilli;
                    diff = diff % minutesInMilli;

                    long elapsedSeconds = diff / secondsInMilli;

                    String time = "";
                    if (elapsedDays > 0) {
                        time =
                                numberFormat.format(elapsedDays) + ":" +
                                        numberFormat.format(elapsedHours) + ":" +
                                        numberFormat.format(elapsedMinutes) + ":" +
                                        numberFormat.format(elapsedSeconds);
                    } else if (elapsedHours > 0) {
                        time =
                                numberFormat.format(elapsedHours) + ":" +
                                        numberFormat.format(elapsedMinutes) + ":" +
                                        numberFormat.format(elapsedSeconds);
                    } else if (elapsedMinutes > 0) {
                        time = "00:" +
                                numberFormat.format(elapsedMinutes) + ":" +
                                numberFormat.format(elapsedSeconds);

                    } else if (elapsedSeconds > 0) {
                        time = "00:00:" +
                                numberFormat.format(elapsedSeconds) + "";
                    }
                    tvtimer.setText(time);

                } else if (evacuation1.getStatus().equalsIgnoreCase(IpAddress.STATUS_STARTED)) {
                    showTimer(evacuation1.getStartTime());
                } else {


                }

            sortByPos(pos);

            // updateUi0(true);


        }
        hidePD();
        if (evacuationResponsible != null)
            if (evacuationResponsible.getUserType()
                    .equalsIgnoreCase(IpAddress.NORMALUSER)) {
            } else {

                getUnreadMessages();
            }

    }

    private void getUnreadMessages() {


        showPD();
        String barcode = null;
        String appName = null;
        String evaId = null;

        Observable<SoapGetUnreadMessageCountResponseEnvelope> lClientDetailsObservable;
        if (evacuationResponsible.getUserType() != null
                && evacuationResponsible.getUserType().equalsIgnoreCase(IpAddress.NORMALUSER)) {
            barcode = "";
            appName = IpAddress.VIPadm;
            evaId = evacuationResponsible.getEvacuationId();
        } else {

            barcode = getBarcode();
            appName = IpAddress.VIPRECEPTION;
            evaId = "";
        }
        if (barcode != null && evaId != null && appName != null) {

            SoapUnreadMessageCountRequestEnvelope soapUnreadMessageCountRequestEnvelope =
                    new SoapUnreadMessageCountRequestEnvelope();


            UnreadMessageCountRequestBody unreadMessageCountRequestBody = new UnreadMessageCountRequestBody();

            GetUnReadMessageCount getUnReadMessageCount = new GetUnReadMessageCount();

            getUnReadMessageCount.setA_sAppName(appName);
            getUnReadMessageCount.setA_sBarcode(barcode);
            getUnReadMessageCount.setA_sEvacuationId(evaId);

            getUnReadMessageCount.setA_sClientId(getClientID());
            //1253
            // getUnReadMessageCount.setA_sPlaceId(evacuationResponsible.getA_sPlaceId());

            getUnReadMessageCount.setA_sPlaceId(mSharedPreferences.getString(IpAddress.PLACEID_NEW,
                    evacuationResponsible.getA_sPlaceId()));

            if (getUnReadMessageCount.getA_sPlaceId() == null
                    || getUnReadMessageCount.getA_sPlaceId().trim().length() == 0) {
                getUnReadMessageCount.setA_sPlaceId(evacuationResponsible.getA_sPlaceId());
            }
            getUnReadMessageCount.setAuthToken(getAuthTokenPref_Evacuation());
            unreadMessageCountRequestBody.setGetUnReadMessageCount(getUnReadMessageCount);
            soapUnreadMessageCountRequestEnvelope.setBody(unreadMessageCountRequestBody);

            lClientDetailsObservable =
                    service.getResposibleUnReadEvacuationMessagesCount(soapUnreadMessageCountRequestEnvelope);
            lClientDetailsObservable.
                    subscribeOn(Schedulers.io())
                    .map(this::onSoapCount)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onCount, this::onError);
        }

    }

    private String onSoapCount(SoapGetUnreadMessageCountResponseEnvelope soapGetUnreadMessageCountResponseEnvelope) {

        GetUnReadMessageCountResponseBody getUnReadMessageCountResponseBody =
                soapGetUnreadMessageCountResponseEnvelope.getBody();

        return getUnReadMessageCountResponseBody.getGetUnReadMessageCountResponse().getUnreadResult();
    }


    private void onCount(String status) {
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
        hidePD();
    }

    private void setclearTime(String clientid, String update, String clear) {


        this.update = update;
        this.clear = clear;
        showPD();

        SoapSetClearTimeRequestEnvelope soapSetClearTimeRequestEnvelope =
                new SoapSetClearTimeRequestEnvelope();


        ClearTimeRequestBody clearTimeRequestBody = new ClearTimeRequestBody();

        SetClearTimeToEvacuations setClearTimeToEvacuations = new SetClearTimeToEvacuations();
        setClearTimeToEvacuations.setA_sClientId(clientid);
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


        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        ClearTimeResponseBody clearTimeResponseBody = soapClearTimeResponseEnvelope.getBody();
        return clearTimeResponseBody.getSetClearTimeToEvacuationsResponse().getSetClearTime();
    }

    private void onSetClear(String messages) {


        if (messages != null && messages.equalsIgnoreCase("Success")) {
            Observable<SoapSendEvacuationPushResponseEnvelope> lClientDetailsObservable = null;

            String string_pushMessage = null;

            if (this.update != null && this.update.length() > 0) {

                string_pushMessage = IpAddress.EVACUATION_CLEAR2;

            } else {

                string_pushMessage = IpAddress.EVACUATION_CLEAR1;

            }

            update = null;
            clear = null;


            SoapSendEvaciationPushRequestEnvelope
                    soapSendEvaciationPushRequestEnvelope = new SoapSendEvaciationPushRequestEnvelope();

            SendEvacuationPushRequestBody sendEvacuationPushRequestBody = new SendEvacuationPushRequestBody();

            SendEvacuationPush sendEvacuationPush = new SendEvacuationPush();
            sendEvacuationPush.setA_sClientId(getClientID());
            sendEvacuationPush.setA_sEvacuationIds("");
            sendEvacuationPush.setAuthToken(getAuthTokenPref_Evacuation());
            sendEvacuationPush.setA_sPlaceId(evacuationResponsible.getA_sPlaceId());
            sendEvacuationPush.setA_sPushMessage(string_pushMessage);
            sendEvacuationPushRequestBody.setSendEvacuationPush(sendEvacuationPush);

            soapSendEvaciationPushRequestEnvelope.setBody(sendEvacuationPushRequestBody);

            lClientDetailsObservable =
                    service.sendEvacuationPush(soapSendEvaciationPushRequestEnvelope);


            lClientDetailsObservable.
                    subscribeOn(Schedulers.io())
                    .map(this::onSoapEvacuationPush)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onPushSuccess, this::onError);

        } else {
            showToast(messages + "");
        }

    }


    public class EvacuationAdapter extends RecyclerView.Adapter<EvacuationAdapter.VH>
            implements Filterable {


        List<Evacuation> filteredList;

        public EvacuationAdapter() {


            filteredList = new ArrayList<>();
        }

        @Override
        public int getItemViewType(int position) {


            if (evacuationList.get(position).getBarcode() == null) {
                return 0;

            } else
                return 1;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {


            if (viewType == 0) {
                return new VH(getLayoutInflater().inflate(R.layout.inflate_evacuation_admin_header1, parent,
                        false));
            } else if (viewType == 1) {

                return new VH(getLayoutInflater().inflate(R.layout.inflate_evacuationlist, parent, false));
            }

            return new VH(getLayoutInflater().inflate(R.layout.inflate_evacuationlist, parent, false));

        }

        @Override
        public void onBindViewHolder(VH holder, int position) {


            Evacuation evacuation = evacuationList.get(position);

            if (holder.getItemViewType() == 0) {

                holder.tvtext.setText(evacuation.getFloor());

            } else {

                StringBuilder stringBuilder1 = new StringBuilder();
                if (evacuation.getAreaName() != null)
                    stringBuilder1.append(evacuation.getAreaName().trim() + ", ");
                if (evacuation.getFloor() != null) {

                    try {
                        stringBuilder1.append(ordinal(Integer.parseInt(evacuation.getFloor().trim())) + " floor");
                    } catch (Exception e) {
                        stringBuilder1.append(evacuation.getFloor().trim() + " floor");
                    }
                }
                if (evacuation.getCompany() != null)
                    stringBuilder1.append(" - " + evacuation.getCompany().trim());
                holder.tvtext.setText(stringBuilder1);

                if (evacuation.getClearTime() != null && evacuation.getClearTime().trim().length() > 0) {

                    StringBuilder stringBuilder = new StringBuilder();

                    stringBuilder.append(evacuation.getClearTime().trim() + "");


                    if (evacuation.getClearedByName() != null && evacuation.getClearedByName().trim().length() > 0) {


                        holder.tvdate.setVisibility(View.VISIBLE);
                        if (evacuation.getAdminID() != null
                                && evacuation.getAdminID().trim().length() > 0)

                            holder.tvdate.setText("By " + evacuation.getClearedByName().trim() + "");

                        else {

                            holder.tvdate.setText("By " + evacuation.getClearedByName().trim() + "");
                        }
                    } else if (evacuation.getAdminID() == null || evacuation.getAdminID().trim().length() == 0) {
                        holder.tvdate.setText("From Reception app ");
                    } else {
                        holder.tvdate.setText("");
                        holder.tvdate.setVisibility(View.GONE);
                    }

                    if (evacuation.getStartTime() != null
                            && evacuation.getStartTime().trim().length() > 0
                            && evacuation.getClearDate() != null) {


                        try {

                            Date date1 = simpleDateFormat.parse(evacuation.getClearDate());
                            Calendar calendar1 = Calendar.getInstance();
                            calendar1.setTime(simpleDateFormat.parse(evacuation.getStartTime()));
                            long different = date1.getTime() - calendar1.getTimeInMillis();

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
                                time = numberFormat.format(elapsedDays) + ":" +
                                        numberFormat.format(elapsedHours) + ":" +
                                        numberFormat.format(elapsedMinutes) + ":" +
                                        numberFormat.format(elapsedSeconds);
                            } else if (elapsedHours > 0) {
                                time =
                                        numberFormat.format(elapsedHours) + ":" +
                                                numberFormat.format(elapsedMinutes) + ":" +
                                                numberFormat.format(elapsedSeconds);
                            } else if (elapsedMinutes > 0) {
                                time = "00:" +
                                        numberFormat.format(elapsedMinutes) + ":" +
                                        numberFormat.format(elapsedSeconds);

                            } else if (elapsedSeconds > 0) {
                                time = "00:00:" + numberFormat.format(elapsedSeconds) + "";
                            }

                            stringBuilder.append(" (" + time + ")");
                        } catch (Exception e) {


                            stringBuilder = new StringBuilder();
                        }

                        holder.tvclear.setText("Cleared at: " + stringBuilder);
                        holder.tvclear.setVisibility(View.VISIBLE);

                    }

                } else {
                    holder.tvclear.setText("");
                    holder.tvdate.setText("");

                    holder.tvclear.setVisibility(View.GONE);
                    holder.tvdate.setVisibility(View.GONE);
                }


                holder.tvtext2.setText(evacuation.getName() + " (" + evacuation.getPriority() + ")");


                ImageRequest request = ImageRequestBuilder.newBuilderWithSource
                        (Uri.parse(evacuation.getPhotoUrl().trim()))
                        .setResizeOptions(new ResizeOptions(
                                getResources().getDimensionPixelOffset(R.dimen.ivwidth50),
                                getResources().getDimensionPixelSize(R.dimen.ivwidth50)))
                        .build();

                holder.ivimage.setController(
                        Fresco.newDraweeControllerBuilder()
                                .setOldController(holder.ivimage.getController())
                                //  .setLowResImageRequest(request)
                                .setImageRequest(request)
                                .build());


                if (evacuation.getUserAvailability() != null &&
                        evacuation.getUserAvailability().equalsIgnoreCase(IpAddress.USERAVAILABILITY_FALSE)) {
                    holder.tvtext2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circlered
                            , 0, 0, 0);
                } else if (evacuation.getUserAvailability() == null || evacuation.getUserAvailability().trim().isEmpty()) {

                    holder.tvtext2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circleyellow,
                            0, 0, 0);
                } else if (
                        evacuation.getUserAvailability() != null && evacuation.getUserAvailability().equalsIgnoreCase(IpAddress.USERAVAILABILITY_TRUE)) {

                    holder.tvtext2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circlegreen, 0, 0, 0);

                } else {
                    holder.tvtext2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }

                if (evacuation.getStatus() != null && evacuation.getStatus().trim().length() > 0) {

                    if (evacuation.getClearTime() != null &&
                            !evacuation.getClearTime().trim().isEmpty()) {
                        holder.ivcount.setImageResource(R.drawable.officegreen);


                    } else {
                        holder.ivcount.setImageResource(R.drawable.officered);

                    }
                } else {
                    holder.ivcount.setImageResource(0);

                }


                holder.ivright.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        hideSoft();
                        Intent intent = new Intent(EvacuationListActivity.this,
                                EvacuateAdminActivty.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(IpAddress.EVACUATIONID,
                                evacuationList.get(holder.getAdapterPosition()).getEvacuationId());
                        intent.putExtra(IpAddress.PLACEID,
                                evacuationList.get(holder.getAdapterPosition()).getPlaceID());
                        intent.putExtra(IpAddress.CLIENTID,
                                getClientID());
                        intent.putExtra(IpAddress.TVSTARTEND, tvstartend.getText().toString().trim());
                        startActivity(intent);
                        etsearch.getText().clear();
                        evacuationSearchList = null;
                    }
                });


                holder.rvrow.setOnClickListener(view -> {
                    hideSoft();

                    Intent intent = new Intent(EvacuationListActivity.this,
                            EvacuateAdminActivty.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(IpAddress.EVACUATIONID,
                            evacuationList.get(holder.getAdapterPosition()).getEvacuationId());
                    intent.putExtra(IpAddress.PLACEID,
                            evacuationList.get(holder.getAdapterPosition()).getPlaceID());
                    intent.putExtra(IpAddress.CLIENTID, getClientID());
                    intent.putExtra(IpAddress.TVSTARTEND, tvstartend.getText().toString().trim());
                    startActivity(intent);
                    etsearch.getText().clear();
                    evacuationSearchList = null;

                });


                if (evacuation.getStatus() == null
                        || evacuation.getStatus().trim().isEmpty()) {
                    holder.tvtext2.setCompoundDrawablesWithIntrinsicBounds(0
                            , 0, 0, 0);
                    holder.ivcount.setImageResource(0);

                } else {


                }


                if (evacuationResponsible.getUserType().equalsIgnoreCase(IpAddress.NORMALUSER)) {
                    holder.ivright.setVisibility(View.GONE);
                } else {
                    holder.ivright.setVisibility(View.VISIBLE);
                }
            }

        }

        @Override
        public int getItemCount() {
            return evacuationList.size();
        }

        @Override
        public Filter getFilter() {


            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    FilterResults filterResults = new FilterResults();
                    filteredList.clear();
                    String charString = charSequence.toString().trim();
                    if (charString.isEmpty()) {
                        filteredList.addAll(evacuationListOLD);
                    } else {

                        for (Evacuation row : evacuationListOLD) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match

//                            if (pos == 0
//                                    && row.getCompany().toLowerCase().contains(charString.toLowerCase())) {
//
//                                filteredList.add(row);
//
//                            } else if (pos == 1
//                                    && row.getAreaName().toLowerCase().contains(charString.toLowerCase())) {
//
//                                filteredList.add(row);
//
//                            } else
                            if (row.getCompany().toLowerCase().contains(charString.toLowerCase()) ||
                                    row.getAreaName().toLowerCase().contains(charString.toLowerCase())
                                    || row.getName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }

                    }
                    filterResults.values = filteredList;
                    filterResults.count = filteredList.size();
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                    evacuationList.clear();
                    evacuationList.addAll((ArrayList<Evacuation>) filterResults.values);
                    sortByPos(pos, evacuationList);

                }
            };
        }

        class Sortbyname implements Comparator<Evacuation> {
            // Used for sorting in ascending order of
            // roll name
            public int compare(Evacuation a, Evacuation b) {


                if (a.getClearTime().trim().isEmpty()) {
                    return Integer.MAX_VALUE;
                } else if (a.getClearTime().trim().isEmpty()) {
                    return Integer.MIN_VALUE;
                } else

                    return a.getClearTime().trim().compareTo(b.getClearTime().trim());
            }
        }

        public class VH extends RecyclerView.ViewHolder {

            @BindView(R.id.tvtext)
            TextView tvtext;
            @BindView(R.id.tvtext2)
            TextView tvtext2;
            @BindView(R.id.tvdate)
            TextView tvdate;

            @BindView(R.id.tvclear)
            TextView tvclear;

            @BindView(R.id.ivimage)
            SimpleDraweeView ivimage;

            @BindView(R.id.rvrow)
            ConstraintLayout rvrow;

            @BindView(R.id.ivcount)
            ImageView ivcount;

            @BindView(R.id.ivright)
            ImageView ivright;


            public VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }


}

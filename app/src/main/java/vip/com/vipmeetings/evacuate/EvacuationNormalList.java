package vip.com.vipmeetings.evacuate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.TransitionManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import java.util.ArrayList;
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
import vip.com.vipmeetings.body.GetResponsibleDetailsRequestBody;
import vip.com.vipmeetings.body.GetResponsibleResponseBody;
import vip.com.vipmeetings.body.GetResponsiblesRequestBody;
import vip.com.vipmeetings.body.GetUnReadMessageCountResponseBody;
import vip.com.vipmeetings.body.SaveResponsiblePhotoBody;
import vip.com.vipmeetings.body.SoapRemoveRequestBody;
import vip.com.vipmeetings.body.UnreadMessageCountRequestBody;
import vip.com.vipmeetings.envelope.SaveResponsiblePhotoResponseEnevelope;
import vip.com.vipmeetings.envelope.SoapEvacuationResponsibleDetailsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapEvacuationResponsibleDetailsResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetEvacuationResponsiblesRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetEvacuationResponsiblesResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetUnreadMessageCountResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapRemoveResponsiblePhotoEnvelope;
import vip.com.vipmeetings.envelope.SoapRemoveResponsiblePhotoResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapSaveResponsiblePhotoEnvelope;
import vip.com.vipmeetings.envelope.SoapUnreadMessageCountRequestEnvelope;
import vip.com.vipmeetings.models.EvacuationResponsible;
import vip.com.vipmeetings.models.EvacuationResponsibles;
import vip.com.vipmeetings.request.GetEvacuationResponsibles;
import vip.com.vipmeetings.request.GetResponsibleDetails;
import vip.com.vipmeetings.request.GetUnReadMessageCount;
import vip.com.vipmeetings.request.RemoveResponsiblePhoto;
import vip.com.vipmeetings.request.SaveResponsiblePhoto;
import vip.com.vipmeetings.utilities.Constants;

import static vip.com.vipmeetings.utilities.Constants.EVACUATIONREFRESH;

public class EvacuationNormalList extends BaseActivity {


    public List<String> areaList;
    private List<EvacuationResponsible> evacuationResponsibleModelList;
    @BindView(R.id.sparea)
    AppCompatSpinner sparea;
    public int position;

    @BindView(R.id.ivcount)
    ImageView ivcount;

    @BindView(R.id.clroot)
    ConstraintLayout clroot;

    @BindView(R.id.tvcount)
    TextView tvcount;

    @BindView(R.id.rvlist)
    RecyclerView rvlist;
    @BindView(R.id.tvevastatus)
    TextView tvevastatus;
    @BindView(R.id.tvcontact)
    TextView tvcontact;

    @BindView(R.id.tvback)
    ImageView tvback;


    @BindView(R.id.ivimagefull)
    SimpleDraweeView ivimagefull;


    AdapterAdmin adapterAdmin;
    List<EvacuationResponsible> evacuationResponsibleList;

    EvacuationResponsible evacuationResponsibleNew;
    private String areaName = "";
    private String mobile = "";


    AlertDialog alertDialog;
    private Uri imageUri;
    private String base64 = "";

    boolean isImageFull;
    ConstraintSet constraintSet;

    boolean isRefersh = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normallist);
        ButterKnife.bind(this);
        areaList = new ArrayList<>();

        evacuationResponsibleModelList = new ArrayList<>();
        evacuationResponsibleModelList.clear();

        evacuationResponsibleList = new ArrayList<>();
        adapterAdmin = new AdapterAdmin();

        rvlist.setLayoutManager(new LinearLayoutManager(this));
//        rvlist.addItemDecoration(new SeparatorDecoration(this,
//                Color.WHITE, 0.1f, 16, 16));
        rvlist.setAdapter(adapterAdmin);
    }

    @OnClick(R.id.tvback)
    public void onBack(View v) {
        onBackPressed();
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEva(String update) {


        refreshScreen();
    }


    private void refreshScreen() {



        deleteStiky(this);
        readEva();

    }

    public void getEvacuationDetailsByEvacuationID(EvacuationResponsible evaCuationID) {

        try {

            showPD(this);
            SoapGetEvacuationResponsiblesRequestEnvelope soapGetEvacuationResponsiblesRequestEnvelope =
                    new SoapGetEvacuationResponsiblesRequestEnvelope();

            GetResponsiblesRequestBody getResponsiblesRequestBody = new GetResponsiblesRequestBody();
            GetEvacuationResponsibles getEvacuationResponsibles = new GetEvacuationResponsibles();

            getEvacuationResponsibles.setA_sClientId(evaCuationID.getClientId());

            getEvacuationResponsibles.setAuthToken(getAuthTokenPref_Evacuation());
            getEvacuationResponsibles.setA_sEvacuationId(evaCuationID.getEvacuationId());

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

        adapterAdmin.notifyDataSetChanged();
        getUnreadMessages();

    }

    private void getUnreadMessages() {


        showPD(this);
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
            getUnReadMessageCount.setA_sPlaceId(evacuationResponsible.getA_sPlaceId());
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

                    evacuationResponsibleList.add(evacuationResponsibleBarcode);
                    break;
                }
            }

        for (EvacuationResponsible evacuationResponsible1 : evacuationResponsibles.getEvacuationResponsible()) {
            if (evacuationResponsible1.getBarcode() != null
                    && getBarcode() != null
                    && evacuationResponsible1.getBarcode().equalsIgnoreCase(getBarcode())) {
            } else {
                evacuationResponsibleList.add(evacuationResponsible1);
            }
        }

        return evacuationResponsibles;
    }

    private EvacuationResponsibles onSoapGetEvacuation(SoapGetEvacuationResponsiblesResponseEnvelope
                                                               soapGetEvacuationResponsiblesResponseEnvelope) {


        GetResponsibleResponseBody getResponsibleResponseBody =
                soapGetEvacuationResponsiblesResponseEnvelope.getBody();


        return getResponsibleResponseBody.getGetEvacuationResponsiblesResponse().
                getGetEvacuationResponsiblesResult().getGetEvacuationResponsibles();
    }


    @OnClick(R.id.tvcontact)
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
            startActivityForResult(intent, EVACUATIONREFRESH);
        }
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


    @Override
    public void onBackPressed() {

        if (isImageFull) {
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
            roundingParams.setRoundAsCircle(true);
            ivimagefull.getHierarchy().setRoundingParams(roundingParams);
            isRefersh = true;
            isImageFull = false;
            constraintSet.applyTo(clroot);
        } else {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void readEva() {


        if (isRefersh) {
          //  clearFresco();
            if (hasNetwork()) {
                showPD();
                getEvacuationDetails();
            } else {
                hidePD();
                showToast(getString(R.string.nointernet));
            }
        }
    }

    private void getEvacuationDetails() {

        try {
            if (evacuationResponsible != null) {

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
            } else if (getClientID() != null && getBarcode() != null) {

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

            } else {
                hidePD();
                showToast(getString(R.string.someerror));
            }


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

            for (EvacuationResponsible evacuationResponsible : evacuationResponsibleModelList) {
                if (getBarcode().equalsIgnoreCase(evacuationResponsible.getBarcode())) {

                    evacuationResponsibleNew = evacuationResponsible;
                    break;
                }
            }

        }


        return evacuationResponsibleModelList;
    }

    private void onEvacuation(List<EvacuationResponsible> evacuationResponsibleModelList) {

        setSpiiner();

    }


    public class SpinnerAdapter extends ArrayAdapter<EvacuationResponsible> {


        public SpinnerAdapter() {

            super(EvacuationNormalList.this, 0, evacuationResponsibleModelList);
        }


        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


            EvacuationResponsible evacuationResponsible = evacuationResponsibleModelList.get(position);
            View view = LayoutInflater.from(EvacuationNormalList.this).inflate(
                    R.layout.inflate_evacationnormal2, parent, false
            );

            TextView tv = view.findViewById(R.id.text1);

            tv.setText(evacuationResponsible.getAreaName());

            TextView tv2 = view.findViewById(R.id.text2);

            if(evacuationResponsible.getPriority()!=null)
            tv2.setText(evacuationResponsible.getPriority().toUpperCase() + " responsible");


            return view;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


            EvacuationResponsible evacuationResponsible = evacuationResponsibleModelList.get(position);
            View view = LayoutInflater.from(EvacuationNormalList.this).inflate(
                    R.layout.inflate_evacationlist, parent, false
            );

            TextView tv = view.findViewById(R.id.text1);

            try {
                tv.setText(ordinal(Integer.parseInt(evacuationResponsible.getFloor()))
                        + " floor, " + evacuationResponsible.getAreaName());
            } catch (Exception e) {
                tv.setText(evacuationResponsible.getFloor() + " floor, " + evacuationResponsible.getAreaName());

            }


            return view;
        }
    }

    private void setSpiiner() {

        sparea.setAdapter(new SpinnerAdapter());

        sparea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


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
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == 0) {

                return new VH(getLayoutInflater().inflate(R.layout.inflate_adapternormallist1, parent, false));
            } else {
                return new VH(getLayoutInflater().inflate(R.layout.inflate_adapternormallist2, parent, false));

            }
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


            if (position == evacuationResponsibleList.size() - 1) {
                holder.vline.setVisibility(View.GONE);

            } else {
                holder.vline.setVisibility(View.VISIBLE);
            }

            switch (position) {


                case 0:

                    holder.tvarea.setVisibility(View.GONE);
                    //   holder.tvtype.setText(IpAddress.CURRENT_AREA);
                    holder.tvtype.setText(IpAddress.CURRENT_AREA_ME);
                    holder.tvtype.setVisibility(View.VISIBLE);
                    break;

                case 1:
                    holder.tvarea.setText("");
                    holder.tvarea.setVisibility(View.GONE);
                    // holder.tvtype.setText(IpAddress.NEXT);
                    holder.tvtype.setText(IpAddress.NEXT_YOU_CO);
                    holder.tvtype.setVisibility(View.VISIBLE);
                    break;
                default:
                    holder.tvtype.setText("");
                    holder.tvarea.setText("");
                    holder.tvarea.setVisibility(View.GONE);
                    holder.tvarea.setVisibility(View.GONE);
                    holder.tvtype.setVisibility(View.GONE);
                    break;
            }


            if (evacuation.getSTATUS() == null
                    || evacuation.getSTATUS().trim().isEmpty()) {

                holder.ivcount.setImageResource(0);
                holder.ivcount_image.setImageResource(0);
            } else {
                if (evacuation.getUserAvailability() != null &&
                        evacuation.getUserAvailability().equalsIgnoreCase(IpAddress.USERAVAILABILITY_TRUE)) {
                    holder.tvstatus.setText("");
                    holder.ivcount.setImageResource(R.drawable.circlegreen);
                    holder.ivcount_image.setImageResource(R.drawable.circlegreen);
                    holder.tvstatus.setTextColor(ContextCompat.getColor(EvacuationNormalList.this,
                            R.color.color_useravailability_no));
                } else if (evacuation.getUserAvailability() != null &&
                        evacuation.getUserAvailability().equalsIgnoreCase(IpAddress.USERAVAILABILITY_FALSE)) {

                    holder.ivcount.setImageResource(R.drawable.circlered);
                    holder.ivcount_image.setImageResource(R.drawable.circlered);
                    holder.tvstatus.setText("(Not in the building)");
                    holder.tvstatus.setTextColor(ContextCompat.getColor(EvacuationNormalList.this,
                            R.color.textred));
                } else {

                    holder.tvstatus.setText("");
                    holder.ivcount.setImageResource(R.drawable.circleyellow);
                    holder.ivcount_image.setImageResource(R.drawable.circleyellow);
                    holder.tvstatus.setTextColor(ContextCompat.getColor(EvacuationNormalList.this,
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
                    && evacuation.getAreaName() != null)
                holder.tvarea.setText(evacuation.getAreaName().trim());
            else
                holder.tvarea.setText("");

            if (evacuation.getName() != null) {
                holder.tvname.setText(evacuation.getName());
            } else if (evacuation.getName() != null
                    && evacuation.getCompany() != null) {
                holder.tvname.setText(evacuation.getName() + " (" + evacuation.getPriority() + "), " +
                        evacuation.getFloor() + ", " + evacuation.getCompany());

                holder.tvname.setText(evacuation.getName() + " (" + evacuation.getPriority() + "), " +
                        evacuation.getFloor() + ", " + evacuation.getCompany());
            } else {
                holder.tvname.setText("");
            }


            if (evacuation.getPriority() != null) {

                holder.tvresponsible.setText(evacuation.getPriority().toUpperCase() + " responsible");
            } else {
                holder.tvresponsible.setText("");
            }

            if (evacuation.getMobile() != null)
                holder.tvmobile.setText(evacuation.getMobile());
            else
                holder.tvmobile.setText("");


//            if (evacuation.getPhotoUrl() != null)
//                holder.ivimage.setImageURI(evacuation.getPhotoUrl());
//            else
//                holder.ivimage.setImageResource(0);

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


                //   clearFresco();
                //  imagePipeline.evictFromCache(Uri.parse(evacuation.getPhotoUrl().trim()));

                holder.ivimage.setController(
                        Fresco.newDraweeControllerBuilder()
                                .setOldController(holder.ivimage.getController())
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

                                        if (holder.getAdapterPosition() == 0) {

                                            holder.ivcameraactual.setVisibility(View.VISIBLE);
                                            holder.ivcamera.setVisibility(View.GONE);


                                        } else {
                                            holder.ivcameraactual.setVisibility(View.GONE);
                                            holder.ivcamera.setVisibility(View.GONE);
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

                                            holder.ivcameraactual.setVisibility(View.GONE);
                                            holder.ivcamera.setVisibility(View.VISIBLE);


                                        } else {
                                            holder.ivcameraactual.setVisibility(View.GONE);
                                            holder.ivcamera.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onRelease(String id) {

                                    }
                                })
                                .setLowResImageRequest(request)
                                .build());

            } else if(imageUri!=null)
            {

                if(position==0) {
                    holder.ivimage.setImageURI(imageUri);
                }else
                {
                    holder.ivimage.setImageResource(R.mipmap.ic_profile);
                }
            }
            else {
                holder.ivimage.setImageResource(R.mipmap.ic_profile);
            }


            if (evacuation.getEmail() != null)
                holder.tvemail.setText(evacuation.getEmail().trim());
            else
                holder.tvemail.setText("");

            holder.tvmobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    callMobile(evacuationResponsibleList.get(holder.getAdapterPosition()).getMobile());
                }
            });

            if (evacuation.getUserAvailability() == null || evacuation.getUserAvailability().isEmpty()) {
                holder.tvstatus.setText("(Unverified position)");
                holder.tvstatus.setTextColor(ContextCompat.getColor(EvacuationNormalList.this,
                        R.color.color_noevacuation));
            } else if (evacuation.getUserAvailability().equalsIgnoreCase("True")) {
                holder.tvstatus.setText("(Verified position)");
                holder.tvstatus.setTextColor(ContextCompat.getColor(EvacuationNormalList.this,
                        R.color.color_noevacuation));
            } else {
                holder.tvstatus.setTextColor(ContextCompat.getColor(EvacuationNormalList.this,
                        R.color.textred));
                holder.tvstatus.setText("(Not in the building)");
            }

            holder.tvemail.setPaintFlags(holder.tvemail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            holder.tvmobile.setPaintFlags(holder.tvemail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            holder.ivcamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    capturePhoto();

                }
            });

            holder.ivcameraactual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    capturePhoto();
                }
            });

        }

        @Override
        public int getItemCount() {
            return evacuationResponsibleList.size();
        }

        public class VH extends RecyclerView.ViewHolder {

            @BindView(R.id.tvname)
            TextView tvname;

            @BindView(R.id.tvresponsible)
            TextView tvresponsible;

            @BindView(R.id.tvmobile)
            TextView tvmobile;

            @BindView(R.id.tvtype)
            TextView tvtype;

            @BindView(R.id.tvemail)
            TextView tvemail;

            @BindView(R.id.ivcamera)
            AppCompatImageView ivcamera;

            @Nullable
            @BindView(R.id.ivcameraactual)
            AppCompatImageView ivcameraactual;

            @BindView(R.id.tvstatus)
            TextView tvstatus;

            @BindView(R.id.ivimage)
            SimpleDraweeView ivimage;

            @BindView(R.id.ivcount_image)
            AppCompatImageView ivcount_image;


            @BindView(R.id.tvarea)
            TextView tvarea;

            @BindView(R.id.vline)
            View vline;

            @BindView(R.id.tvcount)
            TextView tvcount;
            @BindView(R.id.ivcount)
            ImageView ivcount;
            @BindView(R.id.ivmessage)
            ImageView ivmessage;

            public VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }

    private void clearFresco() {

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearMemoryCaches();
        imagePipeline.clearDiskCaches();

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
                        imageUri = FileProvider.getUriForFile(EvacuationNormalList.this,
                                "vip.com.vipmeetings.fileprovider", mCurrentPhotoFile);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } else {
                        imageUri = Uri.fromFile(mCurrentPhotoFile);
                    }
                    intent.setAction(Intent.ACTION_GET_CONTENT);
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
        constraintSet.clone(clroot);
        TransitionManager.beginDelayedTransition(clroot);

        ConstraintSet constraintSet2 = new ConstraintSet();
        constraintSet2.clone(this, R.layout.imagelayout);

        if (isImageFull) {
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
            roundingParams.setRoundAsCircle(true);
            ivimagefull.getHierarchy().setRoundingParams(roundingParams);
            isImageFull = false;
            constraintSet.applyTo(clroot);
        } else {

            isRefersh=false;
            isImageFull = true;
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
            roundingParams.setRoundAsCircle(false);
            ivimagefull.getHierarchy().setRoundingParams(roundingParams);


            constraintSet2.connect(R.id.ivimagefull, ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet2.connect(R.id.ivimagefull, ConstraintSet.END,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);


            //   constraintSet2.constrainWidth(R.id.myButton, ConstraintSet.WRAP_CONTENT);

            ivimagefull.setImageURI(evacuationResponsibleList.get(0).getPhotoUrl());
            constraintSet2.applyTo(clroot);

//            ivimagefull.setImageURI(evacuationResponsibleList.get(0).getPhotoUrl());
//
//            constraintSet2.applyTo(clroot);
        }

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }


    }

    private void cameraPermission() {


        if (ContextCompat.checkSelfPermission(EvacuationNormalList.this, Manifest.permission.CAMERA)
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
                imageUri = FileProvider.getUriForFile(EvacuationNormalList.this,
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

                        imageUri = data.getData();
                        iStream = getContentResolver().openInputStream(data.getData());
                        byte[] inputData = getBytes(iStream);
                        base64 = org.kobjects.base64.Base64.encode(inputData);
                        savePhoto();
                    } catch (FileNotFoundException e) {
                        IpAddress.e("err", e.toString());
                    }

                    break;

                case REQUEST_CAPTURE:

                    try {
                        iStream = getContentResolver().openInputStream(imageUri);
                        byte[] inputData = getBytes(iStream);
                        base64 = org.kobjects.base64.Base64.encode(inputData);
                        savePhoto();

                    } catch (FileNotFoundException e) {
                        IpAddress.e("err", e.toString());
                    }

                    break;


                default:
                    break;
            }
    }

    private void savePhoto() {

        dismissDialogLayout();
        showPD(this);
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
        hidePD();
        clearFresco();


        ivimagefull.setImageURI(imageUri);

       // evacuationResponsibleList.get(0).setPhotoUrl(imageUri.toString());
        adapterAdmin.notifyDataSetChanged();



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
        //  refreshScreen();
    }

    private void onRemovePhoto(SoapRemoveResponsiblePhotoResponseEnvelope
                                       soapRemoveResponsiblePhotoResponseEnvelope) {

        dismissDialogLayout();
        hidePD();
        // refreshScreen();
        clearFresco();
        imageUri=null;
        ivimagefull.setImageResource(R.mipmap.ic_profile);
        evacuationResponsibleList.get(0).setPhotoUrl(null);
        adapterAdmin.notifyDataSetChanged();
    }

    private void callMobile(String mobile) {
        this.mobile = mobile;
        if (ContextCompat.checkSelfPermission(EvacuationNormalList.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EvacuationNormalList.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        } else {
            call();
        }
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

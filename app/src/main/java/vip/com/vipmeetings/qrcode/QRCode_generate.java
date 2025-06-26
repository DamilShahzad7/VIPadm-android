package vip.com.vipmeetings.qrcode;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.EnumMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vip.com.vipmeetings.BaseActivity;
import vip.com.vipmeetings.MainActivity;
import vip.com.vipmeetings.R;
import vip.com.vipmeetings.bluno.BlunoActivity;
import vip.com.vipmeetings.body.GetEvacuationInformationResponseBody;
import vip.com.vipmeetings.body.GetInEvacuationInformationRequestBody;
import vip.com.vipmeetings.body.GetUpdatePINRequestBody;
import vip.com.vipmeetings.body.GetUpdatePINResponseBody;
import vip.com.vipmeetings.body.RegisterInRequestBody;
import vip.com.vipmeetings.body.RegisterInResponseBody;
import vip.com.vipmeetings.body.GetInHouseContactsCountRequestBody;
import vip.com.vipmeetings.body.GetInHouseContactsCountResponseBody;
import vip.com.vipmeetings.body.RegisterOutRequestBody;
import vip.com.vipmeetings.body.RegisterOutResponseBody;
import vip.com.vipmeetings.body.UnRegisterOutRequestBody;
import vip.com.vipmeetings.body.UnRegisterOutResponseBody;
import vip.com.vipmeetings.body.ValidateUserRequestBody;
import vip.com.vipmeetings.body.ValidateUserResponseBody;
import vip.com.vipmeetings.envelope.SoapGetEvacuationInformationRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetEvacuationInformationResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetUpdatePINRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetUpdatePINResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapRegisterInRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapRegisterInResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetInHouseContactsCountRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetInHouseContactsCountResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapRegisterOutRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapRegisterOutResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapUnRegisterOutRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapUnRegisterOutResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapValidateUserRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapValidateUserResponseEnvelope;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.interfaces.MainApi;
import vip.com.vipmeetings.models.Employee;
import vip.com.vipmeetings.models.EvacuationInformation;
import vip.com.vipmeetings.models.EvacuationsInformation;
import vip.com.vipmeetings.request.GetEvacuationInformationRequest;
import vip.com.vipmeetings.request.GetUpdatePINRequest;
import vip.com.vipmeetings.request.RegisterInRequest;
import vip.com.vipmeetings.request.GetInHouseContactsCountRequest;
import vip.com.vipmeetings.request.RegisterOutRequest;
import vip.com.vipmeetings.request.UnRegisterOutRequest;
import vip.com.vipmeetings.request.ValidateUserRequest;
import vip.com.vipmeetings.response.GetEvacuationInformationResponse;
import vip.com.vipmeetings.response.GetEvacuationResult;
import vip.com.vipmeetings.response.GetUpdatePINResponse;
import vip.com.vipmeetings.utilities.Constants;
import vip.com.vipmeetings.utilities.CustomMaterialAutoCompleteTextView;
import android.text.InputType;
import android.provider.MediaStore;
import com.google.gson.Gson;


public class QRCode_generate extends BaseActivity implements TextView.OnEditorActionListener {


    @BindView(R.id.name)
    TextView name;
    Bitmap bitmap;
    @BindView(R.id.cmpimg)
    ImageView cmpimg;
    @BindView(R.id.iv)
    SimpleDraweeView simpleDraweeView;
    @BindView(R.id.iv_logo)
    SimpleDraweeView simpleDraweeViewLogo;
    @BindView(R.id.ivbluno)
    AppCompatImageView ivbluno;
    @BindView(R.id.tvgate)
    AppCompatTextView tvgate;
    SessionManager session;
    Employee employee;
    @BindView(R.id.img_result)
    SimpleDraweeView img_result;

    @BindView(R.id.ivback)
    ImageView ivback;

    @BindView(R.id.view)
    View view;

    @BindView(R.id.checkAccess)
    CustomMaterialAutoCompleteTextView  btnAccess;

    @BindView(R.id.insideUsersCount)
    TextView insideUsersCount;
    @BindView(R.id.inOutTime)
    TextView inOutTime;
    @BindView(R.id.btnEvacuation)
    AppCompatButton btnEvacuation;
    @BindView(R.id.et_setPin)
    TextInputEditText et_setPin;
    @BindView(R.id.llsetpin)
    LinearLayout llSetPin;
    @BindView(R.id.tiaccess)
    TextInputLayout tiaccess;
    @BindView(R.id.ivMore)
    ImageView ivMore;

    String qrcode;
    int width, height;
    String companylogourl, contactphotourl, fullname, mobile;
    MultiFormatWriter multiFormatWriter;
    BarcodeEncoder barcodeEncoder;
    public MainApi mainApi;
    Boolean checkInEvacuationInformationStatus = false;
    ArrayAdapter<String> adapter;
    ProgressDialog progressDialog;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode);
        ButterKnife.bind(this);
        llSetPin.setVisibility(View.GONE);
        // --- wire up the three-dots “more” popup menu ---
        ivMore.setOnClickListener(anchor -> {
            PopupMenu popup = new PopupMenu(QRCode_generate.this, anchor);
            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

            // replace placeholders:
            String livePin = et_setPin.getText().toString();
            popup.getMenu()
                    .findItem(R.id.action_update_pin)
                    .setTitle("Update PIN: " + livePin);

            String liveCount = insideUsersCount.getText().toString();
            popup.getMenu()
                    .findItem(R.id.action_people_inside)
                    .setTitle(liveCount);

            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.action_update_photo:
                        showPhotoOptionsDialog();
                        return true;


                    case R.id.action_update_pin:
                        showPinUpdateDialog();
                        return true;

                    case R.id.action_people_inside:
                        getInHouseContactsCount();
                        Toast.makeText(this, insideUsersCount.getText(), Toast.LENGTH_SHORT).show();
                        return true;

                    default:
                        return false;
                }
            });


            popup.show();
        });

        employee = getEmp();
        barcodeEncoder = new BarcodeEncoder();
        session = new SessionManager(getApplicationContext());
        setRetrofit(getApp().getTcpIp());
        mainApi = retrofit.create(MainApi.class);
        employee=getEmp();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

       /* if(employee.getUSERTYPE()!=null && employee.getUSERTYPE().trim().equalsIgnoreCase(IpAddress.USERTYPE_ACCESS))
        {

            if(employee.isOPENDOOR()) {
                view.setVisibility(View.VISIBLE);
                ivbluno.setVisibility(View.VISIBLE);
                tvgate.setVisibility(View.VISIBLE);
                ivback.setVisibility(View.VISIBLE);
            }else
            {
                ivbluno.setVisibility(View.GONE);
                tvgate.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                ivback.setVisibility(View.GONE);
            }

        }else if(employee.isOPENDOOR() && getIntent().getBooleanExtra(IpAddress.GATEOPENER,false))
        {

            view.setVisibility(View.VISIBLE);
            ivbluno.setVisibility(View.VISIBLE);
            tvgate.setVisibility(View.VISIBLE);
            ivback.setVisibility(View.VISIBLE);

        }
        else
        {

            ivbluno.setVisibility(View.GONE);
            tvgate.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            ivback.setVisibility(View.VISIBLE);

        }*/

        if (employee != null) {
            showProgressDialog();
            qrcode = employee.getQRCODE();
            contactphotourl = employee.getPHOTO();
            companylogourl = employee.getCOMPANYLOGO();
            fullname = employee.getFULLNAME();
            mobile = employee.getMOBILE();
           /* SpannableString ss1 = new SpannableString(fullname );
            ss1.setSpan(new RelativeSizeSpan(1f), 0,
                    fullname.length(), 0);*/
            name.setText(fullname);
            if (qrcode != null && qrcode.trim().length() > 0) {
                Single.fromCallable(this::loadInBackgroundEvacuation)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::updateUi, this::Error);


            }
        }
        getInHouseContactsCount();
        getEvacuationInformation();
        et_setPin.setText(employee.getPIN());


        /*if ((employee.getINTIME() == null ||  employee.getINTIME().isEmpty() ) && (employee.getOUTTIME() == null ||  employee.getOUTTIME().isEmpty() )  && employee.getWORKINGFROM()==null || employee.getWORKINGFROM().isEmpty())
        {
            inOutTime.setVisibility(View.GONE);
            btnAccess.setText("CheckIn");
            setDrawableEnd(R.drawable.btrdownright);
        }
        //(IN is not null and not empty) and (OUT is null or empty) then CheckOut
        else if((employee.getINTIME() != null && !employee.getINTIME().isEmpty()) && (employee.getOUTTIME() == null|| employee.getOUTTIME().isEmpty())){
            btnAccess.setText("CheckOut");
            inOutTime.setVisibility(View.VISIBLE);
            if(employee.getWORKINGFROM().equalsIgnoreCase("Home")) {
                inOutTime.setText("Work from home: " + employModel.getINTIME());
            }else{
                inOutTime.setText("Office In: " + employModel.getINTIME());
            }

        }
        else if((employee.getINTIME() != null && !employee.getINTIME().isEmpty()) && (employee.getOUTTIME() != null && !employee.getOUTTIME().isEmpty())) {
            if(checkInEvacuationInformationStatus){
                btnAccess.setVisibility(View.GONE);
            }else {
                btnAccess.setText("CheckIn");
                setDrawableEnd(R.drawable.btrdownright);
                inOutTime.setVisibility(View.VISIBLE);

                if (employee.getWORKINGFROM().equalsIgnoreCase("Home")) {
                    inOutTime.setText("Work from home: " + employModel.getINTIME() + " " + " " + " Out: " + employModel.getOUTTIME());
                } else {
                    inOutTime.setText("Office In: " + employModel.getINTIME() + " " + " " + " Out: " + employModel.getOUTTIME());
                }
            }
        }
        *//*else{
            btnAccess.setText("CheckIn");
            setDrawableEnd(R.drawable.btrdownright);
            inOutTime.setVisibility(View.VISIBLE);
            if(officeStatus.equalsIgnoreCase("Home")) {
                inOutTime.setText("Work from home: " + employModel.getINTIME() + " " + " " + " Out: " + employModel.getOUTTIME());
            }else{
                inOutTime.setText("Office In: " + employModel.getINTIME() + " " + " " + " Out: " + employModel.getOUTTIME());
            }
        }*//*
        if(btnAccess.getText().toString().equalsIgnoreCase("CheckIn")){
            if(checkInEvacuationInformationStatus){
                btnEvacuation.setVisibility(View.VISIBLE);
            }
            else{
                btnEvacuation.setVisibility(View.GONE);
            }
        }else{
            btnEvacuation.setVisibility(View.GONE);
        }*/

        et_setPin.setOnEditorActionListener(this);
        btnAccess.setOnTouchListener((v, event) -> {
            v.performClick();
            return false; // Returning false allows other click events to proceed
        });

        btnAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonText = btnAccess.getText().toString();
                if (buttonText.equalsIgnoreCase("CheckIn")) {
                    showPD();
                    showDropDownList();
                } else if (buttonText.equalsIgnoreCase("Evacuation CheckOut")) {
                    showPD();
                    unRegisterOutEvacuation(); // register -out
                } else {
                    showPD();
                    registerOut(); // register -out
                }
            }
        });

    }

    private static final int REQUEST_PICK_PHOTO = 37;
    private static final int REQUEST_CAPTURE_PHOTO    = 38;

    private void showPhotoOptionsDialog() {
        final CharSequence[] options = { "Camera", "Photo Album", /*"Delete Photo"*/ };

        new AlertDialog.Builder(this)
                .setTitle("Update Photo")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:  // Camera
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePicture.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(takePicture, REQUEST_CAPTURE_PHOTO);
                            }
                            break;

                        case 1:  // Photo Album
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto , REQUEST_PICK_PHOTO);
                            break;

                        /*case 2:  // Delete
                            img_result.setImageURI((Uri) null);           // clear the DraweeView
                            simpleDraweeView.setImageURI((Uri) null);
                            // 2) clear it in your model
                            employee.setPHOTO(null);

                            // persist
                            String json = new Gson().toJson(employee);
                            storeEmp(json);

                            Toast.makeText(this, "Photo deleted", Toast.LENGTH_SHORT).show();
                            // Optional: notify server that photo was deleted
                            break;*/
                    }
                })
                .show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) return;

        if (requestCode == REQUEST_PICK_PHOTO) {
            Uri selectedImage = data.getData();
            simpleDraweeView.setImageURI(selectedImage);
            // upload new profile photo if needed
            // 1) update your model
            employee.setPHOTO(selectedImage.toString());

            // 2) serialize & store it (you already have storeEmp(json) in comments)
            String json = new Gson().toJson(employee);
            storeEmp(json);


        } else if (requestCode == REQUEST_CAPTURE_PHOTO) {
            Bundle extras = data.getExtras();
            Bitmap photoBitmap = (Bitmap) extras.get("data");
            simpleDraweeView.setImageBitmap(photoBitmap);
            // optionally save the bitmap to a file / upload
        }
    }


    private void showPinUpdateDialog() {
        final AppCompatEditText input = new AppCompatEditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(et_setPin.getText().toString());

        new AlertDialog.Builder(this)
                .setTitle("Update PIN")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String newPin = input.getText().toString().trim();
                    if (!newPin.isEmpty()) {
                        et_setPin.setText(newPin);
                        UpdatePIN(newPin);              // your existing web-service call
                        Toast.makeText(this,
                                "PIN updated to " + newPin,
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void UpdatePIN(String updatedPIN) {
        setOKHTTPAfterLogin();
        setRetrofit(getApp().getTcpIp());
        mainApi = retrofit.create(MainApi.class);
        SoapGetUpdatePINRequestEnvelope soapGetUpdatePINRequestEnvelope = new SoapGetUpdatePINRequestEnvelope();
        GetUpdatePINRequestBody getUpdatePINRequestBody = new GetUpdatePINRequestBody();
        GetUpdatePINRequest getUpdatePINRequest = new GetUpdatePINRequest();
        getUpdatePINRequest.setAuthToken(getAuthTokenPref_Mobile());
        getUpdatePINRequest.setA_sBarcode(getBarcode());
        getUpdatePINRequest.setA_sPin(updatedPIN);
        getUpdatePINRequestBody.setGetUpdatePINRequest(getUpdatePINRequest);
        soapGetUpdatePINRequestEnvelope.setBody(getUpdatePINRequestBody);
        Observable<SoapGetUpdatePINResponseEnvelope> lClientDetailsObservable = mainApi.GetUpdatePIN(soapGetUpdatePINRequestEnvelope);
        lClientDetailsObservable.
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetUpdatePIN, this::onError);
    }

    private void onGetUpdatePIN(SoapGetUpdatePINResponseEnvelope soapGetUpdatePINResponseEnvelope) {
        llSetPin.setVisibility(View.GONE);
        showPD();
        GetUpdatePINResponseBody getUpdatePINResponseBody = soapGetUpdatePINResponseEnvelope.getBody();
        if (getUpdatePINResponseBody != null
                && getUpdatePINResponseBody.getGetUpdatePINResponse() != null
                && getUpdatePINResponseBody.getGetUpdatePINResponse() != null) { ;
            GetUpdatePINResponse getUpdatePINResponse = getUpdatePINResponseBody.getGetUpdatePINResponse();
            if (getUpdatePINResponse!= null) {
                et_setPin.clearFocus();
                if(et_setPin.getText().toString().isEmpty()){
                    llSetPin.setVisibility(View.GONE);
                }else{
                    llSetPin.setVisibility(View.VISIBLE);
                }
                llSetPin.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Updated PIN successfully..", Toast.LENGTH_LONG).show();
            }
            hideSoft();
            hidePD();

        }
    }
    public void setDrawableEnd(int drawableResId ) {
        Drawable drawableEnd = ContextCompat.getDrawable(getApplicationContext(), drawableResId);
       btnAccess.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableEnd, null);
    }
 private void getEvacuationInformation(){
     String cityName ="";
     if (getStoreCitySelect() != null && getStoreCitySelect().getCityName() != null)
         cityName = getStoreCitySelect().getCityName();
     setOKHTTPAfterLogin();
     setRetrofit(getApp().getTcpIp());
     mainApi = retrofit.create(MainApi.class);
     SoapGetEvacuationInformationRequestEnvelope soapGetEvacuationInformationRequestEnvelope = new SoapGetEvacuationInformationRequestEnvelope();
     GetInEvacuationInformationRequestBody getInEvacuationInformationRequestBody = new GetInEvacuationInformationRequestBody();
     GetEvacuationInformationRequest getEvacuationInformationRequest = new GetEvacuationInformationRequest();
     getEvacuationInformationRequest.setAuthToken(getAuthTokenPref_Mobile());
     getEvacuationInformationRequest.setA_sCity(cityName);
     getInEvacuationInformationRequestBody.setGetEvacuationInformationRequest(getEvacuationInformationRequest);
     soapGetEvacuationInformationRequestEnvelope.setBody(getInEvacuationInformationRequestBody);

     Observable<SoapGetEvacuationInformationResponseEnvelope> lClientDetailsObservable = mainApi.GetEvacuationInformation(soapGetEvacuationInformationRequestEnvelope);
     lClientDetailsObservable.
             subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe(this::onGetEvacuationInformation, this::onError);
 }
    private void onGetEvacuationInformation(SoapGetEvacuationInformationResponseEnvelope soapGetEvacuationInformationResponseEnvelope)  {
        showPD();
        GetEvacuationInformationResponseBody getEvacuationInformationResponseBody = soapGetEvacuationInformationResponseEnvelope.getBody();

        if (getEvacuationInformationResponseBody != null
                && getEvacuationInformationResponseBody.getGetEvacuationInformationResponse() != null
                && getEvacuationInformationResponseBody.getGetEvacuationInformationResponse() != null) { ;

            if (getEvacuationInformationResponseBody
                    .getGetEvacuationInformationResponse()!= null) {
                GetEvacuationInformationResponse getEvacuationInformationResponse = getEvacuationInformationResponseBody.getGetEvacuationInformationResponse();
                if(getEvacuationInformationResponse.getGetEvacuationResult()!=null) {
                    GetEvacuationResult getEvacuationResult = getEvacuationInformationResponse.getGetEvacuationResult();
                    if(getEvacuationResult.getEvacuationsInformation()!=null){
                        EvacuationsInformation getEvacuations = getEvacuationResult.getEvacuationsInformation();

                        if(getEvacuations.getEvacuationInformation()!=null && !getEvacuations.getEvacuationInformation().isEmpty() ){
                            EvacuationInformation getEvacuationInformation = getEvacuations.getEvacuationInformation().get(0);
                            showPD();
                            if(getEvacuationInformation.getStatus().equalsIgnoreCase("STARTED")){
                                updateUIAfterEvacuationStatus(getEvacuationInformation.getStatus(),employee.getINTIME(),employee.getOUTTIME(),employee.getWORKINGFROM());
                                btnEvacuation.setText("EvacuationStarted: "+getEvacuationInformation.getStartTime());
                            }
                            else{
                                updateUIAfterEvacuationStatus(getEvacuationInformation.getStatus(),employee.getINTIME(),employee.getOUTTIME(),employee.getWORKINGFROM());
                            }
                        }
                    }
                    else{
                        updateUIAfterEvacuationStatus("",employee.getINTIME(),employee.getOUTTIME(),employee.getWORKINGFROM());
                    }
                }
                hidePD();


            }

        }
    }
    public void updateUIAfterEvacuationStatus(String evacuation, String inTime, String outTime, String workingFrom) {
        boolean isEvacuationStarted = "STARTED".equalsIgnoreCase(evacuation);
        boolean hasInTime = inTime != null && !inTime.isEmpty();
        boolean hasOutTime = outTime != null && !outTime.isEmpty();
        boolean isWorkingFromHome = "HOME".equalsIgnoreCase(workingFrom);

        if (isEvacuationStarted) {
            showRedLabel();
            hideSetPIN();
            if (hasInTime) {
                if (!hasOutTime) {
                    showCheckOutButtonForEvacuation();
                    if (isWorkingFromHome) {
                        showWorkFromHomeIn(inTime);
                    } else {
                        showOfficeIn(inTime);
                    }
                } else {
                    hideCheckInButton();
                    hideCheckOutButton();
                    if (isWorkingFromHome) {
                        showWorkFromHomeInOut(inTime, outTime);
                    } else {
                        showOfficeInOut(inTime, outTime);
                    }
                }
            } else {
                hideCheckInButton();
                hideCheckOutButton();
                hideOfficeInOutText();
                hideWorkFromHomeInOutText();
            }
        } else {
            hideRedLabel();
            if(employee.getPIN()==null || employee.getPIN().isEmpty()){
                hideSetPIN();
            }else {
                showSetPIN();
            }
            hideSetPIN();
            if (hasInTime) {
                if (!hasOutTime) {
                    showCheckOutButton();
                    if (isWorkingFromHome) {
                        showWorkFromHomeIn(inTime);
                    } else {
                        showOfficeIn(inTime);
                    }
                } else {
                    showCheckInButton();
                    if (isWorkingFromHome) {
                        showWorkFromHomeInOut(inTime, outTime);
                    } else {
                        showOfficeInOut(inTime, outTime);
                    }
                }
            } else {
                showCheckInButton();
                hideOfficeInOutText();
                hideWorkFromHomeInOutText();
            }
        }
        hidePD();
    }
    private  void showSetPIN(){
        // code to show setPin

    }
    private  void hideSetPIN(){
        // code to hide setPin
        llSetPin.setVisibility(View.GONE);
    }
    // Placeholder methods for UI operations
    private void showRedLabel() {
        // Code to show red label
        btnEvacuation.setVisibility(View.VISIBLE);
    }

    private void hideRedLabel() {
        // Code to hide red label
        btnEvacuation.setVisibility(View.GONE);
    }

    private void showCheckInButton() {
        // Code to show check-in button
        btnAccess.setVisibility(View.VISIBLE);
        btnAccess.setText("CheckIn");
        setDrawableEnd(R.drawable.btrdownright);
    }

    private void hideCheckInButton() {
        // Code to hide check-in button
        btnAccess.setVisibility(View.GONE);
        tiaccess.setVisibility(View.GONE);

    }

    private void showCheckOutButton() {
        // Code to show check-out button (normal service)
        btnAccess.setVisibility(View.VISIBLE);
        setDrawableEnd(R.drawable.transparant);
        btnAccess.setText("CheckOut");
    }

    private void showCheckOutButtonForEvacuation() {
        // Code to show check-out button (evacuation service)
        btnAccess.setVisibility(View.VISIBLE);
        btnAccess.setText("Evacuation CheckOut");
    }

    private void hideCheckOutButton() {
        // Code to hide check-out button
        btnAccess.setVisibility(View.GONE);
        tiaccess.setVisibility(View.GONE);
    }

    private void showWorkFromHomeIn(String inTime) {
        // Code to show "Work from home IN: [inTime]"
        inOutTime.setVisibility(View.VISIBLE);
        inOutTime.setText("Work from home: " + employModel.getINTIME());
    }

    private void showOfficeIn(String inTime) {
        // Code to show "Office IN: [inTime]"
        inOutTime.setVisibility(View.VISIBLE);
        inOutTime.setText("Office In: " + employModel.getINTIME());

    }

    private void showWorkFromHomeInOut(String inTime, String outTime) {
        // Code to show "Work from home IN: [inTime], OUT: [outTime]"
        inOutTime.setVisibility(View.VISIBLE);
        inOutTime.setText("Work from home: " + employModel.getINTIME() + " " + " " + " Out: " + employModel.getOUTTIME());

    }

    private void showOfficeInOut(String inTime, String outTime) {
        // Code to show "Office IN: [inTime], OUT: [outTime]"
        inOutTime.setVisibility(View.VISIBLE);
        inOutTime.setText("Office In: " + employModel.getINTIME() + " " + " " + " Out: " + employModel.getOUTTIME());
    }

    private void hideOfficeInOutText() {
        // Code to hide Office IN/OUT text
        inOutTime.setVisibility(View.GONE);
    }

    private void hideWorkFromHomeInOutText() {
        // Code to hide Work from home IN/OUT text
        inOutTime.setVisibility(View.GONE);
    }


    private void getInHouseContactsCount()
    {
        String cityName ="";
        if (getStoreCitySelect() != null && getStoreCitySelect().getCityName() != null)
            cityName = getStoreCitySelect().getCityName();
        setOKHTTPAfterLogin();
        setRetrofit(getApp().getTcpIp());
        mainApi = retrofit.create(MainApi.class);
        SoapGetInHouseContactsCountRequestEnvelope soapGetInHouseContactsCountRequestEnvelope = new SoapGetInHouseContactsCountRequestEnvelope();
        GetInHouseContactsCountRequestBody getInHouseContactsCountRequestBody = new GetInHouseContactsCountRequestBody();
        GetInHouseContactsCountRequest getInHouseContactsCountRequest = new GetInHouseContactsCountRequest();
        getInHouseContactsCountRequest.setAuthToken(getAuthTokenPref_Mobile());
        getInHouseContactsCountRequest.setA_sCity(cityName);
        getInHouseContactsCountRequest.setA_sDateDMY(Constants.formatDateFromDMY(Constants.DMY));
        getInHouseContactsCountRequest.setA_sCompany(Constants.COMPANY_NAME);
        getInHouseContactsCountRequestBody.setInHouseContactsCountRequest(getInHouseContactsCountRequest);
        soapGetInHouseContactsCountRequestEnvelope.setBody(getInHouseContactsCountRequestBody);

        Observable<SoapGetInHouseContactsCountResponseEnvelope> lClientDetailsObservable = mainApi.GetInHouseContactsCount(soapGetInHouseContactsCountRequestEnvelope);
        lClientDetailsObservable.
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetInHouseContactsCountValidate, this::onError);
    }
    private void onGetInHouseContactsCountValidate(SoapGetInHouseContactsCountResponseEnvelope soapGetInHouseContactsCountResponseEnvelope) {
        showPD();
        GetInHouseContactsCountResponseBody inHouseContactsCountResponseBody = soapGetInHouseContactsCountResponseEnvelope.getBody();




        if (inHouseContactsCountResponseBody != null
                && inHouseContactsCountResponseBody.getInHouseContactsCountResponse() != null
                && inHouseContactsCountResponseBody.getInHouseContactsCountResponse() != null) { ;

            if (inHouseContactsCountResponseBody
                    .getInHouseContactsCountResponse()!= null) {

                insideUsersCount.setText("People Inside: " +inHouseContactsCountResponseBody
                            .getInHouseContactsCountResponse().getGetInHouseContactsCount().toString());
                hidePD();


            }

        }
    }

    @OnClick({R.id.ivbluno,R.id.tvgate,R.id.view})
    public void OnBluno(View v)
    {

        if(employee!=null && employee.getUSERID()!=null) {

            Intent i = new Intent(this, BlunoActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra(IpAddress.USERID, employee.getUSERID());
            if(employee.getFULLNAME()!=null && employee.getFULLNAME().trim().length()>0)
                i.putExtra(IpAddress.USERNAME, employee.getFULLNAME());
            else if(employee.getFIRSTNAME()!=null && employee.getFIRSTNAME().trim().length()>0)
                i.putExtra(IpAddress.USERNAME, employee.getFIRSTNAME());
            startActivity(i);
        }else
        {

        }
    }

    private void Error(Throwable throwable) {

        hidePD();
    }


    private Bitmap loadInBackgroundEvacuation() {

        try {
            Resources r = getResources();
            multiFormatWriter = new MultiFormatWriter();
            int px = Math.round(TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 180, r.getDisplayMetrics()));
            Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.MARGIN, 0); /* default = 4 */

            width = px;
            height = px;
            BitMatrix bitMatrix = multiFormatWriter.encode(qrcode,
                    BarcodeFormat.QR_CODE, width, height, hints);
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.parseColor("#333333") :
                            Color.parseColor("#D3CDC6"));
                }
            }
        } catch (Exception e) {

        }
        return bitmap;
    }
    private void showProgressDialog() {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this,R.style.MyTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        }
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    private void updateUi(Bitmap bitmap) {
        // Set the QR code image
        img_result.setImageBitmap(bitmap);

        // Set the contact photo
        Uri imageUri = Uri.parse(contactphotourl);
        simpleDraweeView.setImageURI(imageUri);

        // Set the company logo if it exists
        if (companylogourl != null) {
            simpleDraweeViewLogo.setImageURI(companylogourl);
        }

        // Hide the progress dialog
        hideProgressDialog();
    }
    @OnClick(R.id.ivback)
    public void onBack(View v) {
        onBackPressed();
    }

    public Bitmap encodeToQrCode(String text) {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = null;
        try {
            matrix = writer.encode(text, BarcodeFormat.QR_CODE, 600, 600);
        } catch (WriterException ex) {
            ex.printStackTrace();
        }
        bitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < 600; x++) {
            for (int y = 0; y < 600; y++) {
                bitmap.setPixel(x, y, matrix.get(x, y) ? Color.parseColor("#333333") :
                        Color.parseColor("#D3CDC6"));
            }
        }
        return bitmap;
    }


    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public void showDropDownList() {
        btnAccess.showDropDown();
        String[] options = {"Work from home", "Check-In-Office"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, options);
        btnAccess.setAdapter(adapter);
        hidePD();
        btnAccess.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
           // setDrawableEnd(R.drawable.btrdownright);
            //Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_SHORT).show();
            // You can perform actions based on the selected item here
            btnAccess.setText("CheckIn");
            showPD();
            if (selectedItem.equals("Work from home")) {
                // Perform action for "Work from home"
                registerIn("Home");
               // btnAccess.setCompoundDrawablesRelative(null, null, null, null);
            } else if (selectedItem.equals("Check-In-Office")) {
                // Perform action for "Check-in (from office)"
                registerIn("Office");
               // btnAccess.setCompoundDrawablesRelative(null, null, null, null);

            }


        });
    }
    private void unRegisterOutEvacuation() {
        //Toast.makeText(this, "UnChekOut", Toast.LENGTH_SHORT).show();
        setOKHTTPAfterLogin();
        setRetrofit(getApp().getTcpIp());
        mainApi = retrofit.create(MainApi.class);
        SoapUnRegisterOutRequestEnvelope soapUnRegisterOutRequestEnvelope = new SoapUnRegisterOutRequestEnvelope();
        UnRegisterOutRequestBody unregisterOutRequestBody = new UnRegisterOutRequestBody();
        UnRegisterOutRequest unregisterOutRequest = new UnRegisterOutRequest();
        unregisterOutRequest.setAuthToken(getAuthTokenPref_Mobile());
        unregisterOutRequest.setA_sBarcode(getBarcode());
        unregisterOutRequestBody.setUnRegisterOutRequest(unregisterOutRequest);
        soapUnRegisterOutRequestEnvelope.setBody(unregisterOutRequestBody);

        Observable<SoapUnRegisterOutResponseEnvelope> lClientDetailsObservable = mainApi.UnRegisterOut(soapUnRegisterOutRequestEnvelope);
        lClientDetailsObservable.
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onUnRegisterOutValidate, this::onError);
    }
    private void onUnRegisterOutValidate(SoapUnRegisterOutResponseEnvelope soapUnRegisterOutResponseEnvelope) {

        hidePD();

        UnRegisterOutResponseBody unRegisterOutResponseBody = soapUnRegisterOutResponseEnvelope.getBody();

        try {
            if (unRegisterOutResponseBody.getUnRegisterOutResponse()
                    .getUnRegisterOutResult().getError() != null) {

                getAuthToken(false);
                return;
            }
        } catch (Exception e) {

            hidePD();
        }


        if (unRegisterOutResponseBody != null
                && unRegisterOutResponseBody.getUnRegisterOutResponse() != null
                && unRegisterOutResponseBody.getUnRegisterOutResponse().getUnRegisterOutResult() != null) { ;

            if (unRegisterOutResponseBody
                    .getUnRegisterOutResponse().getUnRegisterOutResult().getSTATUS()!= null) {
                if (unRegisterOutResponseBody
                        .getUnRegisterOutResponse().getUnRegisterOutResult().getSTATUS().trim().equalsIgnoreCase("SUCCESS")) {

                    onValidateUser();
                    hidePD();
                    getInHouseContactsCount();
                }

            } else if (unRegisterOutResponseBody.getUnRegisterOutResponse()
                    .getUnRegisterOutResult().getError() != null) {
                showMessage(unRegisterOutResponseBody.getUnRegisterOutResponse()
                        .getUnRegisterOutResult().getError());

            }

        }
    }

    private void registerOut() {
        setOKHTTPAfterLogin();
        setRetrofit(getApp().getTcpIp());
        mainApi = retrofit.create(MainApi.class);
        SoapRegisterOutRequestEnvelope soapRegisterOutRequestEnvelope = new SoapRegisterOutRequestEnvelope();
        RegisterOutRequestBody registerOutRequestBody = new RegisterOutRequestBody();
        RegisterOutRequest registerOutRequest = new RegisterOutRequest();
        registerOutRequest.setAuthToken(getAuthTokenPref_Mobile());
        registerOutRequest.setA_sBarcode(getBarcode());
        registerOutRequestBody.setRegisterOutRequest(registerOutRequest);
        soapRegisterOutRequestEnvelope.setBody(registerOutRequestBody);

        Observable<SoapRegisterOutResponseEnvelope> lClientDetailsObservable = mainApi.RegisterOut(soapRegisterOutRequestEnvelope);
        lClientDetailsObservable.
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onRegisterOutValidate, this::onError);
    }
    private void onRegisterOutValidate(SoapRegisterOutResponseEnvelope soapRegisterOutResponseEnvelope) {

        hidePD();

        RegisterOutResponseBody registerOutResponseBody = soapRegisterOutResponseEnvelope.getBody();

        try {
            if (registerOutResponseBody.getRegisterOutResponse()
                    .getRegisterOutResult().getError() != null) {

                getAuthToken(false);
                return;
            }
        } catch (Exception e) {

            hidePD();
        }


        if (registerOutResponseBody != null
                && registerOutResponseBody.getRegisterOutResponse() != null
                && registerOutResponseBody.getRegisterOutResponse().getRegisterOutResult() != null) { ;

            if (registerOutResponseBody
                    .getRegisterOutResponse().getRegisterOutResult().getSTATUS()!= null) {
                if (registerOutResponseBody
                        .getRegisterOutResponse().getRegisterOutResult().getSTATUS().trim().equalsIgnoreCase("SUCCESS")) {

                    onValidateUser();


                    hidePD();
                    getInHouseContactsCount();
                }

            } else if (registerOutResponseBody.getRegisterOutResponse()
                    .getRegisterOutResult().getError() != null) {
                showMessage(registerOutResponseBody.getRegisterOutResponse()
                        .getRegisterOutResult().getError());

            }

        }

    }

    private void registerIn(String workingFrom) {
        setOKHTTPAfterLogin();
        setRetrofit(getApp().getTcpIp());
        mainApi = retrofit.create(MainApi.class);
        SoapRegisterInRequestEnvelope soapRegisterInRequestEnvelope = new SoapRegisterInRequestEnvelope();
        RegisterInRequestBody registerInRequestBody = new RegisterInRequestBody();
        RegisterInRequest registerInRequest = new RegisterInRequest();
        registerInRequest.setAuthToken(getAuthTokenPref_Mobile());
        registerInRequest.setA_sWorkingFrom(workingFrom);
        registerInRequest.setA_sBarcode(getBarcode());
        registerInRequestBody.setRegisterInRequest(registerInRequest);
        soapRegisterInRequestEnvelope.setBody(registerInRequestBody);

        Observable<SoapRegisterInResponseEnvelope> lClientDetailsObservable = mainApi.RegisterIn(soapRegisterInRequestEnvelope);
        lClientDetailsObservable.
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onRegisterInValidate, this::onError);
    }

    private void onRegisterInValidate(SoapRegisterInResponseEnvelope soapRegisterInResponseEnvelope) {

        hidePD();

        RegisterInResponseBody registerInResponseBody = soapRegisterInResponseEnvelope.getBody();

        try {
            if (registerInResponseBody.getRegisterInResponse()
                    .getRegisterInResult().getError() != null) {

                getAuthToken(false);
                return;
            }
        } catch (Exception e) {

            hidePD();
        }


        if (registerInResponseBody != null
                && registerInResponseBody.getRegisterInResponse() != null
                && registerInResponseBody.getRegisterInResponse().getRegisterInResult() != null) { ;

            if (registerInResponseBody
                    .getRegisterInResponse().getRegisterInResult().getSTATUS()!= null) {
                if (registerInResponseBody
                        .getRegisterInResponse().getRegisterInResult().getSTATUS().trim().equalsIgnoreCase("SUCCESS")) {
                    onValidateUser();
                    hidePD();
                    getInHouseContactsCount();

                }

            } else if (registerInResponseBody.getRegisterInResponse()
                    .getRegisterInResult().getError() != null) {
                showMessage(registerInResponseBody.getRegisterInResponse()
                        .getRegisterInResult().getError());

            }

        }
    }

    private void onValidateUser() {

        showPD();
        setOKHTTPAfterLogin();
        setRetrofit(getApp().getTcpIp());
        mainApi = retrofit.create(MainApi.class);
        SoapValidateUserRequestEnvelope soapValidateUserRequestEnvelope = new SoapValidateUserRequestEnvelope();
        ValidateUserRequestBody validateUserRequestBody = new ValidateUserRequestBody();
        ValidateUserRequest validateUserRequest = new ValidateUserRequest();
        validateUserRequest.setAuthToken(getAuthTokenPref_Mobile());
        validateUserRequest.setA_sEmail(getEmail());
        validateUserRequestBody.setValidateUser(validateUserRequest);
        soapValidateUserRequestEnvelope.setBody(validateUserRequestBody);

        Observable<SoapValidateUserResponseEnvelope> lClientDetailsObservable = mainApi.validateUser(soapValidateUserRequestEnvelope);
        lClientDetailsObservable.
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onValidate, this::onError);

    }

    private void onValidate(SoapValidateUserResponseEnvelope soapValidateUserResponseEnvelope) {

        hidePD();

        ValidateUserResponseBody validateUserResponseBody = soapValidateUserResponseEnvelope.getBody();

        try {
            if (validateUserResponseBody.getValidateUserResponse()
                    .getValidateUserResult().getError() != null) {
                getAuthToken(false);
                return;
            }
        } catch (Exception e) {

            hidePD();
        }


        if (validateUserResponseBody != null
                && validateUserResponseBody.getValidateUserResponse() != null
                && validateUserResponseBody.getValidateUserResponse().getValidateUserResult() != null) {
            employModel = validateUserResponseBody
                    .getValidateUserResponse().getValidateUserResult().getEmployee();

            if (employModel != null) {

                if (employModel.getSTATUS() != null) {
                    if (employModel.getSTATUS().trim().equalsIgnoreCase("SUCCESS")) {
                        storeEmp(mGson.toJson(employModel));
                        employee = getEmp();
                        getEvacuationInformation();
                      /* // String officeStatus = mSharedPreferences.getString(IpAddress.WorkFromStatus,null);

                        if ((employModel.getINTIME() == null ||  employModel.getINTIME().isEmpty() ) && (employModel.getOUTTIME() == null ||  employModel.getOUTTIME().isEmpty() ) && employModel.getWORKINGFROM()==null || employModel.getWORKINGFROM().isEmpty())
                        {
                            btnAccess.setText("CheckIn");
                            setDrawableEnd(R.drawable.btrdownright);
                            inOutTime.setVisibility(View.GONE);
                           *//* if(officeStatus.equalsIgnoreCase("Office")) {
                                inOutTime.setText("Work from home: " + employee.getINTIME() + " " + " " + " Out: " + " ");
                            }else{
                                inOutTime.setText("Office In: " + employee.getINTIME() + " " + " " + " Out: " + " ");
                            }*//*

                        }
                        //(IN is not null and not empty) and (OUT is null or empty) then CheckOut
                        else if((employModel.getINTIME() != null && !employModel.getINTIME().isEmpty()) && (employModel.getOUTTIME() == null|| employModel.getOUTTIME().isEmpty())){
                            btnAccess.setCompoundDrawablesRelative(null, null, null, null);
                            btnAccess.setText("CheckOut");
                            inOutTime.setVisibility(View.VISIBLE);
                            if(employModel.getWORKINGFROM().equalsIgnoreCase("Office")) {
                                inOutTime.setText("Office In: " + employModel.getINTIME());
                            }else {
                                inOutTime.setText("Work from home: " + employModel.getINTIME());
                            }
                        }
                        // IN is not null and not empty and OUT is not null and not empty then UnChekOut.
                        else if((employModel.getINTIME() != null && !employModel.getINTIME().isEmpty()) && (employModel.getOUTTIME() != null && !employModel.getOUTTIME().isEmpty())) {
                            btnAccess.setText("CheckIn");
                            setDrawableEnd(R.drawable.btrdownright);
                            inOutTime.setVisibility(View.VISIBLE);
                            if(employModel.getWORKINGFROM().equalsIgnoreCase("Home")) {
                                inOutTime.setText("Work from home: " + employModel.getINTIME() + " " + " " + " Out: " + employModel.getOUTTIME());
                            }else{
                                inOutTime.setText("Office In: " + employModel.getINTIME() + " " + " " + " Out: " + employModel.getOUTTIME());
                            }
                        }
                       *//* else{
                            btnAccess.setText("CheckIn");
                            setDrawableEnd(R.drawable.btrdownright);
                            inOutTime.setVisibility(View.VISIBLE);
                            if(officeStatus.equalsIgnoreCase("Office")) {
                                inOutTime.setText("Work from home: " + employModel.getINTIME() + " " + " " + " Out: " + employModel.getOUTTIME());
                            }else{
                                inOutTime.setText("Office In: " + employModel.getINTIME() + " " + " " + " Out: " + employModel.getOUTTIME());
                            }
                        }*//*
                        if(btnAccess.getText().toString().equalsIgnoreCase("CheckIn")){
                            if(checkInEvacuationInformationStatus){
                                btnEvacuation.setVisibility(View.VISIBLE);
                            }
                            else{
                                btnEvacuation.setVisibility(View.GONE);
                            }
                        }else{
                            btnEvacuation.setVisibility(View.GONE);
                        }

*/
                    }


                }


            } else if (validateUserResponseBody.getValidateUserResponse()
                    .getValidateUserResult().getError() != null) {
                showMessage(validateUserResponseBody.getValidateUserResponse()
                        .getValidateUserResult().getError()
                        .get_error());

            }


        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()) {
            case R.id.et_setPin:
                switch (actionId) {

                    case EditorInfo.IME_ACTION_DONE:
                        showPD();
                        UpdatePIN(et_setPin.getText().toString());
                        return true;
                    default:
                        return false;

                }
        }
        return false;
    }
}

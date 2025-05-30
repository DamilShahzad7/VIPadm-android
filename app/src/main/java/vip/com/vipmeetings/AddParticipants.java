package vip.com.vipmeetings;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vip.com.vipmeetings.body.AddParticipantsRequestBody;
import vip.com.vipmeetings.body.AddParticipantsResponseBody;
import vip.com.vipmeetings.body.DeleteMeetingRequestBody;
import vip.com.vipmeetings.body.DeleteMeetingResponseBody;
import vip.com.vipmeetings.body.GetClientRequestBody;
import vip.com.vipmeetings.body.GetContactsRequestBody;
import vip.com.vipmeetings.body.GetExcelDataRequestBody;
import vip.com.vipmeetings.body.GetFormerVisitorsRequestBody;
import vip.com.vipmeetings.envelope.SoapAddParticipantsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapAddParticipantsResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapDeleteMeetingRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapDeleteMeetingResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetClient;
import vip.com.vipmeetings.envelope.SoapGetClientResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetContactsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetContactsResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetExcelDataRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetExcelDataResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetFormerVisitorsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetFormerVisitorsResponseEnvelope;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.excel.ExcelReaderListener;
import vip.com.vipmeetings.excel.JSExcelReader;
import vip.com.vipmeetings.interfaces.ContactsApi;
import vip.com.vipmeetings.interfaces.MainApi;
import vip.com.vipmeetings.models.Client;
import vip.com.vipmeetings.models.Contact;
import vip.com.vipmeetings.models.ContactManual;
import vip.com.vipmeetings.models.Contacts;
import vip.com.vipmeetings.models.GetExcelData;
import vip.com.vipmeetings.models.RecordContact;
import vip.com.vipmeetings.models.Status;
import vip.com.vipmeetings.models.Visitor;
import vip.com.vipmeetings.models.Visitors;
import vip.com.vipmeetings.request.AddParticipant;
import vip.com.vipmeetings.request.DeleteMeeting;
import vip.com.vipmeetings.request.GetClient;
import vip.com.vipmeetings.request.GetContacts;
import vip.com.vipmeetings.request.GetFormerVisitors;
import vip.com.vipmeetings.response.AddParticipantsResult;
import vip.com.vipmeetings.response.GetExcelDataResult;
import vip.com.vipmeetings.utilities.Constants;

import static vip.com.vipmeetings.utilities.Constants.MY_PERMISSIONS_REQUEST_READ_LOCATION;
import static vip.com.vipmeetings.utilities.Constants.PHONEURI;
import static vip.com.vipmeetings.utilities.Constants.allContactList;
import static vip.com.vipmeetings.utilities.Constants.visContactList1;


/**
 * Created by Srinath on 15/06/17.
 */

public class AddParticipants extends BaseActivity implements ExcelReaderListener {


    private static final int FILE_SELECT_CODE = 111;
    private static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 1111;
    @BindView(R.id.tvmanual)
    TextView tvmanual;
    @BindView(R.id.llcontact)
    LinearLayout llcontact;
    @BindView(R.id.tvcontact)
    TextView tvcontact;
    @BindView(R.id.plusIconData)
    LinearLayout plusIconData;
    List<ContactManual> contactManualList;
    List<Contact> contactsList;
    Adapter adapter;
    @BindView(R.id.etsearch)
    EditText etsearch;

    @BindView(R.id.rvcontacts)
    RecyclerView rvcontacts;

    @BindView(R.id.tvbottom)
    TextView tvbottom;

    @BindView(R.id.tvback)
    ImageView tvback;

    @BindView(R.id.tvexcel)
    TextView tvexcel;

    @BindView(R.id.ivaddparticipant)
    ImageView ivaddparticipant;
    DateTimeModel dateTimeModel;

    Intent intent;
    List<Contact> excelcontacts = new ArrayList<>();

    @BindView(R.id.rveditcontacts)
    RecyclerView rveditcontacts;
    ContactAdapter contactAdapter;

    @BindView(R.id.btn_add)
    Button btnAdd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addparticipant);
        ButterKnife.bind(this);

        tvcontact.setMovementMethod(new ScrollingMovementMethod());
        if (getStoreCitySelect() != null
                && getStoreCitySelect().getSkipRoom() != null
                && getStoreCitySelect().getSkipRoom().equalsIgnoreCase("True")) {
            tvbottom.setVisibility(View.VISIBLE);
            plusIconData.setVisibility(View.VISIBLE);
        } else if (getStoreCitySelect() != null
                && getStoreCitySelect().getRoomCount() != null
                && getStoreCitySelect().getRoomCount().equalsIgnoreCase("0")) {
            tvbottom.setVisibility(View.VISIBLE);
            plusIconData.setVisibility(View.VISIBLE);
        } else {
            tvbottom.setVisibility(View.GONE);
            plusIconData.setVisibility(View.GONE);
        }

        intent = getIntent();
        if (intent.hasExtra(IpAddress.BACK)) {
            tvback.setVisibility(View.GONE);
        } else {
            tvback.setVisibility(View.VISIBLE);
            tvback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        dateTimeModel = mGson.fromJson(intent.getStringExtra(Constants.DATETIMEMODEL),
                DateTimeModel.class);

        if (allContactList == null) {
            allContactList = new ArrayList<>();
            allContactList.clear();
        }
        contactsList = new ArrayList<>();
        adapter = new Adapter();
        rvcontacts.setVisibility(View.GONE);
        tvbottom.setVisibility(View.VISIBLE);
        plusIconData.setVisibility(View.VISIBLE);
        rvcontacts.setLayoutManager(new LinearLayoutManager(this));
        rvcontacts.setAdapter(adapter);
        contactManualList = new ArrayList<>();


        etsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (isPatient()) {

                    if (contactManualList != null && (contactManualList.size() < 1)) {

                        if (editable.length() == 0) {

                            llcontact.setVisibility(View.GONE);
                            rvcontacts.setVisibility(View.GONE);
                            tvbottom.setVisibility(View.VISIBLE);
                            contactsList.clear();
                            adapter.notifyDataSetChanged();
                            refreshTv();
                        } else {

                            onDone(editable.toString());
                        }
                    } else {

                        if (etsearch.getText().length() > 0) {

                            showMessage("Add Patients!", "You are not able to add more patients for single A" +
                                    "ppointment. Please remove added patient and add a new Patient");
                        }
                    }
                } else {

                    if (editable.length() == 0) {

                        btnAdd.setVisibility(View.GONE);
                        llcontact.setVisibility(View.GONE);


                        rvcontacts.setVisibility(View.GONE);
                        tvbottom.setVisibility(View.VISIBLE);
                        plusIconData.setVisibility(View.VISIBLE);
                        contactsList.clear();
                        adapter.notifyDataSetChanged();

                        refreshTv();

                    } else {
                    if(etsearch.getText().length() > 5){
                       // onDone(editable.toString());
                        btnAdd.setVisibility(View.VISIBLE);
                    }
                    else {
                        btnAdd.setVisibility(View.GONE);
                       // onDone(editable.toString());
                    }
                    }
                }
            }
        });

        etsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {


                switch (i) {
                    case EditorInfo.IME_ACTION_DONE:

                        if (isPatient()) {


                        } else {
                            if (etsearch.getText().toString().trim().length() > 5) {

                                onDone(etsearch.getText().toString().trim());
                            } else {
                                hideKeyboard(textView);
                                rvcontacts.setVisibility(View.GONE);
                                tvbottom.setVisibility(View.VISIBLE);
                                plusIconData.setVisibility(View.VISIBLE);
                                contactsList.clear();
                                adapter.notifyDataSetChanged();
                            }
                            return true;

                        }

                    default:
                        return false;

                }
            }

        });
        checkLocationPermission(getApp().isContactRequest());


        contactAdapter = new ContactAdapter();
        rveditcontacts.setLayoutManager(new LinearLayoutManager(this));
        rveditcontacts.setAdapter(contactAdapter);


        tvbottom.setText(getSpannedText(getString(R.string.addparticipant)));
        // tvbottom.setText(Html.fromHtml(getString(R.string.addparticipant)));
    }

    private Spanned getSpannedText(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(text);
        }
    }


    private void refreshTv() {

        if (contactManualList != null && contactManualList.size() > 0) {

            // llcontact.setVisibility(View.VISIBLE);

            llcontact.setVisibility(View.GONE);
            rveditcontacts.setVisibility(View.VISIBLE);
            rvcontacts.setVisibility(View.GONE);
            plusIconData.setVisibility(View.GONE);
        } else {
            rveditcontacts.setVisibility(View.GONE);
            rvcontacts.setVisibility(View.GONE);
            llcontact.setVisibility(View.GONE);
            plusIconData.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onBackPressed() {

        if (intent != null && intent.hasExtra(IpAddress.BACK)) {

        } else
            super.onBackPressed();
    }

    @Override
    protected void onStop() {
        try {
            hideSoft();
        } catch (Exception e) {

        }
        super.onStop();
    }

    private void getAllContacts() {


        if (allContactList == null || allContactList.size() == 0) {
            showPD(this);
            allContactList = new ArrayList<>();
            allContactList.clear();
            setRetrofit(getApp().getTcpIp());
            contactsApi = retrofit.create(ContactsApi.class);

            SoapGetContactsRequestEnvelope soapGetContactsRequestEnvelope = new SoapGetContactsRequestEnvelope();

            GetContactsRequestBody getContactsRequestBody = new GetContactsRequestBody();

            GetContacts getContacts = new GetContacts();
            getContacts.setAuthToken(getAuthTokenPref_Mobile());
            getContacts.setA_sUserEmail(getEmail());
            getContactsRequestBody.setGetContacts(getContacts);
            soapGetContactsRequestEnvelope.setBody(getContactsRequestBody);


            SoapGetFormerVisitorsRequestEnvelope soapGetFormerVisitorsRequestEnvelope = new SoapGetFormerVisitorsRequestEnvelope();

            GetFormerVisitorsRequestBody getFormerVisitorsRequestBody = new GetFormerVisitorsRequestBody();

            GetFormerVisitors getFormerVisitors = new GetFormerVisitors();
            getFormerVisitors.setAuthToken(getAuthTokenPref_Mobile());
            getFormerVisitors.setA_sHostMail(getEmail());
            getFormerVisitorsRequestBody.setGetFormerVisitors(getFormerVisitors);
            soapGetFormerVisitorsRequestEnvelope.setBody(getFormerVisitorsRequestBody);


            Observable<SoapGetContactsResponseEnvelope> lClientDetailsObservable =
                    contactsApi.getContacts2(soapGetContactsRequestEnvelope);

            Observable<SoapGetFormerVisitorsResponseEnvelope> contactsObservable =
                    contactsApi.getFormerVisitors(soapGetFormerVisitorsRequestEnvelope);


            int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                    android.Manifest.permission.READ_CONTACTS);


            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

                lClientDetailsObservable.
                        subscribeOn(Schedulers.newThread())
                        .map(this::soapContacts)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(this::addContacts)
                        .map(this::getPhoneContacts)
                        .subscribeOn(Schedulers.newThread())
                        .flatMap(contacts -> contactsObservable.
                                subscribeOn(Schedulers.newThread())
                                .map(this::soapVisitors)
                                .observeOn(AndroidSchedulers.mainThread()))
                        .subscribe(this::addVisitors, this::onError);
            } else {
                lClientDetailsObservable.
                        subscribeOn(Schedulers.newThread())
                        .map(this::soapContacts)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(this::addContacts)
                        //  .map(this::getPhoneContacts)
                        .subscribeOn(Schedulers.newThread())
                        .flatMap(contacts -> contactsObservable.
                                subscribeOn(Schedulers.newThread())
                                .map(this::soapVisitors)
                                .observeOn(AndroidSchedulers.mainThread()))
                        .subscribe(this::addVisitors, this::onError);
            }
        } else {

            //showToast(getString(R.string.empty_contacts));
        }
    }

    private Contacts soapContacts(SoapGetContactsResponseEnvelope soapGetContactsResponseEnvelope) {


        Contacts contacts = soapGetContactsResponseEnvelope.getBody().getGetContactsResponse().getGetContactsResult().getContacts();
        return contacts;
    }

    private Visitors soapVisitors(SoapGetFormerVisitorsResponseEnvelope soapGetFormerVisitorsResponseEnvelope) {


        Visitors contacts = soapGetFormerVisitorsResponseEnvelope.getBody().getGetFormerVisitorsResponse()
                .getGetFormerVisitorsResult().getVisitors();
        return contacts;
    }

    private void addVisitors(Visitors visitors) {

        hidePD();
        if (visitors != null && visitors.getVisitor() != null &&
                visitors.getVisitor().size() > 0) {


            for (Visitor visitor : visitors.getVisitor()) {
                Contact contacts = new Contact();

                contacts.setMobile(visitor.getMobile());
                contacts.setCompany(visitor.getCompanyName());
                contacts.setName(visitor.getName());
                contacts.setEmail(visitor.getEMail());
                contacts.setType(visitor.getType());

                if (allContactList.contains(contacts)) {

                } else
                    allContactList.add(contacts);

            }

        }


    }


    private Contacts addContacts(Contacts contacts) {


        if (contacts != null && contacts.getContact() != null && contacts.getContact().size() > 0) {

            // allContactList.addAll(contacts.getContact());

            for (Contact contact : contacts.getContact()) {
                if (allContactList.contains(contact)) {

                } else
                    allContactList.add(contact);
            }
        }


        return contacts;
    }


    public Contacts getPhoneContacts(Contacts contacts) {


        if (isPatient()) {

            return contacts;
        } else {
            try {
                Cursor phones = getContentResolver().query(PHONEURI,
                        null, null, null, null);

                if (phones != null) {
                    while (phones.moveToNext()) {

                        Contact contact = new Contact();

                        contact.setType("My Contact");
                        contact.setName(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds
                                .Phone.DISPLAY_NAME)));
                        contact.setMobile(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));


                        if (allContactList.contains(contact)) {

                        } else
                            allContactList.add(contact);


                    }
                    phones.close();
                }
            } catch (Exception e) {

                Constants.e("err", e.toString());
            }

            return contacts;
        }
    }


    @OnClick(R.id.ivaddparticipant)
    public void addParticipant(View v) {


        hideSoft();
        etsearch.getText().clear();
       // contactsList.clear();
        adapter.notifyDataSetChanged();
        Intent i = new Intent(this, AddParticipantsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra(Constants.CONTACTLIST, mGson.toJson(contactManualList));
        startActivityForResult(i, Constants.REQUESTADD);

    }

    @OnClick(R.id.ivhome)
    public void onHome(View v) {

        if (isPatient()) {

            deleteMeeting(getMeetingId());

        } else

            gotoHome();

    }

    private void deleteMeeting(String meetingID) {

        if (meetingID != null && meetingID.trim().length() > 0) {
            showPD();
            setRetrofit(getApp().getTcpIp());
            mainApi = retrofit.create(MainApi.class);
            SoapDeleteMeetingRequestEnvelope soapDeleteMeetingRequestEnvelope =
                    new SoapDeleteMeetingRequestEnvelope();
            DeleteMeetingRequestBody deleteMeetingRequestBody =
                    new DeleteMeetingRequestBody();
            DeleteMeeting deleteMeeting = new DeleteMeeting();
            deleteMeeting.setA_sMeetingId(meetingID);

            deleteMeeting.setAuthToken(getAuthTokenPref_Mobile());

            deleteMeetingRequestBody.setDeleteMeeting(deleteMeeting);

            soapDeleteMeetingRequestEnvelope.setBody(deleteMeetingRequestBody);

            Observable<SoapDeleteMeetingResponseEnvelope> lClientDetailsObservable = mainApi.deleteMeeting(soapDeleteMeetingRequestEnvelope);

            lClientDetailsObservable.
                    subscribeOn(Schedulers.newThread())
                    .map(AddParticipants.this::onSoapDeleteMeeting)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(AddParticipants.this::onDeleteMeeting,
                            AddParticipants.this::onError);
        } else {
            gotoHome();
        }
    }


    private String onSoapDeleteMeeting(SoapDeleteMeetingResponseEnvelope soapDeleteMeetingResponseEnvelope) {


        DeleteMeetingResponseBody deleteMeetingResponseBody = soapDeleteMeetingResponseEnvelope.getBody();

        if (deleteMeetingResponseBody.getDeleteMeetingResponse().getDeleteMeetingResult() != null
                && deleteMeetingResponseBody.getDeleteMeetingResponse()
                .getDeleteMeetingResult().getStatus() != null)
            return deleteMeetingResponseBody.getDeleteMeetingResponse().getDeleteMeetingResult()
                    .getStatus().get_status();

        else if (deleteMeetingResponseBody.getDeleteMeetingResponse().getDeleteMeetingResult() != null
                && deleteMeetingResponseBody.getDeleteMeetingResponse().
                getDeleteMeetingResult().getError() != null)

            return deleteMeetingResponseBody.getDeleteMeetingResponse().getDeleteMeetingResult()
                    .getError().get_error();


        return null;
    }

    private void onDeleteMeeting(String status) {

        hidePD();
        if (status != null && status.equalsIgnoreCase(IpAddress.SUCCESS)) {

            gotoHome();

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(status + "");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            builder.show();
        }
    }


    private void checkLocationPermission(boolean isContatRequest) {

        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.READ_CONTACTS);


        if (isContatRequest) {


            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_LOCATION);

        } else {

            permissionEnable();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    permissionEnable();

                } else {

                    getApp().setContactRequest(false);


                }

            }
            break;

            case MY_PERMISSIONS_REQUEST_READ_STORAGE:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onStoragePermission();
                }
                break;


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void onStoragePermission() {

        String[] mimeTypes = {"application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "application/x-excel"// .xls & .xlsx
        };

        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            //intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            intent.setDataAndType(null, "application/vnd.ms-excel");
            // intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));

        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/x-excel");
        }

        // intent.setType("application/x-excel");
        // intent.setType("application/x-excel");
        // intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void permissionEnable() {


        //1253
        getClient();
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

            Client client = soapGetClientResponseEnvelope.getBody().getGetClientResponse().getGetClientResult().getClient();
            if (client != null) {

                isPatient = client.isPatientSystem();
                storeIsPatient(isPatient);
                if (isPatient) {
                    tvexcel.setVisibility(View.INVISIBLE);
                    ivaddparticipant.setVisibility(View.GONE);

                    getFormerVisitor();
                } else {

                    ivaddparticipant.setVisibility(View.VISIBLE);
                    tvexcel.setVisibility(View.INVISIBLE);
                    getAllContacts();
                }

            }
        } catch (Exception e) {

        }

    }

    private void getFormerVisitor() {


        if (allContactList == null || allContactList.size() == 0) {
            showPD(this);
            allContactList = new ArrayList<>();
            allContactList.clear();
            setRetrofit(getApp().getTcpIp());
            contactsApi = retrofit.create(ContactsApi.class);

            SoapGetContactsRequestEnvelope soapGetContactsRequestEnvelope = new SoapGetContactsRequestEnvelope();

            GetContactsRequestBody getContactsRequestBody = new GetContactsRequestBody();

            GetContacts getContacts = new GetContacts();
            getContacts.setAuthToken(getAuthTokenPref_Mobile());
            getContacts.setA_sUserEmail(getEmail());
            getContactsRequestBody.setGetContacts(getContacts);
            soapGetContactsRequestEnvelope.setBody(getContactsRequestBody);


            SoapGetFormerVisitorsRequestEnvelope soapGetFormerVisitorsRequestEnvelope = new SoapGetFormerVisitorsRequestEnvelope();

            GetFormerVisitorsRequestBody getFormerVisitorsRequestBody = new GetFormerVisitorsRequestBody();

            GetFormerVisitors getFormerVisitors = new GetFormerVisitors();
            getFormerVisitors.setAuthToken(getAuthTokenPref_Mobile());
            getFormerVisitors.setA_sHostMail(getEmail());
            getFormerVisitorsRequestBody.setGetFormerVisitors(getFormerVisitors);
            soapGetFormerVisitorsRequestEnvelope.setBody(getFormerVisitorsRequestBody);

            Observable<SoapGetFormerVisitorsResponseEnvelope> contactsObservable =
                    contactsApi.getFormerVisitors(soapGetFormerVisitorsRequestEnvelope);


            contactsObservable.
                    subscribeOn(Schedulers.newThread())
                    .map(this::soapVisitors)
                    // .map(this::addVisitors)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::addVisitors, this::onError);
        }
    }


    @OnClick(R.id.tvback)
    public void onBack(View v) {

        onBackPressed();
    }


    @OnClick(R.id.tvnext)
    public void onNext(View v) {


        if (contactManualList != null && contactManualList.size() > 0) {

            StringBuilder sb = new StringBuilder();
            for (ContactManual c : contactManualList) {


                String emailid = "''";

                if (c.getEmailId() != null &&
                        !c.getEmailId().trim().isEmpty()
                        &&
                        !c.getEmailId().trim().equalsIgnoreCase("''")) {
                    emailid = "'" + c.getEmailId() + "'";
                } else {
                    emailid = "''";
                }

                String City = "''";

                if (c.getCity() != null &&
                        !c.getCity().trim().isEmpty() &&
                        !c.getCity().trim().equalsIgnoreCase("''")) {
                    City = "'" + c.getCity() + "'";
                } else if(getStoreCitySelect()!=null)
                {

                    City="'"+getStoreCitySelect().getCityName()+"'";

                }

                else if(dateTimeModel.getRoomName()!=null)
                {

                    City="'"+dateTimeModel.getRoomName()+"'";
                }

                else {
                    City = "''";
                }


                String company = "''";

                if (c.getCompany() != null &&
                        !c.getCompany().trim().isEmpty()
                        &&
                        !c.getCompany().trim().equalsIgnoreCase("''")) {
                    company = "'" + c.getCompany() + "'";
                } else {
                    company = "''";
                }

                String surname = "";

                if (c.getSurName() != null &&
                        !c.getSurName().trim().isEmpty() &&
                        !c.getSurName().trim().equalsIgnoreCase("''")) {
                    surname = c.getSurName();
                } else {
                    surname = "";
                }


                String firstname = "";


                if (c.getFirstName() != null && !c.getFirstName().isEmpty() &&
                        !c.getFirstName().trim().equalsIgnoreCase("''")) {
                    if (!surname.isEmpty()) {
                        firstname = "'" + c.getFirstName() + " " + surname + "'";
                    } else {
                        firstname = "'" + c.getFirstName() + "'";
                    }
                } else {

                    if (!surname.isEmpty()) {
                        firstname = "'" + surname + "'";
                    } else {
                        firstname = "''";
                    }
                }


                String country = "''";

                if (c.getCountry() != null &&
                        !c.getCountry().isEmpty() &&
                        !c.getCountry().trim().equalsIgnoreCase("''")) {
                    country = "'" + c.getCountry() + "'";
                } else {
                    country = "''";
                }

                String mobile = "''";

                if (c.getMobileNo() != null &&
                        !c.getMobileNo().isEmpty() &&
                        !c.getMobileNo().trim().equalsIgnoreCase("''")) {
                    mobile = "'" + c.getMobileNo() + "'";
                } else {
                    mobile = "''";
                }

                String Parking = "''";
                if (c.getCountry() != null &&
                        !c.getCountry().trim().isEmpty()
                        &&
                        !c.getCountry().trim().equalsIgnoreCase("''")) {
                    Parking = "'" + "Yes" + "'";
                } else {
                    Parking = "'Yes'";
                }

                String visitor="'"+getIsPatient()+"'";

                String invitation="'"+c.getSendInvitation()+"'";

                //    Name='%@',Company='%@',Mobile='%@',EMail='%@',MeetingId='%@',City='%@',Country='%@',Parking='%@',SendInvitation='YES'|

                String secInstruction = "'" + dateTimeModel.getSecurityInstruction() + "'";
                String joinOption =  "'" + dateTimeModel.getJoinOption() + "'";
                sb.append("Name=" + firstname + "," +
                        "Company=" + company + "," +
                        "Mobile=" + mobile.replaceAll("\\s+", "") + "," +
                        "EMail=" + emailid + "," +
                        "MeetingId='" + getMeetingId().trim() + "'," +
                        "City=" + City + "," +
                        "Country=" + country + "," +
                        "Parking=" + Parking + "," +
                        "SendInvitation=" +invitation+ "," +
                        "VisitorType=" + visitor + "," +
                        "SendSecurityInfoSms=" + secInstruction + "," +
                        "JoinOption=" + joinOption
                );

                sb.append("|");
            }

            showPD();
            String text = sb.toString().substring(0, sb.toString().length() - 1);
            Constants.e("text", text);
            setRetrofit(getApp().getTcpIp());
            MainApi mainApi = retrofit.create(MainApi.class);

            //retrofit.converterFactories().add("")

            SoapAddParticipantsRequestEnvelope
                    soapAddParticipantsRequestEnvelope = new SoapAddParticipantsRequestEnvelope();


            AddParticipantsRequestBody addParticipantsRequestBody = new AddParticipantsRequestBody();

            AddParticipant addParticipant = new AddParticipant();
            addParticipant.setA_sUserEmail(getEmail());
            addParticipant.setAuthToken(getAuthTokenPref_Mobile());
           // addParticipant.setVisitorType(getIsPatient());
            addParticipant.setA_sParticipantsText(text);


            IpAddress.e("ADDPART", mGson.toJson(addParticipant));

            addParticipantsRequestBody.setAddParticipants(addParticipant);


            soapAddParticipantsRequestEnvelope.setBody(addParticipantsRequestBody);

            Observable<SoapAddParticipantsResponseEnvelope> lClientDetailsObservable =
                    mainApi.addParticipants(soapAddParticipantsRequestEnvelope);

            lClientDetailsObservable.
                    subscribeOn(Schedulers.newThread())
                    .map(this::onSoapAddparticipant)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::addParticipant, this::onError);

        } else {


            Toast.makeText(this, getString(R.string.add_participant), Toast.LENGTH_SHORT).show();

        }


    }

    private AddParticipantsResult onSoapAddparticipant(SoapAddParticipantsResponseEnvelope soapAddParticipantsResponseEnvelope) {


        AddParticipantsResponseBody addParticipantsResponseBody = soapAddParticipantsResponseEnvelope.getBody();

        return addParticipantsResponseBody.getAddParticipantsResponse().getAddParticipantsResult();
    }

    private void addParticipant(AddParticipantsResult status1) {


        hidePD();
        Status status = status1.getStatus();
        if (status1.getError() != null && status1.getError().get_error() != null) {
            Toast.makeText(this, status1.getError().get_error() + "", Toast.LENGTH_SHORT).show();

        } else if (status != null && status.get_status().equalsIgnoreCase("SUCCESS")) {
            if (getStoreCitySelect() != null
                    && getStoreCitySelect().getUseCatering() != null
                    && getStoreCitySelect().getUseCatering().equalsIgnoreCase("TRUE")) {

                hideSoft();
                Intent intent = new Intent(this, OrderFoodActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.MEETINGID, getMeetingId());
                intent.putExtra(Constants.DATETIMEMODEL, mGson.toJson(dateTimeModel));
                startActivity(intent);
            } else if (getStoreCitySelect() != null
                    && getStoreCitySelect().getUseCatering() != null
                    && getStoreCitySelect().getUseCatering().equalsIgnoreCase("FALSE")) {
                hideSoft();
                Intent intent = new Intent(this, SkipRoomActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.MEETINGID, getMeetingId());
                intent.putExtra(Constants.DATETIMEMODEL, mGson.toJson(dateTimeModel));
                startActivity(intent);
            } else {
                hideSoft();Intent intent = new Intent(this, SkipRoomActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.MEETINGID, getMeetingId());
                intent.putExtra(Constants.DATETIMEMODEL, mGson.toJson(dateTimeModel));
                startActivity(intent);

            }

        } else {
            Toast.makeText(this, status.get_status() + "", Toast.LENGTH_SHORT).show();
        }
    }


    private void onDone(String trim) {


        Constants.e("no1", "contacts reading");


        contactsList.clear();

        if (isPatient()) {

            if (visContactList1 != null && visContactList1.size() > 0)
                for (Contact c : visContactList1) {

                    if (c.getName().trim().toLowerCase().startsWith(trim.toLowerCase()) || c.getMobile().trim().toLowerCase().startsWith(trim.toLowerCase()) || c.getEmail().trim().toLowerCase().startsWith(trim.toLowerCase())) {
                        contactsList.add(c);
                    }
                }


        } else {
            for (Contact c : allContactList) {

                if (!c.getName().isEmpty()&&c.getName().trim().toLowerCase().startsWith(trim.toLowerCase()) || !c.getMobile().isEmpty()&&c.getMobile().trim().toLowerCase().startsWith(trim.toLowerCase()) || c.getEmail()!=null&&!c.getEmail().isEmpty() && c.getEmail().trim().toLowerCase().startsWith(trim.toLowerCase())) {
                    contactsList.add(c);
                }
            }
        }
        if (contactsList != null && contactsList.size() > 0) {
            llcontact.setVisibility(View.GONE);
            rvcontacts.setVisibility(View.VISIBLE);
            tvbottom.setVisibility(View.GONE);
            plusIconData.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();

            rveditcontacts.setVisibility(View.GONE);


        } else {
            rveditcontacts.setVisibility(View.VISIBLE);
            contactsList.clear();
            adapter.notifyDataSetChanged();
            Constants.e("no", "contacts");
        }

    }
    @OnClick(R.id.btn_add)
    public void onBtnAdd(View v) {
        if (isPatient() && contactManualList != null && contactManualList.size() > 0) {
            showMessage("Add Patients!", "You are not able to add more patients for single Appointment. Please remove added patient and add a new Patient");
        } else {
            if (etsearch != null && !etsearch.getText().toString().isEmpty()) {
                String searchText = etsearch.getText().toString().trim();

                if (isValidEmail(searchText) || isValidPhone(searchText)) {
                    boolean existsInAllContacts = false;
                    boolean existsInManualContacts = false;

                    // Check if the search text exists in the contact list
                    for (Contact c : allContactList) {
                        if ((isValidEmail(searchText) && c.getEmail() != null && c.getEmail().equalsIgnoreCase(searchText))
                                || (isValidPhone(searchText) && c.getMobile() != null && c.getMobile().equalsIgnoreCase(searchText))) {
                            existsInAllContacts = true;
                            break;
                        }
                    }

                    // Check if the search text exists in the manual contact list
                    for (ContactManual cm : contactManualList) {
                        if ((isValidEmail(searchText) && cm.getEmailId() != null && cm.getEmailId().equalsIgnoreCase(searchText))
                                || (isValidPhone(searchText) && cm.getMobileNo() != null && cm.getMobileNo().equalsIgnoreCase(searchText))) {
                            existsInManualContacts = true;
                            break;
                        }
                    }

                    if (!existsInAllContacts && !existsInManualContacts) {
                        // Create a new ContactManual object and add it to contactManualList
                        ContactManual contactManual = new ContactManual();
                        contactManual.setCompany("''");
                        contactManual.setCity("''");
                        contactManual.setCountry("''");
                        contactManual.setMeetingId(getMeetingId());
                        contactManual.setSendInvitation("YES");
                        contactManual.setSurName("''");
                        contactManual.setFirstName("''"); // Assuming searchText is the name
                        contactManual.setMobileNo(isValidPhone(searchText) ? searchText : null);
                        contactManual.setEmailId(isValidEmail(searchText) ? searchText : null);
                        contactManual.setType("manual");
                        // Add contactManual to the list
                        contactManualList.add(contactManual);

                        // Notify the adapter of changes
                        contactAdapter.notifyDataSetChanged();

                        // Clear search text and hide keyboard
                        hideSoft();
                        etsearch.getText().clear();

                        // Update UI as needed
                        setTVContact();
                    } else if (existsInManualContacts) {
                        Toast.makeText(this, "This contact already exists in the manual contact list.", Toast.LENGTH_LONG).show();
                    } else {
                        onDone(etsearch.getText().toString());
                        Toast.makeText(this, "This contact already exists in the all contact list.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Please enter a valid Email or Phone Number", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Please enter a valid Email or Phone Number", Toast.LENGTH_LONG).show();
            }
        }
    }

   /* @OnClick(R.id.tvmanual)
    public void onManual(View v) {


        if (isPatient() && contactManualList != null && contactManualList.size() > 0) {

            showMessage("Add Patients!", "You are not able to add more patients for single A" +
                    "ppointment. Please remove added patient and add a new Patient");

        } else {

            hideSoft();
            Intent i = new Intent(this, AddManuallyActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra(Constants.CONTACTLIST, mGson.toJson(contactManualList));
            startActivityForResult(i, Constants.REQUESTADD);
        }

    }*/


    @OnClick(R.id.tvedit)
    public void onEdit(View v) {

        if (contactManualList != null && contactManualList.size() > 0) {
            hideSoft();
            Intent i = new Intent(this, EditContactActivity.class);
            i.putExtra(Constants.CONTACTLIST, mGson.toJson(contactManualList));
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(i, Constants.REQUESTEDIT);
        } else {

            refreshTv();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case Constants.EDITCONTACT:
                    if (data != null && data.hasExtra(Constants.POS) && data.hasExtra(Constants.ADDCONTACT)) {
                        int position = data.getIntExtra(Constants.POS, 0);
                        ContactManual editedContact = mGson.fromJson(data.getStringExtra(Constants.ADDCONTACT), ContactManual.class);

                        if (position >= 0 && position < contactManualList.size()) {
                            contactManualList.set(position, editedContact);
                            setTVContact();
                        }
                    }
                    break;

                case FILE_SELECT_CODE:
                    if (data != null) {
                        Uri uri = data.getData();
                        IpAddress.e("EXCEL", "File Uri: " + uri.toString());
                        // Handle file selection or upload as needed
                        sendFile(uri);
                    }
                    break;


                case Constants.REQUESTADD:
                    if (data != null && data.hasExtra(Constants.ADDCONTACT)) {
                        try {
                            String array = data.getStringExtra(Constants.ADDCONTACT);
                            List<ContactManual> contactManualList1 = mGson.fromJson(array, new TypeToken<List<ContactManual>>() {}.getType());
                            Set<ContactManual> contactManualSet = new HashSet<>();

                            if (contactManualList1 != null && !contactManualList1.isEmpty()) {
                                contactManualSet.addAll(contactManualList1);
                                plusIconData.setVisibility(View.GONE);
                            }

                            contactManualList.clear();
                            contactManualList.addAll(contactManualSet);

                            Constants.e("Updated CONTACT", mGson.toJson(contactManualList));
                        } catch (Exception e) {
                            Constants.e("err", e.toString());
                        }
                    } else if (data.hasExtra(Constants.NOCONTACT)) {
                        contactManualList.clear();
                    }
                    setTVContact();
                    break;



                case Constants.REQUESTEDIT:
                    if (data != null && data.hasExtra(Constants.ADDCONTACT)) {
                        String array = data.getStringExtra(Constants.ADDCONTACT);
                        List<ContactManual> editedList = mGson.fromJson(array, new TypeToken<List<ContactManual>>() {}.getType());
                        contactManualList.clear();
                        if (editedList != null) {
                            contactManualList.addAll(editedList);
                        }
                        setTVContact();
                    }
                    break;

                default:
                    break;
            }
        }
    }


    private void sendFile(Uri uri) {

        try {
            showPD();
            InputStream is = getContentResolver().openInputStream(uri);
            // InputStream is = getAssets().open("contact1.xlsx");
            int size = is.available();
            byte[] buffer = new byte[size]; //declare the size of the byte array with size of the file
            is.read(buffer); //read file

            String imageStr = Base64.encodeToString(buffer, Base64.DEFAULT);
            setRetrofit(getApp().getTcpIp());
            contactsApi = retrofit.create(ContactsApi.class);

            SoapGetExcelDataRequestEnvelope soapGetExcelDataRequestEnvelope =
                    new SoapGetExcelDataRequestEnvelope();

            GetExcelDataRequestBody getExcelDataRequestBody =
                    new GetExcelDataRequestBody();

            GetExcelData getExcelData = new GetExcelData();
            getExcelData.setA_sFileCode(imageStr);
            getExcelData.setAuthToken(getAuthTokenPref_Mobile());
            getExcelDataRequestBody.setGetExcelData(getExcelData);

            soapGetExcelDataRequestEnvelope.setBody(getExcelDataRequestBody);


            Observable<SoapGetExcelDataResponseEnvelope> lClientDetailsObservable =
                    contactsApi.getExcelData(soapGetExcelDataRequestEnvelope);
            lClientDetailsObservable.
                    subscribeOn(Schedulers.io())
                    .map(this::onMapExcel)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onExcelData, this::onExcelError);
        } catch (Exception e) {


            IpAddress.e("error", e.toString());
            hidePD();
            showToast(getString(R.string.someerror));
        }

    }

    public SoapGetExcelDataResponseEnvelope onMapExcel(SoapGetExcelDataResponseEnvelope soapGetExcelDataResponseEnvelope) {


        if (soapGetExcelDataResponseEnvelope != null) {

            GetExcelDataResult getExcelDataResult = soapGetExcelDataResponseEnvelope.getBody()
                    .getGetExcelDataResponse()
                    .getGetExcelDataResult();

            List<RecordContact> recordContacts = getExcelDataResult.getRecordContacts();
            if (getExcelDataResult.getError() != null) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        showToast(getExcelDataResult.error.get_error());
                    }
                });

            } else if (recordContacts != null && recordContacts.size() > 0) {


                for (RecordContact recordContact : recordContacts) {


                    if (recordContact != null) {


                        for (Contact contact : recordContact.getRecordContacts()) {

                            if (contact != null) {
                                if (contact.getMobile() != null || contact.getMobile().trim().length() > 0) {

                                    if (!excelcontacts.contains(contact))
                                        excelcontacts.add(contact);
                                } else if (contact.getEmail() != null || contact.getEmail().trim().length() > 0) {
                                    if (!excelcontacts.contains(contact))
                                        excelcontacts.add(contact);
                                } else if (contact.getName() != null || contact.getName().trim().length() > 0) {
                                    if (!excelcontacts.contains(contact))
                                        excelcontacts.add(contact);
                                } else if (contact.getCompany() != null || contact.getCompany().trim().length() > 0) {
                                    if (!excelcontacts.contains(contact))
                                        excelcontacts.add(contact);
                                } else if (contact.getFirstName() != null || contact.getFirstName().trim().length() > 0) {
                                    if (!excelcontacts.contains(contact))
                                        excelcontacts.add(contact);
                                } else if (contact.getFamilyName() != null || contact.getFamilyName().trim().length() > 0) {
                                    if (!excelcontacts.contains(contact))
                                        excelcontacts.add(contact);
                                } else if (contact.getEmail1() != null || contact.getEmail1().trim().length() > 0) {
                                    if (!excelcontacts.contains(contact))
                                        excelcontacts.add(contact);
                                }


                            }
                        }
                    }
                }

            }
        }


        return soapGetExcelDataResponseEnvelope;
    }

    private void onExcelData(SoapGetExcelDataResponseEnvelope soapGetExcelDataResponseEnvelope) {


        hidePD();

        IpAddress.e("size", excelcontacts.size() + " ");


        if (excelcontacts != null && excelcontacts.size() > 0) {
            for (Contact contact : excelcontacts) {
                ContactManual contactManual = new ContactManual();

                if (contact.getCompany() != null && contact.getCompany().trim().length() > 0)
                    contactManual.setCompany(contact.getCompany());
                else
                    contactManual.setCompany("''");

                if (contact.getEmail() != null && contact.getEmail().trim().length() > 0)
                    contactManual.setEmailId(contact.getEmail());
                else if (contact.getEmail1() != null && contact.getEmail1().trim().length() > 0)
                    contactManual.setEmailId(contact.getEmail1());
                else
                    contactManual.setCompany("''");

                contactManual.setCity("''");
                contactManual.setCountry("''");

                contactManual.setMeetingId(getMeetingId());

                contactManual.setSendInvitation("YES");

                if (contact.getFamilyName() != null)
                    contactManual.setSurName(contact.getFamilyName());

                if (contact.getFirstName() != null)
                    contactManual.setFirstName(contact.getFirstName());

                if (contact.getMobile() != null)
                    contactManual.setMobileNo(contact.getMobile());

                if (contactManualList.contains(contactManual)) {

                } else

                    contactManualList.add(contactManual);
            }
            rvcontacts.setVisibility(View.GONE);
            tvbottom.setVisibility(View.VISIBLE);
            plusIconData.setVisibility(View.VISIBLE);
            etsearch.getText().clear();
            setTVContact();
        }


    }

    private void onExcelError(Throwable throwable) {

        hidePD();
        IpAddress.e("error", throwable.toString());
        showToast(getString(R.string.excelerror));
    }

//    private void readXML(Uri uri) {
//
//
//        try {
//
//            InputStream is = getAssets().open("contact1.xlsx");
//            int size = is.available();
//            byte[] buffer = new byte[size]; //declare the size of the byte array with size of the file
//            is.read(buffer); //read file
//
//            List<Contact> contacts = new ArrayList<>();
//            String path = uri.toString();
//            // File file = new File(new URI(path));
//            // InputStream is = getContentResolver().openInputStream(uri);
//            //  FileInputStream excelFile = new FileInputStream(file);
//            XSSFWorkbook hssfWorkbook = new XSSFWorkbook(is);
//            // HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
////            XSSFWorkbook wb = new XSSFWorkbook(excelFile);
//            XSSFSheet sheet = hssfWorkbook.getSheetAt(0);
//
//            // HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
//
//            int rowCount; // number of rows of newly form excel files
//            //rowCount = sheet.getPhysicalNumberOfRows();
//            rowCount = sheet.getPhysicalNumberOfRows();
//            IpAddress.e("XML", rowCount + "");
//
//            //trying to iterate the excel file
//            int i = 0;
//            // Iterator<Row> rowIterator = sheet.iterator();
//            Iterator<Row> rowIterator = sheet.iterator();
//            do {
//                Row row = rowIterator.next();
//                Iterator<Cell> cellIterator = row.cellIterator();
//                while (cellIterator.hasNext()) {
//                    Cell cell = cellIterator.next();
//                    if (cell.getColumnIndex() == 0) { // to read the first column
//                        DataFormatter df = new DataFormatter();
//                        String str2 = df.formatCellValue(cell);
//                        // customerNo.add(str2);
//                        IpAddress.e("data", str2);
//                    }
//                }
//                ++i;
//                if (i > rowCount) break;
//                //break;
//            } while (rowIterator.hasNext());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void setTVContact() {
        String name = "";
        tvbottom.setVisibility(View.VISIBLE);
        llcontact.setVisibility(View.GONE);
        StringBuilder sb = new StringBuilder();

        if (contactManualList != null && contactManualList.size() > 0) {
            for (ContactManual c : contactManualList) {
                if (c.getFirstName() != null && !c.getFirstName().equalsIgnoreCase("''") &&
                        c.getSurName() != null && !c.getSurName().equalsIgnoreCase("''")) {
                    name = c.getFirstName() + " " + c.getSurName();
                } else if (c.getFirstName() != null && !c.getFirstName().equalsIgnoreCase("''")) {
                    name = c.getFirstName();
                } else if (c.getSurName() != null && !c.getSurName().equalsIgnoreCase("''")) {
                    name = c.getSurName();
                } else if (c.getMobileNo() != null && !c.getMobileNo().equalsIgnoreCase("''") && !c.getMobileNo().isEmpty()) {
                    name = c.getMobileNo();
                } else if (c.getEmailId() != null && !c.getEmailId().equalsIgnoreCase("''") && !c.getEmailId().isEmpty()) {
                    name = c.getEmailId();
                }

                // Remove any trailing quotes
                if (name.endsWith("\'\'")) {
                    name = name.substring(0, name.length() - 2);
                }
                sb.append(name);

                sb.append(", ");
            }
            dateTimeModel.setContactString(sb.substring(0, sb.length() - 2)); // Remove the last comma and space
            rveditcontacts.setVisibility(View.VISIBLE);
            rvcontacts.setVisibility(View.GONE);
            plusIconData.setVisibility(View.GONE);
        } else {
            refreshTv();
            tvcontact.setText("");
            dateTimeModel.setContactString(null);
            rveditcontacts.setVisibility(View.GONE);
            plusIconData.setVisibility(View.VISIBLE);
        }

        if (IpAddress.DEBUG()) {
            IpAddress.e("contact", mGson.toJson(dateTimeModel));
        }

        contactAdapter.notifyDataSetChanged();


    }

    @Override
    public void onReadExcelCompleted(List<String> stringList) {


    }


    public class Adapter extends RecyclerView.Adapter<Adapter.VH> {


        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {

            return new VH(LayoutInflater.from(AddParticipants.this).inflate(R.layout.inflate_contact_list1,
                    parent, false));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {


            Contact contact = contactsList.get(position);
            holder.tvcontactname.setText(contact.getName());

            if (contact.getType() != null) {

                if (contact.getType().equalsIgnoreCase("Former")) {
                    holder.tvtype.setText("Former");
                } else {
                    holder.tvtype.setText("My Contact");
                }

            } else {
                holder.tvtype.setText("Company Staff");
            }


            holder.rladd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (isPatient() && contactManualList != null && contactManualList.size() == 1) {

                        showMessage("Add Patients!", "You are not able to add more patients for single A" +
                                "ppointment. Please remove added patient and add a new Patient");

                    } else {

                        try {
                            ContactManual contactManual = new ContactManual();
                            contactManual.setCompany("''");
                            contactManual.setCity("''");
                            contactManual.setCountry("''");
                            contactManual.setMeetingId(getMeetingId());
                            contactManual.setSendInvitation("YES");
                            contactManual.setSurName("''");
                            contactManual.setFirstName(contactsList.get(holder.getAdapterPosition()).getName());
                            contactManual.setMobileNo(contactsList.get(holder.getAdapterPosition()).getMobile());
                            contactManual.setEmailId(contactsList.get(holder.getAdapterPosition()).getEmail());
                            if (contactManualList.contains(contactManual)) {


                                Toast.makeText(AddParticipants.this,
                                        getString(R.string.participant_exist), Toast.LENGTH_SHORT).show();
                                contactManualList.remove(contactManual);
                                contactManualList.add(contactManual);
                                rvcontacts.setVisibility(View.GONE);
                                tvbottom.setVisibility(View.VISIBLE);
                                plusIconData.setVisibility(View.GONE);
                                etsearch.getText().clear();
                                setTVContact();
                            } else {
                                contactManualList.add(contactManual);
                                rvcontacts.setVisibility(View.GONE);
                                tvbottom.setVisibility(View.VISIBLE);
                                plusIconData.setVisibility(View.GONE);
                                etsearch.getText().clear();
                                setTVContact();
                            }

                            hideKeyboard(etsearch);
                        } catch (Exception e) {

                        }
                    }


                }
            });
        }

        @Override
        public int getItemCount() {
            return contactsList.size();
        }

        public class VH extends RecyclerView.ViewHolder {

            @BindView(R.id.tvcontactname)
            TextView tvcontactname;
            @BindView(R.id.rladd)
            RelativeLayout rladd;

            @BindView(R.id.tvtype)
            TextView tvtype;

            public VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }


    public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.VH> {


        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {

            return new VH(getLayoutInflater().inflate(R.layout.inflate_contact, parent, false));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {


            if (position == 0) {
                holder.tvdeleteall.setVisibility(View.VISIBLE);

                holder.tvdeleteall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        contactManualList.clear();
                        notifyDataSetChanged();
                        plusIconData.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                holder.tvdeleteall.setVisibility(View.GONE);
            }


            ContactManual leave = contactManualList.get(position);

            StringBuilder name = new StringBuilder();

            boolean flag = false;

            if (leave.getFirstName() != null
                    && leave.getFirstName().trim().length() > 0
                    && !leave.getFirstName().equalsIgnoreCase("''")) {

                name.append(leave.getFirstName());
                flag = true;
            } else {
                flag = false;
            }


            if (leave.getSurName() != null
                    && leave.getSurName().trim().length() > 0
                    && !leave.getSurName().equalsIgnoreCase("''")) {

                if (flag) {
                    name.append(" ");
                }
                flag = true;
                name.append(leave.getSurName());
            } else {

                flag = false;
            }

            StringBuilder name2 = new StringBuilder();

            if (leave.getMobileNo() != null
                    && leave.getMobileNo().trim().length() > 0
                    && !leave.getMobileNo().equalsIgnoreCase("''")) {

                if (leave.isHasCountryCode()) {

                    if (!leave.getMobileNo().contains("+")) {
                        name2.append("+" + leave.getMobileNo());
                    } else {
                        name2.append(leave.getMobileNo());
                    }


                } else
                    name2.append(leave.getMobileNo());

                flag = true;
            } else {
                if(!flag)
                flag = false;
            }


            if (leave.getEmailId() != null
                    && leave.getEmailId().trim().length() > 0
                    && !leave.getEmailId().equalsIgnoreCase("''")) {
                if (flag) {
                    name2.append(", ");
                }
                name2.append(leave.getEmailId());

                flag = true;
            } else {
                if(!flag)
                flag = false;
            }


            if (leave.getCompany() != null
                    && leave.getCompany().trim().length() > 0
                    && !leave.getCompany().equalsIgnoreCase("''")) {

                if (flag) {
                    name2.append(", ");
                }
                flag=true;
                name2.append(leave.getCompany());


            } else {
                if(!flag)
                flag = false;
            }


            holder.tvcontactname.setText(name);


            holder.tvemail.setText(name2);


            holder.btdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddParticipants.this);
                    builder.setTitle("Delete Record!");
                    builder.setMessage(getString(R.string.delete_meeting));
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            contactManualList.remove(holder.getAdapterPosition());

                            setTVContact();
                        }


                    });


                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                        }
                    });

                    builder.show();





                }
            });

            holder.btedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent i = new Intent(AddParticipants.this, AddManuallyActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra(Constants.POS, holder.getAdapterPosition());
                    i.putExtra(Constants.EDITCONTACTS, true);
                    i.putExtra(Constants.ADDCONTACT
                            , mGson.toJson(contactManualList.get(holder.getAdapterPosition())));
                    startActivityForResult(i, Constants.EDITCONTACT);

                }
            });


        }


        @Override
        public int getItemCount() {
            return contactManualList.size();
        }

        public class VH extends RecyclerView.ViewHolder {


            @BindView(R.id.btdelete)
            ImageView btdelete;


            @BindView(R.id.tvemail)
            TextView tvemail;

            @BindView(R.id.btedit)
            ImageView btedit;

            @BindView(R.id.rledit)
            RelativeLayout rledit;

            @BindView(R.id.tvcontactname)
            TextView tvcontactname;

            @BindView(R.id.tvdeleteall)
            TextView tvdeleteall;


            public VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }


    }
}

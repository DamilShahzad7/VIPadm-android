package vip.com.vipmeetings.bluno;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import vip.com.vipmeetings.R;
import vip.com.vipmeetings.evacuate.IpAddress;
import vip.com.vipmeetings.interfaces.IRegisterUser;
// NEW IMP0RTS
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.IntentFilter;


public class BlunoActivity extends BlunoLibrary {

    // NEW PERMISSIONS ADDED
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 1000;

    private boolean hasBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
                    == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            // pre-Android 12 BLE scanning still needs fine location
            return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_CONNECT
                    },
                    REQUEST_BLUETOOTH_PERMISSIONS
            );
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },
                    REQUEST_BLUETOOTH_PERMISSIONS
            );
        }
    }
    private static final int LOCATION_SETTINGS_REQUEST = 1;
    @BindView(R.id.btconnect)
    Button buttonScan;

    SharedPreferences mSharedPreferences;
    String userid, uuid, userstatus, username;
    String status = "YES";

    boolean sent;


    boolean senttoserver;


    @BindView(R.id.ivback)
    AppCompatImageView ivback;

    @BindView(R.id.tvupdate)
    TextView tvupdate;

    @BindView(R.id.tvuid)
    AppCompatTextView tvuid;

    @BindView(R.id.tvprepare)
    AppCompatTextView tvprepare;

    @BindView(R.id.tvname1)
    TextView tvname1;

    @BindView(R.id.tvmessage)
    AppCompatTextView tvmessage;

    @BindView(R.id.tvconnect)
    TextView tvconnect;

    @BindView(R.id.pb)
    ProgressBar pb;

    @BindView(R.id.ivrefresh)
    ImageView ivrefresh;

    List<BluetoothDevice> bluetoothDevices;
    boolean flag_webservie;


    boolean flag_connect = true;


    Retrofit retrofit;

    boolean getUserStatus;
    ConnectivityManager connectivityManager;
    LocationManager manager;

    private void registerGattReceiver() {
        Log.d("BLUNO-GATT", "→ registerGattReceiver() called");
        IntentFilter filter = makeGattUpdateIntentFilter();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // This receiver is only for our own process:
            registerReceiver(mGattUpdateReceiver,
                    filter,
                    Context.RECEIVER_NOT_EXPORTED);
        } else {
            // pre-API 33, use the old two-arg call
            registerReceiver(mGattUpdateReceiver, filter);
        }
        Log.d("BLUNO-GATT", "→ registerGattReceiver() FINISHED");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluno);
        ButterKnife.bind(this);
        // ───► 1) Register the GATT receiver unconditionally, right away:
        registerGattReceiver();
        Log.d("BLUNO-GATT", "→ registerGattReceiver() finished");
        onCreateProcess();       // binds to BluetoothLeService
        serialBegin(115200);     // configures UART to 115200



        // Initialize location, connectivity and preferences
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        mSharedPreferences = getSharedPreferences(IpAddress.LOGIN, MODE_PRIVATE);
        if (!mSharedPreferences.getBoolean(IpAddress.mainscreen, false)) {
            mSharedPreferences.edit().putBoolean(IpAddress.mainscreen, true).apply();
        }

        // Initial UI state
        bluetoothDevices = new ArrayList<>();
        buttonScan.setEnabled(false);
        buttonScan.setAlpha(0.4f);
        tvupdate.setVisibility(View.INVISIBLE);
        buttonScan.setVisibility(View.INVISIBLE);
        ivrefresh.setEnabled(true);
        if (checkPermissionsAll()) {
            buttonScan.setVisibility(View.INVISIBLE);
            tvprepare.setVisibility(View.VISIBLE);
            tvprepare.setText("PREPARE FOR OPEN..");
        }

        // Android 12+ BLE permission gating
        if (!hasBluetoothPermissions()) {
            requestBluetoothPermissions();
        } else {
            // BLE setup only once we have runtime permission
            if (isServicesOK()) {
                request(1000, new OnPermissionsResult() {
                    @Override
                    public void OnSuccess() {
                        Toast.makeText(BlunoActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();

                        blunoProcess();
                    }
                    @Override
                    public void OnFail(List<String> noPermissions) {
                        Toast.makeText(BlunoActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                });
                try {
                    if (isServicesOK() && checkPermissionsAll()) {
                        onCreateProcess();
                        serialBegin(115200);
                        //registerGattReceiver();
                        blunoProcess();
                    }
                } catch (Exception ignored) { }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Handle our Android 12+ BLE permission request
        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            boolean allGranted = true;
            for (int r : grantResults) {
                if (r != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                // Permissions granted → resume BLE setup
                onCreateProcess();
                serialBegin(115200);
                //registerGattReceiver();
                blunoProcess();
            } else {
                Toast.makeText(
                        this,
                        "Bluetooth permissions are required to open the door",
                        Toast.LENGTH_LONG
                ).show();
            }
            return;  // don’t let other permission handling run
        }

        // If you have other permission requests (e.g. your checkPermissionsAll), handle them below
        // switch(requestCode) { … }
    }

    /*If Success connection bluetooth ----Sekhar */
    @Override
    protected void onResume() {
        super.onResume();
       // if(checkPermissionsAll()) {
            username=getIntent().getStringExtra(IpAddress.USERNAME);
            buttonScan.setVisibility(View.INVISIBLE);
            tvmessage.setVisibility(View.VISIBLE);
            tvname1.setText(username);
            tvprepare.setVisibility(View.VISIBLE);
            tvprepare.setText("PREPARE FOR OPEN..");
       // }
    }

    private boolean isServicesOK() {

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int err = googleAPI.isGooglePlayServicesAvailable(this);
        if (err == ConnectionResult.SUCCESS) {

            return true;
        } else if (googleAPI.isUserResolvableError(err)) {
            googleAPI.showErrorDialogFragment(this, err, ERROR_DIALOG_REQUEST);
        }

        return false;

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @OnClick({R.id.ivback,R.id.tvnext})
    public void onBack(View v)
    {
        // onDestroyProcess();
        onBackPressed();
    }




    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void blunoProcess() {

        try {
//            onCreate Process by BlunoLibrary
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            // mLocationRequest.setSmallestDisplacement(10);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setInterval(10000);
            builder = new LocationSettingsRequest.Builder()
                    .setNeedBle(true)
                    .setAlwaysShow(true)
                    .addLocationRequest(mLocationRequest);
            Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this)
                    .checkLocationSettings(builder.build());

            result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                @Override
                public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                    try {
                        LocationSettingsResponse response =
                                task.getResult(ApiException.class);

                        if(response.getLocationSettingsStates()!=null && response.getLocationSettingsStates().isBlePresent() && response.getLocationSettingsStates().isBleUsable())
                        {
                            //  Toast.makeText(BlunoActivity.this, "Ble  usable ", Toast.LENGTH_SHORT).show();
                            onResumeProcess();
                        }else
                        {
                            Toast.makeText(BlunoActivity.this, "Ble not usable", Toast.LENGTH_SHORT).show();

                        }

                    } catch (ApiException ex) {
                        switch (ex.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException resolvableApiException =
                                            (ResolvableApiException) ex;
                                    resolvableApiException
                                            .startResolutionForResult(BlunoActivity.this,
                                                    LOCATION_SETTINGS_REQUEST);
                                } catch (IntentSender.SendIntentException e) {

                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                                break;
                        }
                    }
                }
            });


        } catch (Exception e) {
            Toast.makeText(BlunoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void blunoProcess2() {

        //onCreate Process by BlunoLibrary

        if (uuid != null && uuid.length() > 0) {


            if (mLeDeviceListAdapter != null &&
                    mLeDeviceListAdapter.getCount() > 0) {

                IpAddress.e("if bluno "+uuid);
                pb.setVisibility(View.VISIBLE);
                ivrefresh.setEnabled(true);
                buttonScan.setVisibility(View.INVISIBLE);
                mLeDeviceListAdapter.notifyDataSetChanged();

            } else {

                IpAddress.e("else bluno");
                pb.setVisibility(View.VISIBLE);
                ivrefresh.setEnabled(true);
                buttonScan.setVisibility(View.INVISIBLE);
                flag_connect = true;
                buttonScan.setText("RECONNECT");
                tvprepare.setText("PREPARE FOR OPEN..");
                tvupdate.setVisibility(View.INVISIBLE);
//                if(uuid!=null) {
//                    tvprepare.setVisibility(View.VISIBLE);
//                    tvprepare.setText("OPENING");
//                    tvuid.setVisibility(View.VISIBLE);
//                    tvuid.setText("Connecting to:\n" + uuid);
//                }

//                    onCreateProcess();
//                    serialBegin(115200);
//                    registerGattReceiver();
                //todo
                scanLeDevice(true);

                //  mConnectionState = connectionStateEnum.isToScan;
                //  onConectionStateChange(mConnectionState);
            }
            tvprepare.setText("PREPARE FOR OPEN..");
        } else {
            Toast.makeText(BlunoActivity.this, "Empty uuid found,please close and reopen app", Toast.LENGTH_SHORT).show();
        }



    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @OnClick(R.id.btconnect)
    public void onConnect(View v) {

        //todo
        // blunoProcess2();
        buttonScan.setVisibility(View.INVISIBLE);
        tvprepare.setVisibility(View.VISIBLE);
        tvprepare.setText("PREPARE FOR OPEN..");

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isConnect=true;
                //  onConnect();//Commented  by Praveen Starts For - Multiple Blunos Connection on Android 12 - SDK 31
                //Added  by Praveen Starts For - Multiple Blunos Connection on Android 12 - SDK 31
                if (isServicesOK()) {

                    onCreateProcess();
                    serialBegin(115200);
                    //registerGattReceiver();
                    blunoProcess();
                }
                //Above Code Added by Praveen Ends For - Multiple Blunos Connection on Android 12 - SDK 31
            }
        }, 300);


        // mLeDeviceListAdapter.notifyDataSetChanged();


        // blunoProcess();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @OnClick(R.id.ivrefresh)
    public void onIV(View v) {
        Log.d("BLUNO", "🔄 onIV() — refresh button tapped");
        // onclic();

        //todo
        // blunoProcess2();
        buttonScan.setVisibility(View.INVISIBLE);
        tvprepare.setVisibility(View.VISIBLE);
        tvprepare.setText("PREPARE FOR OPEN..");

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isConnect=true;
                onConnect();//Code Commented by Praveen  For - Multiple Blunos Connection on Android 12 - SDK 31
                //Added  by Praveen Starts For - Multiple Blunos Connection on Android 12 - SDK 31
                if (isServicesOK()) {

                    onCreateProcess();
                    serialBegin(115200);
                    //registerGattReceiver();
                    blunoProcess();
                }
                //Above Code Added by Praveen Ends For - Multiple Blunos Connection on Android 12 - SDK 31

            }
        }, 300);
        // mLeDeviceListAdapter.notifyDataSetChanged();

        //  onConnect();
        //  blunoProcess();

//        if (!mScanning) {
//            IpAddress.e("if" + mScanning);
//            blunoProcess2();
//        } else {
//            IpAddress.e("else" + mScanning);
//        }
//
//        try {
//
//            if (getUserStatus) {
//                if (isNetWorkAvailble()) {
//                    retroMethod();
//                } else {
//                    getUserStatus = true;
//                    showMessage(getString(R.string.someerror), "error");
//                }
//
//            } else {
//                ivrefresh.setEnabled(false);
//                buttonScan.setVisibility(View.INVISIBLE);
//                pb.setVisibility(View.VISIBLE);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        try {
//                            if (uuid != null && !uuid.trim().isEmpty()) {
//
//                                blunoProcess();
//                            } else {
//                                IpAddress.e("button" + uuid);
//                            }
//                        } catch (Exception e) {
//                            IpAddress.e(e.toString());
//                        }
//
//
//                    }
//                }, 400);
//            }
//        } catch (Exception e) {
//            IpAddress.e("btconnect" + e.toString());
//        }


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void onclic() {

        try {
            if (getUserStatus) {
                if (isNetWorkAvailble()) {
                    retroMethod();
                } else {
                    getUserStatus = true;
                    showMessage(getString(R.string.someerror), "error");
                }

            } else {
                buttonScan.setVisibility(View.INVISIBLE);
                pb.setVisibility(View.VISIBLE);
                ivrefresh.setEnabled(true);
                buttonScan.setEnabled(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            if (uuid != null && !uuid.trim().isEmpty()) {
                                ivrefresh.setEnabled(true);
                                blunoProcess();

                            } else {
                                IpAddress.e("button" + uuid);
                            }
                        } catch (Exception e) {
                            IpAddress.e(e.toString());
                        }


                    }
                }, 400);
            }
        } catch (Exception e) {
            IpAddress.e("ivrefresh" + e.toString());
        }
    }

    private boolean isNetWorkAvailble() {


        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.isAvailable() && networkInfo.isConnected() || networkInfo.isConnectedOrConnecting())
                return true;

        }

        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void retroMethod() {
        uuid=null;
        uuid = mSharedPreferences.getString(IpAddress.UUID, "");
        userstatus="APPROVED";
        username=getIntent().getStringExtra(IpAddress.USERNAME);
        userid=getIntent().getStringExtra(IpAddress.USERID);
        analyzeString(uuid.trim(), username.trim(), userstatus.trim());


//        pb.setVisibility(View.VISIBLE);

//
//        IpAddress.e("userid" + userid);
//        if (retrofit == null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(IpAddress.IP)
//                    .client(new OkHttpClient())
//                    .addConverterFactory(SimpleXmlConverterFactory.create())
//                    .build();
//        }
//        IRegisterUser iRegisterUser = retrofit.create(IRegisterUser.class);
//        Call<ResponseBody> responseCall = iRegisterUser.GetUserStatus(userid,
//                status);
//
//        responseCall.enqueue(new Callback<ResponseBody>() {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                try {
//                    getUserStatus = false;
//                    IpAddress.e("ok" + response.code() + "" + call.request().toString());
//
//                    String xml = response.body().string();
//                    IpAddress.e("ok" + xml);
//
//                    DocumentBuilderFactory factory =
//                            DocumentBuilderFactory.newInstance();
//                    DocumentBuilder builder = factory.newDocumentBuilder();
//                    ByteArrayInputStream input = new ByteArrayInputStream(
//                            xml.toString().getBytes("UTF-8"));
//                    Document doc = builder.parse(input);
//
//                    NodeList nodeList = doc.getElementsByTagName("User").item(0).getChildNodes();
//
//                    for (int i = 0; i < nodeList.getLength(); i++) {
//                        Node n = nodeList.item(i);
//
//                        switch (n.getNodeName()) {
//
//                            case "UUID":
//                                uuid = n.getTextContent();
//                                break;
//
//                            case "UserStatus":
//                                userstatus = n.getTextContent();
//                                break;
//
//                            case "UserName":
//                                username = n.getTextContent();
//                                tvname1.setText(username);
//                                break;
//                        }
//
//
//                    }
//
//
//                    analyzeString(uuid.trim(), username.trim(), userstatus.trim());
//
//
//                } catch (Exception e) {
//                    showMessage("Some error occured, Please contact admin", "ADMIN");
//                    //getUserStatus=true;
//                    pb.setVisibility(View.GONE);
//                    IpAddress.e(e.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                IpAddress.e("okFail" + t.getMessage() + "");
//                getUserStatus = true;
//                showMessage(getString(R.string.someerror), "error");
//                pb.setVisibility(View.GONE);
//            }
//        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void analyzeString(String uuid, String username, String userstatus) {


        IpAddress.e("analyzeString" + userid + username + userstatus + uuid);


        if(userid!=null && userid.trim().length()>0)
        {
            mSharedPreferences.edit().putString(IpAddress.USERID, userid).apply();
        }
        if (userstatus.equalsIgnoreCase("APPROVED")) {
            getUserStatus = false;
            tvmessage.setVisibility(View.VISIBLE);
            tvname1.setText(username);
            if (uuid != null && uuid.trim().length() != 0) {
                IpAddress.e("uuid not null");
                buttonScan.setEnabled(true);
                ivrefresh.setEnabled(true);
                senttoserver = true;
                flag_webservie = true;
                //   mConnectionState = connectionStateEnum.isToScan;
                // onConectionStateChange(mConnectionState);
                isToScanUUID();
            } else {
                userid = mSharedPreferences.getString(IpAddress.USERID, null);
                IpAddress.e("USERID" + userid + "UUID" + uuid);
                if (userid != null) {
                    IpAddress.e("userid not null");
                    tvupdate.setVisibility(View.INVISIBLE);
                    buttonScan.setEnabled(true);
                    ivrefresh.setEnabled(true);
                    flag_webservie = true;
                    // mConnectionState = connectionStateEnum.isToScan;
                    //  onConectionStateChange(mConnectionState);
                    isToScanUUID();

                }
            }

        } else if (userstatus.equalsIgnoreCase("PENDING")) {
            getUserStatus = true;
            tvmessage.setVisibility(View.VISIBLE);
            buttonScan.setText("PENDING");

        } else if (userstatus.equalsIgnoreCase("BLOCKED")) {
            getUserStatus = true;
            tvmessage.setVisibility(View.VISIBLE);
            buttonScan.setText("BLOCKED");

        } else if (userstatus.equalsIgnoreCase("UNAUTHORISED")) {
            getUserStatus = true;
            tvmessage.setVisibility(View.VISIBLE);
            buttonScan.setText("UNAUTHORISED");


        } else {
            getUserStatus = true;
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void isToScanUUID() {

        flag_connect = true;
        buttonScan.setText("RECONNECT");
        tvprepare.setText("PREPARE FOR OPEN..");
        tvupdate.setVisibility(View.INVISIBLE);
//                if(uuid!=null) {
//                    tvprepare.setVisibility(View.VISIBLE);
//                    tvprepare.setText("OPENING");
//                    tvuid.setVisibility(View.VISIBLE);
//                    tvuid.setText("Connecting to:\n" + uuid);
//                }

        scanLeDevice(true);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void updateRetroMethod(String userid, String uuid) {

        String user = "SUCCESS";
        analyzeString1(user);

        tvupdate.setVisibility(View.INVISIBLE);
        tvupdate.setText("UPDATING USER INFORMATION");
//
        IpAddress.e("update" + userid + uuid);
//        if (retrofit == null)
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(IpAddress.IP)
//                    .addConverterFactory(SimpleXmlConverterFactory.create())
//                    .build();
//        IRegisterUser iRegisterUser = retrofit.create(IRegisterUser.class);
//        Call<ResponseBody> responseCall = iRegisterUser.UpdateUUID(
//                userid, uuid);
//
//        responseCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//
//                try {
//                    IpAddress.e("ok" + response.code() + "" + call.request().toString());
//
//                    String xml = response.body().string();
//                    IpAddress.e("ok" + xml);
//
//                    DocumentBuilderFactory factory =
//                            DocumentBuilderFactory.newInstance();
//                    DocumentBuilder builder = factory.newDocumentBuilder();
//
//
//                    ByteArrayInputStream input = new ByteArrayInputStream(
//                            xml.getBytes("UTF-8"));
//                    Document doc = builder.parse(input);
//
//                    String user = doc.getElementsByTagName("status")
//                            .item(0)
//                            .getTextContent();
//
//                    IpAddress.e("doc" + user);
//
//                    analyzeString1(user, doc);
//
//                } catch (Exception e) {
//                    IpAddress.e(e.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                Log.e("okFail", t.getMessage());
//
//                showMessage(getString(R.string.someerror), "");
//            }
//        });


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void analyzeString1(String status) {

        IpAddress.e("success" + status);

        if (status.equalsIgnoreCase("SUCCESS")) {

            senttoserver = true;
            IpAddress.e("device found");
            tvupdate.setVisibility(View.INVISIBLE);
            tvupdate.setText("PREPARE FOR OPENING");
            if(mBluetoothLeService!= null) {
                if (mBluetoothLeService.connect(uuid)) {
                    IpAddress.e("uuid connected");
                } else {
                    IpAddress.e("uuid not connected");
                }
            }

        }
    }


    public void showMessage(String message, final String error) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        mBuilder.setMessage(message);
        mBuilder.setCancelable(false);

        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                dialogInterface.dismiss();
                if (error.equalsIgnoreCase("error")) {
                    buttonScan.setEnabled(true);
                    ivrefresh.setEnabled(true);
                } else if (error.equalsIgnoreCase("ADMIN")) {
                    //todo
//                    Intent intent = new Intent(BlunoActivity.this, WelcomeActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();
                }

            }
        });

        try {
            mBuilder.show();
        } catch (Exception e) {

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        try {
                            mBluetoothAdapter = bluetoothManager.getAdapter();
                            if (!mBluetoothAdapter.isEnabled()) {
                                //1253
                                Intent enableBtIntent = new Intent(
                                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                            } else {
                                onRetro();
                            }
                        } catch (Exception e) {

                            IpAddress.e("error" + e.toString());

                        }

                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to

                        break;
                    default:
                        break;
                }
                break;

            case REQUEST_ENABLE_BT:

                if (resultCode == Activity.RESULT_CANCELED) {
                    finish();
                    break;
                } else {
                    try {
                        onRetro();
                    } catch (Exception e) {

                    }
                }

            case ERROR_DIALOG_REQUEST:
                if (resultCode == RESULT_OK) {

                    //code1253
                    blunoProcess();

                }
                break;

            case 1000:

                if (resultCode == RESULT_OK) {

                    IpAddress.e("onloc true");
                    onLocation();
                }
                break;


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            onPauseProcess();                                                        //onPause Process by BlunoLibrary
        } catch (Exception e) {
            IpAddress.e("onpaus" + e.toString());
        }
        //onRe
    }

    protected void onStop() {
        super.onStop();
        try {
            onStopProcess();                                                        //onStop Process by BlunoLibrary
        } catch (Exception e) {
            IpAddress.e("onstop" + e.toString());
        }
        //onRe
    }

    @Override
    public void onStopProcess() {
        super.onStopProcess();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onDestroy() {

        try {
            unregisterReceiver(mGattUpdateReceiver);

            onDestroyProcess();
            //mConnectionState = connectionStateEnum.isDisconnecting;
            //onConectionStateChange(mConnectionState);

        } catch (Exception e) {
            IpAddress.e("ondestroy" + e.toString());
        } finally {
            super.onDestroy();
        }
        //onRe
        //onDestroy Process by BlunoLibrary
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onRetro() {

        try {
            //  onCreateProcess();
            // serialBegin(115200);
            // registerGattReceiver();

            IpAddress.e("uuid "+uuid);
            if (uuid != null && uuid.trim().length() > 0) {

                IpAddress.e("uuid null");

            } else {
                IpAddress.e("uuid notnull");

            }
            if (isNetWorkAvailble()) {
                retroMethod();
            } else {
                getUserStatus = true;
                showMessage(getString(R.string.nointernet), "error");
            }
        } catch (Exception e) {
        }
    }

    boolean isConnect=false;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void getList(List<BluetoothDevice> bluetoothDevices) {

        IpAddress.e(uuid + "added device  " + bluetoothDevices.size());
        //   Toast.makeText(this, uuid+"uui", Toast.LENGTH_SHORT).show();
        flag_connect = true;
        if (bluetoothDevices != null && bluetoothDevices.size() > 0 && flag_webservie) {

            if (uuid != null && uuid.trim().length()>0) {

                for (BluetoothDevice ble : bluetoothDevices) {

                    IpAddress.e(uuid + "UUID");
                    IpAddress.e("Address" + ble.getAddress());

                    if (uuid.equalsIgnoreCase(ble.getAddress())) {

                        IpAddress.e("device found");
                        tvupdate.setVisibility(View.INVISIBLE);
                        tvupdate.setText("PREPARE FOR OPENING");
                        try {
                            if (ble.getName() != null && ble.getName().equalsIgnoreCase("Bluno")) {
                                if (mBluetoothLeService.connect(uuid)) {
                                    flag_connect = false;
                                    IpAddress.e("uuid connected");
                                    scanLeDevice(false);
                                    break;
                                }
                            } else
                            if (ble.getName() != null && ble.getName().equalsIgnoreCase("BlunoMega")) {
                                if (mBluetoothLeService.connect(uuid)) {
                                    flag_connect = false;
                                    IpAddress.e("uuid connected");
                                    scanLeDevice(false);
                                    break;
                                }
                            }else
                            if (ble.getName() != null && ble.getName().contains("Bluno")) {
                                if (mBluetoothLeService.connect(uuid)) {
                                    flag_connect = false;
                                    IpAddress.e("uuid connected");
                                    scanLeDevice(false);
                                    break;
                                }
                            }
                            else {
                                IpAddress.e("uuid not connected");
                            }
                        } catch (Exception e) {
                            IpAddress.e("loop" + e.toString());
                        }
                        break;

                    } else {

                        // mBluetoothLeService.connect(bluetoothDevices.get(0).getAddress());
                        //Raparthy Starts when  UID & Address do not match - IR Jan-2023
                        IpAddress.e("update device to server");
                        if (ble.getAddress() != null || !ble.getAddress().trim().isEmpty()) {
                            if (bluetoothDevices.size() > 0) {

                                uuid = ble.getAddress();
                                mSharedPreferences.edit().putString(IpAddress.UUID, uuid).apply();
                                updateRetroMethod(userid, uuid);
                                flag_connect = false;
                                scanLeDevice(false);
                                break;

                            } else {
                                flag_connect = true;
                                IpAddress.e("no devices");
//                        mConnectionState = connectionStateEnum.isToScan;
//                        onConectionStateChange(mConnectionState);
                                isToScanUUID();

                            }
                        }else if (ble.getAddress() == null || ble.getAddress().trim().isEmpty()) {
                            if (bluetoothDevices.size() > 0) {
                                // uuid = bluetoothDevices.get(0).getAddress();
                                mSharedPreferences.edit().putString(IpAddress.UUID, uuid).apply();
                                updateRetroMethod(userid, uuid);
                                flag_connect = false;
                                scanLeDevice(false);
                                break;
                            } else {
                                flag_connect = true;
                                IpAddress.e("no devices");
//                        mConnectionState = connectionStateEnum.isToScan;
//                        onConectionStateChange(mConnectionState);
                                isToScanUUID();

                            }
                        }else {
                            IpAddress.e("null");
                        }
                        //Raparthy Ends when UID & Address do not match - IR Jan-2023

                    }

                }

                if (flag_connect) {
//                    mConnectionState = connectionStateEnum.isToScan;
//                    onConectionStateChange(mConnectionState);
                    isToScanUUID();
                }

            } else {
                IpAddress.e("update device to server");
                if (uuid == null || uuid.trim().isEmpty()) {
                    if (bluetoothDevices.size() > 0) {

                        scanLeDevice(false);
                        uuid = bluetoothDevices.get(0).getAddress();
                        mSharedPreferences.edit().putString(IpAddress.UUID, uuid).apply();
                        updateRetroMethod(userid, uuid);
                        flag_connect = false;//Raparthy - IR Jan-2023

                    } else {
                        flag_connect = true;
                        IpAddress.e("no devices");
//                        mConnectionState = connectionStateEnum.isToScan;
//                        onConectionStateChange(mConnectionState);
                        isToScanUUID();

                    }
                } else {
                    IpAddress.e("null");
                }

            }

        } else if(uuid!=null && uuid.trim().length()>0) {


            if (isConnect && mBluetoothLeService.connect(uuid)) {
                flag_connect = false;
                IpAddress.e("uuid connected");
                scanLeDevice(false);
                isConnect=false;
            }
            IpAddress.e("uuid null or  no devices");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public  void onConnect()
    {
        IpAddress.e("uuid connected "+uuid);

        if(uuid!=null && uuid.trim().length()>0)
        {
            isConnect=true;
        }

        if (isConnect && mBluetoothLeService!=null && uuid!=null && uuid.trim().length()>0 &&  mBluetoothLeService.connect(uuid)) {
            flag_connect = false;
            IpAddress.e("uuid connected "+uuid);
            scanLeDevice(false);
            isConnect=false;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onDestroyProcess() {
        try {
            //unregisterReceiver(mGattUpdateReceiver);
            unbindService(mServiceConnection);
            if (mBluetoothLeService != null) {
                mBluetoothLeService.disconnect();
                mBluetoothLeService.close();
                mBluetoothLeService = null;
            }
            mSCharacteristic = null;
        } catch (Exception e) {
            IpAddress.e("des" + e.toString());
        }
        //  mHandler.removeCallbacks(mDisonnectingOverTimeRunnable);
        // mBluetoothLeService.close();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onConectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called

        //  IpAddress.e("onconnect" + theConnectionState);
//        Toast.makeText(BlunoActivity.this,theConnectionState+"",Toast.LENGTH_LONG).show();

        switch (theConnectionState) {                                            //Four connection state

            case isConnected:

                IpAddress.e("uuid  ISConne"+uuid);

                if(uuid!=null) {
                    tvprepare.setVisibility(View.VISIBLE);
                    tvprepare.setText("OPENING");

                    tvuid.setVisibility(View.VISIBLE);
                    tvuid.setText("Connecting to:\n" + uuid);
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        tvprepare.setVisibility(View.INVISIBLE);
                        sent = false;
                        sendSig();
                    }
                }, 400);

                break;
            case isConnecting:

                if(uuid!=null) {
                    tvprepare.setVisibility(View.VISIBLE);
                    tvprepare.setText("OPENING");
                    tvuid.setVisibility(View.VISIBLE);
                    tvuid.setText("Connecting to:\n" + uuid);
                }
                break;
            case isToScan:
                flag_connect = true;
                buttonScan.setText("RECONNECT");
                tvprepare.setText("PREPARE FOR OPEN..");
                tvupdate.setVisibility(View.INVISIBLE);
//                if(uuid!=null) {
//                    tvprepare.setVisibility(View.VISIBLE);
//                    tvprepare.setText("OPENING");
//                    tvuid.setVisibility(View.VISIBLE);
//                    tvuid.setText("Connecting to:\n" + uuid);
//                }

                scanLeDevice(true);
                break;
            case isScanning:
                buttonScan.setEnabled(false);
                buttonScan.setAlpha(0.4f);
                break;
            case isDisconnecting:
                scanLeDevice(false);
                buttonScan.setAlpha(1.0f);
                buttonScan.setEnabled(true);
                ivrefresh.setEnabled(true);
                tvuid.setVisibility(View.INVISIBLE);
                buttonScan.setText("RECONNECT");
                tvupdate.setVisibility(View.INVISIBLE);
                tvprepare.setVisibility(View.INVISIBLE);
                onDestroyProcess();//Raparthy - IR Jan-2023  For releasing the Connection
                break;
            default:
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void sendSig() {

        if (!sent) {
            ivrefresh.setEnabled(true);
            buttonScan.setEnabled(false);
            IpAddress.e("called" + "CONN SIGNAL");
            buttonScan.setAlpha(0.4f);

            serialSend(mPlainProtocol.write(Relay, 1));
            buttonScan.setText("CONNECTED");
            //  tvconnect.setVisibility(View.VISIBLE);
            sent = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (sent) {
                        buttonScan.setText("RECONNECT");
                        buttonScan.setEnabled(true);

                        tvprepare.setVisibility(View.INVISIBLE);
                        tvuid.setVisibility(View.INVISIBLE);
                        //  tvconnect.setVisibility(View.INVISIBLE);
                        tvupdate.setVisibility(View.INVISIBLE);
                        buttonScan.setVisibility(View.VISIBLE);
                        pb.setVisibility(View.GONE);
                        serialSend(mPlainProtocol.write(Relay, 0));
                        sent = false;

                        enable();

                    }

                }
            }, 500);
        }
    }

    private void enable() {

        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void run() {

                IpAddress.e("called" + "DISCONN SIGNAL");
                mConnectionState = connectionStateEnum.isDisconnecting;
                onConectionStateChange(mConnectionState);
            }
        }, 500);
    }


    @Override
    public void onSerialReceived(String theString) {

        //Once connection data received, this function will be called
        IpAddress.e("serial reciverd" + theString);
        // serialReceivedText.append(theString);                            //append the text into the EditText
        //The Serial data from the BLUNO may be sub-packaged, so using a buffer to hold the String is a good choice.
        // ((ScrollView) serialReceivedText.getParent()).fullScroll(View.FOCUS_DOWN);
    }

}

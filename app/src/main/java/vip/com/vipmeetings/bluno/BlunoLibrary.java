package vip.com.vipmeetings.bluno;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;

import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.os.ParcelUuid;
import java.util.Collections;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import vip.com.vipmeetings.R;
import vip.com.vipmeetings.evacuate.IpAddress;
import android.util.Log;
import java.util.UUID;






public  abstract class BlunoLibrary extends BlunoBaseActivity implements
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    public static final int ERROR_DIALOG_REQUEST = 1253;
    // public static final String Relay = "RELAY";
    public static final String Relay = "VIPdoorRELAY";
    // public static final String Relay = "<VIPdoorRELAY>1";
    public static final int REQUEST_CHECK_SETTINGS = 1111;
    public static final int REQUEST_LOC = 1000;
    GoogleApiClient mGoogleApiClient;
    LocationSettingsRequest.Builder builder;
    LocationRequest mLocationRequest;
    BluetoothManager bluetoothManager;

    public boolean statusOfGPS;

    public PlainProtocol mPlainProtocol = new PlainProtocol();

    public ArrayList<BluetoothDevice> mLeDevices;

    public abstract void onConectionStateChange(connectionStateEnum theconnectionStateEnum);

    public abstract void onSerialReceived(String theString);

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void serialSend(String theString) {

        Log.d("BLUNO", "→ serialSend() got: " + theString);

        IpAddress.e("serial send" + theString+mConnectionState.name());
        if (mConnectionState == connectionStateEnum.isConnected) {
            try {
                mSCharacteristic.setValue(theString);
                mBluetoothLeService.writeCharacteristic(mSCharacteristic);
            } catch (Exception e) {
                IpAddress.e("serial send" + e.toString());
            }
        }
    }

    private int mBaudrate = 115200;    //set the default baud rate to 115200
    private String mPassword = "AT+PASSWOR=DFRobot\r\n";


    private String mBaudrateBuffer = "AT+CURRUART=" + mBaudrate + "\r\n";

//	byte[] mBaudrateBuffer={0x32,0x00,(byte) (mBaudrate & 0xFF),(byte) ((mBaudrate>>8) & 0xFF),(byte) ((mBaudrate>>16) & 0xFF),0x00};;


    public void serialBegin(int baud) {
        mBaudrate = baud;
        mBaudrateBuffer = "AT+CURRUART=" + mBaudrate + "\r\n";
    }


    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }

    public static BluetoothGattCharacteristic mSCharacteristic, mModelNumberCharacteristic, mSerialPortCharacteristic, mCommandCharacteristic;
    BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    public LeDeviceListAdapter mLeDeviceListAdapter = null;
    public BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;

    private ScanCallback       scanCallback;
    public boolean mScanning = false;
    private String mDeviceName;
    private String mDeviceAddress;

    public enum connectionStateEnum {isNull, isScanning, isToScan, isConnecting, isConnected, isDisconnecting}

    ;
    public connectionStateEnum mConnectionState = connectionStateEnum.isNull;
    public static final int REQUEST_ENABLE_BT = 1;

    public Handler mHandler = new Handler();

    public boolean mConnected = false;

    private final static String TAG = BlunoLibrary.class.getSimpleName();

    private Runnable mConnectingOverTimeRunnable = new Runnable() {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void run() {
            if (mConnectionState == connectionStateEnum.isConnecting)
                mConnectionState = connectionStateEnum.isToScan;
            onConectionStateChange(mConnectionState);
            mBluetoothLeService.close();
        }
    };

    public Runnable mDisonnectingOverTimeRunnable = new Runnable() {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void run() {
            if (mConnectionState == connectionStateEnum.isDisconnecting)
                mConnectionState = connectionStateEnum.isToScan;
            onConectionStateChange(mConnectionState);
            mBluetoothLeService.close();
        }
    };

    public static final String SerialPortUUID = "0000dfb1-0000-1000-8000-00805f9b34fb";
    public static final String CommandUUID = "0000dfb2-0000-1000-8000-00805f9b34fb";
    public static final String ModelNumberStringUUID = "00002a24-0000-1000-8000-00805f9b34fb";
    private static final UUID BLUNO_SERVICE_UUID = UUID.fromString("0000dfb0-0000-1000-8000-00805f9b34fb");


    public void onCreateProcess() {
        if (!initiate()) {
            Toast.makeText(getApplicationContext(), R.string.error_bluetooth_not_supported,
                    Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        Intent gattServiceIntent = new Intent(BlunoLibrary.this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);



        // Initializes and show the scan Device Dialog
//        mScanDeviceDialog = new AlertDialog.Builder(mainContext)
//                .setTitle("BLE Device Scan...").setAdapter(mLeDeviceListAdapter, new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        IpAddress.e("which" + which);
//                        onclick(which);
//                    }
//                })
//                .setOnCancelListener(new DialogInterface.OnCancelListener() {
//
//                    @Override
//                    public void onCancel(DialogInterface arg0) {
//                        IpAddress.e("mBluetoothAdapter.stopLeScan");
//
//                        mConnectionState = connectionStateEnum.isToScan;
//                        onConectionStateChange(mConnectionState);
//                        mScanDeviceDialog.dismiss();
//
//                        scanLeDevice(false);
//                    }
//                }).create();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onclick(int which) {

        try {
            final BluetoothDevice device = mLeDeviceListAdapter.getDevice(which);
            if (device != null) {
                mDeviceName = device.getName();
                mDeviceAddress = device.getAddress();
                if (mDeviceName.equals("No Device Available") && mDeviceAddress.equals("No Address Available")) {
                    mConnectionState = connectionStateEnum.isToScan;
                    onConectionStateChange(mConnectionState);
                } else {
                    mLeDeviceListAdapter.notifyDataSetChanged();
                    scanLeDevice(false);
                }
            }
        } catch (Exception e) {

        }

    }


    public String getmDeviceAddress() {
        return mDeviceAddress;
    }

    public void setmDeviceAddress(String mDeviceAddress) {
        this.mDeviceAddress = mDeviceAddress;
    }

    public void onResumeProcess() {
        IpAddress.e("BlUNOActivity","onResume");
        // Ensures Bluetooth is enabled on the device. If Bluetooth is not
        // currently enabled,
        // fire an intent to display a dialog asking the user to grant
        // permission to enable it.

        // if (!mBluetoothAdapter.isEnabled()|| ) {

        permission23();

//            }

        // if (statusOfGPS) {

        //onLocation();
//        }
//        else {
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(BlunoLibrary.this);
//
//                builder.setMessage("please enable location");
//
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//
//                        try {
//                            // startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//
//                            Intent callGPSSettingIntent = new
//                                    Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            startActivityForResult(callGPSSettingIntent, 1000);
//                        } catch (Exception e) {
//
//                        }
//
//
//                    }
//                });
//                builder.show();
//            }


    }


    private static final int REQUEST_ALL_PERMS = REQUEST_LOC;  // reuse your existing REQUEST_LOC constant

    private void permission23() {
        // List every “dangerous” permission we need on Android 12+
        String[] perms = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
        };

        // If any are missing, ask for them all at once
        if (!hasAllPermissions(perms)) {
            ActivityCompat.requestPermissions(this, perms, REQUEST_ALL_PERMS);
        } else {
            // All permissions already granted → proceed
            onLocation();
        }
    }

    // Helper to check that each permission is granted
    private boolean hasAllPermissions(String[] perms) {
        for (String p : perms) {
            if (ActivityCompat.checkSelfPermission(this, p)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    //Below Code commented by Praveen Starts For - Multiple Blunos Connection on Android 12 - SDK 31
/*
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOC) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                onLocation();
            } else {

                permission23();
            }


        } else {
            permission23();
        }


    }
*/
    //Above Code commented by praveen Ends  For - Multiple Blunos Connection on Android 12 - SDK 31

    public void onLocation() {


        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationSettingsRequest.Builder builder = new
                LocationSettingsRequest.Builder()
                .setNeedBle(true)
                .setAlwaysShow(true)
                .addLocationRequest(mLocationRequest);



//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this,this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//
//                .build();

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                //  Toast.makeText(BlunoLibrary.this, "addOnSuccessListener", Toast.LENGTH_SHORT).show();
                try {

                    if(bluetoothManager!=null) {

                        if(mBluetoothAdapter==null)
                            mBluetoothAdapter = bluetoothManager.getAdapter();

                        if (!mBluetoothAdapter.isEnabled()) {
                            //1253
                            Intent enableBtIntent = new Intent(
                                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                        } else {
                            onRetro();
                        }
                    }

                } catch (Exception e) {

                    IpAddress.e("error" + e.toString());

                }
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //   Toast.makeText(BlunoLibrary.this, "addOnFailureListener", Toast.LENGTH_SHORT).show();
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(BlunoLibrary.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });


        //  mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {


        IpAddress.e("onconnected");
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates lo = result.getLocationSettingsStates();
                IpAddress.e("status" + status.getStatusCode());
                switch (status.getStatusCode()) {

                    case LocationSettingsStatusCodes.SUCCESS:

                        try {

                            if(bluetoothManager!=null) {

                                if(mBluetoothAdapter==null)
                                    mBluetoothAdapter = bluetoothManager.getAdapter();
                                if (!mBluetoothAdapter.isEnabled()) {
                                    //1253
                                    Intent enableBtIntent = new Intent(
                                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                                } else {
                                    onRetro();
                                }
                            }

                        } catch (Exception e) {

                            IpAddress.e("error" + e.toString());

                        }
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    BlunoLibrary.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.

                        break;
                }
            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {


        mGoogleApiClient.connect();

    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
////        final LocationSettingsStates states = LocationSettingsStates.fromIntent(intent);
//
//
//        switch (requestCode)
//        {
//            case 1000:
//
//                if(resultCode==RESULT_OK)
//                {
//
//                    IpAddress.e("onloc true");
//                    onLocation();
//                }
//                break;
//        }
//
//    }


    public void onPauseProcess() {
        IpAddress.e("BLUNOActivity onPause");
        //scanLeDevice(false);
        //if(mGattUpdateReceiver!=null)
        // unregisterReceiver(mGattUpdateReceiver);
//        if(mLeDeviceListAdapter!=null)
//        mLeDeviceListAdapter.clear();
//        mConnectionState = connectionStateEnum.isToScan;
        //  onConectionStateChange(mConnectionState);
//        if (mBluetoothLeService != null) {
//            mBluetoothLeService.disconnect();
//            mHandler.postDelayed(mDisonnectingOverTimeRunnable, 10000);
//        }
        // mSCharacteristic = null;

    }


    public void onStopProcess() {
        IpAddress.e("MiUnoActivity onStop");
        try {
            if (mBluetoothLeService != null) {
//			mBluetoothLeService.disconnect();
//            mHandler.postDelayed(mDisonnectingOverTimeRunnable, 10000);
                //  unregisterReceiver(mGattUpdateReceiver);
                //  mHandler.removeCallbacks(mDisonnectingOverTimeRunnable);
                // mBluetoothLeService.close();
            }
        } catch (Exception e) {

        }
        // mSCharacteristic = null;
    }


//    public void onActivityResultProcess(int requestCode, int resultCode, Intent data) {
//        // User chose not to enable Bluetooth.
//        if (requestCode == REQUEST_ENABLE_BT
//                && resultCode == Activity.RESULT_CANCELED) {
//            ((Activity) mainContext).finish();
//            return;
//        }
//    }


    boolean initiate() {
        // Use this check to determine whether BLE is supported on the device.
        // Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false;
        }

        // Initializes a Bluetooth adapter. For API level 18 and above, get a
        // reference to
        // BluetoothAdapter through BluetoothManager.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

            if(bluetoothManager==null)
                bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

            if(mBluetoothAdapter==null)
                mBluetoothAdapter = bluetoothManager.getAdapter();
            bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        }

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            return false;
        }
        return true;
    }

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.

    public final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @SuppressLint("DefaultLocale")
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d("BLUNO-GATT", "Received GATT event: " + action);
            switch(action) {
                case BluetoothLeService.ACTION_GATT_CONNECTED:
                    Log.d("BLUNO-GATT","→ GATT Connected");
                    break;
                case BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED:
                    Log.d("BLUNO-GATT","→ Services Discovered");
                    break;
                case BluetoothLeService.ACTION_DATA_AVAILABLE:
                    String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                    Log.d("BLUNO-GATT","→ DATA_AVAILABLE: " + data);
                    break;
                // …etc…
            }
            IpAddress.e("mGattUpdateReceiver->onReceive->action=" + action);
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                // mHandler.removeCallbacks(mConnectingOverTimeRunnable);

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                // mConnectionState = connectionStateEnum.isToScan;
                // onConectionStateChange(mConnectionState);
                // mHandler.removeCallbacks(mDisonnectingOverTimeRunnable);
                // mBluetoothLeService.close();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                for (BluetoothGattService gattService : mBluetoothLeService.getSupportedGattServices()) {
                    IpAddress.e("ACTION_GATT_SERVICES_DISCOVERED  " +
                            gattService.getUuid().toString());
                }
                getGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                if (mSCharacteristic == mModelNumberCharacteristic) {
                    if (intent.getStringExtra(BluetoothLeService.EXTRA_DATA).toUpperCase().
                            startsWith("DF BLUNO")) {
                        mBluetoothLeService.setCharacteristicNotification(mSCharacteristic, false);
                        mSCharacteristic = mCommandCharacteristic;
                        mSCharacteristic.setValue(mPassword);
                        mBluetoothLeService.writeCharacteristic(mSCharacteristic);
                        mSCharacteristic.setValue(mBaudrateBuffer);
                        mBluetoothLeService.writeCharacteristic(mSCharacteristic);
                        mSCharacteristic = mSerialPortCharacteristic;
                        mBluetoothLeService.setCharacteristicNotification(mSCharacteristic, true);
                        mConnectionState = connectionStateEnum.isConnected;
                        onConectionStateChange(mConnectionState);

                    } else if (intent.getStringExtra(BluetoothLeService.EXTRA_DATA).toUpperCase().
                            contains("DF")) {
                        mBluetoothLeService.setCharacteristicNotification(mSCharacteristic, false);
                        mSCharacteristic = mCommandCharacteristic;
                        mSCharacteristic.setValue(mPassword);
                        mBluetoothLeService.writeCharacteristic(mSCharacteristic);
                        mSCharacteristic.setValue(mBaudrateBuffer);
                        mBluetoothLeService.writeCharacteristic(mSCharacteristic);
                        mSCharacteristic = mSerialPortCharacteristic;
                        mBluetoothLeService.setCharacteristicNotification(mSCharacteristic, true);
                        mConnectionState = connectionStateEnum.isConnected;
                        onConectionStateChange(mConnectionState);

                    }
                } else if (mSCharacteristic == mSerialPortCharacteristic) {
                    IpAddress.e("serial recive");
                    onSerialReceived(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                    IpAddress.e("displayData " + intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                }


            }
        }
    };


    boolean isStarted=false;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @SuppressLint("MissingPermission")
    void scanLeDevice(boolean enable) {
        // --- Android-14+ BLE scan setup ---
        if (enable) {
            // 1) build the callback
            scanCallback = new ScanCallback() {
                @Override
                public void onScanResult(int cbType, ScanResult result) {
                    ScanRecord record = result.getScanRecord();
                    String name = record != null ? record.getDeviceName() : null;
                    String addr = result.getDevice().getAddress();
                    Log.d(TAG, String.format("ScanResult: name=%s addr=%s rssi=%d", name, addr, result.getRssi()));

                    if ("Bluno".equalsIgnoreCase(name)) {
                        Log.d(TAG, ">>> Bluno found: connecting to " + addr);
                        bluetoothLeScanner.stopScan(this);
                        mBluetoothLeService.connect(result.getDevice().getAddress());
                    }
                }

                @Override
                public void onScanFailed(int errorCode) { /* you can log this */ }
            };

            // 2) (optional) filter by your Bluno service UUID
            ScanFilter filter = new ScanFilter.Builder()
                    .setServiceUuid(new ParcelUuid(BLUNO_SERVICE_UUID))
                    .build();
            ScanSettings settings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();

            // 3) start the modern scan
            bluetoothLeScanner.startScan(
                    Collections.singletonList(filter),
                    settings,
                    scanCallback
            );
        } else {
            // stop it
            bluetoothLeScanner.stopScan(scanCallback);
        }
// --- end replacement ---

//                    IpAddress.e("mBluetoothAdapter.startLeScan2");
//                    bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
//                    if (bluetoothLeScanner != null) {
//
//
//
//
//                        bluetoothLeScanner.startScan(new ScanCallback() {
//
//
//                            @Override
//                            public void onBatchScanResults(List<ScanResult> results) {
//                                super.onBatchScanResults(results);
//                            }
//
//                            @Override
//                            public void onScanResult(int callbackType, ScanResult result) {
//                                super.onScanResult(callbackType, result);
//                                if (result != null) {
//
//                                  //  Toast.makeText(getApplicationContext(),
//                                         //   "scan started", Toast.LENGTH_SHORT).show();
//
//                                   // Toast.makeText(BlunoLibrary.this, "new code device " +
//                                        //    result.getDevice().getAddress(), Toast.LENGTH_SHORT).show();
//                                    mLeDeviceListAdapter.addDevice(result.getDevice());
//                                }
//                            }
//                        });
//
//              }

        //  }


        // } else {
        // Stops scanning after a pre-defined scan period.

//                if (mLeDeviceListAdapter != null) {
//                    mLeDeviceListAdapter.clear();
//                    mLeDeviceListAdapter.notifyDataSetChanged();
//                }


        //   if (Build.VERSION.SDK_INT < 21) {


//                } else {
//
//                    IpAddress.e("mBluetoothAdapter.stop2");
//                    bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
//                    bluetoothLeScanner.stopScan(new ScanCallback() {
//                        @Override
//                        public void onScanResult(int callbackType, ScanResult result) {
//                            super.onScanResult(callbackType, result);
//
//
//                        }
//                    });
//                }

    }

    // Code to manage Service lifecycle.
    public final ServiceConnection mServiceConnection = new ServiceConnection() {


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.d("BLUNO-SERVICE", "BluetoothLeService connected");
            IpAddress.e("mServiceConnection onServiceConnected");
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                IpAddress.e("Unable to initialize Bluetooth");
                finish();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            IpAddress.e("mServiceConnection onServiceDisconnected");
            mBluetoothLeService = null;
        }
    };

    // Device scan callback.


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void getGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid;
        mModelNumberCharacteristic = null;
        mSerialPortCharacteristic = null;
        mCommandCharacteristic = null;
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            uuid = gattService.getUuid().toString();
            IpAddress.e("displayGattServices + uuid=" + uuid);

            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                uuid = gattCharacteristic.getUuid().toString();
                if (uuid.equals(ModelNumberStringUUID)) {
                    mModelNumberCharacteristic = gattCharacteristic;
                    IpAddress.e("mModelNumberCharacteristic  " + mModelNumberCharacteristic.getUuid().toString());
                } else if (uuid.equals(SerialPortUUID)) {
                    mSerialPortCharacteristic = gattCharacteristic;
                    IpAddress.e("mSerialPortCharacteristic  " + mSerialPortCharacteristic.getUuid().toString());
//                    updateConnectionState(R.string.comm_establish);
                } else if (uuid.equals(CommandUUID)) {
                    mCommandCharacteristic = gattCharacteristic;
                    IpAddress.e("mSerialPortCharacteristic  " + mSerialPortCharacteristic.getUuid().toString());
//                    updateConnectionState(R.string.comm_establish);
                }
            }
            mGattCharacteristics.add(charas);
        }

        if (mModelNumberCharacteristic == null || mSerialPortCharacteristic == null || mCommandCharacteristic == null) {
            // Toast.makeText(mainContext, "Please select DFRobot devices", Toast.LENGTH_SHORT).show();
            mConnectionState = connectionStateEnum.isToScan;
            onConectionStateChange(mConnectionState);
        } else {
            mSCharacteristic = mModelNumberCharacteristic;
            mBluetoothLeService.setCharacteristicNotification(mSCharacteristic, true);
            mBluetoothLeService.readCharacteristic(mSCharacteristic);
        }

    }

    public static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    public class LeDeviceListAdapter extends BaseAdapter {


        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {

            // Toast.makeText(BlunoLibrary.this,device.getName()+"",Toast.LENGTH_LONG).show();

            if (!mLeDevices.contains(device)) {
                if (device.getName() != null && device.getName().equalsIgnoreCase("Bluno")) {
                    IpAddress.e("device added" + device.getName());
                    // Toast.makeText(getApplicationContext(), "device added", Toast.LENGTH_SHORT).show();
                    mLeDevices.add(device);
                    notifyDataSetChanged();
                }else if (device.getName() != null && device.getName().equalsIgnoreCase("BlunoMega")) {
                    IpAddress.e("device added" + device.getName());
                    // Toast.makeText(getApplicationContext(), "device added", Toast.LENGTH_SHORT).show();
                    mLeDevices.add(device);
                    notifyDataSetChanged();
                }else if (device.getName() != null && device.getName().contains("Bluno")) {
                    IpAddress.e("device added" + device.getName());
                    // Toast.makeText(getApplicationContext(), "device added", Toast.LENGTH_SHORT).show();
                    mLeDevices.add(device);
                    notifyDataSetChanged();
                }
            }else
            {
                // notifyDataSetChanged();
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view
                        .findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view
                        .findViewById(R.id.device_name);
                IpAddress.e("mInflator.inflate  getView");
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0) {
                viewHolder.deviceName.setText(deviceName);
            } else {
                viewHolder.deviceName.setText(R.string.unknown_device);
            }
            viewHolder.deviceAddress.setText(device.getAddress());

            if (i == 0) {
                IpAddress.e("called blunolibrary1");
            }

            return view;
        }


        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            IpAddress.e("device added" + mLeDevices.size());
            if (mLeDevices != null && mLeDevices.size() > 0) {

                getList(mLeDevices);
                // Toast.makeText(getApplicationContext(), "get list", Toast.LENGTH_SHORT).show();
            } else {


            }

        }
    }

    public abstract void onRetro();

    public abstract void onConnect();

    public abstract void getList(List<BluetoothDevice> bluetoothDevices);
//praveen starts - Multiple Blunos Connection on Android 12 - SDK 31

    private List<String>  mPerList   = new ArrayList<>();
    private List<String>  mPerNoList = new ArrayList<>();

    private  OnPermissionsResult permissionsResult;
    private  int requestCode;

    public void request(int requestCode, OnPermissionsResult permissionsResult){
        if(!checkPermissionsAll()){
            requestPermissionAll(requestCode, permissionsResult);
        }
    }


    protected boolean checkPermissions(String permissions){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int check = checkSelfPermission(permissions);
            return check == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }


    protected boolean checkPermissionsAll(){
        mPerList.clear();
        for(int i = 0; i < mStrPermission.length; i++ ){
            boolean check = checkPermissions(mStrPermission[i]);
            if(!check){
                mPerList.add(mStrPermission[i]);
            }
        }
        return mPerList.size() > 0 ? false : true;
    }


    protected void requestPermission(String[] mPermissions, int requestCode){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(mPermissions,requestCode);
        }
    }


    protected void requestPermissionAll(int requestCode, OnPermissionsResult permissionsResult){
        this.permissionsResult = permissionsResult;
        requestPermission((String[]) mPerList.toArray(new String[mPerList.size()]),requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_ALL_PERMS) {
            // Verify they granted every permission
            boolean allGranted = grantResults.length > 0;
            for (int res : grantResults) {
                if (res != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                // Now that we have Location + BLUETOOTH_SCAN + BLUETOOTH_CONNECT:
                onLocation();
            } else {
                Toast.makeText(this,
                        "App needs Location and Bluetooth permissions to function.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }


    public interface OnPermissionsResult{
        void OnSuccess();
        void OnFail(List<String> noPermissions);
    }
    private  String [] mStrPermission = {
            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.BLUETOOTH_SCAN,Manifest.permission.BLUETOOTH_CONNECT
    };


    //praveen Ends - Multiple Blunos Connection on Android 12 - SDK 31
}

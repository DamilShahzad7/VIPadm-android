/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vip.com.vipmeetings.bluno;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.util.List;


/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class BluetoothLeService extends Service {
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    BluetoothGatt mBluetoothGatt;
    public String mBluetoothDeviceAddress;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    public int mConnectionState = STATE_DISCONNECTED;


    //To tell the onCharacteristicWrite call back function that this is a new characteristic,
    //not the Write Characteristic to the device successfully.
    private static final int WRITE_NEW_CHARACTERISTIC = -1;
    //define the limited length of the characteristic.
    private static final int MAX_CHARACTERISTIC_LENGTH = 17;
    //Show that Characteristic is writing or not.
    private boolean mIsWritingCharacteristic=false;

    //class to store the Characteristic and content string push into the ring buffer.
    private class BluetoothGattCharacteristicHelper{
    	BluetoothGattCharacteristic mCharacteristic;
    	String mCharacteristicValue;
    	BluetoothGattCharacteristicHelper(BluetoothGattCharacteristic characteristic, String characteristicValue){
    		mCharacteristic=characteristic;
    		mCharacteristicValue=characteristicValue;
    	}
    }
    //ring buffer
    private RingBuffer<BluetoothGattCharacteristicHelper> mCharacteristicRingBuffer = new RingBuffer<BluetoothGattCharacteristicHelper>(8);

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";
//    public final static UUID UUID_HEART_RATE_MEASUREMENT =
//            UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d("BLE-SVC", "→ GATT_CALLBACK: STATE_CONNECTED (status=" + status + ")");
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(ACTION_GATT_CONNECTED);

                // Immediately start service discovery
                Log.d("BLE-SVC", "→ GATT_CALLBACK: calling discoverServices()");
                if (mBluetoothGatt.discoverServices()) {
                    Log.d("BLE-SVC", "→ discoverServices(): initiated successfully");
                } else {
                    Log.w("BLE-SVC", "→ discoverServices(): failed to start");
                }

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d("BLE-SVC", "→ GATT_CALLBACK: STATE_DISCONNECTED (status=" + status + ")");
                mConnectionState = STATE_DISCONNECTED;
                broadcastUpdate(ACTION_GATT_DISCONNECTED);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d("BLE-SVC", "→ GATT_CALLBACK: onServicesDiscovered (status=" + status + ")");

            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w("BLE-SVC", "→ GATT_CALLBACK: onServicesDiscovered failed, status=" + status);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic,
                                          int status) {
            Log.d("BLE-SVC", "→ GATT_CALLBACK: onCharacteristicWrite uuid=" +
                    characteristic.getUuid() + " | status=" + status);

            synchronized(this) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    // Proceed with next write from ring buffer (or finish)
                    if (mCharacteristicRingBuffer.isEmpty()) {
                        mIsWritingCharacteristic = false;
                    } else {
                        // Pop next and write it
                        BluetoothGattCharacteristicHelper helper = mCharacteristicRingBuffer.next();
                        String toSend = helper.mCharacteristicValue;
                        if (toSend.length() > MAX_CHARACTERISTIC_LENGTH) {
                            String part = toSend.substring(0, MAX_CHARACTERISTIC_LENGTH);
                            try {
                                helper.mCharacteristic.setValue(part.getBytes("ISO-8859-1"));
                            } catch (UnsupportedEncodingException e) {
                                throw new IllegalStateException(e);
                            }
                            mBluetoothGatt.writeCharacteristic(helper.mCharacteristic);
                            Log.d("BLE-SVC", "→ writeCharacteristic (partial) uuid=" +
                                    helper.mCharacteristic.getUuid() + " data=" + part);
                            helper.mCharacteristicValue = toSend.substring(MAX_CHARACTERISTIC_LENGTH);
                        } else {
                            try {
                                helper.mCharacteristic.setValue(toSend.getBytes("ISO-8859-1"));
                            } catch (UnsupportedEncodingException e) {
                                throw new IllegalStateException(e);
                            }
                            mBluetoothGatt.writeCharacteristic(helper.mCharacteristic);
                            Log.d("BLE-SVC", "→ writeCharacteristic (final) uuid=" +
                                    helper.mCharacteristic.getUuid() + " data=" + toSend);
                            helper.mCharacteristicValue = "";
                            mCharacteristicRingBuffer.pop();
                        }
                    }
                } else if (status == WRITE_NEW_CHARACTERISTIC) {
                    mIsWritingCharacteristic = true;
                    if (!mCharacteristicRingBuffer.isEmpty()) {
                        BluetoothGattCharacteristicHelper helper = mCharacteristicRingBuffer.next();
                        String toSend = helper.mCharacteristicValue;
                        if (toSend.length() > MAX_CHARACTERISTIC_LENGTH) {
                            String part = toSend.substring(0, MAX_CHARACTERISTIC_LENGTH);
                            try {
                                helper.mCharacteristic.setValue(part.getBytes("ISO-8859-1"));
                            } catch (UnsupportedEncodingException e) {
                                throw new IllegalStateException(e);
                            }
                            mBluetoothGatt.writeCharacteristic(helper.mCharacteristic);
                            Log.d("BLE-SVC", "→ writeCharacteristic (partial) uuid=" +
                                    helper.mCharacteristic.getUuid() + " data=" + part);
                            helper.mCharacteristicValue = toSend.substring(MAX_CHARACTERISTIC_LENGTH);
                        } else {
                            try {
                                helper.mCharacteristic.setValue(toSend.getBytes("ISO-8859-1"));
                            } catch (UnsupportedEncodingException e) {
                                throw new IllegalStateException(e);
                            }
                            mBluetoothGatt.writeCharacteristic(helper.mCharacteristic);
                            Log.d("BLE-SVC", "→ writeCharacteristic (final) uuid=" +
                                    helper.mCharacteristic.getUuid() + " data=" + toSend);
                            helper.mCharacteristicValue = "";
                            mCharacteristicRingBuffer.pop();
                        }
                    }
                    if (mCharacteristicRingBuffer.isFull()) {
                        mCharacteristicRingBuffer.clear();
                        mIsWritingCharacteristic = false;
                    }
                } else {
                    mCharacteristicRingBuffer.clear();
                    Log.w("BLE-SVC", "→ onCharacteristicWrite failed status=" + status);
                }
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            Log.d("BLE-SVC", "→ GATT_CALLBACK: onCharacteristicRead uuid=" +
                    characteristic.getUuid() + " | status=" + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void  onDescriptorWrite(BluetoothGatt gatt,
        								BluetoothGattDescriptor characteristic,
        								int status){
            Log.d("BLE-SVC", "→ GATT_CALLBACK: onDescriptorWrite uuid=" +
                    characteristic.getUuid() + " | status=" + status);
        }
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            Log.d("BLE-SVC", "→ GATT_CALLBACK: onCharacteristicChanged uuid=" +
                    characteristic.getUuid() + " | value=" +
                    new String(characteristic.getValue()));
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    };

    private void broadcastUpdate(final String action) {
        Log.d("BLE‐SVC→BC", "About to send broadcast: " + action);
        final Intent intent = new Intent(action);
        intent.setPackage(getPackageName());
        sendBroadcast(intent);
        Log.d("BLE-SVC→BC", "Sent broadcast: " + action);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        intent.setPackage(getPackageName());
        System.out.println("BluetoothLeService broadcastUpdate");
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                String asString = new String(data);
                intent.putExtra(EXTRA_DATA, new String(data));
                Log.d("BLE-SVC", "→ broadcastUpdate: " + action + "  | data=" + asString);
        		sendBroadcast(intent);
            }
            else {
                Log.d("BLE-SVC", "→ broadcastUpdate: " + action + "  | no data");
                sendBroadcast(intent);
            }
    }

    public class LocalBinder extends Binder {
        BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.

        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     *         is reported asynchronously through the
     *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *         callback.
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean connect(final String address) {
        Log.d("BLE-SVC", "→ connect(address=" + address + ")  | mBluetoothGatt was=" + mBluetoothGatt);

        if (mBluetoothAdapter == null || address == null) {
            Log.w("BLE-SVC", "→ connect() failed: adapter not initialized or address null");
            return false;
        }

         // todo Previously connected device.  Try to reconnect.
//        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
//                && mBluetoothGatt != null) {
//            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
//            if (mBluetoothGatt.connect()) {
//            	System.out.println("mBluetoothGatt connect");
//                mConnectionState = STATE_CONNECTING;
//                return true;
//            } else {
//            	System.out.println("mBluetoothGatt else connect");
//                return false;
//            }
//        }

         BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w("BLE-SVC", "→ connect() failed: device not found " + address);
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        System.out.println("device.connectGatt connect");
		synchronized(this)
		{
			mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
		}
        Log.d("BLE-SVC", "→ connect(): mBluetoothGatt=" + mBluetoothGatt);
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void disconnect() {
    	System.out.println("BluetoothLeService disconnect");

        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void close() {
    	System.out.println("BluetoothLeService close");

        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }


    /**
     * Write information to the device on a given {@code BluetoothGattCharacteristic}. The content string and characteristic is
     * only pushed into a ring buffer. All the transmission is based on the {@code onCharacteristicWrite} call back function,
     * which is called directly in this function
     *
     * @param characteristic The characteristic to write to.
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized");
            return;
        }

    	//The character size of TI CC2540 is limited to 17 bytes, otherwise characteristic can not be sent properly,
    	//so String should be cut to comply this restriction. And something should be done here:
        String writeCharacteristicString;
        try {
        	writeCharacteristicString = new String(characteristic.getValue(),"ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            // this should never happen because "US-ASCII" is hard-coded.
            throw new IllegalStateException(e);
        }
        Log.d(TAG, "✉ allwriteCharacteristicString: " + writeCharacteristicString);

        // 3) Force NO_RESPONSE (most BLE UARTs expect this)
        characteristic.setWriteType(
                BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
        );

        // 4) Try a direct write first (bypass the ring buffer for a sanity check)
        boolean directOk = mBluetoothGatt.writeCharacteristic(characteristic);
        Log.d(TAG, "✉ direct writeCharacteristic() returned: " + directOk);

        //As the communication is asynchronous content string and characteristic should be pushed into an ring buffer for further transmission
    	mCharacteristicRingBuffer.push(new BluetoothGattCharacteristicHelper(characteristic,writeCharacteristicString) );
        Log.d(TAG, "♻ ring-buffer size now: " +
                +                  mCharacteristicRingBuffer.size());


    	//The progress of onCharacteristicWrite and writeCharacteristic is almost the same. So callback function is called directly here
    	//for details see the onCharacteristicWrite function
    	mGattCallback.onCharacteristicWrite(mBluetoothGatt, characteristic, WRITE_NEW_CHARACTERISTIC);

    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        //BluetoothGattDescriptor descriptor = characteristic.getDescriptor(characteristic.getUuid());
        //descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        //mBluetoothGatt.writeDescriptor(descriptor);

        // This is specific to Heart Rate Measurement.
//        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
//            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
//                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
//            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//            mBluetoothGatt.writeDescriptor(descriptor);
//        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }


}

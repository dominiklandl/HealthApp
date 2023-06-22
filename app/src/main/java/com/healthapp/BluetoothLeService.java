package com.healthapp;

import android.Manifest;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

public class BluetoothLeService extends Service {

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICE_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    private int connectionState;
    private final IBinder binder = new LocalBinder();
    private BluetoothGatt bluetoothGatt;
    private BluetoothAdapter BTAdapter;
    private BluetoothManager BTManager;
    private List<BluetoothGattService> gattServices;
    private BluetoothGattCharacteristic gattCharacteristics;
    public String BluetoothDeviceAddress;
    private byte[] charValue;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    private final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            System.out.println("GattCallback called");
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // successfully connected to the GATT Server
                connectionState = STATE_CONNECTED;
                broadcastUpdate(ACTION_GATT_CONNECTED);
                bluetoothGatt.discoverServices();
                System.out.println("Connected");
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // disconnected from the GATT Server
                connectionState = STATE_DISCONNECTED;
                broadcastUpdate(ACTION_GATT_DISCONNECTED);
                System.out.println("Not Connectet");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if(status == BluetoothGatt.GATT_SUCCESS){
                broadcastUpdate(ACTION_GATT_SERVICE_DISCOVERED);
                System.out.println("Service Discovered");
                gattServices = gatt.getServices();
                gattCharacteristics = gattServices.get(2).getCharacteristics().get(0);
                gatt.readCharacteristic(gattServices.get(0).getCharacteristics().get(0));
            }else{
                Toast.makeText(getApplicationContext(), "onServiceDiscovered received: "+status, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            System.out.println("characteristics received");
            super.onCharacteristicRead(gatt, characteristic, status);
            charValue = characteristic.getValue();
            System.out.println("Characeristics: "+charValue+charValue[0]);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            broadcastUpdate(ACTION_DATA_AVAILABLE,characteristic);
            final byte[] dataInput = characteristic.getValue();
            System.out.println("Characteristics changed: "+dataInput);
        }
    };

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    public boolean initialize() {
        BTManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BTAdapter = BTManager.getAdapter();
        if (BTAdapter == null) {
            return false;
        }
        return true;
    }

    public boolean connect(String address) {
        System.out.println("Connect called");
        if (BTAdapter == null || address == null) {
            System.out.println("Adapter null");
            return false;
        }
        // Previously connected device.  Try to reconnect.
        if (BluetoothDeviceAddress != null && address.equals(BluetoothDeviceAddress)
                && bluetoothGatt != null) {
            if (bluetoothGatt.connect()) {
                connectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }
        BluetoothDevice device = BTAdapter.getRemoteDevice(address);
        if (device == null) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
            bluetoothGatt = device.connectGatt(this,false,bluetoothGattCallback,2);
        }
        connectionState = STATE_CONNECTING;
        BluetoothDeviceAddress = address;
        return true;
    }


    public void disconnect() {
        if (BTAdapter == null || bluetoothGatt == null) {
            return;
        }
        bluetoothGatt.disconnect();
    }


    @Override
    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }

    public void close() {
        if (bluetoothGatt == null) {
            return;
        }
        bluetoothGatt.close();
        bluetoothGatt = null;
    }

    public List<BluetoothGattService> getSupportedGattServices(){
        if(bluetoothGatt==null){
            return null;
        }
        return bluetoothGatt.getServices();
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (bluetoothGatt == null) {
            Toast.makeText(getApplicationContext(), "BluetoothGatt not initialized", Toast.LENGTH_SHORT).show();
            return;
        }
        bluetoothGatt.setCharacteristicNotification(characteristic, enabled);
    }

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (bluetoothGatt == null) {
            Toast.makeText(getApplicationContext(), "BluetoothGatt not initialized", Toast.LENGTH_SHORT).show();
            return;
        }
        bluetoothGatt.readCharacteristic(characteristic);
    }

    public byte[] getCharacteristics(){
        readCharacteristic(gattCharacteristics);
        return charValue;
    }
}

package com.healthapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static android.bluetooth.BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE;
import static android.bluetooth.BluetoothAdapter.ACTION_SCAN_MODE_CHANGED;
import static android.bluetooth.BluetoothAdapter.ERROR;
import static android.bluetooth.BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION;
import static android.bluetooth.BluetoothAdapter.EXTRA_SCAN_MODE;
import static android.bluetooth.BluetoothAdapter.SCAN_MODE_CONNECTABLE;
import static android.bluetooth.BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE;
import static android.bluetooth.BluetoothAdapter.SCAN_MODE_NONE;
import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;
import static android.bluetooth.BluetoothAdapter.STATE_CONNECTING;

public class BluetoothLeActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private static final long SCAN_PERIOD = 10000;
    private final static int REQUEST_ENABLE_BT = 1;

    private BluetoothLeService bluetoothService = new BluetoothLeService();
    private String deviceAddress;
    boolean connected;

    private Button mScanBtn;
    private Button mListPairedDevicesBtn;
    private Button mDiscoverBtn;
    private Button mDisconnect;
    private ListView mDevicesListView;

    protected ArrayAdapter<String> mBTArrayAdapter;
    private BluetoothAdapter mBTAdapter = null;
    private BluetoothLeScanner mScanner;
    private Set<BluetoothDevice> mPairedDevices;
    private BluetoothFragment bluetoothFragment;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> GattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();


    private Handler mHandler = new Handler();
    private boolean scanning;

    private ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            System.out.println("Scan result");
            BluetoothDevice device = result.getDevice();
            mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            mBTArrayAdapter.notifyDataSetChanged();
            stopScan();
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            System.out.println("BLE// onBatchScanResults");
            for (ScanResult sr : results) {
                System.out.println("For-Schleife");
                BluetoothDevice device = sr.getDevice();
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                mBTArrayAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            System.out.println("BLE// onScanFailed");
            Toast.makeText(getApplicationContext(), "ScanFailed: " + errorCode, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bluetooth);

        mScanBtn = (Button) findViewById(R.id.btn_BT_ON);
        mListPairedDevicesBtn = (Button) findViewById(R.id.btn_show_paireddvc);
        mDiscoverBtn = (Button) findViewById(R.id.btn_discover_dvc);
        mDisconnect = (Button) findViewById(R.id.btn_disconnect);
        mDevicesListView = (ListView) findViewById(R.id.lv_devicelist);

        System.out.println("onCreate called");

        mBTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mDevicesListView.setAdapter(mBTArrayAdapter);
        mDevicesListView.setOnItemClickListener(mDeviceClickListener);

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBTAdapter = bluetoothManager.getAdapter();
        mScanner = mBTAdapter.getBluetoothLeScanner();

        System.out.println("Intent call");
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        System.out.println("Intent called");

        ActivityCompat.requestPermissions(
                this,
                new String[]
                        {
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        }, 0);

        if(mBTAdapter==null){
            Log.d(TAG,"Device does not support Bluetooth");
        }
        else {
            mScanBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ContextCompat.checkSelfPermission(BluetoothLeActivity.this, android.Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_DENIED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            ActivityCompat.requestPermissions(BluetoothLeActivity.this, new String[]{android.Manifest.permission.BLUETOOTH_ADVERTISE}, 2);
                            return;
                        }
                    }
                    if (ContextCompat.checkSelfPermission(BluetoothLeActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            ActivityCompat.requestPermissions(BluetoothLeActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                            return;
                        }
                    }
                    bluetoothDiscoverable();
                }
            });
            mListPairedDevicesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(BluetoothLeActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            ActivityCompat.requestPermissions(BluetoothLeActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                            return;
                        }
                    }
                    listPairedDevices();
                }
            });
            mDiscoverBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(BluetoothLeActivity.this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_DENIED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            ActivityCompat.requestPermissions(BluetoothLeActivity.this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 2);
                            return;
                        }
                    }
                    discover();
                }
            });
            mDisconnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bluetoothService.disconnect();
                    Toast.makeText(getApplicationContext(), "Disonnected", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter());
        if (bluetoothService != null) {
            final boolean result = bluetoothService.connect(deviceAddress);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(gattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //bluetoothService.close();
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
        unregisterReceiver(gattUpdateReceiver);
        unregisterReceiver(b1receiver);
    }

    protected void bluetoothDiscoverable() {
        Intent discoverableIntent = new Intent(ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(EXTRA_DISCOVERABLE_DURATION,300);
        startActivity(discoverableIntent);                                  //Null Object

        IntentFilter intentFilter = new IntentFilter(mBTAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(b1receiver,intentFilter);
        Toast.makeText(getApplicationContext(),"Device is now discoverable for 300 seconds",Toast.LENGTH_SHORT).show();
    }

    protected void discover(){
        ScanFilter filter = new ScanFilter.Builder().setDeviceName(null).build();
        ArrayList<ScanFilter> filters = new ArrayList<ScanFilter>();
        filters.add(filter);
        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                .setReportDelay(0)
                .build();
        if(!scanning){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanning = false;
                    mScanner.stopScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            scanning = true;
            mScanner.startScan(filters,settings,mLeScanCallback);
        }else{
            scanning = false;
            mScanner.stopScan(mLeScanCallback);
        }
    }

    protected void stopScan(){
        mBTAdapter.getBluetoothLeScanner().stopScan(mLeScanCallback);
    }
    protected void listPairedDevices(){
        //bluetoothFragment.mBTArrayAdapter.clear();
        mPairedDevices = mBTAdapter.getBondedDevices();   //Null Object
        if(mBTAdapter.isEnabled()) {
            // put it's one to the adapter
            for (BluetoothDevice device : mPairedDevices)
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
        }
        else
            Toast.makeText(getApplicationContext(), "BT not on", Toast.LENGTH_SHORT).show();
    }


    private final BroadcastReceiver b1receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(ACTION_SCAN_MODE_CHANGED)){
                int mode = intent.getIntExtra(EXTRA_SCAN_MODE, ERROR);

                switch (mode) {
                    //Device is in discoverable Mode
                    case SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Toast.makeText(getApplicationContext(), "Discoverability Enabled", Toast.LENGTH_SHORT).show();
                        break;
                    //Device not in discoverable mode
                    case SCAN_MODE_CONNECTABLE:
                        Toast.makeText(getApplicationContext(), "Discoverability Enabled. Able to receive connections", Toast.LENGTH_SHORT).show();
                        break;
                    case SCAN_MODE_NONE:
                        Toast.makeText(getApplicationContext(), "Discoverability Disabled. ", Toast.LENGTH_SHORT).show();
                        break;
                    case STATE_CONNECTING:
                        Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_SHORT).show();
                        break;
                    case STATE_CONNECTED:
                        Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver gattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                connected = true;
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                connected = false;
            }else if (BluetoothLeService.ACTION_GATT_SERVICE_DISCOVERED.equals(action)){
                //displayGattServices(bluetoothService.getSupportedGattServices());
            }
        }
    };


    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            System.out.println("Service connection called");
            bluetoothService = ((BluetoothLeService.LocalBinder) service).getService();
            if (bluetoothService != null) {
                if(!bluetoothService.initialize()){
                    Toast.makeText(getApplicationContext(), "No connection done", Toast.LENGTH_SHORT).show();
                    finish();
                }
                bluetoothService.connect(deviceAddress);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();
            bluetoothService = null;
        }
    };

    private IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        return intentFilter;
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String info = ((TextView) view).getText().toString();
            deviceAddress = info.substring(info.length() - 17);
            stopScan();
            Toast.makeText(getApplicationContext(), "MAC-Adress: "+deviceAddress, Toast.LENGTH_SHORT).show();
            bluetoothService.connect(deviceAddress);
        }
    };

}

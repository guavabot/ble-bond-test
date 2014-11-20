package com.guavabot.blebondingtest;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    private TextView mLogTv;

    private final DateFormat mTimeFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM);

    private final BroadcastReceiver mBondStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
            int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, -1);
            String msg = "Bond state change: state " + printBondState(state) +
                    ", previous state " + printBondState(prevState);
            Log.w("Bond state receiver", msg);
            mLogTv.append("\n\n" + mTimeFormat.format(new Date()) + ": " + msg);
        }

        private String printBondState(int state) {
            switch (state) {
                case BluetoothDevice.BOND_NONE:
                    return "BOND_NONE";
                case BluetoothDevice.BOND_BONDING:
                    return "BOND_BONDING";
                case BluetoothDevice.BOND_BONDED:
                    return "BOND_BONDED";
                default:
                    return String.valueOf(state);
            }
        }
    };

    private final BroadcastReceiver mPairRequestReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int variant = intent.getIntExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT, -1);
            String msg = "Pairing request, variant: " + printVariant(variant);
            Log.w("Pair request receiver", msg);
            mLogTv.append("\n\n" + mTimeFormat.format(new Date()) + ": " + msg);
        }

        private String printVariant(int variant) {
            switch (variant) {
                case BluetoothDevice.PAIRING_VARIANT_PIN:
                    return "PAIRING_VARIANT_PIN";
                default:
                    return String.valueOf(variant);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogTv = (TextView) findViewById(R.id.text_view);
        mLogTv.setText("Android " + Build.VERSION.RELEASE);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivity(intent);
            }
        });

        registerReceiver(mBondStateReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
        registerReceiver(mPairRequestReceiver, new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBondStateReceiver);
        unregisterReceiver(mPairRequestReceiver);
    }
}

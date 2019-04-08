package com.example.hadastourgeman.maps123;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;

public class AlarmReceiver extends BroadcastReceiver {
    String string = "1";
    BluetoothConnectionService mBluetoothConnection;
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");



        Toast.makeText(context, "NO",
                Toast.LENGTH_SHORT).show();
    }
}

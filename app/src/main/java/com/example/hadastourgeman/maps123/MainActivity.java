package com.example.hadastourgeman.maps123;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    DatabaseReference mRootRef;
    DatabaseReference dbRecRef;
    String string = "1";
    StringBuilder message = null;
    BluetoothConnectionService mBluetoothConnection;
    TextView des1;
    TextView des2;
    SeekBar sk1;
    SeekBar sk2;
    final static int RQS_TIME = 1;
    int[] values;

    TextView tvAlarmPrompt;
    TextView textView;
    int i, rngl, rngr, head;
    byte elev;
    String strngl, strngr, sthead, stelev, strAnotherOne;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        des1= (TextView) findViewById(R.id.des1);
        des2= (TextView) findViewById(R.id.des2);
        //textView= (TextView) findViewById(R.id.textView);
        sk1= (SeekBar) findViewById(R.id.sk1);
        sk2= (SeekBar) findViewById(R.id.sk2);
        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

        setContentView(R.layout.activity_main);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        dbRecRef=mRootRef.child("Records");

        rngl=0;
        rngr=200;
        head=0;
        elev=-90;



        message = new StringBuilder();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReciver, new IntentFilter("incomingMessage"));




    }


    public static boolean check(String s) {
        if (s.length() != 8)
            return false;
        for (int i = 0 ; i < 8; i++)
        {
            char c = s.charAt(i);
            if (!((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f')))
                return false;
        }
        return true;
    }

    public int[] buildArray(String s)
    {
        int[] arr = new int[4];
        for (int i = 0; i < 8; i+=2)
        {
            String num = ""+s.charAt(i) + s.charAt(i+1);
            arr[i/2] = Integer.parseInt(num , 16);
        }
        return arr;
    }

        /*BroadcastReceiver mReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String Text = intent.getStringExtra("theMessage");

                cancelAlarm();
                start();

                message.append(Text + "/");

                 textView.setText(message);

                while (Text != null) {
                    if (check(Text))
                    {
                        values = buildArray(Text);
                        des1.setText(values[0]);
                        des2.setText(values[1]);
                        sk1.setProgress(values[2]);
                        sk2.setProgress(values[3]);


                    }
                }
            }
        };*/


    public void tosq (View view){
        Intent n = new Intent(this, AlphaSQ.class);
        startActivity(n);
    }








    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();
            calSet.set(Calendar.SECOND, calSet.get(Calendar.SECOND)+2);
            if (calSet.compareTo(calNow) <= 0) {
                calSet.add(Calendar.DATE, 1);
            }
            setAlarm(calSet);
        }
    };

    private void setAlarm(Calendar targetCal) {
        tvAlarmPrompt.setText("Time is ");
        tvAlarmPrompt.append(String.valueOf(targetCal.getTime()));
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, RQS_TIME, intent, 0);
        AlarmManager alarmManager = (AlarmManager)
                getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                pendingIntent);
    }

    private void cancelAlarm() {
        tvAlarmPrompt.setText("Cancel!");
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, RQS_TIME, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private void start() {
        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();
        calSet.set(Calendar.SECOND, calSet.get(Calendar.SECOND)+2);
        setAlarm(calSet);

    }



    public void getArray(String Text)
    {
        while (Text != null) {
            if (check(Text)) {
                values = buildArray(Text);
                des1.setText(""+values[0]);
                des2.setText(""+values[1]);
                sk1.setProgress(values[2]);
                sk2.setProgress(values[3]);


            }
        }
    }


    public void rec(View view) {
        String date = new SimpleDateFormat("MMddyyyy", Locale.getDefault()).format(new Date());
        dbRecRef.setValue(date);
        DatabaseReference dbDatarecRef=dbRecRef.child("Data");
        Boolean datarec=true;
        if(datarec) dbDatarecRef.setValue("Text");

     /*   rngl=0;
        rngr=200;
        head=0;
        elev=-90;

        i = 0;

        final DatabaseReference count = ref.child("count");
        count.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null)
                    count.setValue(0);
                else
                    count.setValue(dataSnapshot.getValue(Integer.class) + 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        count.setValue(24);

        // Create the Handler
        final Handler handler = new Handler();

// Define the code block to be executed
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Insert custom code here
                update();

                handler.postDelayed(this, 5000);
            }
        };

// Start the Runnable immediately
        handler.post(runnable);




        //ExecutorService service = Executors.newSingleThreadExecutor();

        ref.child("record");

        try {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    // Database task
                    for (int i = 0; i < 50; i++)
                    {
                        Data d = new Data(values[0], values[1], values[2], values[3]);
                        ref.child(""+i).setValue(d);
                    }
                }
            };

            Future<?> f = service.submit(r);

            f.get(5, TimeUnit.SECONDS);     // attempt the task for two minutes
        }
        catch (final InterruptedException e) {
            // The thread was interrupted during sleep, wait or join
        }
        catch (final TimeoutException e) {
            // Took too long!
        }
        catch (final ExecutionException e) {
            // An exception from within the Runnable task
        }
        finally {
            service.shutdown();
        } */
    }

    public void toblue(View view) {
        Intent intent=new Intent(this, BluetoothAlpha.class);
        startActivity(intent);
    }

    BroadcastReceiver mReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String Text = intent.getStringExtra("theMessage");
            //  message.append(Text +"/");
            //textView.setText(message);
            Toast.makeText(MainActivity.this, Text, Toast.LENGTH_LONG).show();
        }
    };
}


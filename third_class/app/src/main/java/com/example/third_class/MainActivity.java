package com.example.third_class;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final static String TAGS = "tips";
    private Clock mClock;
    private EditText mText;
    private Button mGetButton;
    private Button mSetButton;
    private Toast toast;
    int[] time;
    String text;
    private Handler mHandler = new Handler ();
    private Runnable runnable;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        mClock = (Clock) findViewById(R.id.clock);
        mClock.start ();
        mText = (EditText) findViewById(R.id.text);

        runnable = new Runnable() {
            @Override
            public void run() {
                mClock.invalidate ();
                update ();
            }
        };

        update();

        mGetButton = (Button) findViewById(R.id.get);
        mGetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = mClock.getTime();
                mText.setText(time[0] + ":" + time[1] + ":" + time[2]);
            }
        });

        mSetButton = (Button) findViewById(R.id.reset);
        mSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = mText.getText().toString();
                String[] tmp = text.split(":");
                if (tmp.length != 3) {
                    toast = Toast.makeText (getApplicationContext(), "invalid format", Toast.LENGTH_SHORT);
                    toast.show ();
                }
                try {
                    time = new int[3];
                    for (int i=0; i<3; i++) {
                        time[i] = Integer.parseInt(tmp[i]);
                    }
                    mClock.setTime(time);
                }catch (NumberFormatException w) {
                    toast = Toast.makeText (getApplicationContext(), "invalid format", Toast.LENGTH_SHORT);
                    toast.show ();
                }
            }
        });
    }

    public void update () {
        mHandler.postDelayed (runnable, 1000);
    }
}
package com.example.first_class;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ExampleActivity extends Activity {
    private TextView resultView;
    private Button mQuitButton;
    private static final String TAG = "MainActivity";

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.search);
        String extra = getIntent ().getStringExtra ("extra");

        resultView = (TextView) findViewById (R.id.result_text);
        resultView.setText ("结果页面 num: " + extra.toString ());

        mQuitButton = (Button) findViewById (R.id.result_quit);
        mQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d (TAG, "return to search");
                finish ();
            }
        });
    }

    @Override
    protected void onStart () {
        super.onStart();
    }

    @Override
    protected void onResume () {
        super.onResume();
    }

    @Override
    protected void onStop () {
        super.onStop();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
    }
}

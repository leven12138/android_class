package com.example.first_class;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.SearchEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText mEditText;
    private Button mSearchButton, mResetButton;
    private RecyclerView mRecyclerView;
    private SearchAdapter mSearchAdapter = new SearchAdapter ();
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById (R.id.text);
        mRecyclerView = (RecyclerView) findViewById (R.id.mRecycle);
        mRecyclerView.setLayoutManager (new LinearLayoutManager (this));
        mRecyclerView.setAdapter (mSearchAdapter);

        List<String> items = new ArrayList<> ();
        List<String> limitItems = new ArrayList<> ();
        for (int i = 1; i <= 100; i++) {
            items.add ("榜单第" + String.valueOf (i));
            limitItems.add ("榜单第" + String.valueOf (i));
        }
        mSearchAdapter.notifyItems (limitItems);

        mSearchButton = (Button) findViewById (R.id.searchB);
        mSearchButton.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = mEditText.getText ().toString ();
                int i = 0;

                Log.d (TAG, str);
                while (true) {
                    if (i == limitItems.size ()) {break;}
                    if (limitItems.get (i).contains (str)) {
                        i++;
                    }else {
                        limitItems.remove (i);
                    }
                }
                Log.d (TAG, "search " + str + " completed");
                mSearchAdapter.notifyItems (limitItems);
            }
        });

        mResetButton = (Button) findViewById (R.id.resetB);
        mResetButton.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limitItems.clear ();
                limitItems.addAll (items);

                mSearchAdapter.notifyItems (limitItems);
                mEditText.setText ("");
            }
        });
    }
}


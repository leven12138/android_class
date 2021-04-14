package com.example.sixth_class;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button mPictureAct;
    private Button mPicOpAct;
    private Button mMovieAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPictureAct = (Button) findViewById (R.id.pic_button);
        mPicOpAct = (Button) findViewById (R.id.picOp_button);
        mMovieAct = (Button) findViewById (R.id.movie_button);

        mPictureAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity (new Intent (MainActivity.this, imageShowActivity.class));
            }
        });

        mPicOpAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity (new Intent (MainActivity.this, imageOperateActivity.class));
            }
        });

        mMovieAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity (new Intent (MainActivity.this, videoActivity.class));
            }
        });
    }
}
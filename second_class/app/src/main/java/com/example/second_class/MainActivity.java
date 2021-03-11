package com.example.second_class;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.second_class.page.pageOne;
import com.example.second_class.page.pageThree;
import com.example.second_class.page.pageTwo;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private Button mWaveButton;
    private ViewPager mPager;
    private TabLayout mTab;
    private static final int PAGE_COUNT = 3;
    private static final String TAG = "inform";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPager = (ViewPager) findViewById (R.id.page_view);
        mTab = (TabLayout) findViewById (R.id.tab);
        mPager.setAdapter(new FragmentPagerAdapter (getSupportFragmentManager ()) {
            @NonNull
            @Override
            public Fragment getItem (int position) {
                switch (position) {
                    case 0:
                        Log.d (TAG, "switch to page 1");
                        return new pageOne ();
                    case 1:
                        Log.d (TAG, "switch to page 2");
                        return new pageTwo ();
                    default:
                        Log.d (TAG, "switch to page 3");
                        return new pageThree ();
                }
            }

            @Override
            public int getCount() {
                return PAGE_COUNT;
            }

            @Override
            public CharSequence getPageTitle (int position) {
                return "EX " + (position + 1);
            }
        });
        mTab.setupWithViewPager (mPager);
    }
}
package com.example.sixth_class;

import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

public class imageShowActivity extends AppCompatActivity {
    ViewPager pager = null;
    LayoutInflater layoutInflater = null;
    List<View> pages = new ArrayList<View>();
    static final String TAG = "inform";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_image);
        layoutInflater = getLayoutInflater ();
        pager = (ViewPager) findViewById (R.id.page_view);

        addImage (R.drawable.scenery);
        addImage (R.drawable.leimu);
        viewAdapter adapter = new viewAdapter ();
        adapter.setData (pages);
        pager.setAdapter (adapter);
    }

    private void addImage (int resId) {
        ImageView imageView = (ImageView) layoutInflater.inflate (R.layout.image_item, null);
        Drawable drawable = ContextCompat.getDrawable (this, resId);
        Glide.with (this)
                .load (resId)
                .error (R.drawable.error)
                .into (imageView);

        pages.add (imageView);
    }
}

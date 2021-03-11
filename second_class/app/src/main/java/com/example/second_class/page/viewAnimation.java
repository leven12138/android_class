package com.example.second_class.page;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.second_class.R;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

public class viewAnimation extends AppCompatActivity {
    private View mColorBlock;
    private View mFirstColor;
    private View mSecondColor;
    private EditText mVelocityText;
    private Button mVelocityButton;
    private Button mQuitButton;
    private AnimatorSet animatorSet;
    private static final String TAG = "inform";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.view_animation);

        mColorBlock = (View) findViewById (R.id.block);
        mFirstColor = (View) findViewById (R.id.first_color);
        mSecondColor = (View) findViewById (R.id.second_color);
        mVelocityText = (EditText) findViewById (R.id.anim_velocity);
        mVelocityButton = (Button) findViewById (R.id.vel_set);
        mQuitButton = (Button) findViewById (R.id.quit_button);

        mFirstColor.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                ColorPicker picker = new ColorPicker (viewAnimation.this);
                picker.setColor (getBackgroundColor (mFirstColor));
                picker.enableAutoClose ();
                picker.setCallback (new ColorPickerCallback () {
                    @Override
                    public void onColorChosen(int color) {
                        onFirstColorChanged(color);
                    }
                });
                picker.show();
            }
        });

        mSecondColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPicker picker = new ColorPicker (viewAnimation.this);
                picker.setColor (getBackgroundColor (mSecondColor));
                picker.enableAutoClose();
                picker.setCallback (new ColorPickerCallback() {
                    @Override
                    public void onColorChosen(int color) {
                        onSecondColorChanged(color);
                    }
                });
                picker.show();
            }
        });
        resetTargetAnimation();

        mVelocityButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                int input;
                try {
                    input = Integer.parseInt (mVelocityText.getText ().toString ());
                    if (input < 100 | input > 10000) {
                        Log.d (TAG, "num out of range");
                        mVelocityText.setText ("10000");
                    }
                } catch (NumberFormatException e) {
                    Log.d(TAG, "invalid number format");
                    mVelocityText.setText("10000");
                }
                resetTargetAnimation();
            }
        });

        mQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d (TAG, "return to main page");
                finish ();
            }
        });
    }

    private void onFirstColorChanged (int color) {
        mFirstColor.setBackgroundColor (color);
        resetTargetAnimation ();
    }

    private void onSecondColorChanged (int color) {
        mSecondColor.setBackgroundColor (color);
        resetTargetAnimation ();
    }

    private int getBackgroundColor (View view) {
        Drawable bg = view.getBackground ();
        if (bg instanceof ColorDrawable) {
            return ((ColorDrawable) bg).getColor ();
        }
        return Color.WHITE;
    }

    private void resetTargetAnimation () {
        if (animatorSet != null) {
            animatorSet.cancel ();
        }

        ObjectAnimator animator_color = ObjectAnimator.ofArgb (mColorBlock, "backgroundColor",
                getBackgroundColor (mFirstColor),
                getBackgroundColor (mSecondColor));
        animator_color.setDuration (Integer.parseInt (mVelocityText.getText ().toString ()));
        animator_color.setRepeatCount (ObjectAnimator.INFINITE);
        animator_color.setRepeatMode (ObjectAnimator.REVERSE);

        ObjectAnimator animator_sizeX = ObjectAnimator.ofFloat (mColorBlock, "scaleX", 1, 2);
        animator_sizeX.setDuration (Integer.parseInt (mVelocityText.getText ().toString ()));
        animator_sizeX.setRepeatCount (ObjectAnimator.INFINITE);
        animator_sizeX.setRepeatMode (ObjectAnimator.REVERSE);

        ObjectAnimator animator_sizeY = ObjectAnimator.ofFloat (mColorBlock, "scaleY", 1, 2);
        animator_sizeY.setDuration (Integer.parseInt (mVelocityText.getText ().toString ()));
        animator_sizeY.setRepeatCount (ObjectAnimator.INFINITE);
        animator_sizeY.setRepeatMode (ObjectAnimator.REVERSE);

        ObjectAnimator animator_transparency = ObjectAnimator.ofFloat (mColorBlock, "alpha", 1f, 0.5f);
        animator_transparency.setDuration (Integer.parseInt (mVelocityText.getText ().toString ()));
        animator_transparency.setRepeatCount (ObjectAnimator.INFINITE);
        animator_transparency.setRepeatMode (ObjectAnimator.REVERSE);

        animatorSet = new AnimatorSet ();
        animatorSet.playTogether (animator_color, animator_sizeX, animator_sizeY, animator_transparency);
        animatorSet.start ();
    }
}

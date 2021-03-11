package com.example.second_class.page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.second_class.R;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

public class pageTwo extends Fragment {
    private Button mButton;
    private static final String TAG = "inform";

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater,
                          @Nullable ViewGroup container,
                          @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.page_two, container, false);
        mButton = (Button) view.findViewById (R.id.goTo);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d (TAG, "go to animation");
                startActivity (new Intent (v.getContext(), viewAnimation.class));
            }
        });

        return view;
    }

}

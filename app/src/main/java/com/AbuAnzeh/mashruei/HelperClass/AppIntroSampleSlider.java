package com.AbuAnzeh.mashruei.HelperClass;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.AbuAnzeh.mashruei.R;


public class AppIntroSampleSlider extends Fragment {
    //Layout id
    private static final String ARG_LAYOUT_RES_ID = "layoutResId";

    public static AppIntroSampleSlider newInstance(int layoutResId) {
        AppIntroSampleSlider sampleSlide = new AppIntroSampleSlider();

        Bundle bundleArgs = new Bundle();
        bundleArgs.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        sampleSlide.setArguments(bundleArgs);

        return sampleSlide;
    }

    private int layoutResId;

    public AppIntroSampleSlider() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID))
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutResId, container, false);

        TextView textView=view.findViewById(R.id.description);
        textView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));


        SpannableString ss=new SpannableString(textView.getText());
        ForegroundColorSpan spanColor=new ForegroundColorSpan(getResources().getColor(R.color.colorAccent));

        ss.setSpan(spanColor,7,13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        textView.setText(ss);

        return view;
    }

}
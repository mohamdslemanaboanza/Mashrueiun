package com.AbuAnzeh.mashruei.HelperClass;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.AbuAnzeh.mashruei.Activities.MyStoreActivity;
import com.AbuAnzeh.mashruei.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ModelBottomSheet extends BottomSheetDialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buttom_sheet, container, false);


        TextView createBranch ;
        createBranch = view.findViewById(R.id.createBranch);
        createBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MyStoreActivity.class));
                ((Activity) getActivity()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });



        return view;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

    }
}
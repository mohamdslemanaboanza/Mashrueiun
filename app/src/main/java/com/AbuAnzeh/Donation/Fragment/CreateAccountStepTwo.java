package com.AbuAnzeh.Donation.Fragment;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.AbuAnzeh.Donation.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;


public class CreateAccountStepTwo extends Fragment {

    private TextView t1,t2,t3,header;
    private EditText name_donation,user_name,pass;
    private Button next;
    private TextInputLayout textInputLayout1,textInputLayout2,textInputLayout3;

    public CreateAccountStepTwo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.step_two_account, container, false);

        t1=view.findViewById(R.id.t1);
        t2=view.findViewById(R.id.t2);
        header=view.findViewById(R.id.header);
        t3=view.findViewById(R.id.t4);
        name_donation=view.findViewById(R.id.name_donation);
        user_name=view.findViewById(R.id.user_name);
        pass=view.findViewById(R.id.pass);
        next=view.findViewById(R.id.next);
        textInputLayout1=view.findViewById(R.id.textInputLayout1);
        textInputLayout2=view.findViewById(R.id.textInputLayout2);
        textInputLayout3=view.findViewById(R.id.textInputLayout3);


        header.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        t1.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        t2.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        t3.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        name_donation.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        user_name.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        pass.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        next.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        textInputLayout1.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        textInputLayout2.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        textInputLayout3.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));

        final SharedPreferences.Editor editor = getActivity().getSharedPreferences("detalisUsre", MODE_PRIVATE).edit();
        SharedPreferences preferences = getActivity().getSharedPreferences("detalisUsre", MODE_PRIVATE);


        name_donation.setText(preferences.getString("Name", ""));
        user_name.setText(preferences.getString("NameUser", ""));
        pass.setText(preferences.getString("Pass", ""));


        textInputLayout3.setHelperText(pass.getText().length()+"/6");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
             if(TextUtils.isEmpty(name_donation.getText()))
             {
                 textInputLayout1.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.r)));
                 textInputLayout1.setHelperText("مطلوب");
                 return;

             }else if(TextUtils.isEmpty(user_name.getText()))
             {
                 textInputLayout2.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.r)));
                 textInputLayout2.setHelperText("مطلوب");
                 return;
             }else if(!isValidEmailId(user_name.getText().toString().trim()))
             {
                 textInputLayout2.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.r)));
                 textInputLayout2.setHelperText("البريد غير صالح");
                 return;
             }else if(TextUtils.isEmpty(pass.getText()))
             {
                 textInputLayout3.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.r)));
                 textInputLayout3.setHelperText("مطلوب");
                 return;
             }else if(pass.getText().toString().length()<6)
             {
                 textInputLayout3.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.r)));
                 textInputLayout3.setHelperText("يجب أن تتكون من 6 من أرقام");
                 return;

             }

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contener_account, new CreateAccountStepThree())
                        .commit();
            }
        });

        name_donation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                editor.putString("Name",charSequence.toString());
                editor.apply();
                textInputLayout1.setHelperText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        user_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editor.putString("NameUser",charSequence.toString());
                editor.apply();
                textInputLayout2.setHelperText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                textInputLayout3.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.b)));
                textInputLayout3.setHelperText(charSequence.toString().length()+"/6");
                editor.putString("Pass",charSequence.toString());
                editor.apply();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager=getActivity().getSupportFragmentManager();
                CreateAccountStepOne registerFragment=new CreateAccountStepOne();
                manager.beginTransaction()
                        .replace(R.id.contener_account,registerFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

            }
        });




        return view;
    }
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
}
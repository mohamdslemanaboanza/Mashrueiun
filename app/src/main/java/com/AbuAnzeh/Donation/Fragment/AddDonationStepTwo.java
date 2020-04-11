package com.AbuAnzeh.Donation.Fragment;


import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.AbuAnzeh.Donation.Adpter.custam_adpter_text;
import com.AbuAnzeh.Donation.HelpClasses.CheckConnecationInternet;
import com.AbuAnzeh.Donation.Models.custm_item_text;
import com.AbuAnzeh.Donation.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddDonationStepTwo extends Fragment {


    //Declare
    private TextView hint,TitleName,TitleDetails,TitlePhoneNumber,TitleCity,header;
    private Button next;
    private EditText name_donation,Details,phone_number;
    private Spinner city;
    private TextInputLayout textInputLayoutDetails,textInputLayoutName,textInputLayoutPhoneNumber;
    private ArrayList<custm_item_text> Governesses;
    private String CitySpinner,CityOfUser;
    private DatabaseReference Donors;
    private FirebaseAuth firebaseAuth;
    private CheckConnecationInternet internet;

    public AddDonationStepTwo() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.step_two_donation, container, false);

        //init
        hint=view.findViewById(R.id.hint);
        next=view.findViewById(R.id.next);
        TitleName=view.findViewById(R.id.TitleName);
        TitleDetails=view.findViewById(R.id.TitleDetails);
        header=view.findViewById(R.id.header);
        TitlePhoneNumber=view.findViewById(R.id.TitlePhoneNumber);
        TitleCity=view.findViewById(R.id.TitleCity);
        name_donation=view.findViewById(R.id.name_donation);
        Details=view.findViewById(R.id.detalis);
        city=view.findViewById(R.id.city);
        textInputLayoutName=view.findViewById(R.id.textInputLayoutName);
        textInputLayoutDetails=view.findViewById(R.id.textInputLayoutDetails);
        textInputLayoutPhoneNumber=view.findViewById(R.id.textInputLayoutPhoneNumber);
        phone_number=view.findViewById(R.id.phone_number);
        firebaseAuth= FirebaseAuth.getInstance();
        internet=new CheckConnecationInternet(getActivity());
        Donors = FirebaseDatabase.getInstance().getReference("Donors");


        Donors.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String phone_numbre=dataSnapshot.child(firebaseAuth.getUid()).child("phoneNumber").getValue(String.class);
                CityOfUser = dataSnapshot.child(firebaseAuth.getUid()).child("city").getValue(String.class);

                phone_number.setText(phone_numbre);


                if (CityOfUser.equals("عمان")) {
                    city.setSelection(1);
                } else if (CityOfUser.equals("الزرقاء")) {
                    city.setSelection(2);
                } else if (CityOfUser.equals("الطفيلة")) {
                    city.setSelection(3);
                } else if (CityOfUser.equals("الكرك")) {
                    city.setSelection(4);
                } else if (CityOfUser.equals("اربد")) {
                    city.setSelection(5);
                } else if (CityOfUser.equals("معان")) {
                    city.setSelection(6);
                } else if (CityOfUser.equals("العقبة")) {
                    city.setSelection(7);
                } else if (CityOfUser.equals("السلط")) {
                    city.setSelection(8);
                } else if (CityOfUser.equals("البحر الميت")) {
                    city.setSelection(9);
                } else if (CityOfUser.equals("مادبا")) {
                    city.setSelection(11);
                } else if (CityOfUser.equals("جرش")) {
                    city.setSelection(12);
                } else if (CityOfUser.equals("عجلون")) {
                    city.setSelection(13);
                } else if (CityOfUser.equals("المفرق")) {
                    city.setSelection(14);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        //بغير نوع الخط
        hint.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        next.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        TitleName.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        textInputLayoutDetails.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        textInputLayoutName.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        TitleDetails.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        header.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        TitlePhoneNumber.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        TitleCity.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        phone_number.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        Details.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        name_donation.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));
        textInputLayoutPhoneNumber.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Omar-Regular-1.ttf"));




        //بدي اجيب الي كتبو عشان اعرضو في حال انو رجع هون
        final SharedPreferences.Editor editor = getActivity().getSharedPreferences("detalis", MODE_PRIVATE).edit();



        //بعبي المدن بالقائمة
        Governesses = GetGovernorate();
        custam_adpter_text ustamAdabterCustam_adpter_text=new custam_adpter_text(getActivity(), Governesses);
        if(city != null)
        {
            city.setAdapter(ustamAdabterCustam_adpter_text);
        }


        //بدي اعرف القائمة
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                custm_item_text itemText =(custm_item_text)parent.getSelectedItem();
                 CitySpinner =itemText.getSpinnertext();

                 //بخزن القائمة
                editor.putInt("CitySpinner",position);
                editor.putString("City",CitySpinner);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //بعرض البيانات الي كتبهم اذا رجع للصفحة
        SharedPreferences preferences = getActivity().getSharedPreferences("detalis", MODE_PRIVATE);
        Details.setText(preferences.getString("Details", ""));
        phone_number.setText(preferences.getString("Phone_number",""));
        name_donation.setText(preferences.getString("name_donation",""));
        int pos = preferences.getInt("CitySpinner",0);
        city.setSelection(pos);




        //بعرض الو عدد الاحرف الي لازم يكتبها
        textInputLayoutDetails.setHelperText(Details.getText().length()+"/155");

        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {


                if(TextUtils.isEmpty(name_donation.getText()))
                {
                    textInputLayoutName.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.r)));
                    textInputLayoutName.setHelperText("مطلوب");

                    return;
                }else if(TextUtils.isEmpty(Details.getText()))
                {
                    textInputLayoutDetails.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.r)));
                    textInputLayoutDetails.setHelperText("مطلوب");
                    return;
                }
                else if(TextUtils.isEmpty(phone_number.getText()))
                {
                    textInputLayoutPhoneNumber.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.r)));
                    textInputLayoutPhoneNumber.setHelperText("مطلوب");
                    return;
                }else if(Details.getText().length()<155)
                {
                    textInputLayoutDetails.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.r)));

                    textInputLayoutDetails.setHelperText("يجب ان تتكون التفاصيل من 155 حرف أو اكثر");
                    return;
                }else if(phone_number.getText().length()<10 || phone_number.getText().length()>10 )
                {
                    textInputLayoutPhoneNumber.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.r)));
                    textInputLayoutPhoneNumber.setHelperText("رقم الهاتف غير صالح");
                    return;
                }else if(CitySpinner.equals("اختيار المحافظة"))
                {
                    Snackbar.make(view,"يجب أختيار المحافظة", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contener_main, new AddDonationStepThree()).commit();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                AddDonationStepThree registerFragment = new AddDonationStepThree();
                manager.beginTransaction()
                        .replace(R.id.contener_main, registerFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

            }
        });


        //بخزن الي كتبو عشان اعرض الي كتبو اذا بدو يرجع
        name_donation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textInputLayoutName.setHelperText("");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editor.putString("name_donation",charSequence.toString());
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //بخزن الي كتبو عشان اعرض الي كتبو اذا بدو يرجع
        Details.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textInputLayoutPhoneNumber.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.b)));
                textInputLayoutDetails.setHelperText(charSequence.toString().length()+"/155");
                editor.putString("Details",charSequence.toString());
                editor.apply();

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        //بخزن الي كتبو عشان اعرض الي كتبو اذا بدو يرجع
        phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editor.putString("Phone_number",charSequence.toString());
                editor.apply();
                textInputLayoutPhoneNumber.setHelperText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //الرجوه الى الخلف
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager=getActivity().getSupportFragmentManager();
                AddDonationStepOne registerFragment=new AddDonationStepOne();
                manager.beginTransaction()
                        .replace(R.id.contener_main,registerFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            }
        });



        return view;
    }


    //بضيف المدن
    private ArrayList<custm_item_text> GetGovernorate()
    {
        Governesses =new ArrayList<>();
        Governesses.add(new custm_item_text("اختيار المحافظة"));
        Governesses.add(new custm_item_text("عمان"));
        Governesses.add(new custm_item_text("الزرقاء"));
        Governesses.add(new custm_item_text("الطفيلة"));
        Governesses.add(new custm_item_text("الكرك"));
        Governesses.add(new custm_item_text("اربد"));
        Governesses.add(new custm_item_text("معان"));
        Governesses.add(new custm_item_text("العقبة"));
        Governesses.add(new custm_item_text("السلط"));
        Governesses.add(new custm_item_text("البحر الميت"));
        Governesses.add(new custm_item_text("مادبا"));
        Governesses.add(new custm_item_text("جرش"));
        Governesses.add(new custm_item_text("عجلون"));
        Governesses.add(new custm_item_text("المفرق"));

        return Governesses;


    }


}

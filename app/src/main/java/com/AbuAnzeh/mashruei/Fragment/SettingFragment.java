package com.AbuAnzeh.mashruei.Fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.AbuAnzeh.mashruei.Activities.ContactUsActivity;
import com.AbuAnzeh.mashruei.Activities.DeskAppActivity;
import com.AbuAnzeh.mashruei.Activities.LogInActivity;
import com.AbuAnzeh.mashruei.Activities.MainActivity;
import com.AbuAnzeh.mashruei.Activities.MyAccountActivity;
import com.AbuAnzeh.mashruei.Activities.SettingActivity;
import com.AbuAnzeh.mashruei.Activities.SignUpActivity;
import com.AbuAnzeh.mashruei.Activities.WhoAreWeActivity;
import com.AbuAnzeh.mashruei.R;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {


    private LinearLayout deckApp, myAccount, contact_us, who_are,share,settingApp,create_account,logout,howUesApp;
    private FirebaseAuth firebaseAuth;
    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        firebaseAuth=FirebaseAuth.getInstance();
        deckApp = view.findViewById(R.id.deckApp);
        contact_us = view.findViewById(R.id.contact_us);
        myAccount = view.findViewById(R.id.myAccount);
        howUesApp = view.findViewById(R.id.howUesApp);
        create_account = view.findViewById(R.id.create_account);
        share = view.findViewById(R.id.share);
        logout = view.findViewById(R.id.logout);
        who_are = view.findViewById(R.id.who_are);
        settingApp = view.findViewById(R.id.settingApp);



        SharedPreferences preferencesInfo = getActivity().getSharedPreferences("InfoUser", Context.MODE_PRIVATE);
        final String userId = preferencesInfo.getString("UserId", "");

        if (!userId.isEmpty()){

            create_account.setVisibility(View.GONE);

        }else {
            create_account.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
        }

        deckApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DeskAppActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SignUpActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        myAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userId.isEmpty())
                {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dilog_login);
                    dialog.setCancelable(true);

                    Window window = dialog.getWindow();
                    WindowManager.LayoutParams wlp = window.getAttributes();

                    wlp.gravity = Gravity.CENTER;
                    wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                    window.setAttributes(wlp);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    TextView skip = dialog.findViewById(R.id.notNow);
                    Button go = dialog.findViewById(R.id.go);



                    skip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                          dialog.dismiss();
                        }
                    });

                    go.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();
                            startActivity(new Intent(getActivity(), SignUpActivity.class));
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                        }
                    });


                    dialog.show();
                }else {

                    startActivity(new Intent(getActivity(), MyAccountActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });

        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ContactUsActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        who_are.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), WhoAreWeActivity.class).putExtra("flag",0));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        howUesApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), WhoAreWeActivity.class).putExtra("flag",1));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });


        settingApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferencesInfoLogin = getActivity().getSharedPreferences("LoginOrSign",Context.MODE_PRIVATE);
                SharedPreferences.Editor editorLogin = preferencesInfoLogin.edit();
                editorLogin.putBoolean("success",false);
                editorLogin.apply();


                SharedPreferences preferencesInfo = getActivity().getSharedPreferences("InfoUser",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencesInfo.edit();
                editor.putString("UserId","");
                editor.apply();

                startActivity(new Intent(getActivity(), LogInActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "مشروعي");
                String v = "https://play.google.com/store/apps/details?id=com.AbuAnzeh.mashruei\n\n";
                intent.putExtra(Intent.EXTRA_TEXT, v);
                startActivity(Intent.createChooser(intent, "مشاركة عبر"));
            }
        });

        return view;
    }

}
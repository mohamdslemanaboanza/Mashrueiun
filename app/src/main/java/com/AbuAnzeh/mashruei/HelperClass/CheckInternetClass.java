package com.AbuAnzeh.mashruei.HelperClass;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.AbuAnzeh.mashruei.R;


import static android.content.Context.CONNECTIVITY_SERVICE;

public class CheckInternetClass
{
    Context context;
    private String Message;
    public CheckInternetClass(Context context, String Message) {
        this.context = context;
        this.Message=Message;

    }

    public CheckInternetClass(Context context) {
        this.context = context;

    }




    public boolean isConnected() {
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(CONNECTIVITY_SERVICE);

        if(connectivityManager != null)
        {
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null)
            {
                if(networkInfo.getState()== NetworkInfo.State.CONNECTED)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void ShowHint()
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View view = LayoutInflater.from(context).inflate(R.layout.no_interet_connecation,null,false);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        Button Reload = view.findViewById(R.id.Reload);
        Reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                ((Activity)context).finish();

            }
        });

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        dialog.show();
    }


    public boolean haveNetwork(){
        ConnectivityManager conxMgr = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo mobileNwInfo = conxMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNwInfo   = conxMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return ((mobileNwInfo== null? false : mobileNwInfo.isConnected() )|| (wifiNwInfo == null? false : wifiNwInfo.isConnected()));
    }

}

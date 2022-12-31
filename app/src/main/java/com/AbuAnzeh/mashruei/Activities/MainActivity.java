package com.AbuAnzeh.mashruei.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.AbuAnzeh.mashruei.Fragment.CartFragment;
import com.AbuAnzeh.mashruei.Fragment.HomeFragment;
import com.AbuAnzeh.mashruei.Fragment.OrderFragment;
import com.AbuAnzeh.mashruei.Fragment.SettingFragment;
import com.AbuAnzeh.mashruei.HelperClass.CheckInternetClass;
import com.AbuAnzeh.mashruei.HelperClass.CustomTypefaceSpan;
import com.AbuAnzeh.mashruei.HelperClass.ModelBottomSheet;
import com.AbuAnzeh.mashruei.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity   {


    //Declare
    private BottomNavigationView customBottomNavigationView1;
    private Typeface font;
    private FrameLayout contener_main;
    private ImageView fab;
    private String nameStore;
    private DatabaseReference databaseStore;
    private int numberOfOrder,orders;
    DatabaseReference databaseMyOrde,databaseOrders;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("AppNotifications").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("subscribeToTopic","Success");
            }
        });
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        SharedPreferences preferencesInfo = getSharedPreferences("InfoUser", Context.MODE_PRIVATE);
        final String userId = preferencesInfo.getString("UserId", "");

        databaseMyOrde = FirebaseDatabase.getInstance().getReference("UserOrder").child(userId);
        databaseOrders= FirebaseDatabase.getInstance().getReference("Order").child(userId);





        CheckInternetClass internetClass=new CheckInternetClass(this,"عفوا ، لا يوجد إتصال بالإنترنت ، تحقق من إتصالك بالانترنت");

        if (!internetClass.haveNetwork())
        {
            internetClass.ShowHint();

            return;
        }






        fab = findViewById(R.id.fab);
        contener_main = findViewById(R.id.contener_main);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences InfoStore=getSharedPreferences("InfoStore",MODE_PRIVATE);
                boolean b  = InfoStore.getBoolean("Created",false);


                if (userId.isEmpty()) {

                    final Dialog dialog = new Dialog(MainActivity.this);
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
                            startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                        }
                    });

                    dialog.show();
                } else {


                    if (b){

                        startActivity(new Intent(MainActivity.this, MyStoreActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    }else {

                        customBottomNavigationView1.setSelectedItemId(0000);
                        ModelBottomSheet bottomSheet = new ModelBottomSheet();
                        bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");

                    }


                }
            }
        });




        customBottomNavigationView1 = findViewById(R.id.customBottomBar);
        customBottomNavigationView1.inflateMenu(R.menu.bottom_menu);
        customBottomNavigationView1.setClickable(false);
        customBottomNavigationView1.setSelectedItemId(R.id.home_page);
        customBottomNavigationView1.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home_page){
                    getSupportFragmentManager().beginTransaction().replace(R.id.contener_main, new HomeFragment())
                            .commit();
                }else if (id == R.id.setting){
                    getSupportFragmentManager().beginTransaction().replace(R.id.contener_main, new SettingFragment())
                            .commit();
                }else if (id == R.id.cart){
                    getSupportFragmentManager().beginTransaction().replace(R.id.contener_main, new CartFragment())
                            .commit();
                }else if (id == R.id.order){
                    getSupportFragmentManager().beginTransaction().replace(R.id.contener_main, new OrderFragment())
                            .commit();
                }
                return true;
            }
        });




        databaseOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                orders=  (int) dataSnapshot.getChildrenCount();
                showBadge(MainActivity.this, customBottomNavigationView1, R.id.order, orders+"");

                if (orders ==0){
                    removeBadge(customBottomNavigationView1,R.id.order);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (!userId.equals("")) {
            databaseMyOrde.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    numberOfOrder = (int) dataSnapshot.getChildrenCount();
                    showBadge(MainActivity.this, customBottomNavigationView1, R.id.cart, numberOfOrder + "");

                    if (numberOfOrder == 0) {
                        removeBadge(customBottomNavigationView1, R.id.cart);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        //هون بعرض الفراغمنت الرئيسية
        getSupportFragmentManager().beginTransaction().replace(R.id.contener_main, new HomeFragment())
                .commit();

        applyFont();









    }

    //تغيير نوع الخط
    private void applyFontToMenuItem(MenuItem mi) {

        font = Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);


    }




    public void applyFont() {
        //تغير خط القائمة الجانبية
        Menu m1 = customBottomNavigationView1.getMenu();
        for (int i = 0; i < m1.size(); i++) {
            MenuItem mi = m1.getItem(i);

            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);

        }

    }

    public static void showBadge(Context context, BottomNavigationView
            bottomNavigationView, @IdRes int itemId, String value) {

        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        View badge = LayoutInflater.from(context).inflate(R.layout.component_tabbar_badge, bottomNavigationView, false);

        TextView text = badge.findViewById(R.id.notificationsBadgeTextView);
        text.setText(value);
        if (value.equals("0")) {
            itemView.removeView(badge);
        } else {
            itemView.addView(badge);
        }
    }

    public static void removeBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        if (itemView.getChildCount() == 3) {
            itemView.removeViewAt(2);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
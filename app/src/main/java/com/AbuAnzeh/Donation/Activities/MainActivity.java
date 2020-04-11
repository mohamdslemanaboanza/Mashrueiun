package com.AbuAnzeh.Donation.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.AbuAnzeh.Donation.Fragment.AddDonationStepOne;
import com.AbuAnzeh.Donation.Fragment.HomeFragment;
import com.AbuAnzeh.Donation.Fragment.NonFoodFragment;
import com.AbuAnzeh.Donation.Fragment.ProfileFragment;
import com.AbuAnzeh.Donation.R;
import com.AbuAnzeh.Donation.HelpClasses.CustomTypefaceSpan;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {


    //Declare
    private MeowBottomNavigation bottomNavigation;
    private NavigationView nav_view;
    private ImageView navigation_open;
    private boolean flag = true;
    private Typeface font;
    private DrawerLayout drawer_layout;
    private FrameLayout contener_main;
    private FirebaseAuth firebaseAuth;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);



        //init
        drawer_layout = findViewById(R.id.drawer_Layout);
        nav_view = findViewById(R.id.nav_view);
        navigation_open = findViewById(R.id.navigation_open);
        contener_main = findViewById(R.id.contener_main);
        bottomNavigation = findViewById(R.id.MeowBottomNavigation);
        firebaseAuth = FirebaseAuth.getInstance();


        //اذا كبس على صورة عشان يقتح القائمة الجانبية
        navigation_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    contener_main.setVisibility(View.INVISIBLE);
                    drawer_layout.openDrawer(GravityCompat.START);
                    flag = false;
                } else {
                    contener_main.setVisibility(View.VISIBLE);
                    drawer_layout.closeDrawers();
                    flag = true;
                }

            }


        });








        //عشان صور العناصر ما يتغيروا
        nav_view.setItemIconTintList(null);
        //هون الاكشن الي بدو يصير لما اكبس ايتم محدد
        nav_view.setNavigationItemSelectedListener(this);

        applyFont();

        //هون بعرض الفراغمنت الرئيسية
        getSupportFragmentManager().beginTransaction().replace(R.id.contener_main, new HomeFragment())
                .commit();


        //ببعت العناصر
        bottomNavigation.add(new MeowBottomNavigation.Model(0, R.drawable.icons_person));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.food));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.fasting));
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_add));

        Menu menu=nav_view.getMenu();
        MenuItem item=menu.findItem(R.id.logout);

        if(firebaseAuth.getUid() == null)
        {
            item.setTitle("انشاء حساب ");
            applyFont();
        }else
            {
                item.setTitle("تسجيل خروج ");
                applyFont();
            }


        //باي دوفيلت ببين العنصر رقم 2
        bottomNavigation.show(2, true);


        //هون الاكشن الي بدو يصير لما اكبس ايتم محدد
        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {

                contener_main.setVisibility(View.VISIBLE);
                int id = model.getId();
                //بسكر القائمة الجانبية اذا كانت مفتوحة
                drawer_layout.closeDrawers();
                //ببين الصورة
                navigation_open.setVisibility(View.VISIBLE);

                flag = true;

                if (id == 0) {

                    //اذا كبس على الاكاونت وهو مش مسجل ببعتو على صفحة التسجيل
                    if (firebaseAuth.getUid() != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.contener_main, new ProfileFragment())
                                .commit();

                    } else {

                        startActivity(new Intent(MainActivity.this, IntroActivity.class));
                    }
                } else if (id == 1) {

                    //اذا كبس على الاكاونت وهو مش مسجل ببعتو على صفحة التسجيل
                    if (firebaseAuth.getUid() != null) {
                        navigation_open.setVisibility(View.INVISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.contener_main, new AddDonationStepOne())
                                .commit();
                    } else {

                        startActivity(new Intent(MainActivity.this, IntroActivity.class));
                    }

                } else if (id == 2) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.contener_main, new HomeFragment())
                            .commit();

                } else if (id == 3) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.contener_main, new NonFoodFragment())
                            .commit();
                }
                return null;
            }
        });


    }


    //تغيير نوع الخط
    private void applyFontToMenuItem(MenuItem mi) {

        font = Typeface.createFromAsset(getAssets(), "Omar-Regular-1.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);


    }



    //on Navigation Item Selected
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        drawer_layout.closeDrawers();
        contener_main.setVisibility(View.VISIBLE);
            if (id == R.id.who_us) {
                startActivity(new Intent(MainActivity.this, WhoAreWeActivity.class));
            } else if (id == R.id.contact) {
                startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
            } else if (id == R.id.logout) {
                SharedPreferences.Editor editor = getSharedPreferences("Success", Context.MODE_PRIVATE).edit();

                if(menuItem.getTitle().equals("تسجيل خروج ")) {
                    //اذا عمل لوجاوت معناتو بخزن فولس عشان ما يرجع
                    editor.putBoolean("Success", false);
                    editor.apply();

                }else
                    {
                        editor.putBoolean("Success", true);
                        editor.apply();
                    }

                startActivity(new Intent(MainActivity.this, IntroActivity.class));
                finish();
            } else if (id == R.id.desk) {

                //معناتو بدو بس يشوف السلايد وبرجعو هناك

                flag = true;
                SharedPreferences.Editor editor = getSharedPreferences("Again", Context.MODE_PRIVATE).edit();
                editor.putBoolean("Again", true);
                editor.apply();
                startActivity(new Intent(MainActivity.this, MyIntro.class));

            }else if(id==R.id.MyInfo)
            {
                //اذا كبس على الاكاونت وهو مش مسجل ببعتو على صفحة التسجيل
                if (firebaseAuth.getUid() != null) {
                   startActivity(new Intent(MainActivity.this,UpdateInfoActivity.class));
                } else {

                    startActivity(new Intent(MainActivity.this, IntroActivity.class));
                }
            }


        return true;
    }

    public void applyFont()
    {
        //تغير خط القائمة الجانبية
        Menu m = nav_view.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

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
}
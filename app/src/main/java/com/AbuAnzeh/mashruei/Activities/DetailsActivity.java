package com.AbuAnzeh.mashruei.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.AbuAnzeh.mashruei.Adpter.ViewPagerAdapterImages;
import com.AbuAnzeh.mashruei.Models.ProductModel;
import com.AbuAnzeh.mashruei.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import it.sephiroth.android.library.numberpicker.NumberPicker;

public class DetailsActivity extends AppCompatActivity {


    private ViewPager viewPager;
    private int dotscount;
    private ImageView[] dots;
    private LinearLayout sliderDotspanel;
    private ViewPagerAdapterImages adpterRec;
    private ArrayList<String> images;
    private TextView name, nameProduct, deskProduct, priceProduct;
    private it.sephiroth.android.library.numberpicker.NumberPicker numberPicker;
    private Button add_to_card, makePhone;
    private EditText hint;
    DatabaseReference databaseStores, databaseMyOrder;
    ProductModel productModel;
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        sliderDotspanel = findViewById(R.id.SliderDots);
        hint = findViewById(R.id.hint);
        viewPager = findViewById(R.id.viewPager);
        name = findViewById(R.id.name);
        makePhone = findViewById(R.id.makePhone);
        nameProduct = findViewById(R.id.nameProduct);
        add_to_card = findViewById(R.id.add_to_card);
        deskProduct = findViewById(R.id.deskProduct);
        priceProduct = findViewById(R.id.priceProduct);
        numberPicker = findViewById(R.id.numberPicker);


        SharedPreferences preferencesInfo = getSharedPreferences("InfoUser", Context.MODE_PRIVATE);
        final String userId = preferencesInfo.getString("UserId", "");

        databaseMyOrder = FirebaseDatabase.getInstance().getReference("UserOrder").child(userId);
        String typeStore = "";
        int pos = getIntent().getExtras().getInt("pos");

        SharedPreferences preferences = getSharedPreferences("StoreType", Context.MODE_PRIVATE);
        String StoreType = preferences.getString("StoreType", "");


        SharedPreferences loginOrSign = getSharedPreferences("LoginOrSign", Context.MODE_PRIVATE);
        final boolean successLogin = loginOrSign.getBoolean("success", false);


        images = new ArrayList<String>();

        final String idProduct = getIntent().getExtras().getString("idProduct");

        switch (StoreType) {
            case "حرف يدوية":
                typeStore = "Handicraft";
                break;
            case "حلويات":
                typeStore = "Candy";
                break;
            case "البان و اجبان":
                typeStore = "Dairies";
                break;
            case "ملابس":
                typeStore = "Clothes";
                break;
            case "طعام":
                typeStore = "Food";
                break;
            case "أثاث منزلي":
                typeStore = "HomeFurnishings";
                break;
            case "أخرى":
                typeStore = "Other";
                break;

            default: typeStore = StoreType;
        }


        databaseStores = FirebaseDatabase.getInstance().getReference("Stores").child(typeStore);

        Log.d("idProduct", idProduct);

        databaseStores.keepSynced(true);
        databaseStores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                    databaseStores.child(dataSnapshot1.getKey()).child("Products").child(idProduct).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            productModel = dataSnapshot.getValue(ProductModel.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    databaseStores.child(dataSnapshot1.getKey()).child("Products").child(idProduct).child("ProductImages").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                images.add(dataSnapshot1.getValue(String.class));

                                Log.d("UrlImages", images.size() + "");
                                Log.d("Images", dataSnapshot1.getValue(String.class));


                            }

                            adpterRec = new ViewPagerAdapterImages(DetailsActivity.this, images);
                            viewPager.setAdapter(adpterRec);


                            dotscount = images.size();
                            dots = new ImageView[dotscount];


//                            dots[0].setImageDrawable(ContextCompat.getDrawable(DetailsActivity.this, R.drawable.active_dot));

                            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                }

                                @Override
                                public void onPageSelected(int position) {

                                    for (int i = 0; i < dotscount; i++) {
                                        dots[i].setImageDrawable(ContextCompat.getDrawable(DetailsActivity.this, R.drawable.nonactive_dot));
                                    }

                                    dots[position].setImageDrawable(ContextCompat.getDrawable(DetailsActivity.this, R.drawable.active_dot));

                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });


                            for (int i = 0; i < dotscount; i++) {

                                dots[i] = new ImageView(DetailsActivity.this);
                                dots[i].setImageDrawable(ContextCompat.getDrawable(DetailsActivity.this, R.drawable.nonactive_dot));

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                params.setMargins(8, 0, 8, 0);

                                sliderDotspanel.addView(dots[i], params);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        String nameProductStr = getIntent().getExtras().getString("nameProduct");
        nameProduct.setText(nameProductStr);


        String detailsProductStr = getIntent().getExtras().getString("detailsProduct");
        deskProduct.setText(detailsProductStr);

        String priceProductStr = getIntent().getExtras().getString("priceProduct");
        priceProduct.setText(priceProductStr + " د.أ ");

        String min = getIntent().getExtras().getString("min");
        final String max = getIntent().getExtras().getString("max");
        boolean flag = getIntent().getExtras().getBoolean("flag");

        if (flag) {
            add_to_card.setVisibility(View.VISIBLE);
            numberPicker.setProgress(Integer.parseInt(min));
        } else {
            String Quantity = getIntent().getExtras().getString("Quantity");
            String hintStr = getIntent().getExtras().getString("hint");
            if (hintStr.equals("لا يوجد")) {
                hint.setText("");
            } else {
                hint.setText(hintStr);
            }
            add_to_card.setVisibility(View.GONE);
            numberPicker.setProgress(Integer.parseInt(Quantity));
        }
        final int maxNumber = Integer.parseInt(max);
        final int minNumber = Integer.parseInt(min);
        numberPicker.setMaxValue(maxNumber);
        numberPicker.setMinValue(minNumber);

        numberPicker.setNumberPickerChangeListener(new NumberPicker.OnNumberPickerChangeListener() {
            @Override
            public void onProgressChanged(@NotNull NumberPicker numberPicker, int i, boolean b) {
                if (i == maxNumber) {
                    Toast.makeText(DetailsActivity.this, "لقد وصلت للحد الاعلى من شراء هذا المنتج", Toast.LENGTH_SHORT).show();
                }

                if (i == minNumber) {
                    Toast.makeText(DetailsActivity.this, "لقد وصلت للحد الادنى من شراء هذا المنتج", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onStartTrackingTouch(@NotNull NumberPicker numberPicker) {

            }

            @Override
            public void onStopTrackingTouch(@NotNull NumberPicker numberPicker) {

            }
        });


        SharedPreferences saveIdStore = getSharedPreferences("saveIdStore", Context.MODE_PRIVATE);
        final String phoneStore = saveIdStore.getString("phoneStore", "");


        Log.d("phoneNumber", phoneStore);
        makePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(DetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) DetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                } else {
                    String dial = "tel:" + phoneStore;
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                }
            }
        });


        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        add_to_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (successLogin) {
                    String key = databaseMyOrder.push().getKey();

                    if (TextUtils.isEmpty(hint.getText())) {
                        productModel.setHintProduct("لا يوجد");
                    } else {
                        productModel.setHintProduct(hint.getText().toString());
                    }
                    productModel.setQuantityProduct(numberPicker.getProgress() + "");

                    databaseMyOrder.child(productModel.idProduct).setValue(productModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(DetailsActivity.this, "تم اضافة الى السلة ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DetailsActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    final Dialog dialog = new Dialog(DetailsActivity.this);
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
                            startActivity(new Intent(DetailsActivity.this, SignUpActivity.class));
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                        }
                    });

                    dialog.show();
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}
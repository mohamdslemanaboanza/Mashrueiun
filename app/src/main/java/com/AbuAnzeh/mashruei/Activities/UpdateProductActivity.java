package com.AbuAnzeh.mashruei.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.AbuAnzeh.mashruei.Models.ProductModel;
import com.AbuAnzeh.mashruei.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateProductActivity extends AppCompatActivity {

    private TextView header;


    private TextInputLayout textInputLayoutNameProduct,textInputLayoutPriceProduct,textInputLayoutDeskProduct,textInputLayoutQuantity,textInputLayoutMinQuantity,textInputLayoutMaxQuantity;
    private EditText NameProduct,price,deskProduct,quantityProduct,minQuantity,maxQuantity;
    private DatabaseReference databaseImages,databaseProducts;
    private LinearLayout lnrImages;
    private Button delete,updateProduct;
    private CardView add_images;
    ArrayList ImageList = new ArrayList();
    ArrayList RemovedImageList = new ArrayList();
    private Uri ImageUri;
    private boolean pathOfImages = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);



        ImageList=new ArrayList();
        RemovedImageList=new ArrayList();
        header=findViewById(R.id.header);
        textInputLayoutNameProduct=findViewById(R.id.textInputLayoutNameProduct);
        delete=findViewById(R.id.delete);
        updateProduct=findViewById(R.id.updateProduct);
        textInputLayoutPriceProduct=findViewById(R.id.textInputLayoutPriceProduct);
        textInputLayoutDeskProduct=findViewById(R.id.textInputLayoutDeskProduct);
        textInputLayoutQuantity=findViewById(R.id.textInputLayoutQuantity);
        textInputLayoutMinQuantity=findViewById(R.id.textInputLayoutMinQuantity);
        textInputLayoutMaxQuantity=findViewById(R.id.textInputLayoutMaxQuantity);
        price=findViewById(R.id.price);
        NameProduct=findViewById(R.id.NameProduct);
        deskProduct=findViewById(R.id.deskProduct);
        deskProduct=findViewById(R.id.deskProduct);
        quantityProduct=findViewById(R.id.quantityProduct);
        minQuantity=findViewById(R.id.minQuantity);
        maxQuantity=findViewById(R.id.maxQuantity);
        lnrImages=findViewById(R.id.lnrImages);
        add_images=findViewById(R.id.add_images);

        header.setText("تحديث المنتج");

        add_images.setVisibility(View.GONE);
        add_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "أختر صور المنتج"), 1);


            }
        });

        delete.setText("حذف المنتج");
        updateProduct.setText("تحديث المنتج");

        String id = getIntent().getExtras().getString("id");

        final SharedPreferences preferencesInfo = getSharedPreferences("InfoUser", Context.MODE_PRIVATE);
        String userId = preferencesInfo.getString("UserId", "");


        final SharedPreferences TypeStoreSharedPreferences = getSharedPreferences("TypeStore", Context.MODE_PRIVATE);
        String TypeStoreStr = TypeStoreSharedPreferences.getString("TypeStore", "");

        databaseImages = FirebaseDatabase.getInstance().getReference("Stores").child(TypeStoreStr).child(userId).child("Products").child(id).child("ProductImages");
        databaseProducts = FirebaseDatabase.getInstance().getReference("Stores").child(TypeStoreStr).child(userId).child("Products").child(id);

        databaseImages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String uriImage= dataSnapshot1.getValue(String.class);

                    Log.d("ImgKey",dataSnapshot1.getKey());

                    ImageView productImageView =
                            new ImageView(getApplicationContext());


                    productImageView.setAdjustViewBounds(true);

                    productImageView.setScaleType(ImageView.ScaleType.FIT_XY);

                    productImageView.setLayoutParams(new LinearLayout
                            .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));


                    final View view1 = LayoutInflater.from(UpdateProductActivity.this).inflate(R.layout.img_design,null);
                    ImageView imageView = view1.findViewById(R.id.imageView);
                    ImageView delete = view1.findViewById(R.id.delete);

                    Glide.with(getApplicationContext())
                            .load(R.drawable.ic_baseline_delete_24)
                            .fitCenter().placeholder(R.drawable.logo)
                            .placeholder(R.drawable.logo)
                            .error(R.drawable.logo)
                            .into(delete);


                    lnrImages.addView(view1);

                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            try {

                                lnrImages.removeView(view1);
                                RemovedImageList.add(dataSnapshot1.getKey());


//                               urlStrings.remove(finalPickedImageCount);

                            }catch (Exception e){
                                lnrImages.removeAllViews();
                                ImageList = new ArrayList();
                                Log.e("Error",e.getMessage());
                            }

                        }
                    });

                    Glide.with(getApplicationContext())
                            .load(uriImage)
                            .fitCenter().placeholder(R.drawable.logo)
                            .error(R.drawable.logo)
                            .into(imageView);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        final String name = getIntent().getExtras().getString("name");
        NameProduct.setText(name);

        String desk = getIntent().getExtras().getString("desk");
        deskProduct.setText(desk);

        String priceStr = getIntent().getExtras().getString("price");
        price.setText(priceStr);

        String max = getIntent().getExtras().getString("max");
        maxQuantity.setText(max);

        String min = getIntent().getExtras().getString("min");
        minQuantity.setText(min);

        String quantity = getIntent().getExtras().getString("quantity");
        quantityProduct.setText(quantity);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        updateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(NameProduct.getText())){
                    textInputLayoutNameProduct.setBoxStrokeColor(Color.RED);
                    NameProduct.requestFocus();
                    return;
                }
                else if (TextUtils.isEmpty(price.getText())){
                    textInputLayoutPriceProduct.setBoxStrokeColor(Color.RED);
                    price.requestFocus();
                    return;
                }
                else if (TextUtils.isEmpty(deskProduct.getText())){
                    textInputLayoutDeskProduct.setBoxStrokeColor(Color.RED);
                    deskProduct.requestFocus();
                    return;
                }
                else if (TextUtils.isEmpty(deskProduct.getText())){
                    textInputLayoutDeskProduct.setBoxStrokeColor(Color.RED);
                    deskProduct.requestFocus();
                    return;
                }
                else if (TextUtils.isEmpty(minQuantity.getText())){
                    textInputLayoutMinQuantity.setBoxStrokeColor(Color.RED);
                    maxQuantity.requestFocus();
                    return;
                }
                else if (TextUtils.isEmpty(maxQuantity.getText())) {
                    textInputLayoutMaxQuantity.setBoxStrokeColor(Color.RED);
                    maxQuantity.requestFocus();
                    return;
                }

//                }else if (pathOfImages){
//                    Toast.makeText(UpdateProductActivity.this, "يجب اختيار صورتين على الاقل للمنتج", Toast.LENGTH_SHORT).show();
//                }

                for (int i =0;i<RemovedImageList.size();i++){
                   databaseImages.child(RemovedImageList.get(i).toString()).removeValue();
                }


                HashMap<String , Object> hashMap=new HashMap<>();
                hashMap.put("nameProduct",NameProduct.getText().toString());
                hashMap.put("minQuantity",minQuantity.getText().toString());
                hashMap.put("maxQuantity",maxQuantity.getText().toString());
                hashMap.put("quantityProduct",quantityProduct.getText().toString());
                hashMap.put("deskProduct",deskProduct.getText().toString());
                hashMap.put("priceProduct",price.getText().toString());

                databaseProducts.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(UpdateProductActivity.this, "تم تحديث بيانات المنتج", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }else {
                            Toast.makeText(UpdateProductActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(UpdateProductActivity.this);
                builder.setTitle(" حذف المنتج ");
                builder.setMessage(" ? "+name+" هل أنت متأكد من حذف منتج ");
                builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseProducts.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(UpdateProductActivity.this, "تم حذف المنتج", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }else {
                                    Toast.makeText(UpdateProductActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

                builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });


                Dialog dialog=builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == 1 && resultCode == RESULT_OK
                    && null != data && null != data.getClipData()) {

                final ClipData mClipData = data.getClipData();
//                horizontalScrollView.setVisibility(View.VISIBLE);


                lnrImages.removeAllViews();

                int pickedImageCount;

                for (pickedImageCount = 0; pickedImageCount < mClipData.getItemCount();
                     pickedImageCount++) {

                    ImageUri = data.getClipData().getItemAt(pickedImageCount).getUri();
                    ImageList.add(ImageUri);

                    final ImageView productImageView =
                            new ImageView(getApplicationContext());

                    Log.d("sizeee:",ImageList.size()+"");


                    productImageView.setAdjustViewBounds(true);

                    productImageView.setScaleType(ImageView.ScaleType.FIT_XY);

                    productImageView.setLayoutParams(new LinearLayout
                            .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));


                    final View viewItem = LayoutInflater.from(UpdateProductActivity.this).inflate(R.layout.img_design,null);
                    ImageView imageView = viewItem.findViewById(R.id.imageView);
                    final ImageView deleteg = viewItem.findViewById(R.id.delete);


                    final int finalPickedImageCount = pickedImageCount;

                    deleteg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            try {

                                lnrImages.removeView(viewItem);
                                ImageList.remove(finalPickedImageCount);


//                               urlStrings.remove(finalPickedImageCount);

                            }catch (Exception e){
                                lnrImages.removeAllViews();
                                ImageList = new ArrayList();
                                Log.e("Error",e.getMessage());
                            }

                        }
                    });

                    Glide.with(getApplicationContext())
                            .load(R.drawable.ic_baseline_delete_24)
                            .fitCenter().placeholder(R.drawable.logo)
                            .error(R.drawable.logo)
                            .into(deleteg);


                    pathOfImages = false;
                    lnrImages.addView(viewItem);





                    Glide.with(getApplicationContext())
                            .load(mClipData.getItemAt(pickedImageCount).getUri())
                            .fitCenter().placeholder(R.drawable.logo)
                            .error(R.drawable.logo)
                            .into(imageView);
                }
            } else {
                Toast.makeText(getApplicationContext(), "يجب اختيار اكثر من صورة واحدة",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {

            Log.d("Error",e.getMessage());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
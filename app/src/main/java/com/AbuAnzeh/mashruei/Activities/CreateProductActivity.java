package com.AbuAnzeh.mashruei.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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

import com.AbuAnzeh.mashruei.HelperClass.Constants;
import com.AbuAnzeh.mashruei.Models.Image;
import com.AbuAnzeh.mashruei.Models.ProductModel;
import com.AbuAnzeh.mashruei.Models.custm_item_text;
import com.AbuAnzeh.mashruei.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;


import java.util.ArrayList;
import java.util.HashMap;

public class CreateProductActivity extends AppCompatActivity {

    private TextView header;
    private Button create_product_,delete;
    private CardView add_images;
    private LinearLayout lnrImages;

    private TextInputLayout textInputLayoutNameProduct,textInputLayoutPriceProduct,textInputLayoutDeskProduct,textInputLayoutQuantity,textInputLayoutMinQuantity,textInputLayoutMaxQuantity;
    private EditText NameProduct,price,deskProduct,quantityProduct,minQuantity,maxQuantity;

    private boolean pathOfImages = true;
    private Uri ImageUri;
    ArrayList ImageList = new ArrayList();
    private int upload_count = 0;
    private DatabaseReference databaseStores;
    private StorageTask mUploadTask;
    private StorageReference ImagesProducts;
    ArrayList urlStrings;
private HorizontalScrollView horizontalScrollView;
    private String imgScreen,nameStore,locationStore,imageStore,idStore;
    private com.airbnb.lottie.LottieAnimationView animationView;
    String userId,TypeStoreStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);



        final SharedPreferences preferencesInfo = getSharedPreferences("InfoUser", Context.MODE_PRIVATE);
        userId  = preferencesInfo.getString("UserId", "");

        final SharedPreferences TypeStoreSharedPreferences = getSharedPreferences("TypeStore", Context.MODE_PRIVATE);
         TypeStoreStr = TypeStoreSharedPreferences.getString("TypeStore", "");
        nameStore = TypeStoreSharedPreferences.getString("nameStore", "");
        imageStore = TypeStoreSharedPreferences.getString("imageStore", "");
        locationStore = TypeStoreSharedPreferences.getString("locationStore", "");
        idStore = TypeStoreSharedPreferences.getString("idStore", "");

        databaseStores = FirebaseDatabase.getInstance().getReference("Stores").child(TypeStoreStr).child(userId);
        ImagesProducts = FirebaseStorage.getInstance().getReference().child("ImagesProducts");

        header=findViewById(R.id.header);
        horizontalScrollView=findViewById(R.id.horizontalScrollView);
        animationView=findViewById(R.id.animationView);
        textInputLayoutNameProduct=findViewById(R.id.textInputLayoutNameProduct);
        textInputLayoutPriceProduct=findViewById(R.id.textInputLayoutPriceProduct);
        textInputLayoutDeskProduct=findViewById(R.id.textInputLayoutDeskProduct);
        textInputLayoutQuantity=findViewById(R.id.textInputLayoutQuantity);
        textInputLayoutMinQuantity=findViewById(R.id.textInputLayoutMinQuantity);
        textInputLayoutMaxQuantity=findViewById(R.id.textInputLayoutMaxQuantity);
        price=findViewById(R.id.price);
        NameProduct=findViewById(R.id.NameProduct);
        deskProduct=findViewById(R.id.deskProduct);
        lnrImages=findViewById(R.id.lnrImages);
        deskProduct=findViewById(R.id.deskProduct);
        quantityProduct=findViewById(R.id.quantityProduct);
        minQuantity=findViewById(R.id.minQuantity);
        maxQuantity=findViewById(R.id.maxQuantity);
        create_product_=findViewById(R.id.updateProduct);
        add_images=findViewById(R.id.add_images);
        delete=findViewById(R.id.delete);
        delete.setVisibility(View.GONE);


        urlStrings = new ArrayList<>();

        horizontalScrollView.setVisibility(View.GONE);
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


        create_product_.setOnClickListener(new View.OnClickListener() {
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
                else if (TextUtils.isEmpty(minQuantity.getText())){
                    textInputLayoutMinQuantity.setBoxStrokeColor(Color.RED);
                    maxQuantity.requestFocus();
                    return;
                }
                else if (TextUtils.isEmpty(maxQuantity.getText())){
                    textInputLayoutMaxQuantity.setBoxStrokeColor(Color.RED);
                    maxQuantity.requestFocus();
                    return;
                }else if (pathOfImages){
                    Toast.makeText(CreateProductActivity.this, "يجب اختيار صورتين على الاقل للمنتج", Toast.LENGTH_SHORT).show();
                }


                animationView.setVisibility(View.VISIBLE);


                StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("ImagesProducts");

                for (upload_count = 0; upload_count < ImageList.size(); upload_count++) {

                    Uri IndividualImage = (Uri) ImageList.get(upload_count);
                    final StorageReference ImageName = ImageFolder.child("Images" + IndividualImage.getLastPathSegment());

                    ImageName.putFile(IndividualImage).addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    ImageName.getDownloadUrl().addOnSuccessListener(
                                            new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    urlStrings.add(String.valueOf(uri));



                                                    if (urlStrings.size() == ImageList.size()){
                                                        storeLink(urlStrings);
                                                    }

                                                }
                                            }
                                    );
                                }
                            }
                    );


                }



            }
        });



        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
//            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
//
//            images = data.getParcelableArrayListExtra(Pix.IMAGE_RESULTS);
//            for (int i = 0; i < returnValue.size(); i++) {
//
//
//                Bitmap YourBitmap = BitmapFactory.decodeFile(returnValue.get(i));
//
//                Toast.makeText(this, returnValue.get(i), Toast.LENGTH_SHORT).show();
//
//                ImageView imageView ;
//                View v = getLayoutInflater().inflate(R.layout.img_design, null);
//                imageView = v.findViewById(R.id.imageView);
//                imageView.setImageBitmap(YourBitmap);
//                imageView.setAdjustViewBounds(true);
//
//
//                if (v.getParent() != null) {
//                    ((ViewGroup) v.getParent()).removeView(v);
//                }
//                lnrImages.addView(v);
//            }
//
//
//
//        }
//    }


    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == 1 && resultCode == RESULT_OK
                    && null != data && null != data.getClipData()) {

                final ClipData mClipData = data.getClipData();
                horizontalScrollView.setVisibility(View.VISIBLE);


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


                    final View viewItem = LayoutInflater.from(CreateProductActivity.this).inflate(R.layout.img_design,null);
                    ImageView imageView = viewItem.findViewById(R.id.imageView);
                    final ImageView delete = viewItem.findViewById(R.id.delete);


                    final int finalPickedImageCount = pickedImageCount;
                    delete.setOnClickListener(new View.OnClickListener() {
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
                            .into(delete);

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

    private void storeLink(ArrayList<String> urlStrings) {

        final HashMap<String, String> hashMap = new HashMap<>();

        for (int i = 0; i <urlStrings.size() ; i++) {
            hashMap.put("ImgLink"+i, urlStrings.get(i));
            imgScreen=urlStrings.get(i);
        }





        final String idProduct= databaseStores.push().getKey();



        databaseStores.child("thereAreItems").setValue(true);

        ProductModel product = new ProductModel(idProduct,NameProduct.getText().toString()
                ,price.getText().toString(),deskProduct.getText().toString(),quantityProduct.getText().toString(),
                minQuantity.getText().toString(),maxQuantity.getText().toString(),System.currentTimeMillis() / 1000,0,imgScreen,userId,"",TypeStoreStr);


        databaseStores.child("Products").child(idProduct).setValue(product)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    databaseStores.child("Products").child(idProduct).child("ProductImages").setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                animationView.setVisibility(View.INVISIBLE);

                                                final Dialog dialog = new Dialog(CreateProductActivity.this);
                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                dialog.setContentView(R.layout.dilog_success);
                                                dialog.setCancelable(true);


                                                Window window = dialog.getWindow();
                                                WindowManager.LayoutParams wlp = window.getAttributes();

                                                wlp.gravity = Gravity.CENTER;
                                                wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                                                window.setAttributes(wlp);
                                                dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                                TextView skip = dialog.findViewById(R.id.notNow);
                                                TextView hint = dialog.findViewById(R.id.hint);
                                                Button go = dialog.findViewById(R.id.go);

                                                go.setVisibility(View.GONE);
                                                skip.setText("اغلاق");

                                                skip.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        onBackPressed();
                                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                                    }
                                                });

                                                hint.setText("تهانينا ! \n لقد تم اضافة المنتج المنزلي الى المتجر المنزلي الخاص بك سوف يتم مراجهة المنتج قبل نشره داخل التطبيق \n سوف يتم ارسال اشعار لك في حال تمت الموافقة على المنتج ...أستعد");

                                                dialog.show();
                                            }else {
                                                Toast.makeText(CreateProductActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                animationView.setVisibility(View.INVISIBLE);

                                            }
                                        }
                                    });
                                }
                            }
                        }
                ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                animationView.setVisibility(View.INVISIBLE);
                Toast.makeText(CreateProductActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ImageList.clear();
    }
}
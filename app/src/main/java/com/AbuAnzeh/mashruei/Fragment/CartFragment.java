package com.AbuAnzeh.mashruei.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.AbuAnzeh.mashruei.Activities.DetailsActivity;
import com.AbuAnzeh.mashruei.Activities.LocationPickerActivity;
import com.AbuAnzeh.mashruei.Models.ProductModel;
import com.AbuAnzeh.mashruei.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shivtechs.maplocationpicker.MapUtility;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CartFragment extends Fragment {


    ImageView imageView4;
    TextView textView9;
    RecyclerView myRecyclerViewCart;
    DatabaseReference databaseMyOrder, databaseUsers, databaseOrder, StoresReference;
    ArrayList<ProductModel> modelProductTwos;
    Button confirmPay;
    int sumPriceOfProducts;
    public static double latitude = 0.0;
    public static double longitude = 0.0;
    String fname, lname, phone, city;
    EditText location;
    AdapterCart adapterCart;
    private String token;

    public CartFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        myRecyclerViewCart = view.findViewById(R.id.cartRecyclerView);
        imageView4 = view.findViewById(R.id.imageView4);
        confirmPay = view.findViewById(R.id.confirmPay);
        textView9 = view.findViewById(R.id.textView9);
        myRecyclerViewCart.setLayoutManager(new LinearLayoutManager(getActivity()));

        textView9.setVisibility(View.GONE);
        imageView4.setVisibility(View.GONE);

        MapUtility.apiKey = getResources().getString(R.string.google_api_key);

        SharedPreferences preferencesInfo = getActivity().getSharedPreferences("InfoUser", Context.MODE_PRIVATE);
        final String userId = preferencesInfo.getString("UserId", "");
        modelProductTwos = new ArrayList<>();

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        StoresReference = FirebaseDatabase.getInstance().getReference("Stores");

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                fname = dataSnapshot.child("fname").getValue(String.class);
                lname = dataSnapshot.child("lname").getValue(String.class);
                phone = dataSnapshot.child("phone").getValue(String.class);
                city = dataSnapshot.child("city").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseMyOrder = FirebaseDatabase.getInstance().getReference("UserOrder").child(userId);

        databaseMyOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ProductModel productTwo = dataSnapshot1.getValue(ProductModel.class);
                    modelProductTwos.add(productTwo);

                    sumPriceOfProducts += Integer.parseInt(productTwo.getPriceProduct());
                    adapterCart = new AdapterCart(getActivity(), modelProductTwos);
                    myRecyclerViewCart.setAdapter(adapterCart);


                }
                if (modelProductTwos.size() == 0) {
                    textView9.setVisibility(View.VISIBLE);
                    imageView4.setVisibility(View.VISIBLE);
                    confirmPay.setVisibility(View.GONE);
                }

                confirmPay.setText("تأكيد الشراء مقابل " + sumPriceOfProducts + " د.أ ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        confirmPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.confirm_the_purchase);
                dialog.setCancelable(true);


                TextView notNow = dialog.findViewById(R.id.notNow);
                final EditText name = dialog.findViewById(R.id.name);
                final EditText phoneNumber = dialog.findViewById(R.id.honeNumber);
                final TextInputLayout textInputLayoutFname = dialog.findViewById(R.id.textInputLayoutLname);
                final TextInputLayout textInputLayoutPhoneNumber = dialog.findViewById(R.id.textInputLayoutPhoneNumber);
                final TextInputLayout textInputLayoutLocation = dialog.findViewById(R.id.textInputLayoutLocation);
                Button confirm = dialog.findViewById(R.id.confirm);
                location = dialog.findViewById(R.id.Location);
                LinearLayout choose_location = dialog.findViewById(R.id.choose_location);

                name.setText(fname + " " + lname);
                phoneNumber.setText(phone);

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(name.getText().toString())) {
                            textInputLayoutFname.setBoxStrokeColor(Color.RED);
                            name.requestFocus();
                            return;
                        } else if (TextUtils.isEmpty(phoneNumber.getText())) {
                            textInputLayoutPhoneNumber.setBoxStrokeColor(Color.RED);
                            phoneNumber.requestFocus();
                            return;
                        }else if (TextUtils.isEmpty(location.getText())){
                            textInputLayoutLocation.setBoxStrokeColor(Color.RED);
                            location.requestFocus();
                            return;
                        }


                        databaseOrder = FirebaseDatabase.getInstance().getReference("Order");
                        databaseMyOrder.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    final ProductModel productTwo = dataSnapshot1.getValue(ProductModel.class);
                                    String id = databaseOrder.push().getKey();


                                    productTwo.setNameUser(fname + " " + lname);
                                    productTwo.setPhoneNumber(phone);
                                    productTwo.setLat(latitude);
                                    productTwo.setLon(longitude);
                                    productTwo.setLocation(location.getText().toString());
                                    productTwo.setCity(city);
                                    productTwo.setIdProduct(id);
                                    databaseOrder.child(productTwo.getIdStore()).child(id).setValue(productTwo).addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            confirmPay.setVisibility(View.GONE);
                                            textView9.setVisibility(View.VISIBLE);
                                            imageView4.setVisibility(View.VISIBLE);
                                            Toast.makeText(requireContext(), "تم ارسال الطلب الى المتجر", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(requireContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });

                                    StoresReference.child(productTwo.getTypeStore()).child(productTwo.getIdStore()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            token = dataSnapshot.child("tokenStore").getValue(String.class);
                                            Log.d("token", token);
                                            senPushdNotification(productTwo.getNameProduct(), "طلب جديد من : " + fname + " " + lname, token);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });

                                }


                                DatabaseReference UserOrder = FirebaseDatabase.getInstance().getReference("UserOrder").child(userId);
                                UserOrder.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                        } else {

                                        }
                                    }
                                });

                                myRecyclerViewCart.setAdapter(null);
                                dialog.dismiss();


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                });

                choose_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(requireActivity(), LocationPickerActivity.class));
                    }
                });

                notNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.CENTER;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                window.setAttributes(wlp);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                dialog.show();


            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            location.setText(getAreaInfo(new LatLng(latitude, longitude)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getAreaInfo(LatLng latLng) throws IOException {

        Geocoder gcd = new Geocoder(requireActivity(), Locale.forLanguageTag("ar"));
        List<Address> addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);

        if (addresses.isEmpty()) {
            return "Location UnKnown";
        } else {
            return addresses.get(0).getAddressLine(0);
        }

    }

    public class AdapterCart extends RecyclerView.Adapter<AdapterCart.ImageViewHolder> {
        private Context context;
        private List<ProductModel> mUploads;
        Activity mActivity;
        DatabaseReference databaseMyOrder;

        public AdapterCart(Context context, List<ProductModel> uploadList) {
            this.context = context;
            this.mUploads = uploadList;


            SharedPreferences preferencesInfo = context.getSharedPreferences("InfoUser", Context.MODE_PRIVATE);
            final String userId = preferencesInfo.getString("UserId", "");

            databaseMyOrder = FirebaseDatabase.getInstance().getReference("UserOrder").child(userId);


        }

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


            View view = LayoutInflater.from(context).inflate(R.layout.item_cart, viewGroup, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ImageViewHolder holder, final int i) {
            final ProductModel upload = mUploads.get(i);

            Picasso.get().load(upload.getImageScreen()).placeholder(R.drawable.logo).into(holder.img_product);
            holder.nameProduct.setText(upload.getNameProduct());
            holder.quantityProduct.setText("الكمية : " + upload.getQuantityProduct());
            holder.priceProduct.setText(upload.getPriceProduct() + "د.أ ");


            Log.d("context", context.toString());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("nameProduct", upload.getNameProduct());
                    intent.putExtra("detailsProduct", upload.getDeskProduct());
                    intent.putExtra("priceProduct", upload.getPriceProduct());
                    intent.putExtra("max", upload.getMaxQuantity());
                    intent.putExtra("min", upload.getMinQuantity());
                    intent.putExtra("Quantity", upload.getQuantityProduct());
                    intent.putExtra("hint", upload.getHintProduct());
                    intent.putExtra("flag", false);
                    intent.putExtra("phoneStore", upload.getPhoneNumber());
                    intent.putExtra("idProduct", upload.getIdProduct());

                    context.startActivity(intent);
                }
            });


            holder.nameProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseMyOrder.child(upload.idProduct).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                mUploads.remove(i);
                                if (mUploads.isEmpty()) {
                                    textView9.setVisibility(View.VISIBLE);
                                    imageView4.setVisibility(View.VISIBLE);
                                    confirmPay.setVisibility(View.GONE);
                                }
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });


        }

        @Override
        public int getItemCount() {
            return mUploads.size();
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder {


            public RoundedImageView img_product;
            public TextView nameProduct, quantityProduct, priceProduct, nameStore;

            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                img_product = itemView.findViewById(R.id.img_product);
                nameProduct = itemView.findViewById(R.id.nameProduct);
                quantityProduct = itemView.findViewById(R.id.quantityProduct);
                priceProduct = itemView.findViewById(R.id.priceProduct);
                nameStore = itemView.findViewById(R.id.nameStore);


            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                String address = data.getStringExtra(MapUtility.ADDRESS);
                double selectedLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                double selectedLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);

                latitude = selectedLatitude;
                longitude = selectedLongitude;

                location.setText(address);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void senPushdNotification(final String body, final String title, final String fcmToken) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject notificationJson = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    notificationJson.put("text", body);
                    notificationJson.put("title", title);
                    notificationJson.put("priority", "high");
                    dataJson.put("customId", "02");
                    dataJson.put("badge", 1);
                    dataJson.put("alert", "Alert");
                    json.put("notification", notificationJson);
                    json.put("data", dataJson);
                    json.put("to", fcmToken);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization", "key=AAAA4P5nnkM:APA91bHCkwZrd9-216twryr7_O5KHhaCklG8hmxHcwyucDOaa653uhj79Sa0HF968os1R1jlZBdFvSU9vGAWLTlGbX9bXoFCnXp6TQH7vFEfgnJBVQjZfF7_whGJRdegKhqEpQ_XMY_F")
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();


                    Log.i("TAG", finalResponse);
                } catch (Exception e) {

                    Log.i("TAG", e.getMessage());
                }
                return null;
            }
        }.execute();
    }
}
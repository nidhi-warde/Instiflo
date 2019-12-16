package com.rohg007.android.instiflo.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rohg007.android.instiflo.MainActivity;
import com.rohg007.android.instiflo.R;
import com.rohg007.android.instiflo.models.Product;
import com.rohg007.android.instiflo.models.User;
import com.squareup.picasso.Picasso;

import java.security.acl.Owner;

public class ProductDetails extends AppCompatActivity {

    String title;
    int rentprice;
    int sellprice;
    int noOfUsersrated;
    String ownerId;
    int rentDuration;
    String description;
    int category;
    int units;
    String imageUrl;
    float rating;
    String ownerEmail;
    String ownerAddress;
    String ownerContact;
    float user_rating;
    float final_rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        final LinearLayout detailsView = (LinearLayout)findViewById(R.id.owner_details);
        detailsView.setVisibility(View.GONE);
        final Intent i = getIntent();
        final String productId = i.getStringExtra("productId");
        final String from = i.getStringExtra("from");
        FloatingActionButton floatingActionButton = findViewById(R.id.share);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareInfo = "Look at this product from Instiflow";
                shareInfo += System.getProperty("line.separator");
                shareInfo += imageUrl;
                shareInfo += System.getProperty("line.separator");
                if (category == 3) {
                    shareInfo += "Available for buying at a price of Rs. " + sellprice + "and on rent too at a price of Rs. " + rentprice + "/per day";
                } else if (category == 1) {
                    shareInfo += "Available for buying at a price of Rs. " + sellprice + ".";
                } else if (category == 2) {
                    shareInfo += "Available on rent at a price of Rs. " + rentprice + "/per day";
                }
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareInfo);
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

            }
        });

        Button rateProduct = (Button)findViewById(R.id.rate_product);
        rateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = (LayoutInflater) ProductDetails.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.rate_product,null);
                //instantiate popup window
                final PopupWindow popupWindow = new PopupWindow(customView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                //display the popup window
                ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.constraint_layout);
                popupWindow.showAtLocation(constraintLayout, Gravity.CENTER, 0, 0);
                popupWindow.setBackgroundDrawable(null);
                View container = popupWindow.getContentView().getRootView();
                if(container != null) {
                    WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
                    WindowManager.LayoutParams p = (WindowManager.LayoutParams)container.getLayoutParams();
                    p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                    p.dimAmount = 0.3f;
                    if(wm != null) {
                        wm.updateViewLayout(container, p);
                    }
                }
                //close the popup window on button click
                RatingBar ratingBar = (RatingBar) findViewById(R.id.rate_bar);
                Button rate = (Button) customView.findViewById(R.id.rate_button);
                rate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RatingBar ratingBar = (RatingBar)findViewById(R.id.rating_bar);
                        rating = ratingBar.getRating();
                        RatingBar rateBar = ((RatingBar)popupWindow.getContentView().findViewById(R.id.rate_bar));
                        user_rating=rateBar.getRating();
                        if(user_rating>0)
                        {
                            final_rating = ((rating * noOfUsersrated) + user_rating)/(noOfUsersrated + 1);
                            noOfUsersrated++;
                        }
                        else
                        {
                            final_rating = rating;
                        }
                        try {

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            ref.child("products").child(productId).child("productRating").setValue(final_rating);
                            ref.child("products").child(productId).child("noOfUsersRated").setValue(noOfUsersrated);
                            ratingBar.setRating(final_rating);
                            TextView ratingView = (TextView)findViewById(R.id.product_detail_rating);
                            ratingView.setText(String.format("%.1f", final_rating));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        popupWindow.dismiss();
                    }
                });
            }});
        final Button vendorDetailsButton = (Button)findViewById(R.id.details);
        vendorDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(detailsView.getVisibility()==View.GONE)
                {
                    vendorDetailsButton.setText("Hide Vendor Details");
                    detailsView.setVisibility(View.VISIBLE);
                    TextView addressView= (TextView)findViewById(R.id.address);
                    TextView emailView= (TextView) findViewById(R.id.mail);
                    TextView phoneview= (TextView)findViewById(R.id.contact);
                    addressView.setText(ownerAddress);
                    emailView.setText(ownerEmail);
                    phoneview.setText(ownerContact);

                    addressView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) { seeMap(); }});

                    emailView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) { sendmail();}});

                    phoneview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) { makeCall();}});
                }
                else {
                    detailsView.setVisibility(View.GONE);
                    vendorDetailsButton.setText("Vendor Details");
                }
            }
        });
        Button chatButton = (Button)findViewById(R.id.chat);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { doChat();}});

        ImageButton chat = (ImageButton) findViewById(R.id.chat_btn);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) { doChat(); }});
        ImageButton call = (ImageButton) findViewById(R.id.call_btn);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { makeCall(); }});
        ImageButton mail = (ImageButton) findViewById(R.id.mail_btn);
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { sendmail(); }});
        ImageButton map = (ImageButton) findViewById(R.id.map_btn);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { seeMap(); }});

        FirebaseDatabase.getInstance().getReference().child("products").child(productId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Product product = dataSnapshot.getValue(Product.class);
                        title = product.getProductTitle();
                        rentprice = product.getProductRentPrice();
                        sellprice = product.getProductSellPrice();
                        noOfUsersrated = product.getNoOfUsersRated();
                        ownerId = product.getOwnerId();
                        rentDuration = product.getRentDuration();
                        description = product.getProductDescription();
                        category = product.getProductCategory();
                        units = product.getProductCount();
                        imageUrl = product.getProductImageUrl();
                        rating = product.getProductRating();

                        ImageView imageView=(ImageView)findViewById(R.id.expanded_image_product);
                        TextView titleView = (TextView)findViewById(R.id.product_detail_title);
                        final TextView ownerView = (TextView)findViewById(R.id.vendor_name);
                        TextView ratingView = (TextView)findViewById(R.id.product_detail_rating);
                        TextView descriptionview = (TextView)findViewById(R.id.product_detail_description);
                        TextView priceView = (TextView)findViewById(R.id.product_detail_price);
                        RatingBar rateBar = (RatingBar)findViewById(R.id.rating_bar);
                        TextView durationView = (TextView)findViewById(R.id.rent_duration);
                        Picasso.get()
                                .load(imageUrl)
                                .into(imageView);
                        titleView.setText(title);
                        rateBar.setRating(rating);
                        ratingView.setText(String.format("%.1f",rating));
                       /*rateBar.setVisibility(View.GONE);
                       ratingView.setVisibility(View.GONE);*/
                        if(from.equals("buyFragment"))
                        {
                            priceView.setText("Rs. "+sellprice);
                            if(category==3)
                            {
                                description+= System.getProperty("line.separator");
                                description+= "Available on rent too at a price of Rs. " + rentprice + "/Day.";
                                description+= System.getProperty("line.separator");
                                if(rentDuration==1)
                                    description+= "Rent Duration: "+rentDuration+" day";
                                else
                                    description+= "Rent Duration: "+rentDuration+" days";

                            }
                        }
                        else
                        {
                            priceView.setText("Rs. "+rentprice +"/Day" );
                            if(category==3)
                            {
                                description+= System.getProperty("line.separator");
                                description+= "Available for buying too at a price of Rs. " + sellprice ;
                                description+= System.getProperty("line.separator");
                                if(rentDuration==1)
                                    description+= "Rent Duration: "+rentDuration+" day";
                                else
                                    description+= "Rent Duration: "+rentDuration+" days";
                            }
                            else
                            {
                                description+= System.getProperty("line.separator");
                                if(rentDuration==1)
                                    description+= "Rent Duration: "+rentDuration+" day";
                                else
                                    description+= "Rent Duration: "+rentDuration+" days";
                            }
                        }
                        descriptionview.setText(description);

                        /*if(rentDuration<=1)
                            durationView.setText("Rent Duration: "+rentDuration+" day");
                        else
                            durationView.setText("Rent Duration: "+rentDuration+" days");
                        if(category == 3 || category ==2)
                        {

                            durationView.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            durationView.setVisibility(View.GONE);
                        }*/
                        durationView.setVisibility(View.GONE);
                        FirebaseDatabase.getInstance().getReference().child("users").child(ownerId)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        User user = dataSnapshot.getValue(User.class);
                                        ownerAddress=user.getAddress();
                                        ownerEmail=user.getEmail();
                                        ownerContact=user.getPhoneNumber();
                                        ownerView.setText("Vendor Name: "+user.getFirstName()+" "+user.getLastName());
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    void sendmail()
    {
        try {
            Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + ownerEmail));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Getting information about your product on Instiflow.");
            intent.putExtra(Intent.EXTRA_TEXT, "hello." + System.getProperty("line.separator")+"Interested in buying you product " + title + System.getProperty("line.separator") + imageUrl + System.getProperty("line.separator") + "Kindle send some more information about this product :-)" + System.getProperty("line.separator")+"Thank You.");
            startActivity(intent);
        } catch(Exception e) {
            Toast.makeText(ProductDetails.this, "Sorry...You don't have any mail app", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    void seeMap()
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://maps.google.co.in/maps?q=" + ownerAddress));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    void doChat()
    {
        try {
            ownerContact = ownerContact.replace(" ", "").replace("+", "");
            ownerContact="91"+ownerContact;
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(ownerContact)+"@s.whatsapp.net");

            startActivity(Intent.createChooser(sendIntent, "Compartir en")
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        } catch(Exception e) {
            Log.e("WS", "ERROR_OPEN_MESSANGER"+e.toString());
        }
    }
    void makeCall()
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:91"+ownerContact));
        startActivity(intent);
    }
}

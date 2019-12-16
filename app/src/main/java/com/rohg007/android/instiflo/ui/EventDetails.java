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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rohg007.android.instiflo.MainActivity;
import com.rohg007.android.instiflo.R;
import com.rohg007.android.instiflo.models.Event;
import com.rohg007.android.instiflo.models.Product;
import com.rohg007.android.instiflo.models.User;
import com.squareup.picasso.Picasso;

import java.security.acl.Owner;

public class EventDetails extends AppCompatActivity {

    String title;
    String firstName;
    String lastName;
    String ownerId;
    String description;
    String imageUrl;
    int pplGoing;
    String date;
    String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_details);
        final Intent i = getIntent();
        final String eventId = i.getStringExtra("eventId");

        FirebaseDatabase.getInstance().getReference().child("events").child(eventId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Event event = dataSnapshot.getValue(Event.class);
                        if(event.getEventTitle() != null)
                            title = event.getEventTitle();
                        description = event.getEventDescription();
                        pplGoing = event.getCount();
                        imageUrl = event.getImageId();
                        ownerId = event.getEventOwner();
                        date = event.getEventDate();
                        time = event.getEventTime();
                        ImageView imageView=findViewById(R.id.expanded_image_event);
                        TextView titleView = findViewById(R.id.event_detail_title);
                        TextView organiserView = findViewById(R.id.organiser_name);
                        TextView pplGoingView = findViewById(R.id.people_going);
                        TextView dateView = findViewById(R.id.event_date);
                        TextView timeView = findViewById(R.id.event_time);
                        TextView descriptionview = findViewById(R.id.event_detail_description);
                        if (imageUrl.isEmpty()) {
                            imageView.setImageResource(R.drawable.instiflo_branding);
                        } else{
                            Picasso.get().load(imageUrl).into(imageView);
                        }
                        titleView.setText(title);
                        //organiserView.setText(organiser);
                        if(pplGoing == 0)
                            pplGoingView.setText("People attending: 0");
                        else
                            pplGoingView.setText("People attending: "+pplGoing);
                        dateView.setText("Date: "+date);
                        timeView.setText("Time: "+time);
                        descriptionview.setText(description);
                        FirebaseDatabase.getInstance().getReference().child("users").child(ownerId)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                firstName = user.getFirstName();
                                lastName = user.getLastName();
                                organiserView.setText("Organiser: "+firstName+" "+lastName);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
}

package com.rohg007.android.instiflo.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rohg007.android.instiflo.R;
import com.rohg007.android.instiflo.adapters.MyEventsAdapter;
import com.rohg007.android.instiflo.adapters.MyProductsAdapter;
import com.rohg007.android.instiflo.models.Event;
import com.rohg007.android.instiflo.models.Product;

import java.util.ArrayList;

public class MyEvents extends AppCompatActivity {
    private ArrayList<Event> lstItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        Toolbar toolbar = findViewById(R.id.toolbar_my_events);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        lstItems= new ArrayList<>();
        String current_uid = user.getUid(); // user.getUid() will return null if you are not logged in
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        Query query = db.child("events").orderByChild("eventOwner").equalTo(current_uid);
        final RecyclerView recyclerView = findViewById(R.id.event_recycler_view);
        query.addListenerForSingleValueEvent(new ValueEventListener(){

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                Event event = ds.getValue(Event.class);
                lstItems.add(event);
            }
            if(lstItems.size()==0) {
                Toast.makeText(MyEvents.this,"Opps! You have no events to show.",Toast.LENGTH_SHORT).show();
            }
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(new MyEventsAdapter(lstItems,MyEvents.this));
            recyclerView.setLayoutManager(new LinearLayoutManager(MyEvents.this));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            // do something
        }
    });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

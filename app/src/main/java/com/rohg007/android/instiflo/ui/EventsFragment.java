package com.rohg007.android.instiflo.ui;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ramotion.foldingcell.FoldingCell;
import com.rohg007.android.instiflo.R;
import com.rohg007.android.instiflo.adapters.EventsAdapter;
import com.rohg007.android.instiflo.models.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    private ArrayList<Event> mEventList=new ArrayList<Event>();
    EventsAdapter adapter;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();


    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            ((FoldingCell) v).toggle(false);
            adapter.registerToggle(viewHolder.getAdapterPosition());


        }
    };

    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_events, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.event_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new EventsAdapter(mEventList);
        recyclerView.setAdapter(adapter);

        databaseReference.child("events").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Event event=dataSnapshot.getValue(Event.class);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
                String date=sdf.format(new Date());

                SimpleDateFormat sdf2=new SimpleDateFormat("hh:mm");
                String time=sdf2.format(new Date());

                Date d1,d2;

                try {
                    d1=sdf.parse(event.getEventDate());
                    d2=sdf.parse(date);

                    if(d1.after(d2)||d1.equals(d2))
                    {
                        mEventList.add(event);
                        adapter.notifyDataSetChanged();
                    }
                }

                catch (Exception e)
                {
                    mEventList.add(event);
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error in retrieving some data,please try again", Toast.LENGTH_SHORT).show();
            }
        });


        adapter.setOnItemClickListener(onItemClickListener);

        final FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.event_add));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy>0) {
                    fab.hide();
                }
                else if(dy<0) {
                    fab.show();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddEvent.class);
                startActivity(intent);
            }
        });

        return v;
    }

}
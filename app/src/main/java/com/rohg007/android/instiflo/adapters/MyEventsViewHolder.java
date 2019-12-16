package com.rohg007.android.instiflo.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rohg007.android.instiflo.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyEventsViewHolder extends RecyclerView.ViewHolder {

    RelativeLayout parentLayout;
    TextView titleTextView;
    TextView dateTextView;
    TextView timeTextView;
    ImageView eventImageView;
    TextView peopleGoingView;

    public MyEventsViewHolder(@NonNull final View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.my_events_title);
        dateTextView = itemView.findViewById(R.id.my_events_title_date);
        timeTextView = itemView.findViewById(R.id.my_events_title_time);
        eventImageView = itemView.findViewById(R.id.event_title_image);
        parentLayout = itemView.findViewById(R.id.my_event_id);
        peopleGoingView = itemView.findViewById(R.id.count_people_going);

    }
}

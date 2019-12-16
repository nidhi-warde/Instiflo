package com.rohg007.android.instiflo.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ramotion.foldingcell.FoldingCell;
import com.rohg007.android.instiflo.R;
import com.rohg007.android.instiflo.models.Event;
import com.rohg007.android.instiflo.utils.ImageRequester;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder> {

    private ArrayList<Event> eventsList;
    private View.OnClickListener onItemClickListener;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private ImageRequester imageRequester;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    public EventsAdapter(ArrayList<Event> eventsList){
        this.eventsList = eventsList;
        imageRequester=ImageRequester.getInstance();
    }

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_cell,parent,false);
        return new EventsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventsViewHolder holder, int position) {
        if(eventsList!=null && position<eventsList.size()){
            final Event event = eventsList.get(position);

            holder.eventTitleTitleView.setText(event.getEventTitle());
            holder.eventDateTitleView.setText(event.getEventDate());
            holder.eventTimeTitleView.setText(event.getEventTime());
            holder.eventTitleContentView.setText(event.getEventTitle());
            holder.eventDateContentView.setText(event.getEventDate());
            holder.eventTimeContentView.setText(event.getEventTime());
            holder.eventLocationContentView.setText(event.getEventLocation());
            holder.eventDescriptionContentView.setText(event.getEventDescription());
            imageRequester.setImageFromUrl(holder.eventImage,event.getImageId());
            imageRequester.setImageFromUrl(holder.eventImage2,event.getImageId());
//            Picasso.get().load(event.getImageId()).into(holder.eventImage);
//            Picasso.get().load(event.getImageId()).into(holder.eventImage2);

            holder.going_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    event.increment_going();
                    String id=event.getEventId();
                    databaseReference.child("events").child(id).setValue(event);

                    try{

                        Calendar cal=Calendar.getInstance();

                        String date=event.getEventDate();

                        String d=date.substring(0,2);
                        String m=date.substring(3,5);
                        String y=date.substring(6);

                        String time=event.getEventTime();

                        String h=time.substring(0,2);
                        String min=time.substring(3,5);
                        String s="00";

                        cal.set(Integer.parseInt(y),Integer.parseInt(m)-1,Integer.parseInt(d),Integer.parseInt(h),Integer.parseInt(min),Integer.parseInt(s));

                        Intent calIntent = new Intent(Intent.ACTION_INSERT);
                        calIntent.setData(CalendarContract.Events.CONTENT_URI);

                        calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis());
                        calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTimeInMillis()+60*60*1000);

                        calIntent.putExtra(CalendarContract.Events.TITLE, event.getEventTitle());
                        calIntent.putExtra(CalendarContract.Events.DESCRIPTION, event.getEventDescription());
                        calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, event.getEventLocation());

                        view.getContext().startActivity(calIntent);
                    }

                    catch (Exception e)
                    {
                        ;
                    }

                    Toast.makeText(view.getContext(), "Remainder for the event saved!!", Toast.LENGTH_SHORT).show();

                    holder.going_button.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener){
        onItemClickListener=itemClickListener;
    }

    public class EventsViewHolder extends RecyclerView.ViewHolder{

        TextView eventTitleTitleView;
        TextView eventDateTitleView;
        TextView eventTimeTitleView;

        TextView eventTitleContentView;
        TextView eventDateContentView;
        TextView eventTimeContentView;
        TextView eventLocationContentView;
        TextView eventDescriptionContentView;

        RelativeLayout relativeLayout;

        NetworkImageView eventImage,eventImage2;

        MaterialButton going_button;

        public EventsViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitleTitleView = itemView.findViewById(R.id.event_title_title);
            eventDateTitleView = itemView.findViewById(R.id.event_title_date);
            eventTimeTitleView = itemView.findViewById(R.id.event_title_time);
            eventTitleContentView = itemView.findViewById(R.id.event_content_title);
            eventDateContentView = itemView.findViewById(R.id.event_content_date);
            eventTimeContentView = itemView.findViewById(R.id.event_content_time);
            eventLocationContentView = itemView.findViewById(R.id.event_content_location);
            eventDescriptionContentView = itemView.findViewById(R.id.event_content_description);
            relativeLayout=itemView.findViewById(R.id.detail_relative);
            eventImage=itemView.findViewById(R.id.event_title_image);
            eventImage2=itemView.findViewById(R.id.event_content_image);
            going_button=itemView.findViewById(R.id.event_content_going_button);


            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
        }
    }
}
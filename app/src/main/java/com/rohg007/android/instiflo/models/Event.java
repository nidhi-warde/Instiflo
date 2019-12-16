package com.rohg007.android.instiflo.models;

import java.util.ArrayList;

public class Event {

    private String eventTitle;
    private String eventDate;
    private String eventTime;
    private String eventLocation;
    private String eventDescription;
    private String eventOwner;
    private String eventId;
    private String imageId;
    private Integer count;

    public Event() {
        this.eventTitle = "";
        this.eventDate = "";
        this.eventTime = "";
        this.eventLocation = "";
        this.eventDescription = "";
        this.eventOwner="";
        this.eventId="";
        this.imageId="";
        this.count=0;
    }

    public Event(String eventOwner,String eventId,String eventTitle, String eventDate, String eventTime, String eventLocation, String eventDescription,String imageId) {
        this.eventTitle = eventTitle;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventLocation = eventLocation;
        this.eventDescription = eventDescription;
        this.eventId=eventId;
        this.eventOwner=eventOwner;
        this.imageId=imageId;
        this.count=0;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) { this.eventDescription = eventDescription; }

    public void setEventOwner(String eventOwner)
    {
        this.eventOwner=eventOwner;
    }

    public String getEventOwner()
    {
        return this.eventOwner;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Integer getCount() { return count; }

    public void increment_going(){count++;}

    //    public static ArrayList<Event> getTestEvents(){
//        ArrayList<Event> eventArrayList = new ArrayList<>();
//
//        eventArrayList.add(new Event("FC Barcelona v Real Madrid C.F.","22/10/2019","8:00","Camp Nou",
//                "TextView display text on android app. by default,TextView show text on a single line, and if it is long then TextView take more lines to display its text.android developers can generate a new line on TextView both programmatically and syntactically.android developers can make a multiline TextView without splitting text to multiline by android:minLines attribute."));
//
//        eventArrayList.add(new Event("Manchester Utd. v Liverpool","23/10/2019","9:00 PM",
//                "Anfield","TextView display text on android app. by default,TextView show text on a single line, and if it is long then TextView take more lines to display its text.android developers can generate a new line on TextView both programmatically and syntactically.android developers can make a multiline TextView without splitting text to multiline by android:minLines attribute."));
//
//        eventArrayList.add(new Event("Juventus v Inter", "24/10/2019","10:00 PM",
//                "San Siro","TextView display text on android app. by default,TextView show text on a single line, and if it is long then TextView take more lines to display its text.android developers can generate a new line on TextView both programmatically and syntactically.android developers can make a multiline TextView without splitting text to multiline by android:minLines attribute."));
//
//        eventArrayList.add(new Event("Bayern Munich v Dortmund","25/10/2019","11:00 PM",
//                "Allianz Arena","TextView display text on android app. by default,TextView show text on a single line, and if it is long then TextView take more lines to display its text.android developers can generate a new line on TextView both programmatically and syntactically.android developers can make a multiline TextView without splitting text to multiline by android:minLines attribute."));
//
//        return eventArrayList;
//    }
}
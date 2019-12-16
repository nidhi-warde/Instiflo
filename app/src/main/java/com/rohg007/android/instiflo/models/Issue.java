package com.rohg007.android.instiflo.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Issue implements Parcelable {
    private String LostFound;
    private String mName;
    private String mContactNumber;
    private String mObjectType;
    private String mDescription;
    private String mEmail;
    private String mId;
    private String mImageUrl;
    private String mVisibililty;
    private String mResolved;
    private String mApproved;
    private String datePosted;

    //Empty because defining a custom constructor
    public Issue() {
        LostFound="Lost";
        mName="";
        mContactNumber="";
        mObjectType="";
        mDescription="";
        mEmail="";
        mId="";
        mImageUrl="";
        mVisibililty="";
        mResolved="";
        mApproved="";
        datePosted="";
    }

    public Issue(String Id, String lostFound, String name, String contactNumber, String objectType, String description, String email, String imageUrl, String visibility,String resolved,String approved, String datePosted) {
        this.mId = Id;
        this.LostFound = lostFound;
        this.mName = name;
        this.mContactNumber = contactNumber;
        this.mObjectType = objectType;
        this.mDescription = description;
        this.mEmail = email;
        this.mImageUrl = imageUrl;
        this.mVisibililty=visibility;
        this.mResolved=resolved;
        this.setmApproved(approved);
        this.datePosted=datePosted;
    }


    protected Issue(Parcel in) {
        LostFound = in.readString();
        mName = in.readString();
        mContactNumber = in.readString();
        mObjectType = in.readString();
        mDescription = in.readString();
        mEmail = in.readString();
        mId = in.readString();
        mImageUrl = in.readString();
        mVisibililty=in.readString();
        mResolved=in.readString();
        setmApproved(in.readString());
        datePosted = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(LostFound);
        dest.writeString(mName);
        dest.writeString(mContactNumber);
        dest.writeString(mObjectType);
        dest.writeString(mDescription);
        dest.writeString(mEmail);
        dest.writeString(mId);
        dest.writeString(mImageUrl);
        dest.writeString(mVisibililty);
        dest.writeString(mResolved);
        dest.writeString(getmApproved());
        dest.writeString(datePosted);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Issue> CREATOR = new Creator<Issue>() {
        @Override
        public Issue createFromParcel(Parcel in) {
            return new Issue(in);
        }

        @Override
        public Issue[] newArray(int size) {
            return new Issue[size];
        }
    };

    /*
    Encapsulating all fields
     */

    public String getLostFound() {
        return LostFound;
    }

    public void setLostFound(String lostFound) {
        LostFound = lostFound;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmContactNumber() {
        return mContactNumber;
    }

    public void setmContactNumber(String mContactNumber) {
        this.mContactNumber = mContactNumber;
    }

    public String getmObjectType() {
        return mObjectType;
    }

    public void setmObjectType(String mObjectType) {
        this.mObjectType = mObjectType;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }


    public String getmVisibililty() {
        return mVisibililty;
    }

    public void setmVisibililty(String mVisibililty) {
        this.mVisibililty = mVisibililty;
    }

    public String getmResolved() {
        return mResolved;
    }

    public void setmResolved(String mResolved) {
        this.mResolved = mResolved;
    }

    public String getmApproved() {
        return mApproved;
    }

    public void setmApproved(String mApproved) {
        this.mApproved = mApproved;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }
}

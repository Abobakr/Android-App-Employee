package com.example.employee;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
	
	public int id;
    public String name;
    public String surname;

    public Review(Parcel source) {
    	id = source.readInt();
    	name = source.readString();
    	surname = source.readString();
    }

    public Review(int ID, String Name, String Surname) { 
        this.id = ID;
        this.name = Name;
        this.surname = Surname;
    }
    
    @Override
	public int describeContents() {
    	return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(surname);
	}
	public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
		
		public Review createFromParcel(Parcel in) {
			return new Review(in);
		}

		public Review[] newArray(int size) {
			return new Review[size];
		}
	};
}
package com.hanibalg.yeneservice;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.applandeo.materialcalendarview.model.EventDay;

import java.util.Calendar;

public class MyEventDay extends EventDay implements Parcelable {
    private String mNote;
    private int color;

    public MyEventDay(Parcel day) {
        super((Calendar)day.readSerializable(),day.readInt());
    }

    public MyEventDay(Calendar day, int imageResource,String note,int colors) {
        super(day, imageResource);
        mNote = note;
        color = colors;
    }
    public static final Creator<MyEventDay> CREATOR = new Creator<MyEventDay>() {
        @Override
        public MyEventDay createFromParcel(Parcel source) {
            return new MyEventDay(source);
        }

        @Override
        public MyEventDay[] newArray(int size) {
            return new MyEventDay[size];
        }
    };

    public String getmNote() {
        return mNote;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(getCalendar());
//        dest.writeInt(getImageResource());
        dest.writeString(mNote);
        dest.writeInt(color);
    }
}

package com.example.android.booklisting;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

/**
 * Created by Kamil on 2018-01-15.
 */

public class Book implements Parcelable {

    private String mTitle;
    private String mAuthor;
    private String mThumbnail;


    public Book(String title, String author, String thumbnail) {
        mTitle = title;
        mAuthor = author;
        mThumbnail = thumbnail;


    }

    protected Book(Parcel in) {
        mAuthor = in.readString();
        mTitle = in.readString();
        mThumbnail = in.readString();



    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }


    @Override
    public int describeContents() {
        return 0;
    }




    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAuthor);
        dest.writeString(mTitle);
        dest.writeString(mThumbnail);

    }
}

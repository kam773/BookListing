package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Kamil on 2018-01-15.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private String mUrl;
    private static final String LOG_TAG = BookLoader.class.getName();

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<Book> books = null;
        try {
            books = QueryUtils.fetchBookData(mUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }
}

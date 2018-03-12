package com.example.android.booklisting;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Kamil on 2018-01-15.
 */

public class BookAdapter extends ArrayAdapter<Book> {


    public BookAdapter(Activity context, ArrayList<Book> books) {
        super(context,0,books);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Book currentBook = getItem(position);

        TextView title = (TextView) listItemView.findViewById(R.id.item_title);
        title.setText(currentBook.getTitle());

        TextView author = (TextView) listItemView.findViewById(R.id.item_author);
        author.setText(currentBook.getAuthor());

        ImageView thumbnail = (ImageView) listItemView.findViewById(R.id.cover_image);
        Picasso.with(getContext())
                .load(currentBook.getmThumbnail())
                .into(thumbnail);

        return listItemView;
    }


}

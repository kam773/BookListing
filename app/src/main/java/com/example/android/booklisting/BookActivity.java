package com.example.android.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;



import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Book>> {

    private BookAdapter mAdapter;

    private SearchView mSearchView;
    private Button mSearchButton;

    private String url = "";

    private String userInput;

    private static final int BOOK_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setIconified(true);

        mSearchButton = (Button) findViewById(R.id.search_button);


        final ListView bookListView = (ListView) findViewById(R.id.listView);

        final View emptyView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(emptyView);

        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        bookListView.setAdapter(mAdapter);


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {

            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

        }

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userInput = mSearchView.getQuery().toString();

                if(userInput.contains(" ")) {
                    userInput.trim().replaceAll(" ","+");
                }

                StringBuilder sb = new StringBuilder();
                sb.append("https://www.googleapis.com/books/v1/volumes?q=")
                        .append(userInput);

                url = sb.toString();

                restartLoader();



            }
        });



    }

    private String updateQueryUrl(String userInput) {

        userInput = mSearchView.getQuery().toString();

        if(userInput.contains(" ")) {
            userInput.replaceAll(" ","+");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("https://www.googleapis.com/books/v1/volumes?q=")
                .append(userInput);

        url = sb.toString();
        return url;
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {

        updateQueryUrl(mSearchView.getQuery().toString());

        return new BookLoader(this, url);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mAdapter.clear();

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);

        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mAdapter.clear();
    }

    public void restartLoader() {
        getLoaderManager().restartLoader(BOOK_LOADER_ID, null, BookActivity.this);
    }

}

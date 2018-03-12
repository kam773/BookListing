package com.example.android.booklisting;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Kamil on 2018-01-15.
 */

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public QueryUtils() {

    }

    public static List<Book> fetchBookData(String requestUrl) throws JSONException {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpsRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Book> books = extractFromJSONResponse(jsonResponse);

        return books;
    }

    private static URL createUrl(String stringUrl) {

        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }

        return url;
    }

    private static String makeHttpsRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {

            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /*milliseconds*/);
            urlConnection.setConnectTimeout(15000 /*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving Book JSON results.", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader;
            inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

    public static String formatListOfAuthors(JSONArray authorsList) throws JSONException {

        String authorsListInString = null;

        if (authorsList.length() == 0) {
            return null;
        }

        for (int i = 0; i < authorsList.length(); i++){
            if (i == 0) {
                authorsListInString = authorsList.getString(0);
            } else {
                authorsListInString += ", " + authorsList.getString(i);
            }
        }

        return authorsListInString;
    }


    private static List<Book> extractFromJSONResponse(String JSONResponse) throws JSONException {
        if (TextUtils.isEmpty(JSONResponse)) {
            return null;
        }

        List<Book> books = new ArrayList<>();


            try {

                JSONObject response = new JSONObject(JSONResponse);

                JSONArray bookArray = response.getJSONArray("items");
                for (int i = 0; i < bookArray.length(); i++) {

                    JSONObject currentBook = (JSONObject) bookArray.get(i);

                    JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                    String title = volumeInfo.getString("title");

                    JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                    String authors = formatListOfAuthors(authorsArray);

                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");

                    String thumbnail = imageLinks.getString("smallThumbnail");

                    StringBuilder stringBuilder = new StringBuilder();

                    Pattern p = Pattern.compile("id=(.*?)&");
                    Matcher m = p.matcher(thumbnail);
                    if (m.matches()) {
                        String id = m.group(1);
                        thumbnail = String.valueOf(stringBuilder.append("https://books.google.com/books/content/images/frontcover/").append(id).append("?fife=w300"));
                    } else {
                        Log.i(LOG_TAG, "Issue with cover");
                    }


                    Book book = new Book(title, authors, thumbnail);

                    books.add(book);

                }

            } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return books;
    }

}
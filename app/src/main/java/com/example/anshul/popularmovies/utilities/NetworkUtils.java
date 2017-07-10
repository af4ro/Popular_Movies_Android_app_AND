package com.example.anshul.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private final static String BASE_MAIN_URL = "http://api.themoviedb.org/3";

    private final static String BASE_IMAGE_URL = "http://image.tmdb.org/t/p";

    private final static String QUERY_MAIN_KEY_PARAM = "api_key";

    private final static String QUERY_IMAGE_SIZE = "/w342";
    private final static String QUERY_IMAGE_SIZE2 = "/w780";

    //Builds the URL using class variables and parameters
    public static URL buildUrl(String sortingCategory, String api_key) {

        Uri builtUri = Uri.parse(BASE_MAIN_URL + sortingCategory).buildUpon()
                .appendQueryParameter(QUERY_MAIN_KEY_PARAM, api_key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v("BASE_URL", "Built URI " + url);

        return url;
    }

    //Builds the image url
    public static URL buildImageUrl(boolean isPoster, String imageExtension) {

        Uri builtUri;
        if(isPoster){
            builtUri = Uri.parse(BASE_IMAGE_URL + QUERY_IMAGE_SIZE + imageExtension);
        }
        else{
            builtUri = Uri.parse(BASE_IMAGE_URL + QUERY_IMAGE_SIZE2 + imageExtension);
        }
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v("BASE_URL", "Built URI " + url);

        return url;
    }

    //Gets the JSON response from the url
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
